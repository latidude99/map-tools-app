package com.latidude99.maptools.activity.unit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.latidude99.maptools.R;
import com.latidude99.maptools.model.scale.InsufficientParameterException;
import com.latidude99.maptools.model.scale.Scale;
import com.latidude99.maptools.model.unit.LengthEntry;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Unit_Length_Activity extends AppCompatActivity {
    Locale locale = Locale.getDefault();
    EditText textInputLength;
    Spinner spinnerInputLengthUnits;
    TextView textLengthUM;
    TextView textLengthMM;
    TextView textLengthCM;
    TextView textLengthIN;
    TextView textLengthFT;
    TextView textLengthYD;
    TextView textLengthM;
    TextView textLengthFTM;
    TextView textLengthKM;
    TextView textLengthMI;
    TextView textLengthNM;

    private String ERROR_INPUT_INT;
    private String ERROR_INPUT_EMPTY;
    private String ERROR_INPUT_0;
    private String ERROR_INPUT_1;
    private String ERROR_INPUT_TOO_BIG;
    private String ERROR_UNIT_CONVERSION;

    LengthEntry lengthEntry;
    List<TextView> resultList;

    // used to store info/calculation when orientation changes
    private boolean inputCleared;
    private boolean converted;
    private String storedLengthOut;  //  length in micrometres
    private int storedUnit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_length);

        ERROR_INPUT_INT = getString(R.string.error_input_int);
        ERROR_INPUT_EMPTY = getString(R.string.error_input_empty);
        ERROR_INPUT_0 = getString(R.string.error_input_zero);
        ERROR_INPUT_1 = getString(R.string.error_input_one);
        ERROR_INPUT_TOO_BIG = getString(R.string.error_input_too_big);
        ERROR_UNIT_CONVERSION = getString(R.string.error_unit_conversion);

        textInputLength = (EditText) findViewById(R.id.unit_length_value);
        spinnerInputLengthUnits = (Spinner) findViewById(R.id.unit_length_units);
        textLengthUM = (TextView) findViewById(R.id.converted_length_um);
        textLengthMM = (TextView) findViewById(R.id.converted_length_mm);
        textLengthCM = (TextView) findViewById(R.id.converted_length_cm);
        textLengthIN = (TextView) findViewById(R.id.converted_length_in);
        textLengthFT = (TextView) findViewById(R.id.converted_length_ft);
        textLengthYD = (TextView) findViewById(R.id.converted_length_yd);
        textLengthM = (TextView) findViewById(R.id.converted_length_m);
        textLengthFTM = (TextView) findViewById(R.id.converted_length_ftm);
        textLengthKM = (TextView) findViewById(R.id.converted_length_km);
        textLengthMI = (TextView) findViewById(R.id.converted_length_statue_mile);
        textLengthNM = (TextView) findViewById(R.id.converted_length_nautical_mile);

        createResultList();

        if("en_US".equals(locale.toString()) || "en_UK".equals(locale.toString())
                || "US".equals(locale.getCountry()) || "UK".equals(locale.getCountry()))
            spinnerInputLengthUnits.setSelection(9);
        else
            spinnerInputLengthUnits.setSelection(8);

        restoreCalculations(savedInstanceState);

        textInputLength.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearAllTextResultStyle();
                    processInputAndConvertLength(view);
                    return true;
                }
                return false;
            }
        });

        spinnerInputLengthUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clearAllTextResultStyle();
                processInputAndConvertLength(view);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        Button btnConvert = (Button) findViewById(R.id.btn_convert);
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stringInput = textInputLength.getText().toString().trim();
                clearAllTextResultStyle();
                processInputAndConvertLength(view);
            }
        });

        // 1st click clears converted length fields
        // 2nd click clears entered value
        Button btnClear = (Button) findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View view) {
                if(!inputCleared){
                    textInputLength.requestFocus();
                    showKeyboard(view);
                    inputCleared = true;

                }
                else{
                    textInputLength.setText("");
                    clearTextFields();
                    converted = false;
                }
            }
        });

        textInputLength.requestFocus();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (converted) {
            System.out.println("save, if, computed: " + converted);
            outState.putBoolean("inputCleared", inputCleared);
            outState.putBoolean("converted", converted);
            outState.putString("storedLengthOut", storedLengthOut);
            outState.putInt("storedUnitOut", storedUnit);
        } else {
            System.out.println("save, else, converted: " + converted);
            outState.putBoolean("inputCleared", inputCleared);
            outState.putBoolean("converted", converted);
            outState.putString("storedLengthOut", textInputLength.getText().toString());
            outState.putInt("storedUnitOut", spinnerInputLengthUnits.getSelectedItemPosition());
        }
    }


    // ---------------------- end onCreate() and similar inherited methods ----------------------

    private void restoreCalculations(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            converted = savedInstanceState.getBoolean("converted");
            inputCleared = savedInstanceState.getBoolean("inputCleared");
            if(converted){ // && !inputCleared){
                storedLengthOut = savedInstanceState.getString("storedLengthOut");
                storedUnit = savedInstanceState.getInt("storedUnitOut");
                System.out.println("restore, if, computed: " + converted);
                spinnerInputLengthUnits.setSelection(storedUnit);
                convertLengthAndDisplay(storedLengthOut, storedUnit);
            }else{
                System.out.println("restore, else, computed: " + converted);
                textInputLength.setText(savedInstanceState.getString("storedLengthOut"));
                spinnerInputLengthUnits.setSelection(savedInstanceState.getInt("storedUnitOut"));
            }
        }
    }


    private void processInputAndConvertLength(View view){
        String stringInputLength = textInputLength.getText().toString();

        System.out.println("stringInputLength: " + stringInputLength);

        if("".equals(stringInputLength)){
            Toast.makeText(this, ERROR_INPUT_EMPTY,Toast.LENGTH_SHORT).show();
        }else if ("0".equals(stringInputLength)){
            Toast.makeText(this, ERROR_INPUT_0,Toast.LENGTH_SHORT).show();
        }else {
            int positionInputLengthUnit = spinnerInputLengthUnits.getSelectedItemPosition();

            convertLengthAndDisplay(stringInputLength, positionInputLengthUnit);
            hideKeyboard(view);
            converted = true;
            inputCleared = false;
            storedLengthOut = stringInputLength;
            storedUnit = positionInputLengthUnit;
        }
    }

    private void convertLengthAndDisplay(String stringInput, int unit){
        double value = convertStringToDoubleInput(stringInput);
        try{
            switch(unit){
                case 0: // um
                    lengthEntry = new LengthEntry.Builder()
                            .setMicrometresAndConvertAll(value)
                            .build();
                    changeTextStyle(textLengthUM);
                    break;
                case 1: // mm
                    lengthEntry = new LengthEntry.Builder()
                            .setMillimetresAndConvertAll(value)
                            .build();
                    changeTextStyle(textLengthMM);
                    break;
                case 2: // cm
                    lengthEntry = new LengthEntry.Builder()
                            .setCentimetresAndConvertAll(value)
                            .build();
                    changeTextStyle(textLengthCM);
                    break;
                case 3: // in
                    lengthEntry = new LengthEntry.Builder()
                            .setInchesAndConvertAll(value)
                            .build();
                    changeTextStyle(textLengthIN);
                    break;
                case 4: // ft
                    lengthEntry = new LengthEntry.Builder()
                            .setFeetAndConvertAll(value)
                            .build();
                    changeTextStyle(textLengthFT);
                    break;
                case 5: // yd
                    lengthEntry = new LengthEntry.Builder()
                            .setYardsAndConvertAll(value)
                            .build();
                    changeTextStyle(textLengthYD);
                    break;
                case 6: // m
                    lengthEntry = new LengthEntry.Builder()
                            .setMetresAndConvertAll(value)
                            .build();
                    changeTextStyle(textLengthM);
                    break;
                case 7: // ftm
                    lengthEntry = new LengthEntry.Builder()
                            .setFathomsAndConvertAll(value)
                            .build();
                    changeTextStyle(textLengthFTM);
                    break;
                case 8: // km
                    lengthEntry = new LengthEntry.Builder()
                            .setKilometresAndConvertAll(value)
                            .build();
                    changeTextStyle(textLengthKM);
                    break;
                case 9: // mi
                    lengthEntry = new LengthEntry.Builder()
                            .setMilesAndConvertAll(value)
                            .build();
                    changeTextStyle(textLengthMI);
                    break;
                case 10: // nm
                    lengthEntry = new LengthEntry.Builder()
                            .setNauticalMilesAndConvertAll(value)
                            .build();
                    changeTextStyle(textLengthNM);
                    break;
            }
            if(lengthEntry != null)
                displayConvertedValues(lengthEntry);
            else
                clearTextFields();
        }catch (InsufficientParameterException e){
            e.printStackTrace();
            Toast.makeText(this, ERROR_UNIT_CONVERSION, Toast.LENGTH_SHORT);
        }
    }

    private void displayConvertedValues(LengthEntry lengthEntry){
        textLengthUM.setText(new DecimalFormat("#,##0.###").format(lengthEntry.getUm()));
        textLengthMM.setText(new DecimalFormat("#,##0.0#####").format(lengthEntry.getMm()));
        textLengthCM.setText(new DecimalFormat("#,##0.0######").format(lengthEntry.getCm()));
        textLengthIN.setText(new DecimalFormat("#,##0.0#########").format(lengthEntry.getIn()));
        textLengthFT.setText(new DecimalFormat("#,##0.0##########").format(lengthEntry.getFt()));
        textLengthYD.setText(new DecimalFormat("#,##0.0###########").format(lengthEntry.getYd()));
        textLengthM.setText(new DecimalFormat("#,##0.0#############").format(lengthEntry.getM()));
        textLengthFTM.setText(new DecimalFormat("#,##0.0###########").format(lengthEntry.getFtm()));
        textLengthKM.setText(new DecimalFormat("#,##0.0###############").format(lengthEntry.getKm()));
        textLengthMI.setText(new DecimalFormat("#,##0.0################").format(lengthEntry.getMi()));
        textLengthNM.setText(new DecimalFormat("#,##0.0################").format(lengthEntry.getNm()));
    }

    private double convertStringToDoubleInput(String input){
        double doubleInput = 0;
        if(input != null){
            try{
                doubleInput = Double.parseDouble(input);
            }catch(NumberFormatException e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), ERROR_INPUT_INT, Toast.LENGTH_SHORT).show();
            }
            if(doubleInput <= 0)
                Toast.makeText(this, ERROR_INPUT_0, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, ERROR_INPUT_EMPTY, Toast.LENGTH_SHORT).show();
        }
        return doubleInput;
    }

    private void clearTextFields(){
        //   resultList.forEach(v -> v.setText(""));  // needs API >24
        for(TextView view : resultList)
            view.setText("");
        lengthEntry = null;
        inputCleared = true;
    }

    private void clearAllTextResultStyle(){
        for(TextView view : resultList)
            clearTextStyle(view);
    }

    private void changeTextStyle(TextView view){
        view.setPaddingRelative(0, 0, 10, 0);
        view.setTypeface(null, Typeface.BOLD);
        view.setTextColor(getResources().getColor(R.color.result_text_active));
        view.setBackgroundColor(getResources().getColor(R.color.result_background));
    }


    private void clearTextStyle(TextView view){
        view.setPaddingRelative(0, 0, 0, 0);
        view.setTypeface(null, Typeface.NORMAL);
        view.setTextColor(getResources().getColor(R.color.result_text_regular));
        view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    private void createResultList() {
        resultList = new ArrayList<>();
        resultList.add(textLengthUM);
        resultList.add(textLengthMM);
        resultList.add(textLengthCM);
        resultList.add(textLengthIN);
        resultList.add(textLengthFT);
        resultList.add(textLengthYD);
        resultList.add(textLengthM);
        resultList.add(textLengthFTM);
        resultList.add(textLengthKM);
        resultList.add(textLengthMI);
        resultList.add(textLengthNM);
    }


    private void hideKeyboard(View view) {
        // Check if no view has focus:
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }

    private void showKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
        }
    }


}
























