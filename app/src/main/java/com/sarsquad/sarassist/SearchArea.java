package com.sarsquad.sarassist;

import android.location.Location;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by CHRIS on 7/18/2015.
 */
@ParseClassName(ParseConsts.SearchArea._Class)
public class SearchArea extends ParseObject {


    public SearchArea(){}

    public static ParseQuery<SearchArea> getQuery(){
        return ParseQuery.getQuery(SearchArea.class);
    }

    public Location getLocation(){
        Location location = new Location("");
        ParseGeoPoint loc = getParseGeoPoint(ParseConsts.SearchArea.Location);
        location.setLatitude(loc.getLatitude());
        location.setLongitude(loc.getLongitude());
        return location;
    }

    public String getSearchAreaName(){
        return getString(ParseConsts.SearchArea.Name);
    }

    public String getSearchAreaID(){
        return getString(ParseConsts.SearchArea.SearchAreaID);
    }
}
