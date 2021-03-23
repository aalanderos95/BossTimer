package com.tapp.bosstimer.Clases;

public class mainAlertas {
    private int ID;
    private String player;
    private String boss;
    private String hour;
    private String min;
    private String imagen;

    public mainAlertas(int id, String player, String boss, String hour, String min, String imagen)
    {
        this.ID = id;
        this.player = player;
        this.boss = boss;
        this.hour = hour;
        this.min = min;
        this.imagen = imagen;
    }

    public int getID() {
        return ID;
    }

    public String getBoss() {
        return boss;
    }

    public String getHour() {
        return hour;
    }

    public String getImagen() {
        return imagen;
    }

    public String getMin() {
        return min;
    }

    public String getPlayer() {
        return player;
    }
}
