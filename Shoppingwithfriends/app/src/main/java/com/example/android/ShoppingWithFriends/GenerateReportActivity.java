package com.example.android.ShoppingWithFriends;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.List;


public class GenerateReportActivity extends Activity implements LocationListener {
    private EditText itemName;
    private EditText itemPrice;
    private Button submitButton;
    protected LocationManager locationManager;
    protected double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_report);
        Parse.initialize(this, "xnRG7E5e4NJdotEwXwxw756i2jclVNDEntRcRSdV", "lFm5wKaTg1dZ0sH6jUgLYa7Zo8AK2HkbNX3mRCjD");
        itemName = (EditText) findViewById (R.id.generateReportItemName);
        itemPrice = (EditText) findViewById(R.id.generateReportItemPrice);
        submitButton = (Button) findViewById(R.id.generateReportsubmitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = itemName.getText().toString().trim();
                final double price = Double.parseDouble(itemPrice.getText().toString());
                ParseUser user = ParseUser.getCurrentUser();
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Items");
                query.whereEqualTo("username", user.getUsername());
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, GenerateReportActivity.this);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        if (parseObjects.size() > 0 && e == null) {
                            ParseObject object = parseObjects.get(0);
                            try {
                                ArrayList<JSONObject> myReports = (ArrayList) object.getList("MyReports");
                                JSONObject newItem = new JSONObject();
                                newItem.put("Item", name);
                                newItem.put("Price", price);
                                newItem.put("Location", Arrays.asList(latitude, longitude));
                                myReports.add(newItem);
                                object.put("MyReports", myReports);
                                object.saveInBackground();
                                Utility.showMessage("Successful!", "Added Report", GenerateReportActivity.this);
                                itemName.setText("");
                                itemPrice.setText("");
                            } catch (JSONException ex) {
                                Utility.showMessage("JSON Error", "Oops!", GenerateReportActivity.this);
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }
    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
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
