package com.example.leyom.strongbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class DisplayIdentifierActivity extends AppCompatActivity {

    private static final String TAG = "DisplayIdentifierActivi";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_identifier);
        Intent intent  = getIntent();
        Bundle position = intent.getBundleExtra(MainActivity.EXTRA_POSITION);

        EditText identifier = (EditText) findViewById(R.id.ed_identifier);
        EditText userName = (EditText) findViewById(R.id.ed_username);
        EditText password = (EditText) findViewById(R.id.ed_password);
        EditText url = (EditText) findViewById(R.id.ed_url);

        identifier.setText(position.getString("identifier"));
        userName.setText(position.getString("username"));
        password.setText(position.getString("password"));
        url.setText(position.getString("url"));

    }
}
