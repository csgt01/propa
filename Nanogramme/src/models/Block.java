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
    * 
    */
   public boolean doOverlapping;

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
   private String colourString;
   /**
    * Char der Farbe.
    */
   private char colourChar;
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

   /**
    * Konstruktor.
    */
   public Block() {
      startIndex = null;
      endIndex = null;
      gone = false;
      doOverlapping = true;
   }

   /**
    * Konstruktor, der aus einem Block ein neues Objekt erzeugt. Benötigt für
    * das Raten der Lösung.
    * 
    * @param block
    */
   public Block(Block block) {
      super();
      this.howMany = new Integer(block.howMany);
      this.colour = block.colour;
      if (block.getStartIndex() != null && block.getEndIndex() != null) {
         this.startIndex = new Integer(block.startIndex);
         this.endIndex = new Integer(block.endIndex);
      }
      this.gone = block.gone;
      this.minStartIndex = new Integer(block.minStartIndex);
      this.maxEndIndex = new Integer(block.maxEndIndex);
      this.minStartIndexNew = new Integer(block.minStartIndexNew);
      this.maxEndIndexNew = new Integer(block.maxEndIndexNew);
      this.colourString = block.colourString;
      this.colourChar = block.colourChar;
      this.entriesSet = new Integer(block.entriesSet);
      this.indeces = new TreeSet<Integer>();
      for (Integer i : block.indeces) {
         this.indeces.add(new Integer(i));
      }
      this.doOverlapping = block.doOverlapping;
   }

   /**
    * @return the colorChar
    */
   public char getColorChar() {
      return colourChar;
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
    * Setzt die Colour, den colourString und den colorChar.
    * 
    * @param colour
    *           the colour to set
    */
   public void setColour(Colour colour) {
      this.colour = colour;
      this.colourString = String.valueOf(colour.getName());
      this.colourChar = colour.getName();
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
    * Gone is set and {@link #startIndex} and {@link #endIndex}.
    * 
    * @param gone
    *           the gone to set
    * @param startIndex
    *           an welcher Stelle beginnt der Block
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
    * @throws Exception
    */
   public void setMinStartIndexNew(Integer minStartIndexNew) throws Exception {
      if (minStartIndexNew > this.maxEndIndexNew) {
         throw new Exception();
      }
      if (minStartIndexNew > this.minStartIndexNew) {
         this.minStartIndexNew = minStartIndexNew;
      }
      if ((this.minStartIndexNew + howMany) == (this.maxEndIndexNew + 1) && !isGone()) {
         increaseEntriesSet(this.minStartIndexNew);
         increaseEntriesSet(this.maxEndIndexNew);
      }
      doOverlapping = true;
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
    * @throws Exception
    */
   public void setMaxEndIndexNew(Integer maxEndIndexNew) throws Exception {
      if (this.minStartIndexNew > maxEndIndexNew) {
         throw new Exception();
      }
      if (maxEndIndexNew < this.maxEndIndexNew) {
         this.maxEndIndexNew = maxEndIndexNew;
      }
      if ((minStartIndexNew + howMany) == (maxEndIndexNew + 1) && !isGone()) {
         increaseEntriesSet(minStartIndexNew);
         increaseEntriesSet(maxEndIndexNew);
      }
      doOverlapping = true;
   }

   /**
    * @return the color
    */
   public String getColourString() {
      return colourString;
   }

   /**
    * @return entriesSet
    */
   public int getEntriesSet() {
      return entriesSet;
   }

   /**
    * Erhöht {@link #entriesSet} und fügt den index {@link #indeces} hinzu.
    * Falls alle Felder gesetzt sind, wird {@link #gone} auf true gesetzt.
    * 
    * @param index Stelle 
    * 
    * @return true if the Block is gone after set.
    */
   public boolean increaseEntriesSet(int index) {
      // // System.out.println("increaseEntriesSet()");
      if (!indeces.add(index)) {
         this.entriesSet++;
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
            + maxEndIndex + ", minStartIndexNew=" + minStartIndexNew + ", maxEndIndexNew=" + maxEndIndexNew + ", color=" + colourString + ", colorChar=" + colourChar + ", entriesSet=" + entriesSet
            + ", indeces=" + indeces + "]\n";
   }

}
