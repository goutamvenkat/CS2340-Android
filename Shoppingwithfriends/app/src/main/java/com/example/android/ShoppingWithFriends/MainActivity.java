package com.example.android.ShoppingWithFriends;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the main page for the application
 *
 * @author Goutam Venkat
 * @version 1.0
 */
public class MainActivity extends Activity {
    private Button addFriends;
    private Button requestItem;
    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Parse.initialize(this, "xnRG7E5e4NJdotEwXwxw756i2jclVNDEntRcRSdV", "lFm5wKaTg1dZ0sH6jUgLYa7Zo8AK2HkbNX3mRCjD");
        ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));
        ParseUser currentUser = ParseUser.getCurrentUser();
        text = (TextView) findViewById(R.id.LoggedinText);

        if (currentUser != null && ParseFacebookUtils.isLinked(currentUser)) {
            Session session = ParseFacebookUtils.getSession();

            if (session != null && session.isOpened()) requestFacebookData();
        } else {
            text.setText("Hi " + currentUser.getUsername());
        }
        addFriends = (Button) findViewById(R.id.requestFriendButtonMain);
        addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, RequestFriends.class));
            }
        });
        requestItem = (Button) findViewById(R.id.requestItemButton);
        requestItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RequestItem.class));
            }
        });

    }
    private void requestFacebookData() {
        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
            @Override
            public void onCompleted(final GraphUser graphUser, Response response) {
                if (graphUser != null) {
                    text.setText("Hi " + graphUser.getName());
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
                    query.whereEqualTo("username", graphUser.getName());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> parseUsers, ParseException e) {
                            if (parseUsers.size() > 0 && e == null) {
                                // Already in the Friends and Items table
                            } else {
                                addToParseTable(graphUser.getName());
                                ParseUser currentUser = ParseUser.getCurrentUser();
                                currentUser.setUsername(graphUser.getName());
                                currentUser.setEmail((String)graphUser.getProperty("email"));
                                currentUser.saveInBackground();
                            }
                        }
                    });

                } else if (response.getError() != null) {
                    if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY) ||
                            (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
                        Log.d("TAG", "The facebook session was invalidated." + response.getError());
                        logOut();
                    } else {
                        Log.d("TAG", "Some other error: " + response.getError());
                    }
                }
            }
        });
        request.executeAsync();
    }
    public void logoutOnClick(View v) {
        logOut();
    }
    private void addToParseTable(String username) {
        ParseObject Friends = new ParseObject("Friends");
        Friends.put("username", username);
        Friends.put("FriendsRequested", new ArrayList<String>());
        Friends.put("Friends", new ArrayList<String>());
        Friends.put("FriendsRequestsReceived", new ArrayList<String>());
        Friends.saveInBackground();

        ParseObject Items = new ParseObject("Items");
        Items.put("username", username);
        Items.put("MyItems", new JSONObject());
        Items.put("MyReports", new JSONArray());
        Items.saveInBackground();
    }
    private void logOut() {
        ParseUser.logOut();

        Intent takeToLogin = new Intent(MainActivity.this, LoginActivity.class);
        takeToLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        takeToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        takeToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(takeToLogin);
        finish();
    }
    public void goToGenerate(View v) {
        Intent intent = new Intent(this, GenerateReportActivity.class);
        startActivity(intent);
    }
    public void goToSalesReport(View v) {
        Intent intent = new Intent(this, SalesReportActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
