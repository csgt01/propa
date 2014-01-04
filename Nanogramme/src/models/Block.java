package models;

import java.util.TreeSet;

/**
 * Farbblock in einer Reihe oder Spalte. Die Klasse Block spielt eine grosse
 * Rolle im Loesungsprozess. Im Laufe des Prozesses werden minIndex und maxIndex
 * immer wieder veraendert, bis der Block eindeutig gesetzt werden kann.
 * 
 * @author cschulte
 * 
 */
public class Block {

   /**
    * Flag fuer Methoden beim Loesen.Wird auf true gesetzt, wenn minIndex oder
    * maxIndex geaendert werden.
    */
   public boolean doOverlapping;

   /**
    * Groesse des Blocks.
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
    * Index wird im Laufe des Loesens angepasst.
    */
   private Integer minIndex;
   /**
    * Index wird im Laufe des Loesens angepasst.
    */
   private Integer maxIndex;
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
    * Konstruktor, der aus einem Block ein neues Objekt erzeugt. Benoetigt fuer
    * das Raten der Loesung.
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
      this.minIndex = new Integer(block.minIndex);
      this.maxIndex = new Integer(block.maxIndex);
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
      this.minIndex = startIndex;
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
      this.maxIndex = endIndex;
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
    * @return the minIndex
    */
   public Integer getMinIndex() {
      return minIndex;
   }

   /**
    * Falls minIndex > this.minIndex wird this. maEndIndex gesetzt. Eswird auch
    * geprueft, ob (minIndex + howMany) == (maxIndex + 1) und gone == false ist.
    * Dann ist der Block bereits richtig platziert. Deshalb werden minIndex und
    * maxIndex zu indeces hinzugefuegt. Eine Methode im Nonosolver sorgt dafuer,
    * dass die Felder dazwischen gesetzt werden.
    * 
    * @param minIndex
    *           the minIndex to set
    */
   public void setMinIndex(Integer minIndex) {
      if (this.minIndex == null) {
         this.minIndex = minIndex;
         return;
      }
      if (minIndex > this.maxIndex) {
         return;
      }
      if (minIndex > this.minIndex) {
         this.minIndex = minIndex;
      }
      if ((this.minIndex + howMany) == (this.maxIndex + 1) && !isGone()) {
         increaseEntriesSet(this.minIndex);
         increaseEntriesSet(this.maxIndex);
      }
      doOverlapping = true;
   }

   /**
    * @return the maxIndex
    */
   public Integer getMaxIndex() {
      return maxIndex;
   }

   /**
    * Falls maxIndex < this.maxIndex wird this. maEndIndex gesetzt. Eswird auch
    * geprueft, ob (minIndex + howMany) == (maxIndex + 1) und gone == false ist.
    * Dann ist der Block bereits richtig platziert. Deshalb werden minIndex und
    * maxIndex zu indeces hinzugefuegt. Eine Methode im Nonosolver sorgt dafuer,
    * dass die Felder dazwischen gesetzt werden.
    * 
    * @param maxIndex
    *           the maxIndex to set
    */
   public void setMaxIndex(Integer maxIndex) {
      if (this.maxIndex == null) {
         this.maxIndex = maxIndex;
         return;
      }
      if (this.minIndex > maxIndex) {
         return;
      }
      if (maxIndex < this.maxIndex) {
         this.maxIndex = maxIndex;
      }
      if ((minIndex + howMany) == (maxIndex + 1) && !isGone()) {
         increaseEntriesSet(minIndex);
         increaseEntriesSet(maxIndex);
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
    * Erhoeht {@link #entriesSet} und fuegt den index {@link #indeces} hinzu.
    * Falls alle Felder gesetzt sind, wird {@link #gone} auf true gesetzt.
    * 
    * @param index
    *           Stelle
    * 
    * @return true if the Block is gone after set.
    */
   public boolean increaseEntriesSet(int index) {
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
      return "Block [howMany=" + howMany + ", colour=" + colour + ", startIndex=" + startIndex + ", endIndex=" + endIndex + ", gone=" + gone + ", minIndex=" + minIndex + ", maxIndex=" + maxIndex
            + ", color=" + colourString + ", colorChar=" + colourChar + ", entriesSet=" + entriesSet + ", indeces=" + indeces + "]\n";
   }

}
