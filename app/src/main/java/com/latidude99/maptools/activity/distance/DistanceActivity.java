package com.latidude99.maptools.activity.distance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.latidude99.maptools.R;

public class DistanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distancecalculator);

        Button btnMapScale = (Button) findViewById(R.id.btn_map_scale);
        final Intent distanceMapScaleIntent = new Intent(this, DistanceMapScaleActivity.class);
        btnMapScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(distanceMapScaleIntent);
            }
        });

        Button btnGroundScale = (Button) findViewById(R.id.btn_ground_scale);
        final Intent distanceGroundScaleIntent = new Intent(this, DistanceGroundScaleActivity.class);
        btnGroundScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(distanceGroundScaleIntent);
            }
        });

        Button btnMapGround = (Button) findViewById(R.id.btn_map_ground);
        final Intent distanceMapGroundIntent = new Intent(this, DistanceMapScaleActivity.class);
        btnMapGround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(distanceMapGroundIntent);
            }
        });

    }
}
