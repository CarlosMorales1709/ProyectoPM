package com.example.entrenar;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;


public class RM extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rm);
	}
    public void siguiente(View view){
    	Intent form = new Intent(this, RM2.class);
    	startActivity(form);
    }
    
    public void siguiente2(View view){
    	Intent form = new Intent(this, Repeticiones.class);
    	startActivity(form);
    }
    
    public void siguiente3(View view){
    	Intent form = new Intent(this, Porcentaje_repeticiones.class);
    	startActivity(form);
    }
    
    public void siguiente4(View view){
    	Intent form = new Intent(this, Series.class);
    	startActivity(form);
    }
    
  
    

}
