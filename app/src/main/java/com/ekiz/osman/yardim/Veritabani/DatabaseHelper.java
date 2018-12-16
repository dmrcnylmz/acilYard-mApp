package com.ekiz.osman.yardim.Veritabani;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "yardim.db";
    public static final String TABLE_NAME = "yardim_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "TC";
    public static final String COL3 = "TELEFON";
    public static final String COL4 = "ENLEM";
    public static final String COL5 = "BOYLAM";
    public static final String COL6 = "ACILDURUM";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " TC TEXT, TELEFON TEXT, ENLEM TEXT, BOYLAM TEXT, ACILDURUM TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String Tc, String Telefon, String Enlem, String Boylam, String Acildurum) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, Tc);
        contentValues.put(COL3, Telefon);
        contentValues.put(COL4, Enlem);
        contentValues.put(COL5, Boylam);
        contentValues.put(COL6, Acildurum);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //Bilgi yanlış girilirse -1 veri girişi oluyor
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }
}
