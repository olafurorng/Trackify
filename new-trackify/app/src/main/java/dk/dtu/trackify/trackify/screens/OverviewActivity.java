package dk.dtu.trackify.trackify.screens;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;
import java.util.List;

import dk.dtu.trackify.trackify.MainApplication;
import dk.dtu.trackify.trackify.R;

public class OverviewActivity extends AppCompatActivity {

    private static final String TAG = OverviewActivity.class.getSimpleName();

    private UsageStatsManager mUsageStatsManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        mUsageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE); //

    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // query the app usage data
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, -1);
                List<UsageStats> queryUsageStats = mUsageStatsManager
                        .queryUsageStats(UsageStatsManager.INTERVAL_BEST, cal.getTimeInMillis(),
                                System.currentTimeMillis());

                Log.d(TAG, "App usage data: " + queryUsageStats);
            }
        }, 5000);

    }
}
