package com.example.galleta.ControladorBromas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CrearDBBromas extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "galleta_bromas.db";
    public static final int DATABASE_VERSION = 1;

    // Sentencias para crear la tabla
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS Bromas (" +
            "id INTEGER PRIMARY KEY, " +
            "setup TEXT, " +
            "delivery TEXT, " +
            "source TEXT);";

    public CrearDBBromas(Context contexto) {
        super(contexto, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Bromas");
        onCreate(db);
    }
}



