package com.latidude99.maptools.activity.distance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import android.content.Context;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.latidude99.maptools.R;
import com.latidude99.maptools.Utils.UnitConverter;
import com.latidude99.maptools.model.distance.MapEntry;
import com.latidude99.maptools.model.distance.MapEntryAdvanced;
import com.latidude99.maptools.model.scale.InsufficientParameterException;
import com.latidude99.maptools.model.scale.Scale;

import java.text.NumberFormat;
import java.util.Locale;

public class DistanceMapScaleActivity extends AppCompatActivity {
    private Locale locale = Locale.getDefault();
    private static final String KM = "km";
    private static final String MILE = "mile";
    private final String SCALE_FRACTIONAL = "fractional";
    private final String SCALE_INTOMILE = "in to the mile";
    private final String SCALE_MILETOIN = "mile to an in    ";
    private final String SCALE_CMTOKM = "cm to a km";
    private final String SCALE_KMTOCM = "km to a cm";
    private final String DISTANCE_MM = "millimetre(s)";
    private final String DISTANCE_CM = "centimetre(s)";
    private final String DISTANCE_IN = "inch(es)";
    private final String DISTANCE_FT = "ft";
    private final String DISTANCE_METRE = "metre(s)";
    private final String DISTANCE_KM = "km";
    private final String DISTANCE_MILE = "mile(s)";
    private final String DISTANCE_NAUTICAL = "nautical mile(s)";

    private boolean clearInput;
    private boolean distanceGroundCalculated;
//    String distanceResultGroundUnit; // not used anymore

    /*
    {
        SCALE_FRACTIONAL = getString(R.string.spinner_scale_fractional);
        SCALE_INTOMILE = getString(R.string.spinner_scale_intothemile);
        SCALE_MILETOIN = getString(R.string.spinner_scale_miletoanin);
        SCALE_CMTOKM = getString(R.string.spinner_scale_cmtoakm);
        SCALE_KMTOCM = getString(R.string.spinner_scale_kmtoacm);
        DISTANCE_MM = getString(R.string.spinner_distance_mm);
        DISTANCE_CM = getString(R.string.spinner_distance_cm);
        DISTANCE_IN = getString(R.string.spinner_distance_in);
    }
*/
    private String currentDistnanceGroundUnit;
   // private String distanceMapUnit; // not used anymore
   // private String distanceScale; // not used anymore
    MapEntryAdvanced mapEntryAdvanced;

    private String ERROR_INPUT_INT;
    private String ERROR_INPUT_EMPTY;
    private String ERROR_INPUT_0;
    private String ERROR_INPUT_1;
    private String ERROR_INPUT_TOO_BIG;
    private String ERROR_UNIT_CONVERSION;
    private String ERROR_CONVERTING_SCALE;
    private String stringInputDistanceMap;
    private String stringInputDistanceScale;
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
        ERROR_INPUT_0 = getString(R.string.error_input_zero);
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

        // default result map unit
     //   radioResultButtonMm.setChecked(true);

        // default ground units
     //   radioResultButtonKm.setChecked(true);

        spinnerResultScaleType = (Spinner) findViewById(R.id.distance_result_scale_type);

        textInputDistanceMap.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && distanceGroundCalculated == false) {
                    showKeyboard(v);
                }
            }
        });
