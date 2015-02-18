package com.example.suvrat.ShoppingWithFriends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by BhavaniJaladanki on 2/13/15.
 */
public class AddFriends extends ActionBarActivity {

    protected EditText mUserName;
    protected Button addFriendButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriends);

        //Initilialize Parse

        //Parse.enableLocalDatastore(this);
        Parse.initialize(this, "xnRG7E5e4NJdotEwXwxw756i2jclVNDEntRcRSdV", "lFm5wKaTg1dZ0sH6jUgLYa7Zo8AK2HkbNX3mRCjD");

        //Initialize Components
        mUserName = (EditText) findViewById(R.id.FriendUsername);
        addFriendButton = (Button) findViewById(R.id.addFriendButton);

        //Listen to Register Button Click
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get Strings
                final String username = mUserName.getEditableText().toString().trim();

                if (username.length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddFriends.this);
                    builder.setMessage("Fields cannot be left empty");
                    builder.setTitle("Friend Add Request Failed");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {

//                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
                    query.whereEqualTo("username", username);
                    query.findInBackground(new FindCallback <ParseObject> () {
                        public void done(List<ParseObject> object, ParseException e) {

                            if (e == null && object.size() > 0) {
                                ParseObject newUser = object.get(0);

                                ParseUser currentUser = ParseUser.getCurrentUser();
                                String currentUserUsername = currentUser.getUsername();
                                ArrayList<String> requestingTo = (ArrayList<String>) currentUser.get("Requesting");
                                ArrayList<String> receivingFrom = (ArrayList<String>) newUser.get("ReceivedRequest");
                                if (requestingTo == null) {
                                    requestingTo = new ArrayList<String>();
                                }
                                if (receivingFrom == null) {
                                    receivingFrom = new ArrayList<String>();
                                }


                                String requesting = username;
                                System.out.println("Requesting TO : " + requestingTo.toString());

                                if (!requestingTo.contains(requesting)) {
                                    requestingTo.add(requesting);
                                    System.out.println("Requesting TO : " + requestingTo.toString());
                                }

                                if (!receivingFrom.contains(currentUserUsername)) {
                                    receivingFrom.add(currentUserUsername);
                                }
                                currentUser.put("Requesting", requestingTo);
                                newUser.put("ReceivedRequest", receivingFrom);
                                currentUser.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            System.out.println("SUCCESS!");
                                        }
                                        else {
                                            System.out.println("FAILURE!");
                                        }
                                    }
                                });
                                newUser.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(AddFriends.this);
                                            builder.setMessage("Friend Request Sent!");
                                            builder.setTitle("Friend Request Sent");
                                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });

                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(AddFriends.this);
                                            builder.setMessage(e.getMessage());
                                            builder.setTitle("User does not exist");
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


                            } else if (e != null) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddFriends.this);
                                builder.setMessage(e.getMessage());
                                builder.setTitle("User does not exist");
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