package models;

import java.util.ArrayList;

/**
 * Stellt eine Reihe in einem Rätsel da
 * 
 * @author csgt
 * 
 */
public class Row {

   private ArrayList<Block> blocks;

   private boolean isGone = false;
   private int entriesSet = 0;
   private int maxEntries = 0;
   private int index;

   /**
    * @return the maxEntries
    */
   public int getMaxEntries() {
      return maxEntries;
   }

   /**
    * @param maxEntries
    *           the maxEntries to set
    */
   public void setMaxEntries(int maxEntries) {
      this.maxEntries = maxEntries;
   }

   /**
    * Konstruktor
    */
   public Row() {
      blocks = new ArrayList<Block>();
   }

   /**
    * Konstruktor
    * 
    * @param row
    */
   public Row(Row row) {
      this.entriesSet = row.entriesSet;
      this.isGone = row.isGone;
      this.maxEntries = row.maxEntries;
      this.blocks = new ArrayList<Block>();
      for (Block block : row.getBlocks()) {
         this.blocks.add(new Block(block));
      }
      this.index = row.getIndex();
   }

   /**
    * Fügt einen Block der Liste hinzu
    * 
    * @param block
    */
   public void addBlock(Block block) {
      if (null == blocks) {
         blocks = new ArrayList<Block>();
      }
      blocks.add(block);
      maxEntries += block.getHowMany();
   }

   /**
    * @return Blöcke der Reihe
    */
   public ArrayList<Block> getBlocks() {
      return blocks;
   }

   /**
    * @param blocks
    */
   public void setBlocks(ArrayList<Block> blocks) {
      this.blocks = blocks;
   }

   /**
    * 
    * @return true wenn Reihe fertig ist.
    */
   public boolean isGone() {
      return isGone;
   }

   /**
    * Sets gone.
    * 
    * @param isGone
    */
   public void setGone(boolean isGone) {
      if (blocks != null && isGone) {
         for (Block block : blocks) {
            block.setGone(true);
         }
      }
      this.isGone = isGone;
   }

   /**
    * 
    * @return Wie viele Farben gesetzt wurden. Leere Felder zählen nicht.
    */
   public int getEntriesSet() {
      return entriesSet;
   }

   /**
    * Inkrementiert die Anzahl der gesetzten Felder.
    * 
    * @param column
    *           Index wo gesetzt wurde.
    * 
    * @return true wenn alle Felder gesetzt sind.
    * @throws Exception
    */
   public boolean setEntriesSet(int column) throws Exception {
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

   protected int getBlockCount() {
      int blockCount = 0;
      for (Block block : blocks) {
         blockCount += block.getHowMany();
      }
      return blockCount;
   }

   /**
    * @return the index
    */
   public int getIndex() {
      return index;
   }

   /**
    * @param index
    *           the index to set
    */
   public void setIndex(int index) {
      this.index = index;
   }

   @Override
   public String toString() {
      return "\nRow [maxEntries=" + maxEntries + ", , entriesSet=" + entriesSet + ", isGone=" + isGone + ", blocks=" + blocks + "]";
   }
}
