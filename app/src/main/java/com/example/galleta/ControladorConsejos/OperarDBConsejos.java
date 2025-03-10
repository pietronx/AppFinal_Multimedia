package com.example.galleta.ControladorConsejos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class OperarDBConsejos {

    private CrearDBConsejos crearDB;

    public OperarDBConsejos(Context contexto) {
        crearDB = new CrearDBConsejos(contexto);
    }

    // Insertar un nuevo consejo en la base de datos
    public void insertarConsejo(int id, String advice) {
        SQLiteDatabase db = crearDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("advice", advice);
        db.insert("Consejos", null, values);
        db.close();
    }

    // Comprobar si el consejo ya existe para evitar duplicados
    public boolean existeConsejo(int id) {
        SQLiteDatabase db = crearDB.getReadableDatabase();
        String query = "SELECT id FROM Consejos WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return existe;
    }

    // Obtener los consejos guardados
    public List<String> obtenerConsejos() {
        List<String> consejos = new ArrayList<>();
        SQLiteDatabase db = crearDB.getReadableDatabase();
        Cursor consulta = db.rawQuery("SELECT id, advice FROM Consejos", null);

        int idIndex = consulta.getColumnIndex("id");
        int adviceIndex = consulta.getColumnIndex("advice");

        if (idIndex != -1 && adviceIndex != -1) {
            if (consulta.moveToFirst()) {
                do {
                    int id = consulta.getInt(idIndex);
                    String advice = consulta.getString(adviceIndex);
                    String consejoFormateado = "Tip #" + id + "\n" + advice;
                    consejos.add(consejoFormateado);
                    //consejos.add("Consejo #: " + id + "\n" + advice);
                } while (consulta.moveToNext());
            }
        } else {
            Log.e("DatabaseError", "Las columnas 'id' y/o 'advice' no se encuentran en la consulta.");
        }
        consulta.close();
        db.close();
        return consejos;
    }

    // Método para eliminar un consejo de la base de datos
    public void borrarConsejo(int id) {
        SQLiteDatabase db = crearDB.getWritableDatabase();
        int filasAfectadas = db.delete("Consejos", "id = ?", new String[]{String.valueOf(id)});
        db.close();

        if (filasAfectadas > 0) {
            Log.d("SQLite", "Consejo eliminado correctamente: " + id);
        } else {
            Log.e("SQLite", "Error: No se encontró el consejo con ID " + id);
        }
    }
}