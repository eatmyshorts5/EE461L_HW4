package com.example.daniel.ee461l_hw4;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
import java.util.concurrent.TimeUnit;


public class activity_map extends AppCompatActivity implements OnMapReadyCallback {

    private int flag = 0;

    public static final String TAG = "RegClient";
    public Bundle extras;
    public double lat;
    public double lng;
    public String start;
    public String end;
    public DirectionsResult results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        }

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
    }

    private void markMap(GoogleMap googleMap) {
        LatLng sydney = new LatLng(lat, lng);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void getDirections(final GoogleMap googleMap) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                DateTime now = new DateTime();

                try {
                    results = DirectionsApi.newRequest(getGeoContext())
                            .mode(TravelMode.DRIVING).origin(start)
                            .destination(end).departureTime(now)
                            .await();


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ApiException e) {
                    e.printStackTrace();
                }

            }
        });

        googleMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[0].legs[0]
                .startLocation.lat, results.routes[0].legs[0].startLocation.lng))
                .title(results.routes[0].legs[0].startAddress));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[0].legs[0]
                .endLocation.lat, results.routes[0].legs[0].endLocation.lng))
                .title(results.routes[0].legs[0].startAddress).snippet(getEndLocationTitle(results)));

        List<LatLng> decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath());
        googleMap.addPolyline(new PolylineOptions().addAll(decodedPath));
    }


    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(3)
                .setApiKey(getString(R.string.directions_api_key))
                .setConnectTimeout(10, TimeUnit.SECONDS)
                .setReadTimeout(10, TimeUnit.SECONDS)
                .setWriteTimeout(10, TimeUnit.SECONDS);
    }

    private String getEndLocationTitle(DirectionsResult results){
        return  "Time :"+ results.routes[0].legs[0].duration.humanReadable
                + " Distance :" + results.routes[0].legs[0].distance.humanReadable;
    }
}
