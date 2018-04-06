package com.example.daniel.ee461l_hw4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

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
