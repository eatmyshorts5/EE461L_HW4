package com.example.daniel.ee461l_hw4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import android.content.Intent;
import android.content.Context;

public class MainActivity extends AppCompatActivity {
    Context hi = this;
    public static final String TAG = "RegClient";

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    public static final String DIR_BASE_URL = "https://maps.googleapis.com/maps/api/directions/json?origin=";
    public static final String API_KEY = "&key=AIzaSyBZZWrbLTwf5hApkWsjvfBeche0Gp0bOPQ";
    public static final String DIR_API_KEY = "&key=AIzaSyAXEJ2j4aXKIA8_CMBKcHMO7Ml6A_ShUdQ";

    private EditText addressField;
    private EditText destinationField;

    public Intent sendShit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addressField = findViewById(R.id.address);
        destinationField = findViewById(R.id.address1);

        sendShit = new Intent(MainActivity.this, activity_map.class);

        Button convertButton = findViewById(R.id.convert_button);
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directions();
            }
        });

    }

    private void directions(){
        String address = addressField.getText().toString();
        String destination = destinationField.getText().toString();

        sendShit.putExtra("Behavior", "Directions");
        sendShit.putExtra("Current", address);
        sendShit.putExtra("Destination", destination);

        startActivity(sendShit);
    }
}
