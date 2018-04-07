package com.example.daniel.ee461l_hw4;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

public class Street_View extends FragmentActivity
        implements OnStreetViewPanoramaReadyCallback {

    public double latitude;
    public double longitude;
    public String address;
    public static final String TAG = "RegClient";
    Intent sendIT;
    int flag;
    public String start;
    public String end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street__view);
//        latitude = getIntent().getDoubleExtra("latitude", 0);
//        //Log.d(TAG, "Nice " + Double.toString(latitude));
//        longitude = getIntent().getDoubleExtra("longitude", 0);
//        //Log.d(TAG, "Oh yeah " +Double.toString(longitude));
//        address = getIntent().getStringExtra("Formatted_Address");

        if(getIntent().getStringExtra("Behavior").equals("Directions")){
            flag = 1;
            start = getIntent().getStringExtra("Current");
            end = getIntent().getStringExtra("Destination");
            latitude = getIntent().getDoubleExtra("latitude", 0);
            longitude = getIntent().getDoubleExtra("longitude", 0);
        } else {
            flag = 0;
            latitude = getIntent().getDoubleExtra("latitude", 0);
            //Log.d(TAG, "Nice " + Double.toString(lat));
            longitude = getIntent().getDoubleExtra("longitude", 0);
            //Log.d(TAG, "Oh yeah " +Double.toString(lng));
            address = getIntent().getStringExtra("Formatted_Address");
        }

        Button homeButton = findViewById(R.id.home_button);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIT = new Intent(Street_View.this, activity_map.class);
                if(flag == 0) {
                    sendIT.putExtra("Latitude", latitude);
                    sendIT.putExtra("Longitude", longitude);
                    sendIT.putExtra("Formatted_Address", address);
                    sendIT.putExtra("Behavior", "Single");
                }
                else if(flag == 1){
                    sendIT.putExtra("Behavior", "Directions");
                    sendIT.putExtra("Current", start);
                    sendIT.putExtra("Destination", end);
                }
                startActivity(sendIT);
            }
        });


        StreetViewPanoramaFragment streetViewPanoramaFragment =
                (StreetViewPanoramaFragment) getFragmentManager()
                        .findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
        panorama.setPosition(new LatLng(latitude, longitude));
    }

}
