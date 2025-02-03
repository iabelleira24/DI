package Views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.videojuegoslista.R;

import Viewmodels.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {

    private RegisterViewModel registerViewModel;

    private EditText fullNameEditText, emailEditText, passwordEditText, confirmPasswordEditText, phoneEditText, addressEditText;

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

        // Configurar el botón de registro
        findViewById(R.id.registerButton).setOnClickListener(v -> registerUser());

        // Configurar el botón de regresar
        findViewById(R.id.backToLoginButton).setOnClickListener(v -> navigateToLogin());

        // Observar el resultado del registro
        registerViewModel.registrationResult.observe(this, result -> {
            if (result != null) {
                Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_SHORT).show();
                if (result.equals("Registro exitoso")) {
                    navigateToLogin();
                }
            }
        });

        // Observar los mensajes de error
        registerViewModel.errorMessage.observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
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
            return;
        }

        // Llamar al método del ViewModel para registrar el usuario
        registerViewModel.registerUser(email, password, fullName, phone, address);
    }

    /**
     * Validación de todos los campos de entrada.
     */
    private boolean validateInputs(String fullName, String email, String password, String confirmPassword, String phone, String address) {
        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Introduce un correo electrónico válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * Navega a la actividad de Login.
     */
    private void navigateToLogin() {
        // Navegar a LoginActivity
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();  // Finaliza esta actividad para que el usuario no pueda volver atrás con el botón de regreso.
    }
}
