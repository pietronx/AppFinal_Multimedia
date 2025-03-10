package com.example.galleta.ControladorConsejos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Clase que maneja la base de datos para almacenar los consejos.
public class CrearDBConsejos extends SQLiteOpenHelper {

    // Nombre del archivo de la base de datos.
    public static final String DATABASE_NAME = "galleta_consejos.db";

    // Versión de la base de datos (se debe incrementar si hay cambios en la estructura).
    public static final int DATABASE_VERSION = 1;

    // Sentencia SQL para crear la tabla "Consejos" si no existe.
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS Consejos (" +
            "id INTEGER PRIMARY KEY, " + // Columna 'id' como clave primaria autoincremental.
            "advice TEXT);"; // Columna 'advice' para almacenar el texto del consejo.

    // Constructor de la base de datos. Recibe el contexto de la aplicación.
    public CrearDBConsejos(Context contexto) {
        super(contexto, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Método que se ejecuta cuando la base de datos se crea por primera vez.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE); // Ejecuta la sentencia SQL para crear la tabla.
    }

    // Método que se ejecuta cuando la base de datos necesita actualizarse (cambio de versión).
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Consejos"); // Elimina la tabla existente para evitar conflictos.
        onCreate(db); // Vuelve a crear la tabla con la nueva estructura.
    }
}
