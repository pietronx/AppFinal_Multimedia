package com.example.galleta.Modelo;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiFraseDiaria {
    // URL para conectarnos a la API de frases diarias
    private static final String URLFraseDiaria = "https://frasedeldia.azurewebsites.net/api/phrase";

    public static String getFrase() {
        OkHttpClient cliente = new OkHttpClient();
        Request pregunta = new Request.Builder().url(URLFraseDiaria).get().build();

        try (Response respuesta = cliente.newCall(pregunta).execute()) {
            if (respuesta.isSuccessful() && respuesta.body() != null) {
                return respuesta.body().string();
            } else {
                return "Error en la solicitud" + respuesta.code();
            }
        } catch (IOException e) {
            return "Excepcion" + e.getMessage();
        }
    }
}