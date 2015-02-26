package com.example.suvrat.ShoppingWithFriends;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Displays friend information upon selection from list View
 * @author Goutam Venkat
 */

public class FriendInfo extends Activity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);
        textView = (TextView) findViewById(R.id.textViewFriendInfo);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String friend = extras.getString("friend");
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("username", friend);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> parseUsers, ParseException e) {
                    if (e == null && parseUsers.size() > 0) {
                        ParseUser user = parseUsers.get(0);
                        textView.setText("Username: " + user.getUsername() + "\n" +
                                        "Number of Sales Reports: 0\n" +
                                        "Email: " + user.getEmail() + "\n" +
                                        "Rating: ");
                    }
                }
            });
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_info, menu);
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
