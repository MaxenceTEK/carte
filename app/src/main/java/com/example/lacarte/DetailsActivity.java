package com.example.lacarte;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DetailsActivity extends AppCompatActivity {
    Button buttono;
    TextView markertxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        markertxt = findViewById(R.id.marker);
        buttono = findViewById(R.id.buttono);
        buttono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MapsActivity.class));
            }
        });
        String title = getIntent().getStringExtra("title");
        markertxt.setText(title);



    }
}