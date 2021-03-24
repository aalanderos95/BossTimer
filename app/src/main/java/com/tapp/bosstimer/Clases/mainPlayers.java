package com.tapp.bosstimer.Clases;

public class mainPlayers {
    private int ID;
    private String Nombre;
    private String Imagen;

    public mainPlayers(int ID, String nombre, String Imagen)
    {
        this.ID = ID;
        this.Nombre = nombre;
        this.Imagen = Imagen;
    }

    public int getID() {
        return ID;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getImagen() {
        return Imagen;
    }
}
