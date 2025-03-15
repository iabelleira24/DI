package com.example.videojuegoslista.models;

public class Item {
    private String id;
    private String titulo;
    private String descripcion;
    private String url;
    private boolean favorite;


    // Constructor vacío (requerido para Firebase)
    public Item() {
    }

    public Item(String id, String titulo, String descripcion, String url) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.url = url;
        this.favorite = false; // Por defecto no es favorita

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setFavorite(boolean favorite) {  // Setter para favorite
        this.favorite = favorite;
    }
}
