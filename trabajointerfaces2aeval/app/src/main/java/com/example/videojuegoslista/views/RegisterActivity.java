package com.example.videojuegoslista.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.videojuegoslista.R;
import com.example.videojuegoslista.databinding.ActivityRegistroBinding;
import com.example.videojuegoslista.viewmodels.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegistroBinding binding;
    private RegisterViewModel viewModel;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registro);

        inicializarViews();
        configurarViewModel();
        configurarObservadores();
        configurarListeners();
    }

    private void inicializarViews() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registrando usuario...");
        progressDialog.setCancelable(false);
    }

    private void configurarViewModel() {
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
    }

    private void configurarObservadores() {
        viewModel.obtenerResultadoRegistro().observe(this, resultado -> {
            if ("SUCCESS".equals(resultado)) {
                Toast.makeText(this, "Usuario creado con éxito", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, DashboardActivity.class));
                finish();
            }
        });

        viewModel.obtenerErrorValidacion().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.obtenerEstadoCarga().observe(this, estaCargando -> {
            if (estaCargando != null && estaCargando) {
                progressDialog.show();
            } else {
                progressDialog.dismiss();
            }
        });
    }

    private void configurarListeners() {
        binding.btnRgistrar.setOnClickListener(v -> registrarUsuario());

        binding.lblLoginR.setOnClickListener(v ->
                startActivity(new Intent(RegisterActivity.this, MainActivity.class)));
    }

    private void registrarUsuario() {
        String nombre = binding.etNombre.getText().toString();
        String apellido = binding.etApellido.getText().toString();
        String correo = binding.etCorreo.getText().toString();
        String contrasena = binding.etContrasena.getText().toString();
        String confirmContrasena = binding.etConfirmContrasena.getText().toString();

        viewModel.registrarUsuario(nombre, apellido, correo, contrasena, confirmContrasena);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}