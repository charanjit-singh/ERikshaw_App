package com.example.vartikasharma.splashscreen;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class mainpage_activity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, View.OnClickListener, MainService.Callbacks {
    private static final String LOG_TAG = "My Activity";
    private static final int[] COLORS = new int[]{R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorPrimary, R.color.colorAccent, R.color.primary_dark_material_light};
    private static final LatLngBounds BOUNDS_JAMAICA = new LatLngBounds(new LatLng(-57.965341647205726, 144.9987719580531),
            new LatLng(72.77492067739843, -9.998857788741589));
    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    // private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    final Context context = this;
    protected GoogleMap map;
    protected LatLng start;
    protected LatLng end;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    TextView ridenowbutton;
    TextView requestride;
    RelativeLayout retry;
    SupportMapFragment sMapFragment;
    ImageView send;
    private GoogleMap mMap;
    private TextView tvClickMe;
    private ProgressDialog progressDialog;
    private List<Polyline> polylines;

    //vars
    private Boolean mLocationPermissionsGranted = false;
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
                    circleOptions.fillColor(Color.BLUE);
                    circleOptions.strokeWidth(6);

                    mMap.addCircle(circleOptions);
                }
            };

    MainService mainService;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Toast.makeText(mainpage_activity.this, "onServiceConnected called", Toast.LENGTH_SHORT).show();
            // We've binded to LocalService, cast the IBinder and get LocalService instance
            MainService.LocalBinder binder = (MainService.LocalBinder) service;
            mainService = binder.getServiceInstance(); //Get instance of your service!
            mainService.registerClient(mainpage_activity.this); //Activity register in the service as client for callabcks!
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Toast.makeText(mainpage_activity.this, "onServiceDisconnected called", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sMapFragment = SupportMapFragment.newInstance();

        android.support.v4.app.FragmentManager sfm = getSupportFragmentManager();
        setContentView(R.layout.activity_firstpage);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageView hamMenu = (ImageView) findViewById(R.id.three);
        hamMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout navDrawer = findViewById(R.id.drawer_layout);
                // If navigation drawer is not open yet open it else close it.
                if (!navDrawer.isDrawerOpen(GravityCompat.START))
                    navDrawer.openDrawer(Gravity.START);
                else navDrawer.closeDrawer(Gravity.END);
            }
        });


        sMapFragment.getMapAsync(this);
        sfm.beginTransaction().add(R.id.maps, sMapFragment).commit();
        if (sMapFragment.isAdded())
            sfm.beginTransaction().hide(sMapFragment).commit();

        // Start Service
        Intent serviceIntent;
        serviceIntent = new Intent(mainpage_activity.this, MainService.class);
        Log.d("SERVICE","Starting Service");
        startService(serviceIntent);
        Log.d("SERVICE","Service Started");
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE); //Binding to the service!


        ridenowbutton = (TextView) findViewById(R.id.ridenow);
        ridenowbutton.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ridenow:
                mainService.showToastService("Hello ISHA ");

//                bottomsheetdialog_view bottomsheetdialogview = bottomsheetdialog_view.getInstance(this);
//                bottomsheetdialogview.setTvTitle("Ramesh is The Driver");
//                bottomsheetdialogview.setTvSubTitle("He is 500m away");
//                // bottomsheetdialogview.setIvAvatar("https://avatars3.githubusercontent.com/u/6635954?v=3&u=d18aab686938ecda4b96f29e4e3b776008ced91f&s=400");
//                bottomsheetdialogview.setCanceledOnTouchOutside(false);
//                bottomsheetdialogview.show();
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
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(30.8080, 76.9169))
                .title("E_Driver1")
                .snippet("200 m away from u")
                .flat(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.rikshaw_icon)));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(30.8026, 76.9145))
                .title("E_Driver2")
                .snippet("100 m away from u")
                .flat(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.rikshaw_icon)));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(30.7484, 76.7632))
                .title("E_Driver3")
                .snippet("300 m away from u")
                .flat(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.rikshaw_icon)));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(30.7528, 76.7582))
                .title("E_Driver4")
                .snippet("300 m away from u")
                .flat(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.rikshaw_icon)));

        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        mMap.setOnMyLocationClickListener(onMyLocationClickListener);
        enableMyLocationIfPermitted();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(11);
        PolylineOptions line =
                new PolylineOptions().add(new LatLng(30.7484, 76.7632),
                        new LatLng(location.getLatitude(),
                                location.getLongitude()))
                        .width(8).color(Color.BLUE);

        mMap.addPolyline(line);

        PolylineOptions line_2 =
                new PolylineOptions().add(new LatLng(30.7528, 76.7582),
                        new LatLng(location.getLatitude(),
                                location.getLongitude()))
                        .width(8).color(Color.BLUE);

        mMap.addPolyline(line_2);


    }

    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(mainpage_activity.this);
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
        LatLng redmond = new LatLng(30.756889, 76.767370);
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


    @Override
    public void showToastService(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}

