package com.example.android.ShoppingWithFriends;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.Iterator;
import java.util.List;

/**
 * SalesReportActivity
 * Returns all appropriate notifications for user, upon which they can and find out where it is
 */

public class SalesReportActivity extends Activity {
    private JSONObject userItems;
    private ParseObject currentUser;
    private ArrayList<MyObject> notificationList;
    private String theKey = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);
        notificationList = new ArrayList<>();
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
                    fillNotificationList(parseObjects);
                    @SuppressWarnings("unchecked") ArrayAdapter adapter = new ArrayAdapter(SalesReportActivity.this, R.layout.activity_each_sales_report, R.id.textViewEachSalesReport, notificationList);
                    salesList.setAdapter(adapter);
                    salesList.setOnItemClickListener(new MyItemClickListener());
                }
            }
        });
    }

    /**
     * MyItemClickListener
     * OnItemClickListener for salesList
     */
    private class MyItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             MyObject item = (MyObject) parent.getItemAtPosition(position);
             JSONObject itemLocation = item.getLocation();
             try {
                 double latitude = itemLocation.getDouble("latitude");
                 double longitude = itemLocation.getDouble("longitude");
                 Intent goToMap = new Intent(SalesReportActivity.this, MapsSalesReportItem.class);
                 goToMap.putExtra("latitude", latitude);
                 goToMap.putExtra("longitude", longitude);
                 goToMap.putExtra("itemName", item.getName());
                 startActivity(goToMap);
             } catch (Exception e) {
                 Utility.showMessage(e.getMessage(), "Oops!", SalesReportActivity.this);
             }

        }
    }

    /**
     * Fills the list based on queries
     * @param parseObjects All users other than the one logged in
     */
    private void fillNotificationList(List<ParseObject> parseObjects) {
        try {
            for (ParseObject obj : parseObjects) {
                JSONArray array = obj.getJSONArray("MyReports");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    String itemName = item.getString("Item").toLowerCase().trim();
                    if (userItems != null && keyInUserItems(itemName) && item.getInt("Price") <= userItems.getInt(theKey)) {
                        notificationList.add(new MyObject(item));
                    }
                }
            }

        } catch (JSONException ex) {
            Utility.showMessage(ex.getMessage(), "Oops!", SalesReportActivity.this);
        }
    }
    /**
    * keyInUserItems
    * @param item_name name of the item you want to find
    * @return  true or false depending on whether it is there in user items
    */
    private boolean keyInUserItems(String item_name) {
//        if (!userItems.isNull(item_name)) return true;
        Iterator<?> it = userItems.keys();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (key.trim().toLowerCase().equalsIgnoreCase(item_name)) {
                theKey = key;
                return true;
            }
        }
        return false;
    }

    /**
     * MyObject special class - easy handling
     */
    private class MyObject {
        private String itemName;
        private int itemPrice;
        private JSONObject location;
        private MyObject(JSONObject obj) {
            try {
                itemName = obj.getString("Item").toLowerCase().trim();
                itemPrice = obj.getInt("Price");
                location = obj.getJSONObject("Location");
            } catch (Exception e) {
                Utility.showMessage(e.getMessage(), "Oops!", SalesReportActivity.this);
            }
        }
        private JSONObject getLocation() {
            return location;
        }
        public String toString() {
            return itemName + " : $" + itemPrice;
        }
        private String getName() {
            return itemName;
        }
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
