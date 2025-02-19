package com.example.videojuegoslista.viewmodels;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.videojuegoslista.models.Usuario;
import com.example.videojuegoslista.repositories.UserRepository;

public class RegisterViewModel extends ViewModel {
    private final MutableLiveData<String> resultadoRegistro = new MutableLiveData<>();
    private final MutableLiveData<String> errorValidacion = new MutableLiveData<>();
    private final MutableLiveData<Boolean> estaCargando = new MutableLiveData<>();
    private final UserRepository userRepository;

    public RegisterViewModel() {
        userRepository = new UserRepository();
        estaCargando.setValue(false);
    }

    public void registrarUsuario(String nombre, String apellido, String correo,
                                 String contrasena, String confirmarContrasena) {
        if (!validarEntradas(nombre, apellido, correo, contrasena, confirmarContrasena)) {
            return;
        }

        estaCargando.setValue(true);

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setApellido(apellido);
        nuevoUsuario.setCorreo(correo);
        nuevoUsuario.setContrasena(contrasena);

        userRepository.registrarUsuario(
                nuevoUsuario,
                usuarioFirebase -> {
                    resultadoRegistro.setValue("SUCCESS");
                    estaCargando.setValue(false);
                },
                excepcion -> {
                    errorValidacion.setValue(excepcion.getMessage());
                    estaCargando.setValue(false);
                }
        );
    }

    private boolean validarEntradas(String nombre, String apellido, String correo,
                                    String contrasena, String confirmarContrasena) {
        if (nombre == null || nombre.trim().isEmpty()) {
            errorValidacion.setValue("El nombre no puede estar vacío");
            return false;
        }

        if (apellido == null || apellido.trim().isEmpty()) {
            errorValidacion.setValue("El apellido no puede estar vacío");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            errorValidacion.setValue("El correo no es válido");
            return false;
        }

        if (contrasena == null || contrasena.length() < 6) {
            errorValidacion.setValue("La contraseña debe tener al menos 6 caracteres");
            return false;
        }

        if (!contrasena.equals(confirmarContrasena)) {
            errorValidacion.setValue("Las contraseñas no coinciden");
            return false;
        }

        return true;
    }

    public LiveData<String> obtenerResultadoRegistro() {
        return resultadoRegistro;
    }

    public LiveData<String> obtenerErrorValidacion() {
        return errorValidacion;
    }

    public LiveData<Boolean> obtenerEstadoCarga() {
        return estaCargando;
    }
}