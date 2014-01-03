package service;

import interfaces.IPlaygame;
import interfaces.IUIListener;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JLabel;

import models.Colour;
import models.Riddle;
import models.SolveStateEnum;

/**
 * Managed den Spielablauf. Die Klasse implementiert das Interface
 * {@link IPlaygame}, über das Methoden dieser Klasse aufgerufen werden können.
 * Die Klasse implementiert über IPlaygame auch die Interfaces ActionListener
 * und MouseListener, so kann auf Ereignisse in der UI reagiert werden. Sie hält
 * mit dem {@link #riddle} und {@link #matrix} die Informationen zu dem Rätsel.
 * Wenn Aktionen in der UI ausgelöst werden, werden sie heir verarbeitet.
 * 
 * @author csgt
 * 
 */
public class PlayGame implements IPlaygame {

   /**
    * Spielmatrix.
    */
   private char[][] matrix;

   /**
    * Colour, die zum Setzten eingestellt ist.
    */
   private Colour currentColor;

   /**
    * Hintergrungfarbe des Rätsels.
    */
   private Colour backGroundColour;

   /**
    * Zu nutzender {@link RiddleService}
    */
   private RiddleService riddleLoader;

   /**
    * Das Rätsel.
    */
   private Riddle riddle;

   /**
    * Verbundene UI.
    */
   private IUIListener listener;

   /**
    * Die Lösung des Rätsels.
    */
   private char[][] solutions;

