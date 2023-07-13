package com.example.entrenar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/* loaded from: classes.dex */
public class Portada extends Activity {
    private static int SPLASH_TIME_OUT = 3000;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_portada);
        new Handler().postDelayed(new Runnable() { // from class: com.example.entrenar.Portada.1
            @Override // java.lang.Runnable
            public void run() {
                Intent i = new Intent(Portada.this, RM.class);
                Portada.this.startActivity(i);
                Portada.this.finish();
            }
        }, SPLASH_TIME_OUT);
    }
}