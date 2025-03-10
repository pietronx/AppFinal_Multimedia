package com.example.galleta.ControladorFrases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class OperarDBFrases {

    private CrearDBFrases crearDB;

    public OperarDBFrases(Context contexto) {
        crearDB = new CrearDBFrases(contexto);
    }

    // Insertar una nueva frase en la base de datos
    public void insertarFrase(String frase, String autor, String source) {
        SQLiteDatabase db = crearDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("frase", frase);
        values.put("autor", autor);
        values.put("source", source);
        db.insert("Frases", null, values);
        db.close();
    }

    // Comprobar si la frase ya existe para evitar duplicados
    public boolean existeFrase(String frase) {
        SQLiteDatabase db = crearDB.getReadableDatabase();
        Cursor consulta = db.rawQuery("SELECT 1 FROM Frases WHERE frase = ?", new String[]{frase});
        boolean exists = consulta.getCount() > 0;  // Si la database ya tiene ese registro, la frase ya existe
        consulta.close();
        db.close();
        return exists;
    }

    // Obtener las frases guardadas
    public List<String> obtenerFrases() {
        List<String> frases = new ArrayList<>();
        SQLiteDatabase db = crearDB.getReadableDatabase();
        Cursor consulta = db.rawQuery("SELECT frase, autor FROM Frases", null);

        int fraseIndex = consulta.getColumnIndex("frase");
        int autorIndex = consulta.getColumnIndex("autor");

        if (fraseIndex != -1 && autorIndex != -1) {
            if (consulta.moveToFirst()) {
                do {
                    String frase = consulta.getString(fraseIndex);
                    String autor = consulta.getString(autorIndex);
                    frases.add("\"" + frase + "\"\n - " + autor);
                } while (consulta.moveToNext());
            }
        } else {
            Log.e("DatabaseError", "Las columnas 'frase' y/o 'autor' no se encuentran en la consulta.");
        }

        consulta.close();
        db.close();
        return frases;
    }

    // Método para eliminar una frase de la base de datos
    public void borrarFrase(String frase) {
        SQLiteDatabase db = crearDB.getWritableDatabase();
        db.execSQL("DELETE FROM Frases WHERE id = (SELECT MAX(id) FROM Frases)");
        // Cerramos la base de datos
        db.close();
    }
}
