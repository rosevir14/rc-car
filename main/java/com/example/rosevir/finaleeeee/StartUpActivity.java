package com.example.rosevir.finaleeeee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartUpActivity extends AppCompatActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_start_up);

        Button btnManual = (Button) findViewById(R.id.btnManual);
        Button btnAuto = (Button) findViewById(R.id.btnAuto);

        btnManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(StartUpActivity.this, MainActivity.class);
                StartUpActivity.this.startActivity(myIntent);
            }
        });

        btnAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(StartUpActivity.this, AutoPilot.class);
                StartUpActivity.this.startActivity(myIntent);
            }
        });
    }
}
