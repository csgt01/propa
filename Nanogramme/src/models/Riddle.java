package models;

import java.util.ArrayList;
import java.util.List;


public class Riddle {

    private List<Colour> colours;
    
    private int width;
    private int height;
    
    public Riddle() {
        colours = new ArrayList<Colour>();
    }

    public List<Colour> getColours() {
        return colours;
    }

    public void setColours(List<Colour> colours) {
        this.colours = colours;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
    
    public void addColour(Colour colour) {
        if (null == colours) {
            colours = new ArrayList<Colour>();
        }
        colours.add(colour);
    }

    @Override
    public String toString() {
        return "Riddle [colours=" + colours + ", width=" + width
                + ", height=" + height + "]";
    }
    
    
    
}
