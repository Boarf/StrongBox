package com.example.leyom.strongbox;


import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.R.attr.password;


public class DisplayIdentifierActivity extends AppCompatActivity {

    private static final String TAG = "DisplayIdentifierActivi";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_identifier);
        final  EditText identifier = (EditText) findViewById(R.id.ed_identifier);
        final EditText userName = (EditText) findViewById(R.id.ed_username);
        final EditText password = (EditText) findViewById(R.id.ed_password);
        final EditText url = (EditText) findViewById(R.id.ed_url);
        Button save = (Button) findViewById(R.id.bn_save);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent  = getIntent();
        if (intent != null) {
            Bundle entry = intent.getBundleExtra(MainActivity.EXTRA_POSITION);
            if (entry != null) {
                /* Read */
                identifier.setText(entry.getString("identifier"));
                userName.setText(entry.getString("username"));
                password.setText(entry.getString("password"));
                url.setText(entry.getString("url"));
            } else {
                /* add */
                Log.d(TAG, "onCreate: add identifier");


                save.setVisibility(View.VISIBLE);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id =  identifier.getText().toString();
                        String pwd = password.getText().toString();
                        String user = userName.getText().toString();
                        String address = url.getText().toString();

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("identifier",id);
                        returnIntent.putExtra("username",user);
                        returnIntent.putExtra("password", pwd);
                        returnIntent.putExtra("url",address);
                        setResult(RESULT_OK,returnIntent);
                        finish();

                    }
                });

            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.identifier_details,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final  EditText identifier = (EditText) findViewById(R.id.ed_identifier);
        final EditText userName = (EditText) findViewById(R.id.ed_username);
        final EditText password = (EditText) findViewById(R.id.ed_password);
        final EditText url = (EditText) findViewById(R.id.ed_url);
    /* Get the ID of the clicked item */
        int id = item.getItemId();

    /* Settings menu item clicked */
        if (id == R.id.action_edit) {

            Button save = (Button) findViewById(R.id.bn_save);
            identifier.setEnabled(true);
            userName.setEnabled(true);
            password.setEnabled(true);
            url.setEnabled(true);

            identifier.setSelectAllOnFocus(true);
            userName.setSelectAllOnFocus(true);
            password.setSelectAllOnFocus(true);
            url.setSelectAllOnFocus(true);

            save.setVisibility(View.VISIBLE);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id =  identifier.getText().toString();
                    String pwd = password.getText().toString();
                    String user = userName.getText().toString();
                    String address = url.getText().toString();

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("identifier",id);
                    returnIntent.putExtra("username",user);
                    returnIntent.putExtra("password", pwd);
                    returnIntent.putExtra("url",address);
                    setResult(RESULT_OK,returnIntent);
                    finish();

                }
            });

        }

        return super.onOptionsItemSelected(item);
    }

}
