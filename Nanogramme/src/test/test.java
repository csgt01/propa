package test;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import service.NotSolvableException;
import models.Block;
import models.Column;
import models.Row;

public class test {

   public static void main(String[] args) {

      new test().start();

   }
   
   /**
	 * TODO: zu langsam
	 * 
	 * @throws Exception
	 */
	private void solveWithPossibilities() throws Exception {
		String methodName = "solveWithPossibilities()";
		// System.out.println(methodName);
		long startTime = new Date().getTime();
		for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
			Row row = getRows().get(rowInt);
			// System.out.println(row.getPossibilities().size());
			getPossibilitiesForRowOrColumn(getIndexOfRow(row), false,
					row.getBlocks(),
					getFirstConditionOfRow(row.getBlocks(), null, 0),
					row.getPossibilities(), 0, 0);
			erasePossibilitiesInRow(row, row.getPossibilities());
			// System.out.println(row.getPossibilities().size());
			// System.out.println(row.getPossibilities());
			schnittmengeFindenInReihe(rowInt, row.getPossibilities());
		}
		// System.out.println(methodName + " in "
		// + (new Date().getTime() - startTime) + " ms.");
	}
	
	/**
	 * Diese Methode nimmt die erste Möglichkeit der ersten Reihe und ruft sich
	 * rekursiv auf, bis sie bei der letzten Reihe angekommen ist. Dann wird
	 * getestet, ob das Ergebnis eine gültige Lösung ist. Wenn nicht, wird die
	 * nächate getestest.
	 * 
	 * @param rowInt
	 * @param possibilityInt
	 * @return
	 */
	private ArrayList<ArrayList<String>> solveByBrutForceByRow(
			ArrayList<ArrayList<String>> listFromBefore, int rowInt,
			int possibilityInt) {
		// // System.out.println("solveByBrutForce");
		// // // System.out.println("solveByBrutForce");
		Integer rowIndex = new Integer(rowInt);

		Integer possibilityIndex = new Integer(possibilityInt);
		ArrayList<ArrayList<String>> returnList = new ArrayList<ArrayList<String>>(
				listFromBefore);
		if (rowIndex < riddle.getHeight()) {
			// // // System.out.println("Row:" + rowIndex);
			Row row = getRows().get(rowIndex);
			// eigene poss hinzufügen
			while (possibilityIndex < row.getPossibilities().size()) {
				// // // System.out.println("pos:" + possibilityIndex);
				returnList = new ArrayList<ArrayList<String>>(listFromBefore);
				returnList.add(new ArrayList<String>(row.getPossibilities()
						.get(possibilityIndex)));
				// Wenn noch Reihen übrig sind Methode neu aufrufen
				if (rowIndex + 1 < riddle.getHeight()) {
					// // // System.out.println("Row before call again:" +
					// (rowIndex
					// +1));
					returnList = solveByBrutForceByRow(returnList,
							(rowIndex + 1), 0);
					// // // System.out.println("Row after call again:" +
					// (rowIndex));
					if (null == returnList) {
						possibilityIndex++;
					} else {
						return returnList;
					}

				} else {
					// // System.out.println("sdafasfddas" + returnList);
					if (!checkStateOfWrittenMatrixByRow(returnList)) {
						returnList = null;
						possibilityIndex++;
					} else {
						return returnList;
					}
				}
			}
		}
		return returnList;
	}
	
	/**
	 * Diese Methode nimmt die erste Möglichkeit der ersten Reihe und ruft sich
	 * rekursiv auf, bis sie bei der letzten Reihe angekommen ist. Dann wird
	 * getestet, ob das Ergebnis eine gültige Lösung ist. Wenn nicht, wird die
	 * nächate getestest. TODO: für column!
	 * 
	 * @param coInt
	 * @param possibilityInt
	 * @return
	 */
	private ArrayList<ArrayList<String>> solveByBrutForceByColumn(
			ArrayList<ArrayList<String>> listFromBefore, int coInt,
			int possibilityInt) {
		// // System.out.println("solveByBrutForceC");
		Integer columnInt = new Integer(coInt);
		Integer possibilityIndex = new Integer(possibilityInt);
		ArrayList<ArrayList<String>> returnList = new ArrayList<ArrayList<String>>(
				listFromBefore);
		if (columnInt < riddle.getWidth()) {
			// // // System.out.println("Row:" + rowIndex);
			Column column = getColumns().get(columnInt);
			// eigene poss hinzufügen
			while (possibilityIndex < column.getPossibilities().size()) {
				// // // System.out.println("pos:" + possibilityIndex);
				returnList = new ArrayList<ArrayList<String>>(listFromBefore);
				returnList.add(new ArrayList<String>(column.getPossibilities()
						.get(possibilityIndex)));
				ArrayList<ArrayList<String>> newList = new ArrayList<ArrayList<String>>(
						returnList);
				// Wenn noch Reihen übrig sind Methode neu aufrufen
				if (columnInt + 1 < riddle.getWidth()) {
					// // // System.out.println("Row before call again:" +
					// (rowIndex
					// +1));
					returnList = solveByBrutForceByColumn(newList,
							(columnInt + 1), 0);
					// // // System.out.println("Row after call again:" +
					// (rowIndex));
					if (null == returnList) {
						possibilityIndex++;
					} else {
						loesungenVonBrutForce.add(returnList);
						return null;
					}

				} else {
					// // System.out.println("sdafasfddas" + returnList);
					if (!checkStateOfWrittenMatrixByColumn(returnList)) {
						returnList = null;
						possibilityIndex++;
					} else {
						return returnList;
					}
				}
			}
		}
		// // System.out.println("ff" + returnList);
		return returnList;
	}

	/**
	 * Prüft ob die in {@link #solveByBrutForceByRow(LinkedList, int, int)}
	 * zusammengestellte Matrix korrekt ist.
	 * 
	 * @param returnList
	 * @return
	 */
	private boolean checkStateOfWrittenMatrixByColumn(
			ArrayList<ArrayList<String>> returnList) {

		// String methodName = "checkStateOfWrittenMatrix()";
		// // System.out.println(methodName);
		// Date startTime = new Date();

		// Array anlegen
		// // System.out.println("XXXXXXXXXXX");
		// for (ArrayList<String> list : returnList) {
		// // // System.out.println(list);
		// }

		// eigentliche Tests:
		int rowInt = 0;
		String empty = "-";
		while (rowInt < riddle.getHeight()) {
			Row column = getRows().get(rowInt);
			ArrayList<Block> blocks = column.getBlocks();
			if (blocks != null && blocks.size() > 0) {
				int columnInt = 0;
				int blockInt = 0;
				while (columnInt < riddle.getWidth()) {
					if (returnList.get(columnInt).get(rowInt).equals(empty)) {
						columnInt++;
					} else {
						// Ist eine Farbe, aber keine Blöcke mehr!
						if (blockInt >= blocks.size()) {
							// // // System.out.println("false1: row: " + roInt
							// +
							// " column: "
							// + columnInt);
							checkedPossibillities++;
							if ((possibillities - checkedPossibillities) % 100000000 == 0) {
								// System.gc();
								// // System.out
								// .println("False1:"
								// + (possibillities - checkedPossibillities));
								long time = new Date().getTime();
								// // System.out.println("Time:" + (time -
								// timeFor));
								timeFor = time;
							}
							return false;
						} else {
							// Block prüfen
							Block block = blocks.get(blockInt);
							for (int i = 0; i < block.getHowMany(); i++) {
								if (!(returnList.get(columnInt).get(rowInt)
										.equals(block.getColourString()))) {
									// // System.out.println("false2: row: " +
									// rowInt
									// + " column: " + columnInt);
									checkedPossibillities++;
									if ((possibillities - checkedPossibillities) % 100000000 == 0) {
										// System.gc();
										long time = new Date().getTime();
										// // System.out.println("Time:"
										// + (time - timeFor));
										timeFor = time;
									}
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
									&& !returnList.get(columnInt).get(rowInt)
											.equals(empty)) {
								// // System.out.println("false3: row: " +
								// rowInt
								// + " column: " + columnInt);
								checkedPossibillities++;
								if ((possibillities - checkedPossibillities) % 100000000 == 0) {
									// System.gc();
									long time = new Date().getTime();
									// // System.out.println("Time:"
									// + (time - timeFor));
									timeFor = time;
								}
								return false;
							}
							blockInt++;
						}
					}
				}
			} else {
				// nur - !!!
				for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
					if (!returnList.get(rowInt).get(columnInt).equals(empty)) {
						// // System.out.println("false4: row: " + rowInt
						// + " column: " + columnInt);
						checkedPossibillities++;
						if ((possibillities - checkedPossibillities) % 100000000 == 0) {
							// System.gc();
						}
						return false;
					}
				}
			}
			rowInt++;
		}

		// // // System.out.println("YYYYYYYYYYY");
		// showAMatrix(testMatrix);
		// // // System.out.println("Time for " + methodName + ": " + (new
		// Date().getTime() - startTime.getTime()) + " ms");
		checkedPossibillities++;
		if ((possibillities - checkedPossibillities) % 100000000 == 0) {
			// System.gc();
			// // System.out.println((possibillities - checkedPossibillities));
			long time = new Date().getTime();
			// // System.out.println("Time:" + (time - timeFor));
			timeFor = time;
		}
		return true;
	}

	// TODO row setEntries mit char...
		/**
		 * Überprüft, ob jede Reihe(ein char) in dieser Spalte mindestens eine
		 * Übereinstimmung in den Possibilities der Reihe hat.
		 * 
		 * @param possibility
		 * @param rowInt
		 * @return
		 */
		private boolean checkPossibilityOfColumnAgainstRows(
				LinkedList<String> possibility, int columnInt) {
			// String methodName = "checkPossibilityOfColumnAgainstRows()";
			// // System.out.println(methodName);
			// long startTime = new Date().getTime();
			boolean isPossible = true;
			if (isPossible) {
				// String (Splaten durchgehen)
				for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
					ArrayList<LinkedList<String>> possibilities = getRows().get(
							rowInt).getPossibilities();
					if (isPossible && possibilities != null
							&& possibilities.size() > 0) {
						boolean isPossibleInColumn = false;
						for (LinkedList<String> possibilityOfRow : possibilities) {
							if (possibilityOfRow.get(columnInt).equals(
									possibility.get(rowInt))) {
								isPossibleInColumn = true;
							}
						}
						if (isPossible) {
							isPossible = isPossibleInColumn;
						}
					}
					// if (isPossible) {
					// char[][] checkMatrix = matrix;
					// checkMatrix = fillListIntoMatrixInRow(checkMatrix, rowInt,
					// possibility);
					// isPossible = checkIfColumnsStateIsPossible(checkMatrix,
					// columnInt);
					// }
				}
			}
			// // System.out.println("Time for " + methodName + ": " + (new
			// Date().getTime() - startTime) + " ms");
			return isPossible;
		}

		/**
		 * Löscht eine Möglichkeit aus der Liste, nachdem sie in
		 * {@link #isPossibilityGoodInRow(LinkedList, int)} geprüft wurde.
		 * 
		 * @param row
		 * @param possibilities
		 * @return ArrayList<LinkedList<String>> weniger .
		 */
		private ArrayList<LinkedList<String>> erasePossibilitiesInRow(Row row,
				ArrayList<LinkedList<String>> possibilities) {
			// String methodName = "erasePossibilitiesInRow()";
			// // System.out.println(methodName);
			// long startTime = new Date().getTime();
			ArrayList<LinkedList<String>> possibilities2 = new ArrayList<LinkedList<String>>(
					possibilities);
			// System.out.println("Row:" + getIndexOfRow(row));
			for (LinkedList<String> possibility : possibilities) {
				boolean possible = isPossibilityGoodInRow(possibility,
						getIndexOfRow(row));
				if (!possible) {
					possibilities2.remove(possibility);
					// // System.out.println(possibility + " erased");
				}
			}
			// // System.out.println("Time for " + methodName + ": "
			// + (new Date().getTime() - startTime) + " ms");
			return possibilities2;
		}
		/**
		 * Löscht eine Möglichkeit aus der Liste, nachdem sie in
		 * {@link #isPossibilityGoodInColumn(LinkedList, int)} geprüft wurde.
		 * 
		 * @param row
		 * @param possibilities
		 * @return ArrayList<LinkedList<String>> weniger .
		 */
		private ArrayList<LinkedList<String>> erasePossibilitiesInColumn(
				Column column, ArrayList<LinkedList<String>> possibilities) {
			// String methodName = "erasePossibilitiesInColumn()";
			// // System.out.println(methodName);
			// long startTime = new Date().getTime();
			ArrayList<LinkedList<String>> possibilities2 = new ArrayList<LinkedList<String>>(
					possibilities);
			for (LinkedList<String> possibility : possibilities) {
				// // System.out.println(possibility);
				boolean possible = isPossibilityGoodInColumn(possibility,
						getIndexOfColumn(column));
				if (!possible) {
					possibilities2.remove(possibility);
					// // System.out.println(possibility + " erased");
				}
			}
			// // System.out.println("Time for " + methodName + ": "
			// + (new Date().getTime() - startTime) + " ms");
			return possibilities2;
		}
		
		/**
		 * Sucht nach einer Lösung nach einem rekursiven Ansatz. Es werden alle
		 * Möglichkeiten der Spalten und Reihen gesucht und nach und nach
		 * ausgeschlossen. Es werden auch die Schnittmengen in die Matrix
		 * geschrieben.
		 * 
		 * @throws Throwable
		 */
		private void solveRecursive() throws Exception {
			String methodName = "solveRecursive()";
			// System.out.println(methodName);

			// long startTime = new Date().getTime();
			boolean run = true;
			for (Column column : getColumns()) {
				// System.out.println("first cond Column:" +
				// getIndexOfColumn(column));
				// // // System.out.println("First condition of column:" +
				// firstConditionOfColumn);
				ArrayList<LinkedList<String>> possibilities = column
						.getPossibilities();
				if (possibilities == null || possibilities.size() == 0) {
					// System.out.println("new");
					LinkedList<String> firstConditionOfColumn = getFirstConditionOfColumn(
							column.getBlocks(), null, 0);
					getPossibilitiesForRowOrColumn(getIndexOfColumn(column), true,
							column.getBlocks(), firstConditionOfColumn,
							possibilities, 0, 0);
					if (run) {
						possibilities.add(getFirstConditionOfColumn(
								column.getBlocks(), null, 0));
					}
					column.setPossibilities(possibilities);
				}
				possibilities = null;
				// System.gc();
			}
			// läuft, bis keine Änderungen mehr vorkommen
			// System.gc();
			// // System.out.println("after first poss");
			while (run) {
				run = false;
				for (Row row : getRows()) {
					// // System.out.println("Row:" + getIndexOfRow(row));
					// Initiale leere Liste
					ArrayList<LinkedList<String>> possibilities = row
							.getPossibilities();
					// // // System.out.println("Pos1:" + possibilities);
					if (row.getBlocks().size() > 0 && !row.isGone()) {
						// Wenn es noch keine Liste mit Möglichkeiten gibt wir sie
						// hier
						// erstellt.
						if (possibilities == null || possibilities.size() == 0) {
							// // System.out.println("new");
							LinkedList<String> firstConditionOfRow = getFirstConditionOfRow(
									row.getBlocks(), null, 0);
							getPossibilitiesForRowOrColumn(getIndexOfRow(row),
									false, row.getBlocks(), firstConditionOfRow,
									possibilities, 0, 0);
							// Initiale Möglichkeit muss von HAnd hinzugefügt werden
							possibilities.add(getFirstConditionOfRow(
									row.getBlocks(), null, 0));
							row.setPossibilities(possibilities);
						}
						int posibillitiesBefore = possibilities.size();
						// // System.out.println("Pos1 size:" +
						// possibilities.size());
						// Unmögliche Möglichkeiten entfernen
						possibilities = erasePossibilitiesInRow(row,
								row.getPossibilities());
						row.setPossibilities(possibilities);
						// System.gc();
						// // System.out.println("Pos2 size:" +
						// possibilities.size());
						int posibillitiesAfter = possibilities.size();
						if (possibilities.size() > 0) {
							if (possibilities.size() == 1) {
								fillListIntoMatrixInRow(getIndexOfRow(row),
										possibilities.get(0));
							} else {
								// // // System.out.println(possibilities);
								schnittmengeFindenInReihe(getIndexOfRow(row),
										possibilities);
							}
						} else {
							throw new NotSolvableException("Not solvable!\n"
									+ "Row " + getIndexOfRow(row) + ":\n" + row);
						}
						int difference = posibillitiesBefore - posibillitiesAfter;
						// // // System.out.println("Difference:" + difference);
						if (difference > 0) {
							run = true;
						}
						// // // System.out.println("------------");
					} else {
						fillRowWithFree(row);
					}
					possibilities = null;
					// System.gc();
				}
				for (Column column : getColumns()) {
					// // System.out.println("Column:" + getIndexOfColumn(column));
					// showMatrix();
					ArrayList<LinkedList<String>> possibilities = column
							.getPossibilities();
					// // // System.out.println("Pos1:" + possibilities);
					if (column.getBlocks().size() > 0 && !column.isGone()) {
						LinkedList<String> firstConditionOfRow = getFirstConditionOfColumn(
								column.getBlocks(), null, 0);
						if (possibilities == null || possibilities.size() == 0) {
							// System.out.println("new");
							getPossibilitiesForRowOrColumn(
									getIndexOfColumn(column), true,
									column.getBlocks(), firstConditionOfRow,
									possibilities, 0, 0);
							possibilities.add(getFirstConditionOfColumn(
									column.getBlocks(), null, 0));
							column.setPossibilities(possibilities);
						}
						int posibillitiesBefore = possibilities.size();
						// System.out.println("Pos1 size:" + possibilities.size());
						possibilities = erasePossibilitiesInColumn(column,
								column.getPossibilities());
						column.setPossibilities(possibilities);
						// System.out.println("Pos2 size:" + possibilities.size());
						// System.gc();
						int posibillitiesAfter = possibilities.size();
						if (possibilities.size() > 0) {
							if (possibilities.size() == 1) {
								fillListIntoMatrixInColumn(
										getIndexOfColumn(column),
										possibilities.get(0));
							} else {
								// // // System.out.println(possibilities);
								schnittmengeFindenInSpalte(
										getIndexOfColumn(column), possibilities);
							}
						} else {
							throw new Exception("Not solvable!\n" + "Column "
									+ getIndexOfColumn(column) + ":\n" + column);
						}
						int difference = posibillitiesBefore - posibillitiesAfter;
						// // // System.out.println("Difference:" + difference);
						if (difference > 0) {
							run = true;
						}
						// // // System.out.println("+++++++++++++++++++++");
					}
					possibilities = null;
					// System.gc();
				}
				// showMatrix();
			}
			// // System.out.println("Time for " + methodName + ": "
			// + (new Date().getTime() - startTime) + " ms");
			// System.gc();
		}

		/**
		 * Schreibt alle Möglichkeiten die Reihe oder Spalte mit den Blöcken zu
		 * belegen in possibilities. Die Methode ruft sich selber auf, um zwischen
		 * den Blöcken zu wechseln. Dabei wird der letzte Block nach hinten
		 * geschoben. Dann wird der vorletzte Block einen weiter nach hinten
		 * geschoben und der letzte wieder nach und nach nach hinten geschoben. Dies
		 * geschieht für alle Blöcke, bis alle Blöcke komplett bis zum Maximum nach
		 * hinten geschoben sind.
		 * 
		 * @param blocks1
		 * @param aa
		 * @param possibilities
		 * @param numberOfBlock
		 * @param add1
		 * @return
		 */
		private ArrayList<LinkedList<String>> getPossibilitiesForRowOrColumn(
				int rowInt, boolean forColumn, ArrayList<Block> blocks1,
				LinkedList<String> aa,
				ArrayList<LinkedList<String>> possibilities2, int numberOfBlock,
				int add1) {
			// String methodName = "getPossibilitiesForRowOrColumn()";
			// // System.out.println(methodName);
			// long startTime = new Date().getTime();
			int add = add1;
			ArrayList<Block> blocks = new ArrayList<Block>(blocks1);
			// ArrayList<LinkedList<String>> possibilities2 = new
			// ArrayList<LinkedList<String>>(
			// possibilities);
			if (blocks.size() > 0) {
				Block block = blocks.get(numberOfBlock);
				int numberOfBlock2 = numberOfBlock + 1;
				int start = block.getMinStartIndex() + add;
				// verschieben des Blocks bis zum Maximum nach hinten.
				for (int i = start; i + block.getHowMany() <= block
						.getMaxEndIndex(); i++) {
					// rekursiv die Blöcke durchgehen
					LinkedList<String> bb = new LinkedList<String>(aa);
					if (numberOfBlock2 < blocks.size()) {
						possibilities2 = getPossibilitiesForRowOrColumn(rowInt,
								forColumn, blocks, bb, possibilities2,
								numberOfBlock2, add);
					}
					bb = null;
					//
					if (aa.getLast().equals("-")) {
						aa.removeLast();
						aa.add(start, "-");
						if (!forColumn) {
							if (isPossibilityGoodInRow(aa, rowInt)) {
								// // // System.out.println("aaaaa" + aa);
								possibilities2.add(new LinkedList<String>(aa));
								// // // System.out.println(aa);
							}
						} else {
							if (isPossibilityGoodInColumn(aa, rowInt)) {
								// // // System.out.println("aaaaa" + aa);
								possibilities2.add(new LinkedList<String>(aa));
								// // // System.out.println(aa);
							}
						}
					} else {
						if (!forColumn) {
							if (isPossibilityGoodInRow(aa, rowInt)) {
								// // // System.out.println("aaaaa" + aa);
								possibilities2.add(new LinkedList<String>(aa));
								// // // System.out.println(aa);
							}
						} else {
							if (isPossibilityGoodInColumn(aa, rowInt)) {
								// // // System.out.println("aaaaa" + aa);
								possibilities2.add(new LinkedList<String>(aa));
								// // // System.out.println(aa);
							}
						}
					}
					add++;
				}
			} else {
				if (forColumn) {
					LinkedList<String> list = new LinkedList<String>();
					for (int i = 0; i < riddle.getHeight(); i++) {
						list.add("-");
					}
					possibilities2.add(list);
					list = null;
				} else {
					LinkedList<String> list = new LinkedList<String>();
					for (int i = 0; i < riddle.getWidth(); i++) {
						list.add("-");
					}
					possibilities2.add(list);
					list = null;
				}
			}
			// // System.out.println("Time for " + methodName + ": "
			// + (new Date().getTime() - startTime) + " ms");
			// // // System.out.println("Possibilities:" + possibilities2.size());
			return possibilities2;
		}

		/**
		 * Prüft, ob die Möglichkeit zu der Spielsituation passt. Falls sie nicht
		 * passt wird false zurück gegeben.
		 * 
		 * @param possibility
		 * @param rowInt
		 * @return boolean
		 */
		private boolean isPossibilityGoodInRow(LinkedList<String> possibility,
				int rowInt) {
			// String methodName = "isPossibilityGoodInRow()";
			// // System.out.println(methodName);
			// long startTime = new Date().getTime();
			boolean isPossible = true;
			// gegen Situation testen
			for (int i = 0; i < riddle.getWidth(); i++) {
				if (isPossible) {
					char charInMatrix = matrix[rowInt][i];
					char charInPoss = possibility.get(i).charAt(0);
					if (charInMatrix != charInPoss && charInMatrix != '*') {
						isPossible = false;
					}
					if (charInMatrix == '*' && getColumns().get(i).isGone()
							&& charInPoss != '-') {
						isPossible = false;
					}
					if (isPossible) {
						// TODO: check this method, performance
						// if (!checkPossibilityOfRowAgainstColumns(possibility,
						// rowInt)) {
						// isPossible = false;
						// }
					}
				}
			}
			// // System.out.println("Time for " + methodName + ": "
			// + (new Date().getTime() - startTime) + " ms");
			return isPossible;
		}

		/**
		 * Prüft, ob die Möglichkeit zu der Spielsituation passt.
		 * 
		 * @param possibility
		 * @param rowInt
		 * @return boolean
		 */
		private boolean isPossibilityGoodInColumn(LinkedList<String> possibility,
				int columnInt) {
			// String methodName = "isPossibilityGoodInColumn()";
			// // System.out.println(methodName);
			// long startTime = new Date().getTime();
			boolean isPossible = true;

			for (int i = 0; i < riddle.getHeight(); i++) {
				if (isPossible) {
					char charInMatrix = matrix[i][columnInt];
					char charInPoss = possibility.get(i).charAt(0);
					if (charInMatrix != charInPoss && charInMatrix != '*') {
						isPossible = false;
					}
					if (charInMatrix == '*' && getRows().get(i).isGone()
							&& charInPoss != '-') {
						isPossible = false;
					}
					// if (!checkPossibilityOfColumnAgainstRows(possibility,
					// columnInt)) {
					// isPossible = false;
					// }
				}
			}
			// // System.out.println("Time for " + methodName + ": " + (new
			// Date().getTime() - startTime) + " ms");
			return isPossible;
		}

		/**
		 * Überprüft, ob jede Spalte in dieser Reihe mindestens eine Übereinstimmung
		 * in den Possibilities der jeweiligen Spalte hat. Ansonsten ist die
		 * Möglichkeit falsch.
		 * 
		 * @param possibility
		 * @param rowInt
		 * @return
		 */
		private boolean checkPossibilityOfRowAgainstColumns(
				LinkedList<String> possibility, int rowInt) {
			// String methodName = "checkPossibilityOfRowAgainstColumns()";
			// // System.out.println(methodName);
			// long startTime = new Date().getTime();
			boolean isPossible = true;
			if (isPossible) {
				// String (Splaten durchgehen)
				for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
					Column column = getColumns().get(columnInt);
					ArrayList<LinkedList<String>> possibilitiesOfColumn = column
							.getPossibilities();
					if (isPossible && possibilitiesOfColumn != null
							&& possibilitiesOfColumn.size() > 0) {
						boolean isPossibleInColumn = false;
						for (LinkedList<String> possibilityOfColumn : possibilitiesOfColumn) {
							if (possibilityOfColumn.get(rowInt).equals(
									possibility.get(columnInt))) {
								isPossibleInColumn = true;
							}
						}
						if (isPossible) {
							isPossible = isPossibleInColumn;
						}
					}
				}
			}
			// // System.out.println("Time for " + methodName + ": " + (new
			// Date().getTime() - startTime) + " ms");
			return isPossible;
		}
		
		/**
		 * Findet chars, die in allen posibilities gesetzt sind. Diese können dann
		 * in die Matrix geschrieben werden.
		 * 
		 * @param indexOfColumn
		 * @param possibilities
		 * @throws Exception
		 */
		private void schnittmengeFindenInSpalte(int indexOfColumn,
				ArrayList<LinkedList<String>> possibilities) throws Exception {
			// long startTime = new Date().getTime();
			// String methodName = "schnittmengeFindenInSpalte()";
			// // System.out.println(methodName);
			// 1. Möglichkeit herausnehmen und Indeces in result schreiben.
			LinkedList<String> firstLinkedList = possibilities.get(0);
			ArrayList<Integer> results = new ArrayList<Integer>();
			for (int j = 0; j < riddle.getHeight(); j++) {
				results.add(new Integer(j));
			}
			// wenn strings an derselben Stelle nicht übereinstimmen herausstreichen
			// aus results
			Integer o;
			for (LinkedList<String> list : possibilities) {
				for (int i = 0; i < riddle.getHeight(); i++) {
					o = new Integer(i);
					if (results.contains(o)) {
						if (!firstLinkedList.get(i).equals(list.get(i))) {
							results.remove(o);
						}
					}
				}
			}
			o = null;
			for (Integer index : results) {
				writeCharInMatrix(index, firstLinkedList.get(index).charAt(0),
						indexOfColumn);
			}
			// // System.out.println("Time for " + methodName + ": "
			// + (new Date().getTime() - startTime) + " ms");
		}

		/**
		 * Findet chars, die in allen posibilities gesetzt sind. Diese können dann
		 * in die Matrix geschrieben werden.
		 * 
		 * @param indexOfRow
		 * @param possibilities
		 * @throws Exception
		 */
		private void schnittmengeFindenInReihe(int indexOfRow,
				ArrayList<LinkedList<String>> possibilities) throws Exception {
			// long startTime = new Date().getTime();
			// String methodName = "schnittmengeFindenInReihe()";
			// // System.out.println(methodName);
			// 1. Möglichkeit herausnehmen und Indeces in result schreiben.
			LinkedList<String> firstLinkedList = possibilities.get(0);
			ArrayList<Integer> results = new ArrayList<Integer>();
			for (int j = 0; j < riddle.getWidth(); j++) {
				results.add(new Integer(j));
			}
			// wenn strings an derselben Stelle nicht übereinstimmen herausstreichen
			// aus results
			Integer o;
			for (LinkedList<String> list : possibilities) {
				for (int i = 0; i < riddle.getWidth(); i++) {
					o = new Integer(i);
					if (results.contains(o)) {
						if (!firstLinkedList.get(i).equals(list.get(i))) {
							results.remove(o);
						}
					}
				}
			}
			o = null;
			for (Integer index : results) {
				writeCharInMatrix(indexOfRow, firstLinkedList.get(index).charAt(0),
						index);
			}
			// // System.out.println("Time for " + methodName + ": "
			// + (new Date().getTime() - startTime) + " ms");
		}

		/**
		 * Display the matrix.
		 * 
		 * @param matrix
		 * @throws Exception
		 */
		private void showAMatrix(String[][] matrix) {
			// String methodName = "showAMatrix()";
			// // // System.out.println(methodName);
			// long startTime = new Date().getTime();
			// for (int i = 0; i < riddle.getHeight(); i++) {
			// String out = "";
			// for (int j = 0; j < riddle.getWidth(); j++) {
			// out += matrix[i][j];
			// out += "  ";
			// }
			// // // // System.out.println(out);
			// }
			// // // System.out.println();
			// showBlockGoneTrue(matrix);
			// // // System.out.println("Time for " + methodName + ": " + (new
			// Date().getTime() - startTime) + " ms");
		}
		/**
		 * Füllt den Inhalt einer zutreffenden Möglichkeit aus
		 * {@link #solveRecursive()} in die Matrix.
		 * 
		 * @param row
		 * @param possibilitiy
		 * @throws Throwable
		 */
		private void fillListIntoMatrixInRow(int row,
				LinkedList<String> possibilitiy) throws Exception {
			// String methodName = "fillListIntoMatrixInRow()";
			// // // System.out.println(methodName);
			// long startTime = new Date().getTime();
			getRows().get(row).setGone(true);
			for (int i = 0; i < riddle.getWidth(); i++) {
				char c = possibilitiy.get(i).charAt(0);
				if (matrix[row][i] != c) {
					matrix[row][i] = c;
					if (c != '-') {
						if (getRows().get(row).setEntriesSet(i)) {
							fillRowWithFree(getRows().get(row));
						}
						if (getColumns().get(i).setEntriesSet(row)) {
							fillColumnWithFree(getColumns().get(i));
						}
					}
				}
			}
			// // // System.out.println("Time for " + methodName + ": " + (new
			// Date().getTime() - startTime) + " ms");
		}

		/**
		 * Füllt den Inhalt einer zutreffenden Möglichkeit aus
		 * {@link #solveRecursive()} in die Matrix.
		 * 
		 * @param row
		 * @param possibilitiy
		 * @throws Exception
		 * @throws Throwable
		 */
		private char[][] fillListIntoMatrixInRow(char[][] checkMatrix, int row,
				LinkedList<String> possibilitiy) throws Exception {
			// String methodName = "fillListIntoMatrixInRow()";
			// // // System.out.println(methodName);
			// long startTime = new Date().getTime();
			getRows().get(row).setGone(true);
			for (int i = 0; i < riddle.getWidth(); i++) {
				char c = possibilitiy.get(i).charAt(0);
				if (checkMatrix[row][i] != c) {
					checkMatrix[row][i] = c;
					if (c != '-') {
						if (getRows().get(row).setEntriesSet(i)) {
							fillRowWithFree(getRows().get(row));
						}
						if (getColumns().get(i).setEntriesSet(row)) {
							fillColumnWithFree(getColumns().get(i));
						}
					}
				}
			}
			// // // System.out.println("Time for " + methodName + ": " + (new
			// Date().getTime() - startTime) + " ms");
			return checkMatrix;
		}

		/**
		 * Füllt den Inhalt einer zutreffenden Möglichkeit aus
		 * {@link #solveRecursive()} in die Matrix.
		 * 
		 * @param row
		 * @param possibilitiy
		 * @throws Exception
		 */
		private void fillListIntoMatrixInColumn(int column,
				LinkedList<String> possibilities) throws Exception {
			// String methodName = "fillListIntoMatrixInColumn()";
			// // // System.out.println(methodName);
			// long startTime = new Date().getTime();
			getColumns().get(column).setGone(true);
			for (int i = 0; i < riddle.getHeight(); i++) {
				char c = possibilities.get(i).charAt(0);
				if (matrix[i][column] != c) {
					matrix[i][column] = c;
					if (c != '-') {
						if (getRows().get(i).setEntriesSet(column)) {
							fillRowWithFree(getRows().get(i));
						}
						if (getColumns().get(column).setEntriesSet(i)) {
							fillColumnWithFree(getColumns().get(column));
						}
					}
				}
			}
			// // // System.out.println("Time for " + methodName + ": " + (new
			// Date().getTime() - startTime) + " ms");
		}
		
		private long getPossibillitySizeOfRow() {
			long rowPosSize = 1;

			for (Row row : getRows()) {
				// // System.out.println("Row " + getIndexOfRow(row)
				// + " pssibilities size:" + row.getPossibilities().size());
				rowPosSize *= row.getPossibilities().size();
				// // // System.out.println(row.getPossibilities());
			}
			return rowPosSize;
		}
		

		private long getPossibillitySizeOfColumn() {
			long columnPosSize = 1;
			for (Column column : getColumns()) {
				// // System.out.println("Column " + getIndexOfColumn(column)
				// + " pssibilities size:" + column.getPossibilities().size());
				// // // System.out.println(column.getPossibilities());
				columnPosSize *= column.getPossibilities().size();
				// // System.out.println();
			}
			return columnPosSize;
		}
		
		private void getSizesOfPossibilities() {
			// String methodName = "getSizesOfPossibilities()";
			// // // System.out.println(methodName);
			// long startTime = new Date().getTime();
			// long rowPosSize = getPossibillitySizeOfRow();
			// long columnPosSize = getPossibillitySizeOfColumn();
			// // System.out.println("RowPos: " + rowPosSize + "\n" + "ColPos: "
			// + columnPosSize);
			// // System.out.println("Time for " + methodName + ": "
			// + (new Date().getTime() - startTime) + " ms");
		}

		/**
		 * Prüft ob die in {@link #solveByBrutForceByRow(LinkedList, int, int)}
		 * zusammengestellte Matrix korrekt ist.
		 * 
		 * @param returnList
		 * @return
		 */
		private boolean checkStateOfWrittenMatrixByRow(
				ArrayList<ArrayList<String>> returnList) {

			// String methodName = "checkStateOfWrittenMatrix()";
			// // System.out.println(methodName);
			// Date startTime = new Date();
			//
			// // Array anlegen
			// // // System.out.println("XXXXXXXXXXX");
			// for (ArrayList<String> list : returnList) {
			// // // System.out.println(list);
			// }

			// eigentliche Tests:
			int columnInt = 0;
			String empty = "-";
			while (columnInt < riddle.getWidth()) {
				Column column = getColumns().get(columnInt);
				ArrayList<Block> blocks = column.getBlocks();
				if (blocks != null && blocks.size() > 0) {
					int roInt = 0;
					int blockInt = 0;
					while (roInt < riddle.getHeight()) {
						if (returnList.get(roInt).get(columnInt).equals(empty)) {
							roInt++;
						} else {
							// Ist eine Farbe, aber keine Blöcke mehr!
							if (blockInt >= blocks.size()) {
								// // System.out.println("false1: row: " + roInt
								// + " column: " + columnInt);
								return false;
							} else {
								// Block prüfen
								Block block = blocks.get(blockInt);
								for (int i = 0; i < block.getHowMany(); i++) {
									if (!(returnList.get(roInt).get(columnInt)
											.equals(block.getColourString()))) {
										// // System.out.println("false2: row: " +
										// roInt
										// + " column: " + columnInt);
										return false;
									}
								}
								roInt += block.getHowMany();
								// Nächster Block ist gleiche Farbe, also muss -
								// sein!
								if ((blockInt + 1) < blocks.size()
										&& block.getColourString().equals(
												blocks.get(blockInt + 1)
														.getColourString())
										&& !returnList.get(roInt).get(columnInt)
												.equals(empty)) {
									// // System.out.println("false3: row: " + roInt
									// + " column: " + columnInt);
									return false;
								}
								blockInt++;
							}
						}
					}
				} else {
					// nur -!!!
					for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
						if (!returnList.get(rowInt).get(columnInt).equals(empty)) {
							// // System.out.println("false4: row: " + rowInt
							// + " column: " + columnInt);
							return false;
						}
					}
				}
				columnInt++;
			}

			// // System.out.println("YYYYYYYYYYY");
			// showAMatrix(testMatrix);
			// // System.out.println("Time for " + methodName + ": "
			// + (new Date().getTime() - startTime.getTime()) + " ms");
			returnList = null;
			return true;
		}

		/**
		 * Anzahl der Möglichkeiten, die Matrix zu setzen.
		 */
		private static long possibillities = 0;

		/**
		 * Anzahl der bereits überprüften Möglichkeiten (in BrutForce)
		 */
		private static long checkedPossibillities = 0;
		private static long timeFor;
		
		private int getRowWithSmallestSizesOfPossibilities() {
			// String methodName = "getSizesOfPossibilities()";
			// // // System.out.println(methodName);
			// long startTime = new Date().getTime();
			int smallestRowInt = 0;
			Integer sizesmallestRowInt = null;
			for (Row row : getRows()) {
				int size = row.getPossibilities().size();
				if (sizesmallestRowInt == null
						|| (size < sizesmallestRowInt && size != 1)) {
					sizesmallestRowInt = size;
					smallestRowInt = getIndexOfRow(row);
				}
				// // // System.out.println("Row " + getIndexOfRow(row) +
				// " pssibilities size:" + size);
			}
			return smallestRowInt;
		}

		
		
		

		
		


   class Block {

      int start;

      int end;

      int min;

      int max;

   }

   private void start() {

      LinkedList<Block> blocks = new LinkedList<Block>();

      Block block3 = new Block();

      Block block2 = new Block();

      Block block1 = new Block();

      block1.start = 0;

      block1.end = 0;

      block1.min = 0;

      block1.max = 25;

      block2.start = 2;

      block2.end = 2;

      block2.min = 2;

      block2.max = 27;

      block3.start = 4;

      block3.end = 5;

      block3.min = 4;

      block3.max = 29;

      blocks.add(block1);

      blocks.add(block2);

      blocks.add(block3);

      ArrayList<LinkedList<String>> possibilities = new ArrayList<LinkedList<String>>();

      LinkedList<String> aa = new LinkedList<String>();

      aa.add("A");

      aa.add("-");

      aa.add("A");

      aa.add("-");

      aa.add("A");

      aa.add("A");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      System.out.println(aa + "start");

      // possibilities = statisch(block1, block2, block3, possibilities, aa);

      Date date = new Date();

      possibilities = rekursiv(blocks, possibilities, aa, 0, 0);

      System.out.println("-------------");

      System.out.println("took " + (new Date().getTime() - date.getTime()) + " ms");

      System.out.println(possibilities.size());

   }

   private ArrayList<LinkedList<String>> rekursiv(LinkedList<Block> blocks1, ArrayList<LinkedList<String>> possibilities,

   LinkedList<String> aa, int numberOfBlock, int add1) {

      int add = add1;

      LinkedList<Block> blocks = new LinkedList<Block>(blocks1);

      ArrayList<LinkedList<String>> possibilities2 = new ArrayList<LinkedList<String>>(

      possibilities);

      blocks.get(numberOfBlock).start = blocks.get(numberOfBlock).min + add;

      int numberOfBlock2 = numberOfBlock + 1;

      for (int i = blocks.get(numberOfBlock).start; i <= blocks.get(numberOfBlock).max; i++) {

         LinkedList<String> bb = new LinkedList<String>(aa);

         if (numberOfBlock2 < blocks.size()) {

            possibilities2 = rekursiv(blocks, possibilities2, bb, numberOfBlock2, add);

         }

         if (aa.getLast() == "-") {

            aa.removeLast();

            aa.add(blocks.get(numberOfBlock).start, "-");

            possibilities2.add(aa);
            System.out.println(possibilities2);

            System.out.println(aa + "i:" + add);

         }

         blocks.get(numberOfBlock).start++;

         add++;

      }

      return possibilities2;

   }

