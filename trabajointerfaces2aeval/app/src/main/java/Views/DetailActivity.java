package Views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import com.example.videojuegoslista.R;
import Models.Game;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DetailActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView gameImageView;
    private Button backButton;
    private FloatingActionButton favButton;

    private FirebaseDatabase database;
    private DatabaseReference favRef;
    private String userId;
    private boolean isFavorite = false;

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        titleTextView = findViewById(R.id.gameTitle);
        descriptionTextView = findViewById(R.id.gameDescription);
        gameImageView = findViewById(R.id.gameImage);
        backButton = findViewById(R.id.backButton);
        favButton = findViewById(R.id.favButton);

        database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            userId = auth.getCurrentUser().getUid();
            favRef = database.getReference("usuarios").child(userId).child("favoritos");
        } else {
            Toast.makeText(this, "Usuario no autenticado. Redirigiendo al inicio de sesión.", Toast.LENGTH_SHORT).show();
            redirectToLogin();
            return;
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("GAME")) {
            game = intent.getParcelableExtra("GAME");
        }

        if (game != null) {
            titleTextView.setText(game.getTitle() != null && !game.getTitle().isEmpty() ? game.getTitle() : "Título no disponible");
            descriptionTextView.setText(game.getDescription() != null && !game.getDescription().isEmpty() ? game.getDescription() : "Descripción no disponible");

            Picasso.get().load(game.getImage() != null && !game.getImage().isEmpty() ? game.getImage() : "")
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(gameImageView);

            checkIfFavorite();
        } else {
            titleTextView.setText("Datos del juego no disponibles");
            descriptionTextView.setText("Información no disponible.");
            favButton.setEnabled(false);
        }

        backButton.setOnClickListener(v -> {
            Intent intentBack = new Intent(DetailActivity.this, DashboardActivity.class);
            intentBack.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentBack);
            finish();
        });

        favButton.setOnClickListener(v -> toggleFavorite());
    }

    private void checkIfFavorite() {
        if (game == null) return;

        favRef.child(game.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    isFavorite = true;
                    favButton.setImageResource(R.drawable.corazon_lleno);
                } else {
                    isFavorite = false;
                    favButton.setImageResource(R.drawable.corazon_vacio);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DetailActivity.this, "Error al verificar favoritos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleFavorite() {
        if (game == null) return;

        favButton.setEnabled(false);

        favRef.child(game.getId()).setValue(isFavorite ? null : true)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        isFavorite = !isFavorite;
                        favButton.setImageResource(isFavorite ? R.drawable.corazon_lleno : R.drawable.corazon_vacio);
                        String message = isFavorite ? "Agregado a favoritos" : "Eliminado de favoritos";
                        Toast.makeText(DetailActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailActivity.this, "Error al actualizar favoritos", Toast.LENGTH_SHORT).show();
                    }
                    favButton.setEnabled(true);
                });
    }

    private void redirectToLogin() {
        if (!isFinishing()) {
            Intent intent = new Intent(DetailActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}
