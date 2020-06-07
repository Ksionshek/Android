package com.example.laboratrium7;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener, SensorEventListener{

    private static final int MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 101;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback locationCallback;
    Marker gpsMarker = null;

    public String JSON_FILE = "tasks.json";
    List<MarkerPos> markerPosList;

    static public SensorManager mSensorManager;

    private Sensor mSensor;
    private boolean isActive;

    FloatingActionButton fab_x;
    FloatingActionButton fab_Dot;

    private  boolean isInvisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        markerPosList = new ArrayList<>();

        isActive = false;
        isInvisible = true;

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if(mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null){
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }else{
            //fail
        }

        fab_x = findViewById(R.id.x_fab);
        fab_Dot = findViewById(R.id.dot_fab);
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
        mMap.setOnMapLoadedCallback(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapLongClickListener(this);
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, null);
    }

    private void createLocationCallback() {

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    if (gpsMarker != null)
                        gpsMarker.remove();
                }
            }
        };
    }

    @Override
    public void onMapLoaded() {
        Log.i(MapsActivity.class.getSimpleName(), "MapLoaded");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        restoreMarkersFromJsonFile();
        createLocationRequest();
        createLocationCallback();
        startLocationUpdates();

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
        if (mSensor != null)
            MapsActivity.mSensorManager.unregisterListener(this, mSensor);

    }

    private void stopLocationUpdates() {
        if (locationCallback != null)
            fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.addMarker(new MarkerOptions()
        .position(new LatLng(latLng.latitude,latLng.longitude))
        .alpha(0.8f)
        .title(String.format("Position:(%.2f, %.2f)",latLng.latitude,latLng.longitude)));

        markerPosList.add(new MarkerPos(latLng.latitude, latLng.longitude));

    }
    @Override
    protected void onDestroy() {
        saveMarkersToJsonFile();
        super.onDestroy();
    }

    private void saveMarkersToJsonFile() {
        Gson gson = new Gson();
        String listJson = gson.toJson(markerPosList);
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(JSON_FILE, MODE_PRIVATE);
            FileWriter writer = new FileWriter(outputStream.getFD());
            writer.write(listJson);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void restoreMarkersFromJsonFile() {
        FileInputStream inputStream;
        int DEFAULT_BUFFER_SIZE = 10000;
        Gson gson = new Gson();
        String readJson;

        try {
            inputStream = openFileInput(JSON_FILE);
            FileReader reader = new FileReader(inputStream.getFD());
            char[] buf = new char[DEFAULT_BUFFER_SIZE];
            int n;
            StringBuilder builder = new StringBuilder();
            while ((n = reader.read(buf)) >= 0) {

                String tmp = String.valueOf(buf);
                String substring = (n < DEFAULT_BUFFER_SIZE) ? tmp.substring(0, n) : tmp;
                builder.append(substring);
            }
            reader.close();
            readJson = builder.toString();
            Type collectionType = new TypeToken<List<MarkerPos>>() {
            }.getType();
            List<MarkerPos> o = gson.fromJson(readJson, collectionType);
            if (o != null) {
                markerPosList.clear();
                for (MarkerPos marker : o) {
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(marker.latitude, marker.longitude))
                            .alpha(0.8f)
                            .title(String.format("Position:(%.2f, %.2f)", marker.latitude, marker.longitude)));

                    markerPosList.add(new MarkerPos(marker.latitude, marker.longitude));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        Animation animation_x = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_x_visible);
        Animation animation_dot = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_dot_visible);

        if (isInvisible) {
            fab_x.setVisibility(View.VISIBLE);
            fab_x.startAnimation(animation_x);

            fab_Dot.setVisibility(View.VISIBLE);
            fab_Dot.startAnimation(animation_dot);

            isInvisible = false;
        }
        return false;

    }

    public void zoomInClick(View v){
        mMap.moveCamera(CameraUpdateFactory.zoomIn());
    }

    public void zoomOutClick(View v){
        mMap.moveCamera(CameraUpdateFactory.zoomOut());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("Acceleration:\nx: %.4f y: %.4f", event.values[0], event.values[1]));

        TextView textView = findViewById(R.id.sensorLabel);
        textView.setText(stringBuilder.toString());
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (mSensor != null) {
            MapsActivity.mSensorManager.registerListener(this, mSensor, 100000);
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void clearAllMarkersFromMap(View view) {
        mMap.clear();
        markerPosList.removeAll(markerPosList);
        saveMarkersToJsonFile();
    }
    public void hideFloatingActionButton(View view) {
        Animation animation_x = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_x_invisible);
        Animation animation_dot = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_dot_invisible);

        fab_x.startAnimation(animation_x);
        fab_x.setVisibility(View.INVISIBLE);

        fab_Dot.startAnimation(animation_dot);
        fab_Dot.setVisibility(View.INVISIBLE);

        isInvisible = true;
    }

    public void switchAccelerometer(View view) {
        if (isActive) {
            isActive = false;
            onPause();
            findViewById(R.id.sensorLabel).setVisibility(View.INVISIBLE);
        } else {
            isActive = true;
            onResume();
            findViewById(R.id.sensorLabel).setVisibility(View.VISIBLE);
        }
    }
}