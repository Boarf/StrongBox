package com.example.leyom.strongbox.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Leyom on 01/09/2017.
 */

public class IdentifierProvider extends ContentProvider {

    private IdentifierDbHelper mDbHelper;


    @Override
    public boolean onCreate() {
        mDbHelper = new IdentifierDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        cursor = mDbHelper.getReadableDatabase().query(
                IdentifierContract.IdentifierEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count;
        count = mDbHelper.getWritableDatabase().update(
                IdentifierContract.IdentifierEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        Uri returnUri;
        long id =  mDbHelper.getWritableDatabase().insert(
                IdentifierContract.IdentifierEntry.TABLE_NAME,
                null,
                values
        );
        if ( id > 0 ) {
            returnUri = ContentUris.withAppendedId(IdentifierContract.IdentifierEntry.CONTENT_URI, id);
        } else {
            throw new android.database.SQLException("Failed to insert row into " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri ;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.beginTransaction();
        int rowsInserted = 0;
        try {
            for (ContentValues value : values) {

                long _id = db.insert(IdentifierContract.IdentifierEntry.TABLE_NAME, null, value);
                if (_id != -1) {
                    rowsInserted++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        if (rowsInserted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsInserted;

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count;
        count = mDbHelper.getWritableDatabase().delete(
                IdentifierContract.IdentifierEntry.TABLE_NAME,
                selection,
                selectionArgs
        );

        getContext().getContentResolver().notifyChange(uri,null);


        return count;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public void shutdown() {
        mDbHelper.close();
        super.shutdown();
    }

}
