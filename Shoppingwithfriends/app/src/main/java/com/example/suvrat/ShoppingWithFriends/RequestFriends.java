package com.example.suvrat.ShoppingWithFriends;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
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

import java.util.List;


/**
* Created by Goutam Venkat on 2/19/15
*/
public class RequestFriends extends Activity {

    protected EditText mUserName;
    protected Button requestFriendButton;
    protected Button displayFriendButton;
    protected Button requests;
    protected Button sent_requestsButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestfriends);

        //Initilialize Parse

        //Parse.enableLocalDatastore(this);
        Parse.initialize(this, "xnRG7E5e4NJdotEwXwxw756i2jclVNDEntRcRSdV", "lFm5wKaTg1dZ0sH6jUgLYa7Zo8AK2HkbNX3mRCjD");

        //Initialize Components
        mUserName = (EditText) findViewById(R.id.FriendUsername);
        requestFriendButton = (Button) findViewById(R.id.requestFriendButton);
        displayFriendButton = (Button) findViewById(R.id.displaybutton);

        requests = (Button) findViewById(R.id.friendRequestsReceived);
        sent_requestsButton = (Button) findViewById(R.id.sent_requests);
        sent_requestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takeToSentRequests = new Intent(RequestFriends.this, ListSentRequests.class);
                startActivity(takeToSentRequests);
            }
        });
        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takeToFriendRequests = new Intent(RequestFriends.this, ListFriendRequests.class);
                startActivity(takeToFriendRequests);
            }
        });

        displayFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent takelistfriends = new Intent(RequestFriends.this, DisplayFriends.class);
                 startActivity(takelistfriends);
                }
            });
        //Listen to Register Button Click
        Button logout = (Button) findViewById(R.id.logoutbutton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();

                Intent takeToLogin = new Intent(RequestFriends.this, LoginActivity.class);
                startActivity(takeToLogin);
                finish();
            }
        });
        requestFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get Strings
                final String username = mUserName.getEditableText().toString().trim();

                if (username.length() == 0) {
                    showMessage("Fields cannot be left empty", "Friend Add Request Failed");
                }
                else if (username.equals(ParseUser.getCurrentUser().getUsername())) {
                    showMessage("Request Failed!", "Can't request yourself!");
                }
                else {
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
                                        List requestingTo = currentUser.getList("FriendsRequested");
                                        List receivingFrom = newUser.getList("FriendsRequestsReceived");
                                        List friendsOfCurrentUser = currentUser.getList("Friends");
                                        // If not friends already, then add to the respective columns
                                        boolean temp = true;
                                        if (!friendsOfCurrentUser.contains(username)) {
                                            temp = false;
                                            if (!requestingTo.contains(username))
                                                requestingTo.add(username);
                                            if (!receivingFrom.contains(ParseUser.getCurrentUser().getUsername()))
                                                receivingFrom.add(ParseUser.getCurrentUser().getUsername());
                                        }
                                        final boolean IfAlreadyFriend = temp;
                                        currentUser.put("FriendsRequested", requestingTo);
                                        newUser.put("FriendsRequestsReceived", receivingFrom);

                                        currentUser.saveInBackground();

                                        newUser.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    if (IfAlreadyFriend) {
                                                        showMessage("Already Friend or requested", "No request");
                                                    }
                                                    else {
                                                        showMessage("Friend Request Sent!", "Friend Request Sent!");
                                                    }
                                                } else {
                                                    showMessage(e.getMessage(), "User not Found!");
                                                }
                                            }
                                        });
                                    }
                                });
                            } else {
                                showMessage("User not Found!", "User not found!");
                            }


                        }
                    });
                }


            }
        });
        mUserName.setText("");

    }
    protected void showMessage(String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RequestFriends.this);
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