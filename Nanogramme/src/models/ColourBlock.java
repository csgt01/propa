package models;

public class ColourBlock {
    
    private Integer howMany;
    
    private Colour colour;
    
    private boolean gone = false;

    public Integer getHowMany() {
        return howMany;
    }

    public void setHowMany(Integer howMany) {
        this.howMany = howMany;
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }
    
    

    public boolean isGone() {
        return gone;
    }

    public void setGone(boolean gone) {
        this.gone = gone;
    }

   @Override
   public String toString() {
      return "ColourBlock [howMany=" + howMany + ", colour=" + colour + ", gone=" + gone + "]";
   }

    

}
