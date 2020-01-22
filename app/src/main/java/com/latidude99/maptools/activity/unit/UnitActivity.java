package com.latidude99.maptools.activity.unit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.latidude99.maptools.R;

public class UnitActivity extends AppCompatActivity {
    String COMING_SOON = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);

        COMING_SOON = getString(R.string.coming_soon);

        Button btnLength = findViewById(R.id.btn_length);
        final Intent lengthActivityIntent = new Intent(getApplicationContext(), Unit_Length_Activity.class);
        btnLength.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(lengthActivityIntent);
            }
        });

        Button btnArea = findViewById(R.id.btn_area);
       // final Intent areaActivityIntent = new Intent(getApplicationContext(), Unit_Area_Activity.class);
        btnArea.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //startActivity(areaActivityIntent);
                Toast.makeText(getApplicationContext(), COMING_SOON, Toast.LENGTH_SHORT).show();
            }
        });

        Button btnAngle = findViewById(R.id.btn_angle);
        // final Intent areaActivityIntent = new Intent(getApplicationContext(), Unit_Area_Activity.class);
        btnAngle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //startActivity(areaActivityIntent);
                Toast.makeText(getApplicationContext(), COMING_SOON, Toast.LENGTH_SHORT).show();
            }
        });

        Button btnWeight = findViewById(R.id.btn_weight);
        // final Intent areaActivityIntent = new Intent(getApplicationContext(), Unit_Area_Activity.class);
        btnWeight.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //startActivity(areaActivityIntent);
                Toast.makeText(getApplicationContext(), COMING_SOON, Toast.LENGTH_SHORT).show();
            }
        });

        Button btnSpeed = findViewById(R.id.btn_speed);
        // final Intent areaActivityIntent = new Intent(getApplicationContext(), Unit_Area_Activity.class);
        btnSpeed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //startActivity(areaActivityIntent);
                Toast.makeText(getApplicationContext(), COMING_SOON, Toast.LENGTH_SHORT).show();
            }
        });

        Button btnVolume = findViewById(R.id.btn_volume);
        // final Intent areaActivityIntent = new Intent(getApplicationContext(), Unit_Area_Activity.class);
        btnVolume.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //startActivity(areaActivityIntent);
                Toast.makeText(getApplicationContext(), COMING_SOON, Toast.LENGTH_SHORT).show();
            }
        });

        Button btnTemperature = findViewById(R.id.btn_temp);
        // final Intent areaActivityIntent = new Intent(getApplicationContext(), Unit_Area_Activity.class);
        btnTemperature.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //startActivity(areaActivityIntent);
                Toast.makeText(getApplicationContext(), COMING_SOON, Toast.LENGTH_SHORT).show();
            }
        });

        Button btnPressure = findViewById(R.id.btn_pressure);
        // final Intent areaActivityIntent = new Intent(getApplicationContext(), Unit_Area_Activity.class);
        btnPressure.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //startActivity(areaActivityIntent);
                Toast.makeText(getApplicationContext(), COMING_SOON, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
