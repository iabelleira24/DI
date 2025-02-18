package Repositories;

import android.util.Log;
import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Models.Game;

public class DashboardRepository {

    private final DatabaseReference gamesRef;
    private final DatabaseReference usersRef;
    private final FirebaseUser currentUser;

    public DashboardRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.gamesRef = database.getReference("games");
        this.usersRef = database.getReference("users");
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();  // Obtener usuario autenticado
    }

    /**
     * Interfaz de callback para manejar los resultados de la carga de juegos.
     */
    public interface GamesCallback {
        void onSuccess(List<Game> games);
        void onFailure(String error);
    }

    /**
     * Obtiene la lista de juegos desde Firebase y marca los favoritos según el usuario actual.
     *
     * @param callback Interfaz para manejar éxito o fallo en la obtención de datos.
     */
    public void fetchGames(GamesCallback callback) {
        if (currentUser == null) {
            callback.onFailure("Usuario no autenticado.");
            return;
        }

        // Consultamos ambos nodos en paralelo
        Task<DataSnapshot> gamesTask = gamesRef.get();
        Task<DataSnapshot> favTask = usersRef.child(currentUser.getUid()).child("favoritos").get();

        // Usamos Task.whenAll() para esperar a que ambas tareas terminen
        Tasks.whenAll(gamesTask, favTask).addOnCompleteListener(task -> {
            if (gamesTask.isSuccessful() && favTask.isSuccessful()) {
                DataSnapshot gamesSnapshot = gamesTask.getResult();
                DataSnapshot favSnapshot = favTask.getResult();

                List<Game> games = new ArrayList<>();
                Set<String> favoritos = new HashSet<>();

                // Procesamos los favoritos
                if (favSnapshot.exists()) {
                    for (DataSnapshot fav : favSnapshot.getChildren()) {
                        String favId = fav.getValue(String.class);
                        if (favId != null) {
                            favoritos.add(favId);
                        }
                    }
                }

                // Procesamos la lista de juegos
                if (gamesSnapshot.exists()) {
                    for (DataSnapshot gameSnapshot : gamesSnapshot.getChildren()) {
                        Game game = gameSnapshot.getValue(Game.class);
                        String gameId = gameSnapshot.getKey();

                        if (game != null && gameId != null) {
                            game.setId(gameId);
                            game.setFavorite(favoritos.contains(gameId)); // Marcar si es favorito
                            games.add(game);
                        }
                    }
                }

                callback.onSuccess(games);
            } else {
                // Manejar errores de cualquier tarea
                String errorMsg = "Error al obtener datos: ";
                if (!gamesTask.isSuccessful()) {
                    errorMsg += gamesTask.getException() != null ? gamesTask.getException().getMessage() : "Desconocido";
                }
                if (!favTask.isSuccessful()) {
                    errorMsg += favTask.getException() != null ? favTask.getException().getMessage() : "Desconocido";
                }
                callback.onFailure(errorMsg);
            }
        });
    }
}