package com.example.entrenar;

import java.util.ArrayList;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Series extends Activity {
    private Spinner spinnerGrupoMuscular;
    private ArrayList<String> registrosList;
    private ArrayList<Integer> registrosIDs;
    private int registroSeleccionadoID = -1;
    private CustomAdapter adapter;
    private EditText editTextSeries;
    private NumberPicker minutesPicker;
    private NumberPicker secondsPicker;
    private static final int MAX_AVISO = 2;
    private int contadorAviso = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);

        if (contadorAviso < MAX_AVISO) {
            mostrarAviso();
            contadorAviso++;
        }
        
        spinnerGrupoMuscular = findViewById(R.id.spinner1);
        editTextSeries = findViewById(R.id.editTextSeries);
        minutesPicker = findViewById(R.id.minutesPicker);
        secondsPicker = findViewById(R.id.secondsPicker);
        
     // Configurar el rango de valores para los NumberPicker
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);

        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);
        // Cargar los grupos musculares en el Spinner
        cargarGruposMusculares();

        // Resto del código...
        Consulta(null);
    }

    private void mostrarAviso() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Texto de advertencia");
        builder.setMessage("La primer serie que guardes sera la primera en comenzar y asi sucesivamente");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acciones a realizar al hacer clic en Aceptar
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    
    public void cargarGruposMusculares() {
        AdminSQLiteOpenHelpener administrador = new AdminSQLiteOpenHelpener(this, "administracion", null, 1);
        SQLiteDatabase bd = administrador.getReadableDatabase();

        String query = "SELECT grupo_muscular FROM rm";
        Cursor cursor = bd.rawQuery(query, null);

        ArrayList<String> gruposMusculares = new ArrayList<String>();

        // Agregar la opción de mostrar todo
        gruposMusculares.add("Mostrar todo");

        if (cursor.moveToFirst()) {
            do {
                String grupoMuscular = cursor.getString(0);
                gruposMusculares.add(grupoMuscular);
            } while (cursor.moveToNext());
        }

        cursor.close();
        bd.close();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gruposMusculares);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrupoMuscular.setAdapter(spinnerAdapter);
    }

    public void Consulta(String filtroGrupoMuscular) {
        AdminSQLiteOpenHelpener administrador = new AdminSQLiteOpenHelpener(this, "administracion", null, 1);
        SQLiteDatabase bd = administrador.getReadableDatabase();

        String query = "SELECT repeticiones.s_e, repeticiones.s_c, repeticiones.s_i, porcentajes.repes, rm.grupo_muscular, repeticiones.kilos, repeticiones.id FROM repeticiones " +
                "INNER JOIN porcentajes ON repeticiones.porcentaje_id = porcentajes.id " +
                "INNER JOIN rm ON repeticiones.rm_id = rm.id";

        if (filtroGrupoMuscular != null && !filtroGrupoMuscular.isEmpty() && !filtroGrupoMuscular.equals("Mostrar todo")) {
            query += " WHERE rm.grupo_muscular LIKE '%" + filtroGrupoMuscular + "%'";
        }

        query += " ORDER BY rm.grupo_muscular ASC, porcentajes.repes ASC"; // Ordenar por grupo_muscular y luego por repeticiones

        Cursor fila = bd.rawQuery(query, null);

        if (fila.moveToFirst()) {
            registrosList = new ArrayList<String>();
            registrosIDs = new ArrayList<Integer>();

            do {
                double s_e = fila.getDouble(0);
                double s_c = fila.getDouble(1);
                double s_i = fila.getDouble(2);
                int numRepeticiones = fila.getInt(3);
                String grupoMuscular = fila.getString(4);
                double kilos = fila.getDouble(5);
                int registroID = fila.getInt(6);

                String registro = "SE: " + s_e + ", SC: " + s_c + ", SI: " + s_i + ", Repeticiones: " + numRepeticiones + ", Musculo: " + grupoMuscular + ", Kilos: " + kilos;
                registrosList.add(registro);
                registrosIDs.add(registroID);

            } while (fila.moveToNext());

            adapter = new CustomAdapter(this, registrosList, registrosIDs);
            final ListView listView = findViewById(R.id.listView1); // Obtener la instancia de ListView
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    registroSeleccionadoID = registrosIDs.get(position);

                    // Actualizar la apariencia visual del elemento seleccionado
                    adapter.setSelectedItem(position);

                    // Notificar al adaptador sobre los cambios realizados
                    adapter.notifyDataSetChanged();
                }
            });

        } else {
            Toast.makeText(this, "No hay registros en la tabla repeticiones", Toast.LENGTH_LONG).show();
        }

        fila.close();
        bd.close();
    }

    public void buscarPorGrupoMuscular(View v) {
        String filtroGrupoMuscular = spinnerGrupoMuscular.getSelectedItem().toString();

        // Realizar la búsqueda por grupo muscular
        Consulta(filtroGrupoMuscular);
    }

    public void registrarDatos(View v) {
        int nSeries = Integer.parseInt(editTextSeries.getText().toString());
        int minutos = minutesPicker.getValue();
        int segundos = secondsPicker.getValue();

        AdminSQLiteOpenHelpener administrador = new AdminSQLiteOpenHelpener(this, "administracion", null, 1);
        SQLiteDatabase bd = administrador.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("n_series", nSeries);
        values.put("minutos", minutos);
        values.put("segundos", segundos);
        values.put("repeticion_id", registroSeleccionadoID);

        long resultado = bd.insert("series", null, values);

        if (resultado == -1) {
            // Error al insertar los datos en la tabla
            Toast.makeText(this, "Error al registrar los datos en la tabla series", Toast.LENGTH_SHORT).show();
        } else {
            // Inserción exitosa
            Toast.makeText(this, "Datos registrados correctamente", Toast.LENGTH_SHORT).show();
        }

        bd.close();
    }

    private static class CustomAdapter extends ArrayAdapter<String> {
        private Context context;
        private ArrayList<String> registrosList;
        private ArrayList<Integer> registrosIDs;
        private int selectedItem = -1; // Variable para almacenar la posición seleccionada

        public CustomAdapter(Context context, ArrayList<String> registrosList, ArrayList<Integer> registrosIDs) {
            super(context, R.layout.list_item_checked, registrosList);
            this.context = context;
            this.registrosList = registrosList;
            this.registrosIDs = registrosIDs;
        }

        public void setSelectedItem(int position) {
            selectedItem = position;
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.list_item_checked, parent, false);
            }

            TextView textView = view.findViewById(android.R.id.text1);
            String registroText = registrosList.get(position);

            // Crear un SpannableString para aplicar estilo al texto seleccionado
            SpannableString spannableString = new SpannableString(registroText);

            if (position == selectedItem) {
                // Aplicar estilo al texto seleccionado
                spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            textView.setText(spannableString);

            return view;
        }
    }
    public void siguiente5(View view){
    	Intent form = new Intent(this, Mostrarserie.class);
    	startActivity(form);
    }
}
