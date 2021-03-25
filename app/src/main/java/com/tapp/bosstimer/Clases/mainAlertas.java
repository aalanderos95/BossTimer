package com.tapp.bosstimer.Clases;

public class mainAlertas {
    private int ID;
    private String player;
    private String boss;
    private String hour;
    private String min;
    private String imagen;
    private String timeSpan;
    private String hourMonsters;
    public mainAlertas(int id, String player, String boss, String hour, String min, String imagen, String timeSpan, String hourMonsters)
    {
        this.ID = id;
        this.player = player;
        this.boss = boss;
        this.hour = hour;
        this.min = min;
        this.imagen = imagen;
        this.timeSpan = timeSpan;
        this.hourMonsters = hourMonsters;
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

    public String getTimeSpan() {
        return timeSpan;
    }

    public String getHourMonsters() {
        return hourMonsters;
    }
}
