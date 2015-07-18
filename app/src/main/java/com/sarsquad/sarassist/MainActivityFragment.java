package com.sarsquad.sarassist;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ArrayList<SearchArea> searchAreas;

    private AbsListView mListView;

    private SearchAreaAdapter searchAreaAdapter;

    Location tempLocation;



    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tempLocation = new Location("");

        tempLocation.setLatitude(38.927176);
        tempLocation.setLongitude(-94.723854);
        //@TODO: get User Location

        searchAreas = new ArrayList<SearchArea>();
        searchAreaAdapter = new SearchAreaAdapter(getActivity(), searchAreas, tempLocation);

        ParseGeoPoint userLocation = new ParseGeoPoint(tempLocation.getLatitude(), tempLocation.getLongitude());
        SearchArea.getQuery()
                .whereWithinMiles(ParseConsts.SearchArea.Location, userLocation, 15.0f)
                .setLimit(10)
                .findInBackground(new FindCallback<SearchArea>() {
            @Override
            public void done(List<SearchArea> searchAreasList, ParseException e) {
                if(e == null){
                    searchAreaAdapter.addAll(searchAreasList);
                    searchAreaAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                    SARAssist.makeToastShort("Could not find local searches.");
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mListView = (AbsListView) view.findViewById(R.id.listSearchArea);
        ((AdapterView<ListAdapter>) mListView).setAdapter(searchAreaAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Navigate to next page

            }
        });

        return view;
    }
}
