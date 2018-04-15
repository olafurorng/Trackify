package dk.dtu.trackify.trackify.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import dk.dtu.trackify.trackify.MainApplication;
import dk.dtu.trackify.trackify.R;

public class WelcomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.welcome_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameEditText = findViewById(R.id.name);
                String name = nameEditText.getText().toString();
                MainApplication mainApplication = (MainApplication) getApplication();
                mainApplication.addUser(name, WelcomeActivity.this);

                findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
                findViewById(R.id.welcome_button).setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        // check if user has already signed up
        SharedPreferences prefs = getSharedPreferences("Trackify", MODE_PRIVATE);
        boolean hasSignedUp = prefs.getBoolean("signup", false);
        Log.d(WelcomeActivity.class.getSimpleName(), "Has signed up: " + hasSignedUp);
        if (hasSignedUp) {
            // opening the overview screen
            Intent intent = new Intent(this, OverviewActivity.class);
            startActivity(intent);
        }
    }
}
