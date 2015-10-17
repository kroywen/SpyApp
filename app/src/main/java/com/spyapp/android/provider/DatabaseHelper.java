package com.spyapp.android.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "spyapp.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Query.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Query.DROP_TABLE_QUERY);
        onCreate(db);
    }

    private static class Query {

        public static final String CREATE_TABLE_QUERY =
                "CREATE TABLE IF NOT EXISTS " + SpyContracts.Sms.TABLE_NAME + " (" +
                        SpyContracts.Sms.Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SpyContracts.Sms.Columns.TYPE + " INTEGER, " +
                        SpyContracts.Sms.Columns.SENDER + " TEXT, " +
                        SpyContracts.Sms.Columns.RECIPIENT + " TEXT, " +
                        SpyContracts.Sms.Columns.MESSAGE + " TEXT, " +
                        SpyContracts.Sms.Columns.DATE + " INTEGER);";

        public static final String DROP_TABLE_QUERY =
                "DROP TABLE IF EXISTS " + SpyContracts.Sms.TABLE_NAME;

    }

}