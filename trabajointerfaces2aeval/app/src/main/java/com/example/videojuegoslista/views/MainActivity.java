package com.example.videojuegoslista.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.videojuegoslista.R;
import com.example.videojuegoslista.databinding.ActivityMainBinding;
import com.example.videojuegoslista.viewmodels.LoginViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private LoginViewModel loginViewModel;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Aplicar el tema guardado al inicio
        applySavedTheme();

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

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
                startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                finish();
            }
        });

        loginViewModel.getLoginError().observe(this, error -> {
            progressDialog.dismiss();
            if (error != null) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(MainActivity.this, RegisterActivity.class)));
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
