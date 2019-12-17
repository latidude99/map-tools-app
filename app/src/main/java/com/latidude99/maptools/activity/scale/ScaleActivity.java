package com.latidude99.maptools.activity.scale;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.latidude99.maptools.R;

import androidx.appcompat.app.AppCompatActivity;

public class ScaleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale);

        Button btnFractional = (Button) findViewById(R.id.btn_fractional);
        final Intent fractionalIntent = new Intent(this, FractionalScaleActivity.class);
        btnFractional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(fractionalIntent);
            }
        });

        Button btnIntomile = (Button) findViewById(R.id.btn_intomile);
        final Intent inchtomileIntent = new Intent(this, InchtomileScaleActivity.class);
        btnIntomile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(inchtomileIntent);
            }
        });

        Button btnMiletoinch = (Button) findViewById(R.id.btn_miletoin);
        final Intent miletoinchIntent = new Intent(this, MiletoinchScaleActivity.class);
        btnMiletoinch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(miletoinchIntent);
            }
        });

        Button btnCmtokm = (Button) findViewById(R.id.btn_cmtokm);
        final Intent cmtokmIntent = new Intent(this, CmtokmScaleActivity.class);
        btnCmtokm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(cmtokmIntent);
            }
        });

        Button btnKmtocm = (Button) findViewById(R.id.btn_kmtocm);
        final Intent kmtocmIntent = new Intent(this, KmtocmScaleActivity.class);
        btnKmtocm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(kmtocmIntent);
            }
        });

    }
}
