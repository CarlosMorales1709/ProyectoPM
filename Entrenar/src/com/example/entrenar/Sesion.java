package com.example.entrenar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

/* loaded from: classes.dex */
public class Sesion extends Activity {
    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion);
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sesion, menu);
        return true;
    }
}