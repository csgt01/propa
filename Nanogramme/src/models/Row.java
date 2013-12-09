package models;

import java.util.ArrayList;
import java.util.LinkedList;

public class Row {
	
	private ArrayList<Block> blocks;

	private boolean isGone = false;
	private int entriesSet = 0;
	private int maxEntries = 0;

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

	private ArrayList<LinkedList<String>> possibilities;

	public Row() {
		blocks = new ArrayList<Block>();
		possibilities = new ArrayList<LinkedList<String>>();
	}

	public Row(Row row) {
		this.entriesSet = row.entriesSet;
		this.isGone = row.isGone;
		this.maxEntries = row.maxEntries;
		this.blocks = new ArrayList<Block>();
		for (Block block : row.getBlocks()) {
			this.blocks.add(new Block(block));
		}
		this.possibilities = new ArrayList<LinkedList<String>>(row.getPossibilities());
	}

	public void addBlock(Block block) {
		if (null == blocks) {
			blocks = new ArrayList<Block>();
		}
		blocks.add(block);
		maxEntries += block.getHowMany();
	}

	public ArrayList<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(ArrayList<Block> blocks) {
		this.blocks = blocks;
	}

	public boolean isGone() {
		return isGone;
	}

	public void setGone(boolean isGone) {
		if (blocks != null && isGone) {
			for (Block block : blocks) {
				block.setGone(true);
			}
		}
		this.isGone = isGone;
	}

	public int getEntriesSet() {
		return entriesSet;
	}

	/**
	 * Inkrementiert die Anzahl der gesetzten Felder.
	 * 
	 * @return true wenn alle Felder gesetzt sind.
	 */
	public boolean setEntriesSet(int column) {
		entriesSet++;
		if (entriesSet == maxEntries) {
			setGone(true);
			return true;
		}
		ArrayList<Integer> indeces = new ArrayList<Integer>();
		if (blocks != null && blocks.size() > 0) {
			for (Block block : blocks) {
				if (column >= block.getMinStartIndexNew()
						&& column <= block.getMaxEndIndexNew()) {
					indeces.add(blocks.indexOf(block));
				}
			}
			if (indeces.size() == 1) {
				blocks.get(indeces.get(0)).increaseEntriesSet(column);
			}
		}

		return false;
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

	protected int getBlockCount() {
		int blockCount = 0;
		for (Block block : blocks) {
			blockCount += block.getHowMany();
		}
		return blockCount;
	}

	@Override
	public String toString() {
		return "\nRow [maxEntries=" + maxEntries + ", , entriesSet="
				+ entriesSet + ", isGone=" + isGone + ", blocks=" + blocks
				+ "]";
	}
}
