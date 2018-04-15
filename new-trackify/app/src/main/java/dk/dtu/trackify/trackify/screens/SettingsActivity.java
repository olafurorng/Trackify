package dk.dtu.trackify.trackify.screens;

import android.app.AlertDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import org.joda.time.Days;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dk.dtu.trackify.trackify.MainApplication;
import dk.dtu.trackify.trackify.R;
import dk.dtu.trackify.trackify.entities.AppUsages;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    private UsageStatsManager mUsageStatsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mUsageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE); //

        findViewById(R.id.end_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endExperiment();

                findViewById(R.id.end_button).setVisibility(View.INVISIBLE);
                findViewById(R.id.progress_bar_settings).setVisibility(View.VISIBLE);
            }
        });

        SharedPreferences prefs = getSharedPreferences("Trackify", MODE_PRIVATE);
        boolean userHasEndedTheExperiment = prefs.getBoolean("ended", false);
        if (userHasEndedTheExperiment) {
            TextView endExperimentDescription = findViewById(R.id.end_experiment_description);
            endExperimentDescription.setText("You have ended the experiment. No more data is sent to Trackify. Your data will be removed from Trackify within 3 weeks.");
            findViewById(R.id.end_button).setVisibility(View.INVISIBLE);
        }


    }

    public int daysBetween(Date d1, Date d2) {
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    private void endExperiment() {
        SharedPreferences prefs = getSharedPreferences("Trackify", MODE_PRIVATE);
        String userId = prefs.getString("userId", "");
        Date signupTime = new Date(prefs.getLong("signupTime", 0));

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        //cal.add(Calendar.DATE, 3); // TODO: REMOVE

        int days = daysBetween(signupTime, new Date(cal.getTimeInMillis()));
        //cal.add(Calendar.DATE, -3); // TODO: REMOVE

        // query the app usage data

        cal.add(Calendar.DATE, -days);
        List<UsageStats> queryUsageStats = mUsageStatsManager
                .queryUsageStats(UsageStatsManager.INTERVAL_DAILY, cal.getTimeInMillis(),
                        System.currentTimeMillis());

        List<UsageStats> socialAppsUsageData = new ArrayList<>();
        for (UsageStats stats : queryUsageStats) {
            if (MainApplication.SOCIAL_APPS_PACKAGE_NAMES.contains(stats.getPackageName())) {
                socialAppsUsageData.add(stats);
            }
        }

        List<AppUsages> appUsagesList = new ArrayList<>();
        for (UsageStats stats : socialAppsUsageData) {
            appUsagesList.add(new AppUsages(stats.getPackageName(), stats.getFirstTimeStamp(), stats.getLastTimeStamp(), stats.getLastTimeUsed(), stats.getTotalTimeInForeground()));

        }

        Log.d(TAG, "App usage data -->  " + appUsagesList);


        // Update Use with app usage data
        MainApplication mainApplication = (MainApplication) getApplication();
        final DocumentReference userRef = mainApplication.getDb().collection("users").document(userId);

// Set the "isCapital" field of the city 'DC'
        userRef
                .update("Usage After", appUsagesList.toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated! - usage after");

                        Toast.makeText(SettingsActivity.this, "Experiment has succuessfully be ended", Toast.LENGTH_LONG).show();

                        SharedPreferences.Editor editor = getSharedPreferences("Trackify", MODE_PRIVATE).edit();
                        editor.putBoolean("ended", true);
                        editor.apply();

                        Date now = new Date();
                        userRef
                                .update("End time", now.toString())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully updated! - end time");
                                        restartApp();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document - end time", e);
                                        restartApp();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document - usage after", e);

                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(SettingsActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                        builder.setTitle("Error")
                                .setMessage("Couldn't end experiment. Try again later or/and let the developers of this app know.")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
    }

    private void restartApp() {
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
