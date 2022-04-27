package com.example.projectepis;

public class Contactos {

    private String nombre, apellido, imagen;
    public Contactos(){

    }

    public Contactos(String nombre, String apellido, String imagen) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.imagen = imagen;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
