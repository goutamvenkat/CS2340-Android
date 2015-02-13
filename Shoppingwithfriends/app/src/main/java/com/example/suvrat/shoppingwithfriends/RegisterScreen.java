package com.example.suvrat.ShoppingWithFriends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Activity to register User for the App
 *
 * @author Suvrat Bhooshan
 * @version 1.0
 */

public class RegisterScreen extends ActionBarActivity {

    protected EditText mUserName;
    protected EditText mEmail;
    protected EditText mPassword;
    protected Button RegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        //Initilialize Parse

        //Parse.enableLocalDatastore(this);
        Parse.initialize(this, "xnRG7E5e4NJdotEwXwxw756i2jclVNDEntRcRSdV", "lFm5wKaTg1dZ0sH6jUgLYa7Zo8AK2HkbNX3mRCjD");

        //Initialize Components
        mUserName = (EditText) findViewById(R.id.RegisterUserNameEditText);
        mEmail = (EditText) findViewById(R.id.RegisterEmailEditText);
        mPassword = (EditText) findViewById(R.id.RegisterPasswordEditText);
        RegisterButton = (Button) findViewById(R.id.RegisterButton);


        //Listen to Register Button Click
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get Strings
                String username = mUserName.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String email = mEmail.getText().toString().trim();

                if (username.length() == 0 || password.length() == 0 || email.length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterScreen.this);
                    builder.setMessage("Fields cannot be left empty");
                    builder.setTitle("Registration Failed");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {


                    //Initialize User
                    ParseUser user = new ParseUser();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setEmail(email);

                    //Register User
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {

                            //Successfull Signup
                            if (null == e) {

                                Toast.makeText(RegisterScreen.this, "Success", Toast.LENGTH_LONG).show();

                                Intent takeMain = new Intent(RegisterScreen.this, MainActivity.class);
                                startActivity(takeMain);

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterScreen.this);
                                builder.setMessage(e.getMessage());
                                builder.setTitle("Registration Failed");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();

                            }

                        }
                    });
                }


            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_screen, menu);
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
