package com.sarsquad.sarassist;

import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by CHRIS on 7/18/2015.
 */
public class BlockRow {

    public ArrayList<Block> blocks = new ArrayList<Block>();

    public BlockRow(){

    }

    public void addBlock(Block block){
        blocks.add(block);
    }

    public Block getItem(int position){
        return blocks.get(position);
    }

    public ArrayList<Block> getBlocks(){
        return  blocks;
    }

    public void setUserBlocks(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        for(Block b : blocks){
            try {
                b.setAssignedTo(currentUser);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
