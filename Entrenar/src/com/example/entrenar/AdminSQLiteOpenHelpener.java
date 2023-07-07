package com.example.entrenar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* loaded from: classes.dex */
public class AdminSQLiteOpenHelpener extends SQLiteOpenHelper {
    public AdminSQLiteOpenHelpener(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE rm (id INTEGER PRIMARY KEY AUTOINCREMENT, grupo_muscular TEXT, rm REAL)");
        db.execSQL("CREATE TABLE porcentajes (id INTEGER PRIMARY KEY AUTOINCREMENT, porcentaje REAL, repes REAL)");
        db.execSQL("CREATE TABLE repeticiones (id INTEGER PRIMARY KEY AUTOINCREMENT, s_e REAL, s_c REAL, s_i REAL, rm_id INTEGER, kilos REAL, porcentaje_id INTEGER, FOREIGN KEY(rm_id) REFERENCES rm(id) ON UPDATE CASCADE, FOREIGN KEY(porcentaje_id) REFERENCES porcentajes(id) ON UPDATE CASCADE)");
        db.execSQL("CREATE TABLE series (id INTEGER PRIMARY KEY AUTOINCREMENT, n_series INTEGER, minutos INTEGER, segundos INTEGER, repeticion_id INTEGER, FOREIGN KEY(repeticion_id) REFERENCES repeticiones(id))");
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        db.execSQL("DROP TABLE IF EXISTS rm");
        db.execSQL("DROP TABLE IF EXISTS porcentajes");
        db.execSQL("DROP TABLE IF EXISTS repeticiones");
        db.execSQL("DROP TABLE IF EXISTS series");
        onCreate(db);
    }
}