package com.example.daniel.ee461l_hw4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.games.Game;

public class InstructionActivity extends AppCompatActivity {

    Intent sendIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        Button playButton = findViewById(R.id.play_button);
        Button homeButton = findViewById(R.id.home_button);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIT = new Intent(InstructionActivity.this, GameActivity.class);
                startActivity(sendIT);
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIT = new Intent(InstructionActivity.this, ActuallyMainActivity.class);
                startActivity(sendIT);
            }
        });
    }
}
