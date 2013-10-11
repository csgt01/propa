package service;

import java.io.IOException;
import java.util.LinkedList;

import models.ColourBlock;
import models.Column;
import models.Riddle;
import models.Row;
import de.feu.propra.nonogramme.interfaces.INonogramSolver;

public class NonoSolver2 implements INonogramSolver {

   private RiddleLoader riddleLoader;

   private Riddle riddle;

   public NonoSolver2() {

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

      // System.out.println("openFile:" + arg0);
      riddleLoader = new RiddleLoader();
      riddle = riddleLoader.readFile(arg0);

   }

   @Override
   public char[][] getSolution() {
      // System.out.println("getSolution");
      char[][] matrix = new char[riddle.getHeight()][riddle.getWidth()];

      for (int i = 0; i < riddle.getHeight(); i++) {
         for (int j = 0; j < riddle.getWidth(); j++) {
            matrix[i][j] = '*';
         }
      }

      try {
         matrix = fillEmptyRowAndColumn(matrix);
         for (int i = 0; i < 6; i++) {
            matrix = fillColumnsWhenFullWithBlock(matrix);
            matrix = fillRowsWhenFullWithBlock(matrix);
            matrix = fillColumnIfAllBlocksAreGone(matrix);
            matrix = fillRowIfAllBlocksAreGone(matrix);
            matrix = fillBlocksOnBeginningOfColumn(matrix);
            matrix = fillBlocksOnEndOfColumn(matrix);
            matrix = fillBlocksOnBeginningOfRow(matrix);
            matrix = fillBlocksOnEndOfRow(matrix);
            matrix = checkIfBeginningAndEndOfRowCanBeWhite(matrix);
            matrix = checkIfBeginningAndEndOfColumnCanBeWhite(matrix);
            matrix = checkRowForWhitespacesBetweenSameBlockFromBeginning(matrix);
            matrix = checkRowForWhitespacesBetweenSameBlockFromEnd(matrix);
            //TODO: auch für column
            matrix = checkInRowIfThereAreSpacesBigger5AndBlockBigger5(matrix);
            
            checkBlocksInColumnsToBeTTrue(matrix);
            checkBlocksInRowsToBeTTrue(matrix);
         }

      } catch (Exception e) {
         showMatrix(matrix);
         showBlockGoneTrue(matrix);
         e.printStackTrace();
      }
      showBlockGoneTrue(matrix);
      showMatrix(matrix);
      showBlockGoneTrue(matrix);
      return matrix;
   }
   
