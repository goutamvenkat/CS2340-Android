package com.example.suvrat.ShoppingWithFriends;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek on 2/17/15.
 */
public class Acceptfriends extends ActionBarActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptfriends);

        final ListView friendList = (ListView) findViewById(R.id.listView);
        Parse.initialize(this, "xnRG7E5e4NJdotEwXwxw756i2jclVNDEntRcRSdV", "lFm5wKaTg1dZ0sH6jUgLYa7Zo8AK2HkbNX3mRCjD");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");

        final String currentUsername = ParseUser.getCurrentUser().getUsername();
        query.whereEqualTo("username", currentUsername);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    ParseObject targetUser = objects.get(0);
                    List<String> Friends = targetUser.getList("Friends");
                    ArrayList<String> rfriends = (ArrayList) Friends;
                    ArrayAdapter<String> listAdapter  = new ArrayAdapter<>(Acceptfriends.this, R.layout.activity_acceptfriend1, R.id.textView, rfriends);
                    friendList.setAdapter(listAdapter);
                }
            }
        });
    }
}
