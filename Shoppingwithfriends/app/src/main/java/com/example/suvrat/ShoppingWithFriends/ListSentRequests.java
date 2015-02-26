package com.example.suvrat.ShoppingWithFriends;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

/**
 * This is a class for Friend Requests received by the user.
 * It will add to the friends column and remove from the friendsRequested and friends requested Received columns
 * @author Goutam V.
 */

public class ListSentRequests extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sent_requests);

        final ListView friendList = (ListView) findViewById(R.id.listViewSentRequests);
        Parse.initialize(this, "xnRG7E5e4NJdotEwXwxw756i2jclVNDEntRcRSdV", "lFm5wKaTg1dZ0sH6jUgLYa7Zo8AK2HkbNX3mRCjD");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");

        final String currentUsername = ParseUser.getCurrentUser().getUsername();
        query.whereEqualTo("username", currentUsername);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    final ParseObject targetUser = objects.get(0);
                    final List FriendRequestsSent = targetUser.getList("FriendsRequested");
                    final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(ListSentRequests.this, R.layout.activity_list_each_sent_request, R.id.textViewEachSentRequest, FriendRequestsSent);
                    friendList.setAdapter(listAdapter);

                }
            }
        });
    }
    protected void showMessage(String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListSentRequests.this);
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
}
