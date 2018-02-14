package com.trackify.trackify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

public class TodayActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);


        findViewById(R.id.tab_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTab2Clicked();
            }
        });
        findViewById(R.id.tab_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTab3Clicked();
            }
        });
    }


    private void onTab2Clicked() {
        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent);
    }

    private void onTab3Clicked() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}