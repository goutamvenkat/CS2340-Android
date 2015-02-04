package com.example.suvrat.shoppingwithfriends;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Goutam on 2/3/15.
 */
public class LoginPage extends ActionBarActivity{

    private String sample_username = "Goutam";
    private String sample_password = "2340";
    String username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);
        Button b = (Button)findViewById(R.id.loginPageButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = ((EditText) findViewById(R.id.usernameLoginPage)).getText().toString();
                password = ((EditText) findViewById(R.id.passwordLoginPage)).getText().toString();
                if (username.equals(sample_username) && password.equals(sample_password)) {
                    setContentView(R.layout.logged_in);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginPage.this);
                    builder.setMessage("Invalid Username and Password!");
                    builder.setCancelable(true);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

    }
}
