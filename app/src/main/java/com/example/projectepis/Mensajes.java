package com.example.projectepis;

public class Mensajes {
    private String de, mensaje, tipo, para, mensajeID, fecha, hora, nombre, apellido;


    public Mensajes(){}

    public Mensajes(String de, String mensaje, String tipo, String para, String mensajeID, String fecha, String hora, String nombre, String apellido) {
        this.de = de;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.para = para;
        this.mensajeID = mensajeID;
        this.fecha = fecha;
        this.hora = hora;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public String getMensajeID() {
        return mensajeID;
    }

    public void setMensajeID(String mensajeID) {
        this.mensajeID = mensajeID;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
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
}
