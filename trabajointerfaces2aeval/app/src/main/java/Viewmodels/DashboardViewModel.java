package Viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Models.Game;

public class DashboardViewModel extends ViewModel {

    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("games");

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

    /**
     * Carga los datos de los juegos desde Firebase Realtime Database.
     */
    public void loadGames() {
        _isLoading.setValue(true);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Game> games = new ArrayList<>();
                for (DataSnapshot gameSnapshot : snapshot.getChildren()) {
                    Game game = gameSnapshot.getValue(Game.class);
                    if (game != null) {
                        games.add(game);
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
}
