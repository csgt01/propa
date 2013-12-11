package models;

import java.util.TreeSet;

/**
 * Farbblock in einer Reihe oder Spalte.
 * 
 * @author cschulte
 * 
 */
public class Block {

   /**
    * Größe des Blocks.
    */
   private Integer howMany;

   /**
    * Das Colourobjekt des Blocks.
    */
   private Colour colour;

   /**
    * Wird gesetzt, wenn Block fertig gesetzt ist, also gone == true ist.
    */
   private Integer startIndex;
   /**
    * Wird gesetzt, wenn Block fertig gesetzt ist, also gone == true ist.
    */
   private Integer endIndex;
   /**
    * True wenn Block komplett gesetzt.
    */
   private boolean gone;
   /**
    * Wird nur noch am Anfang des Lösungsprozesses gebraucht.
    */
   private Integer minStartIndex;
   /**
    * Wird nur noch am Anfang des Lösungsprozesses gebraucht.
    */
   private Integer maxEndIndex;
   /**
    * Index wird im LAufe des Lösens angepasst.
    */
   private Integer minStartIndexNew;
   /**
    * Index wird im LAufe des Lösens angepasst.
    */
   private Integer maxEndIndexNew;
   /**
    * String der Farbe.
    */
   private String colorString;
   /**
    * Char der Farbe.
    */
   private char colorChar;
   /**
    * Anzahl der gesetzten Felder innerhalb des Blocks.
    */
   private int entriesSet = 0;
   /**
    * Alle gesetzten Felder des Blocks
    */
   private TreeSet<Integer> indeces = new TreeSet<Integer>();

   /**
    * @return the indeces
    */
   public TreeSet<Integer> getIndeces() {
      return indeces;
   }

   /**
    * @param indeces
    *           the indeces to set
    */
   public void setIndeces(TreeSet<Integer> indeces) {
      this.indeces = indeces;
   }

   public Block() {
      startIndex = null;
      endIndex = null;
      gone = false;
   }

   public Block(Block block) {
      super();
      this.howMany = block.howMany;
      this.colour = block.colour;
      this.startIndex = block.startIndex;
      this.endIndex = block.endIndex;
      this.gone = block.gone;
      this.minStartIndex = block.minStartIndex;
      this.maxEndIndex = block.maxEndIndex;
      this.minStartIndexNew = block.minStartIndexNew;
      this.maxEndIndexNew = block.maxEndIndexNew;
      this.colorString = block.colorString;
      this.colorChar = block.colorChar;
      this.entriesSet = block.entriesSet;
      this.indeces = block.indeces;
   }

   /**
    * @return the colorChar
    */
   public char getColorChar() {
      return colorChar;
   }

   /**
    * @return the howMany
    */
   public Integer getHowMany() {
      return howMany;
   }

   /**
    * @param howMany
    *           the howMany to set
    */
   public void setHowMany(Integer howMany) {
      this.howMany = howMany;
   }

   /**
    * @return the colour
    */
   public Colour getColour() {
      return colour;
   }

   /**
    * @param colour
    *           the colour to set
    */
   public void setColour(Colour colour) {
      this.colour = colour;
      this.colorString = String.valueOf(colour.getName());
      this.colorChar = colour.getName();
   }

   /**
    * @return the startIndex
    */
   public Integer getStartIndex() {
      return startIndex;
   }

   /**
    * @param startIndex
    *           the startIndex to set
    */
   public void setStartIndex(Integer startIndex) {
      this.startIndex = startIndex;
      this.minStartIndexNew = startIndex;
   }

   /**
    * @return the endIndex
    */
   public Integer getEndIndex() {
      return endIndex;
   }

   /**
    * @param endIndex
    *           the endIndex to set
    */
   public void setEndIndex(Integer endIndex) {
      this.endIndex = endIndex;
      this.maxEndIndexNew = endIndex;
   }

   /**
    * @return the gone
    */
   public boolean isGone() {
      return gone;
   }

   /**
    * Gone is set and startIndex and endIndex.
    * 
    * @param gone
    *           the gone to set
    */
   public void setGone(boolean gone, int startIndex) {
      this.gone = gone;
      setStartIndex(startIndex);
      int endIndex2 = startIndex + howMany - 1;
      setEndIndex(endIndex2);
   }

   /**
    * Gone is set.
    * 
    * @param gone
    *           the gone to set
    */
   public void setGone(boolean gone) {
      this.gone = gone;
   }

   /**
    * @return the minStartIndex
    */
   public Integer getMinStartIndex() {
      return minStartIndex;
   }

