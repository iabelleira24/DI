package Views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.videojuegoslista.R;

import Viewmodels.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {

    private RegisterViewModel registerViewModel;
    private EditText fullNameEditText, emailEditText, passwordEditText, confirmPasswordEditText, phoneEditText, addressEditText;
    private Button registerButton, backToLoginButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar el ViewModel
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        // Enlazar vistas
        fullNameEditText = findViewById(R.id.fullNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        registerButton = findViewById(R.id.registerButton);
        backToLoginButton = findViewById(R.id.backToLoginButton);
        progressBar = findViewById(R.id.progressBar);

        // Configurar los botones
        registerButton.setOnClickListener(v -> {
            registerButton.setEnabled(false); // Evita múltiples clics
            registerUser();
        });

        backToLoginButton.setOnClickListener(v -> navigateToLogin());

        // Observar el resultado del registro
        registerViewModel.registrationResult.observe(this, result -> {
            progressBar.setVisibility(View.GONE);
            registerButton.setEnabled(true); // Reactivar botón después del registro

            if (result != null) {
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                if (result.equals("Registro exitoso")) {
                    navigateToLogin();
                }
            }
        });

        // Observar mensajes de error
        registerViewModel.errorMessage.observe(this, errorMessage -> {
            progressBar.setVisibility(View.GONE);
            registerButton.setEnabled(true);

            if (!TextUtils.isEmpty(errorMessage)) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                registerViewModel.clearErrorMessage(); // Limpiar el mensaje después de mostrarlo
            }
        });
    }

    /**
     * Método principal para registrar al usuario.
     */
    private void registerUser() {
        String fullName = fullNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();

        if (!validateInputs(fullName, email, password, confirmPassword, phone, address)) {
            registerButton.setEnabled(true); // Reactivar botón si falla la validación
            return;
        }

        // Mostrar ProgressBar y deshabilitar botón mientras se procesa
        progressBar.setVisibility(View.VISIBLE);

        // Llamar al método del ViewModel para registrar el usuario
        registerViewModel.registerUser(email, password, fullName, phone, address);
    }

    /**
     * Validación de todos los campos de entrada.
     */
    private boolean validateInputs(String fullName, String email, String password, String confirmPassword, String phone, String address) {
        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {
            showToast("Todos los campos son obligatorios");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Correo electrónico inválido");
            return false;
        }

        if (password.length() < 6) {
            showToast("La contraseña debe tener al menos 6 caracteres");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showToast("Las contraseñas no coinciden");
            return false;
        }

        if (!phone.matches("\\d{9,12}")) { // Se asegura de que el teléfono tenga entre 9 y 12 dígitos
            showToast("Número de teléfono inválido");
            return false;
        }

        return true;
    }

    /**
     * Muestra un mensaje `Toast` y detiene el registro si hay un error.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Navega a la actividad de Login.
     */
    private void navigateToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish(); // Evita que el usuario vuelva a la pantalla de registro con el botón de retroceso.
    }
}
