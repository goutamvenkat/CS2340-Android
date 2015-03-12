package com.example.android.ShoppingWithFriends;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SalesReportActivity extends Activity {
    private JSONObject userItems;
    private ParseObject currentUser;
    private ArrayList<String> notificationList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);
        final ListView salesList = (ListView) findViewById(R.id.listViewSalesReport);
        Parse.initialize(this, "xnRG7E5e4NJdotEwXwxw756i2jclVNDEntRcRSdV", "lFm5wKaTg1dZ0sH6jUgLYa7Zo8AK2HkbNX3mRCjD");
        ParseQuery<ParseObject> queryForUserList = ParseQuery.getQuery("Items");
        queryForUserList.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        queryForUserList.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (parseObjects.size() > 0 && e == null) {
                    currentUser = parseObjects.get(0);
                    userItems = currentUser.getJSONObject("MyItems");
                }
            }
        });
        ParseQuery<ParseObject> queryForItems = ParseQuery.getQuery("Items");
        queryForItems.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        queryForItems.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null && parseObjects.size() > 0) {
                    try {
                        for (ParseObject obj : parseObjects) {
                            JSONArray array = obj.getJSONArray("MyReports");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject item = array.getJSONObject(i);
                                String itemName = item.getString("Item").toLowerCase().trim();
                                if (userItems != null && !userItems.isNull(itemName) && item.getInt("Price") <= userItems.getInt(itemName)) {
                                     notificationList.add("Name: " + itemName + "\n" +
                                                          "Location: " + item.getJSONArray("Location") + "\n" +
                                                          "Price: " + item.getInt("Price"));
                                }
                            }
                        }
                        ArrayAdapter adapter = new ArrayAdapter(SalesReportActivity.this, R.layout.activity_each_sales_report, R.id.textViewEachSalesReport, notificationList);
                        salesList.setAdapter(adapter);
                    } catch (JSONException ex) {
                        Utility.showMessage(ex.getMessage(), "Oops!", SalesReportActivity.this);
                    }

                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sales_report, menu);
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
