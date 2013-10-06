package service;

import java.io.IOException;
import java.util.LinkedList;

import models.ColourBlock;
import models.Column;
import models.Riddle;
import models.Row;
import de.feu.propra.nonogramme.interfaces.INonogramSolver;

public class NonoSolver implements INonogramSolver {

   private RiddleLoader riddleLoader;

   private Riddle riddle;

   public NonoSolver() {

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

      System.out.println("openFile:" + arg0);
      riddleLoader = new RiddleLoader();
      riddle = riddleLoader.readFile(arg0);

   }

   @Override
   public char[][] getSolution() {
      System.out.println("getSolution");
      char[][] matrix = new char[riddle.getHeight()][riddle.getWidth()];

      for (int i = 0; i < riddle.getHeight(); i++) {
         for (int j = 0; j < riddle.getWidth(); j++) {
            matrix[i][j] = '*';
         }
      }

      for (int i = 0; i < 5; i++) {
         matrix = checkColumns(matrix);
         // showMatrix(matrix);
         matrix = checkRows(matrix);
         // showMatrix(matrix);
         matrix = checkRowForMathingBlocks(matrix);
         // showMatrix(matrix);
         matrix = checkColumnForMathingBlocks(matrix);
         // showMatrix(matrix);
         checkBlocksInRowsToBeTTrue(matrix);
         // showMatrix(matrix);
         matrix = checkBeginAndEndOfColumnsForBlocks(matrix);
         // showMatrix(matrix);

         matrix = checkBeginnigOfRowForExactSpaceForFirstBlock(matrix);
         // showMatrix(matrix);

         matrix = checkEndOfRowForExactSpaceForLastBlock(matrix);

         checkBlocksInColumnsToBeTTrue(matrix);
         checkBlocksInRowsToBeTTrue(matrix);
         // matrix = fillReadyRowsAndColumns(matrix);
      }

      showMatrix(matrix);
      return matrix;
   }

   /**
    * Überprüft am Anfang der Row, ob der erste Block genau in die Lücke passt.
    * '-' werden geskipt.
    * 
    * @param matrix
    * @return
    */
   private char[][] checkBeginnigOfRowForExactSpaceForFirstBlock(char[][] matrix) {
      System.out.println("checkBeginnigOfRowForExactSpaceForFirstBlock");
      // showMatrix(matrix);
      // gehe Rows durch
      for (int roInt = 0; roInt < riddle.getHeight(); roInt++) {
         System.out.println("row:" + roInt);
         Row row = riddle.getRows().get(roInt);
         ColourBlock block = null;
         if (row != null && row.getBlocks() != null) {
            block = row.getBlocks().get(0);

            if (null != block && !block.isGone() && block.getHowMany() > 1) {
               int columnInt = 0;
               // vorhangeln bis ersten *
               while (columnInt < riddle.getWidth() && matrix[roInt][columnInt] == '-') {
                  columnInt++;
               }
               if (matrix[roInt][columnInt] == '*') {
                  boolean toFill = true;
                  for (int index = columnInt; index < (columnInt + block.getHowMany()); index++) {
                     if (matrix[roInt][index] != '*' && matrix[roInt][index] != block.getColour().getName().charAt(0)) {
                        toFill = false;
                     }
                  }
                  if (toFill) {
                     if ((columnInt + block.getHowMany()) >= riddle.getWidth() || matrix[roInt][columnInt + block.getHowMany()] == '*') {
                        toFill = false;
                     }
                     for (int index = columnInt; index < riddle.getWidth(); index++) {
                        if (matrix[roInt][index] == block.getColour().getName().charAt(0)) {
                           toFill = false;
                           break;
                        }
                        if (matrix[roInt][index] == '-' || matrix[roInt][index] != '*') {
                           break;
                        }
                     }
                  }
                  if (toFill) {
                     for (int index = columnInt; index < (columnInt + block.getHowMany()); index++) {
                        matrix[roInt][index] = block.getColour().getName().charAt(0);
                        block.setGone(true);
                     }
                  }
               }
            }
         }
      }
      // showMatrix(matrix);
      return matrix;
   }

