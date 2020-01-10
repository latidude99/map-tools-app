package com.latidude99.maptools.activity.distance;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.latidude99.maptools.R;
import com.latidude99.maptools.Utils.UnitConverter;
import com.latidude99.maptools.model.distance.MapEntryAdvanced;
import com.latidude99.maptools.model.scale.InsufficientParameterException;
import com.latidude99.maptools.model.scale.Scale;

import java.util.Locale;

public class DistanceMapScaleActivity extends AppCompatActivity {
    private Locale locale = Locale.getDefault();
    // values used in switches
    private final int SCALE_FRACTIONAL = 0;
    private final int SCALE_INTOMILE = 1;
    private final int SCALE_MILETOIN = 2;
    private final int SCALE_CMTOKM = 3;
    private final int SCALE_KMTOCM = 4;
    private final int DISTANCE_MM = 0;
    private final int DISTANCE_CM = 1;
    private final int DISTANCE_IN = 2;
    private final int DISTANCE_FT = -1; // to be changed when implemented
    private final int DISTANCE_METRE = -2; // to be changed when implemented
    private final int DISTANCE_KM = 0;
    private final int DISTANCE_MILE = 1;
    private final int DISTANCE_NAUTICAL = 2;

    // used to store info/calculation when orientation changes
    private boolean inputCleared;
    private boolean computed;
    private double storedDistanceMapMM;
    private int storedScaleFractional;


    // holds all the calculations for single entry
    MapEntryAdvanced mapEntryAdvanced;

    private String ERROR_INPUT_INT;
    private String ERROR_INPUT_EMPTY;
    private String ERROR_INPUT_EMPTY_ANY;
    private String ERROR_INPUT_0;
    private String ERROR_INPUT_0_ANY;
    private String ERROR_INPUT_1;
    private String ERROR_INPUT_TOO_BIG;
    private String ERROR_UNIT_CONVERSION;
    private String ERROR_CONVERTING_SCALE;

    private EditText textInputDistanceMap;
    private EditText textInputDistanceScale;
    private TextView textResultMapDescription;
    private TextView textResultMap;
    private TextView textResultGroundDescription;
    private TextView textResultGround;
    private TextView textResultScaleDescription;
    private TextView textResultScale;

    // entry controls
    private Spinner spinnerInputDistanceUnit;
    private Spinner spinnerInputScaleType;

    // result controls
    private RadioGroup radioResultGroupDistanceGroundUnit;
    private RadioButton radioResultButtonKm;
    private RadioButton radioResultButtonMile;
    private RadioButton radioResultButtonNautical;

    private RadioGroup radioResultGroupDistanceMapUnit;
    private RadioButton radioResultButtonMm;
    private RadioButton radioResultButtonCm;
    private RadioButton radioResultButtonIn;

