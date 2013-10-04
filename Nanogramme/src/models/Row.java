package models;

import java.util.LinkedList;

public class Row {
    
    private LinkedList<ColourBlock> blocks;
    
    public void addBlock(ColourBlock block) {
        if (null == blocks) {
            blocks = new LinkedList<ColourBlock>();
        }
        blocks.add(block);
    }

    @Override
    public String toString() {
        return "Row [blocks=" + blocks + "]";
    }

    public LinkedList<ColourBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(LinkedList<ColourBlock> blocks) {
        this.blocks = blocks;
    }
    
    

}
