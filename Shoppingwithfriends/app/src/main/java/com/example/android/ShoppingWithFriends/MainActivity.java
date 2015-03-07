package com.example.android.ShoppingWithFriends;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

/**
 * This class is the main page for the application
 *
 * @author Goutam Venkat
 * @version 1.0
 */
public class MainActivity extends Activity {
    private Button addFriends;
    private Button requestItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseUser currentUser = ParseUser.getCurrentUser();
        TextView text = (TextView) findViewById(R.id.LoggedinText);
        text.setText("Hi " + currentUser.getUsername());

        Button logout = (Button) findViewById(R.id.LoggedinLogout);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParseUser.logOut();

                    Intent takeToLogin = new Intent(MainActivity.this, LoginActivity.class);
                    takeToLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    takeToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    takeToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(takeToLogin);
                    finish();


                }
            });
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
