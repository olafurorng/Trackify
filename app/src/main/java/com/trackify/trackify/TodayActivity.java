package com.trackify.trackify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.trackify.trackify.customview.ActivityItem;

public class TodayActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        findViewById(R.id.mapViewButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMapButtonClicked();
            }
        });

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

        ActivityItem activityItem1 = findViewById(R.id.activity_1);
        activityItem1.setData(ActivityItem.Type.HOME, "56 min | 7:00 - 7:56");
        ActivityItem activityItem2 = findViewById(R.id.activity_2);
        activityItem2.setData(ActivityItem.Type.TRANSPORT, "45 min | 7:56 - 8:41");
        ActivityItem activityItem3 = findViewById(R.id.activity_3);
        activityItem3.setData(ActivityItem.Type.WALKING, "12 min | 8:41 - 8:53");
        ActivityItem activityItem4 = findViewById(R.id.activity_4);
        activityItem4.setData(ActivityItem.Type.STUDYING, "7h 12 min | 8:53 - 16:35");
        ActivityItem activityItem5 = findViewById(R.id.activity_5);
        activityItem5.setData(ActivityItem.Type.WALKING, "12 min | 16:35 - 17:47");
        ActivityItem activityItem6 = findViewById(R.id.activity_6);
        activityItem6.setData(ActivityItem.Type.TRANSPORT, "45 min | 17:47 - 18:32");
        ActivityItem activityItem7 = findViewById(R.id.activity_7);
        activityItem7.setData(ActivityItem.Type.HOME, "4h 20 min | 18:32 - 22:52");
    }

    private void onMapButtonClicked() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }


    private void onTab2Clicked() {
        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void onTab3Clicked() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}