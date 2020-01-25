package com.latidude99.maptools.activity.unit;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.latidude99.maptools.R;
import com.latidude99.maptools.model.scale.InsufficientParameterException;
import com.latidude99.maptools.model.unit.AreaEntry;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Unit_Area_Activity extends AppCompatActivity {
    Locale locale = Locale.getDefault();
    EditText textInputArea;
    Spinner spinnerInputAreaUnits;
    TextView textAreaUM;
    TextView textAreaMM;
    TextView textAreaCM;
    TextView textAreaIN;
    TextView textAreaFT;
    TextView textAreaYD;
    TextView textAreaM;
    TextView textAreaARES;
    TextView textAreaACRES;
    TextView textAreaHECTARES;
    TextView textAreaKM;
    TextView textAreaMI;

    List<TextView> resultList;

    private String ERROR_INPUT_INT;
    private String ERROR_INPUT_EMPTY;
    private String ERROR_INPUT_0;
    private String ERROR_INPUT_1;
    private String ERROR_INPUT_TOO_BIG;
    private String ERROR_UNIT_CONVERSION;

    AreaEntry areaEntry;

    // used to store info/calculation when orientation changes
    private boolean inputCleared;
    private boolean converted;
    private String storedAreaOut;  //  area in sq micrometres
    private int storedUnit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_area);

        ERROR_INPUT_INT = getString(R.string.error_input_int);
        ERROR_INPUT_EMPTY = getString(R.string.error_input_empty);
        ERROR_INPUT_0 = getString(R.string.error_input_zero);
        ERROR_INPUT_1 = getString(R.string.error_input_one);
        ERROR_INPUT_TOO_BIG = getString(R.string.error_input_too_big);
        ERROR_UNIT_CONVERSION = getString(R.string.error_unit_conversion);

        textInputArea = (EditText) findViewById(R.id.unit_area_value);
        spinnerInputAreaUnits = (Spinner) findViewById(R.id.unit_area_units);
        textAreaUM = (TextView) findViewById(R.id.converted_area_um);
        textAreaMM = (TextView) findViewById(R.id.converted_area_mm);
        textAreaCM = (TextView) findViewById(R.id.converted_area_cm);
        textAreaIN = (TextView) findViewById(R.id.converted_area_in);
        textAreaFT = (TextView) findViewById(R.id.converted_area_ft);
        textAreaYD = (TextView) findViewById(R.id.converted_area_yd);
        textAreaM = (TextView) findViewById(R.id.converted_area_m);
        textAreaARES = (TextView) findViewById(R.id.converted_area_ares);
        textAreaACRES = (TextView) findViewById(R.id.converted_area_acres);
        textAreaHECTARES = (TextView) findViewById(R.id.converted_area_hectares);
        textAreaKM = (TextView) findViewById(R.id.converted_area_km);
        textAreaMI = (TextView) findViewById(R.id.converted_area_statue_mile);

        createResultList(); // to manipulate display results visuals easier

        if("en_US".equals(locale.toString()) || "en_UK".equals(locale.toString()))
            spinnerInputAreaUnits.setSelection(8);
        else
            spinnerInputAreaUnits.setSelection(9);

        System.out.println(locale);
        System.out.println(spinnerInputAreaUnits.getSelectedItemPosition());

        restoreCalculations(savedInstanceState);

        textInputArea.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearAllTextResultStyle();
                    processInputAndConvertArea(view);
                    return true;
                }
                return false;
            }
        });

        Button btnConvert = (Button) findViewById(R.id.btn_convert);
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stringInput = textInputArea.getText().toString().trim();
                clearAllTextResultStyle();
                processInputAndConvertArea(view);
            }
        });

        // 1st click clears converted area fields
        // 2nd click clears entered value
        Button btnClear = (Button) findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View view) {
                if(!inputCleared){
                    textInputArea.requestFocus();
                    showKeyboard(view);
                    inputCleared = true;

                }
                else{
                    textInputArea.setText("");
                    clearTextFields();
                    converted = false;
                }
            }
        });

        textInputArea.requestFocus();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (converted) {
            System.out.println("save, if, computed: " + converted);
            outState.putBoolean("inputCleared", inputCleared);
            outState.putBoolean("converted", converted);
            outState.putString("storedAreaOut", storedAreaOut);
            outState.putInt("storedUnitOut", storedUnit);
        } else {
            System.out.println("save, else, converted: " + converted);
            outState.putBoolean("inputCleared", inputCleared);
            outState.putBoolean("converted", converted);
            outState.putString("storedAreaOut", textInputArea.getText().toString());
            outState.putInt("storedUnitOut", spinnerInputAreaUnits.getSelectedItemPosition());
        }
    }


    // ---------------------- end onCreate() and similar inherited methods ----------------------

    private void restoreCalculations(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            converted = savedInstanceState.getBoolean("converted");
            inputCleared = savedInstanceState.getBoolean("inputCleared");
            if(converted){ // && !inputCleared){
                storedAreaOut = savedInstanceState.getString("storedAreaOut");
                storedUnit = savedInstanceState.getInt("storedUnitOut");
                System.out.println("restore, if, computed: " + converted);
                spinnerInputAreaUnits.setSelection(storedUnit);
                convertAreaAndDisplay(storedAreaOut, storedUnit);
            }else{
                System.out.println("restore, else, computed: " + converted);
                textInputArea.setText(savedInstanceState.getString("storedAreaOut"));
                spinnerInputAreaUnits.setSelection(savedInstanceState.getInt("storedUnitOut"));
            }
        }
    }


    private void processInputAndConvertArea(View view){
        String stringInputArea = textInputArea.getText().toString();

        System.out.println("stringInputArea: " + stringInputArea);

        if("".equals(stringInputArea)){
            Toast.makeText(this, ERROR_INPUT_EMPTY,Toast.LENGTH_SHORT).show();
        }else if ("0".equals(stringInputArea)){
            Toast.makeText(this, ERROR_INPUT_0,Toast.LENGTH_SHORT).show();
        }else {
            int positionInputAreaUnit = spinnerInputAreaUnits.getSelectedItemPosition();

            convertAreaAndDisplay(stringInputArea, positionInputAreaUnit);
            hideKeyboard(view);
            converted = true;
            inputCleared = false;
            storedAreaOut = stringInputArea;
            storedUnit = positionInputAreaUnit;
        }
    }

    private void convertAreaAndDisplay(String stringInput, int unit){
        double value = convertStringToDoubleInput(stringInput);
        try{
            switch(unit){
                case 0: // um2
                    areaEntry = new AreaEntry.Builder()
                            .setMicrometres2AndConvertAll(value)
                            .build();
                    changeTextStyle(textAreaUM);
                    break;
                case 1: // mm2
                    areaEntry = new AreaEntry.Builder()
                            .setMillimetres2AndConvertAll(value)
                            .build();
                    changeTextStyle(textAreaMM);
                    break;
                case 2: // cm2
                    areaEntry = new AreaEntry.Builder()
                            .setCentimetres2AndConvertAll(value)
                            .build();
                    changeTextStyle(textAreaCM);
                    break;
                case 3: // in2
                    areaEntry = new AreaEntry.Builder()
                            .setInches2AndConvertAll(value)
                            .build();
                    changeTextStyle(textAreaIN);
                    break;
                case 4: // ft2
                    areaEntry = new AreaEntry.Builder()
                            .setFeet2AndConvertAll(value)
                            .build();
                    changeTextStyle(textAreaFT);
                    break;
                case 5: // yd2
                    areaEntry = new AreaEntry.Builder()
                            .setYards2AndConvertAll(value)
                            .build();
                    changeTextStyle(textAreaYD);
                    break;
                case 6: // m2
                    areaEntry = new AreaEntry.Builder()
                            .setMetres2AndConvertAll(value)
                            .build();
                    changeTextStyle(textAreaM);
                    break;
                case 7: // are
                    areaEntry = new AreaEntry.Builder()
                            .setAresAndConvertAll(value)
                            .build();
                    changeTextStyle(textAreaARES);
                    break;
                case 8: // acre
                    areaEntry = new AreaEntry.Builder()
                            .setAcresAndConvertAll(value)
                            .build();
                    changeTextStyle(textAreaACRES);
                    break;
                case 9: // ha
                    areaEntry = new AreaEntry.Builder()
                            .setHectaresAndConvertAll(value)
                            .build();
                    changeTextStyle(textAreaHECTARES);
                    break;
                case 10: // km2
                    areaEntry = new AreaEntry.Builder()
                            .setKilometres2AndConvertAll(value)
                            .build();
                    changeTextStyle(textAreaKM);
                    break;
                case 11: // mi2
                    areaEntry = new AreaEntry.Builder()
                            .setMiles2AndConvertAll(value)
                            .build();
                    changeTextStyle(textAreaMI);
                    break;
            }
            if(areaEntry != null)
                displayConvertedValues(areaEntry);
            else
                clearTextFields();
        }catch (InsufficientParameterException e){
            e.printStackTrace();
            Toast.makeText(this, ERROR_UNIT_CONVERSION, Toast.LENGTH_SHORT);
        }
    }

    private void displayConvertedValues(AreaEntry areaEntry){
        textAreaUM.setText(new DecimalFormat("#,##0.########################").format(areaEntry.getUm2()));
        textAreaMM.setText(new DecimalFormat("#,##0.0#######################").format(areaEntry.getMm2()));
        textAreaCM.setText(new DecimalFormat("#,##0.0#######################").format(areaEntry.getCm2()));
        textAreaIN.setText(new DecimalFormat("#,##0.0#######################").format(areaEntry.getIn2()));
        textAreaFT.setText(new DecimalFormat("#,##0.0#######################").format(areaEntry.getFt2()));
        textAreaYD.setText(new DecimalFormat("#,##0.0#######################").format(areaEntry.getYd2()));
        textAreaM.setText(new DecimalFormat("#,##0.0#######################").format(areaEntry.getM2()));
        textAreaARES.setText(new DecimalFormat("#,##0.0#######################").format(areaEntry.getAre()));
        textAreaACRES.setText(new DecimalFormat("#,##0.0#######################").format(areaEntry.getAcre()));
        textAreaHECTARES.setText(new DecimalFormat("#,##0.0#######################").format(areaEntry.getHa()));
        textAreaKM.setText(new DecimalFormat("#,##0.0#######################").format(areaEntry.getKm2()));
        textAreaMI.setText(new DecimalFormat("#,##0.0#######################").format(areaEntry.getMi2()));
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
        areaEntry = null;
        inputCleared = true;
    }

    private void clearAllTextResultStyle(){
        for(TextView view : resultList)
            clearTextStyle(view);
    }

    private void changeTextStyle(TextView view){
        view.setTypeface(null, Typeface.BOLD);
        view.setTextColor(Color.RED);
        view.setBackgroundColor(Color.LTGRAY);
    }


    private void clearTextStyle(TextView view){
        view.setTypeface(null, Typeface.NORMAL);
        view.setTextColor(getResources().getColor(R.color.tab1_result));
        view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    private void createResultList() {
        resultList = new ArrayList<>();
        resultList.add(textAreaUM);
        resultList.add(textAreaMM);
        resultList.add(textAreaCM);
        resultList.add(textAreaIN);
        resultList.add(textAreaFT);
        resultList.add(textAreaYD);
        resultList.add(textAreaM);
        resultList.add(textAreaARES);
        resultList.add(textAreaACRES);
        resultList.add(textAreaHECTARES);
        resultList.add(textAreaKM);
        resultList.add(textAreaMI);
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
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
        }
    }


}
























