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
	 * @param maxEntries
	 *            the maxEntries to set
	 */
	public void setMaxEntries(int maxEntries) {
		this.maxEntries = maxEntries;
	}

	private ArrayList<Block> blocks;

	private boolean isGone = false;

	private int entriesSet = 0;
	
	private int maxEntries = 0;

	private ArrayList<LinkedList<String>> possibilities;

	public Column() {
		blocks = new ArrayList<>();
		possibilities = new ArrayList<LinkedList<String>>();
	}

	public void addBlock(Block block) {
		if (null == blocks) {
			blocks = new ArrayList<Block>();
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

	public ArrayList<Block> getBlocks() {
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
	public boolean setEntriesSet(int row) {
		entriesSet++;
		if (blocks != null) {
			if (blocks.size() == 1) {
				blocks.get(0).increaseEntriesSet(row);
			} else if (blocks.size() > 1) {
				ArrayList<Integer> indeces = new ArrayList<Integer>();
				for (Block block : blocks) {
					if (row >= block.getMinStartIndexNew() &&  row <= block.getMaxEndIndexNew()) {
						indeces.add(blocks.indexOf(block));
					}
				}
				if (indeces.size() == 1) {
					blocks.get(indeces.get(0)).increaseEntriesSet(row);
				}
			}
		}
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

	public void setBlocks(ArrayList<Block> blocks) {
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((blocks == null) ? 0 : blocks.hashCode());
		result = prime * result + entriesSet;
		result = prime * result + (isGone ? 1231 : 1237);
		result = prime * result + maxEntries;
		result = prime * result
				+ ((possibilities == null) ? 0 : possibilities.hashCode());
		return result;
	}

}
