package com.trackify.trackify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.tab_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTab1Clicked();
            }
        });
        findViewById(R.id.tab_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTab2Clicked();
            }
        });

        findViewById(R.id.delete_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = getBaseContext().getPackageManager()
                //        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(i);

                SharedPreferences.Editor edit= getSharedPreferences("NAME", Context.MODE_PRIVATE).edit();
                edit.clear();
                edit.commit();
            }
        });
    }

    private void onTab1Clicked() {
        Intent intent = new Intent(this, TodayActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void onTab2Clicked() {
        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}