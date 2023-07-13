package com.example.entrenar;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/* loaded from: classes.dex */
public class Repeticiones extends Activity {
    private int registroSeleccionadoID = -1;
    private int registroSeleccionadoIDPorcentaje = -1;
    private int registroSeleccionadoIDRM = -1;
    private EditText s_c;
    private EditText s_e;
    private EditText s_i;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeticiones);
        this.s_c = (EditText) findViewById(R.id.editTextSC);
        this.s_e = (EditText) findViewById(R.id.editTextSE);
        this.s_i = (EditText) findViewById(R.id.editTextSI);
        Consulta(null);
        Consulta2(null);
    }

    public void Consulta(View v) {
        AdminSQLiteOpenHelpener administrador = new AdminSQLiteOpenHelpener(this, "administracion", null, 1);
        SQLiteDatabase bd = administrador.getReadableDatabase();
        Cursor fila = bd.rawQuery("SELECT grupo_muscular, rm, ID FROM rm ORDER BY grupo_muscular ASC", null);
        if (fila.moveToFirst()) {
            ArrayList<String> registrosList = new ArrayList<String>();
            final ArrayList<Integer> registrosIDs = new ArrayList<Integer>();
            do {
                String grupoMuscular = fila.getString(0);
                int rm = fila.getInt(1);
                int registroID = fila.getInt(2);
                String registro = "Grupo Muscular: " + grupoMuscular + ", RM: " + rm;
                registrosList.add(registro);
                registrosIDs.add(Integer.valueOf(registroID));
            } while (fila.moveToNext());
            final CustomAdapter adapter = new CustomAdapter(this, registrosList, registrosIDs);
            ListView listView = (ListView) findViewById(R.id.listViewRM);
            listView.setAdapter((ListAdapter) adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.example.entrenar.Repeticiones.1
                @Override // android.widget.AdapterView.OnItemClickListener
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Repeticiones.this.registroSeleccionadoIDRM = ((Integer) registrosIDs.get(position)).intValue();
                    adapter.setSelectedItem(position);
                    adapter.notifyDataSetChanged();
                }
            });
        } else {
            Toast.makeText(this, "No hay registros en la tabla rm", 1).show();
        }
        fila.close();
        bd.close();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CustomAdapter extends ArrayAdapter<String> {
        private Context context;
        private ArrayList<Integer> registrosIDs;
        private ArrayList<String> registrosList;
        private int selectedItem;

        public CustomAdapter(Context context, ArrayList<String> registrosList, ArrayList<Integer> registrosIDs) {
            super(context, (int) R.layout.list_item_checked, registrosList);
            this.selectedItem = -1;
            this.context = context;
            this.registrosList = registrosList;
            this.registrosIDs = registrosIDs;
        }

        public void setSelectedItem(int position) {
            this.selectedItem = position;
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(this.context);
                view = inflater.inflate(R.layout.list_item_checked, parent, false);
            }
            TextView textView = (TextView) view.findViewById(16908308);
            String registroText = this.registrosList.get(position);
            SpannableString spannableString = new SpannableString(registroText);
            if (position == this.selectedItem) {
                spannableString.setSpan(new ForegroundColorSpan(-16776961), 0, spannableString.length(), 33);
            } else {
                spannableString.setSpan(new ForegroundColorSpan(-16777216), 0, spannableString.length(), 33);
            }
            textView.setText(spannableString);
            return view;
        }
    }

    public void Consulta2(View v) {
        AdminSQLiteOpenHelpener administrador = new AdminSQLiteOpenHelpener(this, "administracion", null, 1);
        SQLiteDatabase bd = administrador.getReadableDatabase();
        Cursor fila = bd.rawQuery("SELECT porcentaje, repes, id FROM porcentajes ORDER BY repes ASC", null);
        if (fila.moveToFirst()) {
            ArrayList<String> registrosList = new ArrayList<String>();
            final ArrayList<Integer> registrosIDs = new ArrayList<Integer>();
            do {
                String porcentajeText = fila.getString(0);
                String repesText = fila.getString(1);
                int registroID = fila.getInt(2);
                String registro = "Porcentaje: " + porcentajeText + ", Repeticiones: " + repesText;
                registrosList.add(registro);
                registrosIDs.add(Integer.valueOf(registroID));
            } while (fila.moveToNext());
            final CustomAdapter2 adapter = new CustomAdapter2(this, registrosList, registrosIDs);
            ListView listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter((ListAdapter) adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.example.entrenar.Repeticiones.2
                @Override // android.widget.AdapterView.OnItemClickListener
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Repeticiones.this.registroSeleccionadoIDPorcentaje = ((Integer) registrosIDs.get(position)).intValue();
                    adapter.setSelectedItem(position);
                    adapter.notifyDataSetChanged();
                }
            });
        } else {
            Toast.makeText(this, "No hay registros en la tabla porcentajes", 1).show();
        }
        fila.close();
        bd.close();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CustomAdapter2 extends ArrayAdapter<String> {
        private Context context;
        private ArrayList<Integer> registrosIDs;
        private ArrayList<String> registrosList;
        private int selectedItem;

        public CustomAdapter2(Context context, ArrayList<String> registrosList, ArrayList<Integer> registrosIDs) {
            super(context, (int) R.layout.list_item_checked, registrosList);
            this.selectedItem = -1;
            this.context = context;
            this.registrosList = registrosList;
            this.registrosIDs = registrosIDs;
        }

        public void setSelectedItem(int position) {
            this.selectedItem = position;
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(this.context);
                view = inflater.inflate(R.layout.list_item_checked, parent, false);
            }
            TextView textView = (TextView) view.findViewById(16908308);
            String registroText = this.registrosList.get(position);
            SpannableString spannableString = new SpannableString(registroText);
            if (position == this.selectedItem) {
                spannableString.setSpan(new ForegroundColorSpan(-16776961), 0, spannableString.length(), 33);
            } else {
                spannableString.setSpan(new ForegroundColorSpan(-16777216), 0, spannableString.length(), 33);
            }
            textView.setText(spannableString);
            return view;
        }
    }

    public void insert(View view) {
        if (this.registroSeleccionadoIDRM != -1 && this.registroSeleccionadoIDPorcentaje != -1) {
            String s_cText = this.s_c.getText().toString();
            String s_eText = this.s_e.getText().toString();
            String s_iText = this.s_i.getText().toString();
            if (!s_cText.isEmpty() && !s_eText.isEmpty() && !s_iText.isEmpty()) {
                int rm_id = this.registroSeleccionadoIDRM;
                int porcentaje_id = this.registroSeleccionadoIDPorcentaje;
                double kilos = calcularKilos(porcentaje_id, rm_id);
                insertarRepeticion(s_cText, s_eText, s_iText, rm_id, kilos, porcentaje_id);
                this.s_c.setText("");
                this.s_e.setText("");
                this.s_i.setText("");
                ListView rmListView = (ListView) findViewById(R.id.listViewRM);
                rmListView.clearChoices();
                rmListView.requestLayout();
                ListView porcentajeListView = (ListView) findViewById(R.id.listView);
                porcentajeListView.clearChoices();
                porcentajeListView.requestLayout();
                Toast.makeText(this, "Datos insertados correctamente", 0).show();
                return;
            }
            Toast.makeText(this, "Por favor, complete todos los campos", 0).show();
            return;
        }
        Toast.makeText(this, "Por favor, seleccione un registro de cada lista", 0).show();
    }

    private double calcularKilos(int porcentaje_id, int rm_id) {
        AdminSQLiteOpenHelpener administrador = new AdminSQLiteOpenHelpener(this, "administracion", null, 1);
        SQLiteDatabase bd = administrador.getReadableDatabase();
        Cursor fila = bd.rawQuery("SELECT porcentaje FROM porcentajes WHERE id = " + porcentaje_id, null);
        double porcentaje = 0.0d;
        if (fila.moveToFirst()) {
            porcentaje = fila.getDouble(0);
        }
        fila.close();
        bd.close();
        SQLiteDatabase bd2 = administrador.getReadableDatabase();
        Cursor fila2 = bd2.rawQuery("SELECT rm FROM rm WHERE id = " + rm_id, null);
        int rm = 0;
        if (fila2.moveToFirst()) {
            rm = fila2.getInt(0);
        }
        fila2.close();
        bd2.close();
        return rm * porcentaje;
    }

    private void insertarRepeticion(String s_cText, String s_eText, String s_iText, int rm_id, double kilos, int porcentaje_id) {
        AdminSQLiteOpenHelpener administrador = new AdminSQLiteOpenHelpener(this, "administracion", null, 1);
        SQLiteDatabase bd = administrador.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("s_c", s_cText);
        valores.put("s_e", s_eText);
        valores.put("s_i", s_iText);
        valores.put("rm_id", Integer.valueOf(rm_id));
        valores.put("kilos", Double.valueOf(kilos));
        valores.put("porcentaje_id", Integer.valueOf(porcentaje_id));
        bd.insert("repeticiones", null, valores);
        bd.close();
    }

    public void siguiente4(View view) {
        Intent form = new Intent(this, Mostrar_repes.class);
        startActivity(form);
    }
}