package com.example.leyom.strongbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.leyom.strongbox.test.RecyclerViewData;

import static android.R.attr.data;
import static android.R.id.message;
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
