package com.example.suvrat.ShoppingWithFriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

/**
 * This class is a dummy logged in screen
 *
 * @author Suvrat Bhooshan
 * @version 1.0
 */
public class MainActivity extends ActionBarActivity {


    protected Button addFriendsButton;
    protected Button checkRequestsButton;
    protected TextView txt;
    protected String un;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addFriendsButton = (Button) findViewById(R.id.addFriendsButton);
        checkRequestsButton = (Button) findViewById(R.id.checkRequestsButton);
        txt = (TextView) findViewById(R.id.LoggedinText);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null)   {
            Intent takeToLogin = new Intent(this, LoginActivity.class);
            startActivity(takeToLogin);
        } else {
            un = currentUser.getString("username");
            txt.setText("Hello "+un );

            Button logout = (Button) findViewById(R.id.LoggedinLogout);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParseUser.logOut();

                    Intent takeToLogin = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(takeToLogin);
                    finish();


                }
            });

            addFriendsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent addFriendsIntent = new Intent(MainActivity.this, AddFriends.class);
                    startActivity(addFriendsIntent);
                }
            });

      /*  checkRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checkRequestsIntent = new Intent(this, CheckRequests.class);
                startActivity(checkRequestsIntent);
            }
        });*/

        }



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
