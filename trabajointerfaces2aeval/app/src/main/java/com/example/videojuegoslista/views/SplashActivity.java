package com.example.videojuegoslista.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Obtiene una instancia de SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        // Recupera el valor asociado a la clave "userId". Si no existe, devuelve null.
        String userId = sharedPref.getString("userId", null);

        // Verifica si el "userId" es diferente de null.
        if (userId != null) {
            // Usuario autenticado previamente, redirigir a MainActivity
            startActivity(new Intent(this, MainActivity.class));
            // Finaliza la actividad actual.
            finish();
        }
        else {
            // Si el "userId" es null, redirige a la pantalla de LoginActivity
            super.onCreate(savedInstanceState);
            // Redirigir a LoginActivity
            startActivity(new Intent(this, LoginActivity.class));
            // Finaliza la actividad actual.
            finish();
        }
    }
}