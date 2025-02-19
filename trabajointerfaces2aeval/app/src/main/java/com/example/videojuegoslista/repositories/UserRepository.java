package com.example.videojuegoslista.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.videojuegoslista.models.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRepository {
    private final FirebaseAuth firebaseAuth;
    private final DatabaseReference databaseReference;

    public UserRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
    }

    public void iniciarSesion(Usuario usuario, MutableLiveData<FirebaseUser> resultado,
                              MutableLiveData<String> error) {
        firebaseAuth.signInWithEmailAndPassword(usuario.getCorreo(), usuario.getContrasena())
                .addOnSuccessListener(authResult ->
                        resultado.setValue(firebaseAuth.getCurrentUser()))
                .addOnFailureListener(e ->
                        error.setValue("Error al iniciar sesión: " + e.getMessage()));
    }

    public void registrarUsuario(Usuario usuario,
                                 OnSuccessListener<FirebaseUser> oyenteExito,
                                 OnFailureListener oyenteFallo) {
        firebaseAuth.createUserWithEmailAndPassword(usuario.getCorreo(), usuario.getContrasena())
                .addOnSuccessListener(authResult -> {
                    FirebaseUser usuarioFirebase = firebaseAuth.getCurrentUser();
                    if (usuarioFirebase != null) {
                        usuario.setUid(usuarioFirebase.getUid());
                        guardarDatosUsuario(usuario, oyenteExito, oyenteFallo);
                    }
                })
                .addOnFailureListener(oyenteFallo);
    }

    private void guardarDatosUsuario(Usuario usuario,
                                     OnSuccessListener<FirebaseUser> oyenteExito,
                                     OnFailureListener oyenteFallo) {
        databaseReference.child(usuario.getUid())
                .setValue(usuario.toMap())
                .addOnSuccessListener(aVoid -> oyenteExito.onSuccess(firebaseAuth.getCurrentUser()))
                .addOnFailureListener(e -> {
                    if (firebaseAuth.getCurrentUser() != null) {
                        firebaseAuth.getCurrentUser().delete();
                    }
                    oyenteFallo.onFailure(e);
                });
    }

    public FirebaseUser obtenerUsuarioActual() {
        return firebaseAuth.getCurrentUser();
    }
}