package com.example.android.ShoppingWithFriends;


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
import java.util.List;

/**
 * This is a class for Friend Requests received by the user.
 * It will add to the friends column and remove from the friendsRequested and friends requested Received columns
 * @author Goutam V.
 */

public class ListFriendRequests extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_friend_requests);

        final ListView friendList = (ListView) findViewById(R.id.listViewFriendRequests);
        Parse.initialize(this, "xnRG7E5e4NJdotEwXwxw756i2jclVNDEntRcRSdV", "lFm5wKaTg1dZ0sH6jUgLYa7Zo8AK2HkbNX3mRCjD");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");

        final String currentUsername = ParseUser.getCurrentUser().getUsername();
        query.whereEqualTo("username", currentUsername);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    final ParseObject targetUser = objects.get(0);
                    final List FriendRequests = targetUser.getList("FriendsRequestsReceived");
                    @SuppressWarnings("unchecked") final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(ListFriendRequests.this, R.layout.activity_list_each_request, R.id.textViewEachRequest, FriendRequests);
                    friendList.setAdapter(listAdapter);
                    friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            final String friendToBeAdded = (String) parent.getItemAtPosition(position);
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            addFriend(targetUser, FriendRequests, friendToBeAdded);
                                            FriendRequests.remove(friendToBeAdded);
                                            listAdapter.notifyDataSetChanged();
                                            dialog.dismiss();
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            dialog.dismiss();
                                            break;
                                    }
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(ListFriendRequests.this);
                            builder.setMessage("Add Friend?").setPositiveButton("Yes", dialogClickListener)
                                    .setNegativeButton("No", dialogClickListener).show();
                        }
                    });
                } else {
                    //noinspection ConstantConditions
                    Utility.showMessage(e.getMessage(), "Can't get friend requests!", ListFriendRequests.this);
                }
            }
        });
    }
    /**
    * addFriend - private helper method
    * when addFriend is selected on alert dialog the list is changed for each user
    * @param currentuser Current user
    * @param FriendRequests List of Friend Requests
    * @param friendToBeAdded Friend to be Added
    */
    private void addFriend(final ParseObject currentuser, List FriendRequests, String friendToBeAdded) {
        FriendRequests.remove(friendToBeAdded);
        currentuser.put("FriendsRequestsReceived", FriendRequests);

        // removing the other guy from this list in case the current requests him as well
        List friendsRequested = currentuser.getList("FriendsRequested");
        friendsRequested.remove(friendToBeAdded);
        currentuser.put("FriendsRequested", friendsRequested);
        //

        List friendsOfUser = currentuser.getList("Friends");
        //noinspection unchecked
        friendsOfUser.add(friendToBeAdded);
        currentuser.put("Friends", friendsOfUser);
        // save back to database
        currentuser.saveInBackground();


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
        query.whereEqualTo("username", friendToBeAdded);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null && parseObjects.size() > 0) {
                    ParseObject otherUser = parseObjects.get(0);
                    List requestsSent = otherUser.getList("FriendsRequested");
                    requestsSent.remove(currentuser.get("username"));
                    otherUser.put("FriendsRequested", requestsSent);

                    //In case the current user requested the user to be added already
                    List receivedRequestsToOtherUser = otherUser.getList("FriendsRequestsReceived");
                    receivedRequestsToOtherUser.remove(currentuser.get("username"));
                    otherUser.put("FriendsRequestsReceived", receivedRequestsToOtherUser);

                    List friendsOfOtherUser = otherUser.getList("Friends");
                    //noinspection unchecked
                    friendsOfOtherUser.add(currentuser.get("username"));
                    otherUser.saveInBackground();
                } else {
                    //noinspection ConstantConditions
                    Utility.showMessage(e.getMessage(), "Something went wrong!", ListFriendRequests.this);
                }
            }
        });
    }
}
