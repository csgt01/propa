package models;

import java.util.ArrayList;

/**
 * Stellt eine Spalte in einem Raetsel dar. Die Klasse enthaelt auch eine Liste
 * von {@link Block}, die in der Spalte vorkommen.
 * 
 * @author csgt
 * 
 */
public class Column {

   /**
    * Der Index der Spalte.
    */
   private int index;

   /**
    * Liste der Bloecke.
    */
   private ArrayList<Block> blocks;

   /**
    * Fertig gesetzt?
    */
   private boolean isGone = false;

   /**
    * Anzahl der gesetzten Felder.
    */
   private int entriesSet = 0;

   /**
    * Maximale Anzahl der gesetzten Felder (Summe von howMany der Bloecke).
    */
   private int maxEntries = 0;

   /**
    * Konstruktor
    */
   public Column() {
      blocks = new ArrayList<Block>();
   }

   /**
    * Konstruktor, der aus einer Column ein neues Objekt erzeugt. Gebraucht fuer
    * das Raten.
    * 
    * @param column
    */
   public Column(Column column) {
      this.blocks = new ArrayList<Block>(column.getBlocks().size());
      for (Block block : column.getBlocks()) {
         this.blocks.add(new Block(block));
      }
      this.entriesSet = column.entriesSet;
      this.isGone = column.isGone;
      this.index = column.getIndex();
      this.maxEntries = column.maxEntries;
   }

   /**
    * Fuegt einen Block hinzu und erhoeht maxEntries um die Blockgroesse.
    * 
    * @param block
    *           Hinzuzufuegende Block.
    */
   public void addBlock(Block block) {
      if (null == blocks) {
         blocks = new ArrayList<Block>();
      }
      blocks.add(block);
      maxEntries += block.getHowMany();
   }

   /**
    * 
    * @return Ist die Column fertig.
    */
   public boolean isGone() {
      return isGone;
   }

   /**
    * Setzt isGone. Ausserdem werden alle Bloecke auch auf gone = true gesetzt
    * wenn isGone == true.
    * 
    * @param isGone
    */
   public void setGone(boolean isGone) {
      this.isGone = isGone;
      if (isGone) {
         for (Block block : blocks) {
            block.setGone(true);
         }
      }
   }

   /**
    * Gibt die Bloecke der Column zurueck.
    * 
    * @return Bloecke
    */
   public ArrayList<Block> getBlocks() {
      return blocks;
   }

   /**
    * Gibt die Anzahl der gesetzten Felder in der Column zurueck.
    * 
    * @return Anzahl gesetzter Felder.
    */
   public int getEntriesSet() {
      return entriesSet;
   }

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
    * Inkrementiert die Anzahl der gesetzten Felder. Ausserdem wird auch beim
    * zugehoerigen Block die entriesSet erhoeht, falls der Block eindeutig ist.
    * 
    * @param row
    *           index
    * @param withBlocks
    *           mit Setzen von indeces bei block
    * 
    * @return true wenn alle Felder gesetzt sind.
    */
   public boolean setEntriesSet(int row, boolean withBlocks) {
      entriesSet++;
      if (withBlocks && blocks != null) {
         if (blocks.size() == 1) {
            blocks.get(0).increaseEntriesSet(row);
         } else if (blocks.size() > 1) {
            ArrayList<Integer> indeces = new ArrayList<Integer>();
            for (Block block : blocks) {
               if (row >= block.getMinIndex() && row <= block.getMaxIndex()) {
                  indeces.add(blocks.indexOf(block));
               }
            }
            if (indeces.size() == 1) {
               blocks.get(indeces.get(0)).increaseEntriesSet(row);
            }
         }
      }
      if (entriesSet == maxEntries) {
         setGone(true);
         return true;
      }
      return false;
   }

   /**
    * @param blocks
    */
   public void setBlocks(ArrayList<Block> blocks) {
      this.blocks = blocks;
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
      return "\nColumn [maxEntries=" + maxEntries + ", , entriesSet=" + entriesSet + ", isGone=" + isGone + ", blocks=" + blocks + "]";

   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((blocks == null) ? 0 : blocks.hashCode());
      result = prime * result + entriesSet;
      result = prime * result + (isGone ? 1231 : 1237);
      result = prime * result + maxEntries;
      return result;
   }

}
