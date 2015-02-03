package com.example.suvrat.shoppingwithfriends;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Registration extends ActionBarActivity {
    private String name;
    private String email;
    private String password;
    protected Map<String, String> emails_and_passwords = new HashMap<>();
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_registration);

        Button register = (Button)findViewById(R.id.registerButton);

        register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                name = ((EditText) findViewById(R.id.name)).getText().toString();
                email = ((EditText) findViewById(R.id.email)).getText().toString();
                password = ((EditText) findViewById(R.id.password)).getText().toString();
                if (!emails_and_passwords.containsKey(email)) {
                    emails_and_passwords.put(email,password);
                    setContentView(R.layout.registration_confirmation);
                    Button ok = (Button)findViewById(R.id.confirmed);
                    ok.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View view) {
//                            goBack();
                            onBackPressed();
                        }
                    });
                } else {
                }

            }
        });

    }

    public void goBack() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
