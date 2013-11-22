package models;

public class Block {
    
    private Integer howMany;
    
    private Colour colour;
    private Integer startIndex;
    private Integer endIndex;
    private boolean gone;
    private Integer minStartIndex;
    private Integer maxEndIndex;
    private Integer minStartIndexNew;
    private Integer maxEndIndexNew;
    private String color;
    private Integer colorInt;
    private char colorChar;
    
    
    
    public Block() {
       startIndex = null;
       endIndex = null;
       gone = false;
    }
    
    

   /**
	 * @return the colorInt
	 */
	public Integer getColorInt() {
		return colorInt;
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
      this.color = String.valueOf(colour.getName());
      this.colorChar = colour.getName();
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
      this.minStartIndexNew = startIndex;
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
      this.minStartIndexNew = this.minStartIndex;
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
      this.maxEndIndexNew = this.maxEndIndex;
   }



   /**
 * @return the minStartIndexNew
 */
public Integer getMinStartIndexNew() {
	return minStartIndexNew;
}



/**
 * @param minStartIndexNew the minStartIndexNew to set
 */
public void setMinStartIndexNew(Integer minStartIndexNew) {
	this.minStartIndexNew = minStartIndexNew;
}



/**
 * @return the maxEndIndexNew
 */
public Integer getMaxEndIndexNew() {
	return maxEndIndexNew;
}



/**
 * @param maxEndIndexNew the maxEndIndexNew to set
 */
public void setMaxEndIndexNew(Integer maxEndIndexNew) {
	this.maxEndIndexNew = maxEndIndexNew;
}



/**
 * @return the color
 */
public String getColor() {
	return color;
}

/* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString() {
      return "Block [howMany=" + howMany + ", colour=" + colour + ", startIndex=" + startIndex + ", endIndex=" + endIndex + ", gone=" + gone + ", minStartIndex=" + minStartIndex + ", maxEndIndex="
              + maxEndIndex + ", minStartIndexNew=" + minStartIndexNew + ", maxEndIndexNew="
              + maxEndIndexNew + "]\n";
   }

   
   
}
