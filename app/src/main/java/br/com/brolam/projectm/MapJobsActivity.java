package br.com.brolam.projectm;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.Manifest;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Map;

import br.com.brolam.projectm.data.DataBaseProvider;
import br.com.brolam.projectm.data.models.GeoLocation;
import br.com.brolam.projectm.data.models.Job;


public class MapJobsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMapClickListener, LocationListener, ChildEventListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private LocationManager locationManager;
    private Location currentBestLocation;

    private FirebaseAuth firebaseAuth;
    private DataBaseProvider dataBaseProvider = null;
    ArrayMap<String, Map> jobs = new ArrayMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_jobs);
        this.firebaseAuth = FirebaseAuth.getInstance();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        starDataBaseProvider();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    public void starDataBaseProvider() {
        FirebaseUser firebaseUser = this.firebaseAuth.getCurrentUser();
        if ( firebaseUser != null) {
            this.dataBaseProvider = new DataBaseProvider(firebaseUser);
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

        /*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMapClickListener(this);
        //mMap.setOnMyLocationClickListener(this);
        enableMyLocation();
    }

    public static void show(Context context){
        Intent intent = new Intent(context, MapJobsActivity.class);
        context.startActivity(intent);
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            locationManager.requestSingleUpdate(criteria, this, null);
            this.dataBaseProvider.addQueryJobsListener(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        if ( this.currentBestLocation != null){
            goToCurrentLocation(this.currentBestLocation);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.i("Map Click", latLng.toString());
    }


    @Override
    public void onLocationChanged(Location location) {
        if (isBetterLocation(location, this.currentBestLocation)){
            goToCurrentLocation(location);
        }
        Log.i("Map L. Changed", location.toString());
    }

    protected void goToCurrentLocation(Location location) {
        this.currentBestLocation = location;
        LatLng currentLatLng = new LatLng(this.currentBestLocation.getLatitude(), this.currentBestLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
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

    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    private void addMarkerOptions(String jobKey, Map job) {
        LatLng jobLatLng = new LatLng(GeoLocation.getLatitude(job), GeoLocation.getLongitude(job));
        Marker jobMarker = this.mMap.addMarker(new MarkerOptions()
                .position(jobLatLng)
                .title((String) job.get(Job.TITLE))
        );
        jobMarker.setTag(jobKey);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String key) {
        boolean jobNotAdded = !jobs.containsKey(key);
        Map job = (Map) dataSnapshot.getValue();
        jobs.put(key,job );
        if ( jobNotAdded ) addMarkerOptions(key, job);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String key) {
        jobs.put(key, (Map) dataSnapshot.getValue());
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        jobs.remove(dataSnapshot.getKey());
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
