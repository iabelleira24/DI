package Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videojuegoslista.R;
import com.google.firebase.auth.FirebaseAuth;

import Adapters1.GameAdapter;
import Models.Game;
import Viewmodels.DashboardViewModel;

public class DashboardActivity extends AppCompatActivity implements GameAdapter.OnGameClickListener {

    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private GameAdapter gameAdapter;
    private ProgressBar progressBar;
    private Button logoutButton, themeButton, favoritesButton;
    private DashboardViewModel dashboardViewModel;
    private SharedPreferences sharedPref;
    private String lastErrorMessage = ""; // Para evitar mostrar errores duplicados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializar preferencias
        sharedPref = getSharedPreferences("AppConfig", MODE_PRIVATE);


        setContentView(R.layout.activity_dashboard);
// Vincular elementos del layout
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        logoutButton = findViewById(R.id.logoutButton);
        themeButton = findViewById(R.id.themeButton);
        favoritesButton = findViewById(R.id.favoritesButton);

        // Configurar tema basado en preferencias
        setupTheme();
        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();



        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gameAdapter = new GameAdapter(this, this);
        recyclerView.setAdapter(gameAdapter);

        // Verificar autenticación del usuario
        if (mAuth.getCurrentUser() == null) {
            redirectToLogin();
        } else {
            setupViewModel();
        }

        // Configurar botones
        logoutButton.setOnClickListener(v -> logoutUser());
        themeButton.setOnClickListener(v -> toggleTheme());
        favoritesButton.setOnClickListener(v -> openFavourites());
    }

    /**
     * Configura el ViewModel y observa los cambios en los datos de los juegos.
     */
    private void setupViewModel() {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        dashboardViewModel.getGameList().observe(this, games -> {
            if (games != null && !games.isEmpty()) {
                gameAdapter.setGames(games);
            } else {
                showToast(getString(R.string.no_games_found));
            }
        });

        dashboardViewModel.getIsLoading().observe(this, isLoading -> {
            boolean loading = Boolean.TRUE.equals(isLoading);
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            logoutButton.setEnabled(!loading);
            recyclerView.setEnabled(!loading);
        });

        dashboardViewModel.getErrorMessage().observe(this, this::showError);

        dashboardViewModel.loadGames();
    }

    @Override
    public void onGameClick(Game game) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("game_id", game.getId());
        startActivity(intent);
    }

    /**
     * Cambia el modo oscuro/claro y guarda la preferencia del usuario.
     */
    private void toggleTheme() {
        boolean isDarkMode = sharedPref.getBoolean("darkMode", false);
        sharedPref.edit().putBoolean("darkMode", !isDarkMode).apply();

        // Alternar entre modo claro y oscuro
        AppCompatDelegate.setDefaultNightMode(isDarkMode ?
                AppCompatDelegate.MODE_NIGHT_NO :
                AppCompatDelegate.MODE_NIGHT_YES);

        // Actualizar el texto del botón
        themeButton.setText(isDarkMode ? "Modo Oscuro" : "Modo Claro");
    }

    /**
     * Configura el tema inicial según las preferencias guardadas.
     */
    private void setupTheme() {
        boolean darkMode = sharedPref.getBoolean("darkMode", false);
        AppCompatDelegate.setDefaultNightMode(darkMode ?
                AppCompatDelegate.MODE_NIGHT_YES :
                AppCompatDelegate.MODE_NIGHT_NO);

        // Cambiar el texto del botón dependiendo del modo actual
        themeButton.setText(darkMode ? "Modo Claro" : "Modo Oscuro");
    }

    /**
     * Cierra sesión y redirige al usuario a la pantalla de inicio de sesión.
     */
    private void logoutUser() {
        mAuth.signOut();
        showToast(getString(R.string.logout_success));
        redirectToLogin();
    }

    /**
     * Abre la actividad de favoritos.
     */
    private void openFavourites() {
        startActivity(new Intent(this, FavouritesActivity.class));
    }

    /**
     * Muestra un mensaje de error si es nuevo (evita repeticiones).
     */
    private void showError(String error) {
        if (error != null && !error.isEmpty() && !error.equals(lastErrorMessage)) {
            showToast(error);
            lastErrorMessage = error;
        }
    }

    /**
     * Muestra un Toast con un mensaje dado.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Redirige al usuario a la pantalla de inicio de sesión.
     */
    private void redirectToLogin() {
        if (!isFinishing()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}
