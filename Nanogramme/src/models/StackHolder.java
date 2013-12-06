package models;

/**
 * Klasse, um den Zustand beim "Raten" der Lösung zu speichern.
 * @author cschulte
 *
 */
public class StackHolder {

	private int column;
	private int row;
	private int indexOfColor;
	private Riddle riddle;
	private char[][] matrix;

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getIndexOfColor() {
		return indexOfColor;
	}

	public void setIndexOfColor(int indexOfColor) {
		this.indexOfColor = indexOfColor;
	}

	public Riddle getRiddle() {
		return riddle;
	}

	public void setRiddle(Riddle riddle) {
		this.riddle = 
				new Riddle(riddle.getColours(), riddle.getWidth(),
				riddle.getHeight(), riddle.getRows(), riddle.getColumns(),
				riddle.getNono());
	}

	public char[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(char[][] matrix, int height, int width) {
		this.matrix = new char[height][width];
		System.out.println("setmatrix");
//		String str = "";
		for (int i = 0; i < height; i++) {
//			str += "\n";
			for (int j = 0; j < width; j++) {
//				str += matrix[i][j] + " ";
				this.matrix[i][j] = matrix[i][j];
			}
		}
//		System.out.println(str);
	}

}
