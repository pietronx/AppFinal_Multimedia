package com.example.galleta.Helpers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleta.ControladorBromas.OperarDBBromas;
import com.example.galleta.ControladorConsejos.OperarDBConsejos;
import com.example.galleta.ControladorFrases.OperarDBFrases;
import com.example.galleta.R;

import java.util.List;

// Adaptador para el RecyclerView, que gestiona frases, consejos y bromas
public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder> {

    private final Context context;
    private final List<String> items; // Lista con frases, consejos y bromas
    private final OperarDBFrases dbOpFrases; // Operaciones para la base de datos de frases
    private final OperarDBConsejos dbOpConsejos; // Operaciones para la base de datos de consejos
    private final OperarDBBromas dbOpBromas; // Operaciones para la base de datos de bromas

    // Constructor del adaptador
    public Adaptador(Context context, List<String> items) {
        this.context = context;
        this.items = items;
        this.dbOpFrases = new OperarDBFrases(context);
        this.dbOpConsejos = new OperarDBConsejos(context);
        this.dbOpBromas = new OperarDBBromas(context);
    }

    // Método que se ejecuta cuando se necesita crear una nueva vista para un elemento
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el diseño XML de un ítem en un objeto View
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_frase, parent, false);
        return new ViewHolder(itemView); // Retorna una instancia de ViewHolder con la vista creada
    }

    // Método que asigna datos a una vista cuando aparece en pantalla
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = items.get(position); // Obtiene el elemento en la posición actual
        holder.textoItem.setText(item); // Muestra el texto en el TextView de la vista

        // Establece la acción cuando el usuario presiona el botón de borrar
        holder.borrar.setOnClickListener(v -> {
            if (item.contains("\"")) {
                // Si la cadena contiene comillas, se asume que es una frase y se elimina de la BD
                dbOpFrases.borrarFrase(item);
                Log.d("Adaptador", "Frase eliminada: " + item);

            } else if (item.startsWith("Tip #")) {
                // Si el texto empieza con "Tip #", se trata de un consejo

                // Extraer el ID del consejo
                int id = Integer.parseInt(item.replace("Tip #", "")
                        .split("\n")[0].trim());

                // Eliminar de la base de datos
                dbOpConsejos.borrarConsejo(id);

                // Comprobar si realmente se eliminó
                if (!dbOpConsejos.existeConsejo(id)) {
                    Log.d("Adaptador", "Consejo eliminado de la BD correctamente.");
                } else {
                    Log.e("Adaptador", "Error: El consejo sigue existiendo en la BBDD.");
                }

            } else if (item.startsWith("Broma #")) {
                // Si el texto empieza con "Broma #", se trata de una broma

                // Extraer el ID de la broma
                int idBroma = Integer.parseInt(item.replace("Broma #", "")
                        .split("\n")[0].trim());

                // Eliminar la broma de la base de datos
                dbOpBromas.borrarBroma(idBroma);

                // Comprobar si realmente se eliminó
                if (!dbOpConsejos.existeConsejo(idBroma)) { // Posible error: debería ser dbOpBromas
                    Log.d("Adaptador", "Broma eliminada de la BD correctamente.");
                } else {
                    Log.e("Adaptador", "Error: La broma sigue existiendo en la BBDD.");
                }
            }

            // Eliminar de la lista y actualizar el RecyclerView
            items.remove(position); // Borra el elemento de la lista
            notifyItemRemoved(position); // Notifica al adaptador que un ítem se eliminó
            notifyItemRangeChanged(position, items.size()); // Actualiza las posiciones de los elementos restantes

            // Mensaje emergente para confirmar la eliminación
            Toast.makeText(context, "Elemento eliminado", Toast.LENGTH_SHORT).show();
        });
    }

    // Devuelve la cantidad de elementos en la lista
    @Override
    public int getItemCount() {
        return items.size();
    }

    // Clase interna ViewHolder: mantiene las referencias a las vistas de cada ítem
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textoItem; // Texto de la frase, consejo o broma
        ImageButton borrar; // Botón para eliminar el ítem

        public ViewHolder(View itemView) {
            super(itemView);
            textoItem = itemView.findViewById(R.id.textoFrase); // Asocia el TextView
            borrar = itemView.findViewById(R.id.btnBorrar); // Asocia el botón de borrar
        }
    }
}
