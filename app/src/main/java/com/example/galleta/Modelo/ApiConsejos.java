package com.example.galleta.Modelo;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiConsejos {
    // URL para conectarnos a la API de consejos
    private static final String URLConsejos = "https://api.adviceslip.com/advice";

    public static String getConsejo() {
        OkHttpClient cliente = new OkHttpClient();
        Request pregunta = new Request.Builder().url(URLConsejos).get().build();

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