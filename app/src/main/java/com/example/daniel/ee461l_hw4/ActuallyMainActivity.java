package com.example.daniel.ee461l_hw4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActuallyMainActivity extends AppCompatActivity {

    Intent sendIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actuallymainactivity);

        Button convertButton = findViewById(R.id.convert_button);
        Button directionButton = findViewById(R.id.direction_button);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIT = new Intent(ActuallyMainActivity.this, MarkActivity.class);
                startActivity(sendIT);
            }
        });

        directionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIT = new Intent(ActuallyMainActivity.this, MainActivity.class);
                startActivity(sendIT);
            }
        });
    }
}
