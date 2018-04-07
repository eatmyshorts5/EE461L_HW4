package com.example.daniel.ee461l_hw4;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class GameActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final String TAG = "RegClient";

    GoogleMap theMap;

    private Intent sendIT;

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    public static final String API_KEY = "&key=AIzaSyBZZWrbLTwf5hApkWsjvfBeche0Gp0bOPQ";
    public static final int NUM_LOC = 3;

    private int hintIndex = 0;
    private int locationIndex = 0;
    private EditText guessField;
    private TextView hintField;
    private TextView heatField;

    private double lat;
    private double lng;
    private double finalLat;
    private double finalLng;
    private double comparator;

    private String formattedAddress;

    private String locations[] = new String[]{"Yankees Stadium", "UT Austin", "Sydney Opera House"};

    private String hints[][] = new String[][]{
            {"The city is sometimes referenced as a large fruit", "They play sports here", "The players wear pinstripes"},
            {"This is an institution of learning", "It's real hot", "They like orange down there"},
            {"It's down under", "They play music here", "Iconic Architecture Mate"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Random lol = new Random();
        int rand = lol.nextInt(NUM_LOC);

        locationIndex = rand;

        guessField = findViewById(R.id.guess_input);
        hintField = findViewById(R.id.hint_text);
        heatField = findViewById(R.id.heat_text);

        getFinalCoord();
        addHint();

        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processGuess();
                Log.d(TAG, Double.toString(comparator));
            }
        });

        Button homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIT = new Intent(GameActivity.this, ActuallyMainActivity.class);
                startActivity(sendIT);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        theMap = googleMap;
    }


    private void processGuess(){
        GameAsync yeah = new GameAsync();
        GameInfo hold = new GameInfo();

        yeah.execute(BASE_URL, API_KEY, guessField.getText().toString());

        try{
            hold = yeah.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e ) {
            e.printStackTrace();
        }

        lat = hold.getLat();
        lng = hold.getLng();
        formattedAddress = hold.getAddress();

        LatLng sydney = new LatLng(lat, lng);
        theMap.addMarker(new MarkerOptions().position(sydney)
                .title(formattedAddress));
        theMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        theMap.animateCamera( CameraUpdateFactory.zoomTo( 13 ) );

        double totLat = Math.abs(finalLat - lat);
        double totLng = Math.abs(finalLng - lng);
        if(totLat > totLng){
            comparator = totLat;
        } else {
            comparator = totLng;
        }

        heatCheck();

        if(hintIndex < 3) {
            addHint();
        }
    }

    private void heatCheck(){
        if(comparator >= 35){
            theMap.animateCamera( CameraUpdateFactory.zoomTo( 1 ) );
            heatField.setText("Heat Status: STONE COLD STEVE AUSTIN");
        } else if(comparator >= 25 && comparator < 35) {
            theMap.animateCamera( CameraUpdateFactory.zoomTo( 2.5f ) );
            heatField.setText("Heat Status: ABSOLUTE ZERO");
        } else if(comparator >= 15 && comparator < 25){
            theMap.animateCamera( CameraUpdateFactory.zoomTo( 4f ) );
            heatField.setText("Heat Status: ICE COLD");
        } else if(comparator >= 5 && comparator < 15){
            theMap.animateCamera( CameraUpdateFactory.zoomTo( 5.5f ) );
            heatField.setText("Heat Status: COLD");
        } else if(comparator >= 1 && comparator < 5){
            theMap.animateCamera( CameraUpdateFactory.zoomTo( 7f ) );
            heatField.setText("Heat Status: Lukewarm");
        } else if(comparator >= 0.5 && comparator < 1){
            theMap.animateCamera( CameraUpdateFactory.zoomTo( 8.5f ) );
            heatField.setText("Heat Status: Warm");
        } else if(comparator >= 0.3 && comparator < 0.5){
            theMap.animateCamera( CameraUpdateFactory.zoomTo( 9f ) );
            heatField.setText("Heat Status: Toasty");
        } else if(comparator >= 0.1 && comparator < 0.3){
            theMap.animateCamera( CameraUpdateFactory.zoomTo( 9.5f ) );
            heatField.setText("Heat Status: HOT");
        } else if(comparator >= 0.03 && comparator < 0.1){
            theMap.animateCamera( CameraUpdateFactory.zoomTo( 11f ) );
            heatField.setText("Heat Status: OH SO HOT IT'S ON FIRE");
        } else if(comparator >= 0.001 && comparator < 0.03){
            theMap.animateCamera( CameraUpdateFactory.zoomTo( 15f ) );
            heatField.setText("Heat Status: OMG YOU'RE SO HOT OHHH YEAHHHHHHHH");
        } else {
            sendIT = new Intent(GameActivity.this, VictoryActivity.class);
            startActivity(sendIT);
        }
    }

    private void addHint(){
        String hint = "Hint #" + (hintIndex + 1) + ": ";
        hint = hint + hints[locationIndex][hintIndex] + "\n";
        hintField.append(hint);
        hintIndex += 1;
    }

    private void getFinalCoord() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int currentIndex = 0;
                int nextIndex;

                JSONObject jsonRequest = null;

                String full_URL = new String(BASE_URL);
                String address = locations[locationIndex];
                String hold;
                String result = "";
                String line;

                int indexOfLastSpace = address.lastIndexOf(" ");

                nextIndex = address.indexOf(" ", currentIndex);
                hold = address.substring(currentIndex, nextIndex);
                full_URL = full_URL + hold;
                currentIndex = nextIndex;

                while (currentIndex != indexOfLastSpace) {
                    nextIndex = address.indexOf(" ", currentIndex + 1);
                    hold = address.substring(currentIndex + 1, nextIndex);
                    full_URL = full_URL + "+" + hold;
                    currentIndex = nextIndex;
                }

                hold = address.substring(currentIndex + 1, address.length());
                full_URL = full_URL + "+" + hold;

                full_URL = full_URL + API_KEY;

                Log.d(TAG, "Final Stop: " + full_URL);

                try {
                    URL url = new URL(full_URL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    while ((line = bufferedReader.readLine()) != null)
                        result += (line);
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    Log.d(TAG, result);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    jsonRequest = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray first = jsonRequest.getJSONArray("results");
                    JSONObject second = first.getJSONObject(0);
                    //formattedAddress = second.getString("formatted_address");
                    JSONObject third = second.getJSONObject("geometry");
                    JSONObject jsonObject = third.getJSONObject("location");
                    finalLat = Double.parseDouble(jsonObject.getString("lat"));
                    finalLng = Double.parseDouble(jsonObject.getString("lng"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
