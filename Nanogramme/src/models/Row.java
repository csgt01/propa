package models;

import java.util.LinkedList;

public class Row {
    
    private LinkedList<Block> blocks;
    
    private boolean isGone = false;
    private int entriesSet = 0;
    private int maxEntries = 0;
    
    public Row (int maxEntries) {
       this.maxEntries = maxEntries;
       blocks = new LinkedList<>();
    }
    
    public void addBlock(Block block) {
        if (null == blocks) {
            blocks = new LinkedList<Block>();
        }
        blocks.add(block);
    }

    public LinkedList<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(LinkedList<Block> blocks) {
        this.blocks = blocks;
    }

   public boolean isGone() {
      return isGone;
   }

   public void setGone(boolean isGone) {
//      if (blocks != null && isGone) {
//         for (ColourBlock block : blocks) {
//            block.setGone(true);
//         }
//      }
      this.isGone = isGone;
   }

   public int getEntriesSet() {
      return entriesSet;
   }

   public void setEntriesSet() {
      entriesSet++;
      int blockCount = maxEntries;
      if (blocks != null) {
         blockCount = getBlockCount();
      }
      
      if (entriesSet == maxEntries || entriesSet >= blockCount) {
         setGone(true);
      }
   }
   
   protected int getBlockCount() {
      int blockCount = 0;
      for (Block block : blocks) {
         blockCount += block.getHowMany();
      }
      return blockCount;
   }


   @Override
   public String toString() {
//      String string = "Row \n"
//            + "isGone: " + isGone + " entriesSet: " + entriesSet +"\n"
//            + "blocks:\n";
//      for (ColourBlock block : blocks) {
//         string = string.concat(block.getHowMany().toString()).concat(""+block.getColour().getName()).concat(" " + block.isGone());
//      }
//      return string;
      return "Row [blocks=" + blocks.toString() + ", isGone=" + isGone + ", entriesSet=" + entriesSet + "]\n";
   }
    
    

}
