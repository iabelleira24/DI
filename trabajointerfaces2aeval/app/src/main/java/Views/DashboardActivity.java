package Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videojuegoslista.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import Adapters1.GameAdapter;
import Models.Game;
import Viewmodels.DashboardViewModel;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private GameAdapter gameAdapter;
    private ProgressBar progressBar;
    private Button logoutButton;
    private DashboardViewModel dashboardViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Vincular elementos del layout
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        logoutButton = findViewById(R.id.logoutButton);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gameAdapter = new GameAdapter(new ArrayList<>(), game -> {
            Toast.makeText(DashboardActivity.this, "Clic en: " + game.getTitle(), Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(gameAdapter);

        // Verificar si el usuario está autenticado
        if (mAuth.getCurrentUser() == null) {
            redirectToLogin();
        } else {
            setupViewModel();
        }

        // Configurar botón de Logout
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(DashboardActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            redirectToLogin();
        });
    }

    /**
     * Configura el ViewModel y observa los cambios en los datos de los juegos.
     */
    private void setupViewModel() {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        dashboardViewModel.getGameList().observe(this, games -> {
            gameAdapter.setGames(games);
        });

        dashboardViewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        dashboardViewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(DashboardActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        // Cargar los juegos al iniciar la actividad
        dashboardViewModel.loadGames();
    }

    /**
     * Redirige al usuario a la pantalla de inicio de sesión.
     */
    private void redirectToLogin() {
        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
