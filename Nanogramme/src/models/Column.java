package models;

import java.util.ArrayList;
import java.util.LinkedList;

public class Column {

	/**
	 * @return the maxEntries
	 */
	public int getMaxEntries() {
		return maxEntries;
	}

	/**
	 * @param maxEntries the maxEntries to set
	 */
	public void setMaxEntries(int maxEntries) {
		this.maxEntries = maxEntries;
	}

	/**
	 * @param entriesSet the entriesSet to set
	 */
	public void setEntriesSet(int entriesSet) {
		this.entriesSet = entriesSet;
	}

	private LinkedList<Block> blocks;

	private boolean isGone = false;

	private int entriesSet = 0;

	private int maxEntries = 0;

	private ArrayList<LinkedList<String>> possibilities;

	public Column() {
		blocks = new LinkedList<>();
		possibilities = new ArrayList<LinkedList<String>>();
	}

	public void addBlock(Block block) {
		if (null == blocks) {
			blocks = new LinkedList<Block>();
		}
		blocks.add(block);
		maxEntries += block.getHowMany();
	}

	public boolean isGone() {
		return isGone;
	}

	public void setGone(boolean isGone) {
		this.isGone = isGone;
		 for (Block block : blocks) {
          block.setGone(true);
       }
	}

	public LinkedList<Block> getBlocks() {
		return blocks;
	}

	public int getEntriesSet() {
		return entriesSet;
	}

	/**
	 * Inkrementiert die Anzahl der gesetzten Felder.
	 * 
	 * @return true wenn alle Felder gesetzt sind.
	 */
	public boolean setEntriesSet() {
		entriesSet++;

		if (entriesSet == maxEntries) {
			setGone(true);
			return true;
		}
		return false;
	}

	protected int getBlockCount() {
		int blockCount = 0;
		for (Block block : blocks) {
			blockCount += block.getHowMany();
		}
		return blockCount;
	}

	public void setBlocks(LinkedList<Block> blocks) {
		this.blocks = blocks;
	}

	/**
	 * @return the possibilities
	 */
	public ArrayList<LinkedList<String>> getPossibilities() {
		return possibilities;
	}

	/**
	 * @param possibilities
	 *            the possibilities to set
	 */
	public void setPossibilities(ArrayList<LinkedList<String>> possibilities) {
		this.possibilities = possibilities;
	}

	@Override
	public String toString() {
		return "\nColumn [maxEntries=" + maxEntries + ", , entriesSet="
				+ entriesSet + ", isGone=" + isGone + ", blocks=" + blocks
				+ "]";

	}

}
