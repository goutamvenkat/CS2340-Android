package com.example.suvrat.ShoppingWithFriends;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.DialogPreference;
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
 * Created by Goutam V. on 2/17/15.
 */
public class DisplayFriends extends Activity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayfriends);

        final ListView friendList = (ListView) findViewById(R.id.listView);
        Parse.initialize(this, "xnRG7E5e4NJdotEwXwxw756i2jclVNDEntRcRSdV", "lFm5wKaTg1dZ0sH6jUgLYa7Zo8AK2HkbNX3mRCjD");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");

        final String currentUsername = ParseUser.getCurrentUser().getUsername();
        query.whereEqualTo("username", currentUsername);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    final ParseObject targetUser = objects.get(0);
                    final List<String> Friends = targetUser.getList("Friends");
                    ArrayList<String> rfriends = (ArrayList) Friends;
                    final ArrayAdapter<String> listAdapter  = new ArrayAdapter<>(DisplayFriends.this, R.layout.activity_display_each_friend, R.id.textView, rfriends);
                    friendList.setAdapter(listAdapter);
                    friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String selectedFriend = (String) parent.getItemAtPosition(position);
                            DialogInterface.OnClickListener infoOrDeleteListener = infoOrDeleteListener(selectedFriend, Friends, listAdapter, targetUser);
                            AlertDialog.Builder builder = new AlertDialog.Builder(DisplayFriends.this);
                            builder.setMessage("Get Friend Info or Delete Friend?");
                            builder.setPositiveButton("Get Info", infoOrDeleteListener).setNegativeButton("Delete", infoOrDeleteListener);
                            builder.show();
                        }
                    });
                }
            }
        });
    }

    private DialogInterface.OnClickListener deleteClickListener(final String selectedFriend, final List Friends,
                                                          final ArrayAdapter<String> listAdapter,
                                                          final ParseObject currentUser) {
        return new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {
               switch (which){
                   case DialogInterface.BUTTON_POSITIVE:
                       removeFriend(Friends, selectedFriend, currentUser);
                       listAdapter.notifyDataSetChanged();
                       dialog.dismiss();
                       break;

                   case DialogInterface.BUTTON_NEGATIVE:
                       dialog.dismiss();
                       break;
               }
           }
        };
    }
    private DialogInterface.OnClickListener infoOrDeleteListener(final String selectedFriend, final List Friends,
                                                                final ArrayAdapter<String> listAdapter,
                                                                final ParseObject currentUser) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        Intent friendInfo = new Intent(DisplayFriends.this, FriendInfo.class);
                        friendInfo.putExtra("friend", selectedFriend);
                        startActivity(friendInfo);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        DialogInterface.OnClickListener deleteListener = deleteClickListener(selectedFriend, Friends, listAdapter, currentUser);
                        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayFriends.this);
                        builder.setMessage("Are you sure?").
                                setPositiveButton("Yes", deleteListener).setNegativeButton("No", deleteListener);
                        builder.show();
                        break;
                }
            }
        };
    }
    private void removeFriend(List Friends, String selectedFriend, final ParseObject currentUser) {
        Friends.remove(selectedFriend);
        currentUser.put("Friends", Friends);
        currentUser.saveInBackground();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
        query.whereEqualTo("username", selectedFriend);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null && parseObjects.size() > 0) {
                    ParseObject friend = parseObjects.get(0);
                    List friendsOfFriend = friend.getList("Friends");
                    friendsOfFriend.remove(currentUser.get("username"));
                    friend.put("Friends", friendsOfFriend);
                    friend.saveInBackground();
                }
            }
        });
    }
}