   /**
    * Überprüft am Anfang der Row, ob der erste Block genau in die Lücke passt.
    * '-' werden geskipt.
    * 
    * @param matrix
    * @return
    */
   private char[][] checkEndOfRowForExactSpaceForLastBlock(char[][] matrix) {
      System.out.println("checkBeginnigOfRowForExactSpaceForFirstBlock");
      // showMatrix(matrix);
      // gehe Rows durch
      for (int roInt = riddle.getWidth() - 1; roInt > -1; roInt--) {
         System.out.println("row:" + roInt);
         Row row = riddle.getRows().get(roInt);
         ColourBlock block = null;
         if (row != null && row.getBlocks() != null) {
            block = row.getBlocks().get(row.getBlocks().size() - 1);

            if (null != block && !block.isGone() && block.getHowMany() > 1) {
               int columnInt = riddle.getWidth() - 1;
               // vorhangeln bis ersten *
               while (columnInt > -1 && matrix[roInt][columnInt] == '-') {
                  columnInt--;
               }
               if (matrix[roInt][columnInt] == '*') {
                  boolean toFill = true;
                  for (int index = columnInt; index > (columnInt - block.getHowMany()); index--) {
                     if (matrix[roInt][index] != '*' && matrix[roInt][index] != block.getColour().getName().charAt(0)) {
                        toFill = false;
                     }
                  }
                  if (toFill) {
                     if ((columnInt - block.getHowMany()) < 0 || matrix[roInt][columnInt - block.getHowMany()] == '*') {
                        toFill = false;
                     }
                     for (int index = columnInt; index > columnInt - block.getHowMany(); index--) {
                        if (matrix[roInt][index] == block.getColour().getName().charAt(0)) {
                           toFill = false;
                           break;
                        }
                        if (matrix[roInt][index] == '-' || matrix[roInt][index] != '*') {
                           break;
                        }
                     }
                  }
                  if (toFill) {
                     for (int index = columnInt; index > (columnInt - block.getHowMany()); index--) {
                        matrix[roInt][index] = block.getColour().getName().charAt(0);
                        block.setGone(true);
                     }
                  }
               }
            }
         }
      }
      // showMatrix(matrix);
      return matrix;
   }

