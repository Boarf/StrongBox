package com.example.leyom.strongbox;

import android.Manifest;
import android.app.IntentService;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.leyom.strongbox.data.IdentifierContract;
import com.example.leyom.strongbox.data.IdentifierDbHelper;
import com.example.leyom.strongbox.serialization.Identifier;
import com.example.leyom.strongbox.serialization.SerializedIdentifier;
import com.example.leyom.strongbox.test.RecyclerViewData;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import static android.R.attr.data;
import static android.R.attr.id;
import static android.R.attr.password;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.example.leyom.strongbox.SyncService.DATA_TO_SYNC_EXTRA;
import static com.example.leyom.strongbox.SyncService.DATA_TO_SYNC_EXTRA;
import static com.example.leyom.strongbox.SyncService.SYNC_TO_STORAGE_EXTRA;


public class MainActivity extends AppCompatActivity implements IdentifierAdapter.IdentifierAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor>, SyncReceiver.CompleteSync {

    RecyclerView mRecyclerView;
    IdentifierAdapter mIdentifierAdapter;
    AsyncQueryHandler mQueryHandler;
    public static final int LOADER_ID = 1;
    public static final String EXTRA_POSITION = "com.strongbox.position";
    private static final String TAG = "MainActivity";
    public final static int ADD_REQUEST_CODE = 1;
    public final static int EDIT_REQUEST_CODE = 2;
    private static final int READ_CONTACT_PERMISSION = 100;
    private static final int REQUEST_PERMISSION_SETTING = 200;
    public static final int ASYNC_DELETE_ALL_REQUEST_CODE = 0;
    public static final int ASYNC_DELETE_ONE_REQUEST_CODE = 1;
    private boolean mSyncDbWithFileRequest = false;
    private boolean mSyncFileWithDB = false;
    private SharedPreferences permissionStatus;
    IdentifierDbHelper mDbHelper;
    Cursor mCursor;
    int mCurrentPosition;
    ArrayList<Identifier> mIdentifiers;
    SerializedIdentifier mSerializedIdentifier;
    //ContentObserver mObserver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mIdentifiers = new ArrayList<Identifier>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_identifier);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mIdentifierAdapter = new IdentifierAdapter(this);

//        mObserver = new ContentObserver(new Handler()) {
//            @Override
//            public void onChange(boolean selfChange) {
//                Log.d(TAG, "onChange: ");
//                super.onChange(selfChange);
//            }
//        };

        mDbHelper = new IdentifierDbHelper(this);


        // UpdateRecycler view
        mRecyclerView.setAdapter(mIdentifierAdapter);


