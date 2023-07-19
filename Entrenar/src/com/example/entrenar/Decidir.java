package com.example.entrenar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Decidir extends Activity {

    private CheckBox concentricaCheckBox;
    private CheckBox excentricaCheckBox;
    private CheckBox isometricaCheckBox;

    private List<String> ordenFases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decidir);

        concentricaCheckBox = findViewById(R.id.concentricaCheckBox);
        excentricaCheckBox = findViewById(R.id.excentricaCheckBox);
        isometricaCheckBox = findViewById(R.id.isometricaCheckBox);

        Button ordenButton = findViewById(R.id.ordenButton);

        ordenFases = new ArrayList<String>();

        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String faseSeleccionada = getFaseSeleccionada(buttonView);
                if (isChecked) {
                    if (!ordenFases.contains(faseSeleccionada)) {
                        ordenFases.add(faseSeleccionada);
                        Toast.makeText(Decidir.this, "Fase seleccionada: " + faseSeleccionada, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ordenFases.remove(faseSeleccionada);
                }
                actualizarNumerosTextViews();
            }
        };

        concentricaCheckBox.setOnCheckedChangeListener(checkedChangeListener);
        excentricaCheckBox.setOnCheckedChangeListener(checkedChangeListener);
        isometricaCheckBox.setOnCheckedChangeListener(checkedChangeListener);

        ordenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ordenFases.isEmpty()) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Decidir.this, android.R.layout.simple_list_item_1, ordenFases);
                    ListView listView = findViewById(R.id.listView);
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(Decidir.this, "No se ha seleccionado ningún orden de fase.", Toast.LENGTH_SHORT).show();
                }

                concentricaCheckBox.setChecked(false);
                excentricaCheckBox.setChecked(false);
                isometricaCheckBox.setChecked(false);
                ordenFases.clear();
            }
        });
    }

    private String getFaseSeleccionada(CompoundButton checkBox) {
        switch (checkBox.getId()) {
            case R.id.concentricaCheckBox:
                return "Fase Concéntrica";
            case R.id.excentricaCheckBox:
                return "Fase Excéntrica";
            case R.id.isometricaCheckBox:
                return "Fase Isométrica";
            default:
                return "";
        }
    }

    private void actualizarNumerosTextViews() {
        TextView numeroConcentricaTextView = findViewById(R.id.numeroConcentricaTextView);
        TextView numeroExcentricaTextView = findViewById(R.id.numeroExcentricaTextView);
        TextView numeroIsometricaTextView = findViewById(R.id.numeroIsometricaTextView);

        numeroConcentricaTextView.setText(ordenFases.contains("Fase Concéntrica") ? String.valueOf(ordenFases.indexOf("Fase Concéntrica") + 1) : "");
        numeroExcentricaTextView.setText(ordenFases.contains("Fase Excéntrica") ? String.valueOf(ordenFases.indexOf("Fase Excéntrica") + 1) : "");
        numeroIsometricaTextView.setText(ordenFases.contains("Fase Isométrica") ? String.valueOf(ordenFases.indexOf("Fase Isométrica") + 1) : "");
    }
}