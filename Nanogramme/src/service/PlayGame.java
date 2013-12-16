package service;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Date;

import javax.swing.JLabel;

import models.Colour;
import models.Riddle;
import Interfaces.IPlaygame;
import Interfaces.IUIListener;

public class PlayGame implements IPlaygame {

	private char[][] matrix;

	private Colour currentColor;

	private Colour backGroundColour;

	private RiddleService riddleLoader;

	private Riddle riddle;

	private IUIListener listener;

	private char[][] solutions;

	public PlayGame(IUIListener listener) {
		this.listener = listener;
		riddleLoader = new RiddleService(listener);
	}

	@Override
	public boolean openRiddleFromFile(String arg0) {
		try {
			String methodName = "openFile(" + arg0 + ")";
			System.out.println(methodName);
			riddleLoader = new RiddleService(listener);
			riddle = riddleLoader.readFile(arg0);
			setupIt(riddle);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public void setupIt(Riddle riddle) {
	   backGroundColour = new Colour();
      backGroundColour.setName('-');
      backGroundColour.setRed(Color.WHITE.getRed());
      backGroundColour.setGreen(Color.WHITE.getGreen());
      backGroundColour.setBlue(Color.WHITE.getBlue());
      this.riddle = riddle;
      setupMatrix();
      char[][] matrixNeu = new char[riddle.getHeight()][riddle.getWidth()];
      for (int i = 0; i < riddle.getHeight(); i++) {
         for (int j = 0; j < riddle.getWidth(); j++) {
            matrixNeu[i][j] = matrix[i][j];
         }
      }
      NonoSolver3 solver = new NonoSolver3(matrixNeu, riddle);
      solutions = solver.getSolution();
      if (solutions != null) {
         listener.setupUIMatrix(riddle.getHeight(), riddle.getWidth(),
               riddle.getRows(), riddle.getColumns());
         listener.setColours(riddle.getColours());
         matrix = riddleLoader.matrix;
         // ist nur gleich null, wenn ein Rätsel neu erstellt wird!
         if (matrix != null) {
            for (int i = 0; i < riddle.getHeight(); i++) {
               for (int j = 0; j < riddle.getWidth(); j++) {
                  if (matrix[i][j] != '*' && matrix[i][j] != '-') {
                     listener.placeAField(i, j, riddle
                           .getColourByName(String
                                 .valueOf(matrix[i][j])));
                  } else if (matrix[i][j] == '-') {
                     listener.placeAField(i, j, backGroundColour);
                  }
               }
            }
         } else {
            setupMatrix();
         }
      } else {
          switch (solver.getSolveState()) {
          case 0:
          listener.showAlert("Fehler beim Laden");
          break;
          case 2:
          listener.showAlert("Dieses Rätsel hat mehr als eine Lösung!");
          break;
          case 3:
          listener.showAlert("Dieses Rätsel hat keine Lösung!");
          break;
          default:
          listener.showAlert("Fehler beim Laden");
          break;
          }
      }
	   
	}

	@Override
	public Riddle createRiddle(BufferedImage image) {
	   this.riddle = riddleLoader.createRiddle(image);
		setupIt(this.riddle);
		return this.riddle;
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
			boolean saved = riddleLoader.save(matrix);
			if (!saved) {
				listener.showAlert("Speichern fehlgeschlagen!");
			}
		} else if (actionCommand.equals("-")) {
			currentColor = backGroundColour;
		} else {
			currentColor = riddle.getColourByName(actionCommand);
			System.out.println(currentColor);
		}
	}

	/**
	 * Prüft, ob das vom User gelöste Rätsel mit der Lösung übereinstimmt. TODO:
	 * Zwischenprüfung! und vielleicht Fehler anzeigen
	 * 
	 * @return
	 */
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
			System.out.println(matrix[row][column]);
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

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

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
