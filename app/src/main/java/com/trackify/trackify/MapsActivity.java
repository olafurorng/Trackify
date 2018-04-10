package com.trackify.trackify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    ArrayList<LatLng> latLngList = new ArrayList<LatLng>();
    private String sharedPrefsLat = "sharedPrefsLat";
    private String sharedPrefsLng = "sharedPrefsLng";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        findViewById(R.id.listViewButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListButtonClicked();
            }
        });

//        List<Float> lat = getFromPrefs(sharedPrefsLat);
//        List<Float> lng = getFromPrefs(sharedPrefsLng);
//
//        for (int i = 0; i < lat.size(); i++) {
//            latLngList.add(new LatLng(lat.get(i), lng.get(i)));
//        }

        getLatLngData();
    }

    @Override
    protected void onDestroy() {


//        SharedPreferences.Editor edit= getSharedPreferences("NAME", Context.MODE_PRIVATE).edit();
//        edit.clear();
//        edit.commit();
//
//        List<Float> lat = new ArrayList<>();
//        List<Float> lng = new ArrayList<>();
//
//        for (int i = 0; i < latLngList.size(); i++) {
//            lat.add((float) latLngList.get(i).latitude);
//            lng.add((float) latLngList.get(i).longitude);
//        }
//
//
//        storeIntArray(sharedPrefsLat, lat);
//        storeIntArray(sharedPrefsLng, lng);
//
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();

        startLocationUpdates();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mMap != null) {
                    //Create polyline
                    mMap.addPolyline(new PolylineOptions()
                            .clickable(false)
                            .addAll(latLngList)
                            .color(Color.BLUE));


                }
            }
        }, 1200);
    }

    public void storeIntArray(String name, List<Float> array){
        SharedPreferences.Editor edit= getSharedPreferences("NAME", Context.MODE_PRIVATE).edit();
        edit.putInt("Count_" + name, array.size());
        int count = 0;
        for (Float i: array){
            edit.putFloat("IntValue_" + name + count++, i);
        }
        edit.commit();
    }
    public List<Float> getFromPrefs(String name){
        List<Float> ret = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences("NAME", Context.MODE_PRIVATE);
        int count = prefs.getInt("Count_" + name, 0);
        for (int i = 0; i < count; i++){
            ret.add(prefs.getFloat("IntValue_"+ name + i, i));
        }
        return ret;
    }

    @SuppressWarnings({"MissingPermission"})
    private void startLocationUpdates() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 150, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latLngList.add(new LatLng(location.getLatitude(), location.getLongitude()));
                    Log.d("oli_data", "Lat: " + location.getLatitude() + " Lng: " + location.getLongitude());
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        } else {
            Toast.makeText(MapsActivity.this, "Location manager is not available!", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Create coordinates for home and DTU
        LatLng dtu = new LatLng(55.785637, 12.521382);
        LatLng home = new LatLng(55.682185, 12.583209);

        //Add circle and marker for DTU
        mMap.addCircle(new CircleOptions()
                .center(dtu)
                .radius(1000)
                .strokeColor(Color.RED)
                .fillColor(0x330000FF));
        mMap.addMarker(new MarkerOptions()
                .position(dtu)
                .title("University")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_school_black_36dp))
                .anchor(.5f,.5f));

        //Add circle and marker for home
        mMap.addCircle(new CircleOptions()
                .center(home)
                .radius(300)
                .strokeColor(Color.RED)
                .fillColor(0x330000FF));
        mMap.addMarker(new MarkerOptions()
                .position(home)
                .title("Home")
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_home_black_36dp))
        .anchor(.5f,.5f));

        //Adjust camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(home));
        mMap.setMinZoomPreference(10);
    }

    private void onListButtonClicked() {
        Intent intent = new Intent(this, TodayActivity.class);
        startActivity(intent);
    }

    private void onTab2Clicked() {
        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent);
    }

    private void onTab3Clicked() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void getLatLngData() {
        //latLngList.add()

        latLngList.add(new LatLng(55.68222427368164, 12.583290100097656));
        latLngList.add(new LatLng(55.682228088378906, 12.583280563354492));
        latLngList.add(new LatLng(55.68223571777344, 12.583273887634277));
        latLngList.add(new LatLng(55.682122313739214, 12.582348690386382));
        latLngList.add(new LatLng(55.682826749730715, 12.58031211474316));
        latLngList.add(new LatLng(55.68348717709406, 12.57820986594056));
        latLngList.add(new LatLng(55.68403276083677, 12.57602297003802));
        latLngList.add(new LatLng(55.68437443686394, 12.573712517492455));
        latLngList.add(new LatLng(55.68552922128491, 12.575006577272026));
        latLngList.add(new LatLng(55.68655234477141, 12.576607999343333));
        latLngList.add(new LatLng(55.68748697128457, 12.57835532010631));
        latLngList.add(new LatLng(55.68851461687188, 12.57662349507016));
        latLngList.add(new LatLng(55.68949200830305, 12.574965242948283));
        latLngList.add(new LatLng(55.69048000107145, 12.573326870099276));
        latLngList.add(new LatLng(55.691169343481846, 12.571223261112857));
        latLngList.add(new LatLng(55.6920302170133, 12.569344252090707));
        latLngList.add(new LatLng(55.69300466330312, 12.567532426029985));
        latLngList.add(new LatLng(55.69399052423391, 12.565785759629426));
        latLngList.add(new LatLng(55.69499784002319, 12.563987016566662));
        latLngList.add(new LatLng(55.69601126586443, 12.562271979698465));
        latLngList.add(new LatLng(55.69735855675163, 12.561765641186744));
        latLngList.add(new LatLng(55.69870747238862, 12.561911283510046));
        latLngList.add(new LatLng(55.7000612134833, 12.562114614689417));
        latLngList.add(new LatLng(55.70145436420995, 12.562272762421676));
        latLngList.add(new LatLng(55.70287517570809, 12.562395339933982));
        latLngList.add(new LatLng(55.70428642338357, 12.562524774380393));
        latLngList.add(new LatLng(55.70563575641362, 12.562845226011703));
        latLngList.add(new LatLng(55.70589541614763, 12.56290477007097));
        latLngList.add(new LatLng(55.70589541614763, 12.56290477007097));
        latLngList.add(new LatLng(55.70589541614763, 12.56290477007097));
        latLngList.add(new LatLng(55.70699235887656, 12.562842674525845));
        latLngList.add(new LatLng(55.70730907465646, 12.562654544252059));
        latLngList.add(new LatLng(55.70730907465646, 12.562654544252059));
        latLngList.add(new LatLng(55.70730907465646, 12.562654544252059));
        latLngList.add(new LatLng(55.70829782741995, 12.562091740308148));
        latLngList.add(new LatLng(55.70860123078761, 12.56191612568869));
        latLngList.add(new LatLng(55.70860123078761, 12.56191612568869));
        latLngList.add(new LatLng(55.70860123078761, 12.56191612568869));
        latLngList.add(new LatLng(55.70959687476751, 12.561220089410435));
        latLngList.add(new LatLng(55.70989379708829, 12.561043356732586));
        latLngList.add(new LatLng(55.70989379708829, 12.561043356732586));
        latLngList.add(new LatLng(55.70989379708829, 12.561043356732586));
        latLngList.add(new LatLng(55.7109336706521, 12.56058494935711));
        latLngList.add(new LatLng(55.7113016120205, 12.56041252724147));
        latLngList.add(new LatLng(55.7113016120205, 12.56041252724147));
        latLngList.add(new LatLng(55.7113016120205, 12.56041252724147));
        latLngList.add(new LatLng(55.71231309458824, 12.559995033026034));
        latLngList.add(new LatLng(55.71265358671647, 12.559936034122352));
        latLngList.add(new LatLng(55.71265358671647, 12.559936034122352));
        latLngList.add(new LatLng(55.71265358671647, 12.559936034122352));
        latLngList.add(new LatLng(55.71368368668707, 12.559567249528477));
        latLngList.add(new LatLng(55.71409760178869, 12.559379854629066));
        latLngList.add(new LatLng(55.71409760178869, 12.559379854629066));
        latLngList.add(new LatLng(55.71409760178869, 12.559379854629066));
        latLngList.add(new LatLng(55.71508964433587, 12.558937477626017));
        latLngList.add(new LatLng(55.715471674320746, 12.558779361782154));
        latLngList.add(new LatLng(55.715471674320746, 12.558779361782154));
        latLngList.add(new LatLng(55.715471674320746, 12.558779361782154));
        latLngList.add(new LatLng(55.71642103411761, 12.558458034834812));
        latLngList.add(new LatLng(55.71679790676234, 12.558281040185824));
        latLngList.add(new LatLng(55.71679790676234, 12.558281040185824));
        latLngList.add(new LatLng(55.71679790676234, 12.558281040185824));
        latLngList.add(new LatLng(55.71771839218597, 12.557609547456964));
        latLngList.add(new LatLng(55.71808833309401, 12.557467271383834));
        latLngList.add(new LatLng(55.71808833309401, 12.557467271383834));
        latLngList.add(new LatLng(55.71808833309401, 12.557467271383834));
        latLngList.add(new LatLng(55.719049675965756, 12.556894718305195));
        latLngList.add(new LatLng(55.71946069901365, 12.556694740393134));
        latLngList.add(new LatLng(55.71946069901365, 12.556694740393134));
        latLngList.add(new LatLng(55.71946069901365, 12.556694740393134));
        latLngList.add(new LatLng(55.720439945415144, 12.556299057257268));
        latLngList.add(new LatLng(55.72083621944046, 12.556137153717215));
        latLngList.add(new LatLng(55.72083621944046, 12.556137153717215));
        latLngList.add(new LatLng(55.72083621944046, 12.556137153717215));
        latLngList.add(new LatLng(55.721827253111535, 12.555647159943442));
        latLngList.add(new LatLng(55.72227408620874, 12.555422958739063));
        latLngList.add(new LatLng(55.72227408620874, 12.555422958739063));
        latLngList.add(new LatLng(55.72227408620874, 12.555422958739063));
        latLngList.add(new LatLng(55.723305114291804, 12.554899083973474));
        latLngList.add(new LatLng(55.72374267233831, 12.554672315221465));
        latLngList.add(new LatLng(55.72374267233831, 12.554672315221465));
        latLngList.add(new LatLng(55.72374267233831, 12.554672315221465));
        latLngList.add(new LatLng(55.724668797039, 12.554072911700272));
        latLngList.add(new LatLng(55.72503164562089, 12.553778715626207));
        latLngList.add(new LatLng(55.72503164562089, 12.553778715626207));
        latLngList.add(new LatLng(55.72503164562089, 12.553778715626207));
        latLngList.add(new LatLng(55.72593494251102, 12.552863235700682));
        latLngList.add(new LatLng(55.72625417692055, 12.552528267458113));
        latLngList.add(new LatLng(55.72625417692055, 12.552528267458113));
        latLngList.add(new LatLng(55.72625417692055, 12.552528267458113));
        latLngList.add(new LatLng(55.72710405375678, 12.551648736550398));
        latLngList.add(new LatLng(55.72746786949483, 12.551243500451756));
        latLngList.add(new LatLng(55.72746786949483, 12.551243500451756));
        latLngList.add(new LatLng(55.72746786949483, 12.551243500451756));
        latLngList.add(new LatLng(55.728234502735575, 12.550333120211956));
        latLngList.add(new LatLng(55.72866010819481, 12.549905035586136));
        latLngList.add(new LatLng(55.72866010819481, 12.549905035586136));
        latLngList.add(new LatLng(55.72866010819481, 12.549905035586136));
        latLngList.add(new LatLng(55.72942815675615, 12.549048878074755));
        latLngList.add(new LatLng(55.729893857221, 12.548497878051615));
        latLngList.add(new LatLng(55.729893857221, 12.548497878051615));
        latLngList.add(new LatLng(55.729893857221, 12.548497878051615));
        latLngList.add(new LatLng(55.730642576947346, 12.547613100576937));
        latLngList.add(new LatLng(55.731158352628725, 12.547017733559112));
        latLngList.add(new LatLng(55.731158352628725, 12.547017733559112));
        latLngList.add(new LatLng(55.731158352628725, 12.547017733559112));
        latLngList.add(new LatLng(55.731966724360646, 12.546091353895488));
        latLngList.add(new LatLng(55.73252524131237, 12.545437543434698));
        latLngList.add(new LatLng(55.73252524131237, 12.545437543434698));
        latLngList.add(new LatLng(55.73252524131237, 12.545437543434698));
        latLngList.add(new LatLng(55.73339822554352, 12.544483999629879));
        latLngList.add(new LatLng(55.73403039469335, 12.54381098330433));
        latLngList.add(new LatLng(55.73403039469335, 12.54381098330433));
        latLngList.add(new LatLng(55.73403039469335, 12.54381098330433));
        latLngList.add(new LatLng(55.735016984513855, 12.542734288029393));
        latLngList.add(new LatLng(55.73569555748767, 12.542006910880671));
        latLngList.add(new LatLng(55.73569555748767, 12.542006910880671));
        latLngList.add(new LatLng(55.73569555748767, 12.542006910880671));
        latLngList.add(new LatLng(55.73672212692315, 12.540929719713787));
        latLngList.add(new LatLng(55.737399787150146, 12.54020524889979));
        latLngList.add(new LatLng(55.737399787150146, 12.54020524889979));
        latLngList.add(new LatLng(55.737399787150146, 12.54020524889979));
        latLngList.add(new LatLng(55.73842094451463, 12.53908739841825));
        latLngList.add(new LatLng(55.73908388301599, 12.538324041644863));
        latLngList.add(new LatLng(55.73908388301599, 12.538324041644863));
        latLngList.add(new LatLng(55.73908388301599, 12.538324041644863));
        latLngList.add(new LatLng(55.740086694441956, 12.5372379921805));
        latLngList.add(new LatLng(55.74077437588026, 12.53649785543802));
        latLngList.add(new LatLng(55.74077437588026, 12.53649785543802));
        latLngList.add(new LatLng(55.74077437588026, 12.53649785543802));
        latLngList.add(new LatLng(55.741799313492095, 12.535406934690926));
        latLngList.add(new LatLng(55.74248735841371, 12.534679570179843));
        latLngList.add(new LatLng(55.74248735841371, 12.534679570179843));
        latLngList.add(new LatLng(55.74248735841371, 12.534679570179843));
        latLngList.add(new LatLng(55.74349684391465, 12.53360546190814));
        latLngList.add(new LatLng(55.744183328968354, 12.532896663824838));
        latLngList.add(new LatLng(55.744183328968354, 12.532896663824838));
        latLngList.add(new LatLng(55.744183328968354, 12.532896663824838));
        latLngList.add(new LatLng(55.74522655431607, 12.531867642903173));
        latLngList.add(new LatLng(55.74592837151438, 12.53114952642649));
        latLngList.add(new LatLng(55.74592837151438, 12.53114952642649));
        latLngList.add(new LatLng(55.74592837151438, 12.53114952642649));
        latLngList.add(new LatLng(55.74696357295259, 12.530076995590388));
        latLngList.add(new LatLng(55.74763378279057, 12.5293276744284));
        latLngList.add(new LatLng(55.74763378279057, 12.5293276744284));
        latLngList.add(new LatLng(55.74763378279057, 12.5293276744284));
        latLngList.add(new LatLng(55.748543805610126, 12.528259059279975));
        latLngList.add(new LatLng(55.74909400067531, 12.527584630523366));
        latLngList.add(new LatLng(55.74909400067531, 12.527584630523366));
        latLngList.add(new LatLng(55.74909400067531, 12.527584630523366));
        latLngList.add(new LatLng(55.74985655748797, 12.52659863503081));
        latLngList.add(new LatLng(55.75033019677538, 12.526020146713021));
        latLngList.add(new LatLng(55.75033019677538, 12.526020146713021));
        latLngList.add(new LatLng(55.75033019677538, 12.526020146713021));
        latLngList.add(new LatLng(55.75106208548172, 12.5252036346833));
        latLngList.add(new LatLng(55.751516131551455, 12.524713578871209));
        latLngList.add(new LatLng(55.751516131551455, 12.524713578871209));
        latLngList.add(new LatLng(55.751516131551455, 12.524713578871209));
        latLngList.add(new LatLng(55.7522588253385, 12.523884928430665));
        latLngList.add(new LatLng(55.75270706149946, 12.52344993887747));
        latLngList.add(new LatLng(55.75270706149946, 12.52344993887747));
        latLngList.add(new LatLng(55.75270706149946, 12.52344993887747));
        latLngList.add(new LatLng(55.7534515113147, 12.522684836427295));
        latLngList.add(new LatLng(55.75389122073261, 12.52229264785466));
        latLngList.add(new LatLng(55.75389122073261, 12.52229264785466));
        latLngList.add(new LatLng(55.75389122073261, 12.52229264785466));
        latLngList.add(new LatLng(55.754681359170384, 12.521660146129001));
        latLngList.add(new LatLng(55.755230312939766, 12.521515035680352));
        latLngList.add(new LatLng(55.755230312939766, 12.521515035680352));
        latLngList.add(new LatLng(55.755230312939766, 12.521515035680352));
        latLngList.add(new LatLng(55.756118230881995, 12.52155355306391));
        latLngList.add(new LatLng(55.75674454522482, 12.521641551666514));
        latLngList.add(new LatLng(55.75674454522482, 12.521641551666514));
        latLngList.add(new LatLng(55.75674454522482, 12.521641551666514));
        latLngList.add(new LatLng(55.757764001555685, 12.521777341535806));
        latLngList.add(new LatLng(55.7584473252226, 12.521867144221305));
        latLngList.add(new LatLng(55.7584473252226, 12.521867144221305));
        latLngList.add(new LatLng(55.7584473252226, 12.521867144221305));
        latLngList.add(new LatLng(55.75955795725326, 12.522011245739064));
        latLngList.add(new LatLng(55.76031646993305, 12.5220850154235));
        latLngList.add(new LatLng(55.76031646993305, 12.5220850154235));
        latLngList.add(new LatLng(55.76031646993305, 12.5220850154235));
        latLngList.add(new LatLng(55.76149913521947, 12.52224292923967));
        latLngList.add(new LatLng(55.76228221394148, 12.522352250879267));
        latLngList.add(new LatLng(55.76228221394148, 12.522352250879267));
        latLngList.add(new LatLng(55.76228221394148, 12.522352250879267));
        latLngList.add(new LatLng(55.763468421670154, 12.522549406222039));
        latLngList.add(new LatLng(55.76428217521177, 12.52266968046597));
        latLngList.add(new LatLng(55.76428217521177, 12.52266968046597));
        latLngList.add(new LatLng(55.76428217521177, 12.52266968046597));
        latLngList.add(new LatLng(55.76547046509219, 12.52299911528873));
        latLngList.add(new LatLng(55.76626645535435, 12.523380331131461));
        latLngList.add(new LatLng(55.76626645535435, 12.523380331131461));
        latLngList.add(new LatLng(55.76626645535435, 12.523380331131461));
        latLngList.add(new LatLng(55.7674395900456, 12.52380761786918));
        latLngList.add(new LatLng(55.76827264134831, 12.523938715564753));
        latLngList.add(new LatLng(55.76827264134831, 12.523938715564753));
        latLngList.add(new LatLng(55.76827264134831, 12.523938715564753));
        latLngList.add(new LatLng(55.769469907269105, 12.523876715042862));
        latLngList.add(new LatLng(55.770270331135386, 12.52379814637877));
        latLngList.add(new LatLng(55.770270331135386, 12.52379814637877));
        latLngList.add(new LatLng(55.770270331135386, 12.52379814637877));
        latLngList.add(new LatLng(55.77145577419675, 12.523830433037459));
        latLngList.add(new LatLng(55.77222686868521, 12.523888167558722));
        latLngList.add(new LatLng(55.77222686868521, 12.523888167558722));
        latLngList.add(new LatLng(55.77222686868521, 12.523888167558722));
        latLngList.add(new LatLng(55.773415098994725, 12.52404130101761));
        latLngList.add(new LatLng(55.77422378293633, 12.52416470676994));
        latLngList.add(new LatLng(55.77422378293633, 12.52416470676994));
        latLngList.add(new LatLng(55.77422378293633, 12.52416470676994));
        latLngList.add(new LatLng(55.77544284778119, 12.524548252471087));
        latLngList.add(new LatLng(55.77621297532767, 12.52485713476188));
        latLngList.add(new LatLng(55.77621297532767, 12.52485713476188));
        latLngList.add(new LatLng(55.77621297532767, 12.52485713476188));
        latLngList.add(new LatLng(55.777352115030006, 12.525313650549924));
        latLngList.add(new LatLng(55.77811241642571, 12.525608537190903));
        latLngList.add(new LatLng(55.77811241642571, 12.525608537190903));
        latLngList.add(new LatLng(55.77811241642571, 12.525608537190903));
        latLngList.add(new LatLng(55.779281367900026, 12.526054049894803));
        latLngList.add(new LatLng(55.78007536044606, 12.526353215176027));
        latLngList.add(new LatLng(55.78007536044606, 12.526353215176027));
        latLngList.add(new LatLng(55.78007536044606, 12.526353215176027));
        latLngList.add(new LatLng(55.78125313873138, 12.526794824318548));
        latLngList.add(new LatLng(55.782037917646676, 12.527095279114635));
        latLngList.add(new LatLng(55.782037917646676, 12.527095279114635));
        latLngList.add(new LatLng(55.782037917646676, 12.527095279114635));
        latLngList.add(new LatLng(55.783223217884654, 12.527536547832756));
        latLngList.add(new LatLng(55.78401527258677, 12.527828809138956));
        latLngList.add(new LatLng(55.78401527258677, 12.527828809138956));
        latLngList.add(new LatLng(55.78401527258677, 12.527828809138956));
        latLngList.add(new LatLng(55.78517770274454, 12.528270500594779));
        latLngList.add(new LatLng(55.78595095431691, 12.52859337844308));
        latLngList.add(new LatLng(55.78595095431691, 12.52859337844308));
        latLngList.add(new LatLng(55.78595095431691, 12.52859337844308));
        latLngList.add(new LatLng(55.78698868111932, 12.529067316005042));
        latLngList.add(new LatLng(55.78751798552857, 12.529245539499067));
        latLngList.add(new LatLng(55.78751798552857, 12.529245539499067));
        latLngList.add(new LatLng(55.78751798552857, 12.529245539499067));
        latLngList.add(new LatLng(55.78832205665649, 12.52958694738206));
        latLngList.add(new LatLng(55.78759799371686, 12.527570508269994));
        latLngList.add(new LatLng(55.78767059739866, 12.526828289515962));
        latLngList.add(new LatLng(55.78767059739866, 12.526828289515962));
        latLngList.add(new LatLng(55.78767059739866, 12.526828289515962));
        latLngList.add(new LatLng(55.786891609683614, 12.525524288957461));
        latLngList.add(new LatLng(55.786639338753034, 12.52528092554648));
        latLngList.add(new LatLng(55.786639338753034, 12.52528092554648));
        latLngList.add(new LatLng(55.786639338753034, 12.52528092554648));
        latLngList.add(new LatLng(55.78666600710095, 12.525196092887242));
        latLngList.add(new LatLng(55.785537802096954, 12.523319523075456));
        latLngList.add(new LatLng(55.785537802096954, 12.523319523075456));
        latLngList.add(new LatLng(55.785537802096954, 12.523319523075456));
        latLngList.add(new LatLng(55.785537802096954, 12.523319523075456));
        latLngList.add(new LatLng(55.785537802096954, 12.523319523075456));
        latLngList.add(new LatLng(55.78566457557105, 12.52093884159968));
        latLngList.add(new LatLng(55.78566457557105, 12.52093884159968));
        latLngList.add(new LatLng(55.78566457557105, 12.52093884159968));
        latLngList.add(new LatLng(55.78566457557105, 12.52093884159968));
        latLngList.add(new LatLng(55.78566457557105, 12.52093884159968));
        latLngList.add(new LatLng(55.784624597212606, 12.519415117537745));
        latLngList.add(new LatLng(55.784624597212606, 12.519415117537745));
        latLngList.add(new LatLng(55.784624597212606, 12.519415117537745));
        latLngList.add(new LatLng(55.784624597212606, 12.519415117537745));
        latLngList.add(new LatLng(55.784624597212606, 12.519415117537745));


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mMap != null) {
                    //Create polyline
                    mMap.addPolyline(new PolylineOptions()
                            .clickable(false)
                            .addAll(latLngList)
                            .color(Color.BLUE));


                }
            }
        }, 800);

    }
}
