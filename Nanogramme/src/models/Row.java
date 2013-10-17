package models;

import java.util.ArrayList;
import java.util.LinkedList;

public class Row {
    
    private LinkedList<Block> blocks;
    
    private boolean isGone = false;
    private int entriesSet = 0;
    private int maxEntries = 0;
    private  ArrayList<LinkedList<String>> possibilities;
    
    public Row (int maxEntries) {
       this.maxEntries = maxEntries;
       blocks = new LinkedList<>();
       possibilities = new ArrayList<LinkedList<String>>();
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
      if (blocks != null && isGone) {
         for (Block block : blocks) {
            block.setGone(true);
         }
      }
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
   
   
   
   /**
    * @return the possibilities
    */
   public ArrayList<LinkedList<String>> getPossibilities() {
      return possibilities;
   }

   /**
    * @param possibilities the possibilities to set
    */
   public void setPossibilities(ArrayList<LinkedList<String>> possibilities) {
      this.possibilities = possibilities;
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
