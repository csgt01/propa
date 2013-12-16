package models;

import java.util.ArrayList;
import java.util.LinkedList;

public class Column {

	/**
	 * Liste der Blöcke.
	 */
	private ArrayList<Block> blocks;

	/**
	 * Fertig gesetzt?
	 */
	private boolean isGone = false;

	/**
	 * Anzahl der gesetzten Felder.
	 */
	private int entriesSet = 0;

	/**
	 * Maximale Anzahl der gesetzten Felder (Summe von howMany der Blöcke).
	 */
	private int maxEntries = 0;

	/**
	 * List der Möglichkeiten, die Spalte zu setzen.
	 */
	private ArrayList<LinkedList<String>> possibilities;

	/**
	 * Konstruktor
	 */
	public Column() {
		blocks = new ArrayList<Block>();
		possibilities = new ArrayList<LinkedList<String>>();
	}

	/**
	 * Konstruktor, der aus einer Column ein neues Objekt erzeugt. Gebraucht für
	 * das Raten.
	 * 
	 * @param column
	 */
	public Column(Column column) {
		this.blocks = new ArrayList<Block>();
		for (Block block : column.getBlocks()) {
			this.blocks.add(new Block(block));
		}
		this.entriesSet = column.entriesSet;
		this.isGone = column.isGone;
		this.possibilities = new ArrayList<LinkedList<String>>(
				column.getPossibilities());
	}

	/**
	 * Fügt einen Block hinzu und erhöht maxEntries um die Blockgröße.
	 * 
	 * @param block
	 *            Hinzuzufügende Block.
	 */
	public void addBlock(Block block) {
		if (null == blocks) {
			blocks = new ArrayList<Block>();
		}
		blocks.add(block);
		maxEntries += block.getHowMany();
	}

	/**
	 * 
	 * @return Ist die Column fertig.
	 */
	public boolean isGone() {
		return isGone;
	}

	/**
	 * Setzt isGone. Ausserdem werden alle Blöcke auch auf gone = true gesetzt
	 * wenn isGone == true.
	 * 
	 * @param isGone
	 */
	public void setGone(boolean isGone) {
		this.isGone = isGone;
		if (isGone) {
			for (Block block : blocks) {
				block.setGone(true);
			}
		}
	}

	/**
	 * Gibt die Blöcke der Column zurück.
	 * 
	 * @return Blöcke
	 */
	public ArrayList<Block> getBlocks() {
		return blocks;
	}

	/**
	 * Gibt die Anzahl der gesetzten Felder in der Column zurück.
	 * 
	 * @return Anzahl gesetzter Felder.
	 */
	public int getEntriesSet() {
		return entriesSet;
	}

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

	/**
	 * Inkrementiert die Anzahl der gesetzten Felder. Ausserdem wird auch beim
	 * zugehörigen Block die entriesSet erhöht, falls der Block eindeutig ist.
	 * 
	 * @return true wenn alle Felder gesetzt sind.
	 * @throws Exception
	 */
	public boolean setEntriesSet(int row) throws Exception {
		entriesSet++;
		if (blocks != null) {
			if (blocks.size() == 1) {
				blocks.get(0).increaseEntriesSet(row);
			} else if (blocks.size() > 1) {
				ArrayList<Integer> indeces = new ArrayList<Integer>();
				for (Block block : blocks) {
					if (row >= block.getMinStartIndexNew()
							&& row <= block.getMaxEndIndexNew()) {
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

	/**
	 * Gibt die Summe von howMany (Größe) der Blöcke zurück.
	 * 
	 * @return Summe der Blockgrößen
	 */
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

	/*
	 * (non-Javadoc)
	 * 
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
