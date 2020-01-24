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

import java.text.DecimalFormat;
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
    private long storedScaleFractional;


    // holds all the calculations for single entry
    MapEntryAdvanced mapEntryAdvanced;

    private String ERROR_INPUT_INT;
    private String ERROR_INPUT_EMPTY;
    private String ERROR_INPUT_EMPTY_ANY;
    private String ERROR_INPUT_0;
    private String ERROR_INPUT_0_DISTANCE;
    private String ERROR_INPUT_0_SCALE;
    private String ERROR_INPUT_1_SCALE;
    private String ERROR_INPUT_1;
    private String ERROR_INPUT_TOO_BIG;
    private String ERROR_UNIT_CONVERSION;
    private String ERROR_CONVERTING_SCALE;

    private String WARNING_LESS_THAN_0_001;
    private String WARNING_LESS_THAN_0_01;
    private String WARNING_LESS_THAN_0_1;
    private String WARNING_LESS_THAN_1;
    private String WARNING_MORE_THAN_1_000_000;
    private String WARNING_MORE_THAN_63_360;
    private String WARNING_MORE_THAN_63_360_IN;
    private String WARNING_MORE_THAN_999_999;
    private String WARNING_MORE_THAN_999_999_CM;
    private String WARNING_MORE_THAN_1_000_000_000;

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
        ERROR_INPUT_0_DISTANCE = getString(R.string.error_input_zero_distance);
        ERROR_INPUT_0_SCALE = getString(R.string.error_input_zero_scale);
        ERROR_INPUT_1_SCALE = getString(R.string.error_input_one_scale);
        ERROR_INPUT_1 = getString(R.string.error_input_one);
        ERROR_INPUT_TOO_BIG = getString(R.string.error_input_too_big);
        ERROR_CONVERTING_SCALE = getString(R.string.error_converting_scale);
        ERROR_UNIT_CONVERSION = getString(R.string.error_unit_conversion);

        WARNING_LESS_THAN_0_001 = getString(R.string.less_than_0_001);
        WARNING_LESS_THAN_0_01 = getString(R.string.less_than_0_01);
        WARNING_LESS_THAN_0_1 = getString(R.string.less_than_0_1);
        WARNING_LESS_THAN_1 = getString(R.string.less_than_1);
        WARNING_MORE_THAN_1_000_000 = getString(R.string.more_than_1_000_000);
        WARNING_MORE_THAN_63_360 = getString(R.string.more_than_63_360);
        WARNING_MORE_THAN_63_360_IN = getString(R.string.more_than_63_360_in);
        WARNING_MORE_THAN_999_999 = getString(R.string.more_than_999_999);
        WARNING_MORE_THAN_999_999 = getString(R.string.more_than_999_999_cm);
        WARNING_MORE_THAN_1_000_000_000 = getString(R.string.more_than_1_000_000_000);

        textInputDistanceMap = (EditText) findViewById(R.id.distance_map);
        textInputDistanceScale = (EditText) findViewById(R.id.distance_scale);
        textResultMapDescription =  (TextView) findViewById(R.id.distance_result_map_description);
        textResultMap =  (TextView) findViewById(R.id.distance_result_map);
        textResultGroundDescription =  (TextView) findViewById(R.id.distance_result_ground_description);
        textResultGround =  (TextView) findViewById(R.id.distance_result_ground);
        textResultScaleDescription = (TextView) findViewById(R.id.distance_result_scale_description);
        textResultScale = (TextView) findViewById(R.id.distance_result_scale);

        spinnerInputDistanceUnit = (Spinner) findViewById(R.id.distance_map_unit);
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


        // deals with screen orientation changes
        restoreCalculations(savedInstanceState);

        // calculates the ground distance when the 'done'/'ok' button on the soft keyboard pressed
        textInputDistanceMap.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    processInputAndCalculateDistanceGround(view);
                    return true;
                }
                return false;
            }
        });

        // calcutates ground distance
        Button btnCalculateDistanceGround = (Button) findViewById(R.id.btn_calculate);
        btnCalculateDistanceGround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processInputAndCalculateDistanceGround(view);
            }
        });

        // 1st click clears map disctance value
        // 2nd click clears map scale value
        Button btnClear = (Button) findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View view) {
                if(!inputCleared){
                    clearTextFields();
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
                if(mapEntryAdvanced !=  null)
                    setResultDistanceGround(mapEntryAdvanced, group);
            }
        });

        // converts result map unit between each other
        radioResultGroupDistanceMapUnit.setOnCheckedChangeListener(new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(mapEntryAdvanced !=  null)
                    setResultDistanceMap(mapEntryAdvanced, group);
            }
        });

        // converts result scale between each scale type
        spinnerResultScaleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedPositionId = spinnerResultScaleType.getSelectedItemPosition();
                setResultMapScale(mapEntryAdvanced, spinnerResultScaleType);
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
            outState.putLong("storedScaleFractional", storedScaleFractional);
        }
    }

    //----------------------------  end of onCreate() & other inherited methods  ---------------------------------------//



    private void restoreCalculations(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            computed = savedInstanceState.getBoolean("computed");
            inputCleared = savedInstanceState.getBoolean("inputCleared");
            if(computed && !inputCleared){
                storedDistanceMapMM = savedInstanceState.getDouble("storedDistanceMapMM");
                storedScaleFractional = savedInstanceState.getLong("storedScaleFractional");

                calculateAndDisplay();
            }
        }
    }

    private void processInputAndCalculateDistanceGround(View view) {
        String stringInputDistanceMap = textInputDistanceMap.getText().toString();
        String stringInputDistanceScale = textInputDistanceScale.getText().toString();

        System.out.println("stringInputDistanceScale: " + stringInputDistanceScale);

        if(!"".equals(stringInputDistanceScale) && !"".equals(stringInputDistanceMap)){
            int positionInputDistanceMapUnit = spinnerInputDistanceUnit.getSelectedItemPosition();
            int positionInputScaleType = spinnerInputScaleType.getSelectedItemPosition();

            calculateDistanceGroundAndDisplay(
                    stringInputDistanceMap, stringInputDistanceScale,
                    positionInputDistanceMapUnit, positionInputScaleType);
            hideKeyboard(view);
            computed = true;
            inputCleared = false;
        }else{
            Toast.makeText(this, ERROR_INPUT_EMPTY_ANY,Toast.LENGTH_SHORT).show();
        }
    }

    /*
     *  Calculates ground distance and sets values to the fields,
     *  MapEntry accepts mapDistance in mm, groundDistance in km, scale as fractional (denominator)
     */
    private void calculateDistanceGroundAndDisplay(
            String inputMap, String inputScale, int mapUnit, int scaleUnit){

        // validates and converts input to base units and stores them in class fields,
        // (to put them into Bundle, used when screen orientation changes)
        boolean distanceValidatedForZero = validateInputForZero(inputMap);
        boolean scaleValidatedForZero = validateInputForZero(inputScale);
        boolean scaleFractionalValidatedForOne = validateInputForOne(inputScale);

        if(!distanceValidatedForZero) {
            Toast.makeText(this, ERROR_INPUT_0_DISTANCE, Toast.LENGTH_SHORT).show();
            textInputDistanceMap.requestFocus();
            textInputDistanceMap.selectAll();
            clearTextFields();
        }else if (!scaleValidatedForZero) {
            Toast.makeText(this, ERROR_INPUT_0_SCALE, Toast.LENGTH_SHORT).show();
            textInputDistanceScale.requestFocus();
            textInputDistanceScale.selectAll();
            clearTextFields();
        }else if(scaleUnit == 0 && !scaleFractionalValidatedForOne){
            Toast.makeText(this, ERROR_INPUT_1_SCALE, Toast.LENGTH_SHORT).show();
            textInputDistanceScale.requestFocus();
            textInputDistanceScale.selectAll();
            clearTextFields();
        }else{
            convertInputToBaseUnits(inputMap, inputScale, mapUnit, scaleUnit);
            calculateAndDisplay();
        }
    }

    private void calculateAndDisplay() {
        // calculates and converts internally all map, ground and scale units
        mapEntryAdvanced = new MapEntryAdvanced(storedDistanceMapMM, storedScaleFractional);

        // reads input units and sets result units according to the defined logic
        readInputAndSetResultUnits(spinnerInputDistanceUnit, spinnerInputScaleType);

        // sets and displays the result in units taken from the result radios/spinner
        setResultDistanceMap(mapEntryAdvanced,radioResultGroupDistanceMapUnit);
        setResultDistanceGround(mapEntryAdvanced, radioResultGroupDistanceGroundUnit);
        setResultMapScale(mapEntryAdvanced, spinnerResultScaleType);
    }

    private boolean validateInputForZero(String input){
        double distance = Double.parseDouble(input);
        if(distance > 0)
            return true;
        return false;
    }

    private boolean validateInputForOne(String input){
        int scale = (int) Double.parseDouble(input);
        if(scale >=1)
            return true;
        return false;
    }

    /*
     * Converts input values to base units - distance to mm / scale to fractional
     */
    private void convertInputToBaseUnits(String inputMap, String inputScale, int mapUnit, int scaleUnit){
        try {
            storedDistanceMapMM = convertMapDistanceToMM(inputMap, mapUnit);
            storedScaleFractional = convertMapScaleToFractional(inputScale, scaleUnit);
        } catch (InsufficientParameterException e) {
            e.printStackTrace();
        }
    }

    /*
     * Reads input units and sets result units accordingly to the defined logic
     */
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

    /*
     * sets and displays result values
     */
    private void setResultDistanceMap(MapEntryAdvanced entry, RadioGroup group){
        int radioId = group.getCheckedRadioButtonId();
        switch(radioId){
            case R.id.radio_mm:
                if(entry.getMapDistanceMM() < 0.1){
                    textResultMap.setText(WARNING_LESS_THAN_0_1);
                    Toast.makeText(this,
                            "Seriously? \nLess than 0.1 MILLIMETRE on the map?",
                            Toast.LENGTH_LONG).show();
                } else if(entry.getMapDistanceMM() > 1_000_000){
                    textResultMap.setText(WARNING_MORE_THAN_1_000_000);
                    Toast.makeText(this,
                            "Seriously? \nMore than a KILOMETRE on the map??" +
                                    "\nYour Nobel Price is on the way then",
                            Toast.LENGTH_LONG).show();
                } else {
                    textResultMap.setText(new DecimalFormat("#,###.0").format(entry.getMapDistanceMM()));
                 //   textResultMap.setText(String.format(locale, "%.1f", entry.getMapDistanceMM()));
                }
                break;
            case R.id.radio_cm:
                if(entry.getMapDistanceMM() < 0.1){
                    textResultMap.setText(WARNING_LESS_THAN_0_1);
                    Toast.makeText(this,
                            "Seriously? \nLess than 0.1 MILLIMETRE on the map?",
                            Toast.LENGTH_LONG).show();
                } else if(entry.getMapDistanceMM() > 1_000_000){
                    textResultMap.setText(WARNING_MORE_THAN_1_000_000);
                    Toast.makeText(this,
                            "Seriously? \nMore than a KILOMETRE on the map??" +
                                    "\nYour Nobel Price is on the way then",
                            Toast.LENGTH_LONG).show();
                }else {
                    textResultMap.setText(new DecimalFormat("#,###.00").format(entry.getMapDistanceCM()));
//                textResultMap.setText(String.format(locale, "%.2f", entry.getMapDistanceCM()));
                }
                break;
            case R.id.radio_in:
                if(entry.getMapDistanceIN() < 0.01){
                    textResultMap.setText(WARNING_LESS_THAN_0_1);
                    Toast.makeText(this,
                            "Seriously? \nLess than 0.01 INCH on the map?",
                            Toast.LENGTH_LONG).show();
                } else if(entry.getMapDistanceIN() > 63_360){
                    textResultMap.setText(WARNING_MORE_THAN_63_360);
                    Toast.makeText(this,
                            "Seriously? \nMore than a MILE on the map??" +
                                    "\nYour Nobel Price is on the way then",
                            Toast.LENGTH_LONG).show();
                } else {
                    textResultMap.setText(new DecimalFormat("#,###.00").format(entry.getMapDistanceIN()));
                  //  textResultMap.setText(String.format(locale, "%.2f", entry.getMapDistanceIN()));
                }
                break;
            default:
                textResultMap.setText("--");
                Toast.makeText(this, ERROR_UNIT_CONVERSION, Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * sets and displays calculated ground distance result values
     */
    private void setResultDistanceGround(MapEntryAdvanced entry, RadioGroup group){
        int radioId = group.getCheckedRadioButtonId();
        switch(radioId) {
            // not implemented yet
//            case R.id.radio_ft:
//                textResultGround.setText(String.format(locale, "%.2f", entry.getGroundDistanceFT()));
//                break;
//            case R.id.radio_m:
//                textResultGround.setText(String.format(locale, "%.2f", entry.getGroundDistanceMetre()));
//                break;
            case R.id.radio_km:
                if (entry.getGroundDistanceKM() < 0.001) {
                    textResultGround.setText(WARNING_LESS_THAN_0_001);
                } else if (entry.getGroundDistanceKM() > 999_999) {
                    textResultGround.setText(WARNING_MORE_THAN_999_999);
                } else {
                    textResultGround.setText(new DecimalFormat("#,###.000").format(entry.getGroundDistanceKM()));
                  //  textResultGround.setText(String.format(locale, "%.3f", entry.getGroundDistanceKM()));
                }
                break;
            case R.id.radio_mile:
                if (entry.getGroundDistanceMile() < 0.001){
                    textResultGround.setText(WARNING_LESS_THAN_0_001);
                }else if(entry.getGroundDistanceMile() > 999_999){
                    textResultGround.setText(WARNING_MORE_THAN_999_999);
                }else {
                    textResultGround.setText(new DecimalFormat("#,###.000").format(entry.getGroundDistanceMile()));
                  //  textResultGround.setText(String.format(locale, "%.3f", entry.getGroundDistanceMile()));
                }
                break;
            case R.id.radio_nautical:
                if(entry.getGroundDistanceNautical() < 0.001) {
                    textResultGround.setText(WARNING_LESS_THAN_0_001);
                }else if(entry.getGroundDistanceNautical() > 999_999){
                    textResultGround.setText(WARNING_MORE_THAN_999_999);
                }else {
                    textResultGround.setText(new DecimalFormat("#,###.000").format(entry.getGroundDistanceNautical()));
                  //  textResultGround.setText(String.format(locale, "%.3f", entry.getGroundDistanceNautical()));
                }
                break;
            default:
                textResultGround.setText("--");
                Toast.makeText(this, ERROR_UNIT_CONVERSION, Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * sets and displays result values
     */
    private void setResultMapScale(MapEntryAdvanced entry, Spinner spinner) {
        int spinnerPosition = spinner.getSelectedItemPosition();
        if(entry != null){
            switch(spinnerPosition) {
                case SCALE_FRACTIONAL:
                    if(entry.getMapScaleFractional() < 1){
                        textResultScale.setText(WARNING_LESS_THAN_1);
                    }else if(entry.getMapScaleFractional() > 1_000_000_000){
                        textResultScale.setText(WARNING_MORE_THAN_1_000_000_000);
                    }else
                        textResultScale.setText(entry.getMapScaleFractionalFormattedString());
                    break;
                case SCALE_INTOMILE:
                    if(entry.getMapScaleIntomile() < 0.1){
                        textResultScale.setText(WARNING_LESS_THAN_0_1);
                    } else if(entry.getMapScaleIntomile() > 63_360){
                        textResultScale.setText(WARNING_MORE_THAN_63_360_IN);
                    } else {
                        textResultScale.setText(new DecimalFormat("#,###.0").format(entry.getMapScaleIntomile()));
//                        textResultScale.setText(String.format(locale, "%.1f", entry.getMapScaleIntomile()));
                    }
                    break;
                case SCALE_MILETOIN:
                    if(entry.getMapScaleMiletoin() < 0.001){
                        textResultScale.setText(WARNING_LESS_THAN_0_001);
                    } else if(entry.getMapScaleMiletoin() > 63_360){
                        textResultScale.setText(WARNING_MORE_THAN_63_360_IN);
                    } else {
                        textResultScale.setText(new DecimalFormat("#,###.000").format(entry.getMapScaleMiletoin()));
//                        textResultScale.setText(String.format(locale, "%.2f", entry.getMapScaleMiletoin()));
                    }
                    break;
                case SCALE_CMTOKM:
                    if(entry.getMapScaleCmtokm() < 0.01){
                        textResultScale.setText(WARNING_LESS_THAN_0_01);
                    } else if(entry.getMapScaleCmtokm() > 999_999){
                        textResultScale.setText(WARNING_MORE_THAN_999_999_CM);
                    } else {
                        textResultScale.setText(new DecimalFormat("#,###.00").format(entry.getMapScaleCmtokm()));
//                        textResultScale.setText(String.format(locale, "%.1f", entry.getMapScaleCmtokm()));
                    }
                    break;
                case SCALE_KMTOCM:
                    if(entry.getMapScaleKmtocm() < 0.001){
                        textResultScale.setText(WARNING_LESS_THAN_0_001);
                    } else if(entry.getMapScaleKmtocm() > 999_999){
                        textResultScale.setText(WARNING_MORE_THAN_999_999_CM);
                    } else {
                        textResultScale.setText(new DecimalFormat("#,###.000").format(entry.getMapScaleKmtocm()));
//                        textResultScale.setText(String.format(locale, "%.2f", entry.getMapScaleKmtocm()));
                    }
                    break;
                default:
                    textResultGround.setText("--");
                    Toast.makeText(this, ERROR_UNIT_CONVERSION, Toast.LENGTH_SHORT).show();
            }
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

    private long convertMapScaleToFractional(String inputScale, int scaleUnit) throws InsufficientParameterException {
        long scaleFractional = 0;
        Scale scale;
        switch(scaleUnit){
            case SCALE_FRACTIONAL:
                scaleFractional = convertStringToLongInput(inputScale);
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

    private long convertStringToLongInput(String input){
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
            else if(inputLong >= 999999999L)
                Toast.makeText(this, ERROR_INPUT_TOO_BIG, Toast.LENGTH_SHORT).show();
            else
                inputLong = inputLong;
        }else{
            Toast.makeText(this, ERROR_INPUT_EMPTY, Toast.LENGTH_SHORT).show();
        }
        System.out.println("denominator: " + inputLong);
        return inputLong;
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
        textResultMap.setText("");
        textResultGround.setText("");
        textResultScale.setText("");
        mapEntryAdvanced = null;
        inputCleared = true;
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
