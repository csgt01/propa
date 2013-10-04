package models;

import java.util.LinkedList;

public class Column {
    
    private LinkedList<ColourBlock> blocks;
    
    public void addBlock(ColourBlock block) {
        if (null == blocks) {
            blocks = new LinkedList<ColourBlock>();
        }
        blocks.add(block);
    }
    
    

    public LinkedList<ColourBlock> getBlocks() {
        return blocks;
    }



    public void setBlocks(LinkedList<ColourBlock> blocks) {
        this.blocks = blocks;
    }



    @Override
    public String toString() {
        return "Column [blocks=" + blocks + "]";
    }

    
    
}
