package com.sarsquad.sarassist;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {

    public static Location mLastKnownLocation;
    public static Location newLocation;
    boolean isLocationEnabled = false;
    boolean isOkClicked = false;
    boolean isFirstTime = true;

    private static Location userLocation;
    private static BlockRow blocks;

    private GoogleApiClient mGoogleApiClient;

    private LocationListener locationListener;
    private LocationManager locationManager;
    private LocationRequest mLocationRequest;


    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(Location location, BlockRow blockRow) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        userLocation = location;
        mLastKnownLocation = location;
        blocks = blockRow;
        return fragment;
    }

    public BlankFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationListener = setupLocationListener();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mLocationRequest = createLocationRequest();
        mGoogleApiClient = createGoogleApiLocationClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
            String message = "Please enable location services. Press 'Ok' to go to the location setting page. Pressing 'Cancel' will take you to Today's Workout.";

            alertBuilder.setMessage(message).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    isOkClicked = true;
                    Intent gpsOptionsIntent = new Intent(
                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(gpsOptionsIntent);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    //Navigate to workout screen
                }
            });
            alertBuilder.create().show();
        }

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    private GoogleApiClient createGoogleApiLocationClient(){
        return new GoogleApiClient.Builder(getActivity()).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {

                //MapUtils.focusOnCurrentLocation(googleMap, mLastKnownLocation);

                mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if(mLastKnownLocation != null){
                    //MapUtils.focusOnCurrentLocation(googleMap, mLastKnownLocation);
                } else {
                    mLastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(mLastKnownLocation == null){
                        mLastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                    if (mLastKnownLocation == null){
                        mLastKnownLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                    }
                }


                startLocationUpdates();
                if(mLastKnownLocation != null){

                } else {
                    SARAssist.makeToastShort("Unable to obtain location, please turn on Location Services.");
                }
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {

                    }
                })
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
    }

    private LocationListener setupLocationListener(){
        return new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                if(isFirstTime){
                    isFirstTime = false;
                    MapUtils.focusOnCurrentLocation(googleMap, location);
                    mLastKnownLocation = location;
                    newLocation = location;
                    getPlaces(location);
                    getLocale(location);
                }

            }
        };
    }
    protected LocationRequest createLocationRequest() {
        return new LocationRequest()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, locationListener);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, locationListener);
    }



}
