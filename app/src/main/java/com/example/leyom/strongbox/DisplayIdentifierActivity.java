package com.example.leyom.strongbox;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



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

        Intent intent  = getIntent();
        if (intent != null) {
            Bundle entry = intent.getBundleExtra(MainActivity.EXTRA_POSITION);
            if (entry != null) {
                //identifier.setEnabled(false);
                identifier.setText(entry.getString("identifier"));
                userName.setText(entry.getString("username"));
                password.setText(entry.getString("password"));
                url.setText(entry.getString("url"));
            } else {
                Log.d(TAG, "onCreate: add identifier");
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
        }





    }
}
