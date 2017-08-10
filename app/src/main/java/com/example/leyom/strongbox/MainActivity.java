package com.example.leyom.strongbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.leyom.strongbox.test.RecyclerViewData;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    IdentifierAdapter mIdentifierAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_identifier);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mIdentifierAdapter = new IdentifierAdapter();

        /* For testing only */
        RecyclerViewData data = new RecyclerViewData();
        data.setDataList();

        /********************/

        mRecyclerView.setAdapter(mIdentifierAdapter);
        mIdentifierAdapter.setData(data);



    }
}
