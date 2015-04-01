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
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Allows to generate report
 * Location will be determined by GPS
 */

public class GenerateReportActivity extends Activity {
    private EditText itemName;
    private EditText itemPrice;
    private Utility.GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_report);
        Parse.initialize(this, "xnRG7E5e4NJdotEwXwxw756i2jclVNDEntRcRSdV", "lFm5wKaTg1dZ0sH6jUgLYa7Zo8AK2HkbNX3mRCjD");
        itemName = (EditText) findViewById (R.id.generateReportItemName);
        itemPrice = (EditText) findViewById(R.id.generateReportItemPrice);
        Button submitButton = (Button) findViewById(R.id.generateReportsubmitButton);
        gpsTracker = new Utility.GPSTracker(GenerateReportActivity.this);
        if (!gpsTracker.canGetLocation()) {
            gpsTracker.showSettingsAlert();
        }
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemName.getText().toString().length() == 0 || itemPrice.getText().toString().length() == 0) {
                    Utility.showMessage("Information can't be empty", "No info", GenerateReportActivity.this);
                } else {
                    final String name = itemName.getText().toString().trim();
                    final double price = Double.parseDouble(itemPrice.getText().toString());

                    ParseUser user = ParseUser.getCurrentUser();
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Items");
                    query.whereEqualTo("username", user.getUsername());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> parseObjects, ParseException e) {
                            if (parseObjects.size() > 0 && e == null) {
                                ParseObject object = parseObjects.get(0);
                                try {
                                    JSONArray myReports = object.getJSONArray("MyReports");
                                    JSONObject newItem = new JSONObject();
                                    int index = getIndex(myReports, name, price);
                                    newItem.put("Item", name);
                                    newItem.put("Price", price);
                                    newItem.put("Location", new ParseGeoPoint(gpsTracker.getLatitude(), gpsTracker.getLongitude()));

                                    if (index == -1) {
                                        myReports.put(newItem);
                                        Utility.showMessage("Successful!", "Added Report", GenerateReportActivity.this);
                                    } else if (index == -2) {
                                        Utility.showMessage("Not Successful!", "Price more than before!", GenerateReportActivity.this);
                                    } else {
                                        myReports = Utility.remove(myReports, index);
                                        myReports.put(index, newItem);
                                        Utility.showMessage("Successful!", "Replaced Report", GenerateReportActivity.this);
                                    }
                                    object.put("MyReports", myReports);
                                    object.saveInBackground();
                                    itemName.setText("");
                                    itemPrice.setText("");
                                } catch (JSONException ex) {
                                    Utility.showMessage("JSON Error", "Oops!", GenerateReportActivity.this);
                                }
                            }
                        }
                    });
                }
            }

        });
    }

    /**
     * Enable GPS if not enabled
     */

    /**
     * Determines circumstance
     * @param array jsonarray
     * @param name name of item
     * @param price price of item
     * @return appropriate index for situation
     */
    private int getIndex(JSONArray array, String name, double price) {
        int index = -1;
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject x = array.getJSONObject(i);
                if (x.getString("Item").trim().equalsIgnoreCase(name)) {
                    if (x.getInt("Price") > price)
                        index = i;
                    else
                        index = -2;
                    break;
                }
            }
        }
        catch (JSONException ex) {
            Utility.showMessage("JSON Error", "Oops!", this);
        }
        return index;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_generate_report, menu);
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
