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
	private int entriesSet = 0;
	private TreeSet<Integer> indeces = new TreeSet<Integer>();

	/**
	 * @return the indeces
	 */
	public TreeSet<Integer> getIndeces() {
		return indeces;
	}

	/**
	 * @param indeces
	 *            the indeces to set
	 */
	public void setIndeces(TreeSet<Integer> indeces) {
		this.indeces = indeces;
	}

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
	 * @param howMany
	 *            the howMany to set
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
	 *            the colour to set
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
	 * @param startIndex
	 *            the startIndex to set
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
	 *            the endIndex to set
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
	 *            the gone to set
	 */
	public void setGone(boolean gone, int startIndex) {
		this.gone = gone;
		setStartIndex(startIndex);
		int endIndex2 = startIndex + howMany - 1;
		setEndIndex(endIndex2);

	}

	/**
	 * Gone is set and startIndex and endIndex.
	 * 
	 * @param gone
	 *            the gone to set
	 */
	public void setGone(boolean gone) {
		this.gone = gone;
		// TODO why nullpointer
		// setStartIndex(startIndex);
		// setEndIndex(startIndex + howMany - 1);
	}

	/**
	 * @return the minStartIndex
	 */
	public Integer getMinStartIndex() {
		return minStartIndex;
	}

	/**
	 * @param minStartIndex
	 *            the minStartIndex to set
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
	 *            the maxEndIndex to set
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
	 * @param minStartIndexNew
	 *            the minStartIndexNew to set
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
	 * @param maxEndIndexNew
	 *            the maxEndIndexNew to set
	 */
	public void setMaxEndIndexNew(Integer maxEndIndexNew) {
		this.maxEndIndexNew = maxEndIndexNew;
		if ((minStartIndexNew + howMany) == (maxEndIndexNew + 1) && !isGone()) {
			System.out.println("Ready");
		}
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	public int getEntriesSet() {
		return entriesSet;
	}

	/**
	 * Increase entriesSet.
	 * 
	 * @return true if the Block is gone after set.
	 */
	public boolean increaseEntriesSet(int index) {
		// System.out.println("increaseEntriesSet()");

		if (!indeces.add(index)) {
			this.entriesSet++;
		}
		if (howMany == entriesSet) {
			// TODO: wann diese?
			setGone(true, indeces.first());
			setGone(true);
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
		return "Block [howMany=" + howMany + ", colour=" + colour
				+ ", startIndex=" + startIndex + ", endIndex=" + endIndex
				+ ", gone=" + gone + ", minStartIndex=" + minStartIndex
				+ ", maxEndIndex=" + maxEndIndex + ", minStartIndexNew="
				+ minStartIndexNew + ", maxEndIndexNew=" + maxEndIndexNew
				+ ", color=" + color + ", colorInt=" + colorInt
				+ ", colorChar=" + colorChar + ", entriesSet=" + entriesSet
				+ ", indeces=" + indeces + "]\n";
	}

	// public void setEntriesSet(int entriesSet) {
	// this.entriesSet = entriesSet;
	// }

}
