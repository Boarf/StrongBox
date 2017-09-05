package com.example.leyom.strongbox.data;

import android.content.ContentProvider;
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
        return count;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        mDbHelper.getWritableDatabase().insert(
                IdentifierContract.IdentifierEntry.TABLE_NAME,
                null,
                values
        );
        getContext().getContentResolver().notifyChange(uri,null);
        return uri ;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count;
        count = mDbHelper.getWritableDatabase().delete(
                IdentifierContract.IdentifierEntry.TABLE_NAME,
                selection,
                selectionArgs
        );
        if (count == 1) {
            getContext().getContentResolver().notifyChange(uri,null);
        }else {
            throw new UnsupportedOperationException("Unknown selection");

        }

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
