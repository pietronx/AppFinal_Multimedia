package com.example.galleta.Modelo;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiBromas {
    // URL para conectarnos a la API de bromas
    private static final String URLBromas = "https://v2.jokeapi.dev/joke/Any?lang=es&type=twopart";

    public static String getBroma() {
        OkHttpClient cliente = new OkHttpClient();
        Request pregunta = new Request.Builder().url(URLBromas).get().build();

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