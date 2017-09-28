package com.example.leyom.strongbox.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Leyom on 22/08/2017.
 */

public class IdentifierDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "identifier.db";
    public static final int DATABASE_VERSION = 1;

    public IdentifierDbHelper(Context context) {
        super(context,DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_IDENTIFIER_TABLE = " CREATE TABLE " +
                IdentifierContract.IdentifierEntry.TABLE_NAME + " (" +
                IdentifierContract.IdentifierEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IdentifierContract.IdentifierEntry.COLUMN_IDENTIFIER + " TEXT NOT NULL, " +
                IdentifierContract.IdentifierEntry.COLUMN_USERNAME + " TEXT NOT NULL, " +
                IdentifierContract.IdentifierEntry.COLUMN_PASSWORD + " TEXT NOT NULL, " +
                IdentifierContract.IdentifierEntry.COLUMN_URL + " TEXT NOT NULL " +
                "); ";
        db.execSQL(SQL_CREATE_IDENTIFIER_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + IdentifierContract.IdentifierEntry.TABLE_NAME);
        onCreate(db);
    }


}