   /**
    * Konstruktor.
    * 
    * @param listener
    */
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
         setupIt(riddle, riddleLoader.getMatrix());
         return true;
      } catch (Exception e) {
         e.printStackTrace();
         return false;
      }
   }

   @Override
   public void setupIt(Riddle riddle, char[][] matrix) {
      backGroundColour = new Colour();
      backGroundColour.setName('-');
      backGroundColour.setRed(Color.WHITE.getRed());
      backGroundColour.setGreen(Color.WHITE.getGreen());
      backGroundColour.setBlue(Color.WHITE.getBlue());
      this.riddle = riddle;
      System.out.println(riddle);
      this.matrix = matrix;
      // matrix kann null sein!
      if (this.matrix == null) {
         setupMatrix();
      }
      // neues Objekt, da die MAtrix im Nonosolver verändert wird
      char[][] matrixNeu = new char[riddle.getHeight()][riddle.getWidth()];
      for (int i = 0; i < riddle.getHeight(); i++) {
         for (int j = 0; j < riddle.getWidth(); j++) {
            matrixNeu[i][j] = this.matrix[i][j];
         }
      }
      NonoSolver solver = new NonoSolver(matrixNeu, riddle);
      solutions = solver.getSolution();
      if (solver.getSolveState() == SolveStateEnum.SOLVED) {
         listener.setupUIMatrix(riddle.getHeight(), riddle.getWidth(), riddle.getRows(), riddle.getColumns());
         listener.setColours(riddle.getColours());
         // ist nur gleich null, wenn ein Rätsel neu erstellt wird!
         if (matrix != null) {
            for (int i = 0; i < riddle.getHeight(); i++) {
               for (int j = 0; j < riddle.getWidth(); j++) {
                  if (matrix[i][j] != '*' && matrix[i][j] != '-') {
                     listener.placeAField(i, j, riddle.getColourByName(String.valueOf(matrix[i][j])), true);
                  } else if (matrix[i][j] == '-') {
                     listener.placeAField(i, j, backGroundColour, true);
                  }
               }
            }
         } else {
            setupMatrix();
         }
         // Fehler ist aufgetreten
      } else {
         System.out.println(solver.getSolveState());
         switch (solver.getSolveState()) {
         case SOLVING:
            listener.showAlert(SolveStateEnum.SOLVING.getMessage());
            break;
         case MULTIPLE_SOLUTIONS:
            listener.showAlert(SolveStateEnum.MULTIPLE_SOLUTIONS.getMessage());
            // erstes char[][] von solutionsFromGuising wird als Lösung
            // genommen. Abweichende Positionen werden als vorausgefüllte
            // Stellen übernommen, um das Rätsel eindeutig zu machen
            matrix = getDifferences(solver.solutionsFromGuising);
            listener.setupUIMatrix(riddle.getHeight(), riddle.getWidth(), riddle.getRows(), riddle.getColumns());
            listener.setColours(riddle.getColours());
            if (matrix != null) {
               for (int i = 0; i < riddle.getHeight(); i++) {
                  for (int j = 0; j < riddle.getWidth(); j++) {
                     if (matrix[i][j] != '*' && matrix[i][j] != '-') {
                        listener.placeAField(i, j, riddle.getColourByName(String.valueOf(matrix[i][j])), false);
                     } else if (matrix[i][j] == '-') {
                        listener.placeAField(i, j, backGroundColour, false);
                     }
                  }
               }
            }
            break;
         case NO_SOLUTION:
            listener.showAlert(SolveStateEnum.NO_SOLUTION.getMessage());
            break;
         default:
            listener.showAlert("Fehler beim Laden");
            break;
         }
      }

   }

   /**
    * Vergleicht das erste char[][] mit den Restlichen der Liste. Das char[][],
    * das zurück gegeben wird, wird zuerst mit '*' gefüllt. An den Stellen, an
    * denen das erste char[][] von einem anderen char[][] abweicht wird an
    * dieser Stelle der char des ersten Arrays in das zurückgegebene Array
    * geschrieben.
    * 
    * @param solutionsFromGuising
    *           Liste der gefundenen Lösungen für das Rätsel
    * @return char[][], das mit '*' oder an abweichenden Stellen mit dem char
    *         des ersten char[][] gefüllt ist.
    */
   private char[][] getDifferences(ArrayList<char[][]> solutionsFromGuising) {
      char[][] result = new char[riddle.getHeight()][riddle.getWidth()];
      for (int i = 0; i < riddle.getHeight(); i++) {
         for (int j = 0; j < riddle.getWidth(); j++) {
            result[i][j] = '*';
         }
      }
      char[][] solutionOne = solutionsFromGuising.get(0);
      for (int k = 1; k < solutionsFromGuising.size(); k++) {
         char[][] newOne = solutionsFromGuising.get(k);
         for (int i = 0; i < riddle.getHeight(); i++) {
            for (int j = 0; j < riddle.getWidth(); j++) {
               if (newOne[i][j] != solutionOne[i][j]) {
                  result[i][j] = solutionOne[i][j];
               }
            }
         }
      }
      return result;
   }

   @Override
   public Riddle createRiddle(BufferedImage image) {
      this.riddle = riddleLoader.createRiddle(image);
      setupIt(this.riddle, null);
      return this.riddle;
   }

   @Override
   public void actionPerformed(ActionEvent arg0) {
      String actionCommand = arg0.getActionCommand();
      System.out.println(actionCommand);
      if (actionCommand.equals("check")) {
         boolean isRight = checkSolution();
         if (isRight) {
            listener.wasRight(isRight, null);
         } else {
            listener.wasRight(isRight, wrongCoordinates);
         }
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

   String wrongCoordinates;

   /**
    * Prüft, ob das vom User gelöste Rätsel mit der Lösung übereinstimmt. Falls
    * das Rätsel nicht richtig gelöst wurde, werden die Koordinaten von den
    * falschen Feldern im String wrongCoordinates gespeichert.
    * 
    * @return true wenn Lösung richtig ist.
    */
   private boolean checkSolution() {
      boolean isRight = true;
      StringBuilder builder = new StringBuilder();
      for (int row = 0; row < riddle.getHeight(); row++) {
         for (int column = 0; column < riddle.getWidth(); column++) {
            if (matrix[row][column] != solutions[row][column]) {
               if (matrix[row][column] != '*') {
                  if (builder.length() == 0) {
                     builder.append("Fehler an Folgenden Koordinaten:\n");
                  }
                  builder.append("Reihe:" + row + " Spalte:" + column + "\n");
               }
               isRight = false;
            }
         }
      }
      wrongCoordinates = builder.toString();
      return isRight;
   }

   /**
    * Erstellt eine neue Matrix mit der Breite und Höhe des Rätsels und füllt
    * diese mit '*'.
    */
   private void setupMatrix() {
      this.matrix = new char[riddle.getHeight()][riddle.getWidth()];
      for (int i = 0; i < riddle.getHeight(); i++) {
         for (int j = 0; j < riddle.getWidth(); j++) {
            this.matrix[i][j] = '*';
         }
      }
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
         listener.placeAField(row, column, currentColor, true);
      } else {
         matrix[row][column] = '*';
         listener.placeAField(row, column, null, true);
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

}
