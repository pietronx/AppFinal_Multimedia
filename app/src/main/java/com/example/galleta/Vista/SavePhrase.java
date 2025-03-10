/*

package com.example.galleta.ControladorFrases;

import android.os.Bundle;
import android.media.MediaPlayer;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleta.R;
//import com.example.galleta.Controlador.FraseAdapter;

import java.util.List;

public class SavePhrase extends AppCompatActivity {

    private MediaPlayer sonido;
    private RecyclerView recyclerViewFrases;
    private FraseAdapter fraseAdapter;
    private List<String> frasesGuardadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_phrase);

        // Reproducir música al entrar en esta activity
        sonido = MediaPlayer.create(this, R.raw.soundeffectchina); // Cargamos el sonido
        sonido.start(); // Reproduce automáticamente el sonido

//-------------------------------- FRASES --------------------------------\\

        // Configurar el RecyclerView
        recyclerViewFrases = findViewById(R.id.listViewFrases);

        // Obtener las frases guardadas desde la base de datos
        OperarDBFrases dbOp = new OperarDBFrases(this);
        frasesGuardadas = dbOp.obtenerFrases();

        // Configurar el Adapter y LayoutManager
        fraseAdapter = new FraseAdapter(this, frasesGuardadas);
        recyclerViewFrases.setAdapter(fraseAdapter);
        recyclerViewFrases.setLayoutManager(new LinearLayoutManager(this));



//-------------------------------- CONSEJOS --------------------------------\\


//-------------------------------- BROMAS --------------------------------\\


    }

    @Override
    // Método para liberar y destruir el sonido al salir de la activity
    protected void onDestroy() {
        super.onDestroy();
        if (sonido != null) {
            sonido.release();
            sonido = null;
        }
    }
}

*/
package com.example.galleta.Vista;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleta.ControladorBromas.OperarDBBromas;
import com.example.galleta.ControladorConsejos.OperarDBConsejos;
import com.example.galleta.ControladorFrases.OperarDBFrases;
import com.example.galleta.Helpers.Adaptador;
import com.example.galleta.R;

import java.util.ArrayList;
import java.util.List;

public class SavePhrase extends AppCompatActivity {

    private MediaPlayer sonido;
    private RecyclerView recyclerViewFrases;
    private Adaptador adapter; // Adaptador unificado
    private List<String> items; // Lista unificada para frases, consejos y bromas
    private OperarDBFrases dbOpFrases;
    private OperarDBConsejos dbOpConsejos;
    private OperarDBBromas dbOpBromas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_phrase);
        EdgeToEdge.enable(this);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Reproducir música al entrar en esta activity
        sonido = MediaPlayer.create(this, R.raw.soundeffectchina); // Cargamos el sonido
        sonido.start(); // Reproduce automáticamente el sonido

        // Inicializar las operaciones de la base de datos
        dbOpFrases = new OperarDBFrases(this);
        dbOpConsejos = new OperarDBConsejos(this);
        dbOpBromas = new OperarDBBromas(this);

        // Configurar el RecyclerView
        recyclerViewFrases = findViewById(R.id.listViewFrases);

        // Obtener y combinar los datos de frases, consejos y bromas
        items = new ArrayList<>();
        obtenerDatosUnificados();

        // Configurar el Adapter y LayoutManager
        adapter = new Adaptador(this, items);
        recyclerViewFrases.setAdapter(adapter);
        recyclerViewFrases.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú de la Toolbar
        getMenuInflater().inflate(R.menu.menu_savedphrase, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.inicio) {
            Intent intent = new Intent(SavePhrase.this, FortuneCookie.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(SavePhrase.this, FortuneCookie.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.help) {
            Intent intent = new Intent(SavePhrase.this, Help.class);
            startActivity(intent);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    // Método para obtener y combinar frases, consejos y bromas
    private void obtenerDatosUnificados() {
        // Obtener frases
        List<String> frases = dbOpFrases.obtenerFrases();
        items.addAll(frases);

        // Obtener consejos
        List<String> consejos = dbOpConsejos.obtenerConsejos();
        items.addAll(consejos);

        // Obtener bromas
        List<String> bromas = dbOpBromas.obtenerBromas();
        items.addAll(bromas);
    }

    @Override
    // Método para liberar y destruir el sonido al salir de la activity
    protected void onDestroy() {
        super.onDestroy();
        if (sonido != null) {
            sonido.release();
            sonido = null;
        }
    }
}