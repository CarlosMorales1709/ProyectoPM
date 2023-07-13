package com.example.entrenar;

import java.util.ArrayList;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;




import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RM2 extends Activity {
	EditText grupo_muscular, rm;
	Spinner spinner;
	ListView listView;
	private int registroSeleccionadoID = -1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rm2);
		
		grupo_muscular = (EditText)findViewById(R.id.grupo_muscular);
		rm = (EditText)findViewById(R.id.rm);
		listView = (ListView) findViewById(R.id.listView);
		Consulta(null);
	}
	public boolean onOptionsItemSelected(MenuItem item){
    	int id = item.getItemId();
    	if(id == R.id.action_settings){
    		return true;
    	}
    	return super.onOptionsItemSelected(item);
    }

	public void Alta(View view) {
	    AdminSQLiteOpenHelpener administrador = new AdminSQLiteOpenHelpener(this, "administracion", null, 1);
	    SQLiteDatabase bd = administrador.getWritableDatabase();

	    String grupoMuscular = grupo_muscular.getText().toString();
	    String peso = rm.getText().toString();

	    if (registroSeleccionadoID != -1) {
	        // Actualizar el registro seleccionado
	        ContentValues registro = new ContentValues();
	        registro.put("grupo_muscular", grupoMuscular);
	        registro.put("rm", peso);

	        int filasActualizadas = bd.update("rm", registro, "ID = ?", new String[]{String.valueOf(registroSeleccionadoID)});

	        if (filasActualizadas > 0) {
	            Toast.makeText(this, "Registro actualizado exitosamente", Toast.LENGTH_LONG).show();

	            // Reiniciar los EditText y cambiar el texto del botón de "Actualizar" a "Alta"
	            grupo_muscular.setText("");
	            rm.setText("");
	            Button btnAlta = findViewById(R.id.btnInsertar);
	            btnAlta.setText("Guardar");

	            Consulta(null);
	        } else {
	            Toast.makeText(this, "Error al actualizar el registro", Toast.LENGTH_LONG).show();
	        }

	        // Reiniciar el registro seleccionado
	        registroSeleccionadoID = -1;
	    } else {
	        // Verificar si el grupo muscular ya existe en la base de datos
	        Cursor fila = bd.rawQuery("SELECT grupo_muscular FROM rm WHERE grupo_muscular = ?", new String[]{grupoMuscular});
	        if (fila.moveToFirst()) {
	            Toast.makeText(this, "El grupo muscular ya existe", Toast.LENGTH_LONG).show();
	        } else {
	            ContentValues registro = new ContentValues();
	            registro.put("grupo_muscular", grupoMuscular);
	            registro.put("rm", peso);

	            long x = bd.insert("rm", null, registro);
	            if (x != -1) {
	                Toast.makeText(this, "Se cargaron los datos", Toast.LENGTH_LONG).show();

	                // Reiniciar los EditText
	                grupo_muscular.setText("");
	                rm.setText("");
	                
	                Consulta(null);
	            } else {
	                Toast.makeText(this, "Hubo un error al cargar los datos", Toast.LENGTH_LONG).show();
	            }
	        }

	        fila.close();
	    }

	    bd.close();
	}




    
  /*  public void Consulta(View v){
        AdminSQLiteOpenHelpener administrador = new AdminSQLiteOpenHelpener(this,"administracion",null,1);
        SQLiteDatabase bd=administrador.getReadableDatabase();
        
        String codigo = Codigo.getText().toString();
        
        Cursor fila=bd.rawQuery("select nombre, apellido, edad from persona where codigo="+codigo+"" ,  null);
        
        if(fila.moveToFirst()){
            Nombre.setText(fila.getString(0));
            Apellido.setText(fila.getString(1));
            Edad.setText(fila.getString(2));
        }else{
            Toast.makeText(this, "No existe una persona con dicho codigo", Toast.LENGTH_LONG).show();
        }
        bd.close();
    }
	*/
    
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
	            registrosIDs.add(registroID);

	        } while (fila.moveToNext());

	        final CustomAdapter adapter = new CustomAdapter(this, registrosList, registrosIDs);
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
	        Toast.makeText(this, "No hay registros en la tabla rm", Toast.LENGTH_LONG).show();
	    }

	    fila.close();
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
	        } else {
	            // Restablecer el estilo del texto no seleccionado
	            spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	        }

	        textView.setText(spannableString);

	        return view;
	    }
	}




	public void borrarRegistro(View view) {
	    if (registroSeleccionadoID != -1) {
	        AdminSQLiteOpenHelpener administrador = new AdminSQLiteOpenHelpener(this, "administracion", null, 1);
	        SQLiteDatabase bd = administrador.getWritableDatabase();

	        // Borrar el registro seleccionado utilizando el ID almacenado
	        int filasBorradas = bd.delete("rm", "ID = ?", new String[]{String.valueOf(registroSeleccionadoID)});

	        if (filasBorradas > 0) {
	            Toast.makeText(this, "Registro borrado exitosamente", Toast.LENGTH_LONG).show();
	            Consulta(null);
	        } else {
	            Toast.makeText(this, "Error al borrar el registro", Toast.LENGTH_LONG).show();
	            Consulta(null);
	        }

	        bd.close();
	    } else {
	        Toast.makeText(this, "Ningún registro seleccionado", Toast.LENGTH_LONG).show();
	    }
	}

	public void Actualizar(View view) {
	    if (registroSeleccionadoID != -1) {
	        AdminSQLiteOpenHelpener administrador = new AdminSQLiteOpenHelpener(this, "administracion", null, 1);
	        SQLiteDatabase bd = administrador.getWritableDatabase();

	        // Obtener los datos del registro seleccionado
	        Cursor fila = bd.rawQuery("SELECT grupo_muscular, rm FROM rm WHERE ID = ?", new String[]{String.valueOf(registroSeleccionadoID)});

	        if (fila.moveToFirst()) {
	            String grupoMuscular = fila.getString(0);
	            int rm = fila.getInt(1);

	            // Mostrar los datos del registro en los EditText
	            grupo_muscular.setText(grupoMuscular);
	            this.rm.setText(String.valueOf(rm));

	            // Cambiar el texto del botón de "Alta" a "Actualizar"
	            Button btnAlta = findViewById(R.id.btnInsertar);
	            btnAlta.setText("Actualizar");
	        }

	        fila.close();
	        bd.close();
	    } else {
	        Toast.makeText(this, "Ningún registro seleccionado", Toast.LENGTH_LONG).show();
	    }
	}


}
