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
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Mostrar_repes extends Activity {
    private EditText scc, see, sii;
    private int registroSeleccionadoID = -1;
    private CustomAdapter adapter;
    private ArrayList<String> registrosList;
    private ArrayList<Integer> registrosIDs;
    private Spinner spinnerGrupoMuscular;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_repes);

        // Inicializar los objetos EditText
        scc = findViewById(R.id.scw);
        see = findViewById(R.id.sew);
        sii = findViewById(R.id.siw);
        spinnerGrupoMuscular = findViewById(R.id.spinnerGrupoMuscular);

        // Cargar los grupos musculares en el Spinner
        cargarGruposMusculares();

        // Resto del código...
        Consulta(null);
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
                double porcentaje = fila.getDouble(3);
                double rm = fila.getDouble(6);
                kilos = porcentaje * rm;

                // Actualizar el campo "kilos" en la tabla "repeticiones"
                ContentValues valores = new ContentValues();
                valores.put("kilos", kilos);
                bd.update("repeticiones", valores, "id = ?", new String[]{String.valueOf(registroID)});


            } while (fila.moveToNext());

            adapter = new CustomAdapter(this, registrosList, registrosIDs);
            final ListView listView = findViewById(R.id.listView); // Obtener la instancia de ListView
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

    // Resto del código...

    public void borrarRegistro(View v) {
        if (registroSeleccionadoID != -1) {
            AdminSQLiteOpenHelpener administrador = new AdminSQLiteOpenHelpener(this, "administracion", null, 1);
            SQLiteDatabase bd = administrador.getWritableDatabase();

            bd.delete("repeticiones", "id = ?", new String[]{String.valueOf(registroSeleccionadoID)});

            registrosList.remove(registrosIDs.indexOf(registroSeleccionadoID));
            registrosIDs.remove(registrosIDs.indexOf(registroSeleccionadoID));
            adapter.notifyDataSetChanged();

            bd.close();
        }
    }


    public void actualizarRegistro(View view) {
        if (registroSeleccionadoID != -1) {
            String seText = see.getText().toString();
            String scText = scc.getText().toString();
            String siText = sii.getText().toString();

            if (!TextUtils.isEmpty(seText) && !TextUtils.isEmpty(scText) && !TextUtils.isEmpty(siText)) {
                float s_e = Float.parseFloat(seText);
                float s_c = Float.parseFloat(scText);
                float s_i = Float.parseFloat(siText);

                AdminSQLiteOpenHelpener administrador = new AdminSQLiteOpenHelpener(this, "administracion", null, 1);
                SQLiteDatabase bd = administrador.getWritableDatabase();

                // Actualizar los datos del registro seleccionado en la tabla repeticiones
                ContentValues valores = new ContentValues();
                valores.put("s_e", s_e);
                valores.put("s_c", s_c);
                valores.put("s_i", s_i);
                bd.update("repeticiones", valores, "id = ?", new String[]{String.valueOf(registroSeleccionadoID)});

                bd.close();

                // Volver a cargar los datos actualizados
                Consulta(null);

                Toast.makeText(this, "Registro actualizado exitosamente", Toast.LENGTH_LONG).show();
                see.setText("");
                scc.setText("");
                sii.setText("");
            } else {
                Toast.makeText(this, "Debes ingresar valores en los campos de texto", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Ningún registro seleccionado", Toast.LENGTH_LONG).show();
        }
    }

    public void Actualizar(View view) {
        if (registroSeleccionadoID != -1) {
            AdminSQLiteOpenHelpener administrador = new AdminSQLiteOpenHelpener(this, "administracion", null, 1);
            SQLiteDatabase bd = administrador.getWritableDatabase();

            // Obtener los datos del registro seleccionado en la tabla repeticiones
            Cursor fila = bd.rawQuery("SELECT s_e, s_c, s_i FROM repeticiones WHERE id = ?", new String[]{String.valueOf(registroSeleccionadoID)});

            if (fila.moveToFirst()) {
                String seText = fila.getString(0);
                String scText = fila.getString(1);
                String siText = fila.getString(2);

                // Mostrar los datos del registro en los EditText
                see.setText(seText);
                scc.setText(scText);
                sii.setText(siText);

                // Cambiar el texto del botón de "Alta" a "Actualizar"
                Button btnAlta2 = findViewById(R.id.btnAlta221);
                btnAlta2.setText("Actualizar registro");
            }

            fila.close();
            bd.close();
        } else {
            Toast.makeText(this, "Ningún registro seleccionado", Toast.LENGTH_LONG).show();
        }
    }



   

    public void siguiente4(View view){
        Intent form = new Intent(this, RM.class);
        startActivity(form);
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
}
