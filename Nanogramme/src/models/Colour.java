package models;

public class Colour {
    
    private int red;
    
    private int green;
    
    private int blue;
    
    private char name;
    
    private Integer nameInt;
    
    

    /**
	 * @param nameInt the nameInt to set
	 */
	public void setNameInt(Integer nameInt) {
		this.nameInt = nameInt;
	}

	/**
	 * @return the nameInt
	 */
	public Integer getNameInt() {
		return nameInt;
	}

	public int getRed() {
        return red;
    }

    public void setRed(Integer red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(Integer green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(Integer blue) {
        this.blue = blue;
    }

    public char getName() {
        return name;
    }

    public void setName(char name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Colour [red=" + red + ", green=" + green + ", blue="
                + blue + ", name=" + name + "]";
    }
    
    

}
