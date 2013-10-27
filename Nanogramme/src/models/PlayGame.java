package models;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Date;

import service.RiddleLoader;

public class PlayGame implements ActionListener {
   
   private char[][] matrix;
   
   private Colour currentColor;
   
   private RiddleLoader riddleLoader;

   private Riddle riddle;
   
   private IPlayGame listener;
   
   public PlayGame(IPlayGame listener) {
      this.listener = listener;
   }

   public void openFile(String arg0) throws IOException {
      String methodName = "openFile(" + arg0 + ")";
      // // System.out.println(methodName);
      riddleLoader = new RiddleLoader();
      riddle = riddleLoader.readFile(arg0);
   }

   @Override
   public void actionPerformed(ActionEvent arg0) {
      String actionCommand = arg0.getActionCommand();
      int i = Integer.valueOf(actionCommand);
      System.out.println(actionCommand);
      int row = i/7;
      System.out.println(row);
      int column = i % 7;
      System.out.println(column);
      
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
        System.out.println("Time for " + methodName + ": " + (new
       Date().getTime() - startTime) + " ms");
   }
   
   private void writeCharInMatrix(int row, int column) {
      matrix[row][column] = currentColor.getName();
   }

}
