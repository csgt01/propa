package models;

import java.util.ArrayList;
import java.util.LinkedList;

public class Row {
    
    private LinkedList<Block> blocks;
    
    private boolean isGone = false;
    private int entriesSet = 0;
    private int maxEntries = 0;
    /**
	 * @return the maxEntries
	 */
	public int getMaxEntries() {
		return maxEntries;
	}

	/**
	 * @param maxEntries the maxEntries to set
	 */
	public void setMaxEntries(int maxEntries) {
		this.maxEntries = maxEntries;
	}


	private  ArrayList<LinkedList<String>> possibilities;
    
    public Row () {
       blocks = new LinkedList<>();
       possibilities = new ArrayList<LinkedList<String>>();
    }
    
    public void addBlock(Block block) {
        if (null == blocks) {
            blocks = new LinkedList<Block>();
        }
        blocks.add(block);
        maxEntries += block.getHowMany();
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

   /**
	 * Inkrementiert die Anzahl der gesetzten Felder.
	 * 
	 * @return true wenn alle Felder gesetzt sind.
	 */
   public boolean setEntriesSet(int column) {
      entriesSet++;
      if (entriesSet == maxEntries) {
         setGone(true);
         return true;
      }
      ArrayList<Integer> indeces = new ArrayList<Integer>();
      if (blocks != null && blocks.size() > 0) {
    	  for (Block block : blocks) {
    		  if (column >= block.getMinStartIndexNew() && column <= block.getMaxEndIndexNew()) {
    			  indeces.add(blocks.indexOf(block));
    		  }
    	  }
    	  if (indeces.size() == 1) {
    		  blocks.get(indeces.get(0)).increaseEntriesSet(column);
    	  }
      }
     
      return false;
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
	      return "\nRow [maxEntries=" + maxEntries + ", , entriesSet=" + entriesSet + ", isGone=" + isGone + ", blocks=" + blocks + "]";
   }
    
    

}
