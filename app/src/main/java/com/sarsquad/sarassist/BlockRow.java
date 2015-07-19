package com.sarsquad.sarassist;

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

}