    private Spinner spinnerResultScaleType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distancecalculator_map_scale);


        ERROR_INPUT_INT = getString(R.string.error_input_int);
        ERROR_INPUT_EMPTY = getString(R.string.error_input_empty);
        ERROR_INPUT_EMPTY_ANY = getString(R.string.error_input_empty_any);
        ERROR_INPUT_0 = getString(R.string.error_input_zero);
        ERROR_INPUT_0_ANY = getString(R.string.error_input_zero_any);
        ERROR_INPUT_1 = getString(R.string.error_input_one);
        ERROR_INPUT_TOO_BIG = getString(R.string.error_input_too_big);
        ERROR_CONVERTING_SCALE = getString(R.string.error_converting_scale);
        ERROR_UNIT_CONVERSION = getString(R.string.error_unit_conversion);

        textInputDistanceMap = (EditText) findViewById(R.id.distance_map);
        textInputDistanceScale = (EditText) findViewById(R.id.distance_scale);
        textResultMapDescription =  (TextView) findViewById(R.id.distance_result_map_description);
        textResultMap =  (TextView) findViewById(R.id.distance_result_map);
        textResultGroundDescription =  (TextView) findViewById(R.id.distance_result_ground_description);
        textResultGround =  (TextView) findViewById(R.id.distance_result_ground);
        textResultScaleDescription = (TextView) findViewById(R.id.distance_result_scale_description);
        textResultScale = (TextView) findViewById(R.id.distance_result_scale);

        spinnerInputDistanceUnit = (Spinner) findViewById(R.id.distance_distance_unit);
        spinnerInputScaleType = (Spinner) findViewById(R.id.distance_scale_type);

        radioResultGroupDistanceGroundUnit = (RadioGroup) findViewById(R.id.radios_distance_ground_unit);
        radioResultButtonKm = (RadioButton) findViewById(R.id.radio_km);
        radioResultButtonMile = (RadioButton) findViewById(R.id.radio_mile);
        radioResultButtonNautical = (RadioButton) findViewById(R.id.radio_nautical);
        radioResultGroupDistanceMapUnit = (RadioGroup) findViewById(R.id.radios_distance_map_unit);
        radioResultButtonMm = (RadioButton) findViewById(R.id.radio_mm);
        radioResultButtonCm = (RadioButton) findViewById(R.id.radio_cm);
        radioResultButtonIn = (RadioButton) findViewById(R.id.radio_in);

        spinnerResultScaleType = (Spinner) findViewById(R.id.distance_result_scale_type);

        // Deals with screen orientation changes
        restoreCalculations(savedInstanceState);


        // calculates the ground distance when the 'done'/'ok' button on the soft keyboard pressed
        textInputDistanceMap.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    calculateDistanceGround(view);
                    return true;
                }
                return false;
            }
        });

        Button btnCalculateDistanceGround = (Button) findViewById(R.id.btn_calculate);
        btnCalculateDistanceGround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateDistanceGround(view);
            }
        });

        // 1st click clears calculated values
        // 2nd click focuses on textInputDistanceMap and opens the keyboard
        Button btnClear = (Button) findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View view) {
                if(!inputCleared){
                    clearTextFields(view);
                    textInputDistanceMap.setText("");
                    textInputDistanceMap.requestFocus();
                    showKeyboard(view);
                }else{
                    textInputDistanceScale.setText("");
                    textInputDistanceScale.requestFocus();
                }
            }
        });

        // converts result ground unit between each other
        radioResultGroupDistanceGroundUnit.setOnCheckedChangeListener(new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                convertAndSetResultGroundDistance(mapEntryAdvanced, group);
            }
        });

        // converts result map unit between each other
        radioResultGroupDistanceMapUnit.setOnCheckedChangeListener(new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                convertAndSetResultMapDistance(mapEntryAdvanced, group);
            }
        });

        // converts result scale between each scale type
        spinnerResultScaleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedPositionId = spinnerResultScaleType.getSelectedItemPosition();
                convertAndSetResultMapScale(mapEntryAdvanced, selectedPositionId);
                Log.e("Selected item : ", "" + selectedPositionId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        textInputDistanceScale.requestFocus();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(computed == true){
            outState.putBoolean("inputCleared", inputCleared);
            outState.putBoolean("computed", computed);
            outState.putDouble("storedDistanceMapMM", storedDistanceMapMM);
            outState.putInt("storedScaleFractional", storedScaleFractional);
        }
    }

    //----------------------------  end of onCreate() & other overriden methods  ---------------------------------------//



    private void restoreCalculations(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            computed = savedInstanceState.getBoolean("computed");
            inputCleared = savedInstanceState.getBoolean("inputCleared");
            if(computed && !inputCleared){
                storedDistanceMapMM = savedInstanceState.getDouble("storedDistanceMapMM");
                storedScaleFractional = savedInstanceState.getInt("storedScaleFractional");

                calculateAndDisplay();
            }
        }
    }

    private void calculateDistanceGround(View view) {
        String stringInputDistanceMap = textInputDistanceMap.getText().toString();
        String stringInputDistanceScale = textInputDistanceScale.getText().toString();

        if(!"".equals(stringInputDistanceScale) && !"".equals(stringInputDistanceMap)){
            int positionInputDistanceMapUnit = spinnerInputDistanceUnit.getSelectedItemPosition();
            int positionInputScaleType = spinnerInputScaleType.getSelectedItemPosition();

            calculateDistanceGroundAndDisplay(view,
                    stringInputDistanceMap,
                    stringInputDistanceScale,
                    positionInputDistanceMapUnit,
                    positionInputScaleType);
            hideKeyboard(view);
            computed = true;
            inputCleared = false;
        }else{
            Toast.makeText(this, ERROR_INPUT_EMPTY_ANY,Toast.LENGTH_SHORT);
        }
    }


    /*
     *  Calculates ground distance and sets values to the fields,
     *  MapEntry stores mapDistance in mm, groundDistance in km, scale as fractional (denominator)
     */
    private void calculateDistanceGroundAndDisplay(View view,
                                                   String inputMap,
                                                   String inputScale,
                                                   int mapUnit,
                                                   int scaleUnit){
        // converts input to base units and stores them in class fields,
        // (to put them into Bundle, used when screen orientation changes)
        convertInputToBaseUnits(inputMap, inputScale, mapUnit, scaleUnit);

        calculateAndDisplay();
    }

    private void calculateAndDisplay() {
        // calculates and converts internally all map, ground and scale units
        mapEntryAdvanced = new MapEntryAdvanced(storedDistanceMapMM, storedScaleFractional);

        // reads input units and sets result units
        readInputAndSetResultUnits(spinnerInputDistanceUnit, spinnerInputScaleType);

        // sets and displays the result
        setResultDistanceMap(mapEntryAdvanced,radioResultGroupDistanceMapUnit);
        setResultDistanceGround(mapEntryAdvanced, radioResultGroupDistanceGroundUnit);
        setResultMapScale(mapEntryAdvanced, spinnerResultScaleType);
    }

    // converts input values to base units - distance to mm / scale to fractional
    private void convertInputToBaseUnits(String inputMap, String inputScale, int mapUnit, int scaleUnit){
        try {
            storedDistanceMapMM = convertMapDistanceToMM(inputMap, mapUnit);
            storedScaleFractional = convertMapScaleToFractional(inputScale, scaleUnit);
        } catch (InsufficientParameterException e) {
            e.printStackTrace();
        }
    }

    // reads input units and sets result units accordingly
    private void readInputAndSetResultUnits(Spinner spinnerInputMapUnit, Spinner spinnerInputScaleType){
        // map unit
        int inputMapUnit = spinnerInputMapUnit.getSelectedItemPosition();
        switch(inputMapUnit){
            case DISTANCE_MM:
                radioResultButtonMm.setChecked(true);
                break;
            case DISTANCE_CM:
                radioResultButtonCm.setChecked(true);
                break;
            case DISTANCE_IN:
                radioResultButtonIn.setChecked(true);
                break;
            default:
                radioResultButtonMm.setChecked(true);
        }
        // scale type
        int inputScaleType = spinnerInputScaleType.getSelectedItemPosition();
        switch(inputScaleType){
            case SCALE_FRACTIONAL:
                spinnerResultScaleType.setSelection(0);
                break;
            case SCALE_INTOMILE:
                spinnerResultScaleType.setSelection(1);
                break;
            case SCALE_MILETOIN:
                spinnerResultScaleType.setSelection(2);
                break;
            case SCALE_CMTOKM:
                spinnerResultScaleType.setSelection(3);
                break;
            case SCALE_KMTOCM:
                spinnerResultScaleType.setSelection(4);
                break;
            default: // fractional
                spinnerResultScaleType.setSelection(0);
        }
        // ground units (the result units)
        // the rule: if input in imperial unit result in imperial as well
        switch(inputMapUnit){
            case DISTANCE_MM:
                radioResultButtonKm.setChecked(true);
                break;
            case DISTANCE_CM:
                radioResultButtonKm.setChecked(true);
                break;
            case DISTANCE_IN:
                radioResultButtonMile.setChecked(true);
                break;
            default:
                radioResultButtonKm.setChecked(true);
        }
    }
    // sets and displays result
    private void setResultDistanceMap(MapEntryAdvanced entry, RadioGroup group){
        int radioId = group.getCheckedRadioButtonId();
        switch(radioId){
            case R.id.radio_mm:
                textResultMap.setText(String.format(locale, "%.1f", entry.getMapDistanceMM()));
                break;
            case R.id.radio_cm:
                textResultMap.setText(String.format(locale, "%.2f", entry.getMapDistanceCM()));
                break;
            case R.id.radio_in:
                textResultMap.setText(String.format(locale, "%.2f", entry.getMapDistanceIN()));
                break;
            default:
                textResultMap.setText("--");
                Toast.makeText(this, ERROR_UNIT_CONVERSION, Toast.LENGTH_SHORT);
        }
    }

    // sets and displays result
    private void setResultDistanceGround(MapEntryAdvanced entry, RadioGroup group){
        int radioId = group.getCheckedRadioButtonId();
        switch(radioId){
            // not implemented yet
//            case R.id.radio_ft:
//                textResultGround.setText(String.format(locale, "%.2f", entry.getGroundDistanceFT()));
//                break;
//            case R.id.radio_m:
//                textResultGround.setText(String.format(locale, "%.2f", entry.getGroundDistanceMetre()));
//                break;
            case R.id.radio_km:
                textResultGround.setText(String.format(locale, "%.2f", entry.getGroundDistanceKM()));
                break;
            case R.id.radio_mile:
                textResultGround.setText(String.format(locale, "%.2f", entry.getGroundDistanceMile()));
                break;
            case R.id.radio_nautical:
                textResultGround.setText(String.format(locale, "%.2f", entry.getGroundDistanceNautical()));
                break;
            default:
                textResultGround.setText("--");
                Toast.makeText(this, ERROR_UNIT_CONVERSION, Toast.LENGTH_SHORT);
        }
    }

    private void setResultMapScale(MapEntryAdvanced entry, Spinner spinnerInputScaleType) {
        int spinnerPosition = spinnerInputScaleType.getSelectedItemPosition();
        switch(spinnerPosition) {
            case SCALE_FRACTIONAL:
                textResultScale.setText(entry.getMapScaleFractionalFormattedString());
                break;
            case SCALE_INTOMILE:
                textResultScale.setText(String.format(locale, "%.1f", entry.getMapScaleIntomile()));
                break;
            case SCALE_MILETOIN:
                textResultScale.setText(String.format(locale, "%.2f", entry.getMapScaleMiletoin()));
                break;
            case SCALE_CMTOKM:
                textResultScale.setText(String.format(locale, "%.1f", entry.getMapScaleCmtokm()));
                break;
            case SCALE_KMTOCM:
                textResultScale.setText(String.format(locale, "%.2f", entry.getMapScaleKmtocm()));
                break;
            default:
                textResultGround.setText("--");
                Toast.makeText(this, ERROR_UNIT_CONVERSION, Toast.LENGTH_SHORT);
        }

    }

    private double convertMapDistanceToMM(String inputMap, int mapUnit){
        double inputMM = 0D;
        double inputMapDouble = convertStringToDoubleInput(inputMap);
        switch(mapUnit){
            case DISTANCE_MM:
                inputMM = inputMapDouble;
                break;
            case DISTANCE_CM:
                inputMM = inputMapDouble * 10D;
                break;
            case DISTANCE_IN:
                inputMM = UnitConverter.inToMM(inputMapDouble);
                break;
        }
        return inputMM;
    }

    private int convertMapScaleToFractional(String inputScale, int scaleUnit) throws InsufficientParameterException {
        int scaleFractional = 0;
        Scale scale;
        switch(scaleUnit){
            case SCALE_FRACTIONAL:
                scaleFractional = convertStringToIntInput(inputScale);
                break;
            case SCALE_INTOMILE:
                scale = new Scale.ScaleBuilder()
                        .setInToMileAndConvert(convertStringToDoubleInput(inputScale))
                        .build();
                scaleFractional = scale.getFractional();
                break;
            case SCALE_MILETOIN:
                scale = new Scale.ScaleBuilder()
                        .setMileToInAndConvert(convertStringToDoubleInput(inputScale))
                        .build();
                scaleFractional = scale.getFractional();
                break;
            case SCALE_CMTOKM:
                scale = new Scale.ScaleBuilder()
                        .setCmToKmAndConvert(convertStringToDoubleInput(inputScale))
                        .build();
                scaleFractional = scale.getFractional();
                break;
            case SCALE_KMTOCM:
                scale = new Scale.ScaleBuilder()
                        .setKmToCmAndConvert(convertStringToDoubleInput(inputScale))
                        .build();
                scaleFractional = scale.getFractional();
                break;
        }
        return scaleFractional;
    }

    // checks current map units
    public int getCurrentDistanceMapUnit(RadioGroup group) {
        int id = group.getCheckedRadioButtonId();
        int unit;
        switch(id) {
            case R.id.radio_mm:
                unit = DISTANCE_MM;
                break;
            case R.id.radio_cm:
                unit = DISTANCE_CM;
                break;
            case R.id.radio_in:
                unit = DISTANCE_IN;
                break;
            default:
                unit = DISTANCE_MM;
        }
        return unit;
    }
    // checks current ground units
    public int getCurrentDistanceGroundUnit(RadioGroup group) {
        int id = group.getCheckedRadioButtonId();
        int unit;
        switch(id) {
            case R.id.radio_km:
                unit = DISTANCE_KM;
                break;
            case R.id.radio_mile:
                unit = DISTANCE_MILE;
                break;
            case R.id.radio_nautical:
                unit = DISTANCE_NAUTICAL;
                break;
            default:
                unit = DISTANCE_KM;
        }
        return unit;
    }

    // converts the map distance between mm, cm and inch
    // and sets the current map distance units
    public double convertAndSetResultMapDistance(MapEntryAdvanced entry, RadioGroup radioGroup){
        int checkedId = radioGroup.getCheckedRadioButtonId();
        double converted = 0D;
        switch(checkedId) {
            case R.id.radio_mm:
                converted = entry.getMapDistanceMM();
                textResultMap.setText(String.format(locale, "%.1f", converted));
                break;
            case R.id.radio_cm:
                converted = entry.getMapDistanceCM();
                textResultMap.setText(String.format(locale, "%.2f", converted));
                break;
            case R.id.radio_in:
                converted = entry.getMapDistanceIN();
                textResultMap.setText(String.format(locale, "%.2f", converted));
                break;
        }
        return converted;  //not used for now
    }

    // converts calculated ground distance between km, miles and nautical miles
    // and sets current ground distance units
    public double convertAndSetResultGroundDistance(MapEntryAdvanced entry, RadioGroup radioGroup){
        int checkedId = radioGroup.getCheckedRadioButtonId();
        double converted = 0D;
        switch(checkedId) {
            // not implemented yet
//            case R.id.radio_ft:
//                converted = entry.getGroundDistanceFT();
//                currentDistnanceGroundUnit = DISTANCE_FT;
//                break;
//            case R.id.radio_metre:
//                converted = entry.getGroundDistanceMetre();
//                currentDistnanceGroundUnit = DISTANCE_METRE;
//                break;
            case R.id.radio_km:
                converted = entry.getGroundDistanceKM();
                break;
            case R.id.radio_mile:
                converted = entry.getGroundDistanceMile();
                break;
            case R.id.radio_nautical:
                converted = entry.getGroundDistanceNautical();
                break;
        }
        textResultGround.setText(String.format(locale, "%.2f", converted));
        return converted;  //not used for now
    }

    public void convertAndSetResultMapScale(MapEntryAdvanced entry, int selectedPositionId){
        if(entry != null){
            switch(selectedPositionId){
                case SCALE_FRACTIONAL:
                    textResultScale.setText(entry.getMapScaleFractionalFormattedString());
                    break;
                case SCALE_INTOMILE:
                    textResultScale.setText(String.format(locale, "%.1f", entry.getMapScaleIntomile()));
                    break;
                case SCALE_MILETOIN:
                    textResultScale.setText(String.format(locale, "%.2f", entry.getMapScaleMiletoin()));
                    break;
                case SCALE_CMTOKM:
                    textResultScale.setText(String.format(locale, "%.1f", entry.getMapScaleCmtokm()));
                    break;
                case SCALE_KMTOCM:
                    textResultScale.setText(String.format(locale, "%.2f", entry.getMapScaleKmtocm()));
                    break;
                default:
                    textResultGround.setText("--");
                    Toast.makeText(this, ERROR_UNIT_CONVERSION, Toast.LENGTH_SHORT);
            }
        }
    }


    private void clearTextFields(View view){
        textResultMap.setText("");
        textResultGround.setText("");
        textResultScale.setText("");
        mapEntryAdvanced = null;
        inputCleared = true;

    }

    private int convertStringToIntInput(String input){
        int inputInt = 0;
        long inputLong = 0L;
        if(input != null){
            try{
                inputLong = Long.parseLong(input);
            }catch(NumberFormatException e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), ERROR_INPUT_INT, Toast.LENGTH_SHORT).show();
            }
            if(inputLong < 1)
                Toast.makeText(this, ERROR_INPUT_1, Toast.LENGTH_SHORT).show();
            if(inputLong >= 999999999L)
                Toast.makeText(this, ERROR_INPUT_TOO_BIG, Toast.LENGTH_SHORT).show();
            else
                inputInt = (int) inputLong;
        }else{
            Toast.makeText(this, ERROR_INPUT_EMPTY, Toast.LENGTH_SHORT).show();
        }
        System.out.println("denominator: " + inputInt);
        return inputInt;
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

    private void showKeyboard2(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}
