package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.TreeSet;

import models.Block;
import models.Column;
import models.Riddle;
import models.Row;
import models.StackHolder;
import de.feu.propra.nonogramme.interfaces.INonogramSolver;

public class NonoSolver3 implements INonogramSolver {

	/**
	 * Zu nutzende {@link RiddleService}
	 */
	private RiddleService riddleLoader;

	/**
	 * Das zu lösende {@link Riddle}.
	 */
	private Riddle riddle;

	/**
	 * Die Matrix mit dem aktuellen Stand der Lösung.
	 */
	private char[][] matrix;

	
	/**
	 * Der Status beim Lösen. 0 = lösend 1 = 2 =
	 */
	private int solveState = 0;
	/**
	 * Lösungen, die beim raten der Lösung gefunden wurden.
	 */
	ArrayList<char[][]> solutionsFromGuising = new ArrayList<char[][]>();
	/**
	 * Liste der StackHolder, die bei jedem Raten hinzugefügt werden.
	 */
	LinkedList<StackHolder> stacks = new LinkedList<StackHolder>();
	/**
	 * Lösungen, die beim brutforce gefunden wurden.
	 */
	ArrayList<ArrayList<ArrayList<String>>> loesungenVonBrutForce = new ArrayList<ArrayList<ArrayList<String>>>();

	/**
	 * Konstruktor. Er kann mit einer bereits gefüllten matrix aufgerufen
	 * werden. Sonst wird eine matrix initialisiert.
	 * 
	 * @param matrix
	 */
	public NonoSolver3(char[][] matrix, Riddle riddle) {

		if (matrix == null) {

		} else {
			this.matrix = matrix;
		}

		this.riddle = riddle;

	}

	/**
	 * Konstruktor.
	 */
	public NonoSolver3() {

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
		// // String methodName = "openFile(" + arg0 + ")";
		// // // System.out.println(methodName);
		riddleLoader = new RiddleService(null);
		riddle = riddleLoader.readFile(arg0);
		matrix = riddleLoader.matrix;
	}

	@Override
	public char[][] getSolution() {
		// String methodName = "getSolution()";
		// // System.out.println(methodName);
		// long startTime = new Date().getTime();
		if (matrix == null) {
			setupMatrix();
		}
		setupBlocks();
		while (solveState != 1) {
			if (solveState == 2) {
				// System.out.println("solveState:2");
				if (stacks != null && stacks.size() > 0) {
					// showMatrix();
					// showBlockGoneTrue();
					// showMatrix();
					// return matrix;
					if (!changeLastStacksMember()) {
						switch (solutionsFromGuising.size()) {
						case 0:
							return matrix;
						case 1:
							matrix = solutionsFromGuising.get(0);
							showMatrix();
							return solutionsFromGuising.get(0);
						default:
							return null;
						}
					}
				} else {
					// System.out.println("out");
					if (solutionsFromGuising == null
							|| solutionsFromGuising.size() != 1) {
						// System.out.println(solutionsFromGuising);
						// System.out.println("null");
						// showMatrix();
						// showBlockGoneTrue();
						 showMatrix();
						return matrix;
						// return null;
					} else {
						// // System.out.println(matrix);
						// showMatrix();
						// showBlockGoneTrue();
						// showMatrix();
						matrix = solutionsFromGuising.get(0);
						showMatrix();
						return solutionsFromGuising.get(0);
					}
				}
			} else if (solveState == 3) {
				// System.out.println("solveState:3");
				try {
					// System.out.println("solveState:3");
					// showMatrix();
					// showBlockGoneTrue();
					// showMatrix();
					// return matrix;
					setFirstStarToSomething();
					solveState = 0;

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (solveState == 4) {
				// System.out.println("solveState:4");
				// return matrix;
				if (!changeLastStacksMember()) {
					switch (solutionsFromGuising.size()) {
					case 0:
						showMatrix();
						return matrix;
					case 1:
						matrix = solutionsFromGuising.get(0);
						showMatrix();
						return solutionsFromGuising.get(0);
					default:
						return null;
					}
				}
				solveState = 0;
			}
			solveState = 0;
			solve();
		}
		// // System.out.println("Time for " + methodName + ": "
		// + (new Date().getTime() - startTime) + " ms");
		// if (solveState == 1) {
		// // System.out.println("Good");
		// showMatrix();
		// showBlockGoneTrue();
		// showMatrix();
		showMatrix();
		return matrix;
		// } else {
		// // System.out.println("Bad");
		// return null;
		// }
	}

	/**
	 * Managed den Ablauf des Lösens.
	 * 
	 * @return solveState:
	 */
	private int solve() {
		// System.out.println("solve()");
		try {
			findOverlappingAreasInColumn();
			findOverlappingAreasInRow();
			boolean run1 = true;
			while (run1) {
				int starCount = getStarCountInRiddle();
				solveIterative();
				// solveRecursive();
				// solveWithPossibilities();
				if (starCount <= getStarCountInRiddle()) {
					solveState = 3;
					run1 = false;
				}
				// mögliche Lösung gefunden
				if (getStarCountInRiddle() == 0) {
					// direkte Lösung, da vorher nicht geraten wurde
					if (stacks == null || stacks.size() == 0) {
						solveState = 1;
						run1 = false;
						if (solutionFromTryingOk()) {
							// TODO: testen ob Möglichkeit ok!
							solutionsFromGuising.add(matrix);
							// andere Möglichkeiten beim Raten testen!
						}
					} else {
						run1 = false;
						// TODO: testen ob Möglichkeit ok!
						solutionsFromGuising.add(matrix);
						solveState = 4;
					}
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			// showMatrix();
			// // System.out.println(stacks.size());
			solveState = 2;
		} finally {
			// showMatrix();
			// showBlockGoneTrue();
		}
		return solveState;
	}

	private boolean solutionFromTryingOk() {
		// System.out.println("solutionFromTryingOk()");

		return true;
	}	

	/**
	 * Prüft ob die in {@link #solveByBrutForceByRow(LinkedList, int, int)}
	 * zusammengestellte Matrix korrekt ist.
	 * 
	 * @param returnList
	 * @return
	 */
	private boolean checkStateOfWrittenMatrixByRow(char[][] checkMatrix, int rowIn) {

		// String methodName = "checkStateOfWrittenMatrix()";
		// // System.out.println(methodName);
		// Date startTime = new Date();

		// Array anlegen
		// // System.out.println("XXXXXXXXXXX");
		// for (ArrayList<String> list : returnList) {
		// // // System.out.println(list);
		// }

		// eigentliche Tests:
		int rowInt = rowIn;
		char empty = '-';
		Row column = getRows().get(rowInt);
		ArrayList<Block> blocks = column.getBlocks();
		if (blocks != null && blocks.size() > 0) {
			int columnInt = 0;
			int blockInt = 0;
			while (columnInt < riddle.getWidth()) {
				if (checkMatrix[columnInt][rowInt] == empty) {
					columnInt++;
				} else {
					// Ist eine Farbe, aber keine Blöcke mehr!
					if (blockInt >= blocks.size()) {
						// // // System.out.println("false1: row: " + roInt +
						// " column: "
						// + columnInt);
						return false;
					} else {
						// Block prüfen
						Block block = blocks.get(blockInt);
						for (int i = 0; i < block.getHowMany(); i++) {
							if (!(checkMatrix[columnInt][rowInt] ==
									block.getColorChar())) {
								// // System.out.println("false2: row: " +
								// rowInt
								// + " column: " + columnInt);
								return false;
							}
						}
						columnInt += block.getHowMany();
						// Nächster Block ist gleiche Farbe, also muss -
						// sein!
						if ((blockInt + 1) < blocks.size()
								&& block.getColourString().equals(
										blocks.get(blockInt + 1)
												.getColourString())
								&& !(checkMatrix[columnInt][rowInt] == empty)) {
							// // System.out.println("false3: row: " + rowInt
							// + " column: " + columnInt);
							return false;
						}
						blockInt++;
					}
				}
			}
		} else {
			// nur - !!!
			for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
				if (!(checkMatrix[columnInt][rowInt] == empty)) {
					// // System.out.println("false4: row: " + rowInt
					// + " column: " + columnInt);
					return false;
				}
			}
		}

		// // // System.out.println("YYYYYYYYYYY");
		// showAMatrix(testMatrix);
		// // // System.out.println("Time for " + methodName + ": " + (new
		// Date().getTime() - startTime.getTime()) + " ms");
		return true;
	}

	/**
	 * Holt den zuletzt hinzugefügten Stackholder und ändert die Farbe. Das
	 * Riddle und die Matrix werden auf den Stand, der im Stackholder
	 * gespeichert ist zurückgesetzt. Falls alle Farben getestet wurden, wird
	 * der Stackholder entfernt und diese Methode nochmal aufgerufen.
	 * 
	 * @return false, wenn stacks null oder leer ist.
	 */
	private boolean changeLastStacksMember() {
		// System.out.println("changeLastStacksMember");
		// System.out.println("stacks size:" + stacks.size());
		// showMatrix();
		if (stacks != null && stacks.size() > 0) {
			StackHolder lastStackHolder = stacks.get(stacks.size() - 1);
			// // System.out.println("changeLastStacksMember():" + stacks.size()
			// + " colors:" + riddle.getColours().size() + " indexOfCo:"
			// + lastStackHolder.getIndexOfColor());
			int indexOfColor = lastStackHolder.getIndexOfColor();
			// // System.out.println("Index:" + indexOfColor);
			// String out = "";
			// out = showRow(out, lastStackHolder.getRow());
			// // System.out.println("row " + lastStackHolder.getRow()
			// + " before change:\n" + out);
			indexOfColor++;
			// System.out.println("color of stack:" + indexOfColor);
			// System.out.println();
			if (indexOfColor >= riddle.getColours().size()) {

				Riddle stackRiddle = lastStackHolder.getRiddle();
				riddle = new Riddle(stackRiddle.getColours(),
						stackRiddle.getWidth(), stackRiddle.getHeight(),
						stackRiddle.getRows(), stackRiddle.getColumns(),
						stackRiddle.getNono());
				matrix = new char[riddle.getHeight()][riddle.getWidth()];
				for (int i = 0; i < riddle.getHeight(); i++) {
					// str += "\n";
					for (int j = 0; j < riddle.getWidth(); j++) {
						// str += matrix[i][j] + " ";
						matrix[i][j] = lastStackHolder.getMatrix()[i][j];
					}
				}

				// System.out.println("removed");
				stacks.removeLast();
				if (!changeLastStacksMember()) {
					return false;
				}
			} else {
				lastStackHolder.setIndexOfColor(indexOfColor);
				// System.out.println(riddle
				// .getColours().get(indexOfColor));
				// System.out.println(lastStackHolder.getRow() + " " +
				// lastStackHolder.getColumn());
				Riddle stackRiddle = lastStackHolder.getRiddle();
				riddle = new Riddle(stackRiddle.getColours(),
						stackRiddle.getWidth(), stackRiddle.getHeight(),
						stackRiddle.getRows(), stackRiddle.getColumns(),
						stackRiddle.getNono());
				matrix = new char[riddle.getHeight()][riddle.getWidth()];
				for (int i = 0; i < riddle.getHeight(); i++) {
					// str += "\n";
					for (int j = 0; j < riddle.getWidth(); j++) {
						// str += matrix[i][j] + " ";
						matrix[i][j] = lastStackHolder.getMatrix()[i][j];
					}
				}
				// System.out.println("changed");
				try {
					writeCharInMatrix(lastStackHolder.getRow(), riddle
							.getColours().get(indexOfColor).getName(),
							lastStackHolder.getColumn());
				} catch (Exception e) {
					// System.out.println("OHNOOHNO");
					e.printStackTrace();
				}
			}
			// showMatrix();
			// System.out.println("endchangeLastStacksMember()");
			return true;
		} else {
			// System.out.println("change false");
			// System.out.println("endchangeLastStacksMember()");
			return false;
		}
	}

	/**
	 * Setzt den ersten "*" in der Matrix auf "-". Des weiteren wird ein
	 * StackHolder-Objekt angelegt, um den Status des Lösungsweges zu speichern.
	 * 
	 * @throws Exception
	 *             kann nicht passieren.
	 */
	private void setFirstStarToSomething() throws Exception {
		// System.out.println("setFirstStarToSomething()");
		// System.out.println("stacks size:" + stacks.size());
		// showMatrix();
		for (int row = 0; row < riddle.getHeight(); row++) {
			for (int column = 0; column < riddle.getWidth(); column++) {
				if (matrix[row][column] == '*') {
					// // System.out.println("new Stack:" + row + "/" + column);
					StackHolder stack = new StackHolder();
					stack.setRiddle(new Riddle(riddle.getColours(), riddle
							.getWidth(), riddle.getHeight(), riddle.getRows(),
							riddle.getColumns(), riddle.getNono()));
					stack.setMatrix(matrix, riddle.getHeight(),
							riddle.getWidth());
					stack.setRow(row);
					stack.setColumn(column);
					stack.setIndexOfColor(-1);
					stacks.add(stack);
					writeCharInMatrix(row, '-', column);
					// String out = "";
					// out = showRow(out, row);
					// // System.out.println("new row:" + row + "\n" + out +
					// "\n");
					// // System.out.println("Stacks:" + stacks.size());
					// System.out.println("setFirstStarToSomething() end");
					// showMatrix();
					return;
				}
			}
		}

	}

	/**
	 * Überprüft, ob ein "*" nur zu einem Block gehört. Falls dies der Fall ist,
	 * wird überprüft, ob row.getMaxEntries() - row.getEntriesSet() - starCount)
	 * == 0 ist. Dies bedeutet, dass nur noch ein Farbfeld zu setzen ist und es
	 * mit der Farbe des Blocks gesetzt werden kann.
	 * 
	 * @param row
	 * @throws Exception
	 */
	private void checkStarBelongingToBlockForRow(Row row) throws Exception {
		ArrayList<Block> blocks = row.getBlocks();
		if (blocks == null || blocks.size() == 0) {
			return;
		}
		int rowInt = getIndexOfRow(row);
		// // System.out.println();
		for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
			// showMatrix();// System.out.println();
			if (matrix[rowInt][columnInt] == '*') {
				LinkedList<Integer> blockInts = new LinkedList<Integer>();
				for (Block block : blocks) {
					if (columnInt >= block.getMinStartIndexNew()
							&& columnInt <= block.getMaxEndIndexNew()) {
						blockInts.add(blocks.indexOf(block));
					}
				}
				// // System.out.println("row:" + rowInt + "columnInt: " +
				// columnInt
				// + "\n" + blockInts.size() + " " + blockInts);
				if (blockInts.size() == 1) {
					int starCount = 0;
					for (int i = 0; i < riddle.getWidth(); i++) {
						if (matrix[getIndexOfRow(row)][i] == '*') {
							starCount++;
						}
					}
					if ((row.getMaxEntries() - row.getEntriesSet() - starCount) == 0) {
						// // System.out.println("QQQQQrow:" + rowInt +
						// "columnInt: "
						// + columnInt);
						fillAreaInRowWithChar(rowInt, columnInt, columnInt + 1,
								blocks.get(blockInts.get(0)).getColourString()
										.charAt(0));
					}
				} else if (blockInts.size() == 0) {
					// // System.out.println("EMPTYEMPTYEMPTY");
					fillAreaInRowWithChar(rowInt, columnInt, columnInt + 1, '-');
				} else if (blockInts.size() > 1) {
					// // System.out.println("> 1 Row:" + rowInt + " col:" +
					// columnInt);

					// Wenn es keinen Block in der Spalte gibt mit derselben
					// Farbe und
					// rowInt >= block2.getMinStartIndexNew() && rowInt <=
					// block2.getMaxEndIndexNew() dann mit leer füllen
					boolean bo = true;
					for (Integer i : blockInts) {
						Block block = blocks.get(i);
						ArrayList<Block> blocks2 = getColumns().get(columnInt)
								.getBlocks();
						for (Block block2 : blocks2) {
							if (((block.getColorChar() == block2.getColorChar())
									&& rowInt >= block2.getMinStartIndexNew() && rowInt <= block2
									.getMaxEndIndexNew())) {
								bo = false;
							}
						}
					}
					if (bo) {
						fillAreaInRowWithChar(rowInt, columnInt, columnInt + 1,
								'-');
					}
				}
				// // System.out.println("JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ");
			}
		}
	}

	/**
	 * Prüft, ob eine gesetzte Farbe zu einem Block gehört. Wenn ja wird der
	 * Index in indeces von Block geschrieben und die maximale und minimale
	 * Ausdehnung geändert.
	 * 
	 * @param row
	 * @throws Exception
	 */
	private void checkColorBelongingToBlockForRow(Row row) throws Exception {
		ArrayList<Block> blocks = row.getBlocks();
		if (blocks == null || blocks.size() == 0) {
			return;
		}
		int rowInt = getIndexOfRow(row);
		for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
			char c = matrix[rowInt][columnInt];
			// color
			if (c != '*' && c != '-') {
				LinkedList<Integer> blockInts = new LinkedList<Integer>();
				for (Block block : blocks) {
					if (columnInt >= block.getMinStartIndexNew()
							&& columnInt <= block.getMaxEndIndexNew()
							&& c == block.getColorChar()) {
						blockInts.add(blocks.indexOf(block));
					}
				}
				// "*" gehört nur zu einem Block
				if (blockInts.size() == 1) {
					Block block = blocks.get(blockInts.get(0));
					int min = columnInt - block.getHowMany() + 1;
					if (min < 0) {
						min = 0;
					}
					int max = columnInt + block.getHowMany() - 1;
					if (max >= riddle.getWidth()) {
						max = riddle.getWidth() - 1;
					}
					block.setMaxEndIndexNew(max);
					block.setMinStartIndexNew(min);
				} else if (blockInts.size() == 0) {
					throw new Exception("Farbe gehört zu keinem Block:"
							+ getIndexOfRow(row) + "/" + columnInt + ":" + c);
				}
			}
		}
	}

	/**
	 * Prüft, ob eine gesetzte Farbe zu einem Block gehört. Wenn ja wird der
	 * Index in indeces von Block geschrieben und die maximale und minimale
	 * Ausdehnung geändert.
	 * 
	 * @param column
	 * @throws Exception
	 */
	private void checkColorBelongingToBlockForColumn(Column column)
			throws Exception {
		ArrayList<Block> blocks = column.getBlocks();
		if (blocks == null || blocks.size() == 0) {
			return;
		}
		int columnInt = getIndexOfColumn(column);
		for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
			char c = matrix[rowInt][columnInt];
			// color
			if (c != '*' && c != '-') {
				LinkedList<Integer> blockInts = new LinkedList<Integer>();
				for (Block block : blocks) {
					if (rowInt >= block.getMinStartIndexNew()
							&& rowInt <= block.getMaxEndIndexNew()
							&& c == block.getColorChar()) {
						blockInts.add(blocks.indexOf(block));

					}
					// TODO check what to do
					// else
					// if (rowInt >= block.getMinStartIndexNew()
					// && rowInt <= block.getMaxEndIndexNew()) {
					// // System.out.println("row:" + rowInt + " column:" +
					// columnInt +
					// " other color:" + c + " block:" + block);
					//
					// }
				}
				if (blockInts.size() == 1) {
					Block block = blocks.get(blockInts.get(0));
					int min = rowInt - block.getHowMany() + 1;
					if (min < 0) {
						min = 0;
					}
					int max = rowInt + block.getHowMany() - 1;
					if (max >= riddle.getHeight()) {
						max = riddle.getHeight() - 1;
					}
					block.setMaxEndIndexNew(max);
					block.setMinStartIndexNew(min);
				} else if (blockInts.size() == 0) {
					throw new Exception("Farbe gehört zu keinem Block:"
							+ rowInt + "/" + getIndexOfColumn(column) + ":" + c);
				}
			}
		}
	}

