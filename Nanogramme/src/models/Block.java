package models;

public class Block {
    
    private Integer howMany;
    
    private Colour colour;
    private Integer startIndex;
    private Integer endIndex;
    private boolean gone;
    private Integer minStartIndex;
    private Integer maxEndIndex;
    
    private int start;
    private int end;
    
    public Block() {
       startIndex = null;
       endIndex = null;
       gone = false;
    }

    
    
    /**
    * @return the start
    */
   public int getStart() {
      return start;
   }



   /**
    * @param start the start to set
    */
   public void setStart(int start) {
      this.start = start;
      this.end = (start + getHowMany() -1);
   }



   /**
    * @return the end
    */
   public int getEnd() {
      return end;
   }



   /**
    * @param end the end to set
    */
   public void setEnd(int end) {
      this.end = end;
   }



   /**
    * @return the howMany
    */
   public Integer getHowMany() {
      return howMany;
   }

   /**
    * @param howMany the howMany to set
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
    * @param colour the colour to set
    */
   public void setColour(Colour colour) {
      this.colour = colour;
   }



   /**
    * @return the startIndex
    */
   public Integer getStartIndex() {
      return startIndex;
   }



   /**
    * @param startIndex the startIndex to set
    */
   public void setStartIndex(Integer startIndex) {
      this.startIndex = startIndex;
      this.minStartIndex = startIndex;
   }



   /**
    * @return the endIndex
    */
   public Integer getEndIndex() {
      return endIndex;
   }



   /**
    * @param endIndex the endIndex to set
    */
   public void setEndIndex(Integer endIndex) {
      this.endIndex = endIndex;
      this.maxEndIndex = endIndex;
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
    * @param gone the gone to set
    */
   public void setGone(boolean gone, int startIndex) {
      this.gone = gone;
      setStartIndex(startIndex);
      setEndIndex(startIndex + howMany - 1);
   }
   
   /**
    * Gone is set and startIndex and endIndex.
    * 
    * @param gone the gone to set
    */
   public void setGone(boolean gone) {
      this.gone = gone;
      //TODO why nullpointer
//      setStartIndex(startIndex);
//      setEndIndex(startIndex + howMany - 1);
   }



   /**
    * @return the minStartIndex
    */
   public Integer getMinStartIndex() {
      return minStartIndex;
   }



   /**
    * @param minStartIndex the minStartIndex to set
    */
   public void setMinStartIndex(Integer minStartIndex) {
      if (null != this.minStartIndex) {
         if (this.minStartIndex < minStartIndex) {
            this.minStartIndex = minStartIndex;
         }
      } else {
         this.minStartIndex = minStartIndex;
      }
      setStart(minStartIndex);
   }



   /**
    * @return the maxEndIndex
    */
   public Integer getMaxEndIndex() {
      return maxEndIndex;
   }



   /**
    * @param maxEndIndex the maxEndIndex to set
    */
   public void setMaxEndIndex(Integer maxEndIndex) {
      if (null != this.maxEndIndex) {
         if (this.maxEndIndex > maxEndIndex) {
            this.maxEndIndex = maxEndIndex;
         }
      } else {
         this.maxEndIndex = maxEndIndex;
      }
   }



   /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString() {
      return "Block [howMany=" + howMany + ", colour=" + colour + ", startIndex=" + startIndex + ", endIndex=" + endIndex + ", gone=" + gone + ", minStartIndex=" + minStartIndex + ", maxEndIndex="
            + maxEndIndex + "]\n";
   }

   
   
}
