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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + blue;
		result = prime * result + green;
		result = prime * result + name;
		result = prime * result + ((nameInt == null) ? 0 : nameInt.hashCode());
		result = prime * result + red;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Colour other = (Colour) obj;
		if (blue != other.blue)
			return false;
		if (green != other.green)
			return false;
		if (name != other.name)
			return false;
		if (nameInt == null) {
			if (other.nameInt != null)
				return false;
		} else if (!nameInt.equals(other.nameInt))
			return false;
		if (red != other.red)
			return false;
		return true;
	}
    
}
