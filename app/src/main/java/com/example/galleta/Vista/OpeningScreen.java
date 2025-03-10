package com.example.galleta.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.galleta.R;

public class OpeningScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_openingscreen);

        // Referencia a la ProgressBar
        ProgressBar pBar = findViewById(R.id.prBar);

        // Mostrar la ProgressBar
        pBar.setVisibility(View.INVISIBLE);

        // Usar un Handler para realizar el Intent después de 2 segundos
        new Handler().postDelayed(() -> {

            Intent intent = new Intent(OpeningScreen.this, FortuneCookie.class);
            startActivity(intent);

            // Finalizar esta actividad si no es necesaria volver a ella
            finish();
        }, 2500); // 1000 milisegundos = 1 segundos
    }
}