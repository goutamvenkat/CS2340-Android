package com.example.android.ShoppingWithFriends;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

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
* Created by Goutam Venkat on 2/19/15
*/
public class RequestFriends extends Activity {

    private AutoCompleteTextView autoComplete;
    private String fromAutoComplete = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestfriends);

        //Initialize Parse

        //Parse.enableLocalDatastore(this);
        Parse.initialize(this, "xnRG7E5e4NJdotEwXwxw756i2jclVNDEntRcRSdV", "lFm5wKaTg1dZ0sH6jUgLYa7Zo8AK2HkbNX3mRCjD");

        //Initialize Components
        Button requestFriendButton = (Button) findViewById(R.id.requestFriendButton);
        Button displayFriendButton = (Button) findViewById(R.id.displaybutton);

        Button requests = (Button) findViewById(R.id.friendRequestsReceived);
        Button sent_requestsButton = (Button) findViewById(R.id.sent_requests);
        sent_requestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takeToSentRequests = new Intent(RequestFriends.this, ListSentRequests.class);
                startActivity(takeToSentRequests);
            }
        });

        autoComplete = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        doAutoComplete();
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
                takeToLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                takeToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                takeToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(takeToLogin);
                finish();
            }
        });
        requestFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get Strings
//                final String username = fromAutoComplete;
                if (fromAutoComplete.length() == 0 && autoComplete.getText().toString().length() == 0) {
                    Utility.showMessage("Fields cannot be left empty", "Friend Add Request Failed", RequestFriends.this);
                } else if (fromAutoComplete.equals(ParseUser.getCurrentUser().getUsername())) {
                    Utility.showMessage("Request Failed!", "Can't request yourself!", RequestFriends.this);
                } else {
                    if (fromAutoComplete.equalsIgnoreCase(""))
                        fromAutoComplete = autoComplete.getText().toString();
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
                    query.whereEqualTo("username", fromAutoComplete);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> object, ParseException e) {

                            if (e == null && object.size() > 0) {
                                final ParseObject newUser = object.get(0);
                                ParseQuery<ParseObject> currentUserQuery = ParseQuery.getQuery("Friends");
                                currentUserQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                                currentUserQuery.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> parseObjects, ParseException e) {
                                        ParseObject currentUser = parseObjects.get(0);
                                        List<String> requestingTo = currentUser.getList("FriendsRequested");
                                        List<String> receivingFrom = newUser.getList("FriendsRequestsReceived");
                                        List<String> friendsOfCurrentUser = currentUser.getList("Friends");
                                        // If not friends already, then add to the respective columns
                                        boolean already_friend = true;
                                        boolean already_requested = true;
                                        if (!friendsOfCurrentUser.contains(fromAutoComplete)) {
                                            already_friend = false;
                                            if (!requestingTo.contains(fromAutoComplete)) {
                                                requestingTo.add(fromAutoComplete);
                                                already_requested = false;
                                            }
                                            if (!receivingFrom.contains(ParseUser.getCurrentUser().getUsername()))
                                                receivingFrom.add(ParseUser.getCurrentUser().getUsername());
                                        }
                                        final boolean AlreadyFriend = already_friend;
                                        final boolean AlreadyRequested = already_requested;
                                        currentUser.put("FriendsRequested", requestingTo);
                                        newUser.put("FriendsRequestsReceived", receivingFrom);

                                        currentUser.saveInBackground();

                                        newUser.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    if (AlreadyFriend) {
                                                        Utility.showMessage("Already a Friend!", "No request", RequestFriends.this);
                                                    } else if (AlreadyRequested) {
                                                        Utility.showMessage("Already Requested!", "No request", RequestFriends.this);
                                                    } else {
                                                        Utility.showMessage("Friend Request Sent!", "Friend Request Sent!", RequestFriends.this);
                                                    }
                                                } else {
                                                    Utility.showMessage(e.getMessage(), "User not Found!", RequestFriends.this);
                                                }
                                                autoComplete.setText("");
                                            }
                                        });
                                    }
                                });
                            } else {
                                Utility.showMessage("User not Found!", "User not found!", RequestFriends.this);
                                autoComplete.setText("");
                            }
                        }
                    });
                }
            }
        });
    }

    private void doAutoComplete() {
        final List<String> users = new ArrayList<>();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if (e == null && parseUsers.size() > 0) {
                    for (ParseUser user : parseUsers) {
                        users.add(user.getUsername());
                    }
                }
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.textView3, users);
        autoComplete.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                fromAutoComplete = (String) adapterView.getItemAtPosition(position);
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