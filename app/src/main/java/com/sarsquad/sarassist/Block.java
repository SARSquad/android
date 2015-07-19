package com.sarsquad.sarassist;

import android.location.Location;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by CHRIS on 7/18/2015.
 */
@ParseClassName(ParseConsts.Block._Class)
public class Block extends ParseObject {

    public static MarkerOptions markerOptions;

    public static ParseQuery<Block> getQuery(){
        return ParseQuery.getQuery(Block.class);
    }


    public Location getLocation(){
        Location location = new Location("");
        location.setLatitude(this.getLatitude());
        location.setLongitude(this.getLongitude());
        return location;
    }

    public int getRow(){
        return getInt(ParseConsts.Block.Row);
    }

    public int getColumn() {
        return getInt(ParseConsts.Block.Column);
    }

    public String getSearchAreaID(){
        return getString(ParseConsts.Block.SearchAreaID);
    }

    public boolean getIsComplete(){
        return getBoolean(ParseConsts.Block.IsComplete);
    }

    public void setIsComplete(boolean isComplete){
        put(ParseConsts.Block.IsComplete, isComplete);
    }

    public ParseUser getAssignedTo(){
        return getParseUser(ParseConsts.Block.AssignedTo);
    }

    public Date getDate(){
        return getDate(ParseConsts.UpdatedAt);
    }

    public MarkerOptions createMarker(){
        markerOptions = new MarkerOptions()
                .position(new LatLng(this.getLocation().getLatitude(), this.getLocation().getLongitude()))
                .title(Double.toString(this.getLocation().getLatitude()) + ", " + Double.toString(this.getLocation().getLongitude()))
                .snippet("Click to check-off location.");
        if(getIsComplete()){
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker());
        }

        return markerOptions;
    }

    public Double getLatitude(){
        return getDouble(ParseConsts.Block.Latitude);
    }

    public Double getLongitude(){
        return getDouble(ParseConsts.Block.Longitude);
    }

    public void setAssignedTo(ParseUser user) throws ParseException {
        this.put(ParseConsts.Block.AssignedTo, user);
        this.save();
    }

}
