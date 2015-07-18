package com.sarsquad.sarassist;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by CHRIS on 7/18/2015.
 */
public class SearchAreaAdapter extends ArrayAdapter<SearchArea> {

    private static Location userLocation;

    public SearchAreaAdapter(Context context, List<SearchArea> searchAreas, Location location){
        super(context, R.layout.search_list_row, searchAreas);
        userLocation = location;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        SearchArea searchArea = getItem(position);

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.search_list_row, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvSearchAreaName = (TextView) convertView.findViewById(R.id.tvSearchAreaName);
            viewHolder.tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.search_list_row, parent, false);

        viewHolder = new ViewHolder();
        viewHolder.tvSearchAreaName = (TextView) convertView.findViewById(R.id.tvSearchAreaName);
        viewHolder.tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);
        convertView.setTag(viewHolder);

        viewHolder.tvSearchAreaName.setText(searchArea.getSearchAreaName());
        viewHolder.tvDistance.setText(MapUtils.metersToMiles(MapUtils.getDistanceAway(userLocation, searchArea.getLocation())) + " miles away");

        return convertView;


    }

    private static class ViewHolder {
        TextView tvSearchAreaName;
        TextView tvDistance;

    }
}