/*
        // calculates the ground distance when the 'done'/'ok' button on the soft keyboard pressed
        // or moves focus to textInputDistanceScale if empty
        textInputDistanceMap.setOnEditorActionListener(new EditText.OnEditorActionListener() {
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

        // calculates the ground distance when the 'done'/'ok' button on the soft keyboard pressed
        // or moves focus to textInputDistanceMap if empty
        textInputDistanceScale.setOnEditorActionListener(new EditText.OnEditorActionListener() {
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
*/

        Button btnCalculateDistanceGround = (Button) findViewById(R.id.btn_calculate);
        btnCalculateDistanceGround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringInputDistanceMap = textInputDistanceMap.getText().toString().trim();
                stringInputDistanceScale = textInputDistanceScale.getText().toString().trim();

                String stringInputDistanceMapUnit = String.valueOf(spinnerInputDistanceUnit.getSelectedItem());
                String stringInputScaleType = String.valueOf(spinnerInputScaleType.getSelectedItem());

             //   currentDistnanceGroundUnit = getCurrentDistanceGroundUnit(radioResultGroupDistanceGroundUnit);
     //           spinnerResultScaleType.setSelection(spinnerInputDistanceUnit.getSelectedItemPosition());

                System.out.println("stringInputDistanceMapUnit: " + stringInputDistanceMapUnit);
                System.out.println("stringInputDistanceScaleType: " + stringInputScaleType);

                calculateDistanceGroundAndDisplay(view,
                                                   // currentDistnanceGroundUnit, // not used anymore
                                                    stringInputDistanceMap,
                                                    stringInputDistanceScale,
                                                    stringInputDistanceMapUnit,
                                                    stringInputScaleType);
                hideKeyboard(view);
            }
        });

        // 1st click clears calculated values
        // 2nd click focuses on textInputDistanceMap and opens the keyboard
        Button btnClear = (Button) findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View view) {
                if(clearInput)
                    textInputDistanceMap.setText("");
                else
                    clearTextFields(view);
            }
        });

        radioResultGroupDistanceGroundUnit.setOnCheckedChangeListener(new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                convertAndSetResultGroundDistance(mapEntryAdvanced, group);
            }
        });

        radioResultGroupDistanceMapUnit.setOnCheckedChangeListener(new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                convertAndSetResultMapDistance(mapEntryAdvanced, group);
            }
        });

        spinnerResultScaleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                int selectedPositionId = spinnerResultScaleType.getSelectedItemPosition();
                convertAndSetResultMapScale(mapEntryAdvanced, selectedPositionId);

                Log.e("Selected item : ", "" + selectedPositionId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

    }

    //----------------------------  end of onCreate()  ---------------------------------------//



    // calculates ground distance and sets values to the fields
    // MapEntry stores mapDistance in mm, groundDistance in km, scale as fractional (denominator)
    private void calculateDistanceGroundAndDisplay(View view,
                                               //    String groundUnit, // not used anymore
                                                   String inputMap,
                                                   String inputScale,
                                                   String mapUnit,
                                                   String scaleUnit){
        //currentDistnanceGroundUnit = groundUnit;
        try {
            double mapDistanceMM = convertMapDistanceToMM(inputMap, mapUnit);
  //          System.out.println("inputScale: "+ inputScale + ",   scaleUnit: " + scaleUnit);
            int mapScaleFractional = convertMapScaleToFractional(inputScale, scaleUnit);

            System.out.println("mapScaleFractional: " + mapScaleFractional);
            System.out.println("mapDistanceMM: " + mapDistanceMM);
 //           System.out.println("mapUnit: " + mapUnit);

            // calculates and converts internally all map, ground and scale units
            mapEntryAdvanced = new MapEntryAdvanced(mapDistanceMM, mapScaleFractional);

            System.out.println("mapEntry: " + mapEntryAdvanced);

            // reads input units and sets result units
            readInputAndSetResultUnits(spinnerInputDistanceUnit, spinnerInputScaleType);

            // sets and displays the result
            setResultDistanceMap(mapEntryAdvanced,radioResultGroupDistanceMapUnit);
            setResultDistanceGround(mapEntryAdvanced, radioResultGroupDistanceGroundUnit);
            setResultMapScale(mapEntryAdvanced, spinnerResultScaleType);

        } catch (InsufficientParameterException e) {
            e.printStackTrace();
        }
    }


    // reads input units and sets result units accordingly
    private void readInputAndSetResultUnits(Spinner spinnerInputMapUnit, Spinner spinnerInputScaleType){
        // map unit
        int inputMapUnit = spinnerInputMapUnit.getSelectedItemPosition();
        switch(inputMapUnit){
            case 0: // mm
                radioResultButtonMm.setChecked(true);
                break;
            case 1: // cm
                radioResultButtonCm.setChecked(true);
                break;
            case 2: // inch
                radioResultButtonIn.setChecked(true);
                break;
            default:
                radioResultButtonMm.setChecked(true);
        }
        // scale type
        int inputScaleType = spinnerInputScaleType.getSelectedItemPosition();
        switch(inputScaleType){
            case 0: // fractional
                spinnerResultScaleType.setSelection(0);
                break;
            case 1: // in to mile
                spinnerResultScaleType.setSelection(1);
                break;
            case 2: // mile to inch
                spinnerResultScaleType.setSelection(2);
                break;
            case 3: // cm to km
                spinnerResultScaleType.setSelection(3);
                break;
            case 4: // km to cm
                spinnerResultScaleType.setSelection(4);
                break;
            default: // fractional
                spinnerResultScaleType.setSelection(0);
        }
        // ground units (the result units)
        // the rule: if input in imperial unit result in imperial as well
        switch(inputMapUnit){
            case 0: // mm
                radioResultButtonKm.setChecked(true);
                break;
            case 1: // cm
                radioResultButtonKm.setChecked(true);
                break;
            case 2: // inch
                radioResultButtonMile.setChecked(true);
                break;
            default:
                radioResultButtonKm.setChecked(true);
        }
    }
    // sets and displays result, no option to change units here yet
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
            case 0:
                textResultScale.setText(entry.getMapScaleFractionalFormattedString());
             //   spinnerResultScaleType.setSelection(0);
                break;
            case 1:
                textResultScale.setText(String.format(locale, "%.1f", entry.getMapScaleIntomile()));
            //    spinnerResultScaleType.setSelection(1);
                break;
            case 2:
                textResultScale.setText(String.format(locale, "%.2f", entry.getMapScaleMiletoin()));
            //    spinnerResultScaleType.setSelection(2);
                break;
            case 3:
                textResultScale.setText(String.format(locale, "%.1f", entry.getMapScaleCmtokm()));
            //    spinnerResultScaleType.setSelection(3);
                break;
            case 4:
                textResultScale.setText(String.format(locale, "%.2f", entry.getMapScaleKmtocm()));
            //    spinnerResultScaleType.setSelection(4);
                break;
            default:
                textResultGround.setText("--");
                Toast.makeText(this, ERROR_UNIT_CONVERSION, Toast.LENGTH_SHORT);
        }

    }

    private double convertMapDistanceToMM(String inputMap, String mapUnit){
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

    private int convertMapScaleToFractional(String inputScale, String scaleUnit) throws InsufficientParameterException {
        int scaleFractional = 0;
        Scale scale = null;
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
    public String getCurrentDistanceMapUnit(RadioGroup group) {
        int id = group.getCheckedRadioButtonId();
        String unit = "";
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
        }
        return unit;
    }
    // checks current ground units
    public String getCurrentDistanceGroundUnit(RadioGroup group) {
        int id = group.getCheckedRadioButtonId();
        String unit = "";
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
        }
        return unit;
    }

    //  -----change for calculations using MapEntry ------

    // converts the map distance between mm, cm and inch
    // and sets the current map distance units
    public double convertAndSetResultMapDistance(MapEntryAdvanced entry,
                                                    RadioGroup radioGroup
                                                    // int checkedId, // new distance ground units
                                                    // String previousResultGroundUnit
    ){
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
    public double convertAndSetResultGroundDistance(MapEntryAdvanced entry,
                                                    RadioGroup radioGroup
                                                    // int checkedId, // new distance ground units
                                                    // String previousResultGroundUnit
                                                     ){
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
                currentDistnanceGroundUnit = DISTANCE_KM;
                break;
            case R.id.radio_mile:
                converted = entry.getGroundDistanceMile();
                currentDistnanceGroundUnit = DISTANCE_MILE;
                break;
            case R.id.radio_nautical:
                converted = entry.getGroundDistanceNautical();
                currentDistnanceGroundUnit = DISTANCE_NAUTICAL;
                break;
        }
        textResultGround.setText(String.format(locale, "%.2f", converted));
        return converted;  //not used for now
    }

    public void convertAndSetResultMapScale(MapEntryAdvanced entry, int selectedPositionId){
        if(entry != null){
            switch(selectedPositionId){
                case 0:
                    textResultScale.setText(entry.getMapScaleFractionalFormattedString());
                    break;
                case 1:
                    textResultScale.setText(String.format(locale, "%.1f", entry.getMapScaleIntomile()));
                    break;
                case 2:
                    textResultScale.setText(String.format(locale, "%.2f", entry.getMapScaleMiletoin()));
                    break;
                case 3:
                    textResultScale.setText(String.format(locale, "%.1f", entry.getMapScaleCmtokm()));
                    break;
                case 4:
                    textResultScale.setText(String.format(locale, "%.2f", entry.getMapScaleKmtocm()));
                    break;
                default:
                    textResultGround.setText("--");
                    Toast.makeText(this, ERROR_UNIT_CONVERSION, Toast.LENGTH_SHORT);
            }
        }
    }


    private void clearTextFields(View view){
        // to to
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
        // Check if no view has focus:
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
        }
    }
}
