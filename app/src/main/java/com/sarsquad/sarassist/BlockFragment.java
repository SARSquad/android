package com.sarsquad.sarassist;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlockFragment extends Fragment {

    private ArrayList<Block> blocks;

    private ArrayList<BlockRow> rows;

    private AbsListView mListView;

    private BlockAdapter blockAdapter;

    private static Location tempLocation;

    private LocationManager locationManager;


    private static SearchArea mSearchArea;

    public static BlockFragment newInstance(SearchArea searchArea, Location location) {
        BlockFragment fragment = new BlockFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        mSearchArea = searchArea;
        tempLocation = location;
        return fragment;
    }

    public BlockFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        blocks = new ArrayList<Block>();
        rows = new ArrayList<BlockRow>();
        blockAdapter = new BlockAdapter(getActivity(), rows, tempLocation);

        Block.getQuery()
                .whereEqualTo(ParseConsts.Block.SearchAreaID, mSearchArea.getSearchAreaID())
                .orderByAscending(ParseConsts.Block.Row)
                .findInBackground(new FindCallback<Block>() {
                    @Override
                    public void done(List<Block> blocksReturned, ParseException e) {
                        if (e == null) {
                            int rowNumber = blocksReturned.get(0).getRow();
                            BlockRow blockRow = new BlockRow();
                            for (Block block : blocksReturned) {
                                if (rowNumber == block.getRow()) {
                                    blockRow.addBlock(block);
                                } else {
                                    rows.add(blockRow);
                                    rowNumber = block.getRow();
                                    blockRow = new BlockRow();
                                    blockRow.addBlock(block);
                                }

                            }
                            rows.add(blockRow);


                            blockAdapter.notifyDataSetChanged();
                        } else {
                            e.printStackTrace();
                            SARAssist.makeToastShort("No Blocks were found.");
                        }
                    }
                });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_block_fagment, container, false);

        mListView = (AbsListView) view.findViewById(R.id.listBlock);
        ((AdapterView<ListAdapter>) mListView).setAdapter(blockAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TextView textView = (TextView) view.findViewById(R.id.tvAssigne);
                String text = textView.getText().toString();
                if ( text.equals("Not Assigned") || text.equals(ParseUser.getCurrentUser().getUsername())){
                    checkNavigate(position);
                }else{
                    SARAssist.makeToastShort("Already assigned.");
                }
            }
        });
        return view;
    }



    private void checkNavigate(final int position){

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
            String message = "Please enable location services. Press 'Ok' to go to the location setting page. Then select a row.";

            alertBuilder.setMessage(message).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
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
        } else {

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
            String message = "You will be assigned this search area, press 'Ok' to confirm or 'Cancel' to cancel.";

            alertBuilder.setMessage(message).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //save as assigned and set user
                    rows.get(position).setUserBlocks();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, MapFragment.newInstance(tempLocation, rows.get(position)))
                                    .addToBackStack("home")
                                    .commit();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertBuilder.create().show();

        }
    }


}
