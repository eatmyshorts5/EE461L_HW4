package com.example.daniel.ee461l_hw4;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class MarkActivity extends AppCompatActivity {

    public static final String TAG = "RegClient";

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    public static final String API_KEY = "&key=AIzaSyBZZWrbLTwf5hApkWsjvfBeche0Gp0bOPQ";

    private EditText addressField;

    private double lat;
    private double lng;
    private String formattedAddress;

    public Intent sendShit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);

        addressField = findViewById(R.id.address);

        sendShit = new Intent(MarkActivity.this, activity_map.class);

        Button convertButton = findViewById(R.id.convert_button);
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convert();
            }
        });

    }

    private void convert(){
        Log.d(TAG, "Entered function");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int currentIndex = 0;
                int nextIndex;

                JSONObject jsonRequest = null;

                String full_URL = new String(BASE_URL);
                String address = addressField.getText().toString();
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
                    formattedAddress = second.getString("formatted_address");
                    JSONObject third = second.getJSONObject("geometry");
                    JSONObject jsonObject = third.getJSONObject("location");
                    lat = Double.parseDouble(jsonObject.getString("lat"));
                    lng = Double.parseDouble(jsonObject.getString("lng"));
                    sendShit.putExtra("Latitude", lat);
                    sendShit.putExtra("Longitude", lng);
                    sendShit.putExtra("Formatted_Address", formattedAddress);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                sendShit.putExtra("Behavior", "Single");

                startActivity(sendShit);

            }
        });
    }

}
