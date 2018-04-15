package dk.dtu.trackify.trackify.screens;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.dtu.trackify.trackify.MainApplication;
import dk.dtu.trackify.trackify.R;
import dk.dtu.trackify.trackify.entities.AppUsages;

public class OverviewActivity extends AppCompatActivity {

    private static final String TAG = OverviewActivity.class.getSimpleName();

    private HashMap<String, String> appsNames = new HashMap<>();
    private HashMap<String, AppUsages> appsUsedSumed = new HashMap<>();


    private UsageStatsManager mUsageStatsManager;

    private ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        listView = findViewById(R.id.listview);

        mUsageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE); //
        setupMapOfAppsNames();

        findViewById(R.id.settings_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OverviewActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });




        /*
         * FINDING THE TOP USED APPS
         */
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

        for (AppUsages appUsage : appUsagesList) {
            if (appsUsedSumed.containsKey(appUsage.getmPackageName())) {
                AppUsages summedAppUsage = appsUsedSumed.get(appUsage.getmPackageName());
                summedAppUsage.setmTotalTimeInForeground(summedAppUsage.getmTotalTimeInForeground() + appUsage.getmTotalTimeInForeground());
                appsUsedSumed.put(appUsage.getmPackageName(), summedAppUsage);
            } else {
                appsUsedSumed.put(appUsage.getmPackageName(), appUsage);
            }
        }

        Log.d(TAG, "App usage summed -->  " + appsUsedSumed);

        List<AppUsages> sortedSummedAppUsages = new ArrayList<>();
        for (Map.Entry<String, AppUsages> entry : appsUsedSumed.entrySet()) {
            sortedSummedAppUsages.add(entry.getValue());
        }
        Collections.sort(sortedSummedAppUsages);



        /*
         * THE TOP 5 USED APPS
         */
        String appName0 = appsNames.get(sortedSummedAppUsages.get(0).getmPackageName());
        String appName1 = appsNames.get(sortedSummedAppUsages.get(1).getmPackageName());
        String appName2 = appsNames.get(sortedSummedAppUsages.get(2).getmPackageName());
        String appName3 = appsNames.get(sortedSummedAppUsages.get(3).getmPackageName());
        String appName4 = appsNames.get(sortedSummedAppUsages.get(4).getmPackageName());

        long appUsed0 = sortedSummedAppUsages.get(0).getmTotalTimeInForeground() / 1000 / 60;
        long appUsed1 = sortedSummedAppUsages.get(1).getmTotalTimeInForeground() / 1000 / 60;
        long appUsed2 = sortedSummedAppUsages.get(2).getmTotalTimeInForeground() / 1000 / 60;
        long appUsed3 = sortedSummedAppUsages.get(3).getmTotalTimeInForeground() / 1000 / 60;
        long appUsed4 = sortedSummedAppUsages.get(4).getmTotalTimeInForeground() / 1000 / 60;

        final String appName0Final = appName0;
        final String appName1Final = appName1;
        final String appName2Final = appName2;
        final String appName3Final = appName3;
        final String appName4Final = appName4;


        /*
         * SETTING UP THE GRAPH
         */
        GraphView graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, appUsed0),
                new DataPoint(1, appUsed1),
                new DataPoint(2, appUsed2),
                new DataPoint(3, appUsed3),
                new DataPoint(4, appUsed4)
        });
        series.setSpacing(20);
        graph.addSeries(series);

        graph.getGridLabelRenderer().setTextSize(20);
        graph.getGridLabelRenderer().reloadStyles();

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                  //  Log.d("value - oli", "" + value);
                  //  if (true) return ".";
                    // show normal x values
                    if (value == 0) {
                        return appName0Final;
                    }
                    if (value == 1) {
                        return appName1Final;
                    }
                    if (value == 2) {
                        return appName2Final;
                    }
                    if (value == 3) {
                        return appName3Final;
                    }
                    if (value == 4) {
                        return appName4Final;
                    }
                    return "";
                } else {
                    return String.valueOf(value);
                }
            }
        });


        /*
         * Setting up the list view
         */

        // Get ListView object from xml
        ListView listView = (ListView) findViewById(R.id.listview);

        // Defined Array values to show in ListView
        String[] values = new String[appsUsedSumed.size()];
        int i = 0;
        for (Map.Entry<String, AppUsages> entry : appsUsedSumed.entrySet()) {
            long timeInForeground = entry.getValue().getmTotalTimeInForeground() / 1000 / 60;
            values[i] = appsNames.get(entry.getValue().getmPackageName()) + ": " + timeInForeground + " min.";
            i++;
        }


        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        // Assign adapter to ListView
        listView.setAdapter(adapter);
    }

    private void setupMapOfAppsNames() {
        appsNames.put("facebook.com.katana", "Facebook");
        appsNames.put("com.facebook.katana", "Facebook");
        appsNames.put("com.facebook.orca", "Messenger");
        appsNames.put("com.snapchat.android", "Snapchat");
        appsNames.put("com.instagram.android", "Instagram");
        appsNames.put("kik.android", "Kik");
        appsNames.put("com.reddit.frontpage", "Reddit");
        appsNames.put("com.google.android.youtube", "Youtube");
        appsNames.put("com.whatsapp", "Whatsapp");
        appsNames.put("com.twitter.android", "Twitter");
        appsNames.put("com.tinder", "Tinder");
        appsNames.put("com.android.chrome", "Chrome");
        appsNames.put("com.tellm.android.app", "Jodel");

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
