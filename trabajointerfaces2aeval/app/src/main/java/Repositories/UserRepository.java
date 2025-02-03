package Repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Models.User;

public class UserRepository {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

    /**
     * Interfaz para manejar los resultados del inicio de sesión.
     */
    public interface LoginCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }

    /**
     * Interfaz para manejar los resultados del registro de usuario.
     */
    public interface RegisterCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }

    /**
     * Registra un nuevo usuario con correo y contraseña, y guarda sus datos en Firebase Realtime Database.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @param fullName Nombre completo del usuario.
     * @param phone Número de teléfono del usuario.
     * @param address Dirección del usuario.
     * @param callback Callback para manejar el resultado del registro.
     */
    public void registerUser(String email, String password, String fullName, String phone, String address, RegisterCallback callback) {
        // Crear un nuevo usuario con correo y contraseña
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                if (firebaseUser != null) {
                    String userId = firebaseUser.getUid();
                    User user = new User(fullName, email, phone, address);

                    // Guardar los datos del usuario en Firebase Realtime Database
                    usersRef.child(userId).setValue(user).addOnCompleteListener(dbTask -> {
                        if (dbTask.isSuccessful()) {
                            // Enviar correo de verificación
                            firebaseUser.sendEmailVerification().addOnCompleteListener(verificationTask -> {
                                if (verificationTask.isSuccessful()) {
                                    callback.onSuccess("Registro exitoso. Por favor, verifica tu correo electrónico.");
                                } else {
                                    callback.onFailure("Registro exitoso, pero no se pudo enviar el correo de verificación: " + verificationTask.getException().getMessage());
                                }
                            });
                        } else {
                            callback.onFailure("Error al guardar datos en la base de datos: " + dbTask.getException().getMessage());
                        }
                    });
                }
            } else {
                callback.onFailure("Error en el registro: " + task.getException().getMessage());
            }
        });
    }

    /**
     * Inicia sesión de un usuario con correo y contraseña, y verifica si el correo está validado.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @param callback Callback para manejar el resultado del inicio de sesión.
     */
    public void loginUser(String email, String password, LoginCallback callback) {
        // Intentar iniciar sesión con correo y contraseña
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    // Verificar si el correo electrónico está verificado
                    if (user.isEmailVerified()) {
                        callback.onSuccess("Login exitoso");
                    } else {
                        callback.onFailure("Por favor verifica tu correo antes de iniciar sesión.");
                    }
                } else {
                    callback.onFailure("Error: Usuario no encontrado");
                }
            } else {
                callback.onFailure("Error en el login: " + task.getException().getMessage());
            }
        });
    }

    /**
     * Cierra la sesión del usuario actual.
     */
    public void logoutUser() {
        auth.signOut();
    }
}
