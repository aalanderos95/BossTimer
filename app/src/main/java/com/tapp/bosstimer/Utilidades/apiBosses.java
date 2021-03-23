package com.tapp.bosstimer.Utilidades;

public class apiBosses {
    public String name;
    public int hours;

    public apiBosses(String name, int hours)
    {
        this.name = name;
        this.hours = hours;
    }

    public int getHours() {
        return hours;
    }

    public String getName() {
        return name;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setName(String name) {
        this.name = name;
    }
}
