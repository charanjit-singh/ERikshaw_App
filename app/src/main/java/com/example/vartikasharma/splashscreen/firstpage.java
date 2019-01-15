package com.example.vartikasharma.splashscreen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class firstpage extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback,View.OnClickListener
         {
    private GoogleMap mMap;
             Location mLastLocation;
             Marker mCurrLocationMarker;
             GoogleApiClient mGoogleApiClient;
             LocationRequest mLocationRequest;
             private TextView tvClickMe;
             TextView ridenowbutton;
             SupportMapFragment sMapFragment;

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
   // private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
             private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    //vars
    private Boolean mLocationPermissionsGranted = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sMapFragment = SupportMapFragment.newInstance();
        android.support.v4.app.FragmentManager sfm =getSupportFragmentManager();
        setContentView(R.layout.activity_firstpage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sMapFragment.getMapAsync(this);
        sfm.beginTransaction().add(R.id.maps,sMapFragment).commit();
        if(sMapFragment.isAdded())
            sfm.beginTransaction().hide(sMapFragment).commit();

        ridenowbutton = (TextView) findViewById(R.id.ridenow);
        ridenowbutton.setOnClickListener(this);




    }
             @Override
             public void onClick(View v) {
                 switch (v.getId()) {
                     case R.id.ridenow:

                         MyBottomSheetDialog myBottomSheetDialog = MyBottomSheetDialog.getInstance(this);
                         myBottomSheetDialog.setTvTitle("Your Ride Has Been Accepted By Ramesh");
                         myBottomSheetDialog.setTvSubTitle("Ramesh is on his way");
                        // myBottomSheetDialog.setIvAvatar("https://avatars3.githubusercontent.com/u/6635954?v=3&u=d18aab686938ecda4b96f29e4e3b776008ced91f&s=400");
                         myBottomSheetDialog.setCanceledOnTouchOutside(false);
                         myBottomSheetDialog.show();

                         break;
                 }
             }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.firstpage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_search) {
          //  return true;
        //}
        switch (item.getItemId()) {
            case R.id.action_search:
                // search action
                return true;

            case R.id.action_fav:
                // location found
                return true;

            case R.id.action_notify:
                // refresh
                return true;
            case R.id.action_rik:
                // refresh
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        mMap.setOnMyLocationClickListener(onMyLocationClickListener);
        enableMyLocationIfPermitted();

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(11);


    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(firstpage.this);
    }

             private void enableMyLocationIfPermitted() {
                 if (ContextCompat.checkSelfPermission(this,
                         Manifest.permission.ACCESS_FINE_LOCATION)
                         != PackageManager.PERMISSION_GRANTED) {
                     ActivityCompat.requestPermissions(this,
                             new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                     Manifest.permission.ACCESS_FINE_LOCATION},
                             LOCATION_PERMISSION_REQUEST_CODE);
                 } else if (mMap != null) {
                     mMap.setMyLocationEnabled(true);
                 }
             }

             private void showDefaultLocation() {
                 Toast.makeText(this, "Location permission not granted, " +
                                 "showing default location",
                         Toast.LENGTH_SHORT).show();
                 LatLng redmond = new LatLng(47.6739881, -122.121512);
                 mMap.moveCamera(CameraUpdateFactory.newLatLng(redmond));
             }

             @Override
             public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                    @NonNull int[] grantResults) {
                 switch (requestCode) {
                     case LOCATION_PERMISSION_REQUEST_CODE: {
                         if (grantResults.length > 0
                                 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                             enableMyLocationIfPermitted();
                         } else {
                             showDefaultLocation();
                         }
                         return;
                     }

                 }
             }

             private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
                     new GoogleMap.OnMyLocationButtonClickListener() {
                         @Override
                         public boolean onMyLocationButtonClick() {
                             mMap.setMinZoomPreference(15);
                             return false;
                         }
                     };

             private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
                     new GoogleMap.OnMyLocationClickListener() {
                         @Override
                         public void onMyLocationClick(@NonNull Location location) {

                             mMap.setMinZoomPreference(12);

                             CircleOptions circleOptions = new CircleOptions();
                             circleOptions.center(new LatLng(location.getLatitude(),
                                     location.getLongitude()));

                             circleOptions.radius(200);
                             circleOptions.fillColor(Color.RED);
                             circleOptions.strokeWidth(6);

                             mMap.addCircle(circleOptions);
                         }
                     };






         }

   /** private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

             protected synchronized void buildGoogleApiClient() {
                 mGoogleApiClient = new GoogleApiClient.Builder(this)
                         .addConnectionCallbacks(this)
                         .addOnConnectionFailedListener(this)
                         .addApi(LocationServices.API).build();
                 mGoogleApiClient.connect();
             }

             @Override
             public void onLocationChanged(Location location) {
                 mLastLocation = location;
                 if (mCurrLocationMarker != null) {
                     mCurrLocationMarker.remove();
                 }
                 //Place current location marker
                 LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                 MarkerOptions markerOptions = new MarkerOptions();
                 markerOptions.position(latLng);
                 markerOptions.title("Current Position");
                 markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                 mCurrLocationMarker = mMap.addMarker(markerOptions);

                 //move map camera
                 mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                 mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

                 //stop location updates
                 if (mGoogleApiClient != null) {
                     LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
                 }

             }

             @Override
             public void onStatusChanged(String s, int i, Bundle bundle) {

             }

             @Override
             public void onProviderEnabled(String s) {

             }

             @Override
             public void onProviderDisabled(String s) {

             }

             @Override
             public void onConnected(@Nullable Bundle bundle) {

                 mLocationRequest = new LocationRequest();
                 mLocationRequest.setInterval(1000);
                 mLocationRequest.setFastestInterval(1000);
                 mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                 if (ContextCompat.checkSelfPermission(this,
                         Manifest.permission.ACCESS_FINE_LOCATION)
                         == PackageManager.PERMISSION_GRANTED) {
                     LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
                 }
             }

             @Override
             public void onConnectionSuspended(int i) {

             }

             @Override
             public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

             }
   **/


