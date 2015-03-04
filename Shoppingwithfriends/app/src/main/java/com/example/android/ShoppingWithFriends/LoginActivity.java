package com.example.android.ShoppingWithFriends;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * The Main Home page activity for the app
 *
 * @author Suvrat Bhooshan
 * @version 1.0
 */
public class LoginActivity extends Activity {

    protected EditText mUsername;
    protected EditText mPassword;
    protected Button LoginButton;
    protected Button RegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Initialize Parse
     //   Parse.enableLocalDatastore(this);
        Parse.initialize(this, "xnRG7E5e4NJdotEwXwxw756i2jclVNDEntRcRSdV", "lFm5wKaTg1dZ0sH6jUgLYa7Zo8AK2HkbNX3mRCjD");

        //Initialize Variables
        mUsername = (EditText) findViewById(R.id.LoginUsername);
        mPassword = (EditText) findViewById(R.id.LoginPassword);
        LoginButton = (Button) findViewById(R.id.LoginButton);
        RegisterButton = (Button) findViewById(R.id.LoginRegister);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get Strings
                String username = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString().trim();


                ParseUser.logInInBackground(username, password, new LogInCallback() {

                    public void done(ParseUser user, ParseException e) {

                        if (user != null) {

                            Toast.makeText(LoginActivity.this, "Welcome Back", Toast.LENGTH_LONG).show();

                            Intent takeHome = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(takeHome);



                        } else {
                            Utility.showMessage(e.getMessage(), "Login Failed!", LoginActivity.this);
                        }
                    }
                });
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterScreen.class);
                startActivity(registerIntent);
            }
        });
    }

    public void forgotPassword(View v) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
