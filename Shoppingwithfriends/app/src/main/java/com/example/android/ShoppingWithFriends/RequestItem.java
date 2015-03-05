package com.example.android.ShoppingWithFriends;

import android.app.Activity;
import android.os.Bundle;
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

import java.util.List;

/**
 * Created by BhavaniJaladanki on 3/4/15.
 */
public class RequestItem extends Activity {

    protected EditText itemName;
    protected EditText itemPrice;
    protected Button requestItemButton;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_item);

        //Initilialize Parse

        //Parse.enableLocalDatastore(this);
        Parse.initialize(this, "xnRG7E5e4NJdotEwXwxw756i2jclVNDEntRcRSdV", "lFm5wKaTg1dZ0sH6jUgLYa7Zo8AK2HkbNX3mRCjD");

        //Initialize Components
        itemName = (EditText) findViewById(R.id.ItemName);
        itemPrice = (EditText) findViewById(R.id.MaxPrice);
        requestItemButton = (Button) findViewById(R.id.RequestItemButton);

        requestItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nameOfItem = itemName.getEditableText().toString().trim();
                final Integer price = Integer.parseInt(itemPrice.getText().toString());

                if (nameOfItem == null || null == price) {
                    Utility.showMessage("Fields cannot be left empty", "Item Request Failed", RequestItem.this);
                }

                ParseQuery<ParseObject> currentUserQuery = ParseQuery.getQuery("Items");
                    currentUserQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    currentUserQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> parseObjects, ParseException e) {
                            ParseObject currentUser = parseObjects.get(0);
                            List<String> itemsName = currentUser.getList("MyItems");
                            List<Integer> itemsPrice = currentUser.getList("MyItemPrices");
                            itemsName.add(nameOfItem);
                            itemsPrice.add(price);
                            currentUser.put("MyItems", itemsName);
                            currentUser.put("MyItemPrices", itemsPrice);
                            currentUser.saveInBackground();

                        }
                    });
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_request_item, menu);
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