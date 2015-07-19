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

        blocks = new ArrayList<Block>();
        rows = new ArrayList<BlockRow>();
        blockAdapter = new BlockAdapter(getActivity(), rows, tempLocation);

        Block.getQuery()
                .whereEqualTo(ParseConsts.Block.SearchAreaID, mSearchArea.getSearchAreaID())
                .orderByAscending(ParseConsts.Block.Row)
                .orderByAscending(ParseConsts.Block.Column)
                .findInBackground(new FindCallback<Block>() {
                    @Override
                    public void done(List<Block> blocksReturned, ParseException e) {
                        if(e == null){
                            int rowNumber = blocksReturned.get(0).getRow();
                            BlockRow blockRow = new BlockRow();
                            for(Block block : blocksReturned){
                                if(rowNumber == block.getRow()){
                                    blockRow.addBlock(block);
                                } else {
                                    rows.add(blockRow);
                                    rowNumber = block.getRow();
                                    blockRow = new BlockRow();
                                    blockRow.addBlock(block);
                                }
                            }

                            blockAdapter.addAll(rows);
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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SARAssist.makeToastShort("Clicked");
            }
        });
        return view;
    }


}
