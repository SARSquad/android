package com.sarsquad.sarassist;

import android.location.Location;

import com.google.android.gms.maps.model.Marker;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by CHRIS on 7/18/2015.
 */
@ParseClassName(ParseConsts.Block._Class)
public class Block extends ParseObject {

    public static Marker marker;

    public static ParseQuery<Block> getQuery(){
        return ParseQuery.getQuery(Block.class);
    }


    public Location getLocation(){
        Location location = new Location("");
        ParseGeoPoint loc = getParseGeoPoint(ParseConsts.Block.Location);
        location.setLatitude(loc.getLatitude());
        location.setLongitude(loc.getLongitude());
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

}
