package com.example.entrenar;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Mostrarserie extends Activity {
    private ArrayList<Integer> registrosIDs;
    private CustomAdapter adapter;
    private int registroSeleccionadoID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrarserie);

        Consulta();

        Button btnBorrar = findViewById(R.id.btnBorrar2);
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarRegistros();
            }
        });
    }

    public void siguiente3(View view) {
        Intent form = new Intent(this, Series.class);
        startActivity(form);
    }

    public void siguiente4(View view) {
        Intent form = new Intent(this, Decidir.class);
        startActivity(form);
    }

    public void Consulta() {
        AdminSQLiteOpenHelpener administrador = new AdminSQLiteOpenHelpener(this, "administracion", null, 1);
        SQLiteDatabase bd = administrador.getReadableDatabase();

        Cursor fila = bd.rawQuery("SELECT id, n_series, minutos, segundos FROM series", null);

        if (fila.moveToFirst()) {
            ArrayList<String> registrosList = new ArrayList<String>();
            registrosIDs = new ArrayList<Integer>();
            int numeroRegistro = 1;

            do {
                int id = fila.getInt(0);
                int n_series = fila.getInt(1);
                int minutos = fila.getInt(2);
                int segundos = fila.getInt(3);

                String registro = numeroRegistro + ", Número de series: " + n_series + ", Descanso: " + minutos + " minutos " + segundos + " segundos";
                registrosList.add(registro);
                registrosIDs.add(id);

                numeroRegistro++;

            } while (fila.moveToNext());

            adapter = new CustomAdapter(this, registrosList, registrosIDs);
            final ListView listView = findViewById(R.id.listView1);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    registroSeleccionadoID = registrosIDs.get(position);
                    adapter.setSelectedItem(position);
                    adapter.notifyDataSetChanged();
                }
            });

        } else {
            Toast.makeText(this, "No hay registros en la tabla series", Toast.LENGTH_LONG).show();
        }

        fila.close();
        bd.close();
    }



    private void borrarRegistros() {
        if (registroSeleccionadoID != -1) {
            AdminSQLiteOpenHelpener administrador = new AdminSQLiteOpenHelpener(this, "administracion", null, 1);
            SQLiteDatabase bd = administrador.getWritableDatabase();

            int filasBorradas = bd.delete("series", "id = ?", new String[]{String.valueOf(registroSeleccionadoID)});

            if (filasBorradas > 0) {
                Toast.makeText(this, "Registro borrado exitosamente", Toast.LENGTH_LONG).show();
                Consulta();
            } else {
                Toast.makeText(this, "Error al borrar el registro", Toast.LENGTH_LONG).show();
            }

            bd.close();
            registroSeleccionadoID = -1;
            adapter.setSelectedItem(-1);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Ningún registro seleccionado", Toast.LENGTH_LONG).show();
        }
    }

    private static class CustomAdapter extends ArrayAdapter<String> {
        private Context context;
        private ArrayList<String> registrosList;
        private ArrayList<Integer> registrosIDs;
        private int selectedItem = -1;

        public CustomAdapter(Context context, ArrayList<String> registrosList, ArrayList<Integer> registrosIDs) {
            super(context, R.layout.list_item_checked, registrosList);
            this.context = context;
            this.registrosList = registrosList;
            this.registrosIDs = registrosIDs;
        }

        public void setSelectedItem(int position) {
            selectedItem = position;
        }

        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.list_item_checked, parent, false);
            }

            TextView textView = view.findViewById(android.R.id.text1);
            String registroText = registrosList.get(position);

            SpannableString spannableString = new SpannableString(registroText);

            if (position == selectedItem) {
                spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            textView.setText(spannableString);

            return view;
        }

    }
}
