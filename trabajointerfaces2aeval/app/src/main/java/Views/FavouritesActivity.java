package Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videojuegoslista.R;

import java.util.List;

import Models.Game;
import Viewmodels.FavouriteViewModel;
import Adapters1.GameAdapter; // Asegúrate de que la ruta es correcta

public class FavouritesActivity extends AppCompatActivity implements GameAdapter.OnGameClickListener {

    private RecyclerView favouritesRecyclerView;
    private GameAdapter gameAdapter;
    private FavouriteViewModel favouriteViewModel;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        // Vincular las vistas
        favouritesRecyclerView = findViewById(R.id.recyclerViewFavourites);
        progressBar = findViewById(R.id.progressBar);

        // Inicializar el ViewModel
        favouriteViewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);

        // Configurar el RecyclerView
        favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        gameAdapter = new GameAdapter(this, this);
        favouritesRecyclerView.setAdapter(gameAdapter);

        // Observar cambios en la lista de favoritos
        favouriteViewModel.getFavouriteGames().observe(this, this::updateUI);

        // Observar el estado de carga
        favouriteViewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Observar errores
        favouriteViewModel.getErrorMessage().observe(this, this::showError);
    }

    @Override
    protected void onResume() {
        super.onResume();
        favouriteViewModel.loadFavouriteGames(); // Cargar favoritos al volver a la pantalla
    }

    @Override
    public void onGameClick(Game game) {
        if (game != null) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("GAME", game);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Juego no disponible", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(List<Game> games) {
        // Corrección: Se obtiene la lista de juegos desde el ViewModel, evitando llamar a un método inexistente en el adaptador
        List<Game> currentGames = favouriteViewModel.getFavouriteGames().getValue();

        if (games == null || games.isEmpty()) {
            favouritesRecyclerView.setVisibility(View.GONE);
        } else {
            if (currentGames == null || !currentGames.equals(games)) { // Solo actualiza si hay cambios
                gameAdapter.setGames(games);
            }
            favouritesRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void showError(String error) {
        if (error != null && !error.isEmpty()) {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }
    }
}
