package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import models.Block;
import models.Column;
import models.Riddle;
import models.Row;
import de.feu.propra.nonogramme.interfaces.INonogramSolver;

public class NonoSolver3 implements INonogramSolver {

	private RiddleLoader riddleLoader;

	private Riddle riddle;

	char[][] matrix;

	/**
	 * Konstruktor. Er kann mit einer bereits gefüllten matrix aufgerufen
	 * werden. Sonst wird eine matrix initialisiert.
	 * 
	 * @param matrix
	 */
	public NonoSolver3(char[][] matrix) {

		if (matrix == null) {

		} else {
			this.matrix = matrix;
		}

	}

	@Override
	public String getEmail() {
		return "csgt01@gmail.com";
	}

	@Override
	public String getMatrNr() {
		return "8352437";
	}

	@Override
	public String getName() {
		return "Christian Schulte genannt Trux";
	}

	@Override
	public void openFile(String arg0) throws IOException {
		String methodName = "openFile(" + arg0 + ")";
		System.out.println(methodName);
		riddleLoader = new RiddleLoader();
		riddle = riddleLoader.readFile(arg0);
	}

	@Override
	public char[][] getSolution() {
		String methodName = "getSolution()";
		System.out.println(methodName);
		long startTime = new Date().getTime();
		setupMatrix();
		setupBlocks();
		try {
			findOverlappingAreasInColumn();
			findOverlappingAreasInRow();
			showMatrix();
			showBlockGoneTrue();
			solveRecursive();
			showMatrix();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Time for " + methodName + ": "
				+ (new Date().getTime() - startTime) + " ms");
		return matrix;
	}

	/**
	 * Setzt die initialen Werte für minStartIndex und maxStartIndex.
	 */
	private void setupBlocks() {
		// alle Reihen durchgehen.
		for (Row row : getRows()) {
			LinkedList<Block> blocks = row.getBlocks();
			// Blöcke durchgehen.
			setupBlocksInRowAndColumn(blocks, riddle.getWidth());
		}

		for (Column column : getColumns()) {
			LinkedList<Block> blocks = column.getBlocks();
			// Blöcke durchgehen.
			setupBlocksInRowAndColumn(blocks, riddle.getHeight());
		}
	}

	/**
	 * Setzt die initialen Werte für minStartIndex und maxStartIndex für eine
	 * Reihe oder Spalte.
	 * 
	 * @param blocks
	 * @param size
	 */
	private void setupBlocksInRowAndColumn(LinkedList<Block> blocks, int size) {
		for (int i = 0; i < blocks.size(); i++) {
			Block block = blocks.get(i);
			if (blocks.size() == 0) {
				block.setMinStartIndex(0);
				block.setMaxEndIndex(riddle.getWidth() - 1);
			} else {
				if (i == 0) {
					block.setMinStartIndex(0);
					block.setMaxEndIndex(getMaxEndIndexOfBlock(blocks, i, size));
				} else if (i == blocks.size() - 1) {
					block.setMinStartIndex(getMinStartIndexOfBlock(blocks, i,
							size));
					block.setMaxEndIndex(size - 1);
				} else {
					block.setMinStartIndex(getMinStartIndexOfBlock(blocks, i,
							size));
					block.setMaxEndIndex(getMaxEndIndexOfBlock(blocks, i, size));
				}
			}
		}
	}

	private int getMinStartIndexOfBlock(LinkedList<Block> blocks,
			int indexOfBlock, int widthOfRiddle) {
		int index = 0;
		Block lastBlock = blocks.get(indexOfBlock);
		for (int i = indexOfBlock - 1; i >= 0; i--) {
			Block block = blocks.get(i);
			index += block.getHowMany();
			if (null != lastBlock
					&& lastBlock.getColour().getName() == block.getColour()
							.getName()) {
				index++;
			}
			lastBlock = block;
		}
		return index;
	}

	/**
	 * Berechnet den maximalen Index für einen Block.
	 * 
	 * @param blocks
	 * @param indexOfBlock
	 * @param size
	 * @return
	 */
	private int getMaxEndIndexOfBlock(LinkedList<Block> blocks,
			int indexOfBlock, int size) {
		int index = 0;
		Block lastBlock = blocks.get(indexOfBlock);
		for (int i = index + 1; i < blocks.size(); i++) {
			Block block = blocks.get(i);
			index += block.getHowMany();
			if (lastBlock.getColour().getName() == block.getColour().getName()) {
				index++;
			}
			lastBlock = block;
		}
		return (size - 1 - index);
	}

