package models;

public class Colour {

	/**
	 * Rotwert der Farbe.
	 */
	private int red;

	/**
	 * Grünwert der Farbe.
	 */
	private int green;

	/**
	 * Blauwert der Farbe.
	 */
	private int blue;

	/**
	 * Name der Farbe.
	 */
	private char name;

	/**
	 * 
	 * @return Rotwert
	 */
	public int getRed() {
		return red;
	}

	/**
	 * Setzt den Rotwert.
	 * 
	 * @param red
	 *            Rotwert
	 */
	public void setRed(Integer red) {
		this.red = red;
	}

	/**
	 * 
	 * @return Grünwert
	 */
	public int getGreen() {
		return green;
	}

	/**
	 * Setzt den Grünwert
	 * 
	 * @param green
	 *            Grünwert
	 */
	public void setGreen(Integer green) {
		this.green = green;
	}

	/**
	 * 
	 * @return Blauwert
	 */
	public int getBlue() {
		return blue;
	}

	/**
	 * Setzt Blauwert
	 * @param blue Blauwert
	 */
	public void setBlue(Integer blue) {
		this.blue = blue;
	}

	/**
	 * 
	 * @return Namen der Farbe als char
	 */
	public char getName() {
		return name;
	}

	/**
	 * Setzt den char-Namen
	 * @param name charName
	 */
	public void setName(char name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Colour [red=" + red + ", green=" + green + ", blue=" + blue
				+ ", name=" + name + "]";
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
		result = prime * result + blue;
		result = prime * result + green;
		result = prime * result + red;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
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
		if (red != other.red)
			return false;
		return true;
	}

}
