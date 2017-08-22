package com.example.leyom.strongbox;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.leyom.strongbox.test.RecyclerViewData;

import static android.R.attr.data;
import static android.R.id.message;
import static android.os.Build.VERSION_CODES.M;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity implements IdentifierAdapter.IdentifierAdapterOnClickHandler
, LoaderManager.LoaderCallbacks<RecyclerViewData.RecyclerViewDataList>{

    RecyclerView mRecyclerView;
    IdentifierAdapter mIdentifierAdapter;
    RecyclerViewData.RecyclerViewDataList mRecyclerViewDataList;
    private static final int LOADER_ID = 0;

    public static final String EXTRA_POSITION = "com.strongbox.position";
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoaderManager.LoaderCallbacks<RecyclerViewData.RecyclerViewDataList> callbacks = MainActivity.this;
        Bundle bundleForLoader = null;
        getSupportLoaderManager().initLoader(LOADER_ID,bundleForLoader,callbacks);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_identifier);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mIdentifierAdapter = new IdentifierAdapter(this);


        /********************/

        mRecyclerView.setAdapter(mIdentifierAdapter);



        final Context context = this;
        FloatingActionButton addIdentiferFab = (FloatingActionButton)  findViewById(R.id.add_identifier);
        addIdentiferFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayIdentifierActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public Loader<RecyclerViewData.RecyclerViewDataList> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<RecyclerViewData.RecyclerViewDataList>(this) {


            @Override
            public RecyclerViewData.RecyclerViewDataList loadInBackground() {
                RecyclerViewData recyclerViewData = new RecyclerViewData();
                RecyclerViewData.RecyclerViewDataList list =  recyclerViewData.new  RecyclerViewDataList();
                return list;
            }

            @Override
            protected void onStartLoading() {
                if (mRecyclerViewDataList != null) {
                    deliverResult(mRecyclerViewDataList);
                }
                else {
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(RecyclerViewData.RecyclerViewDataList data) {
                mRecyclerViewDataList = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<RecyclerViewData.RecyclerViewDataList> loader, RecyclerViewData.RecyclerViewDataList data) {
        mIdentifierAdapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<RecyclerViewData.RecyclerViewDataList> loader) {
        mIdentifierAdapter.setData(null);
    }


    @Override
    public void onClick(int position) {
        Intent intent = new Intent(this, DisplayIdentifierActivity.class);
        Bundle identifier = new Bundle();
        identifier.putString("identifier",mRecyclerViewDataList.getIdentifier(position));
        identifier.putString("username",mRecyclerViewDataList.getUsername(position));
        Log.d(TAG, "onClick: username " + mRecyclerViewDataList.getUsername(position));
        identifier.putString("password",mRecyclerViewDataList.getPassword(position));
        identifier.putString("url", mRecyclerViewDataList.getUrl(position));
        intent.putExtra(EXTRA_POSITION, identifier);
        startActivity(intent);

    }
}
