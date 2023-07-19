package com.example.entrenar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import android.view.ViewGroup;
import android.os.Bundle;
import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TablaDetalleActivity extends Activity {
    private TextView nombreTablaTextView;
    private ListView listView;
    private String nombreTabla; // Variable para almacenar el nombre de la tabla seleccionada

    private SQLiteDatabase database;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> registrosList;
    private HashMap<Integer, Integer> seleccionMap; // Mapa para mantener el número de selección de elementos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_detalle);

        // Obtener el nombre de la tabla de los extras
        nombreTabla = getIntent().getStringExtra("sesionSeleccionada");

        nombreTablaTextView = findViewById(R.id.nombreTablaTextView);
        listView = findViewById(R.id.listView);

        if (nombreTabla != null) {
            // Crear la instancia de AdminSQLiteOpenHelper
            AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(this, "administracion.db", null, 1);
            database = dbHelper.getReadableDatabase();

            // Obtener los detalles de la tabla
            obtenerDetallesTabla(nombreTabla);

            // Mostrar el nombre de la tabla en el TextView correspondiente
            nombreTablaTextView.setText(nombreTabla);
        } else {
            // Mostrar un mensaje de error si el nombre de la tabla es null
            Toast.makeText(this, "Nombre de tabla no válido", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean tablaExiste(String nombreTabla) {
        // Verificar si la tabla ya existe en la base de datos
        Cursor cursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{nombreTabla});
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        return existe;
    }

    private void obtenerDetallesTabla(String nombreTabla) {
        registrosList = new ArrayList<String>();
        seleccionMap = new HashMap<Integer, Integer>(); // Inicializar el mapa de selección

        // Verificar si la tabla existe en la base de datos
        if (!tablaExiste(nombreTabla)) {
            Toast.makeText(this, "La tabla no existe", Toast.LENGTH_SHORT).show();
            return;
        }

        // Consultar la tabla en la base de datos
        Cursor cursor = database.rawQuery("SELECT * FROM `" + nombreTabla + "`", null);

        // Verificar si hay registros en la tabla
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int nSeries = cursor.getInt(cursor.getColumnIndex("n_series"));
                int minutos = cursor.getInt(cursor.getColumnIndex("minutos"));
                int segundos = cursor.getInt(cursor.getColumnIndex("segundos"));
                int repeticionId = cursor.getInt(cursor.getColumnIndex("repeticion_id"));

                String registro = "ID: " + id +
                        "\nNúmero de series: " + nSeries +
                        "\nMinutos: " + minutos +
                        "\nSegundos: " + segundos +
                        "\nID de Repetición: " + repeticionId +
                        "\n";

                registrosList.add(registro);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "La sesión no tiene series.", Toast.LENGTH_SHORT).show();
        }

        // Cerrar el cursor
        cursor.close();

        // Configurar el adaptador para el ListView
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, registrosList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView textView = view.findViewById(android.R.id.text1);

                // Obtener el número de selección para el elemento actual
                int numeroSeleccion = obtenerNumeroSeleccion(position);

                // Establecer el estilo del texto según el estado de selección
                if (numeroSeleccion > 0) {
                    // Si hay un número de selección válido, mostrarlo en el registro
                    String registro = registrosList.get(position);
                    registro += "Selección: " + numeroSeleccion + "\n";

                    SpannableString spannableString = new SpannableString(registro);
                    StyleSpan boldStyleSpan = new StyleSpan(Typeface.BOLD);
                    spannableString.setSpan(boldStyleSpan, registro.indexOf("Selección:"), spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    textView.setText(spannableString);
                } else {
                    // Si no hay selección, mostrar el registro sin formato
                    textView.setText(registrosList.get(position));
                }

                return view;
            }

        };
        listView.setAdapter(adapter);

        // Habilitar la selección de elementos en el ListView
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Marcar el elemento como seleccionado
                listView.setItemChecked(position, true);

                // Actualizar los estados de selección de los elementos
                actualizarEstadosSeleccion(position);
            }
        });
    }

    private int obtenerNumeroSeleccion(int position) {
        // Verificar si el elemento en la posición dada está seleccionado
        if (seleccionMap.containsKey(position)) {
            // Si está seleccionado, obtener el número de selección
            return seleccionMap.get(position);
        }

        // Si no está seleccionado, devolver 0 (valor inválido)
        return 0;
    }

    private void actualizarEstadosSeleccion(int position) {
        // Actualizar los estados de selección de los elementos
        boolean seleccionado = seleccionMap.containsKey(position);
        if (seleccionado) {
            // Si el elemento ya estaba seleccionado, eliminarlo del mapa
            seleccionMap.remove(position);

            // Actualizar los números de selección de los elementos después de la posición modificada
            seleccionMap = actualizarNumerosSeleccionDespuesDeDeseleccion(seleccionMap, position);

            // Actualizar los registros en el ListView
            adapter.notifyDataSetChanged();
        } else {
            // Si el elemento no estaba seleccionado, asignarle un número de selección y actualizar los demás elementos
            seleccionMap.put(position, obtenerSiguienteNumeroSeleccion(seleccionMap));

            // Actualizar los números de selección de los elementos después de la posición modificada
            seleccionMap = actualizarNumerosSeleccionDespuesDeDeseleccion(seleccionMap, position);

            // Actualizar los registros en el ListView
            adapter.notifyDataSetChanged();

            // Si se ha seleccionado un elemento intermedio, deseleccionar los elementos anteriores
            deseleccionarElementosAnteriores(position);
        }
    }

    private void deseleccionarElementosAnteriores(int position) {
        for (int i = 0; i < position; i++) {
            if (seleccionMap.containsKey(i)) {
                seleccionMap.remove(i);
            }
        }

        // Actualizar los números de selección de los elementos después de la deselección
        seleccionMap = actualizarNumerosSeleccionDespuesDeDeseleccion(seleccionMap, position - 1);

        // Actualizar los registros en el ListView
        adapter.notifyDataSetChanged();
    }

    private HashMap<Integer, Integer> actualizarNumerosSeleccionDespuesDeDeseleccion(HashMap<Integer, Integer> map, int position) {
        // Crear una copia del mapa existente para no afectar la iteración
        HashMap<Integer, Integer> nuevoMapa = new HashMap<Integer, Integer>(map);

        // Actualizar los números de selección para los elementos después de la posición modificada
        for (int key : nuevoMapa.keySet()) {
            int numeroSeleccion = nuevoMapa.get(key);
            if (key > position) {
                nuevoMapa.put(key, numeroSeleccion - 1);
            }
        }

        return nuevoMapa;
    }

    private int obtenerSiguienteNumeroSeleccion(HashMap<Integer, Integer> map) {
        // Obtener el número máximo de selección actual
        int maximoNumeroSeleccion = 0;
        for (int numeroSeleccion : map.values()) {
            if (numeroSeleccion > maximoNumeroSeleccion) {
                maximoNumeroSeleccion = numeroSeleccion;
            }
        }

        // Incrementar el número de selección en 1 para obtener el siguiente número
        return maximoNumeroSeleccion + 1;
    }

    private static class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "administracion.db";
        private static final int DATABASE_VERSION = 1;

        public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // No es necesario realizar ninguna acción en este método
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // No es necesario realizar ninguna acción en este método
        }
    }

    public void irASeries(View view) {
        if (nombreTabla != null) {
            // Registra los datos en la tabla seleccionada
            // Aquí puedes implementar la lógica para registrar los datos en la tabla antes de ir a la actividad Series

            // Abrir la actividad Series
            Intent intent = new Intent(this, Series.class);
            intent.putExtra("tablaSeleccionada", nombreTabla);
            startActivity(intent);
        } else {
            // No se ha seleccionado ninguna tabla
            Toast.makeText(this, "No se ha seleccionado una tabla", Toast.LENGTH_SHORT).show();
        }
    }
}
