// DatabaseHelper.java
package com.example.lab04;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ConducatoriDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "conducatori";

    // Table columns
    private static final String KEY_ID = "id";
    private static final String KEY_NUME = "nume";
    private static final String KEY_ARE_PERMIS = "arePermis";
    private static final String KEY_ANI_EXPERIENTA = "aniExperienta";
    private static final String KEY_TIP_PERMIS = "tipPermis";
    private static final String KEY_VARSTA = "varsta";
    private static final String KEY_DATA_OBTINERE = "dataObtinerePermis";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NUME + " TEXT,"
                + KEY_ARE_PERMIS + " INTEGER,"
                + KEY_ANI_EXPERIENTA + " INTEGER,"
                + KEY_TIP_PERMIS + " TEXT,"
                + KEY_VARSTA + " REAL,"
                + KEY_DATA_OBTINERE + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertConducator(Conducator conducator) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NUME, conducator.getNume());
        values.put(KEY_ARE_PERMIS, conducator.isArePermis() ? 1 : 0);
        values.put(KEY_ANI_EXPERIENTA, conducator.getAniExperienta());
        values.put(KEY_TIP_PERMIS, conducator.getTipPermis().toString());
        values.put(KEY_VARSTA, conducator.getVarsta());
        values.put(KEY_DATA_OBTINERE, conducator.getDataObtinerePermis().getTime());

        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public ArrayList<Conducator> getAllConducatori() {
        ArrayList<Conducator> conducatori = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Conducator conducator = new Conducator(
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_NUME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ARE_PERMIS)) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ANI_EXPERIENTA)),
                        Conducator.LicenseType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIP_PERMIS))),
                        cursor.getFloat(cursor.getColumnIndexOrThrow(KEY_VARSTA)),
                        new Date(cursor.getLong(cursor.getColumnIndexOrThrow(KEY_DATA_OBTINERE)))
                );
                conducatori.add(conducator);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return conducatori;
    }

    public Conducator getConducatorByName(String nume) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, KEY_NUME + "=?",
                new String[]{nume}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Conducator conducator = new Conducator(
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_NUME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ARE_PERMIS)) == 1,
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ANI_EXPERIENTA)),
                    Conducator.LicenseType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIP_PERMIS))),
                    cursor.getFloat(cursor.getColumnIndexOrThrow(KEY_VARSTA)),
                    new Date(cursor.getLong(cursor.getColumnIndexOrThrow(KEY_DATA_OBTINERE)))
            );
            cursor.close();
            db.close();
            return conducator;
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }

    public ArrayList<Conducator> getConducatoriByExperienceRange(int minExp, int maxExp) {
        ArrayList<Conducator> conducatori = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null,
                KEY_ANI_EXPERIENTA + " BETWEEN ? AND ?",
                new String[]{String.valueOf(minExp), String.valueOf(maxExp)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Conducator conducator = new Conducator(
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_NUME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ARE_PERMIS)) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ANI_EXPERIENTA)),
                        Conducator.LicenseType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIP_PERMIS))),
                        cursor.getFloat(cursor.getColumnIndexOrThrow(KEY_VARSTA)),
                        new Date(cursor.getLong(cursor.getColumnIndexOrThrow(KEY_DATA_OBTINERE)))
                );
                conducatori.add(conducator);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return conducatori;
    }

    public int deleteConducatoriByExperience(int threshold, boolean greaterThan) {
        SQLiteDatabase db = this.getWritableDatabase();
        String condition = greaterThan ? ">" : "<";
        int rowsDeleted = db.delete(TABLE_NAME,
                KEY_ANI_EXPERIENTA + condition + "?",
                new String[]{String.valueOf(threshold)});
        db.close();
        return rowsDeleted;
    }

    public int incrementExperienceByNameStart(char letter) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME + " SET " + KEY_ANI_EXPERIENTA + " = " + KEY_ANI_EXPERIENTA + " + 1 WHERE " + KEY_NUME + " LIKE ?";
        db.execSQL(sql, new String[]{letter + "%"});

        String countSql = "SELECT changes()";
        Cursor cursor = db.rawQuery(countSql, null);
        int rowsUpdated = 0;
        if (cursor.moveToFirst()) {
            rowsUpdated = cursor.getInt(0);
        }
        cursor.close();

        db.close();
        return rowsUpdated;
    }
}