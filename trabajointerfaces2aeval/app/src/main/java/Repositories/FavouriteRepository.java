package Repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Models.Game;
import Views.FavouriteItem;

public class FavouriteRepository {

    private final DatabaseReference usersRef;

    public FavouriteRepository() {
        this.usersRef = FirebaseDatabase.getInstance().getReference("users");
    }

    public interface AddFavouriteCallback {
        void onResult(boolean success);
    }

    public interface RemoveFavouriteCallback {
        void onResult(boolean success);
    }

    /**
     * Obtiene la lista de favoritos en tiempo real desde Firebase.
     */
    public LiveData<List<FavouriteItem>> getFavourites(String userId) {
        MutableLiveData<List<FavouriteItem>> liveData = new MutableLiveData<>();
        DatabaseReference favRef = usersRef.child(userId).child("favoritos");

        favRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<FavouriteItem> favouritesList = new ArrayList<>();
                for (DataSnapshot favSnapshot : snapshot.getChildren()) {
                    String gameId = favSnapshot.getKey();
                    // Hacer una segunda consulta para obtener los detalles del videojuego
                    DatabaseReference gameRef = FirebaseDatabase.getInstance().getReference("videojuegos").child(gameId);
                    gameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot gameSnapshot) {
                            Game game = gameSnapshot.getValue(Game.class);
                            if (game != null) {
                                favouritesList.add(new FavouriteItem(game.getId(), game));
                            }
                            // Si hemos añadido todos los juegos favoritos, actualizamos el LiveData
                            if (favouritesList.size() == snapshot.getChildrenCount()) {
                                liveData.setValue(favouritesList);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Log.e("FavouriteRepository", "Error al obtener los detalles del juego: " + error.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("FavouriteRepository", "Error al obtener favoritos: " + error.getMessage());
            }
        });

        return liveData;
    }

    /**
     * Agrega un juego a la lista de favoritos en Firebase.
     */
    public void addFavourite(String userId, Game favouriteItem, AddFavouriteCallback callback) {
        if (favouriteItem == null) {
            callback.onResult(false);
            return;
        }

        DatabaseReference favRef = usersRef.child(userId).child("favoritos").child(favouriteItem.getId());
        favRef.setValue(true).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onResult(true);
            } else {
                Log.e("FavouriteRepository", "Error al agregar favorito: " + task.getException());
                callback.onResult(false);
            }
        });
    }

    /**
     * Elimina un juego de la lista de favoritos en Firebase.
     */
    public void removeFavourite(String userId, String itemId, RemoveFavouriteCallback callback) {
        if (itemId == null || itemId.trim().isEmpty()) {
            callback.onResult(false);
            return;
        }

        DatabaseReference favRef = usersRef.child(userId).child("favoritos").child(itemId);
        favRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onResult(true);
            } else {
                Log.e("FavouriteRepository", "Error al eliminar favorito: " + task.getException());
                callback.onResult(false);
            }
        });
    }
}