//   private ArrayList<LinkedList<String>> statisch(Block block1, Block block2,
//
//   Block block3, ArrayList<LinkedList<String>> possibilities,
//
//   LinkedList<String> aa) {
//
//      int add = 0;
//
//      ArrayList<LinkedList<String>> possibilities2 = new ArrayList<LinkedList<String>>(
//
//      possibilities);
//
//      for (int i = block1.start; i <= block1.max; i++) {
//
//         LinkedList<String> bb = new LinkedList<String>(aa);
//
//         block2.start = block2.min + add;
//
//         for (int j = block2.end + add; j < block2.max; j++) {
//
//            LinkedList<String> cc = new LinkedList<String>(bb);
//
//            block3.start = block3.min + add;
//
//            for (int k = block3.end + add; k < block3.max; k++) {
//
//               if (cc.getLast() == "-") {
//
//                  cc.removeLast();
//
//                  cc.add(block3.start, "-");
//
//                  possibilities2.add(cc);
//
//                  System.out.println(cc + "k:" + add);
//
//               }
//
//            }
//
//            if (bb.getLast() == "-") {
//
//               bb.removeLast();
//
//               bb.add(block2.start, "-");
//
//               possibilities2.add(bb);
//
//               System.out.println(bb + "j:" + add);
//
//            }
//
//         }
//
//         if (aa.getLast() == "-") {
//
//            aa.removeLast();
//
//            aa.add(block1.start, "-");
//
//            possibilities2.add(aa);
//
//            System.out.println(aa + "i:" + add);
//
//         }
//
//         block1.start++;
//
//         add++;
//
//      }
//
//      return possibilities2;
//
//   }

}