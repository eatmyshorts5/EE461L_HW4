package com.example.daniel.ee461l_hw4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class activity_map extends AppCompatActivity implements OnMapReadyCallback {

    public static final String TAG = "RegClient";
    public Bundle extras;
    public double lat;
    public double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_map);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        //extras = getIntent().getExtras();
        lat = getIntent().getDoubleExtra("Latitude", 0);
        Log.d(TAG, "Nice " + Double.toString(lat));
        lng = getIntent().getDoubleExtra("Longitude", 0);
        Log.d(TAG, "Oh yeah " +Double.toString(lng));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // Include the OnCreate() method here too, as described above.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        Log.d(TAG, Double.toString(lat));
        Log.d(TAG, Double.toString(lng));
        LatLng sydney = new LatLng(lat, lng);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
