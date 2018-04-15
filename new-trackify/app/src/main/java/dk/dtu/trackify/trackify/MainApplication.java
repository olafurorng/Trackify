package dk.dtu.trackify.trackify;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.dtu.trackify.trackify.entities.AppUsages;
import dk.dtu.trackify.trackify.screens.OverviewActivity;
import dk.dtu.trackify.trackify.screens.WelcomeActivity;

public class MainApplication extends Application {

    private static final String TAG = MainApplication.class.getSimpleName();

    private FirebaseFirestore db; // our database, Firestore from Google Firebase
    private UsageStatsManager mUsageStatsManager;

    public static final ArrayList<String> SOCIAL_APPS_PACKAGE_NAMES = new ArrayList<String>() {{
        add("facebook.com.katana");
        add("com.facebook.katana");
        add("com.facebook.orca");
        add("com.snapchat.android");
        add("com.instagram.android");
        add("kik.android");
        add("com.reddit.frontpage");
        add("com.google.android.youtube");
        add("com.whatsapp");
        add("com.twitter.android");
        add("com.tinder");
        add("com.android.chrome");
        add("com.tellm.android.app");
    }};

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(MainApplication.this);
        mUsageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
    }

    public FirebaseFirestore getDb() {
        db = FirebaseFirestore.getInstance();
        return db;
    }

    public void addUser(String name, final Activity activity) {
        db = FirebaseFirestore.getInstance();

        Date now = new Date();

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("Full Name", name);
        user.put("Start time", now.toString());
        user.put("FCM token", FirebaseInstanceId.getInstance().getToken());


        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                        SharedPreferences.Editor editor = getSharedPreferences("Trackify", MODE_PRIVATE).edit();
                        editor.putString("userId", documentReference.getId());
                        editor.putLong("signupTime", Calendar.getInstance().getTimeInMillis());
                        editor.putBoolean("signup", true);
                        editor.apply();

                        sendAppUsageDataInTheStartOfTheExperiment(documentReference.getId());

                        // opening the overview screen
                        Intent intent = new Intent(activity, OverviewActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);

                        // showing alert dialog
                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
                        builder.setTitle("Error")
                                .setMessage("Couldn't send data to server. Please let the developers of this app know.")
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

    public void sendAppUsageDataInTheStartOfTheExperiment(String userId) {

        // query the app usage data
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
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
        DocumentReference userRef = db.collection("users").document(userId);

// Set the "isCapital" field of the city 'DC'
        userRef
                .update("Usage Before", appUsagesList.toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }
}
