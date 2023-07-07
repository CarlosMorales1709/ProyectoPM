package com.example.entrenar;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Porcentaje_repeticiones extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_porcentaje_repeticiones);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.porcentaje_repeticiones, menu);
		return true;
	}

}
