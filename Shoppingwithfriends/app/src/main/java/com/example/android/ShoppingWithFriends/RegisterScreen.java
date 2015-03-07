package com.example.android.ShoppingWithFriends;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONObject;

import java.util.*;

/**
 * Activity to register User for the App
 *
 * @author Suvrat Bhooshan
 * @version 1.0
 */

public class RegisterScreen extends Activity {

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
                final String username = mUserName.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                String email = mEmail.getText().toString().trim();

                if (username.length() == 0 || password.length() == 0 || email.length() == 0) {
                    Utility.showMessage("Fields cannot be left empty", "Registration Failed", RegisterScreen.this);
                } else {
                    //Initialize User
                    final ParseUser user = new ParseUser();
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
                                ParseObject Friends = new ParseObject("Friends");
                                Friends.put("username", username);
                                Friends.put("FriendsRequested", new ArrayList<String>());
                                Friends.put("Friends", new ArrayList<String>());
                                Friends.put("FriendsRequestsReceived", new ArrayList<String>());
                                Friends.saveInBackground();

                                ParseObject Items = new ParseObject("Items");
                                Items.put("username", username);
                                Items.put("MyItems", new JSONObject());
                                Items.saveInBackground();

                                Intent takeMain = new Intent(RegisterScreen.this, LoginActivity.class);
                                startActivity(takeMain);

                            } else {
                                Utility.showMessage(e.getMessage(), "Registration Failed", RegisterScreen.this);
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
