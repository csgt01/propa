package models;


/**
 * Klasse, um den Zustand beim "Raten" der Lösung zu speichern.
 * 
 * @author cschulte
 * 
 */
public class StackHolder {

	/**
	 * Spaltenindex des Stackholders.
	 */
	private int column;
	/**
	 * Reihenindex des Stackholders.
	 *
	 */
	private int row;
	
	/**
	 * Index der Farbe.
    *
	 */
	private int indexOfColor;

	/**
	 * Das {@link Riddle} zum Zeitpunkt des Ratens.
	 */
	private Riddle riddle;
	/**
	 * Die Matrix zum Zeitpunkt des Ratens.
	 */
	private char[][] matrix;

	/**
	 * @return Spaltenindex.
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Spaltenindex setzen.
	 * 
	 * @param column
	 *            Spaltenindex.
	 */
	public void setColumn(int column) {
		this.column = column;
	}

	/**
	 * @return Spaltenindex.
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Reihenindex setzen.
	 * 
	 * @param row
	 *            Reihenindex.
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * 
	 * @return Index der Farbe.
	 */
	public int getIndexOfColor() {
		return indexOfColor;
	}

	/**
	 * Index der Farbe setzen.
	 * 
	 * @param indexOfColor
	 *            Index
	 */
	public void setIndexOfColor(int indexOfColor) {
		this.indexOfColor = indexOfColor;
	}

	/**
	 * 
	 * @return Rätsel zum Zeitpunkt des Ratens.
	 */
	public Riddle getRiddle() {
		return riddle;
	}

	/**
	 * Setzt das Rätsel.
	 * 
	 * @param riddle
	 *            .
	 */
	public void setRiddle(Riddle riddle) {
		this.riddle = new Riddle(riddle.getColours(), riddle.getWidth(),
				riddle.getHeight(), riddle.getRows(), riddle.getColumns(),
				riddle.getNono());
	}

	/**
	 * Gibt die Matrix zum Zeitpunkt des Ratens zurück.
	 * 
	 * @return .
	 */
	public char[][] getMatrix() {
		return matrix;
	}

	/**
	 * Setzt die Matrix.
	 * 
	 * @param matrix
	 *            original MAtrix
	 * @param height
	 *            Höhe
	 * @param width
	 *            Breite
	 */
	public void setMatrix(char[][] matrix, int height, int width) {
		this.matrix = new char[height][width];
//		System.out.println("setmatrix");
		// String str = "";
		for (int i = 0; i < height; i++) {
			// str += "\n";
			for (int j = 0; j < width; j++) {
				// str += matrix[i][j] + " ";
				this.matrix[i][j] = matrix[i][j];
			}
		}
	}

}
