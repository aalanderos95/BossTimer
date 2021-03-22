package com.tapp.bosstimer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.tapp.bosstimer.Utilidades.Utilidades;

public class SqliteDbHelper extends SQLiteOpenHelper {
    public SqliteDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.CREATE_TABLE_BOSS);
        db.execSQL(Utilidades.CREATE_TABLE_NOTIFICACIONES);
        db.execSQL(Utilidades.CREATE_TABLE_PLAYERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS PLAYERS");
        db.execSQL("DROP TABLE IF EXISTS BOSS");
        db.execSQL("DROP TABLE IF EXISTS NOTIFICACIONES");
        onCreate(db);
    }
}
