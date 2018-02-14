package com.trackify.trackify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.trackify.trackify.customview.ActivityItem;

public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laucher);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openApp();
            }
        }, 2300);

        ShimmerFrameLayout container = findViewById(R.id.shimmer_view_container1);
        container.startShimmerAnimation();
    }


    private void openApp() {
        Intent intent = new Intent(this, TodayActivity.class);
        startActivity(intent);
    }
}