package dk.dtu.trackify.trackify;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.usage.UsageStatsManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import dk.dtu.trackify.trackify.screens.OverviewActivity;
import dk.dtu.trackify.trackify.screens.WelcomeActivity;

public class MainApplication extends Application {

    private static final String TAG = MainApplication.class.getSimpleName();

    private FirebaseFirestore db; // our database, Firestore from Google Firebase

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(MainApplication.this);



    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public void addUser(String name, final Activity activity) {
        db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("Full Name", name);
        user.put("Time", System.currentTimeMillis());


        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                        SharedPreferences.Editor editor = getSharedPreferences("Trackify", MODE_PRIVATE).edit();
                        editor.putBoolean("signup", true);
                        editor.apply();


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
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(activity);
                        }
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
}
