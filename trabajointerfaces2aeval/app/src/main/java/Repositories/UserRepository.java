package Repositories;

import android.util.Log;

import com.google.firebase.auth.*;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.User;

public class UserRepository {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("usuarios");

    private static final String ERROR_UNKNOWN = "Error desconocido.";
    private static final String ERROR_INVALID_EMAIL = "Correo electrónico inválido.";
    private static final String ERROR_WEAK_PASSWORD = "La contraseña es demasiado débil.";
    private static final String ERROR_INVALID_CREDENTIALS = "Credenciales inválidas. Por favor, revisa tu correo y contraseña.";
    private static final String ERROR_USER_COLLISION = "Este correo ya está registrado. Intenta con otro.";
    private static final String ERROR_USER_NOT_FOUND = "El usuario no existe o ha sido deshabilitado.";

    public interface LoginCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }

    public interface RegisterCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }

    public interface FavoritesCallback {
        void onSuccess(List<String> favorites);
        void onFailure(String error);
    }

    public interface ToggleFavoriteCallback {
        void onResult(boolean success);
    }

    /**
     * Registra un nuevo usuario y almacena sus datos en Firebase.
     */
    public void registerUser(String email, String password, String fullName, String phone, String address, RegisterCallback callback) {
        if (email == null || !email.contains("@")) {
            callback.onFailure(ERROR_INVALID_EMAIL);
            return;
        }
        if (password == null || password.length() < 6) {
            callback.onFailure("La contraseña debe tener al menos 6 caracteres.");
            return;
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful() && auth.getCurrentUser() != null) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                String userId = firebaseUser.getUid();
                User user = new User(fullName, email, phone, address);

                usersRef.child(userId).setValue(user).addOnCompleteListener(dbTask -> {
                    if (dbTask.isSuccessful()) {
                        firebaseUser.sendEmailVerification().addOnCompleteListener(verificationTask -> {
                            if (verificationTask.isSuccessful()) {
                                callback.onSuccess("Registro exitoso. Por favor, verifica tu correo electrónico.");
                            } else {
                                callback.onFailure("Registro exitoso, pero no se pudo enviar el correo de verificación.");
                            }
                        });
                    } else {
                        callback.onFailure("Error al guardar datos en la base de datos.");
                    }
                });
            } else {
                handleAuthError(task.getException(), callback);
            }
        });
    }

    /**
     * Inicia sesión con correo y contraseña.
     */
    public void loginUser(String email, String password, LoginCallback callback) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful() && auth.getCurrentUser() != null) {
                FirebaseUser user = auth.getCurrentUser();
                if (user.isEmailVerified()) {
                    callback.onSuccess("Login exitoso.");
                } else {
                    callback.onFailure("Por favor, verifica tu correo antes de iniciar sesión.");
                }
            } else {
                handleAuthError(task.getException(), callback);
            }
        });
    }

    /**
     * Cierra la sesión del usuario actual.
     */
    public void logoutUser() {
        auth.signOut();
    }

    /**
     * Obtiene los favoritos del usuario desde Firebase.
     */
    public void getUserFavorites(FavoritesCallback callback) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            callback.onFailure("Usuario no autenticado.");
            return;
        }

        usersRef.child(user.getUid()).child("favoritos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<String> favorites = new ArrayList<>();
                for (DataSnapshot favSnapshot : snapshot.getChildren()) {
                    String gameId = favSnapshot.getKey();
                    if (gameId != null) {
                        favorites.add(gameId);
                    }
                }
                callback.onSuccess(favorites);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onFailure("Error al obtener los favoritos: " + error.getMessage());
            }
        });
    }

    /**
     * Agrega o elimina un favorito para el usuario en Firebase.
     */
    public void toggleFavorite(String itemId, boolean addFavorito, ToggleFavoriteCallback callback) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null || itemId == null || itemId.trim().isEmpty()) {
            callback.onResult(false);
            return;
        }

        DatabaseReference favRef = usersRef.child(user.getUid()).child("favoritos").child(itemId);
        if (addFavorito) {
            favRef.setValue(true).addOnCompleteListener(task -> callback.onResult(task.isSuccessful()))
                    .addOnFailureListener(e -> {
                        Log.e("UserRepository", "Error al agregar favorito: " + e.getMessage());
                        callback.onResult(false);
                    });
        } else {
            favRef.removeValue().addOnCompleteListener(task -> callback.onResult(task.isSuccessful()))
                    .addOnFailureListener(e -> {
                        Log.e("UserRepository", "Error al eliminar favorito: " + e.getMessage());
                        callback.onResult(false);
                    });
        }
    }

    /**
     * Manejo de errores de autenticación.
     */
    private void handleAuthError(Exception exception, Object callback) {
        String errorMessage = ERROR_UNKNOWN;

        if (exception instanceof FirebaseAuthWeakPasswordException) {
            errorMessage = ERROR_WEAK_PASSWORD;
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            errorMessage = ERROR_INVALID_CREDENTIALS;
        } else if (exception instanceof FirebaseAuthUserCollisionException) {
            errorMessage = ERROR_USER_COLLISION;
        } else if (exception instanceof FirebaseAuthInvalidUserException) {
            errorMessage = ERROR_USER_NOT_FOUND;
        } else if (exception != null) {
            errorMessage = exception.getMessage();
        }

        if (callback instanceof RegisterCallback) {
            ((RegisterCallback) callback).onFailure(errorMessage);
        } else if (callback instanceof LoginCallback) {
            ((LoginCallback) callback).onFailure(errorMessage);
        }
    }
}
