package com.bravvura.nestledtime.userstory.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.activity.BaseActivity;
import com.bravvura.nestledtime.userstory.model.UserStoryAddressModel;
import com.bravvura.nestledtime.utils.Constants;
import com.bravvura.nestledtime.utils.PermissionUtils;
import com.bravvura.nestledtime.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Deepak Saini on 19-02-2018.
 */

public class GoogleMapActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, PlaceSelectionListener {


    private static final int GOOGLE_API_CLIENT_ID = 0;
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private UserStoryAddressModel selectedAddress;
    private boolean requestFromUser;
    private Location userLastLocation;
    private PlaceAutocompleteFragment autocompleteFragment;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        setupToolBar();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.BUNDLE_KEY.SELECTED_LOCATION)) {
                selectedAddress = bundle.getParcelable(Constants.BUNDLE_KEY.SELECTED_LOCATION);
            }
        }
        initComponent();

        setTitle("Location");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_done) {
            if (selectedAddress == null) {
                refershAddress(googleMap.getCameraPosition().target);
            }

            if (selectedAddress == null) {
                selectedAddress = new UserStoryAddressModel();
                selectedAddress.placeAddress = "";
                selectedAddress.placeName = "";
                selectedAddress.latLng = googleMap.getCameraPosition().target;
            }

            if (selectedAddress != null) {
                Intent intent = new Intent();
                intent.putExtra(Constants.BUNDLE_KEY.SELECTED_LOCATION, selectedAddress);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initComponent() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
        findViewById(R.id.image_current_location).setOnClickListener(this);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(this);
        autocompleteFragment.setHint("Enter Location...");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        initGoogleMap();
        setUpGoogleMap();
        initializeGoogleServices();
    }


    private void initGoogleMap() {
        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (selectedAddress == null)
                    refershAddress(googleMap.getCameraPosition().target);
            }
        });

        googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                if (i == 1)
                    selectedAddress = null;
            }
        });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                animateCamera(latLng);
                refershAddress(latLng);
            }
        });
    }

    private void refershAddress(LatLng latLng) {
        Address mySelectedAddress = Utils.getValidAddress(getApplicationContext(), latLng.latitude, latLng.longitude);
        if (mySelectedAddress != null) {
            this.selectedAddress = new UserStoryAddressModel();
            this.selectedAddress.latLng = latLng;
            this.selectedAddress.placeName = mySelectedAddress.getAddressLine(0);
            this.selectedAddress.placeAddress = mySelectedAddress.getAddressLine(0);
            refershAddress(mySelectedAddress.getAddressLine(0));
        }
    }

    public void animateCamera(LatLng latLng) {
        if (googleMap != null && latLng != null) {
            float minZoomLevel = 17;
            if (googleMap.getCameraPosition().zoom > minZoomLevel)
                minZoomLevel = googleMap.getCameraPosition().zoom;
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .zoom(minZoomLevel)
                    .target(latLng)
                    .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.REQUEST_CODE.PERMISSION_LOCATION:
                if (PermissionUtils.checkLocationPermission(getApplicationContext())) {
                    initializeGoogleServices();
                }
                break;
            case Constants.REQUEST_CODE.PERMISSION_LOCATION_FOR_USER:
                if (PermissionUtils.checkLocationPermission(getApplicationContext())) {
                    navigateToCurrentLocation();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.REQUEST_CODE.REQUEST_CHECK_LOCATION_SETTINGS:
                if (resultCode == RESULT_OK) {
//                    showGPSSetting();
                    if (requestFromUser || selectedAddress == null)
                        requestLocationUpdates();
                }
                break;
            case Constants.REQUEST_CODE.REQUEST_LOCATION:
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    this.selectedAddress = new UserStoryAddressModel();
                    this.selectedAddress.latLng = place.getLatLng();
                    this.selectedAddress.placeName = place.getName().toString();
                    this.selectedAddress.placeAddress = place.getAddress().toString();
                    animateCamera(place.getLatLng());
                    refershAddress(selectedAddress.placeName);
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                } else if (resultCode == RESULT_CANCELED) {
                }
                break;
        }
    }

    private void refershAddress(String address) {
        autocompleteFragment.setText(address);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.image_current_location) {
            if (userLastLocation != null) {
                animateCamera(new LatLng(userLastLocation.getLatitude(), userLastLocation.getLongitude()));
            } else {
                requestFromUser = true;
                navigateToCurrentLocation();
            }
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            if (PermissionUtils.checkLocationPermission(getApplicationContext())) {
                if (selectedAddress == null)
                    animateCamera(getCurrentLocation());
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception x) {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void setUpGoogleMap() {
        if (PermissionUtils.checkLocationPermission(getApplicationContext()))
            googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        if (selectedAddress != null) {
            animateCamera(selectedAddress.latLng);
            refershAddress(selectedAddress.placeName);
        }
    }

    void navigateToCurrentLocation() {
        if (!PermissionUtils.checkLocationPermission(getApplicationContext())) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.REQUEST_CODE.PERMISSION_LOCATION_FOR_USER);
            return;
        }

        setUpGoogleMap();
        if (showGPSSetting())
            requestLocationUpdates();
    }

    private LatLng getCurrentLocation() {
        if (PermissionUtils.checkLocationPermission(getApplicationContext()) && mGoogleApiClient != null) {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                return new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            }
        }
        return null;
    }

    private void initializeGoogleServices() {
        if (!PermissionUtils.checkLocationPermission(getApplicationContext())) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.REQUEST_CODE.PERMISSION_LOCATION);
        } else {
            setUpGoogleMap();
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addApi(LocationServices.API)
                        .build();
            }
            mGoogleApiClient.connect();
            showGPSSetting();
        }
    }

    private boolean showGPSSetting() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            return true;

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(5 * 1000);//5 sec Time interval for location update
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(GoogleMapActivity.this, Constants.REQUEST_CODE.REQUEST_CHECK_LOCATION_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
        return false;
    }


    LocationRequest mLocationRequest;
    FusedLocationProviderClient mFusedLocationClient;
    LocationCallback locationCallBack;

    public void requestLocationUpdates() {
        removeLocationUpdates();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000); // two minute interval
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (PermissionUtils.checkLocationPermission(getApplicationContext())) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallBack = new LocationCallback() {
                public void onLocationResult(LocationResult var1) {
                    Location lastLocation = var1.getLastLocation();
                    if (lastLocation != null) {
                        userLastLocation = lastLocation;
                        if (requestFromUser || selectedAddress == null)
                            animateCamera(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));
                        requestFromUser = false;
                        removeLocationUpdates();
                    }
                }
            }, Looper.myLooper());
        }
    }

    public void removeLocationUpdates() {
        if (mFusedLocationClient != null && locationCallBack != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallBack);
        }
    }

    @Override
    public void onPlaceSelected(Place place) {
        this.selectedAddress = new UserStoryAddressModel();
        this.selectedAddress.latLng = place.getLatLng();
        this.selectedAddress.placeName = place.getName().toString();
        this.selectedAddress.placeAddress = place.getAddress().toString();
        animateCamera(place.getLatLng());
    }

    @Override
    public void onError(Status status) {

    }
}