   /**
    * @param minStartIndex
    *           the minStartIndex to set
    */
   public void setMinStartIndex(Integer minStartIndex) {
      if (null != this.minStartIndex) {
         if (this.minStartIndex < minStartIndex) {
            this.minStartIndex = minStartIndex;
         }
      } else {
         this.minStartIndex = minStartIndex;
      }
      this.minStartIndexNew = this.minStartIndex;
   }

   /**
    * @return the maxEndIndex
    */
   public Integer getMaxEndIndex() {
      return maxEndIndex;
   }

   /**
    * @param maxEndIndex
    *           the maxEndIndex to set
    */
   public void setMaxEndIndex(Integer maxEndIndex) {
      if (null != this.maxEndIndex) {
         if (this.maxEndIndex > maxEndIndex) {
            this.maxEndIndex = maxEndIndex;
         }
      } else {
         this.maxEndIndex = maxEndIndex;
      }
      this.maxEndIndexNew = this.maxEndIndex;
   }

   /**
    * @return the minStartIndexNew
    */
   public Integer getMinStartIndexNew() {
      return minStartIndexNew;
   }

   /**
    * Falls minStartIndexNew > this.minStartIndexNew wird this. maEndIndex
    * gesetzt. Eswird auch geprüft, ob (minStartIndexNew + howMany) ==
    * (maxEndIndexNew + 1) und gone == false ist. Dann ist der Block bereits
    * richtig platziert. Deshalb werden minStartIndexNew und maxEndIndexNew zu
    * indeces hinzugefügt. Eine Methode im Nonosolver sorgt dafür, dass die
    * Felder dazwischen gesetzt werden.
    * 
    * @param minStartIndexNew
    *           the minStartIndexNew to set
    */
   public void setMinStartIndexNew(Integer minStartIndexNew) {
      if (minStartIndexNew > this.minStartIndexNew) {
         this.minStartIndexNew = minStartIndexNew;
      }
      if ((minStartIndexNew + howMany) == (maxEndIndexNew + 1) && !isGone()) {
         indeces.add(minStartIndexNew);
         indeces.add(maxEndIndexNew);
      }
   }

   /**
    * @return the maxEndIndexNew
    */
   public Integer getMaxEndIndexNew() {
      return maxEndIndexNew;
   }

   /**
    * Falls maxEndIndexNew < this.maxEndIndexNew wird this. maEndIndex gesetzt.
    * Eswird auch geprüft, ob (minStartIndexNew + howMany) == (maxEndIndexNew +
    * 1) und gone == false ist. Dann ist der Block bereits richtig platziert.
    * Deshalb werden minStartIndexNew und maxEndIndexNew zu indeces hinzugefügt.
    * Eine Methode im Nonosolver sorgt dafür, dass die Felder dazwischen gesetzt
    * werden.
    * 
    * @param maxEndIndexNew
    *           the maxEndIndexNew to set
    */
   public void setMaxEndIndexNew(Integer maxEndIndexNew) {
      if (maxEndIndexNew < this.maxEndIndexNew) {
         this.maxEndIndexNew = maxEndIndexNew;
      }
      if ((minStartIndexNew + howMany) == (maxEndIndexNew + 1) && !isGone()) {
         indeces.add(minStartIndexNew);
         indeces.add(maxEndIndexNew);
      }
   }

   /**
    * @return the color
    */
   public String getColourString() {
      return colorString;
   }

   /**
    * @return entriesSet
    */
   public int getEntriesSet() {
      return entriesSet;
   }

   /**
    * Increase entriesSet.
    * 
    * @return true if the Block is gone after set.
    * @throws Exception 
    */
   public boolean increaseEntriesSet(int index) throws Exception {
      // System.out.println("increaseEntriesSet()");

      if (!indeces.add(index)) {
         this.entriesSet++;
         if (entriesSet > howMany) {
            throw new Exception();
         }
      }
      if (howMany == entriesSet) {
         setGone(true, indeces.first());
         return true;
      }
      return false;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString() {
      return "Block [howMany=" + howMany + ", colour=" + colour + ", startIndex=" + startIndex + ", endIndex=" + endIndex + ", gone=" + gone + ", minStartIndex=" + minStartIndex + ", maxEndIndex="
            + maxEndIndex + ", minStartIndexNew=" + minStartIndexNew + ", maxEndIndexNew=" + maxEndIndexNew + ", color=" + colorString + ", colorChar=" + colorChar + ", entriesSet=" + entriesSet
            + ", indeces=" + indeces + "]\n";
   }

   // public void setEntriesSet(int entriesSet) {
   // this.entriesSet = entriesSet;
   // }

}
