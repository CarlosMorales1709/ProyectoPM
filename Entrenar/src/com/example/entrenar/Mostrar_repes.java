package com.example.entrenar;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Mostrar_repes extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostrar_repes);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mostrar_repes, menu);
		return true;
	}

}
