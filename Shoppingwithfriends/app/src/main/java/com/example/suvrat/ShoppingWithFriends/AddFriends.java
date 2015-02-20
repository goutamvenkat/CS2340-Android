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
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by BhavaniJaladanki on 2/13/15.
 */
public class AddFriends extends ActionBarActivity {

    protected EditText mUserName;
    protected Button addFriendButton;
    protected Button displayFriendButton;
    protected TextView userLoggedIn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriends);

        //Initilialize Parse

        //Parse.enableLocalDatastore(this);
        Parse.initialize(this, "xnRG7E5e4NJdotEwXwxw756i2jclVNDEntRcRSdV", "lFm5wKaTg1dZ0sH6jUgLYa7Zo8AK2HkbNX3mRCjD");
        userLoggedIn = (TextView) findViewById(R.id.userLoggedIn);
        userLoggedIn.setText("Hi " + ParseUser.getCurrentUser().getUsername() + "!");

        //Initialize Components
        mUserName = (EditText) findViewById(R.id.FriendUsername);
        addFriendButton = (Button) findViewById(R.id.addFriendButton);
        displayFriendButton = (Button) findViewById(R.id.displaybutton);

        displayFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent takelistfriends = new Intent(AddFriends.this, Acceptfriends.class);
                 startActivity(takelistfriends);
                }
            });
        //Listen to Register Button Click
        Button logout = (Button) findViewById(R.id.logoutbutton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();

                Intent takeToLogin = new Intent(AddFriends.this, LoginActivity.class);
                startActivity(takeToLogin);
                finish();
            }
        });
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get Strings
                final String username = mUserName.getEditableText().toString().trim();

                if (username.length() == 0) {
                    showMessage(null, "Fields cannot be left empty", "Friend Add Request Failed");
                } else {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
                    query.whereEqualTo("username", username);
                    query.findInBackground(new FindCallback <ParseObject> () {
                        public void done(List<ParseObject> object, ParseException e) {

                            if (e == null && object.size() > 0) {
                                final ParseObject newUser = object.get(0);
                                ParseQuery<ParseObject> currentUserQuery = ParseQuery.getQuery("Friends");
                                currentUserQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                                currentUserQuery.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> parseObjects, ParseException e) {
                                        ParseObject currentUser = parseObjects.get(0);
                                        ArrayList<String> requestingTo = (ArrayList<String>) currentUser.get("FriendsRequested");
                                        ArrayList<String> receivingFrom = (ArrayList<String>) newUser.get("FriendsRequestsReceived");
                                        if (requestingTo == null) requestingTo = new ArrayList<String>();
                                        if (receivingFrom == null) receivingFrom = new ArrayList<String>();

                                        if (!requestingTo.contains(username)) requestingTo.add(username);
                                        if (!receivingFrom.contains(ParseUser.getCurrentUser().getUsername())) receivingFrom.add(ParseUser.getCurrentUser().getUsername());

                                        ArrayList<String> friendsOfUser = (ArrayList<String>) currentUser.get("Friends");
                                        ArrayList<String> friendsOfNewUser = (ArrayList<String>) newUser.get("Friends");

                                        if (friendsOfNewUser == null) friendsOfNewUser = new ArrayList<String>();
                                        if (friendsOfUser == null) friendsOfUser = new ArrayList<String>();

                                        if (!friendsOfUser.contains(username)) {
                                            friendsOfUser.add(username);
                                        }
                                        if (!friendsOfNewUser.contains(ParseUser.getCurrentUser().getUsername())) {
                                            friendsOfNewUser.add(ParseUser.getCurrentUser().getUsername());
                                        }

                                        currentUser.put("Friends", friendsOfUser);
                                        newUser.put("Friends", friendsOfNewUser);
                                        currentUser.put("FriendsRequested", requestingTo);
                                        newUser.put("FriendsRequestsReceived", receivingFrom);

                                        currentUser.saveInBackground();

                                        newUser.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    showMessage(e, "Friend Request Sent!", "Friend Request Sent!");
                                                } else {
                                                    showMessage(e, e.getMessage(), "User not Found!");
                                                }
                                            }
                                        });
                                    }
                                });
                            } else {
                                showMessage(e, "User not Found!", "User not found!");
                            }


                        }
                    });
                }


            }
        });

    }
    private void showMessage(ParseException e, String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddFriends.this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_addfriends, menu);
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