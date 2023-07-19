package com.example.entrenar;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/* loaded from: classes.dex */

public class Sesion extends Activity {
    private EditText nombreSesionEditText;
    private Button crearSesionButton;
    private Button borrarSerieButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    private List<String> administracion;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion);

        nombreSesionEditText = findViewById(R.id.nombreSesionEditText);
        crearSesionButton = findViewById(R.id.crearSesionButton);
        borrarSerieButton = findViewById(R.id.borrarSerieButton);
        listView = findViewById(R.id.listView);

        administracion = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, administracion);
        listView.setAdapter(adapter);

        // Crear la base de datos y la tabla
        database = new AdminSQLiteOpenHelpener(this, "administracion.db", null, 1).getWritableDatabase();

        // Cargar las administracion existentes
        cargaradministracion();

        crearSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreSesion = nombreSesionEditText.getText().toString().trim();
                if (!nombreSesion.isEmpty()) {
                    crearTabla(nombreSesion);
                    administracion.add(nombreSesion);
                    adapter.notifyDataSetChanged();
                    nombreSesionEditText.setText("");
                } else {
                    Toast.makeText(Sesion.this, "Ingresa un nombre de sesión válido", Toast.LENGTH_SHORT).show();
                }
            }
        });

        borrarSerieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarSerie();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtén el elemento seleccionado
                String sesionSeleccionada = (String) parent.getItemAtPosition(position);

                // Cambia el color de fondo del elemento seleccionado
                view.setSelected(true);

                // Crear el Intent para la nueva actividad
                Intent intent = new Intent(Sesion.this, TablaDetalleActivity.class);

                // Pasar el nombre de la sesión seleccionada como dato extra en el Intent
                intent.putExtra("sesionSeleccionada", sesionSeleccionada);

                // Iniciar la nueva actividad
                startActivity(intent);
            }
        });
    }

    private void cargaradministracion() {
        AdminSQLiteOpenHelpener dbHelper = new AdminSQLiteOpenHelpener(this, "administracion.db", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'android_%' AND name != 'sqlite_sequence' AND name NOT IN ('rm', 'porcentajes', 'repeticiones')", null);

        if (cursor.moveToFirst()) {
            do {
                String tableName = cursor.getString(0);
                String sesion = tableName;
                administracion.add(sesion);
            } while (cursor.moveToNext());
        }

        cursor.close();
        adapter.notifyDataSetChanged();
    }


    private void crearTabla(String nombreSesion) {
        // Reemplazar espacios con guiones bajos y caracteres no alfanuméricos con guion bajo
        String nombreTabla = nombreSesion.replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9_]", "_");

        if (tablaExiste(nombreTabla)) {
            Toast.makeText(this, "El nombre de sesión ya existe", Toast.LENGTH_SHORT).show();
        } else {
            String tablaSQL = "CREATE TABLE IF NOT EXISTS `" + nombreTabla + "` (id INTEGER PRIMARY KEY AUTOINCREMENT, n_series INTEGER, minutos INTEGER, segundos INTEGER, repeticion_id INTEGER, FOREIGN KEY(repeticion_id) REFERENCES repeticiones(id))";
            database.execSQL(tablaSQL);
            Toast.makeText(this, "Se creó la tabla: " + nombreSesion, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean tablaExiste(String nombreTabla) {
        // Verificar si la tabla ya existe en la base de datos
        Cursor cursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{nombreTabla});
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        return existe;
    }

    private void borrarSerie() {
        // Lógica para borrar una serie de la tabla
        // Aquí puedes implementar la funcionalidad que necesites para borrar una serie específica
        Toast.makeText(this, "Se borró una serie", Toast.LENGTH_SHORT).show();
    }

    private static class AdminSQLiteOpenHelpener extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "administracion.db";
        private static final int DATABASE_VERSION = 1;

        public AdminSQLiteOpenHelpener(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE rm (id INTEGER PRIMARY KEY AUTOINCREMENT, grupo_muscular TEXT, rm REAL)");
            db.execSQL("CREATE TABLE porcentajes (id INTEGER PRIMARY KEY AUTOINCREMENT, porcentaje REAL, repes REAL)");
            db.execSQL("CREATE TABLE repeticiones (id INTEGER PRIMARY KEY AUTOINCREMENT, s_e REAL, s_c REAL, s_i REAL, rm_id INTEGER, kilos REAL, porcentaje_id INTEGER, FOREIGN KEY(rm_id) REFERENCES rm(id) ON UPDATE CASCADE, FOREIGN KEY(porcentaje_id) REFERENCES porcentajes(id) ON UPDATE CASCADE)");
            db.execSQL("CREATE TABLE series (id INTEGER PRIMARY KEY AUTOINCREMENT, n_series INTEGER, minutos INTEGER, segundos INTEGER, repeticion_id INTEGER, FOREIGN KEY(repeticion_id) REFERENCES repeticiones(id))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
            db.execSQL("DROP TABLE IF EXISTS rm");
            db.execSQL("DROP TABLE IF EXISTS porcentajes");
            db.execSQL("DROP TABLE IF EXISTS repeticiones");
            db.execSQL("DROP TABLE IF EXISTS series");
            onCreate(db);
        }
    }
}