        //  Handle add button
        FloatingActionButton addIdentiferFab = (FloatingActionButton) findViewById(R.id.add_identifier);
        addIdentiferFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DisplayIdentifierActivity.class);

                startActivityForResult(intent, ADD_REQUEST_CODE);

            }
        });

        // Handle swipe in recycler view
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {


                IdentifierAdapter.IdentifierAdapterViewHolder holder = (IdentifierAdapter.IdentifierAdapterViewHolder) viewHolder;

                mCursor.moveToPosition(holder.getAdapterPosition());
                int id = mCursor.getInt(mCursor.getColumnIndex(IdentifierContract.IdentifierEntry._ID));

                removeIdentifier(id);


            }
        }).attachToRecyclerView(mRecyclerView);


        // Define add, update and delete database request
        mQueryHandler = new AsyncQueryHandler(getContentResolver()) {

            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                super.onInsertComplete(token, cookie, uri);
                int id;
                id = Integer.parseInt(uri.getLastPathSegment());
                // We need to save the database id to know which element can be modified or deleted
                // Token is the position of the newly inserted element
                mIdentifiers.get(token).setId(id);
                // Synchronize the file with database

                //mSerializedIdentifier.writeJsonStream(mIdentifiers);
                startSync(getApplicationContext(),true);

            }

            @Override
            protected void onUpdateComplete(int token, Object cookie, int result) {
                super.onUpdateComplete(token, cookie, result);
                // Synchronize file with database

                //mSerializedIdentifier.writeJsonStream(mIdentifiers);
                startSync(getApplicationContext(),false);

            }

            @Override
            protected void onDeleteComplete(int token, Object cookie, int result) {
                super.onDeleteComplete(token, cookie, result);

                if (token == ASYNC_DELETE_ALL_REQUEST_CODE) {
                    Log.d(TAG, "onDeleteComplete: ");
                    // We use the data from backup
                    // So we deleted all elements of the DB
                    // Theb we update the DB with data from list
                    // that was updated with data fo the backup

                    // relaunch loader with the new identifier list
                    updateCursorWithList(mIdentifiers);


                } else {
                    // Only one element was deleted in the DB

                    //mSerializedIdentifier.writeJsonStream(mIdentifiers);
                    startSync(getApplicationContext(),false);
                }

            }
        };


        // Get username
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        requestReadContactPermission();

        // Register broadcast receiver for after synchronization status
        IntentFilter syncIntentFilter = new IntentFilter( SyncConstantsReceiver.SYNC_BROADCAST_ACTION);

        SyncReceiver syncReceiver = new SyncReceiver(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(syncReceiver,syncIntentFilter);

        // Launch service for synchronization by reading the file from storage
         startSync(this,false);
        // Check if backup exists
        //mIdentifiers = (ArrayList<Identifier>) mSerializedIdentifier.readJsonStream();




    }

    @Override
    public void finishSync(ArrayList<Identifier> readFromStorage) {
        //If a backup exists
        // request a synchronization of the  DB with backup
        // which will be done once the database is loaded
        mIdentifiers = readFromStorage;
        if (mIdentifiers.size() != 0) {

            mSyncDbWithFileRequest = true;
        }


        // Launch database (and synchronize if needed)
        LoaderManager.LoaderCallbacks<Cursor> callbacks = MainActivity.this;
        Bundle bundleForLoader = null;

        getSupportLoaderManager().initLoader(LOADER_ID, bundleForLoader, callbacks);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {


            case ADD_REQUEST_CODE:
                Log.d(TAG, "onActivityResult: add ");
                if (resultCode == RESULT_OK) {

                    Log.d(TAG, "onActivityResult: add result ok");
                    addIdentifier(data.getStringExtra("identifier"),
                            data.getStringExtra("username"),
                            data.getStringExtra("password"),
                            data.getStringExtra("url"));
                    //Synchronize database and file

                }
                break;
            case EDIT_REQUEST_CODE:
                Log.d(TAG, "onActivityResult: edit ");
                if (resultCode == RESULT_OK) {

                    Log.d(TAG, "onActivityResult: edit result ok");
                    int Id = mCursor.getInt(mCursor.getColumnIndex(IdentifierContract.IdentifierEntry._ID));
                    updateIdentifier(Id, data.getStringExtra("identifier"),
                            data.getStringExtra("username"),
                            data.getStringExtra("password"),
                            data.getStringExtra("url"));

                    //Synchronize database and file
                }
                break;
        }




    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return getAllIdentifiers(this);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: ");
        if (data == null) {
            Log.d(TAG, "onLoadFinished: cursor null");
        }
        mCursor = data;
        //mCursor.registerContentObserver(mObserver);
        mIdentifierAdapter.swapCursor(data);

        //Synchronize DB with file
        if (mSyncDbWithFileRequest) {

            boolean syncDBWithFile = false;
            ArrayList<Identifier> dbIdentifierList = new ArrayList<Identifier>();
            Log.d(TAG, "onLoadFinished: synchronization is requested");
            fillJsonListWithDB(dbIdentifierList, mCursor);

            // if size of data set from file and from DB are different
            // synch the DB with data from file
            if (mIdentifiers.size() == dbIdentifierList.size()) {
                Log.d(TAG, "onLoadFinished: check if synchronization is needed");
                // First sort arraylist using id
                Comparator<Identifier> c = new Comparator<Identifier>() {
                    @Override
                    public int compare(Identifier o1, Identifier o2) {
                        return o1.getId() - o2.getId();
                    }
                };

                Collections.sort(mIdentifiers, c);
                Collections.sort(dbIdentifierList, c);


                for (int i = 0; i < mIdentifiers.size(); i++) {

                    if (!mIdentifiers.get(i).equals(dbIdentifierList.get(i))) {
                        //Update database with data from file
                        Log.d(TAG, "onLoadFinished: Update database with data from file");
                        syncDBWithFile = true;
                        break;


                    }
                }
            } else {
                syncDBWithFile = true;
            }

            if (syncDBWithFile) {

                Log.d(TAG, "onLoadFinished: synchronization is needed");
                //use data from file to update database

                if (mCursor.getCount() != 0) {
                    // if database is not empty, erase it
                    // then relaunch the loader in onDeleteComplete
                    mQueryHandler.startDelete(ASYNC_DELETE_ALL_REQUEST_CODE, null, IdentifierContract.IdentifierEntry.CONTENT_URI, null, null);
                    //getContentResolver().delete(IdentifierContract.IdentifierEntry.CONTENT_URI,null,null);

                } else {
                    //directly launch the loader
                    updateCursorWithList(mIdentifiers);
                }


            }
            // Request to synchronize DB with File is done only once at the start
            mSyncDbWithFileRequest = false;

        } else if (mSyncFileWithDB) {
            // We are here because a sync of the file with data from DB was requested due to the change of id
            // when the DB has been re populated with data from the file
            // The goal is to keep id from DB and id from backup the same
            fillJsonListWithDB(mIdentifiers, mCursor);
            //mSerializedIdentifier.writeJsonStream(mIdentifiers);
            startSync(this,true);
            mSyncFileWithDB = false;
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: ");
        mIdentifierAdapter.swapCursor(null);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: ");
        if (requestCode == READ_CONTACT_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // continue with the functionality that needs permission

                proceedPermissionGranted();

            } else {


                Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    void requestReadContactPermission() {
        Log.d(TAG, "requestReadContactPermission: ");
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CONTACTS)) {
                Log.d(TAG, "requestReadContactPermission: explanation");

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("[Permission]");
                builder.setMessage("This app needs [PERMISSION].");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        SharedPreferences.Editor editor = permissionStatus.edit();
                        editor.putInt(Manifest.permission.READ_CONTACTS, permissionStatus.getInt(Manifest.permission.READ_CONTACTS, 0) + 1);
                        editor.commit();
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACT_PERMISSION);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {

                Log.d(TAG, "requestReadContactPermission: pref" + permissionStatus.getInt(Manifest.permission.READ_CONTACTS, 0));
                if (permissionStatus.getInt(Manifest.permission.READ_CONTACTS, 0) > 1) {
                    // The request has been refused and “Don’t ask again” has been crossed
                    // requestPermission() won't do anything: redirect the user to application settings
                    // where he can set the permission back
                    Log.d(TAG, "requestReadContactPermission: deny and don't ask again");
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("[Permission]");
                    builder.setMessage("This app needs [PERMISSION].");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                            Toast.makeText(getBaseContext(), "Go to settings to Grant permission", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Log.d(TAG, "requestReadContactPermission: first request");
                    // At first, we pass here
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACT_PERMISSION);
                }

            }

        } else {

            proceedPermissionGranted();
        }


    }

    private void proceedPermissionGranted() {
        Log.d(TAG, "proceedPermissionGranted: ");
        mSerializedIdentifier = SerializedIdentifier.getInstance(this);
        mSerializedIdentifier.setProvider("microsoft");
        mSerializedIdentifier.setProviderUsername("guillaume");

        //mIdentifiers = (ArrayList<Identifier>) mSerializedIdentifier.readJsonStream();
    }

    /* called in loadInBackground() so not in the UI thread*/
    private CursorLoader getAllIdentifiers(Context context) {
        return new CursorLoader(
                context,
                IdentifierContract.IdentifierEntry.CONTENT_URI,
                null,
                null,
                null,
                IdentifierContract.IdentifierEntry.COLUMN_IDENTIFIER);
    }

    /* This method should be called n a worker thread */
    private void addIdentifier(String identifier, String username, String password, String url) {
        ContentValues cv = new ContentValues();
        cv.put(IdentifierContract.IdentifierEntry.COLUMN_IDENTIFIER, identifier);
        cv.put(IdentifierContract.IdentifierEntry.COLUMN_USERNAME, username);
        cv.put(IdentifierContract.IdentifierEntry.COLUMN_PASSWORD, password);
        cv.put(IdentifierContract.IdentifierEntry.COLUMN_URL, url);
        // Add the new element in list of identifiers
        mIdentifiers.add(new Identifier(identifier, username, password, url));
        // Pass the index of the newly added identifier to know which element must save the database id
        // in the callback onInsertComplete
        // Process in a worker thread
        mQueryHandler.startInsert(mIdentifiers.size() - 1, null, IdentifierContract.IdentifierEntry.CONTENT_URI, cv);

        // next : Synchronize the file with database in onInsertComplete


    }

    private void updateCursorWithList(final ArrayList<Identifier> list) {


        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                ContentValues[] cvArray = new ContentValues[list.size()];
                int i = 0;
                for (Identifier identifier : list) {
                    ContentValues cv = new ContentValues();
                    cv.put(IdentifierContract.IdentifierEntry.COLUMN_IDENTIFIER, identifier.getIdentifier());
                    cv.put(IdentifierContract.IdentifierEntry.COLUMN_USERNAME, identifier.getUsername());
                    cv.put(IdentifierContract.IdentifierEntry.COLUMN_PASSWORD, identifier.getPassword());
                    cv.put(IdentifierContract.IdentifierEntry.COLUMN_URL, identifier.getUrl());
                    cvArray[i] = cv;
                    i++;
                }
                getContentResolver().bulkInsert(IdentifierContract.IdentifierEntry.CONTENT_URI, cvArray);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                // At this point, new Ids have been generated by SQL
                // We have to update the list and the file with those new Ids
                //To achieve this, we relaunch a query for all data
                // and indicate that the file must updated with DB data
                mSyncFileWithDB = true;
                LoaderManager.LoaderCallbacks<Cursor> callbacks = MainActivity.this;
                Bundle bundleForLoader = null;

                getSupportLoaderManager().restartLoader(LOADER_ID, bundleForLoader, callbacks);



                super.onPostExecute(aVoid);
            }
        }.execute();

    }


    private void removeIdentifier(long id) {

        ListIterator<Identifier> iterator = mIdentifiers.listIterator();
        while (iterator.hasNext()) {
            Identifier ident = iterator.next();
            if (ident.getId() == id) {
                iterator.remove();
                break;
            }
        }

        // Process in a worker thread
        mQueryHandler.startDelete(
                ASYNC_DELETE_ONE_REQUEST_CODE,
                null,
                IdentifierContract.IdentifierEntry.CONTENT_URI,
                IdentifierContract.IdentifierEntry._ID + "=" + id,
                null);


        // next : Synchronize the file with database in onDeleteComplete


    }


    private void updateIdentifier(long id, String identifier, String username, String password, String url) {
        ContentValues cv = new ContentValues();
        Identifier replacement = new Identifier();

        ListIterator<Identifier> iterator = mIdentifiers.listIterator();
        while (iterator.hasNext()) {
            Identifier ident = iterator.next();
            if (ident.getId() == id) {
                replacement.setId((int) id);
                break;
            }
        }
        if (identifier != null) {
            cv.put(IdentifierContract.IdentifierEntry.COLUMN_IDENTIFIER, identifier);
            replacement.setIdentifier(identifier);
        }
        if (username != null) {
            cv.put(IdentifierContract.IdentifierEntry.COLUMN_USERNAME, username);
            replacement.setUsername(username);
        }
        if (password != null) {
            cv.put(IdentifierContract.IdentifierEntry.COLUMN_PASSWORD, password);
            replacement.setPassword(password);
        }
        if (url != null) {
            cv.put(IdentifierContract.IdentifierEntry.COLUMN_URL, url);
            replacement.setUrl(url);
        }

        //update the list
        iterator.set(replacement);

        // Process in a worker thread
        mQueryHandler.startUpdate(
                0,
                null,
                IdentifierContract.IdentifierEntry.CONTENT_URI,
                cv,
                IdentifierContract.IdentifierEntry._ID + "=" + id,
                null);

        // next : Synchronize the file with database in onUpdateComplete

    }


    private void fillJsonListWithDB(List<Identifier> list, Cursor cursor) {
        if (list == null || cursor == null) return;
        list.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            Identifier id = new Identifier();
            id.setIdentifier(cursor.getString(cursor.getColumnIndex(IdentifierContract.IdentifierEntry.COLUMN_IDENTIFIER)));
            id.setUsername(cursor.getString(cursor.getColumnIndex(IdentifierContract.IdentifierEntry.COLUMN_USERNAME)));
            id.setPassword(cursor.getString(cursor.getColumnIndex(IdentifierContract.IdentifierEntry.COLUMN_PASSWORD)));
            id.setUrl(cursor.getString(cursor.getColumnIndex(IdentifierContract.IdentifierEntry.COLUMN_URL)));
            id.setId(cursor.getInt(cursor.getColumnIndex(IdentifierContract.IdentifierEntry._ID)));
            list.add(id);
        }

    }

    public void startSync(Context context,boolean toStorage) {
        Intent syncIntent = new Intent(context,SyncService.class);
        syncIntent.putExtra(SYNC_TO_STORAGE_EXTRA,toStorage);
        if (toStorage) {
            syncIntent.putParcelableArrayListExtra(DATA_TO_SYNC_EXTRA, mIdentifiers);
        }
        context.startService(syncIntent);
    }

    private void syncFileToDb(String filename) {

        fillJsonListWithDB(mIdentifiers, mCursor);
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(this, DisplayIdentifierActivity.class);
        Bundle identifier = new Bundle();
        mCurrentPosition = position;
        mCursor.moveToPosition(position);
        identifier.putString("identifier", mCursor.getString(mCursor.getColumnIndex(IdentifierContract.IdentifierEntry.COLUMN_IDENTIFIER)));
        identifier.putString("username", mCursor.getString(mCursor.getColumnIndex(IdentifierContract.IdentifierEntry.COLUMN_USERNAME)));
        identifier.putString("password", mCursor.getString(mCursor.getColumnIndex(IdentifierContract.IdentifierEntry.COLUMN_PASSWORD)));
        identifier.putString("url", mCursor.getString(mCursor.getColumnIndex(IdentifierContract.IdentifierEntry.COLUMN_URL)));
        intent.putExtra(EXTRA_POSITION, identifier);
        startActivityForResult(intent, EDIT_REQUEST_CODE);

    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();

    }
}
