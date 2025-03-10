package com.example.galleta.Vista;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.galleta.ControladorFrases.OperarDBFrases;
import com.example.galleta.Modelo.ApiFraseDiaria;
import com.example.galleta.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FortuneCookie extends AppCompatActivity {

    private TextView TV;
    private ExecutorService ejecutor = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fortune_cookie);

        // ImageButton que nos dirige al popup
        ImageButton i_B_Closed = findViewById(R.id.I_B_Cookie);
        i_B_Closed.setOnClickListener(v -> {
            Toast.makeText(FortuneCookie.this, "La galleta se ha abierto", Toast.LENGTH_LONG).show();
            popup();
        });

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Añadir un botón de retroceso
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú de la Toolbar
        getMenuInflater().inflate(R.menu.menu_frase, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ir_savePhrase) {
            Intent intent = new Intent(FortuneCookie.this, SavePhrase.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            Toast.makeText(this, "Inicio", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(FortuneCookie.this, OpeningScreen.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.help) {
            Intent intent = new Intent(FortuneCookie.this, Help.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void popup() {
        // Inflamos el popup
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popupfrase_layout, null);

        // Creamos el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(popupView.getContext());
        builder.setView(popupView);
        AlertDialog alertDialog = builder.create();

        TV = popupView.findViewById(R.id.TextoRecibir);

        ejecutor.execute(() -> {
            String response = ApiFraseDiaria.getFrase();
            handler.post(() -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String frase = jsonObject.getString("phrase");  // Extraemos la frase diaria
                    String autor = jsonObject.getString("author");  // Extraemos el autor de esta
                    String source = jsonObject.optString("source", "");  // Extraemos la fuente, si está disponible

                    // Colocamos la frase y el autor en el TextView
                    TV.setText("\n\"" + frase + "\"\n\n - " + autor);

                    // Guardamos la frase en la base de datos cuando el usuario hace clic en el botón de guardar
                    Button guardarfrase = popupView.findViewById(R.id.GuardarFrase);
                    guardarfrase.setOnClickListener(v -> {
                        Toast.makeText(FortuneCookie.this, "Guardando Frase", Toast.LENGTH_SHORT).show();
                        guardaFraseDB(frase, autor, source);  // Guardamos la frase, el autor y la fuente en la base de datos

                        // Abrimos la actividad SavePhrase
                        Intent intent2 = new Intent(FortuneCookie.this, SavePhrase.class);
                        startActivity(intent2);
                    });

                } catch (JSONException e) {
                    TV.setText("Error al procesar la frase");
                }
            });
        });

        Button quieromas = popupView.findViewById(R.id.QuieroMas);
        quieromas.setOnClickListener(v -> {
            Intent intent1 = new Intent(FortuneCookie.this, MoreAPIs.class);
            startActivity(intent1);
        });

        // Mostrar el popup
        alertDialog.show();
    }

    // Método para guardar la frase en la base de datos sin duplicados
    private void guardaFraseDB(String frase, String autor, String source) {
        OperarDBFrases dbOp = new OperarDBFrases(this);

        // Verificar si la frase ya está en la base de datos
        if (!dbOp.existeFrase(frase)) {
            dbOp.insertarFrase(frase, autor, source);
            Toast.makeText(this, "Frase guardada correctamente.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "La frase ya está guardada.", Toast.LENGTH_SHORT).show();
        }
    }
}