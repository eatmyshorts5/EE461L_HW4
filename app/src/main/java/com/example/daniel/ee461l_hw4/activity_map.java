package com.example.daniel.ee461l_hw4;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.maps.android.PolyUtil;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.GeoApiContext;
import com.google.maps.DirectionsApi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import android.content.Intent;

public class activity_map extends AppCompatActivity implements OnMapReadyCallback {


    public double latitude;
    public double longitude;

    private int flag = 0;

    public static final String TAG = "RegClient";
    public Bundle extras;
    public double lat;
    public double lng;
    public String start;
    public String end;
    public String address;
    public DirectionsResult results;

    private Intent sendIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        //latitude = intent.getDoubleExtra()
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_map);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        if(getIntent().getStringExtra("Behavior").equals("Directions")){
            flag = 1;
            start = getIntent().getStringExtra("Current");
            end = getIntent().getStringExtra("Destination");
        } else {
            lat = getIntent().getDoubleExtra("Latitude", 0);
            Log.d(TAG, "Nice " + Double.toString(lat));
            lng = getIntent().getDoubleExtra("Longitude", 0);
            Log.d(TAG, "Oh yeah " +Double.toString(lng));
            address = getIntent().getStringExtra("Formatted_Address");
        }

        Button homeButton = findViewById(R.id.home_button);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIT = new Intent(activity_map.this, ActuallyMainActivity.class);
                startActivity(sendIT);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // Include the OnCreate() method here too, as described above.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        if(flag == 1){
            getDirections(googleMap);
        } else {
            markMap(googleMap);
        }
        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_SATELLITE)
                .compassEnabled(true)
                .rotateGesturesEnabled(true)
                .tiltGesturesEnabled(true)
                .zoomControlsEnabled(true);
    }

    private void markMap(GoogleMap googleMap) {
        LatLng sydney = new LatLng(lat, lng);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title(address));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        googleMap.animateCamera( CameraUpdateFactory.zoomTo( 15.0f ) );
    }

    private void getDirections(final GoogleMap googleMap) {
        DirectionsResult results = null;
        MyAsyncTask yeah = new MyAsyncTask();

        yeah.execute(start, end, getString(R.string.directions_api_key));

        try{
            results = yeah.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e ) {
            e.printStackTrace();
        }

        String i = results.routes[0].legs[0].distance.humanReadable;
        Log.d(TAG, "DISTANCE: " + i);

        int check = i.indexOf(' ');
        i = i.substring(0, check);
        i = i.replace(",", "");
        check = Integer.parseInt(i);

        check = check * 1609;

        googleMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[0].legs[0]
                .startLocation.lat, results.routes[0].legs[0].startLocation.lng))
                .title(results.routes[0].legs[0].startAddress));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[0].legs[0]
                .endLocation.lat, results.routes[0].legs[0].endLocation.lng))
                .title(results.routes[0].legs[0].startAddress).snippet(getEndLocationTitle(results)));

        List<LatLng> decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath());
        googleMap.addPolyline(new PolylineOptions().addAll(decodedPath));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(decodedPath.get(decodedPath.size()/2)));

        Circle circle = googleMap.addCircle(new CircleOptions().center(decodedPath.get(decodedPath.size()/2)).radius(check/2));
        circle.setVisible(false);

        googleMap.animateCamera( CameraUpdateFactory.zoomTo( getZoomLevel(circle) ) );
    }

    public int getZoomLevel(Circle circle) {
        int zoomLevel = 17;
        if (circle != null){
            double radius = circle.getRadius();
            double scale = radius / 500;
            zoomLevel =(int) (16 - Math.log(scale) / Math.log(2));
        }
        return zoomLevel;
    }

    private String getEndLocationTitle(DirectionsResult results){
        return  "Time :"+ results.routes[0].legs[0].duration.humanReadable
                + " Distance :" + results.routes[0].legs[0].distance.humanReadable;
    }
}
