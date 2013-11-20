package models;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Date;

import javax.swing.JLabel;

import service.NonoSolver3;
import service.RiddleService;

public class PlayGame implements ActionListener, MouseListener {

	private char[][] matrix;

	private Colour currentColor;

	private Colour backGroundColour;

	private RiddleService riddleLoader;

	private Riddle riddle;

	private IPlayGame listener;

	private char[][] solutions;

	public PlayGame(IPlayGame listener) {
		this.listener = listener;
	}

	public void openFile(String arg0) throws IOException {
		String methodName = "openFile(" + arg0 + ")";
		// // System.out.println(methodName);
		riddleLoader = new RiddleService();
		riddle = riddleLoader.readFile(arg0);
		backGroundColour = new Colour();
		backGroundColour.setName('-');
		backGroundColour.setRed(Color.WHITE.getRed());
		backGroundColour.setGreen(Color.WHITE.getGreen());
		backGroundColour.setBlue(Color.WHITE.getBlue());
		setupMatrix();
		listener.setupMatrix(riddle.getHeight(), riddle.getWidth(),
				riddle.getRows(), riddle.getColumns());
		char[][] matrixNeu = new char[riddle.getHeight()][riddle.getWidth()];
		for (int i = 0; i < riddle.getHeight(); i++) {
			for (int j = 0; j < riddle.getWidth(); j++) {
				matrixNeu[i][j] = matrix[i][j];
			}
		}
		NonoSolver3 solver = new NonoSolver3(matrixNeu, riddle);
		solutions = solver.getSolution();
		listener.setColours(riddle.getColours());
		matrix = riddleLoader.matrix;
		for (int i = 0; i < riddle.getHeight(); i++) {
			for (int j = 0; j < riddle.getWidth(); j++) {
				if (matrix[i][j] != '*' && matrix[i][j] != '-') {
					listener.placeAField(i, j, riddle.getColourByName(String
							.valueOf(matrix[i][j])));
				} else if (matrix[i][j] == '-') {
					listener.placeAField(i, j, backGroundColour);
				}
			}
		}
		// listener.setLeftPAnel(riddle.getRows());
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String actionCommand = arg0.getActionCommand();
		System.out.println(actionCommand);
		if (actionCommand.equals("check")) {
			boolean isRight = checkSolution();
			listener.wasRight(isRight);
		} else if (actionCommand.equals("Reset")) {
			currentColor = null;
		} else if (actionCommand.equals("Speichern")) {
			riddleLoader.save(matrix);
		} else if (actionCommand.equals("-")) {
			currentColor = backGroundColour;
		} else {
			currentColor = riddle.getColourByName(actionCommand);
			System.out.println(currentColor);
		}
	}

	private boolean checkSolution() {
		boolean isRight = true;
		for (int row = 0; row < riddle.getHeight(); row++) {
			for (int column = 0; column < riddle.getWidth(); column++) {
				if (matrix[row][column] != solutions[row][column]) {
					isRight = false;
				}
			}
		}
		showMatrix();
		return isRight;
	}

	/**
	 * Erstellt eine neue Matrix mit der Breite und Höhe des Rätsels und füllt
	 * diese mit '*'.
	 */
	private void setupMatrix() {
		String methodName = "setupBlocks()";
		// // System.out.println(methodName);
		long startTime = new Date().getTime();
		matrix = new char[riddle.getHeight()][riddle.getWidth()];
		for (int i = 0; i < riddle.getHeight(); i++) {
			for (int j = 0; j < riddle.getWidth(); j++) {
				matrix[i][j] = '*';
			}
		}
		System.out.println("Time for " + methodName + ": "
				+ (new Date().getTime() - startTime) + " ms");
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		String actionCommand = ((JLabel) e.getSource()).getText();
		System.out.println(actionCommand);
		String[] splits = actionCommand.split("--");
		int row = Integer.valueOf(splits[0]);
		System.out.println(row);
		int column = Integer.valueOf(splits[1]);
		System.out.println(column);
		if (null != currentColor) {
			matrix[row][column] = currentColor.getName();
			showMatrix();
			listener.placeAField(row, column, currentColor);
		} else {
			matrix[row][column] = '*';
			showMatrix();
			listener.placeAField(row, column, null);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private void showMatrix() {
		String methodName = "showMatrix()";
		System.out.println(methodName);
		long startTime = new Date().getTime();
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
		System.out.println("Time for " + methodName + ": "
				+ (new Date().getTime() - startTime) + " ms");
	}
}
