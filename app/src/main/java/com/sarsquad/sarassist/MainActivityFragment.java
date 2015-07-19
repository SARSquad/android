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
import com.parse.LocationCallback;
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

    public static MainActivityFragment newInstance(){
        MainActivityFragment fragment = new MainActivityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tempLocation = new Location("");
        tempLocation.setLatitude(38.927176);
        tempLocation.setLongitude(-94.723854);


        SARAssist.makeToastLong("Getting search areas...");
        getLocation();
        getLocations();


        searchAreas = new ArrayList<SearchArea>();
        searchAreaAdapter = new SearchAreaAdapter(getActivity(), searchAreas, tempLocation);

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
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, BlockFragment.newInstance(searchAreas.get(position), tempLocation))
                        .addToBackStack("home")
                        .commit();
            }
        });

        return view;
    }

    private void getLocation(){
        ParseGeoPoint.getCurrentLocationInBackground(5000, new LocationCallback() {
            @Override
            public void done(ParseGeoPoint parseGeoPoint, ParseException e) {
                if (parseGeoPoint != null) {
                    tempLocation.setLatitude(parseGeoPoint.getLatitude());
                    tempLocation.setLongitude(parseGeoPoint.getLongitude());

                } else {
                    e.printStackTrace();
                    tempLocation.setLatitude(38.927176);
                    tempLocation.setLongitude(-94.723854);
                }
            }
        });
    }

    private void getLocations(){
        ParseGeoPoint userLocation = new ParseGeoPoint(tempLocation.getLatitude(), tempLocation.getLongitude());
        SearchArea.getQuery()
                .whereWithinMiles(ParseConsts.SearchArea.Location, userLocation, 50.0f)
                .whereEqualTo(ParseConsts.SearchArea.IsComplete, false)
                .setLimit(10)
                .findInBackground(new FindCallback<SearchArea>() {
                    @Override
                    public void done(List<SearchArea> searchAreasList, ParseException e) {
                        if (e == null) {
                            searchAreaAdapter.addAll(searchAreasList);
                            searchAreaAdapter.notifyDataSetChanged();
                        } else {
                            e.printStackTrace();
                            SARAssist.makeToastShort("Could not find local searches.");
                        }
                    }
                });
    }
}
