package Repositories;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import Models.Game;

public class DashboardRepository {

    private final DatabaseReference gamesRef;

    // Constructor para facilitar la inyección de dependencias o pruebas unitarias
    public DashboardRepository() {
        this.gamesRef = FirebaseDatabase.getInstance().getReference("games");
    }

    /**
     * Interfaz de callback para manejar los resultados de la carga de juegos.
     */
    public interface GamesCallback {
        void onSuccess(List<Game> games);
        void onFailure(String error);
    }

    /**
     * Obtiene la lista de juegos desde Firebase y usa el callback para devolver los resultados.
     *
     * @param callback Interfaz para manejar éxito o fallo en la obtención de datos.
     */
    public void fetchGames(GamesCallback callback) {
        gamesRef.get().addOnSuccessListener(snapshot -> {
            List<Game> games = new ArrayList<>();

            if (snapshot.exists()) {
                for (DataSnapshot gameSnapshot : snapshot.getChildren()) {
                    Game game = gameSnapshot.getValue(Game.class);
                    if (game != null) {
                        // Eliminamos el setId, ya que no lo necesitamos.
                        games.add(game);
                    }
                }
                callback.onSuccess(games);  // Devuelve la lista de juegos al ViewModel
            } else {
                callback.onFailure("No se encontraron juegos disponibles.");
            }

        }).addOnFailureListener(e -> {
            Log.e("DashboardRepository", "Error al obtener los juegos: " + e.getMessage(), e);
            callback.onFailure(e.getMessage() != null ? e.getMessage() : "Error al obtener los juegos. Por favor intente nuevamente.");
        });
    }
}
