package com.example.leyom.strongbox;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
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

public class MainActivity extends AppCompatActivity implements IdentifierAdapter.IdentifierAdapterOnClickHandler{

    RecyclerView mRecyclerView;
    IdentifierAdapter mIdentifierAdapter;
    RecyclerViewData mRecyclerViewData;
    public static final String EXTRA_POSITION = "com.strongbox.position";
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_identifier);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mIdentifierAdapter = new IdentifierAdapter(this);

        /* For testing only */
        mRecyclerViewData = new RecyclerViewData();
        mRecyclerViewData.setDataList();

        /********************/

        mRecyclerView.setAdapter(mIdentifierAdapter);
        mIdentifierAdapter.setData(mRecyclerViewData);
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
    public void onClick(int position) {
        Intent intent = new Intent(this, DisplayIdentifierActivity.class);
        Bundle identifier = new Bundle();
        identifier.putString("identifier",mRecyclerViewData.getIdentifier(position));
        identifier.putString("username",mRecyclerViewData.getUsername(position));
        Log.d(TAG, "onClick: username " + mRecyclerViewData.getUsername(position));
        identifier.putString("password",mRecyclerViewData.getPassword(position));
        identifier.putString("url", mRecyclerViewData.getUrl(position));
        intent.putExtra(EXTRA_POSITION, identifier);
        startActivity(intent);

    }
}