   /**
    * Übrtprüft, ob es Blöcke gibt, die vollständig sind, aber nicht gone=true
    * sind.
    * 
    * @param matrix
    */
   private char[][] checkBlocksInColumnsToBeTTrue(char[][] matrix) {
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
            if (column != null && column.getBlocks() != null) {
               for (ColourBlock block : column.getBlocks()) {
                  block.setGone(true);
               }
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
   private char[][] checkBlocksInRowsToBeTTrue(char[][] matrix) {
      for (int roInt = 0; roInt < riddle.getHeight(); roInt++) {
         Row row = riddle.getRows().get(roInt);
         int filledColumnsInRow = 0;
         int blocksLengthCount = 0;
         for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
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
      return matrix;
   }

   private char[][] checkInRowIfThereAreSpacesBigger5AndBlockBigger5(char[][] matrix) throws Exception {
      for (Row row : riddle.getRows()) {
         int rowInt = getIndexOfRow(row);
         if (!row.isGone()) {
            int sizeOfBiggestStarsInRow = 0;
            int positionOfBiggestStarsInRow = 0;
            ColourBlock biggestBlock = null;
            int starCounter = 0;
            for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
               if (matrix[rowInt][columnInt] == '*') {
                  starCounter++;
                  if (columnInt == riddle.getWidth() - 1) {
                     if (starCounter > sizeOfBiggestStarsInRow) {
                        sizeOfBiggestStarsInRow = starCounter;
                        positionOfBiggestStarsInRow = columnInt;
                        starCounter = 0;
                     }
                  }
               } else {
                  if (starCounter > sizeOfBiggestStarsInRow) {
                     sizeOfBiggestStarsInRow = starCounter;
                     positionOfBiggestStarsInRow = columnInt;
                     starCounter = 0;
                  }
               }
            }
            
            if (sizeOfBiggestStarsInRow > 5) {
               LinkedList<ColourBlock> blocks = row.getBlocks();
               for (ColourBlock block : blocks) {
                  if (block.getHowMany() == sizeOfBiggestStarsInRow) {
                     if (biggestBlock == null) {
                        biggestBlock = block;
                     } else {
                        break;
                     }
                  }
               }
               if (biggestBlock != null) {
                  fillAreaInRowWithChar(matrix, rowInt, positionOfBiggestStarsInRow + 1 - biggestBlock.getHowMany(), positionOfBiggestStarsInRow + 1, biggestBlock.getColour().getName());
               }
            }
         }
      }
      return matrix;
   }

   private char[][] checkRowForWhitespacesBetweenSameBlockFromBeginning(char[][] matrix) throws Exception {
      System.out.println("checkRowForWhitespacesBetweenSameBlock");
      // showMatrix(matrix);
      // gehe Rows durch
      for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
         Row row = riddle.getRows().get(rowInt);
         if (row.getBlocks() == null) {
            return matrix;
         } else {
            int columnInt = 0;
            boolean goOn = true;
            for (ColourBlock block : row.getBlocks()) {
               if (goOn && columnInt < riddle.getWidth()) {
                  while (columnInt < riddle.getWidth() && matrix[rowInt][columnInt] == '-') {
                     columnInt++;
                  }
                  if (columnInt < riddle.getWidth()) {
                     if (matrix[rowInt][columnInt] != block.getColour().getName()) {
                        goOn = false;
                     } else {
                        if ((columnInt + block.getHowMany() - 1) < riddle.getWidth()) {
                           if (matrix[rowInt][(columnInt + block.getHowMany() - 1)] != block.getColour().getName() && matrix[rowInt][(columnInt + block.getHowMany() - 1)] != '*') {
                              throw new Exception("must be !!" + rowInt + " " + columnInt);
                           }
                           fillAreaInRowWithChar(matrix, rowInt, columnInt, columnInt + block.getHowMany(), block.getColour().getName());
                           block.setGone(true);
                           columnInt += block.getHowMany();
                        }
                     }
                  }
               }
            }
         }

      }
      return matrix;
   }

   private char[][] checkRowForWhitespacesBetweenSameBlockFromEnd(char[][] matrix) throws Exception {
      System.out.println("checkRowForWhitespacesBetweenSameBlock");
      // showMatrix(matrix);
      // gehe Rows durch
      for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
         Row row = riddle.getRows().get(rowInt);
         if (row.getBlocks() == null) {
            return matrix;
         } else {
            
            boolean goOn = true;
            int columnIn = riddle.getWidth() - 1;
            for (int blockInt = row.getBlocks().size() -1; blockInt > -1; blockInt--) {
               ColourBlock block = row.getBlocks().get(blockInt);
               if (goOn && columnIn > -1) {
                  while (columnIn > -1 && matrix[rowInt][columnIn] == '-') {
                     columnIn--;
                  }
                  if (columnIn > -1) {
                     if (matrix[rowInt][columnIn] != block.getColour().getName()) {
                        goOn = false;
                     } else {
                        if ((columnIn - block.getHowMany() + 1) > -1) {
                           if (matrix[rowInt][(columnIn - block.getHowMany() + 1)] != block.getColour().getName() && matrix[rowInt][(columnIn - block.getHowMany() + 1)] != '*') {
                              throw new Exception("must be !!" + rowInt + " " + columnIn + " " + matrix[rowInt][(columnIn - block.getHowMany() + 1)] + " " + block.getColour().getName());
                           }
                           fillAreaInRowWithChar(matrix, rowInt, (columnIn + 1 - block.getHowMany()), columnIn + 1, block.getColour().getName());
                           block.setGone(true);
                           columnIn -= block.getHowMany();
                        }
                     }
                  }
               }
            }
         }

      }
      return matrix;
   }

