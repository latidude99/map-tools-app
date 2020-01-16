package com.latidude99.maptools.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.latidude99.maptools.R;
import com.latidude99.maptools.activity.distance.DistanceActivity;
import com.latidude99.maptools.activity.scale.ScaleActivity;
import com.latidude99.maptools.activity.unit.UnitActivity;

public class MapToolsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_tools);
        final String pro = getString(R.string.pro);

        Button btnUnit = findViewById(R.id.btn_unit);
        final Intent unitActivityIntent = new Intent(getApplicationContext(), UnitActivity.class);
        btnUnit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(unitActivityIntent);
            }
        });

        Button btnScale = findViewById(R.id.btn_scale);
        final Intent scaleActivityIntent = new Intent(getApplicationContext(), ScaleActivity.class);
        btnScale.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(scaleActivityIntent);
            }
        });

        Button btnDistance = findViewById(R.id.btn_distance);
        final Intent distanceActivityIntent = new Intent(getApplicationContext(), DistanceActivity.class);
        btnDistance.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(distanceActivityIntent);
            }
        });

        Button btnCoords = findViewById(R.id.btn_coords);
        btnCoords.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), pro, Toast.LENGTH_SHORT).show();
            }
        });

        Button btnArea = findViewById(R.id.btn_area);
        btnArea.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), pro, Toast.LENGTH_SHORT).show();
            }
        });




        /* Crashlytics forced crash

        Button crashButton = new Button(this);
        crashButton.setText("Crash!");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Crashlytics.getInstance().crash(); // Force a crash
            }
        });

        addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        */
    }
}
