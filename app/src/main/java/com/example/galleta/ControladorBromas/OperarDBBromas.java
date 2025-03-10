package com.example.galleta.ControladorBromas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class OperarDBBromas {

    private CrearDBBromas crearDB;

    public OperarDBBromas(Context contexto) {
        crearDB = new CrearDBBromas(contexto);
    }

    // Insertar una nueva broma en la base de datos
    public void insertarBroma(String setup, String delivery, String source) {
        SQLiteDatabase db = crearDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("setup", setup);
        values.put("delivery", delivery);
        values.put("source", source);
        db.insert("Bromas", null, values);
        db.close();
    }


    // Comprobar si una broma con el mismo ID ya existe para evitar duplicados
    public boolean existeBroma(String setup, String delivery) {
        SQLiteDatabase db = crearDB.getReadableDatabase();
        Cursor consulta = db.rawQuery("SELECT 1 FROM Bromas WHERE setup = ? AND delivery = ?", new String[]{setup, delivery});
        boolean exists = consulta.getCount() > 0;
        consulta.close();
        db.close();
        return exists;
    }


    // Obtener todas las bromas guardadas
    public List<String> obtenerBromas() {
        List<String> bromas = new ArrayList<>();
        SQLiteDatabase db = crearDB.getReadableDatabase();
        Cursor consulta = db.rawQuery("SELECT id, setup, delivery FROM Bromas", null);

        int idIndex = consulta.getColumnIndex("id");
        int setupIndex = consulta.getColumnIndex("setup");
        int deliveryIndex = consulta.getColumnIndex("delivery");

        if (idIndex != -1 && setupIndex != -1 && deliveryIndex != -1) {
            if (consulta.moveToFirst()) {
                do {
                    int id = consulta.getInt(idIndex);
                    String setup = consulta.getString(setupIndex);
                    String delivery = consulta.getString(deliveryIndex);
                    bromas.add("Broma #"+ id + "\n" + setup + "\n" + delivery );
                } while (consulta.moveToNext());
            }
        } else {
            Log.e("DatabaseError", "Las columnas necesarias no se encuentran en la consulta.");
        }

        consulta.close();
        db.close();
        return bromas;
    }

    // Método para eliminar una broma de la base de datos por ID
    public void borrarBroma(int id) {
        SQLiteDatabase db = crearDB.getWritableDatabase();
        int filasAfectadasBromas = db.delete("Bromas", "id = ?", new String[]{String.valueOf(id)});
        db.close();
        if (filasAfectadasBromas > 0) {
            Log.d("SQLite", "Broma eliminada correctamente: " + id);
        } else {
            Log.e("SQLite", "Error: No se encontró la broma con ID " + id);
        }
    }
}
