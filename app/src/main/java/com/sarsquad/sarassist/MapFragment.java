package com.sarsquad.sarassist;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {

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

    private MapView mapView;
    private GoogleMap googleMap;

    HashMap<Marker, Block> hashMapMarker = new HashMap<>();

    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(Location location, BlockRow blockRow) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        userLocation = location;
        mLastKnownLocation = location;
        blocks = blockRow;
        return fragment;
    }

    public MapFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        initializeMap(rootView,savedInstanceState);

        MapUtils.focusOnCurrentLocation(googleMap, userLocation);

        addMarkers();


        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        //stopLocationUpdates();
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
                    MapUtils.focusOnCurrentLocation(googleMap, mLastKnownLocation);

                } else {
                    SARAssist.makeToastShort("Unable to obtain location, please turn on Location Services.");
                }
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        }).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
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
                }

                MapUtils.focusOnCurrentLocation(googleMap, location);

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

    private void initializeMap( View rootViewT, Bundle savedInstanceStateT )
    {
        mapView = (MapView) rootViewT.findViewById( R.id.fragment_map );
        mapView.onCreate(savedInstanceStateT);
        mapView.onResume();

        try
        {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        googleMap = mapView.getMap();
        googleMap.setMyLocationEnabled(true);

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                MapUtils.focusOnMarkerLocation(googleMap, marker.getPosition());
                return false;
            }

        });

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                String message = "Check off this location?";

                alertBuilder.setMessage(message).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //save as assigned and set user
                        setMarkerChecked(marker);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertBuilder.create().show();


            }
        });



        //googleMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter(getActivity()));
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //mVibrator.vibrate(200);

                final Location location = new Location("");
                location.setLatitude(latLng.latitude);
                location.setLongitude(latLng.longitude);

            }
        });
    }

    private void setMarkerChecked(Marker marker){
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        Block tempBlock = hashMapMarker.get(marker);
        tempBlock.setIsComplete(true);
        try {
            tempBlock.save();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void addMarkers(){
        for(Block block : blocks.getBlocks()){
            Marker marker = googleMap.addMarker(block.createMarker());
            hashMapMarker.put(marker, block);
        }
    }



}
