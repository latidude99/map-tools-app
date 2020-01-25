package com.latidude99.maptools.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.latidude99.maptools.R;
import com.latidude99.maptools.activity.distance.DistanceActivity;
import com.latidude99.maptools.activity.scale.ScaleActivity;
import com.latidude99.maptools.activity.unit.UnitActivity;

import static com.crashlytics.android.Crashlytics.log;

public class MapToolsActivity extends AppCompatActivity {
    private AppUpdateManager appUpdateManager;
    int REQUEST_APP_UPDATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_tools);
        final String pro = getString(R.string.pro);

        Button btnUnit = findViewById(R.id.btn_unit);
        final Intent unitActivityIntent = new Intent(getApplicationContext(), UnitActivity.class);
        btnUnit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(unitActivityIntent);
            }
        });

        Button btnScale = findViewById(R.id.btn_scale);
        final Intent scaleActivityIntent = new Intent(getApplicationContext(), ScaleActivity.class);
        btnScale.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(scaleActivityIntent);
            }
        });

        Button btnDistance = findViewById(R.id.btn_distance);
        final Intent distanceActivityIntent = new Intent(getApplicationContext(), DistanceActivity.class);
        btnDistance.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(distanceActivityIntent);
            }
        });

        Button btnCoords = findViewById(R.id.btn_coords);
        btnCoords.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), pro, Toast.LENGTH_SHORT).show();
            }
        });

        Button btnArea = findViewById(R.id.btn_area);
        btnArea.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), pro, Toast.LENGTH_SHORT).show();
            }
        });

        // onCreate(){
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(this);

        // Don't need to do this here anymore
        // Returns an intent object that you use to check for an update.
        //Task<AppUpdateInfo> appUpdateInfo = appUpdateManager.getAppUpdateInfo();

        REQUEST_APP_UPDATE = 152349126; //  ?

        updateCheckAndExecution_IMMEDIATE();


        /* Crashlytics forced crash

        Button crashButton = new Button(this);
        crashButton.setText("Crash!");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Crashlytics.getInstance().crash(); // Force a crash
            }
        });

        addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        */
    }

    private void updateCheckAndExecution_IMMEDIATE() {
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {

                            // Checks that the platform will allow the specified type of update.
                            if ((appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE)
                                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))
                            {
                                // Request the update.
                                try {
                                    appUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            AppUpdateType.IMMEDIATE,
                                            this,
                                            REQUEST_APP_UPDATE);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
    }

    //Checks that the update is not stalled during 'onResume()'.
    //However, you should execute this check at all entry points into the app.
    @Override
    protected void onResume() {
        super.onResume();

        REQUEST_APP_UPDATE = 152349126; //  ?

        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {

                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                try {
                                    appUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            AppUpdateType.IMMEDIATE,
                                            this,
                                            REQUEST_APP_UPDATE);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        REQUEST_APP_UPDATE = 152349126;
        if (requestCode == REQUEST_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                log("Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
                updateCheckAndExecution_IMMEDIATE();
            }
        }
    }

}
