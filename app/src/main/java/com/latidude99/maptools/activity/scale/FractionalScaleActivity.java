package com.latidude99.maptools.activity.scale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.latidude99.maptools.R;
import com.latidude99.maptools.model.scale.InsufficientParameterException;
import com.latidude99.maptools.model.scale.Scale;

import java.text.NumberFormat;
import java.util.Locale;

public class FractionalScaleActivity extends AppCompatActivity {
    boolean clearInput;
    private boolean scaleConverted;
    String ERROR_INPUT_INT;
    String ERROR_INPUT_EMPTY;
    String ERROR_INPUT_0;
    String ERROR_INPUT_1;
    String ERROR_INPUT_TOO_BIG;
    String ERROR_CONVERTING_SCALE;
    private ScrollView layoutScales;
    private String stringInput;
    private EditText textInput;
    private TextView textViewFractional;
    private TextView textViewInToMile;
    private TextView textViewInToMileDescription;
    private TextView textViewMileToIn;
    private TextView textViewMileToInDescription;
    private TextView textViewCmToKm;
    private TextView textViewCmToKmDescription;
    private TextView textViewKmToCM;
    private TextView textViewKmToCMDescription;
    Locale locale = Locale.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scaleconverter_fractional_scale);

        ERROR_INPUT_INT = getString(R.string.error_input_int);
        ERROR_INPUT_EMPTY = getString(R.string.error_input_empty);
        ERROR_INPUT_TOO_BIG = getString(R.string.error_input_too_big);
        ERROR_INPUT_0 = getString(R.string.error_input_zero);
        ERROR_INPUT_1 = getString(R.string.error_input_one);
        ERROR_CONVERTING_SCALE = getString(R.string.error_converting_scale);

        layoutScales = (ScrollView) findViewById(R.id.scales);
        layoutScales.setVisibility(View.INVISIBLE);
        textInput = (EditText) findViewById(R.id.fractional_denominator);
        textViewFractional = (TextView) findViewById(R.id.converted_fractional);
        textViewInToMile = (TextView) findViewById(R.id.converted_in_to_mile);
        textViewInToMileDescription = (TextView) findViewById(R.id.in_to_mile_decription);
        textViewMileToIn = (TextView) findViewById(R.id.converted_mile_to_in);
        textViewMileToInDescription = (TextView) findViewById(R.id.mile_to_in_description);
        textViewCmToKm = (TextView) findViewById(R.id.converted_cm_to_km);
        textViewCmToKmDescription = (TextView) findViewById(R.id.cm_to_km_description);
        textViewKmToCM = (TextView) findViewById(R.id.converted_km_to_cm);
        textViewKmToCMDescription = (TextView) findViewById(R.id.km_to_cm_description);

        textInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && scaleConverted == false) {
                    showKeyboard(v);
                }
            }
        });

        // converts the scale when the 'done'/'ok' button on the soft keyboard pressed
        textInput.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    stringInput = textInput.getText().toString().trim();
                    convertScaleAndDisplay(view, stringInput);
                    return true;
                }
                return false;
            }
        });

        Button btnConvert = (Button) findViewById(R.id.btn_convert);
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringInput = textInput.getText().toString().trim();
                convertScaleAndDisplay(view, stringInput);
            }
        });

        // 1st click clears converted scales' fields
        // 2nd click clears entered value
        Button btnClear = (Button) findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View view) {
                if(clearInput)
                    textInput.setText("");
                else
                    clearTextFields(view);
            }
        });

        // restores converted scales after an orientation change
        restoreScaleConverted(savedInstanceState);

        // shows the keyboard when activity starts
        textInput.requestFocus();

        // hides the keyboard after an orientation change if the scale was converted already
        if(scaleConverted == true)
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void convertScaleAndDisplay(View view, String stringInput) {
        if(!"".equals(stringInput)){
            Scale scale = null;
            try {
                scale = convertFromFractional(stringInput);
            } catch (InsufficientParameterException e) {
                e.printStackTrace();
            }
            hideKeyboard(view);
            if(scale != null)
                displayConvertedScales(scale);
            else
                Toast
                        .makeText(getApplicationContext(), ERROR_CONVERTING_SCALE, Toast.LENGTH_SHORT)
                        .show();
        }else{
            Toast
                    .makeText(getApplicationContext(), ERROR_INPUT_EMPTY, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    // saves the state if the scale was converted already
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(scaleConverted == true){
            outState.putBoolean("scaleConverted", scaleConverted);
            outState.putString("stringInput", stringInput);
        }
    }

    // restores converted scales
    private void restoreScaleConverted(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            scaleConverted = savedInstanceState.getBoolean("scaleConverted");
            if(scaleConverted){
                stringInput = savedInstanceState.getString("stringInput");
                Scale scale = null;
                try {
                    scale = convertFromFractional(stringInput);
                    displayConvertedScales(scale);
                } catch (InsufficientParameterException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Scale convertFromFractional(String stringInput) throws InsufficientParameterException {
        int intInput = convertStringToIntInput(stringInput);
        Scale scale = new Scale.ScaleBuilder()
                .setFractionalAndConvert(intInput)
                .build();
        System.out.println("scale: " + scale);
        return scale;
    }

    private int convertStringToIntInput(String input){
        int denominatorInt = 0;
        long denominatorLong = 0L;
        if(input != null){
            try{
                denominatorLong = Long.parseLong(input);
            }catch(NumberFormatException e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), ERROR_INPUT_INT, Toast.LENGTH_SHORT).show();
            }
            if(denominatorLong < 1)
                Toast.makeText(this, ERROR_INPUT_1, Toast.LENGTH_SHORT).show();
            if(denominatorLong >= 999999999L)
                Toast.makeText(this, ERROR_INPUT_TOO_BIG, Toast.LENGTH_SHORT).show();
            else
                denominatorInt = (int) denominatorLong;
        }else{
            Toast.makeText(this, ERROR_INPUT_EMPTY, Toast.LENGTH_SHORT).show();
        }
        System.out.println("denominator: " + denominatorInt);
        return denominatorInt;
    }


    private void displayConvertedScales(Scale scale){
        setFractionalFormatted(scale);
        setInToMileFormatted(scale);
        setMileToInFormatted(scale);
        setCmToKmFormatted(scale);
        setKmToCmFormatted(scale);
        layoutScales.setVisibility(View.VISIBLE);
        scaleConverted = true;
    }

    // sets the TextView scale fields with converted and formated scales' strings

    public void setFractionalFormatted(Scale scale) {
        textViewFractional.setText(
                "1: " + NumberFormat.getNumberInstance(locale).format(scale.getFractional()));
    }

    public void setInToMileFormatted(Scale scale) {
        if(scale.getInToMile() < 0.1) {
            textViewInToMile.setText("-");
            textViewInToMileDescription.setText("less than 0.1 inch to the mile");
        }
        else{
            textViewInToMile.setText(String.format(locale, "%.1f", scale.getInToMile()));
            textViewInToMileDescription.setText(R.string.in_to_mile);
        }
    }

    public void setMileToInFormatted(Scale scale) {
        if(scale.getMileToIn() < 0.01) {
            textViewMileToIn.setText("-");
            textViewMileToInDescription.setText("less than 0.01 mile to an inch");
        }
        else {
            textViewMileToIn.setText(String.format(locale, "%.2f", scale.getMileToIn()));
            textViewMileToInDescription.setText(R.string.mile_to_in);
        }
    }

    public void setCmToKmFormatted(Scale scale) {
        if(scale.getCmToKm() < 0.1){
            textViewCmToKm.setText("-");
            textViewCmToKmDescription.setText("less than 0.1 cm to a kilometre");
        }
        else{
            textViewCmToKm.setText(String.format(locale,"%.1f", scale.getCmToKm()));
            textViewCmToKmDescription.setText(R.string.cm_to_km);
        }
    }

    public void setKmToCmFormatted(Scale scale) {
        if(scale.getKmToCm() >= 0.1){
         textViewKmToCM.setText(String.format(locale, "%.1f", scale.getKmToCm()));
         textViewKmToCMDescription.setText(R.string.km_to_cm);
        }
        else{
            textViewKmToCM.setText(String.format(locale, "%.1f", scale.getKmToCm()*1000));
            textViewKmToCMDescription.setText(R.string.m_to_cm);
        }
    }

    // clears scales' TextView fields and makes them invisible
    // also it supposed to show the keyboard (not working at the moment)
    private void clearTextFields(View view){
        layoutScales.setVisibility(View.INVISIBLE);
        textViewFractional.setText("");
        textViewInToMile.setText("");
        textViewInToMileDescription.setText("");
        textViewMileToIn.setText("");
        textViewMileToInDescription.setText("");
        textViewCmToKm.setText("");
        textViewCmToKmDescription.setText("");
        textViewKmToCM.setText("");
        textViewKmToCMDescription.setText("");
        scaleConverted = false;
        clearInput = true;
        textInput.requestFocus();
        textInput.setSelectAllOnFocus(true);
        showKeyboard(view);
    }

    private void hideKeyboard(View view) {
        // Check if no view has focus:
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
        view.clearFocus();
    }

    private void showKeyboard(View view) {
        // Check if no view has focus:
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
        }
    }



}
