package com.sarsquad.sarassist;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by CHRIS on 7/18/2015.
 */
public class BlockAdapter extends ArrayAdapter<BlockRow> {

    private static Location userLocation;

    public BlockAdapter(Context context, List<BlockRow> blockRows, Location location){
        super(context, R.layout.block_list_row, blockRows);
        userLocation = location;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Block block = getItem(position).getItem(0);

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.block_list_row, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvAssigne = (TextView) convertView.findViewById(R.id.tvAssigne);
            viewHolder.tvDistance = (TextView) convertView.findViewById(R.id.tvDistanceBlock);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.block_list_row, parent, false);

        viewHolder = new ViewHolder();
        viewHolder.tvAssigne = (TextView) convertView.findViewById(R.id.tvAssigne);
        viewHolder.tvDistance = (TextView) convertView.findViewById(R.id.tvDistanceBlock);
        convertView.setTag(viewHolder);

        ParseUser assignedTo = block.getAssignedTo();

        if(assignedTo != null) {

            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo(ParseConsts.ObjectID, block.getAssignedTo().getObjectId());
            try {
                List<ParseUser> tempUser = query.find();
                setName(tempUser.get(0), viewHolder);
            } catch (Exception e){
                setName(ParseUser.getCurrentUser(), viewHolder);
            }

            viewHolder.tvDistance.setText(block.getUpdatedAt().toString());
        } else {
            viewHolder.tvAssigne.setText("Not Assigned");
            viewHolder.tvDistance.setText(MapUtils.metersToMiles(MapUtils.getDistanceAway(userLocation, block.getLocation())) + " miles away");
        }

        return convertView;


    }

    private void setName(ParseUser tempUser, ViewHolder viewHolder){
        viewHolder.tvAssigne.setText(tempUser.getUsername());
    }
    private static class ViewHolder {
        TextView tvAssigne;
        TextView tvDistance;

    }
}
