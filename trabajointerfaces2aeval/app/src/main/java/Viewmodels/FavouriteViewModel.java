package Viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Models.Game;

public class FavouriteViewModel extends ViewModel {

    private final MutableLiveData<List<Game>> favouriteGames = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private final FirebaseDatabase database;
    private final FirebaseAuth auth;
    private final DatabaseReference gamesRef; // Se asume que la ruta correcta es "games"

    public FavouriteViewModel() {
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        gamesRef = database.getReference("games"); // Asegúrate de que en Firebase los juegos estén en "games"
    }

    public LiveData<List<Game>> getFavouriteGames() {
        return favouriteGames;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadFavouriteGames() {
        if (Boolean.TRUE.equals(isLoading.getValue())) return;

        isLoading.setValue(true);
        String userId = (auth.getCurrentUser() != null) ? auth.getCurrentUser().getUid() : null;
        if (userId == null) {
            errorMessage.setValue("Usuario no autenticado.");
            isLoading.setValue(false);
            return;
        }

        DatabaseReference favouritesRef = database.getReference("users").child(userId).child("favoritos");

        favouritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> favoritosIds = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String gameId = childSnapshot.getValue(String.class);
                    if (gameId != null && !gameId.trim().isEmpty()) {
                        favoritosIds.add(gameId);
                    }
                }
                if (favoritosIds.isEmpty()) {
                    favouriteGames.setValue(new ArrayList<>());
                    isLoading.setValue(false);
                } else {
                    fetchGames(favoritosIds);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                errorMessage.setValue("Error al obtener los favoritos: " + error.getMessage());
                isLoading.setValue(false);
            }
        });
    }

    private void fetchGames(List<String> gameIds) {
        List<Game> allGames = new ArrayList<>();
        final int totalGames = gameIds.size();
        final int[] completedRequests = {0};

        for (String gameId : gameIds) {
            gamesRef.child(gameId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Game game = snapshot.getValue(Game.class);
                    if (game != null) {
                        game.setId(gameId);
                        allGames.add(game);
                    }
                    completedRequests[0]++;
                    if (completedRequests[0] == totalGames) {
                        favouriteGames.setValue(allGames);
                        isLoading.setValue(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    errorMessage.setValue("Error al obtener los juegos: " + error.getMessage());
                    isLoading.setValue(false);
                }
            });
        }
    }
}
