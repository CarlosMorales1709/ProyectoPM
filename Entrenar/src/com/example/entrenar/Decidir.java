package com.example.entrenar;

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Decidir extends Activity {

	private RelativeLayout mainLayout;
    private View eccentricView;
    private View concentricView;
    private View isometricView;

    private float prevY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decidir);

        mainLayout = findViewById(R.id.mainLayout);
        eccentricView = findViewById(R.id.eccentricView);
        concentricView = findViewById(R.id.concentricView);
        isometricView = findViewById(R.id.isometricView);

        eccentricView.setBackgroundColor(Color.RED);
        concentricView.setBackgroundColor(Color.GREEN);
        isometricView.setBackgroundColor(Color.BLUE);

        eccentricView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handleTouch(v, event);
                return true;
            }
        });

        concentricView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handleTouch(v, event);
                return true;
            }
        });

        isometricView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handleTouch(v, event);
                return true;
            }
        });
    }

    private void handleTouch(View view, MotionEvent event) {
        float currY = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                prevY = currY;
                break;

            case MotionEvent.ACTION_MOVE:
                float dy = currY - prevY;

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                layoutParams.topMargin += dy;
                view.setLayoutParams(layoutParams);

                prevY = currY;
                break;
        }
    }
}