	/**
	 * Überprüft, ob ein "*" nur zu einem Block gehört. Falls dies der Fall ist,
	 * wird überprüft, ob row.getMaxEntries() - row.getEntriesSet() - starCount)
	 * == 0 ist. Dies bedeutet, dass nur noch ein Farbfeld zu setzen ist und es
	 * mit der Farbe des Blocks gesetzt werden kann.
	 * 
	 * @param column
	 * @throws Exception
	 */
	private void checkStarBelongingToBlockForColumn(Column column)
			throws Exception {
		ArrayList<Block> blocks = column.getBlocks();
		if (blocks == null || blocks.size() == 0) {
			return;
		}
		int columnInt = getIndexOfColumn(column);
		for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
			if (matrix[rowInt][columnInt] == '*') {
				LinkedList<Integer> blockInts = new LinkedList<Integer>();
				for (Block block : blocks) {
					if (rowInt >= block.getMinStartIndexNew()
							&& rowInt <= block.getMaxEndIndexNew()) {
						blockInts.add(blocks.indexOf(block));

					}
				}
				if (blockInts.size() == 1) {
					int starCount = 0;
					for (int i = 0; i < riddle.getHeight(); i++) {
						if (matrix[i][getIndexOfColumn(column)] == '*') {
							starCount++;
						}
					}
					if ((column.getMaxEntries() - column.getEntriesSet() - starCount) == 0) {
						// // System.out.println("QQQQQrow:" + rowInt +
						// "columnInt: "
						// + columnInt);
						fillAreaInColumnWithChar(columnInt, rowInt, rowInt + 1,
								blocks.get(blockInts.get(0)).getColourString()
										.charAt(0));
					}
				} else if (blockInts.size() == 0) {
					// // System.out.println("EMPTYEMPTYEMPTY");
					fillAreaInColumnWithChar(columnInt, rowInt, rowInt + 1, '-');
				} else if (blockInts.size() > 1) {
					// // System.out.println("> 1 Row:" + rowInt + " col:" +
					// columnInt);

					// Wenn es keinen Block in der Reihe gibt mit derselben
					// Farbe und
					// columnInt >= block2.getMinStartIndexNew() && columnInt <=
					// block2.getMaxEndIndexNew() dann mit leer füllen
					boolean bo = true;
					for (Integer i : blockInts) {
						Block block = blocks.get(i);
						ArrayList<Block> blocks2 = getRows().get(rowInt)
								.getBlocks();
						for (Block block2 : blocks2) {
							if (((block.getColorChar() == block2.getColorChar())
									&& columnInt >= block2
											.getMinStartIndexNew() && columnInt <= block2
									.getMaxEndIndexNew())) {
								bo = false;
							}
						}
					}
					if (bo) {
						fillAreaInColumnWithChar(columnInt, rowInt, rowInt + 1,
								'-');
					}
				}
			}
		}
	}

	/**
	 * Überprüft, ob ein Leerfeld zu einem Block gehört. Wenn der Index gleich
	 * dem MinStart oder MaxEnd ist, werden diese in/dekrementiert. Wird in
	 * {@link #checkByBlock()} aufgerufen.
	 * 
	 * @param row
	 * @throws Exception
	 */
	private void checkEmptyBelongingToBlockForRow(Row row) throws Exception {
		ArrayList<Block> blocks = row.getBlocks();
		if (blocks == null || blocks.size() == 0) {
			return;
		}
		int rowInt = getIndexOfRow(row);
		for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
			if (matrix[rowInt][columnInt] == '-') {
				for (Block block : blocks) {
					if (!block.isGone()
							&& columnInt == block.getMinStartIndexNew()) {
						block.setMinStartIndexNew(block.getMinStartIndexNew() + 1);
					} else if (!block.isGone()
							&& columnInt == block.getMaxEndIndexNew()) {
						block.setMaxEndIndexNew(block.getMaxEndIndexNew() - 1);
						// Leeres Feld innerhalb des möglichen Blocks.
					} else if (block.getMinStartIndexNew() < columnInt
							&& block.getMaxEndIndexNew() > columnInt) {
						checkSizesBeforeAndAfterEmptyInBlockRange(block,
								columnInt);
					}
				}
			}
		}
	}

	/**
	 * TODO why not dom Prüft, ob nach bzw. vor dem leeren Feld noch genug Platz
	 * für den Block ist. falls nicht, wird minStartIndexNey bzw maxEndIndexNew
	 * angepasst. Wird in {@link #checkEmptyBelongingToBlockForRow(Row)} und
	 * {@link #checkEmptyBelongingToBlockForColumn(Column)} aufgerufen.
	 * 
	 * @param block
	 * @param columnInt
	 * @throws Exception
	 */
	private void checkSizesBeforeAndAfterEmptyInBlockRange(Block block,
			int columnInt) throws Exception {
		if ((columnInt - block.getMinStartIndexNew()) < block.getHowMany()) {
			block.setMinStartIndexNew(columnInt + 1);
		} else if (block.getMaxEndIndexNew() - columnInt < block.getHowMany()) {
			block.setMaxEndIndexNew(columnInt - 1);
		}
	}

	/**
	 * Überprüft, ob ein Leerfeld zu einem Block gehört. Wenn der Index gleich
	 * dem MinStart oder MaxEnd ist, werden diese in/dekrementiert. Wird in
	 * {@link #checkByBlock()} aufgerufen.
	 * 
	 * @param column
	 * @throws Exception
	 */
	private void checkEmptyBelongingToBlockForColumn(Column column)
			throws Exception {
		ArrayList<Block> blocks = column.getBlocks();
		if (blocks == null || blocks.size() == 0) {
			return;
		}
		int columnInt = getIndexOfColumn(column);
		for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
			if (matrix[rowInt][columnInt] == '-') {
				for (Block block : blocks) {
					if (!block.isGone()
							&& rowInt == block.getMinStartIndexNew()) {
						block.setMinStartIndexNew(block.getMinStartIndexNew() + 1);
					} else if (!block.isGone()
							&& rowInt == block.getMaxEndIndexNew()) {
						block.setMaxEndIndexNew(block.getMaxEndIndexNew() - 1);
					} else if (block.getMinStartIndexNew() < rowInt
							&& block.getMaxEndIndexNew() > rowInt) {
						checkSizesBeforeAndAfterEmptyInBlockRange(block, rowInt);
					}
				}
			}
		}
	}

	

	
	private void solveIterative() throws Exception {
//		String methodName = "solveIterative()";
		// System.out.println(methodName);
//		long startTime = new Date().getTime();
		boolean run = true;
		while (run) {
			int starCountInRiddle = getStarCountInRiddle();
			// showMatrix();
			fillBlocksOnBeginningOfColumns();
			// showMatrix();
			fillBlocksOnEndOfColumns();
			// showMatrix();
			fillBlocksOnBeginningOfRows();
			// showMatrix();
			fillBlocksOnEndOfRows();
			// showMatrix();
			checkRowsAndColumnsForGone();
			// showMatrix();
			checkByBlock();
			fillBlocksOnEndOfColumns();
			if (starCountInRiddle <= getStarCountInRiddle()) {
				run = false;
			}
		}
		// System.out.println("Time for " + methodName + ": "
		// + (new Date().getTime() - startTime) + " ms");
		// showMatrix();
	}

	/**
	 * Ruft mehrere Methoden auf, die versuchen das Rätsel auf Blockebene zu
	 * lösen.
	 * 
	 * @throws Exception
	 */
	private void checkByBlock() throws Exception {
		for (Row row : getRows()) {
			// // System.out.println("ROWROWROWROWROWOROWRO" +
			// getIndexOfRow(row));
			updateMinAndMaxIndexOfBlocks(row);
			if (getIndexOfRow(row) == 2) {
				// // System.out.println("dsf");
				// showMatrix();
				// showBlockGoneTrue();
			}
			updateMinAndMaxIndexOfBlocks2(row);
			checkStarBelongingToBlockForRow(row);
			checkEmptyBelongingToBlockForRow(row);
			overlapBlocksInRow(row);
			checkColorBelongingToBlockForRow(row);
			checkIfEntriesNotSetInBlock(row);
			checkEmptyInBetweenBlock(row);
			fillWithEmptyAfterGone(row);
			fillIfMinMaxEqualToHowMany(row);
			fillEntriesFromBlockIntoMatrix(row);
		}
		for (Column column : getColumns()) {
			updateMinAndMaxIndexOfBlocks(column);
			updateMinAndMaxIndexOfBlocks2(column);
			checkStarBelongingToBlockForColumn(column);
			checkEmptyBelongingToBlockForColumn(column);
			overlapBlocksInColumn(column);
			checkColorBelongingToBlockForColumn(column);
			checkIfEntriesNotSetInBlock(column);
			checkEmptyInBetweenBlock(column);
			fillWithEmptyAfterGone(column);
			fillIfMinMaxEqualToHowMany(column);
			fillEntriesFromBlockIntoMatrix(column);
		}
	}

	/**
	 * Schreibt die bereits gesetzten Felder des Blocks in die Matrix.
	 * 
	 * @param row
	 * @throws Exception
	 */
	private void fillEntriesFromBlockIntoMatrix(Row row) throws Exception {

		ArrayList<Block> blocks = row.getBlocks();
		if (blocks == null || blocks.size() == 0) {
			return;
		}
		for (Block block : blocks) {
			if (!block.isGone()) {
				TreeSet<Integer> indeces = block.getIndeces();
				if (indeces.size() > 1) {
					for (int column : indeces) {
						writeCharInMatrix(getIndexOfRow(row), block
								.getColourString().charAt(0), column);
					}
				}
			}
		}

	}

	/**
	 * Schreibt die bereits gesetzten Felder des Blocks in die Matrix.
	 * 
	 * @param row
	 * @throws Exception
	 */
	private void fillEntriesFromBlockIntoMatrix(Column column) throws Exception {

		ArrayList<Block> blocks = column.getBlocks();
		if (blocks == null || blocks.size() == 0) {
			return;
		}
		for (Block block : blocks) {
			if (!block.isGone()) {
				TreeSet<Integer> indeces = block.getIndeces();
				if (indeces.size() > 1) {
					for (int row : indeces) {
						writeCharInMatrix(row, block.getColourString()
								.charAt(0), getIndexOfColumn(column));
					}
				}
			}
		}

	}

	/**
	 * Füllt den Bereich zwischen MinStartIndexNew und MaxEndIndexNew des
	 * Blockes, falls die Differenz gleich der Größe des Blocks ist
	 * 
	 * @param row
	 * @throws Exception
	 */
	private void fillIfMinMaxEqualToHowMany(Row row) throws Exception {
		ArrayList<Block> blocks = row.getBlocks();
		if (blocks == null || blocks.size() < 2) {
			return;
		}
		for (Block block : blocks) {
			if (block.getMaxEndIndexNew() + 1 - block.getMinStartIndexNew() == block
					.getHowMany()) {
				fillAreaInRowWithChar(getIndexOfRow(row),
						block.getMinStartIndexNew(),
						block.getMaxEndIndexNew() + 1, block.getColorChar());
				block.setGone(true, block.getMinStartIndexNew());
			}
		}
	}

	/**
	 * Füllt den Bereich zwischen MinStartIndexNew und MaxEndIndexNew des
	 * Blockes, falls die Differenz gleich der Größe des Blocks ist
	 * 
	 * @param column
	 * @throws Exception
	 */
	private void fillIfMinMaxEqualToHowMany(Column column) throws Exception {
		ArrayList<Block> blocks = column.getBlocks();
		if (blocks == null || blocks.size() < 2) {
			return;
		}
		for (Block block : blocks) {
			if (block.getMaxEndIndexNew() + 1 - block.getMinStartIndexNew() == block
					.getHowMany()) {
				fillAreaInColumnWithChar(getIndexOfColumn(column),
						block.getMinStartIndexNew(),
						block.getMaxEndIndexNew() + 1, block.getColorChar());
				block.setGone(true, block.getMinStartIndexNew());
			}
		}
	}

	/**
	 * Falls die Reihe fertig ist, werden alle "*" mit "-" gefüllt.
	 * 
	 * @param row
	 * @throws Exception
	 */
	private void fillWithEmptyAfterGone(Row row) throws Exception {
		ArrayList<Block> blocks = row.getBlocks();
		if (blocks == null || blocks.size() < 2) {
			return;
		}
		int indexOfRow = getIndexOfRow(row);
		for (int blockInt = 0; blockInt < blocks.size(); blockInt++) {
			Block block = blocks.get(blockInt);
			if (block.isGone()) {
				if (blockInt == 0) {
					if (block.getColourString().equals(
							blocks.get(blockInt + 1).getColourString())
							&& block.getEndIndex() != null
							&& (block.getEndIndex() + 1) < riddle.getWidth()) {
						writeCharInMatrix(indexOfRow, '-',
								block.getEndIndex() + 1);
					}
				} else if (blockInt == blocks.size() - 1) {
					if (block.getColourString().equals(
							blocks.get(blockInt - 1).getColourString())
							&& block.getStartIndex() != null
							&& (block.getStartIndex() - 1) > -1) {
						writeCharInMatrix(indexOfRow, '-',
								block.getStartIndex() - 1);
					}
				} else {
					if (block.getColourString().equals(
							blocks.get(blockInt + 1).getColourString())
							&& block.getEndIndex() != null
							&& (block.getEndIndex() + 1) < riddle.getWidth()) {
						writeCharInMatrix(indexOfRow, '-',
								block.getEndIndex() + 1);
					}
					if (block.getColourString().equals(
							blocks.get(blockInt - 1).getColourString())
							&& block.getStartIndex() != null
							&& (block.getStartIndex() - 1) > -1) {
						writeCharInMatrix(indexOfRow, '-',
								block.getStartIndex() - 1);
					}
				}
			}
		}
	}

	/**
	 * Falls die Spalte fertig ist, werden alle "*" mit "-" gefüllt.
	 * 
	 * @param spalte
	 * @throws Exception
	 */
	private void fillWithEmptyAfterGone(Column column) throws Exception {
		ArrayList<Block> blocks = column.getBlocks();
		if (blocks == null || blocks.size() < 2) {
			return;
		}
		int indexOfColumn = getIndexOfColumn(column);
		for (int blockInt = 0; blockInt < blocks.size(); blockInt++) {
			Block block = blocks.get(blockInt);
			if (block.isGone()) {
				if (blockInt == 0) {
					if (block.getColourString().equals(
							blocks.get(blockInt + 1).getColourString())
							&& block.getEndIndex() != null
							&& (block.getEndIndex() + 1) < riddle.getHeight()) {
						writeCharInMatrix(block.getEndIndex() + 1, '-',
								indexOfColumn);
					}
				} else if (blockInt == blocks.size() - 1) {
					if (block.getColourString().equals(
							blocks.get(blockInt - 1).getColourString())
							&& block.getStartIndex() != null
							&& (block.getStartIndex() - 1) > -1) {
						writeCharInMatrix(block.getStartIndex() - 1, '-',
								indexOfColumn);
					}
				} else {
					if (block.getColourString().equals(
							blocks.get(blockInt + 1).getColourString())
							&& block.getEndIndex() != null
							&& (block.getEndIndex() + 1) < riddle.getHeight()) {
						writeCharInMatrix(block.getEndIndex() + 1, '-',
								indexOfColumn);
					}
					if (block.getColourString().equals(
							blocks.get(blockInt - 1).getColourString())
							&& block.getStartIndex() != null
							&& (block.getStartIndex() - 1) > -1) {
						writeCharInMatrix(block.getStartIndex() - 1, '-',
								indexOfColumn);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param row
	 * @throws Exception
	 */
	private void checkEmptyInBetweenBlock(Row row) throws Exception {

		ArrayList<Block> blocks = row.getBlocks();
		if (blocks == null || blocks.size() == 0) {
			return;
		}
		int indexOfRow = getIndexOfRow(row);
		for (Block block : blocks) {
			for (int i = block.getMinStartIndexNew(); i <= block
					.getMaxEndIndexNew(); i++) {
				if (matrix[indexOfRow][i] == '-') {
					if ((i - block.getMinStartIndexNew()) < block.getHowMany()) {
						block.setMinStartIndexNew(i + 1);
					}
				}
			}
			for (int i = block.getMaxEndIndexNew(); i >= block
					.getMinStartIndexNew(); i--) {
				if (matrix[indexOfRow][i] == '-') {
					if ((block.getMaxEndIndexNew() - i) < block.getHowMany()) {
						block.setMaxEndIndexNew(i - 1);
					}
				}
			}
		}
	}

	private void checkEmptyInBetweenBlock(Column column) throws Exception {

		ArrayList<Block> blocks = column.getBlocks();
		if (blocks == null || blocks.size() == 0) {
			return;
		}
		int indexOfColumn = getIndexOfColumn(column);
		for (Block block : blocks) {
			for (int i = block.getMinStartIndexNew(); i <= block
					.getMaxEndIndexNew(); i++) {
				if (matrix[i][indexOfColumn] == '-') {
					if ((i - block.getMinStartIndexNew()) < block.getHowMany()) {
						block.setMinStartIndexNew(i + 1);
					}
				}
			}
			for (int i = block.getMaxEndIndexNew(); i >= block
					.getMinStartIndexNew(); i--) {
				if (matrix[i][indexOfColumn] == '-') {
					if ((block.getMaxEndIndexNew() - i) < block.getHowMany()) {
						block.setMaxEndIndexNew(i - 1);
					}
				}
			}
		}
	}

	/**
	 * Testet, ob Felder innerhalb von Blöcken gesetzt werden können. Z.B. wenn
	 * in indeces 2 und 5 eingetragen sind, ,müssen 3 und vier auch gesetzt
	 * werden.
	 * 
	 * @param row
	 * @throws Exception
	 */
	private void checkIfEntriesNotSetInBlock(Row row) throws Exception {

		ArrayList<Block> blocks = row.getBlocks();
		if (blocks == null || blocks.size() == 0) {
			return;
		}
		for (Block block : blocks) {
			if (!block.isGone()) {
				TreeSet<Integer> indeces = block.getIndeces();
				if (indeces.size() > 1) {
					for (int column = indeces.first() + 1; column < indeces
							.last(); column++) {
						if (!indeces.contains(column)) {
							block.increaseEntriesSet(column);
							writeCharInMatrix(getIndexOfRow(row), block
									.getColourString().charAt(0), column);
						}
					}
				}
			}
		}
	}

	/**
	 * Testet, ob Felder innerhalb von Blöcken gesetzt werden können. Z.B. wenn
	 * in indeces 2 und 5 eingetragen sind, ,müssen 3 und vier auch gesetzt
	 * werden.
	 * 
	 * @param column
	 * @throws Exception
	 */
	private void checkIfEntriesNotSetInBlock(Column column) throws Exception {

		ArrayList<Block> blocks = column.getBlocks();
		if (blocks == null || blocks.size() == 0) {
			return;
		}
		for (Block block : blocks) {
			if (!block.isGone()) {
				TreeSet<Integer> indeces = block.getIndeces();
				if (indeces.size() > 1) {
					for (int row = indeces.first() + 1; row < indeces.last(); row++) {
						if (!indeces.contains(row)) {
							block.increaseEntriesSet(row);
							writeCharInMatrix(row, block.getColourString()
									.charAt(0), getIndexOfColumn(column));
						}
					}
				}
			}
		}
	}

	/**
	 * Setzt Min/MaxIndex von Blöcken neu, falls der vorherige oder nachfolgende
	 * gone ist. Somit schränkt sich der Bereich, in dem der Block gesetzt
	 * werden kann ein. Überprüft auch, ob bei nicht fertigen Blöcken das
	 * nächste/vorherige von maxEndIndexNew/minStartIndexNew gleich der
	 * Blockfarbe ist und passt dann die Werte an.
	 * 
	 * @param row
	 * @throws Exception
	 */
	private void updateMinAndMaxIndexOfBlocks(Row row) throws Exception {
		// // System.out.println("updateMinAndMaxIndexOfBlocks:" +
		// getIndexOfRow(row));
		// showBlockGoneTrue();
		ArrayList<Block> blocks = row.getBlocks();
		if (blocks != null && blocks.size() > 1) {
			for (int blockIndex = 0; blockIndex < blocks.size(); blockIndex++) {
				Block block = blocks.get(blockIndex);
				if (block.isGone()) {
					int minIndex = block.getMinStartIndexNew();
					int maxIndex = block.getMaxEndIndexNew();
					// erster Block, also nur nachfolgende updaten!
					if (blockIndex == 0) {
						updateBlocksAfterThisBlock(blocks, blockIndex, block,
								maxIndex);
						// letzer Block, also nur davor updaten
					} else if ((blockIndex + 1) == blocks.size()) {
						updateBlocksBeforeThisBlock(blocks, blockIndex, block,
								minIndex);
					} else {
						updateBlocksAfterThisBlock(blocks, blockIndex, block,
								maxIndex);
						updateBlocksBeforeThisBlock(blocks, blockIndex, block,
								minIndex);
					}
				} else {
					// wenn der vorherige/nachfolgende char von Min/Max gleich
					// block.char dann erhöhen/erniedrigen, da ein Feld leer
					// sein
					// muss.
					int minIndex = block.getMinStartIndexNew();
					int maxIndex = block.getMaxEndIndexNew();
					while ((minIndex - 1 > -1)
							&& matrix[getIndexOfRow(row)][minIndex - 1] == block
									.getColorChar()) {
						minIndex++;
						block.setMinStartIndexNew(minIndex);
					}
					while ((maxIndex + 1 < riddle.getWidth())
							&& matrix[getIndexOfRow(row)][maxIndex + 1] == block
									.getColorChar()) {
						maxIndex--;
						block.setMaxEndIndexNew(maxIndex);
					}
				}
			}
		}
		// // System.out.println("bliblarow2:" + getIndexOfRow(row));
		// showBlockGoneTrue();
	}

	/**
	 * Geht durch die Blöcke der Reihe. Wenn es mehr als einen Block gibt,
	 * werden immer zwei aufeinander folgende Blöcke überprüft, ob der
	 * maxEndIndexNew - der Größe des 2. Blocks kleiner ist, als der
	 * maxEndIndexNew des ersten Blocks. Dann muss der Index des 1. Blocks
	 * angepasst werden. Gleiches geschieht für den minStartIndexNew, nur dass
	 * hier der 2. Block angepasst wird.
	 * 
	 * @param row
	 * @throws Exception
	 */
	private void updateMinAndMaxIndexOfBlocks2(Row row) throws Exception {
		// // System.out.println("updateMinAndMaxIndexOfBlocks2:");
		ArrayList<Block> blocks = row.getBlocks();
		if (blocks != null && blocks.size() > 1) {
			for (int blockIndex = 0; blockIndex < blocks.size(); blockIndex++) {
				Block block = blocks.get(blockIndex);
				// prüfen, ob min/maxIndex gleich einer anderen Farbe ist
				if (matrix[getIndexOfRow(row)][block.getMaxEndIndexNew()] != '*'
						&& matrix[getIndexOfRow(row)][block.getMaxEndIndexNew()] != block
								.getColorChar()) {
					block.setMaxEndIndexNew(block.getMaxEndIndexNew() - 1);
				}
				if (matrix[getIndexOfRow(row)][block.getMinStartIndexNew()] != '*'
						&& matrix[getIndexOfRow(row)][block
								.getMinStartIndexNew()] != block.getColorChar()) {
					block.setMinStartIndexNew(block.getMinStartIndexNew() + 1);
				}
				// es gibt noch einen nächsteb Block
				if (blockIndex + 1 < blocks.size()) {
					Block nextBlock = blocks.get(blockIndex + 1);
					if (block.getColorChar() != nextBlock.getColorChar()) {
						if (nextBlock.getMaxEndIndexNew()
								- nextBlock.getHowMany() < block
									.getMaxEndIndexNew()) {
							block.setMaxEndIndexNew(nextBlock
									.getMaxEndIndexNew()
									- nextBlock.getHowMany());
						}
					} else {
						if (nextBlock.getMaxEndIndexNew()
								- nextBlock.getHowMany() - 1 < block
									.getMaxEndIndexNew()) {
							block.setMaxEndIndexNew(nextBlock
									.getMaxEndIndexNew()
									- nextBlock.getHowMany() - 1);
						}
					}
					if (block.getColorChar() != nextBlock.getColorChar()) {
						if (block.getMinStartIndexNew() + block.getHowMany() > nextBlock
								.getMinStartIndexNew()) {
							nextBlock
									.setMinStartIndexNew(block
											.getMinStartIndexNew()
											+ block.getHowMany());
						}
					} else {
						if (block.getMinStartIndexNew() + block.getHowMany()
								+ 1 > nextBlock.getMinStartIndexNew()) {
							nextBlock.setMinStartIndexNew(block
									.getMinStartIndexNew()
									+ block.getHowMany()
									+ 1);
						}
					}
					// kein nächster, also nur vorherigen anpassen!
				} else {
					if (blockIndex - 1 > -1) {
						Block previousBlock = blocks.get(blockIndex - 1);
						if (block.getColorChar() != previousBlock
								.getColorChar()) {
							if (previousBlock.getMinStartIndexNew()
									+ previousBlock.getHowMany() > block
										.getMinStartIndexNew()) {
								block.setMinStartIndexNew(previousBlock
										.getMinStartIndexNew()
										+ previousBlock.getHowMany());
							}
						} else {
							if (previousBlock.getMinStartIndexNew()
									+ previousBlock.getHowMany() + 1 > block
										.getMinStartIndexNew()) {
								block.setMinStartIndexNew(previousBlock
										.getMinStartIndexNew()
										+ previousBlock.getHowMany() + 1);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Geht durch die Blöcke der Spalte. Wenn es mehr als einen Block gibt,
	 * werden immer zwei aufeinander folgende Blöcke überprüft, ob der
	 * maxEndIndexNew - der Größe des 2. Blocks kleiner ist, als der
	 * maxEndIndexNew des ersten Blocks. Dann muss der Index des 1. Blocks
	 * angepasst werden. Gleiches geschieht für den minStartIndexNew, nur dass
	 * hier der 2. Block angepasst wird.
	 * 
	 * @param column
	 * @throws Exception
	 */
	private void updateMinAndMaxIndexOfBlocks2(Column column) throws Exception {
		// // System.out.println("updateMinAndMaxIndexOfBlocks2:");
		ArrayList<Block> blocks = column.getBlocks();
		if (blocks != null && blocks.size() > 1) {
			for (int blockIndex = 0; blockIndex < blocks.size(); blockIndex++) {
				Block block = blocks.get(blockIndex);
				// maxIndex hat bereits eine andere Farbe!
				if (matrix[block.getMaxEndIndexNew()][getIndexOfColumn(column)] != '*'
						&& matrix[block.getMaxEndIndexNew()][getIndexOfColumn(column)] != block
								.getColorChar()) {
					block.setMaxEndIndexNew(block.getMaxEndIndexNew() - 1);
				}
				if (matrix[block.getMinStartIndexNew()][getIndexOfColumn(column)] != '*'
						&& matrix[block.getMinStartIndexNew()][getIndexOfColumn(column)] != block
								.getColorChar()) {
					block.setMinStartIndexNew(block.getMinStartIndexNew() + 1);
				}
				// TODO einrechnen, wenn gleiche Farben
				// es gibt noch einen nächsteb Block
				if (blockIndex + 1 < blocks.size()) {
					Block nextBlock = blocks.get(blockIndex + 1);
					if (block.getColorChar() != nextBlock.getColorChar()) {
						if (nextBlock.getMaxEndIndexNew()
								- nextBlock.getHowMany() < block
									.getMaxEndIndexNew()) {
							block.setMaxEndIndexNew(nextBlock
									.getMaxEndIndexNew()
									- nextBlock.getHowMany());
						}
					} else {
						if (nextBlock.getMaxEndIndexNew()
								- nextBlock.getHowMany() - 1 < block
									.getMaxEndIndexNew()) {
							block.setMaxEndIndexNew(nextBlock
									.getMaxEndIndexNew()
									- nextBlock.getHowMany() - 1);
						}
					}

					if (block.getColorChar() != nextBlock.getColorChar()) {
						if (block.getMinStartIndexNew() + block.getHowMany() > nextBlock
								.getMinStartIndexNew()) {
							nextBlock
									.setMinStartIndexNew(block
											.getMinStartIndexNew()
											+ block.getHowMany());
						}
					} else {
						if (block.getMinStartIndexNew() + block.getHowMany()
								+ 1 > nextBlock.getMinStartIndexNew()) {
							nextBlock.setMinStartIndexNew(block
									.getMinStartIndexNew()
									+ block.getHowMany()
									+ 1);
						}
					}
					// kein nächster, also nur vorherigen anpassen!
				} else {
					if (blockIndex - 1 > -1) {
						Block previousBlock = blocks.get(blockIndex - 1);
						if (block.getColorChar() != previousBlock
								.getColorChar()) {
							if (previousBlock.getMinStartIndexNew()
									+ previousBlock.getHowMany() > block
										.getMinStartIndexNew()) {
								block.setMinStartIndexNew(previousBlock
										.getMinStartIndexNew()
										+ previousBlock.getHowMany());
							}
						} else {
							if (previousBlock.getMinStartIndexNew()
									+ previousBlock.getHowMany() + 1 > block
										.getMinStartIndexNew()) {
								block.setMinStartIndexNew(previousBlock
										.getMinStartIndexNew()
										+ previousBlock.getHowMany() + 1);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Setzt MaxEndIndex der vorherigen Blöcke.
	 * 
	 * 
	 * @param blocks
	 * @param blockIndex
	 * @param block
	 * @param minIndex
	 * @throws Exception
	 */
	private void updateBlocksBeforeThisBlock(ArrayList<Block> blocks,
			int blockIndex, Block block, int minIndex) throws Exception {
		for (int newIndex = (blockIndex - 1); newIndex > -1; newIndex--) {
			Block checkBlock = blocks.get(newIndex);
			if (!checkBlock.isGone()) {
				if (checkBlock.getMaxEndIndexNew() >= minIndex) {
					if (block.getColourString().equals(
							checkBlock.getColourString())) {
						checkBlock.setMaxEndIndexNew(minIndex - 2);
					} else {
						checkBlock.setMaxEndIndexNew(minIndex - 1);
					}
				}
			}
		}
	}

	/**
	 * Setzt MinStartIndexNew der folgenden Blöcke.
	 * 
	 * @param blocks
	 * @param blockIndex
	 * @param block
	 * @param maxIndex
	 * @throws Exception
	 */
	private void updateBlocksAfterThisBlock(ArrayList<Block> blocks,
			int blockIndex, Block block, int maxIndex) throws Exception {
		for (int newIndex = (blockIndex + 1); newIndex < blocks.size(); newIndex++) {
			Block checkBlock = blocks.get(newIndex);
			if (!checkBlock.isGone()) {
				if (checkBlock.getMinStartIndexNew() <= maxIndex) {
					if (block.getColourString().equals(
							checkBlock.getColourString())) {
						checkBlock.setMinStartIndexNew(maxIndex + 2);
					} else {
						checkBlock.setMinStartIndexNew(maxIndex + 1);
					}
				}
			}
		}
	}

	/**
	 * Setzt Min/MaxIndex von Blöcken neu, falls der vorherige oder nachfolgende
	 * gone ist. Somit schränkt sich der Bereich, in dem der Block gesetzt
	 * werden kann ein. Überprüft auch, ob bei nicht fertigen Blöcken das
	 * nächste/vorherige von maxEndIndexNew/minStartIndexNew gleich der
	 * Blockfarbe ist und passt dann die Werte an.
	 * 
	 * @param column
	 * @throws Exception
	 */
	private void updateMinAndMaxIndexOfBlocks(Column column) throws Exception {
		ArrayList<Block> blocks = column.getBlocks();
		if (blocks != null && blocks.size() > 1) {
			for (int blockIndex = 0; blockIndex < blocks.size(); blockIndex++) {
				Block block = blocks.get(blockIndex);
				if (block.isGone()) {
					int minIndex = block.getMinStartIndexNew();
					int maxIndex = block.getMaxEndIndexNew();
					// erster Block, also nur nachfolgende updaten!
					if (blockIndex == 0) {
						updateBlocksAfterThisBlock(blocks, blockIndex, block,
								maxIndex);
					} else if ((blockIndex + 1) == blocks.size()) {
						updateBlocksBeforeThisBlock(blocks, blockIndex, block,
								minIndex);
					} else {
						updateBlocksAfterThisBlock(blocks, blockIndex, block,
								maxIndex);
						updateBlocksBeforeThisBlock(blocks, blockIndex, block,
								minIndex);
					}
				} else {
					// wenn der vorherige/nachfolgende char von Min/Max gleich
					// block.char dann erhöhen/erniedrigen, da ein Feld leer
					// sein
					// muss.
					int minIndex = block.getMinStartIndexNew();
					int maxIndex = block.getMaxEndIndexNew();
					while ((minIndex - 1 > -1)
							&& matrix[minIndex - 1][getIndexOfColumn(column)] == block
									.getColorChar()) {
						minIndex++;
						block.setMinStartIndexNew(minIndex);
					}
					while ((maxIndex + 1 < riddle.getHeight())
							&& matrix[maxIndex + 1][getIndexOfColumn(column)] == block
									.getColorChar()) {
						maxIndex--;
						block.setMaxEndIndexNew(maxIndex);
					}
				}
			}
		}
	}

	/**
	 * Erstellt alle Möglichkeiten, den Block innerhalb von minStartIndexNew und
	 * maxEndIndexNew zu platzieren und setzt die Felder in der Matrix, die bei
	 * allen Möglichkeiten gesetzt send.
	 * 
	 * @param row
	 * @throws Exception
	 */
	private void overlapBlocksInRow(Row row) throws Exception {
		// // System.out.println("Row:" + getIndexOfRow(row));
		// showMatrix();
		// showBlockGoneTrue();
		ArrayList<Block> blocks = row.getBlocks();
		if (blocks != null && blocks.size() > 0) {
			for (Block block : blocks) {
				if (!block.isGone()) {
					int start = block.getMinStartIndexNew();
					LinkedList<String> first = initializeBlockList(block, start);
					LinkedList<String> workingList = new LinkedList<String>();
					workingList.addAll(first);
					LinkedList<Integer> result = new LinkedList<Integer>();
					for (int i = 0; i < first.size(); i++) {
						if (!first.get(i).equals("-")) {
							result.add(i);
						}
					}
					// // System.out.println(result);
					String removed;
					// so lange nach rechts verschieben, bis Ende erreicht und
					// zu
					// asd hinzufügen.
					while (workingList.getLast().equals("-")) {
						removed = workingList.removeLast();
						workingList.addFirst(removed);
						int size = result.size();
						for (int i = size - 1; i > -1; i--) {
							Integer index = result.get(i);
							if (!first.get(index)
									.equals(workingList.get(index))) {
								result.remove(result.get(i));
							}
						}
					}
					// // System.out.println(result);
					for (Integer column : result) {
						char charAt = first.get(column).charAt(0);
						writeCharInMatrix(getIndexOfRow(row), charAt,
								(column + start));
					}
				}
			}
		}
	}

	/**
	 * Erstellt die erste Möglichkeit den Block zu setzten. Wird in
	 * overlapBlocksInColumn und overlapInRow aufgerufen.
	 * 
	 * @param block
	 * @param start
	 * @return
	 */
	private LinkedList<String> initializeBlockList(Block block, int start) {
		int end = block.getMaxEndIndexNew();
		int colorsSet = 0;
		LinkedList<String> first = new LinkedList<String>();
		for (int i = 0; i < block.getHowMany(); i++) {
			first.add(String.valueOf(block.getColour().getName()));
			colorsSet++;
		}
		int ij = end - start + 1 - colorsSet;
		while (ij > 0) {
			first.add("-");
			ij--;
		}
		return first;
	}

	/**
	 * Erstellt alle Möglichkeiten, den Block innerhalb von minStartIndexNew und
	 * maxEndIndexNew zu platzieren und setzt die Felder in der Matrix, die bei
	 * allen Möglichkeiten gesetzt send.
	 * 
	 * @param column
	 * @throws Exception
	 */
	private void overlapBlocksInColumn(Column column) throws Exception {
		ArrayList<Block> blocks = column.getBlocks();
		if (blocks != null && blocks.size() > 0) {
			for (Block block : blocks) {
				if (!block.isGone()) {
					int start = block.getMinStartIndexNew();
					LinkedList<String> first = initializeBlockList(block, start);
					LinkedList<String> workingList = new LinkedList<String>();
					workingList.addAll(first);
					LinkedList<Integer> result = new LinkedList<Integer>();
					for (int i = 0; i < first.size(); i++) {
						if (!first.get(i).equals("-")) {
							result.add(i);
						}
					}
					// String removed;
					// so lange nach rechts verschieben, bis Ende erreicht und
					// zu
					// asd hinzufügen.
					while (workingList.getLast().equals("-")) {
						moveBlocksInList(workingList, first, result);
					}
					for (Integer row : result) {
						char charAt = first.get(row).charAt(0);
						writeCharInMatrix((row + start), charAt,
								getIndexOfColumn(column));
					}
				}
			}
		}
	}

	/**
	 * Erstellt eine neue Matrix mit der Breite und Höhe des Rätsels und füllt
	 * diese mit '*'.
	 */
	private void setupMatrix() {
		// // String methodName = "setupBlocks()";
		// // // System.out.println(methodName);
		// // long startTime = new Date().getTime();
		matrix = new char[riddle.getHeight()][riddle.getWidth()];
		for (int i = 0; i < riddle.getHeight(); i++) {
			for (int j = 0; j < riddle.getWidth(); j++) {
				matrix[i][j] = '*';
			}
		}
		// // // System.out.println("Time for " + methodName + ": " + (new
		// Date().getTime() - startTime) + " ms");
	}

	/**
	 * Setzt die initialen Werte für minStartIndex und maxStartIndex. Wichtig
	 * für {@link #solveRecursive()}.
	 */
	private void setupBlocks() {
		// // String methodName = "setupBlocks()";
		// // // System.out.println(methodName);
		// // long startTime = new Date().getTime();

		// alle Reihen durchgehen.
		for (Row row : getRows()) {
			ArrayList<Block> blocks = row.getBlocks();
			// int index = getIndexOfRow(row);
			// // // System.out.println(index);

			// Blöcke durchgehen.
			setupBlocksInRowAndColumn(blocks, riddle.getWidth());
		}

		for (Column column : getColumns()) {
			ArrayList<Block> blocks = column.getBlocks();
			// Blöcke durchgehen.
			setupBlocksInRowAndColumn(blocks, riddle.getHeight());
		}
		// // // System.out.println("Time for " + methodName + ": " + (new
		// Date().getTime() - startTime) + " ms");
	}

	// Iterativ

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
		// String methodName = "findOverlappingAreasInColumn()";
		// // // System.out.println(methodName);
		// long startTime = new Date().getTime();
		for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
			// Liste, die zum schieben der Blöcke genutzt wird
			LinkedList<String> workingList = new LinkedList<String>();
			// Vergleichsliste
			LinkedList<String> firstList = new LinkedList<String>();
			Column column = riddle.getColumns().get(columnInt);
			// // // System.out.println(column);
			ArrayList<Block> blocks = column.getBlocks();
			// nur wenn es Blöcke gibt
			if (blocks.size() != 0) {
				Block lastBlock = null;
				int resultIndex = 0;
				// Blöcke durchgehen und Größen und mögliche Zwischenräume
				// addieren
				workingList = getFirstConditionOfColumn(blocks, lastBlock,
						resultIndex);
				firstList.addAll(workingList);

				// nur die Indeces der Farben in die ResultList schreiben
				LinkedList<Integer> result = new LinkedList<Integer>();
				for (int i = 0; i < firstList.size(); i++) {

					if (!firstList.get(i).equals("-")) {
						result.add(i);
					}
				}
				// eine Stelle nach hinten schieben
				while (workingList.getLast().equals("-")) {
					moveBlocksInList(workingList, firstList, result);
				}
				for (Integer rowIndex : result) {
					char charAt = firstList.get(rowIndex).charAt(0);
					writeCharInMatrix(rowIndex, charAt, columnInt);
				}
				// sonst: keine Blöcke, also mit '-' füllen
			} else {
				column.setGone(true);
				fillAreaInColumnWithChar(columnInt, 0, riddle.getHeight(), '-');
			}
		}
		// // // System.out.println("Time for " + methodName + ": " + (new
		// Date().getTime() - startTime) + " ms");
		return;
	}

	/**
	 * Verschiebt das letzte Zeichen (immer ein -) von der letzten an die erste
	 * Position. Dann wird jede Position der workingList mit der Position in der
	 * firstList verglichen. Falls die Posiitonen nicht gleich sind wird die
	 * Position aus result gelöscht.
	 * 
	 * @param workingList
	 * @param firstList
	 * @param result
	 *            Liste mit Positionen (aus firstList) die gesetzt werden
	 *            können.
	 */
	private void moveBlocksInList(LinkedList<String> workingList,
			LinkedList<String> firstList, LinkedList<Integer> result) {
		String removed = workingList.removeLast();
		workingList.addFirst(removed);
		int size = result.size();
		// der eigentliche Vergleich
		for (int i = size - 1; i > -1; i--) {
			Integer index = result.get(i);
			if (!firstList.get(index).equals(workingList.get(index))) {
				result.remove(index);
			}
		}
	}

	/**
	 * Geht durch die Spalten und sucht nach überlappenden Bereichen. Dabei
	 * werden die Blöcke aneinander gelegt und bis ans Ende der Spalten
	 * geschoben. An Stellen die immer gefüllt sind kann die Matrix gefüllt
	 * werden.
	 * 
	 * @param matrix
	 * @return
	 * @throws Exception
	 */
	private void findOverlappingAreasInRow() throws Exception {
		// String methodName = "findOverlappingAreasInRow()";
		// // // System.out.println(methodName);
		// long startTime = new Date().getTime();
		for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
			LinkedList<String> workingList = new LinkedList<String>();
			LinkedList<String> first = new LinkedList<String>();
			Row row = riddle.getRows().get(rowInt);
			// // // System.out.println(row);
			ArrayList<Block> blocks = row.getBlocks();
			// nur wenn es Blöcke gibt
			if (blocks.size() != 0) {
				Block lastBlock = null;
				int resultIndex = 0;
				// alle Blöcke so weit wie möglich nach links verschoben
				workingList = getFirstConditionOfRow(blocks, lastBlock,
						resultIndex);
				first.addAll(workingList);
				LinkedList<Integer> result = new LinkedList<Integer>();
				for (int i = 0; i < first.size(); i++) {
					if (!first.get(i).equals("-")) {
						result.add(i);
					}
				}
				// String removed;
				// so lange nach rechts verschieben, bis Ende erreicht und zu
				// asd hinzufügen.
				while (workingList.getLast().equals("-")) {
					moveBlocksInList(workingList, first, result);
				}
				for (Integer column : result) {
					char charAt = first.get(column).charAt(0);
					writeCharInMatrix(rowInt, charAt, column);
				}
				// sonst: keine Blöcke, also mit - füllen
			} else {
				row.setGone(true);
				fillAreaInRowWithChar(rowInt, 0, riddle.getWidth(), '-');
			}
		}
		// // // System.out.println("Time for " + methodName + ": " + (new
		// Date().getTime() - startTime) + " ms");
		return;
	}

	/**
	 * Spalte und Reihe wird mit '-' aufgefüllt, falls gone == true ist.
	 */
	private void checkRowsAndColumnsForGone() {
		// String methodName = "checkRowsAndColumnsForGone()";
		// // // System.out.println(methodName);
		// long startTime = new Date().getTime();
		for (Row row : getRows()) {
			if (row.isGone()) {
				fillRowWithFree(row);
			}
		}
		for (Column column : getColumns()) {
			if (column.isGone()) {
				fillColumnWithFree(column);
			}
		}
		// // // System.out.println("Time for " + methodName + ": " + (new
		// Date().getTime() - startTime) + " ms");
	}

	/**
	 * Füllt Blöcke am Ende der Spalten.
	 * 
	 * @return
	 * @throws Exception
	 */
	private char[][] fillBlocksOnEndOfColumns() throws Exception {
		// String methodName = "fillBlocksOnEndOfColumns()";
		// // // System.out.println(methodName);
		// long startTime = new Date().getTime();
		for (Column column : riddle.getColumns()) {
			fillBlocksOnEndOfColumn(column, column.getBlocks().size() - 1,
					riddle.getHeight() - 1);
		}
		// // // System.out.println("Time for " + methodName + ": " + (new
		// Date().getTime() - startTime) + " ms");
		return matrix;
	}

	/**
	 * Wenn eine Spalte mit einer Farbe endet wird der Block gefüllt. Auch
	 * angefangene Blöcke danach werden gefüllt.
	 * 
	 * @return
	 * @throws Exception
	 */
	private void fillBlocksOnEndOfColumn(Column column, int blockIndex,
			int rowIndex) throws Exception {
		// String methodName = "fillBlocksOnEndOfColumn()";
		// // System.out.println(methodName);
		// long startTime = new Date().getTime();
		int block = blockIndex;
		int rowInt = rowIndex;
		boolean run = true;
		// bis char != - weiterlaufen
		while (run) {
			if (rowInt > -1 && matrix[rowInt][getIndexOfColumn(column)] == '-') {
				rowInt--;
			} else {
				run = false;
			}
		}
		// nur wenn char eine Farbe ist Füllen
		if (rowInt > -1 && matrix[rowInt][getIndexOfColumn(column)] != '*') {
			ArrayList<Block> blocks = column.getBlocks();
			if (blocks != null) {
				Block colourBlock = blocks.get(block);
				if (matrix[rowInt][getIndexOfColumn(column)] == colourBlock
						.getColour().getName()) {
					fillAreaInColumnWithChar(getIndexOfColumn(column), (rowInt
							- colourBlock.getHowMany() + 1), (rowInt + 1),
							colourBlock.getColour().getName());
					rowInt = rowInt - colourBlock.getHowMany();
					colourBlock.setGone(true, rowInt + 1);
					// weiter wenn es noch mehr Blöcke gibt
					if (-1 < block - 1) {
						block--;
						Block nextBlock = blocks.get(block);
						// wenn nächster Block gleiche Farbe hat - setzen
						if (nextBlock != null
								&& nextBlock.getColour().getName() == colourBlock
										.getColour().getName() && rowInt > -1) {
							fillAreaInColumnWithChar(getIndexOfColumn(column),
									rowInt, rowInt + 1, '-');
							rowInt--;
						}
						// nochmal aufrufen
						fillBlocksOnEndOfColumn(column, block, rowInt);
					} else {
						// keine Blöcke mehr, also mit - füllen
						fillColumnWithFree(column);
					}
				} else {
					solveState = 3;
					throw new DataCollisionException(
							"Char wurde nochmal in fillBlocksOnEndOfColumn gesetzt \nchar "
									+ matrix[getIndexOfColumn(column)][rowInt]
									+ " ungleich "
									+ colourBlock.getColour().getName());
				}

			}
		}
		// // // System.out.println("Time for " + methodName + ": " + (new
		// Date().getTime() - startTime) + " ms");
	}

	/**
	 * Füllt Blöcke am Anfang der Spalten.
	 * 
	 * @return
	 * @throws Exception
	 */
	private char[][] fillBlocksOnBeginningOfColumns() throws Exception {
		// String methodName = "fillBlocksOnBeginningOfColumns()";
		// // // System.out.println(methodName);
		// long startTime = new Date().getTime();
		for (Column column : riddle.getColumns()) {
			fillBlocksOnBeginningOfColumn(column, 0, 0);
		}
		// // // System.out.println("Time for " + methodName + ": " + (new
		// Date().getTime() - startTime) + " ms");
		return matrix;
	}

	/**
	 * Füllt Blöcke am Anfang einer Spalte. Ruft sich rekursiv auf.
	 * 
	 * @param column
	 * @param blockIndex
	 * @throws Exception
	 */
	private void fillBlocksOnBeginningOfColumn(Column column, int blockIndex,
			int rowIndex) throws Exception {
		// String methodName = "fillBlocksOnBeginningOfColumn()";
		// // System.out.println(methodName);
		// long startTime = new Date().getTime();
		if (!column.isGone()) {
			int block = blockIndex;
			int rowInt = rowIndex;
			boolean run = true;
			// '-' überspringen
			while (run) {
				if (rowInt < riddle.getHeight()
						&& matrix[rowInt][getIndexOfColumn(column)] == '-') {
					rowInt++;
				} else {
					run = false;
				}
			}
			// Wenn kein '*' und nicht hinter dem Ende
			if (rowInt < riddle.getHeight()
					&& matrix[rowInt][getIndexOfColumn(column)] != '*') {
				ArrayList<Block> blocks = column.getBlocks();
				if (blocks != null) {
					Block colourBlock = blocks.get(block);
					// wenn die Farben übereinstimmen Matrix mit Block füllen
					if ((rowInt + colourBlock.getHowMany()) < riddle
							.getHeight()
							&& matrix[rowInt][getIndexOfColumn(column)] == colourBlock
									.getColour().getName()) {
						fillAreaInColumnWithChar(getIndexOfColumn(column),
								rowInt, (rowInt + colourBlock.getHowMany()),
								colourBlock.getColour().getName());
						colourBlock.setGone(true, rowInt);
						rowInt = rowInt + colourBlock.getHowMany();
						// wenn dahinter noch ein Block Methode wieder aufrufen
						if (blocks.size() > block + 1) {
							block++;
							Block nextBlock = blocks.get(block);
							if (nextBlock != null
									&& nextBlock.getColour().getName() == colourBlock
											.getColour().getName()) {
								fillAreaInColumnWithChar(
										getIndexOfColumn(column), rowInt,
										rowInt + 1, '-');
								rowInt++;
							}
							fillBlocksOnBeginningOfColumn(column, block, rowInt);
							// falls kein Block dahinter kann Spalte mit '-'
							// gefüllt werden
						} else {
							fillColumnWithFree(column);
						}
					} else {
						if ((rowInt + colourBlock.getHowMany()) < riddle
								.getHeight()) {
							solveState = 3;
							throw new DataCollisionException(
									"Char wurde nochmal in fillBlocksOnEndOfColumn gesetzt \nchar "
											+ matrix[rowInt][getIndexOfColumn(column)]
											+ " at" + rowInt + "/"
											+ getIndexOfColumn(column)
											+ " ungleich "
											+ colourBlock.getColour().getName());
						}
					}

				}
			}
		}
		// // // System.out.println("Time for " + methodName + ": " + (new
		// Date().getTime() - startTime) + " ms");
	}

	/**
	 * Füllt Blöcke am Ende der Spalten.
	 * 
	 * @return
	 * @throws Exception
	 */
	private char[][] fillBlocksOnEndOfRows() throws Exception {
		// String methodName = "fillBlocksOnEndOfRows()";
		// // // System.out.println(methodName);
		// long startTime = new Date().getTime();
		for (Row row : riddle.getRows()) {
			fillBlocksOnEndOfRow(row, row.getBlocks().size() - 1,
					riddle.getWidth() - 1);
		}
		// // // System.out.println("Time for " + methodName + ": " + (new
		// Date().getTime() - startTime) + " ms");
		return matrix;
	}

	/**
	 * Wenn eine Spalte mit einer Farbe endet wird der Block gefüllt. Auch
	 * angefangene Blöcke danach werden gefüllt.
	 * 
	 * @return
	 * @throws Exception
	 */
	private void fillBlocksOnEndOfRow(Row row, int blockIndex, int columnIndex)
			throws Exception {
		// String methodName = "fillBlocksOnEndOfRow()";
		// // System.out.println(methodName);
		// long startTime = new Date().getTime();
		int block = blockIndex;
		int columnInt = columnIndex;
		boolean run = true;
		while (run) {
			if (columnInt > -1 && matrix[getIndexOfRow(row)][columnInt] == '-') {
				columnInt--;
			} else {
				run = false;
			}
		}
		if (columnInt > -1 && matrix[getIndexOfRow(row)][columnInt] != '*') {
			ArrayList<Block> blocks = row.getBlocks();
			if (blocks != null) {
				Block colourBlock = blocks.get(block);
				if (matrix[getIndexOfRow(row)][columnInt] == colourBlock
						.getColour().getName()) {
					fillAreaInRowWithChar(getIndexOfRow(row), (columnInt
							- colourBlock.getHowMany() + 1), (columnInt + 1),
							colourBlock.getColour().getName());
					columnInt = columnInt - colourBlock.getHowMany();
					colourBlock.setGone(true, columnInt + 1);
					if (-1 < block - 1) {
						block--;
						Block nextBlock = blocks.get(block);
						if (nextBlock != null
								&& nextBlock.getColour().getName() == colourBlock
										.getColour().getName()) {
							fillAreaInRowWithChar(getIndexOfRow(row),
									columnInt, columnInt + 1, '-');
							columnInt--;
						}
						fillBlocksOnEndOfRow(row, block, columnInt);
					} else {
						fillRowWithFree(row);
					}
				} else {
					solveState = 3;
					throw new DataCollisionException("char "
							+ matrix[getIndexOfRow(row)][columnInt]
							+ " ungleich " + colourBlock.getColour().getName());
				}

			}
		}
		// // // System.out.println("Time for " + methodName + ": " + (new
		// Date().getTime() - startTime) + " ms");
	}

	/**
	 * Füllt Blöcke am Anfang der Spalten.
	 * 
	 * @return
	 * @throws Exception
	 */
	private char[][] fillBlocksOnBeginningOfRows() throws Exception {
		// String methodName = "fillBlocksOnBeginningOfRows()";
		// // // System.out.println(methodName);
		// long startTime = new Date().getTime();
		for (Row row : riddle.getRows()) {
			fillBlocksOnBeginningOfRow(row, 0, 0);
		}
		// // // System.out.println("Time for " + methodName + ": " + (new
		// Date().getTime() - startTime) + " ms");
		return matrix;
	}

	/**
	 * Füllt Blöcke am Anfang einer Spalte. Ruft sich rekursiv auf.
	 * 
	 * @param row
	 * @param blockIndex
	 * @throws Exception
	 */
	private void fillBlocksOnBeginningOfRow(Row row, int blockIndex,
			int columnIndex) throws Exception {
		// String methodName = "fillBlocksOnBeginningOfRow()";
		// // System.out.println(methodName);
		// long startTime = new Date().getTime();
		if (!row.isGone()) {
			// showMatrix();
			int block = blockIndex;
			int columnInt = columnIndex;
			boolean run = true;
			// '-' überspringen
			while (run) {
				if (columnInt < riddle.getWidth()
						&& matrix[getIndexOfRow(row)][columnInt] == '-') {
					columnInt++;
				} else {
					run = false;
				}
			}
			// Wenn kein '*' und nicht hinter dem Ende
			if (columnInt < riddle.getWidth()
					&& matrix[getIndexOfRow(row)][columnInt] != '*') {
				ArrayList<Block> blocks = row.getBlocks();
				if (blocks != null) {
					Block colourBlock = blocks.get(block);
					// wenn die Farben übereinstimmen Matrix mit Block füllen
					if ((columnInt + colourBlock.getHowMany()) <= riddle
							.getWidth()
							&& matrix[getIndexOfRow(row)][columnInt] == colourBlock
									.getColour().getName()) {
						fillAreaInRowWithChar(getIndexOfRow(row), columnInt,
								(columnInt + colourBlock.getHowMany()),
								colourBlock.getColour().getName());
						colourBlock.setGone(true, columnInt);
						columnInt = columnInt + colourBlock.getHowMany();
						// wenn dahinter noch ein Block Methode wieder aufrufen
						if (blocks.size() > block + 1) {
							block++;
							Block nextBlock = blocks.get(block);
							if (nextBlock != null
									&& nextBlock.getColour().getName() == colourBlock
											.getColour().getName()) {
								fillAreaInRowWithChar(getIndexOfRow(row),
										columnInt, columnInt + 1, '-');
								columnInt++;
							}
							fillBlocksOnBeginningOfRow(row, block, columnInt);
							// falls kein Block dahinter kann Spalte mit '-'
							// gefüllt werden
						} else {
							// System.out.println("Hierjfjfj");
							fillRowWithFree(row);
						}
					} else {
						if ((columnInt + colourBlock.getHowMany()) < riddle
								.getWidth()) {
							solveState = 3;
							throw new DataCollisionException("char "
									+ matrix[columnInt][getIndexOfRow(row)]
									+ " at" + columnInt + "/"
									+ getIndexOfRow(row) + " ungleich "
									+ colourBlock.getColour().getName());
						}
					}

				}
			}
		}
		// // System.out.println("Time for " + methodName + ": "
		// + (new Date().getTime() - startTime) + " ms");
	}

	// Rekursiv

	
	
	// Helper

	/**
	 * Liefert die initiale Belegung für die Reihe.
	 * 
	 * @param asd
	 * @param blocks
	 * @param lastBlock
	 * @param resultIndex
	 * @return
	 */
	private LinkedList<String> getFirstConditionOfRow(ArrayList<Block> blocks,
			Block lastBlock, int resultIndex) {
		// String methodName = "getFirstConditionOfRow()";
		// // System.out.println(methodName);
		// long startTime = new Date().getTime();
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
		// // // System.out.println("firstCond:" + asd);
		// // System.out.println("Time for " + methodName + ": "
		// + (new Date().getTime() - startTime) + " ms");
		// // System.out.println("asd:" + asd);
		return asd;
	}

	/**
	 * Liefert die initiale Belegung für die Spalte. Dabei werden alle Blöcke
	 * maximal nach oben geschoben.
	 * 
	 * @param asd
	 * @param blocks
	 * @param lastBlock
	 * @param resultIndex
	 * @return
	 */
	private LinkedList<String> getFirstConditionOfColumn(
			ArrayList<Block> blocks, Block lastBlock, int resultIndex) {
		// String methodName = "getFirstConditionOfColumn()";
		// // System.out.println(methodName);
		// long startTime = new Date().getTime();
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
		for (int i = resultIndex; i < riddle.getHeight(); i++) {
			asd.add("-");
		}
		// // // System.out.println("firstCond:" + asd);
		// // System.out.println("Time for " + methodName + ": "
		// + (new Date().getTime() - startTime) + " ms");
		return asd;
	}

	/**
	 * Display the matrix.
	 * 
	 * @param matrix
	 * @throws Exception
	 */
	private void showMatrix() {
//		 String methodName = "showMatrix()";
		 // System.out.println(methodName);
//		 long startTime = new Date().getTime();
		 String out = "\n";
		 for (int i = 0; i < riddle.getHeight(); i++) {
		 out = showRow(out, i);
		 }
		  System.out.println(out);
//		   System.out.println("Time for " + methodName + ": "
//		 + (new Date().getTime() - startTime) + " ms");
	}

	/**
	 * Returns the Row of the current mfatrix as String.
	 * 
	 * @param out
	 * @param i
	 * @return
	 */
	private String showRow(String out, int i) {
		for (int j = 0; j < riddle.getWidth(); j++) {
			out += matrix[i][j];
			out += "  ";
		}
		out += "\n";
		return out;
	}

	private void showBlockGoneTrue() {
//		String methodName = "showBlockGoneTrue()";
		// System.out.println(methodName);
		long startTime = new Date().getTime();
		for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
			Row row = getRows().get(rowInt);
			// System.out.println("Row:" + rowInt + " -- " + row.isGone());
			ArrayList<Block> blocks = row.getBlocks();
			if (null != blocks) {
				for (Block block : blocks) {
					// System.out.println(block);
				}
			}
		}
		for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
			Column column = getColumns().get(columnInt);
			// System.out
			// .println("Column:" + columnInt + " -- " + column.isGone());
			ArrayList<Block> blocks = column.getBlocks();
			if (null != blocks) {
				for (Block block : blocks) {
					// System.out.println(block);
				}
			}
		}
		// System.out.println("Time for " + methodName + ": "
		// + (new Date().getTime() - startTime) + " ms");
	}

	/**
	 * Füllt die Spalte mit '-'.
	 * 
	 * @param column
	 */
	private void fillColumnWithFree(Column column) {
		// // String methodName = "fillColumnWithFree()";
		// // // System.out.println(methodName);
		// // long startTime = new Date().getTime();
		LinkedList<String> list = new LinkedList<String>();
		for (int row = 0; row < riddle.getHeight(); row++) {
			int indexOfColumn = getIndexOfColumn(column);
			if (matrix[row][indexOfColumn] == '*') {
				matrix[row][indexOfColumn] = '-';
			}
			list.add("-");
		}
		ArrayList<LinkedList<String>> possibilities = column.getPossibilities();
		if (possibilities == null || possibilities.size() == 0) {
			possibilities.add(list);
			column.setPossibilities(possibilities);
		}
		// // // System.out.println("Time for " + methodName + ": " + (new
		// Date().getTime() - startTime) + " ms");
	}

	/**
	 * Füllt die Reihe mit '-'.
	 * 
	 * @param row
	 */
	private void fillRowWithFree(Row row) {
		// // String methodName = "fillRowWithFree()";
		// // // System.out.println(methodName);
		// // long startTime = new Date().getTime();
		for (int column = 0; column < riddle.getWidth(); column++) {
			int indexOfRow = getIndexOfRow(row);
			if (matrix[indexOfRow][column] == '*') {
				matrix[indexOfRow][column] = '-';
			}
		}

		// // // System.out.println("Time for " + methodName + ": " + (new
		// Date().getTime() - startTime) + " ms");
	}

	/**
	 * Setzt die initialen Werte für minStartIndex und maxStartIndex für eine
	 * Reihe oder Spalte.
	 * 
	 * @param blocks
	 * @param size
	 */
	private void setupBlocksInRowAndColumn(ArrayList<Block> blocks, int size) {
		// // String methodName = "setupBlocksInRowAndColumn()";
		// // // System.out.println(methodName);
		// // long startTime = new Date().getTime();
		for (int i = 0; i < blocks.size(); i++) {
			Block block = blocks.get(i);
			if (blocks.size() == 0) {
				block.setMinStartIndex(0);
				block.setMaxEndIndex(size - 1);
			} else if (blocks.size() == 1) {
				block.setMinStartIndex(0);
				block.setMaxEndIndex(size - 1);
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
		// // // System.out.println("Time for " + methodName + ": " + (new
		// Date().getTime() - startTime) + " ms");
	}

	/**
	 * Berechnet den minimalen Startindex eines Blockes, indem die Größen der
	 * vorherigen Blöcke unter Berücksichtigung etwaiger Zwischenräume addiert
	 * werden.
	 * 
	 * @param blocks
	 * @param indexOfBlock
	 * @param widthOfRiddle
	 * @return
	 */
	private int getMinStartIndexOfBlock(ArrayList<Block> blocks,
			int indexOfBlock, int widthOfRiddle) {
		// String methodName = "getMinStartIndexOfBlock()";
		// // // System.out.println(methodName);
		// long startTime = new Date().getTime();
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
		// // // System.out.println("Time for " + methodName + ": " + (new
		// Date().getTime() - startTime) + " ms");
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
	private int getMaxEndIndexOfBlock(ArrayList<Block> blocks,
			int indexOfBlock, int size) {
		// String methodName = "getMaxEndIndexOfBlock()";
		// // // System.out.println(methodName);
		// long startTime = new Date().getTime();
		int index = 0;
		Block lastBlock = blocks.get(indexOfBlock);
		for (int i = indexOfBlock + 1; i < blocks.size(); i++) {
			Block block = blocks.get(i);
			index += block.getHowMany();
			if (lastBlock.getColour().getName() == block.getColour().getName()) {
				index++;
			}
			lastBlock = block;
		}
		// // // System.out.println("Time for " + methodName + ": " + (new
		// Date().getTime() - startTime) + " ms");
		return (size - 1 - index);
	}

	/**
	 * Gibt alle Spalten des Rätsels zurück.
	 * 
	 * @return
	 */
	private LinkedList<Column> getColumns() {
		return riddle.getColumns();
	}

	
	/**
	 * Gibt alle Reihen des Rätsels zurück.
	 * 
	 * @return
	 */
	private LinkedList<Row> getRows() {
		return riddle.getRows();
	}

	private int getIndexOfColumn(Column column) {
		return getColumns().indexOf(column);
	}

	private int getIndexOfRow(Row row) {
		return getRows().indexOf(row);
	}

	/**
	 * Füllt den Bereich in der column zwischen rowBegin (inklusive) und rowEnd
	 * (exklusive) mit dem char c.
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
		// String methodName = "fillAreaInColumnWithChar()";
		// // // System.out.println(methodName);
		// long startTime = new Date().getTime();
		for (int row = rowBegin; row < rowEnd; row++) {
			writeCharInMatrix(row, c, columnIndex);
		}
		// // // System.out.println("Time for " + methodName + ": " + (new
		// Date().getTime() - startTime) + " ms");
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
		// String methodName = "fillAreaInRowWithChar()";
		// // // System.out.println(methodName);
		// long startTime = new Date().getTime();
		for (int column = columnBegin; column < columnEnd; column++) {
			writeCharInMatrix(rowIndex, c, column);
		}
		// // // System.out.println("Time for " + methodName + ": " + (new
		// Date().getTime() - startTime) + " ms");
	}

	/**
	 * Füllt die Stelle in der Matrix.
	 * 
	 * @param rowIndex
	 *            Nummer der Reihe in der Matrix.
	 * @param c
	 *            Farbe
	 * @param columnIndex
	 *            rowIndex Nummer der Reihe in der Matrix.
	 * @throws Exception
	 *             falls an der Stelle bereits ein anderer char als c oder '*'
	 *             steht.
	 */
	private void writeCharInMatrix(int rowIndex, char c, int columnIndex)
			throws Exception {
		// String methodName = "writeCharInMatrix()";
		// // // System.out.println(methodName);
		// long startTime = new Date().getTime();
		// // System.out.println(matrix.length);
		// // System.out.println(rowIndex);
		// // System.out.println(columnIndex);
		// if (columnIndex == -1) {
		// showBlockGoneTrue();
		// }
		if (matrix[rowIndex][columnIndex] != '*'
				&& matrix[rowIndex][columnIndex] != c) {
			solveState = 3;
			throw new DataCollisionException("Fehler: row:" + rowIndex
					+ " column:" + columnIndex + " " + c + " ungleich "
					+ matrix[rowIndex][columnIndex]);
		}
		if (matrix[rowIndex][columnIndex] != c) {
			matrix[rowIndex][columnIndex] = c;
			if (matrix[rowIndex][columnIndex] != '-') {
				if (getRows().get(rowIndex).setEntriesSet(columnIndex)) {
					fillRowWithFree(getRows().get(rowIndex));
					// TODO vieleicht noch benötigt
					// LinkedList<String> list = new LinkedList<String>();
					// ArrayList<LinkedList<String>> list2 = new
					// ArrayList<LinkedList<String>>();
					// for (int column = 0; column < riddle.getWidth();
					// column++) {
					// list.add(String.valueOf(matrix[rowIndex][column]));
					// }
					// list2.add(list);
					// getRows().get(rowIndex).setPossibilities(list2);
				}
				if (getColumns().get(columnIndex).setEntriesSet(rowIndex)) {
					fillColumnWithFree(getColumns().get(columnIndex));
					// TODO vieleicht noch benötigt
					// LinkedList<String> list = new LinkedList<String>();
					// ArrayList<LinkedList<String>> list2 = new
					// ArrayList<LinkedList<String>>();
					// for (int row = 0; row < riddle.getHeight(); row++) {
					// list.add(String.valueOf(matrix[row][columnIndex]));
					// }
					// list2.add(list);
					// getColumns().get(columnIndex).setPossibilities(list2);
				}
			}
		}
		// // // System.out.println("Time for " + methodName + ": " + (new
		// Date().getTime() - startTime) + " ms");
	}

	

	/**
	 * Gibt die Anzahl der nicht belegten Felder zurück.
	 * 
	 * @return
	 */
	private int getStarCountInRiddle() {
		// String methodName = "getStarCountInRiddle()";
		// // System.out.println(methodName);
		// long startTime = new Date().getTime();
		int starCount = 0;

		for (Row row : getRows()) {
			for (int i = 0; i < riddle.getWidth(); i++) {
				if (matrix[getIndexOfRow(row)][i] == '*') {
					starCount++;
				}
			}
		}
		// // System.out.println("Time for " + methodName + ": "
		// + (new Date().getTime() - startTime) + " ms");
		return starCount;
	}

	

	/**
	 * @return the solveState
	 */
	public int getSolveState() {
		return solveState;
	}

	/**
	 * @param solveState
	 *            the solveState to set
	 */
	public void setSolveState(int solveState) {
		this.solveState = solveState;
	}

}