   /**
    * Überprüft den Anfang und das Ende der Columns, ob ein angefangener Block
    * eingetragen ist.
    * 
    * @param matrix
    * @return
    */
   private char[][] checkBeginAndEndOfColumnsForBlocks(char[][] matrix) {
      System.out.println("checkBeginAndEndOfColumnsForBlocks");
      for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
         Column column = riddle.getColumns().get(columnInt);
         if (matrix[0][columnInt] != '-' && matrix[0][columnInt] != '*') {
            if (column.getBlocks() != null && column.getBlocks().get(0).getColour().getName().charAt(0) == matrix[0][columnInt]) {
               for (int index = 0; index < column.getBlocks().get(0).getHowMany(); index++) {
                  matrix[index][columnInt] = column.getBlocks().get(0).getColour().getName().charAt(0);
               }
               column.getBlocks().get(0).setGone(true);
            }
         }

         if (matrix[riddle.getHeight() - 1][columnInt] != '-' && matrix[riddle.getHeight() - 1][columnInt] != '*') {
            if (column.getBlocks() != null && column.getBlocks().get(column.getBlocks().size() - 1).getColour().getName().charAt(0) == matrix[riddle.getHeight() - 1][columnInt]) {
               for (int index = riddle.getHeight() - 1; index > riddle.getHeight() - column.getBlocks().get(column.getBlocks().size() - 1).getHowMany() - 1; index--) {
                  matrix[index][columnInt] = column.getBlocks().get(column.getBlocks().size() - 1).getColour().getName().charAt(0);
               }
               column.getBlocks().get(column.getBlocks().size() - 1).setGone(true);
            }
         }

      }
      return matrix;
   }

   /**
    * Übrtprüft, ob es Blöcke gibt, die vollständig sind, aber nicht gone=true
    * sind.
    * 
    * @param matrix
    */
   private void checkBlocksInColumnsToBeTTrue(char[][] matrix) {
      for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
         Column column = riddle.getColumns().get(columnInt);
         int filledRowsInColumn = 0;
         int blocksLengthCount = 0;
         for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
            if (matrix[rowInt][columnInt] != '*' && matrix[rowInt][columnInt] != '-') {
               filledRowsInColumn++;
            }
         }
         if (column != null && column.getBlocks() != null) {
            for (ColourBlock block : column.getBlocks()) {
               blocksLengthCount += block.getHowMany();
            }
         }
         if (filledRowsInColumn == blocksLengthCount) {
            for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
               if (matrix[rowInt][columnInt] == '*') {
                  matrix[rowInt][columnInt] = '-';
               }
            }
         }
      }
   }

   /**
    * Übrtprüft, ob es Blöcke gibt, die vollständig sind, aber nicht gone=true
    * sind.
    * 
    * @param matrix
    */
   private void checkBlocksInRowsToBeTTrue(char[][] matrix) {
      for (int roInt = 0; roInt < riddle.getHeight(); roInt++) {
         Row row = riddle.getRows().get(roInt);
         int filledColumnsInRow = 0;
         int blocksLengthCount = 0;
         for (int columnInt = 0; columnInt < riddle.getHeight(); columnInt++) {
            if (matrix[roInt][columnInt] != '*' && matrix[roInt][columnInt] != '-') {
               filledColumnsInRow++;
            }
         }
         if (row != null && row.getBlocks() != null) {
            for (ColourBlock block : row.getBlocks()) {
               blocksLengthCount += block.getHowMany();
            }
         }
         if (filledColumnsInRow == blocksLengthCount) {
            for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
               if (matrix[roInt][columnInt] == '*') {
                  matrix[roInt][columnInt] = '-';
               }
            }
            if (row != null && row.getBlocks() != null) {
               for (ColourBlock block : row.getBlocks()) {
                  block.setGone(true);
               }
            }
         }
      }
   }

   /**
    * Überprüft, ob alle Blöcke true sind. Falls ja, werden "*" mit "-" ersetzt.
    * 
    * @param matrix
    * @return
    */
   private char[][] fillReadyRowsAndColumns(char[][] matrix) {
      for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
         Row row = riddle.getRows().get(rowInt);
         boolean toFill = true;
         if (null != row && null != row.getBlocks()) {
            for (ColourBlock block : row.getBlocks()) {
               if (!block.isGone()) {
                  toFill = false;
               }
            }
         }
         if (toFill) {
            for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
               if (matrix[rowInt][columnInt] == '*') {
                  matrix[rowInt][columnInt] = '-';
               }
            }
         }
      }

      for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
         Column column = riddle.getColumns().get(columnInt);
         boolean toFill = true;
         if (null != column && null != column.getBlocks()) {
            for (ColourBlock block : column.getBlocks()) {
               if (!block.isGone()) {
                  toFill = false;
               }
            }
         }
         if (toFill) {
            for (int rowInt = 0; rowInt < riddle.getWidth(); rowInt++) {
               if (matrix[rowInt][columnInt] == '*') {
                  matrix[rowInt][columnInt] = '-';
               }
            }
         }
      }

      return matrix;
   }

   /**
    * 
    * @param matrix
    * @return
    */
   private char[][] checkRowForMathingBlocks(char[][] matrix) {
      System.out.println("checkRowForMathingBlocks");
      for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
         Row row = riddle.getRows().get(rowInt);
         int count = 0;
         // auch wenn nur nocj block flase?
         for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
            if (matrix[rowInt][columnInt] == '-' && (columnInt == 0 || columnInt == (riddle.getWidth() - 1))) {
               count++;
            }
         }
         if (null != row && null != row.getBlocks()) {
            for (int blockInt = 0; blockInt < row.getBlocks().size(); blockInt++) {
               ColourBlock block = row.getBlocks().get(blockInt);
               count += block.getHowMany();
               if ((blockInt + 1) < row.getBlocks().size() && block.getColour().getName().equals(row.getBlocks().get(blockInt + 1).getColour().getName())) {
                  count++;
               }
            }
         }
         if (count == riddle.getHeight()) {
            matrix = fillOutRow(rowInt, matrix);
         }
      }
      return matrix;
   }

   private char[][] fillOutRow(int rowInt, char[][] matrix) {
      System.out.println("fillOutRow");
      Row row = riddle.getRows().get(rowInt);
      LinkedList<ColourBlock> blocks = row.getBlocks();
      LinkedList<ColourBlock> newBlocks = null;
      for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
         if (matrix[rowInt][columnInt] == '-') {

         } else {
            newBlocks = new LinkedList<ColourBlock>();
            for (int blockInt = 0; blockInt < blocks.size(); blockInt++) {
               ColourBlock block = row.getBlocks().get(blockInt);
               newBlocks.add(block);
               if (block.isGone()) {
                  newBlocks.remove(block);
                  columnInt += block.getHowMany();
               } else {
                  for (int k = columnInt; k < (columnInt + block.getHowMany()); k++) {
                     matrix[rowInt][k] = block.getColour().getName().charAt(0);
                  }
                  block.setGone(true);
                  newBlocks.remove(block);
                  columnInt += block.getHowMany();
               }
               if ((blockInt + 1) < row.getBlocks().size() && block.getColour().getName().equals(row.getBlocks().get(blockInt + 1).getColour().getName())) {
                  matrix[rowInt][columnInt] = '-';
                  columnInt++;
               }
            }
            blocks = newBlocks;
         }
      }
      return matrix;
   }

   private char[][] checkColumnForMathingBlocks(char[][] matrix) {
      System.out.println("checkColumnForMathingBlocks");
      for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
         Column column = riddle.getColumns().get(columnInt);
         int count = 0;
         for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
            if (column.getBlocks() != null) {
               if (matrix[rowInt][columnInt] == '-' && (column.getBlocks().size() == 1 || (rowInt == 0 || rowInt == (riddle.getHeight() - 1)))) {
                  count++;
               }
            }
         }
         if (null != column && null != column.getBlocks()) {
            for (int blockInt = 0; blockInt < column.getBlocks().size(); blockInt++) {
               ColourBlock block = column.getBlocks().get(blockInt);
               count += block.getHowMany();
               if ((blockInt + 1) < column.getBlocks().size() && block.getColour().getName().equals(column.getBlocks().get(blockInt + 1).getColour().getName())) {
                  count++;
               }
            }
         }
         if (count == riddle.getWidth()) {
            matrix = fillOutColumn(columnInt, matrix);
         }
      }
      return matrix;
   }

   private char[][] fillOutColumn(int columnInt, char[][] matrix) {
      System.out.println("fillOutColumn");
      System.out.println(columnInt);
      Column column = riddle.getColumns().get(columnInt);
      LinkedList<ColourBlock> blocks = column.getBlocks();
      LinkedList<ColourBlock> newBlocks = null;
      for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
         if (matrix[rowInt][columnInt] == '-') {

         } else {
            newBlocks = new LinkedList<ColourBlock>();
            for (int blockInt = 0; blockInt < blocks.size(); blockInt++) {
               ColourBlock block = column.getBlocks().get(blockInt);
               newBlocks.add(block);
               if (block.isGone()) {
                  newBlocks.remove(block);
                  rowInt += block.getHowMany();
               } else {
                  for (int k = rowInt; k < (rowInt + block.getHowMany()); k++) {
                     matrix[k][columnInt] = block.getColour().getName().charAt(0);
                  }
                  block.setGone(true);
                  newBlocks.remove(block);
                  rowInt += block.getHowMany();
               }
               if ((blockInt + 1) < column.getBlocks().size() && block.getColour().getName().equals(column.getBlocks().get(blockInt + 1).getColour().getName())) {
                  matrix[rowInt][columnInt] = '-';
                  rowInt++;
               }
            }
            blocks = newBlocks;
         }
      }
      return matrix;
   }

   private void showMatrix(char[][] matrix) {
      for (int i = 0; i < riddle.getHeight(); i++) {
         String out = "";
         for (int j = 0; j < riddle.getWidth(); j++) {
            out += matrix[i][j];
         }
         System.out.println(out);
      }
      System.out.println();
   }

   /**
    * Überprüft, ob es überhaupt Blöcke in der Column gibt. Wenn nicht wird die
    * Column mit "-" aufgefüllt. Wenn ja, dann wird die Größe der Blöcke
    * aufaddiert (wenn gleich Farben aufeinanderfolgen wird zusätzlich 1
    * addiert. Wenn dann die Anzahl = der Breite ist, werden die Blöcke in die
    * Matrix übertragen (in fillColumn()).
    * 
    * @param matrix
    * @return
    */
   private char[][] checkColumns(char[][] matrix) {
      System.out.println("checkColumns");
      for (int j = 0; j < riddle.getColumns().size(); j++) {
         Column column = riddle.getColumns().get(j);
         int count = 0;
         if (column != null && column.getBlocks() != null) {
            ColourBlock blockBefore = null;
            for (ColourBlock block : column.getBlocks()) {
               if (blockBefore != null && blockBefore.getColour().getName().equals(block.getColour().getName())) {
                  count++;
               }
               blockBefore = block;
               count += block.getHowMany();
            }
            if (count == riddle.getHeight()) {
               matrix = fillColumn(j, column, matrix);
            }
         } else {
            matrix = fillColumnWithEmpty(j, matrix);

         }
      }
      return matrix;
   }

   /**
    * Überprüft, ob es überhaupt Blöcke in der Row gibt. Wenn nicht wird die Row
    * mit "-" aufgefüllt. Wenn ja, dann wird die Größe der Blöcke aufaddiert
    * (wenn gleich Farben aufeinanderfolgen wird zusätzlich 1 addiert. Wenn dann
    * die Anzahl = der Breite ist, werden die Blöcke in die Matrix übertragen
    * (in fillRow()).
    * 
    * @param matrix
    * @return
    */
   private char[][] checkRows(char[][] matrix) {
      System.out.println("checkRows");
      for (int j = 0; j < riddle.getRows().size(); j++) {
         Row row = riddle.getRows().get(j);
         int count = 0;
         if (row != null && row.getBlocks() != null) {
            ColourBlock blockBefore = null;
            for (ColourBlock block : row.getBlocks()) {
               if (blockBefore != null && blockBefore.getColour().getName().equals(block.getColour().getName())) {
                  count++;
               }
               blockBefore = block;
               count += block.getHowMany();
            }
            if (count == riddle.getHeight()) {
               matrix = fillRow(j, row, matrix);
            }
         } else {
            matrix = fillRowWithEmpty(j, matrix);
         }
      }
      return matrix;
   }

   /**
    * Füllt die komplette Row mit "-".
    * 
    * @param j
    * @param matrix
    * @return
    */
   private char[][] fillRowWithEmpty(int j, char[][] matrix) {
      System.out.println("fillRowWithEmpty");
      for (int i = 0; i < riddle.getWidth(); i++) {
         if (matrix[j][i] == '*') {
            matrix[j][i] = '-';
         }
      }
      return matrix;
   }

   /**
    * Füllt die komplette Column mit "-".
    * 
    * @param j
    * @param matrix
    * @return
    */
   private char[][] fillColumnWithEmpty(int j, char[][] matrix) {
      System.out.println("fillColumnWithEmpty(" + j + ")");
      for (int i = 0; i < riddle.getWidth(); i++) {
         if (matrix[i][j] == '*') {
            matrix[i][j] = '-';
         }
      }
      return matrix;
   }

   /**
    * Füllt die Column mit den Blöcken und etwaigen "-",
    * 
    * @param number
    * @param column
    * @param matrix
    * @return
    */
   private char[][] fillColumn(int number, Column column, char[][] matrix) {
      char[][] newMatrix = matrix;

      int row = 0;
      ColourBlock lastBlock = null;
      for (ColourBlock block : column.getBlocks()) {
         block.setGone(true);
         if (lastBlock != null && lastBlock.getColour().getName().equals(block.getColour().getName())) {
            matrix[row][number] = '-';
            row++;
         }
         for (int times = 0; times < block.getHowMany(); times++) {
            matrix[row][number] = block.getColour().getName().charAt(0);
            row++;
         }
         lastBlock = block;
      }
      System.out.println(newMatrix);
      return newMatrix;
   }

   /**
    * Füllt die Row mit den Blöcken und etwaigen "-",
    * 
    * @param number
    * @param column
    * @param matrix
    * @return
    */
   private char[][] fillRow(int number, Row row, char[][] matrix) {
      char[][] newMatrix = matrix;

      int column = 0;
      ColourBlock lastBlock = null;
      for (ColourBlock block : row.getBlocks()) {
         block.setGone(true);
         if (lastBlock != null && lastBlock.getColour().getName().equals(block.getColour().getName())) {
            matrix[number][column] = '-';
            column++;
         }
         for (int times = 0; times < block.getHowMany(); times++) {
            matrix[number][column] = block.getColour().getName().charAt(0);
            column++;
         }
         lastBlock = block;
      }
      System.out.println(newMatrix);
      return newMatrix;
   }

}
