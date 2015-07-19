package com.sarsquad.sarassist;

import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;

/**
 * Created by CHRIS on 7/18/2015.
 */
public class MapUtils {

    private MapUtils(){ }

    public static final double DefaultLatitude = 39.1866;
    public static final double DefaultLongitude = -96.581;

    public static final float MarkerZoomLevel = 18;
    public static final float DefaultZoomLevel = 15;
    public static final float LocationZoomLevel = DefaultZoomLevel;

    public static Location defaultIfNull( Location currentLocation )
    {
        if( currentLocation != null )
        {
            return currentLocation;
        }
        else
        {
            Location location = new Location( "" );
            location.setLatitude( MapUtils.DefaultLatitude );
            location.setLongitude( MapUtils.DefaultLongitude );
            return location;
        }
    }

    public static MarkerOptions createCurrentLocationMarker( Location currentLocation )
    {
        currentLocation = defaultIfNull( currentLocation );
        return new MarkerOptions()
                .position( new LatLng( currentLocation.getLatitude(), currentLocation.getLongitude() ) )
                .title( "Current Location" )
                .snippet( "You are currently here!" )
                .icon( BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE) );
    }

    public static void focusOnMarkerLocation(GoogleMap map, Location parkLocation)
    {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target( new LatLng( parkLocation.getLatitude(), parkLocation.getLongitude() ) )
                .zoom( MapUtils.MarkerZoomLevel )
                .build();
        map.animateCamera( CameraUpdateFactory.newCameraPosition( cameraPosition ), 1000, null );
    }

    public static void focusOnMarkerLocation(GoogleMap map, LatLng parkLocation)
    {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target( new LatLng( parkLocation.latitude, parkLocation.longitude ) )
                .zoom( MapUtils.MarkerZoomLevel )
                .build();
        map.animateCamera( CameraUpdateFactory.newCameraPosition( cameraPosition ), 1000, null );
    }

    public static void focusOnCurrentLocation( GoogleMap map, Location currentLocation )
    {
        currentLocation = MapUtils.defaultIfNull( currentLocation );
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target( new LatLng( currentLocation.getLatitude(), currentLocation.getLongitude() ) )
                .zoom( MapUtils.LocationZoomLevel )
                .build();
        map.animateCamera( CameraUpdateFactory.newCameraPosition( cameraPosition ) );
    }

//    public static float setZoomLevel( List<Block> bathrooms, Location userLocation )
//    {
//        float zoomLevel = 15.0f;
//        float maxDistance = 1.0f;
//        float earthEquator = 40008000f;
//        float tempDistance;
//        for( Block bathroom : bathrooms )
//        {
//            tempDistance = bathroom.getDistanceAway( userLocation );
//            if( tempDistance > maxDistance )
//            {
//                maxDistance = tempDistance;
//            }
//        }
//
//        maxDistance *= 2;
//        zoomLevel = Math.round( Math.log( earthEquator / maxDistance ) / Math.log( 2 ) + 1 );
//
//        if( zoomLevel > 18 )
//        {
//            zoomLevel = 18;
//        }
//        if( zoomLevel < 10 )
//        {
//            zoomLevel = 10;
//        }
//
//        return zoomLevel;
//    }

    public static float getDistanceAway( Location location, Location secondLocation )
    {
        final Location loc = secondLocation;
        if( loc == null || location == null )
        {
            return -1.0f;
        }
        else
        {
            return loc.distanceTo( location );
        }
    }

    public static String metersToMiles(float distance){
        float miles = distance * 0.000621371f;
        DecimalFormat format = new DecimalFormat("0.0");
        return format.format(miles).toString();
    }
}
