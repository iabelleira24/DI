package com.example.videojuegoslista;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso; // Importación de Picasso
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView titleTextView, descriptionTextView;
    private ImageView gameImageView;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Inicializar FirebaseAuth y DatabaseReference
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("games");

        // Vincular elementos del layout
        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        gameImageView = findViewById(R.id.gameImageView);
        logoutButton = findViewById(R.id.logoutButton);

        // Verificar si el usuario está autenticado
        if (mAuth.getCurrentUser() == null) {
            // Si no está autenticado, redirigir a LoginActivity
            redirectToLogin();
        } else {
            // Cargar datos del juego desde Firebase
            loadGameData();
        }

        // Configurar botón de Logout
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut(); // Cerrar sesión en FirebaseAuth
            Toast.makeText(DashboardActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            redirectToLogin();
        });
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

    /**
     * Carga los datos de un juego desde Firebase Realtime Database.
     */
    private void loadGameData() {
        final String gameId = "game1"; // ID del juego a mostrar (ejemplo)
        mDatabase.child(gameId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Extraer datos del juego
                    String title = snapshot.child("title").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);
                    String imageUrl = snapshot.child("image").getValue(String.class);

                    // Validar datos antes de usarlos
                    if (title != null && description != null) {
                        // Actualizar la interfaz con el título y la descripción
                        titleTextView.setText(title);
                        descriptionTextView.setText(description);

                        // Si la URL de la imagen está disponible, cargarla con Picasso
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Picasso.get().load(imageUrl).into(gameImageView);
                        }
                    } else {
                        Toast.makeText(DashboardActivity.this, "Datos incompletos del juego", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DashboardActivity.this, "Juego no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DashboardActivity", "Error al cargar datos", error.toException());
                Toast.makeText(DashboardActivity.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
