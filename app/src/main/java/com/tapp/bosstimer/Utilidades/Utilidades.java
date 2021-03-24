package com.tapp.bosstimer.Utilidades;

import android.media.Image;

public class Utilidades {
    public static String DB = "BOSS";
    public static String ID = "ID";
    public static String TABLA_PLAYERS = "PLAYERS";
    public static String TABLA_BOSS = "BOSS";
    public static String TABLA_NOTIFICACIONES = "NOTIFICACIONES";
    public static String PlayerID = "ID_PLAYER";
    public static String Nombre = "NOMBRE";
    public static String Imagen = "IMAGEN";
    public static String BossID = "BOSS_ID";
    public static String Categoria = "CAT";
    public static String TIMEHOUR = "TIME";
    public static String Hour = "HOUR";
    public static String Min = "MIN";

    public static String CREATE_TABLE_PLAYERS ="CREATE TABLE "+TABLA_PLAYERS+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Nombre + " TEXT)";
    public static String CREATE_TABLE_BOSS = "CREATE TABLE "+TABLA_BOSS+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +Nombre+" TEXT, "+ Imagen + " TEXT, "+Categoria+ "TEXT, "+Hour+" TEXT);";
    public static String CREATE_TABLE_NOTIFICACIONES ="CREATE TABLE "+TABLA_NOTIFICACIONES+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +BossID+" TEXT, "+ PlayerID +" INTEGER, "+ Hour + " TEXT, " + Min + " TEXT, "+Imagen+" TEXT);";
}