   /**
    * Wenn die Anzahl der Sterne bis zum ersten - kleiner als erste Blöcke oder
    * erster Block == gone dann mit - füllen
    * 
    * @param matrix
    * @return
    * @throws Exception
    */
   private char[][] checkIfBeginningAndEndOfRowCanBeWhite(char[][] matrix) throws Exception {
      System.out.println("checkIfBeginningAndEndOfRowCanBeWhite");
      for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
         Row row = riddle.getRows().get(rowInt);

         if (!row.isGone()) {
            int starCounter = 0;
            int whatToDo = 0;
            for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
               if (matrix[rowInt][columnInt] == '*') {
                  starCounter++;
               } else if (matrix[rowInt][columnInt] == '-') {
                  whatToDo = 1;
                  break;
               } else if (matrix[rowInt][columnInt] != '-') {
                  whatToDo = 2;
                  break;
               }
            }
            LinkedList<ColourBlock> blocks = row.getBlocks();
            if (starCounter > 0 && blocks != null) {
               ColourBlock block = blocks.get(blocks.size() - 1);
               if (whatToDo == 1 && (starCounter < block.getHowMany() || block.isGone())) {
                  fillAreaInRowWithChar(matrix, rowInt, 0, starCounter, '-');
               } else {
                  // TODO: Think about it.
               }
            }
            starCounter = 0;
            whatToDo = 0;
            for (int columnInt = (riddle.getWidth() - 1); columnInt > -1; columnInt--) {
               if (matrix[rowInt][columnInt] == '*') {
                  starCounter++;
               } else if (matrix[rowInt][columnInt] == '-') {
                  whatToDo = 1;
                  break;
               } else if (matrix[rowInt][columnInt] != '-') {
                  whatToDo = 2;
                  break;
               }
            }
            if (starCounter > 0 && blocks != null) {
               ColourBlock block = blocks.get(blocks.size() - 1);
               if (whatToDo == 1 && (starCounter < block.getHowMany() || block.isGone())) {
                  fillAreaInRowWithChar(matrix, rowInt, riddle.getWidth() - starCounter, riddle.getWidth(), '-');
               } else {

               }
            }
         }
      }
      return matrix;
   }

   /**
    * Wenn die Anzahl der Sterne bis zum ersten - kleiner als erste Blöcke oder
    * erster Block == gone dann mit - füllen
    * 
    * @param matrix
    * @return
    * @throws Exception
    */
   private char[][] checkIfBeginningAndEndOfColumnCanBeWhite(char[][] matrix) throws Exception {
      System.out.println("checkIfBeginningAndEndOfColumnCanBeWhite");
      for (Column column : riddle.getColumns()) {
         int columnInt = getIndexOfColumn(column);
         if (!column.isGone()) {
            int starCounter = 0;
            int whatToDo = 0;
            for (int rowInt = 0; rowInt < riddle.getWidth(); rowInt++) {
               if (matrix[rowInt][columnInt] == '*') {
                  starCounter++;
               } else if (matrix[rowInt][columnInt] == '-') {
                  whatToDo = 1;
                  break;
               } else if (matrix[rowInt][columnInt] != '-') {
                  whatToDo = 2;
                  break;
               }
            }
            LinkedList<ColourBlock> blocks = column.getBlocks();
            if (starCounter > 0 && blocks != null) {
               ColourBlock block = blocks.get(blocks.size() - 1);
               if (whatToDo == 1 && (starCounter < block.getHowMany() || block.isGone())) {
                  fillAreaInColumnWithChar(matrix, columnInt, 0, starCounter, '-');
               } else {
                  // TODO: Think about it.
               }
            }
            starCounter = 0;
            whatToDo = 0;
            for (int rowInt = (riddle.getHeight() - 1); columnInt > -1; rowInt--) {
               if (matrix[rowInt][columnInt] == '*') {
                  starCounter++;
               } else if (matrix[rowInt][columnInt] == '-') {
                  whatToDo = 1;
                  break;
               } else if (matrix[rowInt][columnInt] != '-') {
                  whatToDo = 2;
                  break;
               }
            }
            if (starCounter > 0 && blocks != null) {
               ColourBlock block = blocks.get(blocks.size() - 1);
               if (whatToDo == 1 && (starCounter < block.getHowMany() || block.isGone())) {
                  fillAreaInRowWithChar(matrix, columnInt, riddle.getHeight() - starCounter, riddle.getHeight(), '-');
               } else {

               }
            }
         }
      }
      return matrix;
   }

   private char[][] fillBlocksOnBeginningOfColumn(char[][] matrix) throws Exception {
      // System.out.println("fillBlocksOnBeginningOfColumn");
      for (Column column : riddle.getColumns()) {
         int rowInt = 0;
         boolean run = true;
         while (run) {
            if (rowInt < riddle.getHeight() && matrix[rowInt][getIndexOfColumn(column)] == '-') {
               rowInt++;
            } else {
               run = false;
            }
         }
         if (rowInt < riddle.getHeight() && matrix[rowInt][getIndexOfColumn(column)] != '*') {
            LinkedList<ColourBlock> blocks = column.getBlocks();
            if (blocks != null) {
               ColourBlock colourBlock = blocks.get(0);
               if (matrix[rowInt][getIndexOfColumn(column)] == colourBlock.getColour().getName()) {
                  fillAreaInColumnWithChar(matrix, getIndexOfColumn(column), rowInt, (rowInt + colourBlock.getHowMany()), colourBlock.getColour().getName());
                  if (blocks.size() > 1) {
                     ColourBlock nextBlock = blocks.get(1);
                     if (nextBlock.getColour().getName() == colourBlock.getColour().getName()) {
                        fillAreaInColumnWithChar(matrix, getIndexOfColumn(column), rowInt + colourBlock.getHowMany(), rowInt + colourBlock.getHowMany() + 1, '-');
                     }
                  }
               } else {
                  throw new Exception("char " + matrix[rowInt][getIndexOfColumn(column)] + " ungleich " + colourBlock.getColour().getName());
               }

            }
         }
      }
      return matrix;
   }

   private char[][] fillBlocksOnEndOfColumn(char[][] matrix) throws Exception {
      // System.out.println("fillBlocksOnEndOfColumn");
      for (Column column : riddle.getColumns()) {
         int rowInt = riddle.getHeight() - 1;
         boolean run = true;
         while (run) {
            if (rowInt > -1 && matrix[rowInt][getIndexOfColumn(column)] == '-') {
               rowInt--;
            } else {
               run = false;
            }
         }
         if (rowInt > -1 && matrix[rowInt][getIndexOfColumn(column)] != '*') {
            LinkedList<ColourBlock> blocks = column.getBlocks();
            if (blocks != null) {
               ColourBlock colourBlock = blocks.get(blocks.size() - 1);
               if (matrix[rowInt][getIndexOfColumn(column)] == colourBlock.getColour().getName()) {
                  rowInt += 1;
                  fillAreaInColumnWithChar(matrix, getIndexOfColumn(column), (rowInt - colourBlock.getHowMany()), (rowInt), colourBlock.getColour().getName());
                  // Wenn nächster Block vorhanden und gleicher Farbe ein -
                  // dazwischen.
                  if (blocks.size() > 1) {
                     ColourBlock nextBlock = blocks.get(blocks.size() - 2);
                     if (nextBlock.getColour().getName() == colourBlock.getColour().getName()) {
                        fillAreaInColumnWithChar(matrix, getIndexOfColumn(column), rowInt - colourBlock.getHowMany() - 1, rowInt - colourBlock.getHowMany(), '-');
                     }
                  }
               } else {
                  throw new Exception("char " + matrix[rowInt][getIndexOfColumn(column)] + " ungleich " + colourBlock.getColour().getName());
               }

            }
         }
      }
      return matrix;
   }

   private char[][] fillBlocksOnBeginningOfRow(char[][] matrix) throws Exception {
      // System.out.println("fillBlocksOnBeginningOfRow");
      for (Row row : riddle.getRows()) {
         int columnInt = 0;
         boolean run = true;
         while (run) {
            if (columnInt < riddle.getWidth() && matrix[getIndexOfRow(row)][columnInt] == '-') {
               columnInt++;
            } else {
               run = false;
            }
         }
         if (columnInt < riddle.getWidth() && matrix[getIndexOfRow(row)][columnInt] != '*') {
            LinkedList<ColourBlock> blocks = row.getBlocks();
            if (blocks != null) {
               ColourBlock colourBlock = blocks.get(0);
               if (matrix[getIndexOfRow(row)][columnInt] == colourBlock.getColour().getName()) {
                  fillAreaInRowWithChar(matrix, getIndexOfRow(row), columnInt, (columnInt + colourBlock.getHowMany()), colourBlock.getColour().getName());
                  if (blocks.size() > 1) {
                     ColourBlock nextBlock = blocks.get(1);
                     if (nextBlock.getColour().getName() == colourBlock.getColour().getName()) {
                        fillAreaInRowWithChar(matrix, getIndexOfRow(row), columnInt + colourBlock.getHowMany(), columnInt + colourBlock.getHowMany() + 1, '-');
                     }
                  }
               } else {
                  throw new Exception("char " + matrix[getIndexOfRow(row)][columnInt] + " ungleich " + colourBlock.getColour().getName());
               }

            }
         }
      }
      return matrix;
   }

   private char[][] fillBlocksOnEndOfRow(char[][] matrix) throws Exception {
      // System.out.println("fillBlocksOnEndOfRow");
      for (Row row : riddle.getRows()) {
         int columnInt = riddle.getWidth() - 1;
         boolean run = true;
         while (run) {
            if (columnInt > -1 && matrix[getIndexOfRow(row)][columnInt] == '-') {
               columnInt--;
            } else {
               run = false;
            }
         }
         if (columnInt > -1 && matrix[getIndexOfRow(row)][columnInt] != '*') {
            LinkedList<ColourBlock> blocks = row.getBlocks();
            if (blocks != null) {
               ColourBlock colourBlock = blocks.get(blocks.size() - 1);
               if (matrix[getIndexOfRow(row)][columnInt] == colourBlock.getColour().getName()) {
                  columnInt += 1;
                  fillAreaInRowWithChar(matrix, getIndexOfRow(row), (columnInt - colourBlock.getHowMany()), (columnInt), colourBlock.getColour().getName());
                  if (blocks.size() > 1) {
                     ColourBlock nextBlock = blocks.get(blocks.size() - 2);
                     if (nextBlock.getColour().getName() == colourBlock.getColour().getName()) {
                        fillAreaInRowWithChar(matrix, getIndexOfRow(row), columnInt - colourBlock.getHowMany() - 1, columnInt - colourBlock.getHowMany(), '-');
                     }
                  }
               } else {
                  throw new Exception("char " + matrix[getIndexOfRow(row)][columnInt] + " ungleich " + colourBlock.getColour().getName());
               }

            }
         }
      }
      return matrix;
   }

   private char[][] fillColumnIfAllBlocksAreGone(char[][] matrix) {
      // System.out.println("fillColumnIfAllBlocksAreGone");
      for (Column column : riddle.getColumns()) {
         boolean fillIt = true;
         if (column.getBlocks() != null) {
            for (ColourBlock block : column.getBlocks()) {
               if (!block.isGone()) {
                  fillIt = false;
               }
            }
            if (fillIt) {
               for (Row row : riddle.getRows()) {
                  if (matrix[getIndexOfRow(row)][getIndexOfColumn(column)] == '*') {
                     matrix[getIndexOfRow(row)][getIndexOfColumn(column)] = '-';
                  }
               }
               column.setGone(true);
            }
         }
      }
      return matrix;
   }

   private char[][] fillRowIfAllBlocksAreGone(char[][] matrix) {
      // System.out.println("fillRowIfAllBlocksAreGone");
      for (Row row : riddle.getRows()) {
         boolean fillIt = true;
         if (row.getBlocks() != null) {
            for (ColourBlock block : row.getBlocks()) {
               if (!block.isGone()) {
                  fillIt = false;
               }
            }
            if (fillIt) {
               for (Column column : riddle.getColumns()) {
                  if (matrix[getIndexOfRow(row)][getIndexOfColumn(column)] == '*') {
                     matrix[getIndexOfRow(row)][getIndexOfColumn(column)] = '-';
                  }
               }
               row.setGone(true);
            }
         }
      }
      return matrix;
   }

   /**
    * Gefüllt werden die columns, deren blocks die column ausfüllen.
    * 
    * @param matrix
    * @return
    * @throws Exception
    */
   private char[][] fillColumnsWhenFullWithBlock(char[][] matrix) throws Exception {
      // System.out.println("fillColumnsWhenFullWithBlock");
      for (Column column : riddle.getColumns()) {
         // System.out.println("Column:" + getIndexOfColumn(column));
         if (column.getBlocks() != null) {
            int blockCounter = 0;
            ColourBlock lastBlock = null;
            // Blocks in column durchgehen und Größe der Blocks
            // zusammenrechnen.
            for (ColourBlock block : column.getBlocks()) {
               blockCounter += block.getHowMany();
               if (null != lastBlock) {
                  if (lastBlock.getColour().getName() == block.getColour().getName()) {
                     blockCounter++;
                  }
               }
               lastBlock = block;
            }
            // wenn gleich dann ausfüllen
            int height = riddle.getWidth();
            int index = 0;
            while (index < riddle.getHeight() && matrix[index][getIndexOfColumn(column)] == '-') {
               height--;
               index++;
               // System.out.println("Row " + index + " is -.");
            }
            index = riddle.getHeight() - 1;
            while (index > -1 && matrix[index][getIndexOfColumn(column)] == '-') {
               height--;
               index--;
               // System.out.println("Row " + index + " is -.");
            }
            if (blockCounter == height) {
               lastBlock = null;
               int rowIndex = 0;
               for (ColourBlock block : column.getBlocks()) {
                  if (null != lastBlock) {
                     if (lastBlock.getColour().getName() == block.getColour().getName()) {
                        fillAreaInColumnWithChar(matrix, getIndexOfColumn(column), rowIndex, rowIndex + 1, '-');
                        rowIndex++;
                     }
                  }
                  boolean run = true;
                  while (run) {
                     if (matrix[rowIndex][getIndexOfColumn(column)] == '-') {
                        rowIndex++;
                     } else {
                        run = false;
                     }
                  }
                  fillAreaInColumnWithChar(matrix, getIndexOfColumn(column), rowIndex, rowIndex + block.getHowMany(), block.getColour().getName());
                  rowIndex += block.getHowMany();
                  lastBlock = block;
                  block.setGone(true);
               }
               column.setGone(true);
            }
         }
      }
      return matrix;
   }

   private int getIndexOfColumn(Column column) {
      return riddle.getColumns().indexOf(column);
   }

   /**
    * Gefüllt werden die rows, deren blocks die row ausfüllen.
    * 
    * @param matrix
    * @return
    * @throws Exception
    */
   private char[][] fillRowsWhenFullWithBlock(char[][] matrix) throws Exception {
      // System.out.println("fillRowsWhenFullWithBlock");
      for (Row row : riddle.getRows()) {
         if (!row.isGone()) {
            // System.out.println("Row:" + getIndexOfRow(row));
            if (row.getBlocks() != null) {
               int blockCounter = 0;
               ColourBlock lastBlock = null;
               // Blocks in row durchgehen und Größe der Blocks
               // zusammenrechnen.
               for (ColourBlock block : row.getBlocks()) {
                  blockCounter += block.getHowMany();
                  if (null != lastBlock) {
                     if (lastBlock.getColour().getName() == block.getColour().getName()) {
                        blockCounter++;
                     }
                  }
                  lastBlock = block;
               }
               // wenn gleich dann ausfüllen

               int width = riddle.getWidth();
               int index = 0;
               while (index < riddle.getWidth() && matrix[getIndexOfRow(row)][index] == '-') {
                  width--;
                  index++;
                  // System.out.println("Row " + index + " is -.");
               }
               index = riddle.getWidth() - 1;
               while (index > -1 && matrix[getIndexOfRow(row)][index] == '-') {
                  width--;
                  index--;
                  // System.out.println("Row " + index + " is -.");
               }
               if (blockCounter == width) {
                  lastBlock = null;
                  int columnIndex = 0;
                  for (ColourBlock block : row.getBlocks()) {
                     if (null != lastBlock) {
                        if (lastBlock.getColour().getName() == block.getColour().getName()) {
                           fillAreaInRowWithChar(matrix, getIndexOfRow(row), columnIndex, columnIndex + 1, '-');
                           columnIndex++;
                        }
                     }
                     boolean run = true;
                     while (run) {
                        if (riddle.getColumns().get(columnIndex).isGone() && matrix[getIndexOfRow(row)][columnIndex] == '-') {
                           columnIndex++;
                        } else {
                           run = false;
                        }
                     }

                     fillAreaInRowWithChar(matrix, getIndexOfRow(row), columnIndex, columnIndex + block.getHowMany(), block.getColour().getName());
                     columnIndex += block.getHowMany();
                     lastBlock = block;
                     block.setGone(true);
                  }
                  row.setGone(true);
               }
            }
         }
      }
      return matrix;
   }

   private int getIndexOfRow(Row row) {
      return riddle.getRows().indexOf(row);
   }

   /**
    * Gefüllt werden die rows und columns, die leer sind.
    * 
    * @param matrix
    * @return
    * @throws Exception
    */
   private char[][] fillEmptyRowAndColumn(char[][] matrix) throws Exception {
      // System.out.println("fillEmptyRowAndColumn");
      for (Row row : riddle.getRows()) {
         if (row.getBlocks() == null) {
            int rowIndex = getIndexOfRow(row);
            matrix = fillAreaInRowWithChar(matrix, rowIndex, 0, riddle.getHeight(), '-');
            row.setGone(true);
         }
      }
      for (Column column : riddle.getColumns()) {
         if (column.getBlocks() == null) {
            int columnIndex = getIndexOfColumn(column);
            matrix = fillAreaInColumnWithChar(matrix, columnIndex, 0, riddle.getHeight(), '-');
            column.setGone(true);
         }
      }
      return matrix;
   }

   /**
    * Füllt den Bereich in der column zwischen rowBegin (inklusive) und rowEnd
    * (exklisive) mit dem char c.
    * 
    * @param matrix
    * @param columnIndex
    * @param rowBegin
    * @param rowEnd
    * @param c
    * @return
    * @throws Exception
    */
   private char[][] fillAreaInColumnWithChar(char[][] matrix, int columnIndex, int rowBegin, int rowEnd, char c) throws Exception {
      // System.out.println("fillAreaInColumnWithChar");
      for (int row = rowBegin; row < rowEnd; row++) {
         if (matrix[row][columnIndex] != '*' && matrix[row][columnIndex] != c) {
            throw new Exception("Fehler: row:" + row + " column:" + columnIndex + " " + c + " ungleich " + matrix[row][columnIndex]);
         }
         if (matrix[row][columnIndex] != c) {
            matrix[row][columnIndex] = c;
            if (matrix[row][columnIndex] != '-') {
               riddle.getColumns().get(columnIndex).setEntriesSet();
               riddle.getRows().get(row).setEntriesSet();
            }
         }

      }
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
   private char[][] fillAreaInRowWithChar(char[][] matrix, int rowIndex, int columnBegin, int columnEnd, char c) throws Exception {
      // System.out.println("fillAreaInRowWithChar");
      for (int column = columnBegin; column < columnEnd; column++) {
         if (matrix[rowIndex][column] != '*' && matrix[rowIndex][column] != c) {
            throw new Exception("Fehler: row:" + rowIndex + " column:" + column + " " + c + " ungleich " + matrix[rowIndex][column]);
         }
         if (matrix[rowIndex][column] != c) {
            matrix[rowIndex][column] = c;
            if (matrix[rowIndex][column] != '-') {
               riddle.getRows().get(rowIndex).setEntriesSet();
               riddle.getColumns().get(column).setEntriesSet();
            }
         }
      }
      return matrix;
   }

   /**
    * Display the matrix.
    * 
    * @param matrix
    */
   private void showMatrix(char[][] matrix) {
      for (int i = 0; i < riddle.getHeight(); i++) {
         String out = "";
         for (int j = 0; j < riddle.getWidth(); j++) {
            out += matrix[i][j];
            out += "  ";
         }
         System.out.println(out);
      }
      System.out.println();
      System.out.println(riddle);
   }

   private void showBlockGoneTrue(char[][] matrix) {
      System.out.println("showBlockGoneTrue");
      for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
         System.out.println("Row:" + rowInt);
         Row row = riddle.getRows().get(rowInt);
         LinkedList<ColourBlock> blocks = row.getBlocks();
         if (null != blocks) {
            for (ColourBlock block : blocks) {
               System.out.println(block.getHowMany() + "--" + block.isGone());
            }
         }
      }
      for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
         System.out.println("Column:" + columnInt);
         Column column = riddle.getColumns().get(columnInt);
         LinkedList<ColourBlock> blocks = column.getBlocks();
         if (null != blocks) {
            for (ColourBlock block : blocks) {
               System.out.println(block.getHowMany() + "--" + block.isGone());
            }
         }
      }
   }

}
