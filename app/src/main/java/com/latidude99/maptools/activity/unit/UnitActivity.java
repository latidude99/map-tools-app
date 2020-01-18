package com.latidude99.maptools.activity.unit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.latidude99.maptools.R;

public class UnitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);

        Button btnLength = findViewById(R.id.btn_length);
        final Intent lengthActivityIntent = new Intent(getApplicationContext(), Unit_Length_Activity.class);
        btnLength.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(lengthActivityIntent);
            }
        });
    }
}
