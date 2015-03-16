package com.example.android.ShoppingWithFriends;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.ParseGeoPoint;


public class MapsSalesReportItem extends Activity {
    private GoogleMap googleMap;
    private double latitude, longitude;
    private String name;
    LatLng itemPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_sales_report_item);
        Bundle extras = getIntent().getExtras();
        try {
            if (extras != null) {
                latitude = extras.getDouble("latitude");
                longitude = extras.getDouble("longitude");
                name = extras.getString("itemName");
                itemPos = new LatLng(latitude, longitude);
                initilizeMap();
            }
        } catch (Exception e) {
            Utility.showMessage(e.getMessage(), "Oops!", this);
        }
    }

    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            MarkerOptions marker = new MarkerOptions().position(itemPos).title(name);
            googleMap.addMarker(marker);
            googleMap.setMyLocationEnabled(true);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(itemPos).zoom(14).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//            LatLng myPos = new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude());

//            googleMap.addPolyline(new PolylineOptions().add(myPos, itemPos).width(5).color(Color.BLUE).geodesic(true));
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create map", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_maps_sales_report_item, menu);
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
