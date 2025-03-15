package com.example.videojuegoslista;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 segundos de retardo para el splash screen
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Mostrar Splash Screen con retardo
        new Handler().postDelayed(this::checkUserStatus, SPLASH_DELAY);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Saludar al usuario autenticado si existe
        greetAuthenticatedUser();
    }

    /**
     * Verifica el estado del usuario y redirige a la actividad correspondiente.
     */
    private void checkUserStatus() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Redirigir al Dashboard si el usuario está autenticado
            navigateTo(DashboardActivity.class);
        } else {
            // Redirigir al Login si no hay usuario autenticado
            navigateTo(LoginActivity.class);
        }
    }

    /**
     * Saluda al usuario autenticado con un Toast.
     */
    private void greetAuthenticatedUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            String welcomeMessage = email != null ? "Bienvenido de nuevo, " + email : "Bienvenido de nuevo.";
            Toast.makeText(this, welcomeMessage, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Navega a la actividad especificada.
     *
     * @param targetActivity Clase de la actividad a la que se quiere navegar.
     */
    private void navigateTo(Class<?> targetActivity) {
        Intent intent = new Intent(MainActivity.this, targetActivity);
        startActivity(intent);
        finish(); // Finalizar MainActivity para evitar volver atrás
    }
}
