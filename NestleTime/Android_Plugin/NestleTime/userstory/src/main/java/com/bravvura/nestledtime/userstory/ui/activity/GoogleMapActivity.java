package com.bravvura.nestledtime.userstory.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Bundle;
import android.os.PersistableBundle;
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
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Deepak Saini on 19-02-2018.
 */

public class GoogleMapActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks {


    private GoogleMap googleMap;
    private TextView text_address_search;
    private GoogleApiClient mGoogleApiClient;
    private UserStoryAddressModel selectedAddress;

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
        initializeGoogleServices();
        setTitle("Location");
        initComponent();
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
        text_address_search = findViewById(R.id.text_address_search);
        text_address_search.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        initGoogleMap();
        if (!PermissionUtils.checkLocationPermission(getApplicationContext())) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.REQUEST_CODE.PERMISSION_LOCATION);
        } else {
            setUpGoogleMap();

        }
    }

    private void setUpGoogleMap() {
        if (PermissionUtils.checkLocationPermission(getApplicationContext())) {
            googleMap.setMyLocationEnabled(true);
        }
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        if(selectedAddress!=null) {
            animateCamera(selectedAddress.latLng);
            text_address_search.setText(selectedAddress.placeName);
        }
    }

    private void initializeGoogleServices() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
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
            text_address_search.setText(mySelectedAddress.getAddressLine(0));
        }
    }

    public void animateCamera(LatLng latLng) {
        if (googleMap != null) {
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
                setUpGoogleMap();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
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
        text_address_search.setText(address);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.text_address_search) {
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                .build(this);
                startActivityForResult(intent, Constants.REQUEST_CODE.REQUEST_LOCATION);
            } catch (GooglePlayServicesRepairableException e) {
                // TODO: Handle the error.
            } catch (GooglePlayServicesNotAvailableException e) {
                // TODO: Handle the error.
            }


//            Intent intent = new Intent(this, SearchAddressListActivity.class);
//            startActivityForResult(intent, Constants.REQUEST_CODE.REQUEST_LOCATION);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
