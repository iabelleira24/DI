package com.example.videojuegoslista.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.videojuegoslista.R;
import com.example.videojuegoslista.databinding.ActivityLoginBinding;
import com.example.videojuegoslista.viewmodels.LoginViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        applySavedTheme();

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        inicializarViews();
        configurarViewModel();
        configurarObservadores();
        configurarListeners();
    }

    private void inicializarViews() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.setCancelable(false);
    }

    private void configurarViewModel() {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    private void configurarObservadores() {
        loginViewModel.getLoginResult().observe(this, usuarioFirebase -> {
            progressDialog.dismiss();
            if (usuarioFirebase != null) {
                // Obtiene una instancia de SharedPreferences
                SharedPreferences sharedPref = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
                // Obtiene el editor para modificar las preferencias compartidas
                SharedPreferences.Editor editor = sharedPref.edit();
                // Guarda el usuario, usando el UID del usuario actualmente autenticado en Firebase
                editor.putString("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                // Aplica los cambios
                editor.apply();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });


        loginViewModel.getLoginError().observe(this, error -> {
            progressDialog.dismiss();
            if (error != null) {
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        loginViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null && isLoading) {
                progressDialog.show();
            } else {
                progressDialog.dismiss();
            }
        });
    }

    private void configurarListeners() {
        binding.btnIngresarL.setOnClickListener(v -> intentarLogin());

        binding.lblRegistrar.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void intentarLogin() {
        String correo = binding.etCorreoL.getText().toString().trim();
        String contrasena = binding.etContrasenaL.getText().toString().trim();
        loginViewModel.login(correo, contrasena);
    }

    private void applySavedTheme() {
        SharedPreferences sharedPref = getSharedPreferences("AppConfig", MODE_PRIVATE);
        boolean darkMode = sharedPref.getBoolean("darkMode", false);

        AppCompatDelegate.setDefaultNightMode(darkMode ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
