package com.example.videojuegoslista;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Vincular elementos del layout
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // Configurar botones
        loginButton.setOnClickListener(v -> loginUser());
        registerButton.setOnClickListener(v -> openRegisterActivity());
    }

    /**
     * Inicia el proceso de inicio de sesión del usuario.
     */
    private void loginUser() {
        // Obtener el email y la contraseña ingresados
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validar campos
        if (!validateFields(email, password)) return;

        // Intentar iniciar sesión con FirebaseAuth
        if (mAuth != null) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Inicio de sesión exitoso
                    Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    redirectToDashboard();
                } else {
                    // Mostrar mensaje de error
                    String errorMessage = task.getException() != null ? task.getException().getMessage() : "Error desconocido";
                    Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(LoginActivity.this, "Error: Autenticación no inicializada", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Abre la actividad de registro.
     */
    private void openRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Redirige al usuario a la pantalla principal (DashboardActivity).
     */
    private void redirectToDashboard() {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Finalizar LoginActivity para evitar regresar al presionar "atrás"
    }


    private boolean validateFields(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Por favor, ingresa tu correo electrónico.");
            emailEditText.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Por favor, ingresa tu contraseña.");
            passwordEditText.requestFocus();
            return false;
        }

        return true;
    }
}
