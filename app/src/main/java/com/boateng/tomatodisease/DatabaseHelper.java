package com.boateng.tomatodisease;

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
    private static final String DATABASE_NAME = "tomato_scans.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SCANS = "scans";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DISEASE = "disease";
    private static final String COLUMN_CONFIDENCE = "confidence";
    private static final String COLUMN_TREATMENT = "treatment";
    private static final String COLUMN_INFO = "info";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_SCANS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DISEASE + " TEXT,"
                + COLUMN_CONFIDENCE + " REAL,"
                + COLUMN_TREATMENT + " TEXT,"
                + COLUMN_INFO + " TEXT,"
                + COLUMN_TIMESTAMP + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCANS);
        onCreate(db);
    }

    public long addScan(String disease, float confidence, String treatment, String info) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DISEASE, disease);
        values.put(COLUMN_CONFIDENCE, confidence);
        values.put(COLUMN_TREATMENT, treatment);
        values.put(COLUMN_INFO, info);
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        values.put(COLUMN_TIMESTAMP, timestamp);
        long id = db.insert(TABLE_SCANS, null, values);
        db.close();
        return id;
    }

    public ArrayList<Scan> getAllScans() {
        ArrayList<Scan> scanList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SCANS + " ORDER BY " + COLUMN_TIMESTAMP + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Scan scan = new Scan();
                scan.setId(cursor.getInt(0));
                scan.setDisease(cursor.getString(1));
                scan.setConfidence(cursor.getFloat(2));
                scan.setTreatment(cursor.getString(3));
                scan.setInfo(cursor.getString(4));
                scan.setTimestamp(cursor.getString(5));
                scanList.add(scan);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return scanList;
    }
}