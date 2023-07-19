package com.example.entrenar;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Respaldo {
/*public class TablaDetalleActivity extends Activity {
    private TextView nombreTablaTextView;
    private ListView listView;
    private String nombreTabla; // Variable para almacenar el nombre de la tabla seleccionada

    private SQLiteDatabase database;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> registrosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_detalle);

        // Obtener el nombre de la tabla de los extras
        nombreTabla = getIntent().getStringExtra("sesionSeleccionada");

        nombreTablaTextView = findViewById(R.id.nombreTablaTextView);
        listView = findViewById(R.id.listView);

        if (nombreTabla != null) {
            // Crear la instancia de AdminSQLiteOpenHelpener
            AdminSQLiteOpenHelpener dbHelper = new AdminSQLiteOpenHelpener(this, "administracion.db", null, 1);
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
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, registrosList);
        listView.setAdapter(adapter);
    }



    // Clase auxiliar para crear y actualizar la base de datos
    private static class AdminSQLiteOpenHelpener extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "administracion.db";
        private static final int DATABASE_VERSION = 1;

        public AdminSQLiteOpenHelpener(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
*/
}
