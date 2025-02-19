package com.example.videojuegoslista.models;

import java.util.HashMap;
import java.util.Map;

public class Usuario {
    private String uid;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;

    // Constructor vacío (necesario para Firebase)
    public Usuario() {
    }

    // Constructor con todos los campos
    public Usuario(String uid, String nombre, String apellido, String correo, String contrasena) {
        this.uid = uid;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    // Constructor para login (solo correo y contraseña)
    public Usuario(String correo, String contrasena) {
        this.correo = correo;
        this.contrasena = contrasena;
    }

    // Getters y Setters
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    // Método toMap para Firebase
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("nombre", nombre);
        result.put("apellido", apellido);
        result.put("correo", correo);
        return result;
    }
}