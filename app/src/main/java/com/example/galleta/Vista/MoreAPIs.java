package com.example.galleta.Vista;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.galleta.ControladorBromas.OperarDBBromas;
import com.example.galleta.ControladorConsejos.OperarDBConsejos;
import com.example.galleta.Modelo.ApiBromas;
import com.example.galleta.Modelo.ApiConsejos;
import com.example.galleta.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Clase que representa la pantalla MoreAPIs, donde el usuario puede obtener y guardar consejos y bromas desde APIs externas.
public class MoreAPIs extends AppCompatActivity implements TextToSpeech.OnInitListener {

    // Ejecutores y manejadores para realizar operaciones en segundo plano
    private final ExecutorService ejecutor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    // Variables para el motor de Text-to-Speech (TTS)
    private TextToSpeech tts;
    private boolean ttsReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_more_apis);

        // Inicializar el motor de síntesis de voz (TTS)
        tts = new TextToSpeech(this, this);

        // CardView para abrir popups de consejos
        CardView consejos = findViewById(R.id.Consejos);
        consejos.setOnClickListener(v -> {
            Toast.makeText(this, "Consejos en Inglés", Toast.LENGTH_SHORT).show();
            popupConsejos();
        });

        // CardView para abrir popups de bromas
        CardView bromas = findViewById(R.id.Bromas);
        bromas.setOnClickListener(v -> {
            Toast.makeText(this, "Bromas", Toast.LENGTH_SHORT).show();
            popupBromas();
        });

        // Configuración de la toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // Método que se ejecuta cuando el TTS se inicializa
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Idioma no soportado");
            } else {
                ttsReady = true;
            }
        } else {
            Log.e("TTS", "Error en la inicialización");
        }
    }

    // Método para hacer que el TTS lea un texto en voz alta
    private void speakText(String text) {
        if (ttsReady && !text.isEmpty()) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    // Método que se ejecuta cuando la actividad se destruye
    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    // Método para inflar el menú de la toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_moreapis, menu);
        return true;
    }

    // Método que maneja la selección de opciones en la barra de herramientas
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ir_savePhrase) {
            startActivity(new Intent(MoreAPIs.this, SavePhrase.class));
            return true;
        } else if (item.getItemId() == R.id.inicio) {
            startActivity(new Intent(MoreAPIs.this, FortuneCookie.class));
            return true;
        } else if (item.getItemId() == R.id.help) {
            startActivity(new Intent(MoreAPIs.this, Help.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    // Método para mostrar el popup de consejos
    public void popupConsejos() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popupconsejos_layout, null);

        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(popupView).create();

        TextView TV = popupView.findViewById(R.id.ConsejosRecibir);
        Button btnSpeak = popupView.findViewById(R.id.btnTTS);
        Button guardarConsejo = popupView.findViewById(R.id.GuardarConsejo);

        // Llamar a la API de consejos en segundo plano
        ejecutor.execute(() -> {
            String response = ApiConsejos.getConsejo();
            handler.post(() -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // Extraer datos del JSON recibido
                    JSONObject slipObject = jsonObject.getJSONObject("slip");
                    int id = slipObject.getInt("id");
                    String advice = slipObject.getString("advice");
                    String consejoFormateado = "Tip #" + id + "\n" + advice;
                    TV.setText(consejoFormateado);

                    // Botón para leer el consejo en voz alta
                    btnSpeak.setOnClickListener(v -> speakText(consejoFormateado));

                    // Botón para guardar el consejo en la base de datos
                    guardarConsejo.setOnClickListener(v -> {
                        OperarDBConsejos dbOp = new OperarDBConsejos(this);
                        if (!dbOp.existeConsejo(id)) {
                            dbOp.insertarConsejo(id, advice);
                            Toast.makeText(this, "Consejo guardado correctamente.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Este consejo ya está guardado.", Toast.LENGTH_SHORT).show();
                        }
                        startActivity(new Intent(MoreAPIs.this, SavePhrase.class));
                        alertDialog.dismiss();
                    });

                } catch (JSONException e) {
                    TV.setText("Error al procesar el consejo");
                }
            });
        });

        alertDialog.show();
    }

    // Método para mostrar el popup de bromas
    public void popupBromas() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popupbromas_layout, null);

        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(popupView).create();

        TextView TV = popupView.findViewById(R.id.BromasRecibir);
        Button guardarBroma = popupView.findViewById(R.id.GuardarBroma);

        // Llamar a la API de bromas en segundo plano
        ejecutor.execute(() -> {
            String response = ApiBromas.getBroma();
            handler.post(() -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // Extraer las partes de la broma del JSON
                    String bromaparte1 = jsonObject.getString("setup");
                    String bromaparte2 = jsonObject.getString("delivery");
                    String bromaFormateada = "\n\"" + bromaparte1 + "\"\n\"" + bromaparte2 + "\"";
                    TV.setText(bromaFormateada);

                    // Botón para guardar la broma en la base de datos
                    guardarBroma.setOnClickListener(v -> {
                        OperarDBBromas dbBromas = new OperarDBBromas(this);
                        if (!dbBromas.existeBroma(bromaparte1, bromaparte2)) {
                            dbBromas.insertarBroma(bromaparte1, bromaparte2, "ApiBromas");
                            Toast.makeText(this, "Broma guardada correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Esta broma ya está guardada", Toast.LENGTH_SHORT).show();
                        }
                        startActivity(new Intent(MoreAPIs.this, SavePhrase.class));
                        alertDialog.dismiss();
                    });

                } catch (JSONException e) {
                    TV.setText("Error al procesar la broma");
                }
            });
        });
        alertDialog.show();
    }
}