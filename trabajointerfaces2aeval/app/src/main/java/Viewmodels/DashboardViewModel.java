package Viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Models.Game;

public class DashboardViewModel extends ViewModel {

    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("games");
    private ValueEventListener gameListener;

    private final MutableLiveData<List<Game>> _gameList = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<Game>> getGameList() {
        return _gameList;
    }

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> getIsLoading() {
        return _isLoading;
    }

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public LiveData<String> getErrorMessage() {
        return _errorMessage;
    }

    // Variables para la paginación
    private String lastKey = null;

    /**
     * Carga los datos de los juegos desde Firebase Realtime Database sin actualizaciones en tiempo real.
     */
    public void loadGames() {
        if (Boolean.TRUE.equals(_isLoading.getValue())) return;  // Prevenir llamadas duplicadas

        _isLoading.setValue(true);
        lastKey = null; // Reiniciar clave para evitar conflictos con paginación

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Game> games = new ArrayList<>();
                for (DataSnapshot gameSnapshot : snapshot.getChildren()) {
                    try {
                        Game game = gameSnapshot.getValue(Game.class);
                        if (game != null) {
                            games.add(game);
                        }
                    } catch (Exception e) {
                        _errorMessage.setValue("Error al convertir datos: " + e.getMessage());
                    }
                }
                _gameList.setValue(games);
                _isLoading.setValue(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                _errorMessage.setValue("Error al cargar juegos: " + error.getMessage());
                _isLoading.setValue(false);
            }
        });
    }

    /**
     * Carga juegos en bloques con paginación.
     *
     * @param limitTo Número máximo de juegos a cargar.
     */
    public void loadGamesWithLimit(int limitTo) {
        if (Boolean.TRUE.equals(_isLoading.getValue())) return;

        _isLoading.setValue(true);

        Query query;
        if (lastKey == null) {
            query = mDatabase.limitToFirst(limitTo);
        } else {
            query = mDatabase.orderByKey().startAfter(lastKey).limitToFirst(limitTo);
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Game> games = new ArrayList<>();
                for (DataSnapshot gameSnapshot : snapshot.getChildren()) {
                    try {
                        Game game = gameSnapshot.getValue(Game.class);
                        if (game != null) {
                            games.add(game);
                        }
                    } catch (Exception e) {
                        _errorMessage.setValue("Error al convertir datos: " + e.getMessage());
                    }
                }

                if (!games.isEmpty()) {
                    lastKey = games.get(games.size() - 1).getId(); // Guardar la clave del último juego

                    // Agregar nuevos juegos a la lista en lugar de sobrescribir
                    List<Game> currentGames = _gameList.getValue();
                    if (currentGames == null) currentGames = new ArrayList<>();
                    currentGames.addAll(games);
                    _gameList.setValue(currentGames);
                }

                _isLoading.setValue(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                _errorMessage.setValue("Error al cargar juegos: " + error.getMessage());
                _isLoading.setValue(false);
            }
        });
    }

    /**
     * Filtra los juegos por género con paginación.
     *
     * @param genre   Género a filtrar.
     * @param limitTo Número máximo de juegos a cargar.
     */
    public void filterGamesByGenre(String genre, int limitTo) {
        if (Boolean.TRUE.equals(_isLoading.getValue())) return;

        _isLoading.setValue(true);
        lastKey = null; // Reiniciar la clave de paginación al cambiar de filtro

        Query query = mDatabase.orderByChild("genre").equalTo(genre).limitToFirst(limitTo);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Game> filteredGames = new ArrayList<>();
                for (DataSnapshot gameSnapshot : snapshot.getChildren()) {
                    try {
                        Game game = gameSnapshot.getValue(Game.class);
                        if (game != null) {
                            filteredGames.add(game);
                        }
                    } catch (Exception e) {
                        _errorMessage.setValue("Error al convertir datos: " + e.getMessage());
                    }
                }
                if (!filteredGames.isEmpty()) {
                    lastKey = filteredGames.get(filteredGames.size() - 1).getId();

                    // Agregar nuevos juegos filtrados a la lista en lugar de sobrescribir
                    List<Game> currentGames = _gameList.getValue();
                    if (currentGames == null) currentGames = new ArrayList<>();
                    currentGames.addAll(filteredGames);
                    _gameList.setValue(currentGames);
                }

                _isLoading.setValue(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                _errorMessage.setValue("Error al filtrar juegos: " + error.getMessage());
                _isLoading.setValue(false);
            }
        });
    }

    /**
     * Elimina el listener para evitar memory leaks cuando el ViewModel se destruye.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        if (gameListener != null) {
            mDatabase.removeEventListener(gameListener);
        }
    }
}
