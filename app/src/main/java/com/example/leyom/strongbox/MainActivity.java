package com.example.leyom.strongbox;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.example.leyom.strongbox.data.IdentifierContract;
import com.example.leyom.strongbox.data.IdentifierDbHelper;
import com.example.leyom.strongbox.test.RecyclerViewData;


public class MainActivity extends AppCompatActivity implements IdentifierAdapter.IdentifierAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor>{

    RecyclerView mRecyclerView;
    IdentifierAdapter mIdentifierAdapter;

    public static final int LOADER_ID = 1 ;
    public static final String EXTRA_POSITION = "com.strongbox.position";
    private static final String TAG = "MainActivity";
    public final static int ADD_REQUEST_CODE = 1;
    public final static int EDIT_REQUEST_CODE = 2;
    IdentifierDbHelper mDbHelper;
    SQLiteDatabase mDb;
    Cursor mCursor;
    int mCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_identifier);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mIdentifierAdapter = new IdentifierAdapter(this);


        mDbHelper = new IdentifierDbHelper(this);

        LoaderManager.LoaderCallbacks<Cursor> callbacks = MainActivity.this;
        Bundle bundleForLoader = null;


        getSupportLoaderManager().initLoader(LOADER_ID,bundleForLoader,callbacks);




        mRecyclerView.setAdapter(mIdentifierAdapter);



        final Context context = this;
        FloatingActionButton addIdentiferFab = (FloatingActionButton)  findViewById(R.id.add_identifier);
        addIdentiferFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayIdentifierActivity.class);

                startActivityForResult(intent,ADD_REQUEST_CODE);

            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

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

                getSupportLoaderManager().getLoader(LOADER_ID).forceLoad();
            }
        }).attachToRecyclerView(mRecyclerView);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode) {


            case ADD_REQUEST_CODE:
                Log.d(TAG, "onActivityResult: add ");
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "onActivityResult: add result ok");
                    addIdentifier(data.getStringExtra("identifier"),
                            data.getStringExtra("username"),
                            data.getStringExtra("password"),
                            data.getStringExtra("url"));

                }
                break;
            case EDIT_REQUEST_CODE:
                Log.d(TAG, "onActivityResult: edit ");
                if(resultCode == RESULT_OK) {
                    Log.d(TAG, "onActivityResult: edit result ok");
                    int Id = mCursor.getInt(mCursor.getColumnIndex(IdentifierContract.IdentifierEntry._ID));
                    updateIdentifier(Id,data.getStringExtra("identifier"),
                            data.getStringExtra("username"),
                            data.getStringExtra("password"),
                            data.getStringExtra("url"));
                }
                break;
        }

        getSupportLoaderManager().getLoader(LOADER_ID).forceLoad();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            @Override
            public Cursor loadInBackground() {

                Log.d(TAG, "loadInBackground: ");
                mDb = mDbHelper.getWritableDatabase();
                Cursor cursor = getAllIdentifiers();
                if (cursor == null) {
                    Log.d(TAG, "loadInBackground: cursor null");
                }
                return cursor;
            }

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (mCursor != null) {
                    Log.d(TAG, "onStartLoading: delivery");
                    deliverResult(mCursor);

                } else
                {
                    Log.d(TAG, "onStartLoading: load");
                    forceLoad();
                }

            }

            @Override
            public void deliverResult(Cursor data) {
                Log.d(TAG, "deliverResult: ");
                mCursor = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: " );
        if(data == null) {
            Log.d(TAG, "onLoadFinished: cursor null");
        }
        
        mIdentifierAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: ");
        mIdentifierAdapter.swapCursor(null);

    }


    private Cursor getAllIdentifiers() {
        return mDb.query(
                IdentifierContract.IdentifierEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                IdentifierContract.IdentifierEntry.COLUMN_IDENTIFIER
        );
    }

    private long addIdentifier(String identifier, String username,String password, String url) {
        ContentValues cv = new ContentValues();
        cv.put(IdentifierContract.IdentifierEntry.COLUMN_IDENTIFIER, identifier);
        cv.put(IdentifierContract.IdentifierEntry.COLUMN_USERNAME,username);
        cv.put(IdentifierContract.IdentifierEntry.COLUMN_PASSWORD,password);
        cv.put(IdentifierContract.IdentifierEntry.COLUMN_URL,url);

        return mDb.insert(IdentifierContract.IdentifierEntry.TABLE_NAME,null, cv);
    }

    private boolean removeIdentifier(long id) {

        return mDb.delete(IdentifierContract.IdentifierEntry.TABLE_NAME,
                IdentifierContract.IdentifierEntry._ID + "="+id,null) > 0;
    }
    private void updateIdentifier(long id,String identifier, String username,String password, String url) {
        ContentValues cv = new ContentValues();
        if(identifier != null) {
            cv.put(IdentifierContract.IdentifierEntry.COLUMN_IDENTIFIER, identifier);
        }
        if (username != null) {
            cv.put(IdentifierContract.IdentifierEntry.COLUMN_USERNAME,username);
        }
        if (password != null) {
            cv.put(IdentifierContract.IdentifierEntry.COLUMN_PASSWORD,password);
        }
        if (url != null) {
            cv.put(IdentifierContract.IdentifierEntry.COLUMN_URL,url);
        }
        mDb.update(IdentifierContract.IdentifierEntry.TABLE_NAME,
                cv,
                IdentifierContract.IdentifierEntry._ID + "="+id,
                null);

    }


    @Override
    public void onClick(int position) {
        Intent intent = new Intent(this, DisplayIdentifierActivity.class);
        Bundle identifier = new Bundle();
        mCurrentPosition = position;
        mCursor.moveToPosition(position);
        identifier.putString("identifier", mCursor.getString(mCursor.getColumnIndex(IdentifierContract.IdentifierEntry.COLUMN_IDENTIFIER)));
        identifier.putString("username",mCursor.getString(mCursor.getColumnIndex(IdentifierContract.IdentifierEntry.COLUMN_USERNAME)));
        identifier.putString("password",mCursor.getString(mCursor.getColumnIndex(IdentifierContract.IdentifierEntry.COLUMN_PASSWORD)));
        identifier.putString("url", mCursor.getString(mCursor.getColumnIndex(IdentifierContract.IdentifierEntry.COLUMN_URL)));
        intent.putExtra(EXTRA_POSITION, identifier);
        startActivityForResult(intent,EDIT_REQUEST_CODE);

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
        mDb.close();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }
}
