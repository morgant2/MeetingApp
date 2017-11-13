package com.meetingapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.meetingapp.R;

/**
 * Created by Jacob on 11/8/2017.
 */

public class LocationTest extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
//public class LocationTest extends AppCompatActivity {

    private static final long UPDATE_INTERVAL =  60 * 1000;
    private static final long FASTEST_INTERVAL = 20 * 1000;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    TextView currentLocation;
    TextView secondLocation;
    Button calculateLocation;

    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager locationManager;
    private LocationRequest mLocationRequest;
    protected LocationListener locationListener;

    protected double currentLat, currentLon;
    protected double secondLat = 39.96;
    protected double secondLon = -83.00;
    double centerLat;
    double centerLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.meetingapp.R.layout.event_location_test);

        secondLocation = (TextView) findViewById(R.id.locationTestLocation2Text) ;
        currentLocation = (TextView) findViewById(R.id.locationTestLocation1Text);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        currentLocation = (TextView) findViewById(com.meetingapp.R.id.locationTestLocation1Text);
        secondLocation = (TextView) findViewById(com.meetingapp.R.id.locationTestLocation2Text);
        calculateLocation = (Button) findViewById(com.meetingapp.R.id.locationTestCalculate);



       //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
       // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        secondLocation.setText(Double.toString(secondLat) + ", " + Double.toString(secondLon));
        //secondLocation.setText("dfsadfadsf");

        //setCurrentLocation();

       //setupButtonEvents();
    }

    private void setupButtonEvents() {
        calculateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent calculatedLocationIntent = new Intent(LocationTest.this, com.meetingapp.MapsActivity.class);
                LocationTest.this.startActivity(calculatedLocationIntent);
            }
        });
    }

    private void setCurrentLocation() {
        //currentLocation.setText(Double.toString(mLocation.getLatitude())+ ", " + Double.toString(mLocation.getLongitude()));
        currentLocation.setText(Double.toString(currentLat)+ ", " + Double.toString(currentLon));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {
            currentLat = mLocation.getLatitude();
            currentLon = mLocation.getLongitude();
            setCurrentLocation();

        } else {
            // Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startLocationUpdates();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

   private void calculateCenterLocation() {
       double curLat = currentLat;
       double curLon = currentLon;
       double lat2 = secondLat;
       double lon2 = secondLon;

       double dLon = Math.toRadians(lon2 - curLon);

       curLat  = Math.toRadians(curLat);
       lat2 = Math.toRadians(lat2);
       curLon = Math.toRadians(curLon);

       double Bx = Math.cos(lat2) * Math.cos(dLon);
       double By = Math.cos(lat2) * Math.sin(dLon);
       double var1 = Math.sin(curLat) + Math.sin(lat2);
       double var2 =  Math.sqrt((Math.cos(curLat) + Bx) * (Math.cos(curLat) + Bx) + By * By);
       centerLat = Math.atan2(var1, var2);
       centerLon = curLon + Math.atan2(By, Math.cos(curLat) + Bx);


   }
}
