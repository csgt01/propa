package models;

import java.util.LinkedList;

public class Column {

   private LinkedList<ColourBlock> blocks;

   private boolean isGone = false;

   private int entriesSet = 0;

   private int maxEntries = 0;
   
   public Column (int maxEntries) {
      this.maxEntries = maxEntries;
   }

   public void addBlock(ColourBlock block) {
      if (null == blocks) {
         blocks = new LinkedList<ColourBlock>();
      }
      blocks.add(block);
   }

   public boolean isGone() {
      return isGone;
   }

   public void setGone(boolean isGone) {
      if (blocks != null && isGone) {
         for (ColourBlock block : blocks) {
            block.setGone(true);
         }
      }
      this.isGone = isGone;
   }

   public LinkedList<ColourBlock> getBlocks() {
      return blocks;
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
      for (ColourBlock block : blocks) {
         blockCount += block.getHowMany();
      }
      return blockCount;
   }

   public void setBlocks(LinkedList<ColourBlock> blocks) {
      this.blocks = blocks;
   }

   @Override
   public String toString() {
      return "Column [blocks=" + blocks + ", isGone=" + isGone + ", entriesSet=" + entriesSet + "]";
   }

}