	private LinkedList<Column> getColumns() {
		return riddle.getColumns();
	}

	private LinkedList<Row> getRows() {
		return riddle.getRows();
	}

	/**
	 * Erstellt eine neue Matrix mit der Breite und Höhe des Rätsels und füllt
	 * diese mit '*'.
	 */
	private void setupMatrix() {
		matrix = new char[riddle.getHeight()][riddle.getWidth()];
		for (int i = 0; i < riddle.getHeight(); i++) {
			for (int j = 0; j < riddle.getWidth(); j++) {
				matrix[i][j] = '*';
			}
		}
	}

	private int getIndexOfColumn(Column column) {
		return getColumns().indexOf(column);
	}

	private int getIndexOfRow(Row row) {
		return getRows().indexOf(row);
	}

	private void showBlockGoneTrue() {
		System.out.println("showBlockGoneTrue");
		for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
			Row row = getRows().get(rowInt);
			System.out.println("Row:" + rowInt + " -- " + row.isGone());
			LinkedList<Block> blocks = row.getBlocks();
			if (null != blocks) {
				for (Block block : blocks) {
					System.out.println(block);
				}
			}
		}
		for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
			Column column = getColumns().get(columnInt);
			System.out
					.println("Column:" + columnInt + " -- " + column.isGone());
			LinkedList<Block> blocks = column.getBlocks();
			if (null != blocks) {
				for (Block block : blocks) {
					System.out.println(block);
				}
			}
		}
	}

	/**
	 * Füllt den Bereich in der column zwischen rowBegin (inklusive) und rowEnd
	 * (exklisive) mit dem char c.
	 * 
	 * @param matrix
	 * @param columnIndex
	 * @param rowBegin
	 * @param rowEnd
	 * @param c
	 * @return
	 * @throws Exception
	 */
	private char[][] fillAreaInColumnWithChar(int columnIndex, int rowBegin,
			int rowEnd, char c) throws Exception {
		System.out.println("fillAreaInColumnWithChar");
		for (int row = rowBegin; row < rowEnd; row++) {
			writeCharInMatrix(row, c, columnIndex);
		}
		return matrix;
	}

	/**
	 * Füllt den Bereich in der row zwischen columnBegin (inklusive) und
	 * columnEnd (exklusive) mit dem char c.
	 * 
	 * @param matrix
	 * @param columnIndex
	 * @param rowBegin
	 * @param rowEnd
	 * @param c
	 * @return
	 * @throws Exception
	 */
	private void fillAreaInRowWithChar(int rowIndex, int columnBegin,
			int columnEnd, char c) throws Exception {
		System.out.println("fillAreaInRowWithChar");
		for (int column = columnBegin; column < columnEnd; column++) {
			writeCharInMatrix(rowIndex, c, column);
		}
	}

	/**
	 * Nochnötig?
	 * 
	 * @param matrix
	 * @return
	 * @throws Exception
	 */
	private void findOverlappingAreasInRow() throws Exception {
		System.out.println("findOverlappingAreasInRow");
		for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
			LinkedList<String> asd = new LinkedList<String>();
			LinkedList<String> first = new LinkedList<String>();
			Row row = riddle.getRows().get(rowInt);
			System.out.println(row);
			LinkedList<Block> blocks = row.getBlocks();
			// nur wenn es Blöcke gibt
			if (blocks.size() != 0) {
				Block lastBlock = null;
				int resultIndex = 0;
				asd = getFirstConditionOfRow(blocks, lastBlock, resultIndex);
				first.addAll(asd);
				LinkedList<Integer> result = new LinkedList<Integer>();
				for (int i = 0; i < first.size(); i++) {
					if (!first.get(i).equals("-")) {
						result.add(i);
					}
				}
				String removed;
				while (asd.getLast().equals("-")) {
					removed = asd.removeLast();
					asd.addFirst(removed);
					int size = result.size();
					for (int i = size - 1; i > -1; i--) {
						Integer index = result.get(i);
						if (!first.get(index).equals(asd.get(index))) {
							result.remove(index);
						}
					}
				}
				for (Integer column : result) {
					char charAt = first.get(column).charAt(0);
					writeCharInMatrix(rowInt, charAt, column);
				}
				// sonst: keine Blöcke, also mit - füllen
			} else {
				fillAreaInRowWithChar(rowInt, 0, riddle.getWidth(), '-');
			}
		}
		return;
	}

	/**
	 * @param asd
	 * @param blocks
	 * @param lastBlock
	 * @param resultIndex
	 * @return
	 */
	private LinkedList<String> getFirstConditionOfRow(LinkedList<Block> blocks,
			Block lastBlock, int resultIndex) {
		LinkedList<String> asd = new LinkedList<String>();
		for (Block block : blocks) {
			if (null != lastBlock
					&& lastBlock.getColour().getName() == block.getColour()
							.getName()) {
				asd.add("-");
				resultIndex++;
			}

			for (int i = 0; i < block.getHowMany(); i++) {
				asd.add(String.valueOf(block.getColour().getName()));
			}

			resultIndex += block.getHowMany();
			lastBlock = block;
		}
		for (int i = resultIndex; i < riddle.getWidth(); i++) {
			asd.add("-");
		}
		return asd;
	}

	/**
	 * Geht durch die Spalten und sucht nach überlappenden Bereichen. Dabei
	 * werden die Blöcke aneinander gelegt und bis ans Ende der Spalten
	 * geschoben. An Stellen die immer gefüllt sind kann die Matrix gefüllt
	 * werden.
	 * 
	 * @throws Exception
	 *             falls eine Stelle in der Matrix schon mit einem anderen char
	 *             gefüllt ist.
	 */
	private void findOverlappingAreasInColumn() throws Exception {
		System.out.println("findOverlappingAreasInColumn");
		for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
			LinkedList<String> asd = new LinkedList<String>();
			LinkedList<String> first = new LinkedList<String>();
			Column column = riddle.getColumns().get(columnInt);
			System.out.println(column);
			LinkedList<Block> blocks = column.getBlocks();
			// nur wenn es Blöcke gibt
			if (blocks.size() != 0) {
				Block lastBlock = null;
				int resultIndex = 0;
				// Blöcke durchgehen und Größen und mögliche Zeischenräume
				// addieren
				for (Block block : blocks) {
					if (null != lastBlock
							&& lastBlock.getColour().getName() == block
									.getColour().getName()) {
						asd.add("-");
						resultIndex++;
					}
					for (int i = 0; i < block.getHowMany(); i++) {
						asd.add(String.valueOf(block.getColour().getName()));
					}
					resultIndex += block.getHowMany();
					lastBlock = block;
				}
				// Restliche, nicht mit Blöcken besetzte Stellen mit - füllen
				for (int i = resultIndex; i < riddle.getHeight(); i++) {
					asd.add("-");
				}
				first.addAll(asd);
				// nur die Indeces der Farben in die ResultList schreiben
				LinkedList<Integer> result = new LinkedList<Integer>();
				for (int i = 0; i < first.size(); i++) {

					if (!first.get(i).equals("-")) {
						result.add(i);
					}
				}
				// eine Stelle nach hinten schieben
				String removed;
				while (asd.getLast().equals("-")) {
					removed = asd.removeLast();
					asd.addFirst(removed);
					int size = result.size();
					// der eigentliche Vergleich
					for (int i = size - 1; i > -1; i--) {
						Integer index = result.get(i);
						if (!first.get(index).equals(asd.get(index))) {
							result.remove(index);
						}
					}
				}
				for (Integer rowIndex : result) {
					char charAt = first.get(rowIndex).charAt(0);
					writeCharInMatrix(rowIndex, charAt, columnInt);
				}
				// sonst: keine Blöcke, also mit '-' füllen
			} else {
				fillAreaInColumnWithChar(columnInt, 0, riddle.getHeight(), '-');
			}
		}
		return;
	}

	/**
	 * Füllt die Stelle in der Matrix.
	 * 
	 * @param rowIndex
	 * @param c
	 * @param columnIndex
	 * @throws Exception
	 */
	private void writeCharInMatrix(int rowIndex, char c, int columnIndex)
			throws Exception {
		if (matrix[rowIndex][columnIndex] != '*'
				&& matrix[rowIndex][columnIndex] != c) {
			throw new Exception("Fehler: row:" + rowIndex + " column:"
					+ columnIndex + " " + c + " ungleich "
					+ matrix[rowIndex][columnIndex]);
		}
		if (matrix[rowIndex][columnIndex] != c) {
			matrix[rowIndex][columnIndex] = c;
			if (matrix[rowIndex][columnIndex] != '-') {
				getRows().get(rowIndex).setEntriesSet();
				getColumns().get(columnIndex).setEntriesSet();
			}
		}
	}

	private void solveRecursive() throws Exception {
		System.out.println("solveRecursive()");
		for (Row row : getRows()) {
			ArrayList<LinkedList<String>> possibilities = row
					.getPossibilities();
			System.out.println("Pos1:" + possibilities);
			if (row.getBlocks().size() > 0 && !row.isGone()) {
				LinkedList<String> firstConditionOfRow = getFirstConditionOfRow(
						row.getBlocks(), null, 0);
				System.out.println("Row:" + getIndexOfRow(row));
				if (possibilities == null || possibilities.size() == 0) {
					possibilities = getPossibilitiesForRowOrColumn(
							row.getBlocks(), firstConditionOfRow,
							possibilities, 0, 0);
					possibilities.add(getFirstConditionOfRow(row.getBlocks(),
							null, 0));
				}
				row.setPossibilities(possibilities);
				System.out.println("Pos1 size:" + possibilities.size() + " "
						+ possibilities);
				possibilities = erasePossibilities(row, row.getPossibilities());
				row.setPossibilities(possibilities);
				System.out.println("Pos2 size:" + possibilities.size() + " "
						+ possibilities);
				if (possibilities.size() > 0) {
					if (possibilities.size() == 1) {
						fillListIntoMatrix(getIndexOfRow(row),
								possibilities.get(0));
					} else {
						// TODO: nothing?
					}
				} else {
					throw new Exception("No solvable!");
				}
				System.out.println("------------");
			}
		}

	}

	private void fillListIntoMatrix(int row, LinkedList<String> possibilities) {

		for (int i = 0; i < riddle.getWidth(); i++) {
			matrix[row][i] = possibilities.get(i).charAt(0);
		}
	}

	private ArrayList<LinkedList<String>> erasePossibilities(Row row,
			ArrayList<LinkedList<String>> possibilities) {
		ArrayList<LinkedList<String>> possibilities2 = new ArrayList<LinkedList<String>>(
				possibilities);
		for (LinkedList<String> possibility : possibilities) {
			System.out.println(possibility);
			boolean possible = isPossibilityGoodInRow(possibility,
					getIndexOfRow(row));
			if (!possible) {
				possibilities2.remove(possibility);
				System.out.println(possibility + " erased");
			}
		}
		return possibilities2;
	}

	private boolean isPossibilityGoodInRow(LinkedList<String> possibility,
			int rowInt) {
		boolean isPossible = true;

		for (int i = 0; i < riddle.getWidth(); i++) {
			char c = matrix[rowInt][i];
			char charAt = possibility.get(i).charAt(0);
			if (c != charAt && c != '*') {
				isPossible = false;
			}
		}
		return isPossible;
	}

	private ArrayList<LinkedList<String>> getPossibilitiesForRowOrColumn(
			LinkedList<Block> blocks1, LinkedList<String> aa,
			ArrayList<LinkedList<String>> possibilities, int numberOfBlock,
			int add1) {
		int add = add1;
		LinkedList<Block> blocks = new LinkedList<Block>(blocks1);
		ArrayList<LinkedList<String>> possibilities2 = new ArrayList<LinkedList<String>>(
				possibilities);
		Block block = blocks.get(numberOfBlock);
		int numberOfBlock2 = numberOfBlock + 1;
		int start = block.getMinStartIndex() + add;
		for (int i = start; i + block.getHowMany() <= block.getMaxEndIndex(); i++) {
			// iterativ die Blöcke durchgehen
			LinkedList<String> bb = new LinkedList<String>(aa);
			if (numberOfBlock2 < blocks.size()) {
				possibilities2 = getPossibilitiesForRowOrColumn(blocks, bb,
						possibilities2, numberOfBlock2, add);
			}
			//
			if (aa.getLast().equals("-")) {
				aa.removeLast();
				aa.add(start, "-");
				possibilities2.add(new LinkedList<String>(aa));
				System.out.println(aa + "i1:" + add);
			} else {
				possibilities2.add(new LinkedList<String>(aa));
				System.out.println(aa + "i2:" + add);
			}
			add++;
		}
		return possibilities2;
	}

	/**
	 * Display the matrix.
	 * 
	 * @param matrix
	 * @throws Exception
	 */
	private void showMatrix() throws Exception {
		for (int i = 0; i < riddle.getHeight(); i++) {
			String out = "";
			for (int j = 0; j < riddle.getWidth(); j++) {
				out += matrix[i][j];
				out += "  ";
			}
			System.out.println(out);
		}
		System.out.println();
		// showBlockGoneTrue(matrix);
	}

}