/**
 * private void getLocationPermission(){
 * Log.d(TAG, "getLocationPermission: getting location permissions");
 * String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
 * Manifest.permission.ACCESS_COARSE_LOCATION};
 * <p>
 * if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
 * FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
 * if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
 * COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
 * mLocationPermissionsGranted = true;
 * }else{
 * ActivityCompat.requestPermissions(this,
 * permissions,
 * LOCATION_PERMISSION_REQUEST_CODE);
 * }
 * }else{
 * ActivityCompat.requestPermissions(this,
 * permissions,
 * LOCATION_PERMISSION_REQUEST_CODE);
 * }
 * }
 *
 * @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
 * Log.d(TAG, "onRequestPermissionsResult: called.");
 * mLocationPermissionsGranted = false;
 * <p>
 * switch(requestCode){
 * case LOCATION_PERMISSION_REQUEST_CODE:{
 * if(grantResults.length > 0){
 * for(int i = 0; i < grantResults.length; i++){
 * if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
 * mLocationPermissionsGranted = false;
 * Log.d(TAG, "onRequestPermissionsResult: permission failed");
 * return;
 * }
 * }
 * Log.d(TAG, "onRequestPermissionsResult: permission granted");
 * mLocationPermissionsGranted = true;
 * //initialize our map
 * initMap();
 * }
 * }
 * }
 * }
 * <p>
 * protected synchronized void buildGoogleApiClient() {
 * mGoogleApiClient = new GoogleApiClient.Builder(this)
 * .addConnectionCallbacks(this)
 * .addOnConnectionFailedListener(this)
 * .addApi(LocationServices.API).build();
 * mGoogleApiClient.connect();
 * }
 * @Override public void onLocationChanged(Location location) {
 * mLastLocation = location;
 * if (mCurrLocationMarker != null) {
 * mCurrLocationMarker.remove();
 * }
 * //Place current location marker
 * LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
 * MarkerOptions markerOptions = new MarkerOptions();
 * markerOptions.position(latLng);
 * markerOptions.title("Current Position");
 * markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
 * mCurrLocationMarker = mMap.addMarker(markerOptions);
 * <p>
 * //move map camera
 * mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
 * mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
 * <p>
 * //stop location updates
 * if (mGoogleApiClient != null) {
 * LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
 * }
 * <p>
 * }
 * @Override public void onStatusChanged(String s, int i, Bundle bundle) {
 * <p>
 * }
 * @Override public void onProviderEnabled(String s) {
 * <p>
 * }
 * @Override public void onProviderDisabled(String s) {
 * <p>
 * }
 * @Override public void onConnected(@Nullable Bundle bundle) {
 * <p>
 * mLocationRequest = new LocationRequest();
 * mLocationRequest.setInterval(1000);
 * mLocationRequest.setFastestInterval(1000);
 * mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
 * if (ContextCompat.checkSelfPermission(this,
 * Manifest.permission.ACCESS_FINE_LOCATION)
 * == PackageManager.PERMISSION_GRANTED) {
 * LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
 * }
 * }
 * @Override public void onConnectionSuspended(int i) {
 * <p>
 * }
 * @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
 * <p>
 * }
 **/


