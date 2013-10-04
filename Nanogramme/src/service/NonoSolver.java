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
   public char[][] getSolution() {
      System.out.println("getSolution");
      char[][] matrix = new char[riddle.getHeight()][riddle.getWidth()];

      for (int i = 0; i < riddle.getHeight(); i++) {
         for (int j = 0; j < riddle.getWidth(); j++) {
            matrix[i][j] = '*';
         }
      }

      for (int i = 0; i < 3; i++) {
         matrix = checkColumns(matrix);
         showMatrix(matrix);
         matrix = checkRows(matrix);
         showMatrix(matrix);
         matrix = checkRowsForSpaceBetweenColour(matrix);
         showMatrix(matrix);
         matrix = checkColumnsForSpaceBetweenColour(matrix);
         showMatrix(matrix);
         matrix = checkRowForMathingBlocks(matrix);
         showMatrix(matrix);
         matrix = checkColumnForMathingBlocks(matrix);
         showMatrix(matrix);
         checkBlocksInColumnsToBeTTrue(matrix);
         checkBlocksInRowsToBeTTrue(matrix);
         matrix = fillReadyRowsAndColumns(matrix);
         showMatrix(matrix);
      }
      
      
//      for (int i = 0; i < riddle.getHeight(); i++) {
//         System.out.println("Row:" + i);
//        Row row = riddle.getRows().get(i);
//        boolean ready = true;
//        if (row != null && row.getBlocks() != null) {
//           for (ColourBlock block : row.getBlocks()) {
//               System.out.println(block.getColour() + " " +
//               block.getHowMany() + " " + block.isGone());
//              if (!block.isGone()) {
//                 ready = false;
//              }
//           }
//        }
//        if (ready) {
//           matrix = fillRowWithEmpty(i, matrix);
//        }
//     }
//      
//      for (int i = 0; i < riddle.getWidth(); i++) {
//         System.out.println("Column:" + i);
//        Column column = riddle.getColumns().get(i);
//        boolean ready = true;
//        if (column != null && column.getBlocks() != null) {
//           for (ColourBlock block : column.getBlocks()) {
//               System.out.println(block.getColour() + " " +
//               block.getHowMany() + " " + block.isGone());
//              if (!block.isGone()) {
//                 ready = false;
//              }
//           }
//        }
//        if (ready) {
//           matrix = fillRowWithEmpty(i, matrix);
//        }
//     }

      showMatrix(matrix);
      return matrix;
   }

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
         }
      }
   }

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

   private char[][] checkRowForMathingBlocks(char[][] matrix) {
      System.out.println("checkRowForMathingBlocks");
      for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
         Row row = riddle.getRows().get(rowInt);
         int count = 0;
         for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
            if (matrix[rowInt][columnInt] == '-') {
               count++;
            }
         }
         if (null != row && null != row.getBlocks()) {
            for (int blockInt = 0; blockInt < row.getBlocks().size(); blockInt++) {
               ColourBlock block = row.getBlocks().get(blockInt);
               count += block.getHowMany();
               if ((blockInt+1) < row.getBlocks().size() && block.getColour().getName().equals(row.getBlocks().get(blockInt+1).getColour().getName())) {
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
               if ((blockInt+1) < row.getBlocks().size() && block.getColour().getName().equals(row.getBlocks().get(blockInt+1).getColour().getName())) {
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
            if (matrix[rowInt][columnInt] == '-') {
               count++;
            }
         }
         if (null != column && null != column.getBlocks()) {
            for (int blockInt = 0; blockInt < column.getBlocks().size(); blockInt++) {
               ColourBlock block = column.getBlocks().get(blockInt);
               count += block.getHowMany();
               if ((blockInt+1) < column.getBlocks().size() && block.getColour().getName().equals(column.getBlocks().get(blockInt+1).getColour().getName())) {
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
      System.out.println("fillOutRow");
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
               if ((blockInt+1) < column.getBlocks().size() && block.getColour().getName().equals(column.getBlocks().get(blockInt+1).getColour().getName())) {
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

   private char[][] checkRowsForSpaceBetweenColour(char[][] matrix) {
      System.out.println("checkRowsForSpaceBetweenColour");
      for (int j = 0; j < riddle.getRows().size(); j++) {
         // System.out.println("Row:" + j);
         Row row = riddle.getRows().get(j);
         if (row != null && row.getBlocks() != null) {
            for (int i = 0; i < riddle.getColumns().size(); i++) {
               for (ColourBlock block : row.getBlocks()) {
                  // System.out.println(block.getColour() + " " +
                  // block.getHowMany() + " " + block.isGone());
                  if (!block.isGone() && block.getHowMany() > 2) {
                     if (String.valueOf(matrix[j][i]).equals(block.getColour().getName())) {
                        if ((i + block.getHowMany()) < riddle.getColumns().size() && String.valueOf(matrix[j][(i + block.getHowMany() - 1)]).equals(block.getColour().getName())) {
                           block.setGone(true);
                           for (int k = i; k < (i + block.getHowMany() - 1); k++) {
                              matrix[j][k] = block.getColour().getName().charAt(0);
                           }
                        }
                     }
                  }
                  // System.out.println(block.getColour() + " " +
                  // block.getHowMany() + " " + block.isGone());
               }
            }
         }
      }
      return matrix;
   }

   private char[][] checkColumnsForSpaceBetweenColour(char[][] matrix) {
      System.out.println("checkColumnsForSpaceBetweenColour");
      for (int j = 0; j < riddle.getColumns().size(); j++) {
         // System.out.println("Row:" + j);
         Column column = riddle.getColumns().get(j);
         if (column != null && column.getBlocks() != null) {
            for (int i = 0; i < riddle.getRows().size(); i++) {
               for (ColourBlock block : column.getBlocks()) {
                  // System.out.println(block.getColour() + " " +
                  // block.getHowMany() + " " + block.isGone());
                  if (!block.isGone() && block.getHowMany() > 2) {
                     if (String.valueOf(matrix[i][j]).equals(block.getColour().getName())) {
                        if ((i + block.getHowMany()) < riddle.getRows().size() && String.valueOf(matrix[(i + block.getHowMany() - 1)][j]).equals(block.getColour().getName())) {
                           block.setGone(true);
                           for (int k = i; k < (i + block.getHowMany() - 1); k++) {
                              matrix[k][j] = block.getColour().getName().charAt(0);
                           }
                        }
                     }
                  }
                  // System.out.println(block.getColour() + " " +
                  // block.getHowMany() + " " + block.isGone());
               }
            }
         }
      }
      return matrix;
   }

   private char[][] fillRowWithEmpty(int j, char[][] matrix) {
      System.out.println("fillRowWithEmpty");
      for (int i = 0; i < riddle.getWidth(); i++) {
         if (matrix[j][i] == '*') {
            matrix[j][i] = '-';
         }
      }
      return matrix;
   }

   private char[][] fillColumnWithEmpty(int j, char[][] matrix) {
      System.out.println("fillColumnWithEmpty");
      for (int i = 0; i < riddle.getWidth(); i++) {
         if (matrix[i][j] == '*') {
            matrix[i][j] = '-';
         }
      }
      return matrix;
   }

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

   @Override
   public void openFile(String arg0) throws IOException {

      System.out.println("openFile:" + arg0);
      riddleLoader = new RiddleLoader();
      riddle = riddleLoader.readFile(arg0);

   }

}
