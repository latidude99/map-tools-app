package com.latidude99.maptools.activity.unit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.latidude99.maptools.R;

public class Unit_Length_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_length);

        TextView textLengthUM = (TextView) findViewById(R.id.converted_length_um);
        textLengthUM.setText("1342222222222222");
    }
}
