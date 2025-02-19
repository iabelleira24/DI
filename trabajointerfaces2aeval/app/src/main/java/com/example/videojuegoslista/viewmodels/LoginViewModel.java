package com.example.videojuegoslista.viewmodels;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.videojuegoslista.models.Usuario;
import com.example.videojuegoslista.repositories.UserRepository;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends ViewModel {
    private UserRepository userRepository;
    private MutableLiveData<FirebaseUser> loginResult = new MutableLiveData<>();
    private MutableLiveData<String> loginError = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LoginViewModel() {
        userRepository = new UserRepository();
    }

    public void login(String email, String password) {
        if (!validateInput(email, password)) return;

        isLoading.setValue(true);
        Usuario user = new Usuario(email, password);
        userRepository.iniciarSesion(user, loginResult, loginError);
    }

    private boolean validateInput(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            loginError.setValue("El email no puede estar vacío");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginError.setValue("Email inválido");
            return false;
        }

        if (password == null || password.trim().isEmpty()) {
            loginError.setValue("La contraseña no puede estar vacía");
            return false;
        }

        if (password.length() < 6) {
            loginError.setValue("La contraseña debe tener al menos 6 caracteres");
            return false;
        }

        return true;
    }

    public LiveData<FirebaseUser> getLoginResult() {
        return loginResult;
    }

    public LiveData<String> getLoginError() {
        return loginError;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}