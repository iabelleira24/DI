package Views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.videojuegoslista.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private Button loginButton, registerButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Verificar si el usuario ya está autenticado
        if (mAuth.getCurrentUser() != null) {
            redirectToDashboard();
            finish();
        }

        // Vincular elementos del layout
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);

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

        // Mostrar progreso
        progressBar.setVisibility(View.VISIBLE);

        // Intentar iniciar sesión con FirebaseAuth
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            // Ocultar progreso
            progressBar.setVisibility(View.GONE);

            if (task.isSuccessful()) {
                // Inicio de sesión exitoso
                Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                redirectToDashboard();
            } else {
                // Manejo de errores personalizado
                String errorMessage;
                try {
                    throw task.getException();
                } catch (FirebaseAuthInvalidUserException e) {
                    errorMessage = "Usuario no registrado. Por favor, regístrate primero.";
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    errorMessage = "Correo o contraseña incorrectos.";
                } catch (Exception e) {
                    errorMessage = "Error: " + e.getMessage();
                }
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
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
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Finalizar LoginActivity para evitar regresar al presionar "atrás"
    }

    /**
     * Valida los campos de correo y contraseña.
     *
     * @return true si los campos son válidos, false si no lo son.
     */
    private boolean validateFields(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Por favor, ingresa tu correo electrónico.");
            emailEditText.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Por favor, ingresa un correo electrónico válido.");
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
