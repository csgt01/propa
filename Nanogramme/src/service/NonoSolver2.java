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

      try {
         matrix = fillEmptyRowAndColumn(matrix);
         for (int i = 0; i < 10; i++) {
            matrix = fillColumnsWhenFullWithBlock(matrix);
            // showMatrix(matrix);
            // showBlockGoneTrue(matrix);
            matrix = fillRowsWhenFullWithBlock(matrix);
            // showMatrix(matrix);
            // showBlockGoneTrue(matrix);
            matrix = fillColumnIfAllBlocksAreGone(matrix);
            // showMatrix(matrix);
            // showBlockGoneTrue(matrix);
            matrix = fillRowIfAllBlocksAreGone(matrix);
            // showMatrix(matrix);
            // showBlockGoneTrue(matrix);
            matrix = fillBlocksOnBeginningOfColumn(matrix);
            // showMatrix(matrix);
            // showBlockGoneTrue(matrix);
            matrix = fillBlocksOnEndOfColumn(matrix);
            // showMatrix(matrix);
            // showBlockGoneTrue(matrix);
            matrix = fillBlocksOnBeginningOfRow(matrix);
            // showMatrix(matrix);
            // showBlockGoneTrue(matrix);
            matrix = fillBlocksOnEndOfRow(matrix);
            // showMatrix(matrix);
            // showBlockGoneTrue(matrix);
            matrix = checkIfBeginningAndEndOfRowCanBeWhite(matrix);
            // showMatrix(matrix);
            // showBlockGoneTrue(matrix);
            matrix = checkIfBeginningAndEndOfColumnCanBeWhite(matrix);
            // showMatrix(matrix);
            // showBlockGoneTrue(matrix);
            matrix = checkRowForWhitespacesBetweenSameBlockFromBeginning(matrix);
            // showMatrix(matrix);
            // showBlockGoneTrue(matrix);
            matrix = checkRowForWhitespacesBetweenSameBlockFromEnd(matrix);
            // showMatrix(matrix);
            // showBlockGoneTrue(matrix);
            // TODO: auch für column
            matrix = checkIfThereAreSpacesBigger5AndBlockBigger5InRow(matrix);
            // showMatrix(matrix);
            // showBlockGoneTrue(matrix);
            matrix = checkInIfThereAreSpacesBigger5AndBlockBigger5InColumn(matrix);
            // showMatrix(matrix);
            // showBlockGoneTrue(matrix);
            findOverlappingAreasInRow(matrix);
            // showMatrix(matrix);
            // showBlockGoneTrue(matrix);
            findOverlappingAreasInColumn(matrix);
            // showMatrix(matrix);
            // showBlockGoneTrue(matrix);
            checkBlocksInColumnsToBeTTrue(matrix);
            // showBlockGoneTrue(matrix);
            // showMatrix(matrix);
            checkBlocksInRowsToBeTTrue(matrix);
            // showMatrix(matrix);
            // showBlockGoneTrue(matrix);
         }
      } catch (Exception e) {
         showMatrix(matrix);
         // showBlockGoneTrue(matrix);
         e.printStackTrace();
      }
      // showBlockGoneTrue(matrix);
      showMatrix(matrix);
      // showBlockGoneTrue(matrix);
      return matrix;
   }

   private char[][] findOverlappingAreasInRow(char[][] matrix) throws Exception {
      for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
         LinkedList<String> asd = new LinkedList<String>();
         LinkedList<String> first = new LinkedList<String>();
         Row row = riddle.getRows().get(rowInt);
         System.out.println(row);
         LinkedList<ColourBlock> blocks = row.getBlocks();
         if (blocks == null) {
            return matrix;
         }
         ColourBlock lastBlock = null;
         int resultIndex = 0;
         for (ColourBlock block : blocks) {
            if (null != lastBlock && lastBlock.getColour().getName() == block.getColour().getName()) {
               asd.add("-");
               resultIndex++;
            }

            for (int i = 0; i < block.getHowMany(); i++) {
               asd.add(String.valueOf(block.getColour().getName()));
            }

            resultIndex += block.getHowMany();
            lastBlock = block;
         }
         for (int i = resultIndex; i < riddle.getWidth(); i++) {
            asd.add("-");
         }
         first.addAll(asd);
         LinkedList<Integer> result = new LinkedList<Integer>();
         for (int i = 0; i < first.size(); i++) {

            if (!first.get(i).equals("-")) {
               result.add(i);
            }
         }
         String removed;
         while (asd.getLast().equals("-")) {

            removed = asd.removeLast();

            asd.addFirst(removed);

            int size = result.size();
            for (int i = size - 1; i > -1; i--) {

               Integer index = result.get(i);
               if (!first.get(index).equals(asd.get(index))) {

                  result.remove(index);

               }

            }

         }
         for (Integer column : result) {
            char charAt = first.get(column).charAt(0);
            if (matrix[rowInt][column] != '*' && matrix[rowInt][column] != charAt) {
               throw new Exception("Fehler: row:" + rowInt + " column:" + column + " " + charAt + " ungleich " + matrix[rowInt][column]);
            }
            if (matrix[rowInt][column] != charAt) {
               matrix[rowInt][column] = charAt;
               if (matrix[rowInt][column] != '-') {
                  riddle.getRows().get(rowInt).setEntriesSet();
                  riddle.getColumns().get(column).setEntriesSet();
               }
            }
         }
      }
      return matrix;
   }

   private char[][] findOverlappingAreasInColumn(char[][] matrix) throws Exception {
      for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
         LinkedList<String> asd = new LinkedList<String>();
         LinkedList<String> first = new LinkedList<String>();
         Column column = riddle.getColumns().get(columnInt);
         System.out.println(column);
         LinkedList<ColourBlock> blocks = column.getBlocks();
         if (blocks == null) {
            return matrix;
         }
         ColourBlock lastBlock = null;
         int resultIndex = 0;
         for (ColourBlock block : blocks) {
            if (null != lastBlock && lastBlock.getColour().getName() == block.getColour().getName()) {
               asd.add("-");
               resultIndex++;
            }

            for (int i = 0; i < block.getHowMany(); i++) {
               asd.add(String.valueOf(block.getColour().getName()));
            }

            resultIndex += block.getHowMany();
            lastBlock = block;
         }
         for (int i = resultIndex; i < riddle.getHeight(); i++) {
            asd.add("-");
         }
         first.addAll(asd);
         LinkedList<Integer> result = new LinkedList<Integer>();
         for (int i = 0; i < first.size(); i++) {

            if (!first.get(i).equals("-")) {
               result.add(i);
            }
         }
         String removed;
         while (asd.getLast().equals("-")) {

            removed = asd.removeLast();

            asd.addFirst(removed);

            int size = result.size();
            for (int i = size - 1; i > -1; i--) {

               Integer index = result.get(i);
               if (!first.get(index).equals(asd.get(index))) {

                  result.remove(index);

               }

            }
         }
         for (Integer rowIndex : result) {
            char charAt = first.get(rowIndex).charAt(0);
            if (matrix[rowIndex][columnInt] != '*' && matrix[rowIndex][columnInt] != charAt) {
               throw new Exception("Fehler: row:" + rowIndex + " column:" + columnInt + " " + charAt + " ungleich " + matrix[rowIndex][columnInt]);
            }
            if (matrix[rowIndex][columnInt] != charAt) {
               matrix[rowIndex][columnInt] = charAt;
               if (matrix[rowIndex][columnInt] != '-') {
                  riddle.getRows().get(rowIndex).setEntriesSet();
                  riddle.getColumns().get(columnInt).setEntriesSet();
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
    * @throws Exception
    */
   private char[][] checkBlocksInColumnsToBeTTrue(char[][] matrix) throws Exception {
      // Zuerst nur erste.
      for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
         Column column = riddle.getColumns().get(columnInt);
         LinkedList<ColourBlock> blocks = column.getBlocks();
         if (blocks != null) {
            int roInt = 0;
            int blockInt = 0;
            for (int rowInt = roInt; rowInt < riddle.getHeight(); ) {
               if (matrix[rowInt][columnInt] == '-') {
                  rowInt++;
               } else 
               if (rowInt < riddle.getHeight() && matrix[rowInt][columnInt] != '*') {
                  ColourBlock colourBlock = blocks.get(blockInt);
                  if (!colourBlock.isGone()) {
                     boolean fillIt = true;
                     for (int index = rowInt; index < rowInt + colourBlock.getHowMany(); index++) {
                        if (matrix[rowInt][columnInt] != colourBlock.getColour().getName()) {
                           fillIt = false;
                        }
                     }
                     if (fillIt) {
                        colourBlock.setGone(true);
                        rowInt += colourBlock.getHowMany();
                        if ((blockInt + 1) < blocks.size() && colourBlock.getColour().getName() == blocks.get(blockInt + 1).getColour().getName()) {
                           fillAreaInColumnWithChar(matrix, columnInt, rowInt, rowInt + 1, '-');
                        }

                     } else {
                        break;
                     }

                  } else {
                     rowInt += colourBlock.getHowMany();
                  }
               }
            }
         }
      }

      // Zuerst nur erste.
      for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
         Column column = riddle.getColumns().get(columnInt);
         LinkedList<ColourBlock> blocks = column.getBlocks();
         if (blocks != null) {
            int rowInt = riddle.getHeight() - 1;
            int blockInt = blocks.size() - 1;
            while (rowInt > -1 && matrix[rowInt][columnInt] == '-') {
               rowInt--;
            }
            if (rowInt > -1 && matrix[rowInt][columnInt] != '*') {
               ColourBlock colourBlock = blocks.get(blockInt);
               if (!colourBlock.isGone()) {
                  boolean fillIt = true;
                  for (int index = rowInt; index > rowInt - colourBlock.getHowMany(); index--) {
                     if (matrix[rowInt][columnInt] != colourBlock.getColour().getName()) {
                        fillIt = false;
                     }
                  }
                  if (fillIt) {
                     colourBlock.setGone(true);
                     rowInt -= colourBlock.getHowMany();
                     if ((blockInt - 1) > -1 && colourBlock.getColour().getName() == blocks.get(blockInt - 1).getColour().getName()) {
                        fillAreaInColumnWithChar(matrix, columnInt, rowInt - 1, rowInt, '-');
                     }
                  } else {
                     break;
                  }

               } else {
                  rowInt -= colourBlock.getHowMany();
               }
               blockInt--;
               if (blockInt < 0) {
                  break;
               }
            }
         }
      }

      // Hier: Wenn Anzahl gefüllter gleich Anzahl der Blöcke
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
    * @throws Exception
    */
   private char[][] checkBlocksInRowsToBeTTrue(char[][] matrix) throws Exception {
      // Zuerst nur erste.
      for (int roInt = 0; roInt < riddle.getWidth(); roInt++) {
         Row row = riddle.getRows().get(roInt);
         LinkedList<ColourBlock> blocks = row.getBlocks();
         if (blocks != null) {
            int columnInt = 0;
            int blockInt = 0;
            while (columnInt < riddle.getHeight() && matrix[roInt][columnInt] == '-') {
               columnInt++;
            }
            if (columnInt < riddle.getHeight() && matrix[roInt][columnInt] != '*') {
               ColourBlock colourBlock = blocks.get(blockInt);
               if (!colourBlock.isGone()) {
                  boolean fillIt = true;
                  for (int index = columnInt; index < columnInt + colourBlock.getHowMany(); index++) {
                     if (matrix[roInt][columnInt] != colourBlock.getColour().getName()) {
                        fillIt = false;
                     }
                  }
                  if (fillIt) {
                     colourBlock.setGone(true);
                     columnInt += colourBlock.getHowMany();
                     if ((blockInt + 1) < blocks.size() && colourBlock.getColour().getName() == blocks.get(blockInt + 1).getColour().getName()) {
                        fillAreaInRowWithChar(matrix, roInt, columnInt, columnInt + 1, '-');
                     }

                  } else {
                     break;
                  }

               } else {
                  columnInt += colourBlock.getHowMany();
               }
            }
         }
      }

      // Zuerst nur erste.
      for (int roInt = 0; roInt < riddle.getWidth(); roInt++) {
         Row row = riddle.getRows().get(roInt);
         LinkedList<ColourBlock> blocks = row.getBlocks();
         if (blocks != null) {
            int columnInt = riddle.getWidth() - 1;
            int blockInt = blocks.size() - 1;
            while (columnInt > -1 && matrix[columnInt][roInt] == '-') {
               columnInt--;
            }
            if (columnInt > -1 && matrix[roInt][columnInt] != '*') {
               ColourBlock colourBlock = blocks.get(blockInt);
               if (!colourBlock.isGone()) {
                  boolean fillIt = true;
                  for (int index = columnInt; index > columnInt - colourBlock.getHowMany(); index--) {
                     if (matrix[roInt][columnInt] != colourBlock.getColour().getName()) {
                        fillIt = false;
                     }
                  }
                  if (fillIt) {
                     colourBlock.setGone(true);
                     columnInt -= colourBlock.getHowMany();
                     if ((blockInt - 1) > -1 && colourBlock.getColour().getName() == blocks.get(blockInt - 1).getColour().getName()) {
                        fillAreaInRowWithChar(matrix, roInt, columnInt - 1, columnInt, '-');
                     }
                  } else {
                     break;
                  }

               } else {
                  columnInt -= colourBlock.getHowMany();
               }
               blockInt--;
               if (blockInt < 0) {
                  break;
               }
            }
         }
      }

      // Hier: Wenn Anzahl gefüllter gleich Anzahl der Blöcke
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

   private char[][] checkIfThereAreSpacesBigger5AndBlockBigger5InRow(char[][] matrix) throws Exception {
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
                        // TODO: check if it fits!
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

   private char[][] checkInIfThereAreSpacesBigger5AndBlockBigger5InColumn(char[][] matrix) throws Exception {
      for (Column column : riddle.getColumns()) {
         int columnInt = getIndexOfColumn(column);
         if (!column.isGone()) {
            int sizeOfBiggestStarsInColumn = 0;
            int positionOfBiggestStarsInColumn = 0;
            ColourBlock biggestBlock = null;
            int starCounter = 0;
            for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
               if (matrix[rowInt][columnInt] == '*') {
                  starCounter++;
                  if (rowInt == riddle.getHeight() - 1) {
                     if (starCounter > sizeOfBiggestStarsInColumn) {
                        sizeOfBiggestStarsInColumn = starCounter;
                        positionOfBiggestStarsInColumn = rowInt;
                        starCounter = 0;
                     }
                  }
               } else {
                  if (starCounter > sizeOfBiggestStarsInColumn) {
                     sizeOfBiggestStarsInColumn = starCounter;
                     positionOfBiggestStarsInColumn = rowInt;
                     starCounter = 0;
                  }
               }
            }

            if (sizeOfBiggestStarsInColumn > 5) {
               LinkedList<ColourBlock> blocks = column.getBlocks();
               for (ColourBlock block : blocks) {
                  if (block.getHowMany() == sizeOfBiggestStarsInColumn) {
                     if (biggestBlock == null) {
                        biggestBlock = block;
                     } else {
                        // TODO: check if it fits!
                        break;
                     }
                  }
               }
               if (biggestBlock != null) {
                  fillAreaInColumnWithChar(matrix, columnInt, positionOfBiggestStarsInColumn + 1 - biggestBlock.getHowMany(), positionOfBiggestStarsInColumn + 1, biggestBlock.getColour().getName());
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
            for (int blockInt = row.getBlocks().size() - 1; blockInt > -1; blockInt--) {
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
      System.out.println("fillBlocksOnBeginningOfColumn");
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
      System.out.println("fillBlocksOnEndOfColumn");
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
      System.out.println("fillBlocksOnBeginningOfRow");
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
      System.out.println("fillBlocksOnEndOfRow");
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
      System.out.println("fillColumnIfAllBlocksAreGone");
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
      System.out.println("fillRowIfAllBlocksAreGone");
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
      System.out.println("fillColumnsWhenFullWithBlock");
      for (Column column : riddle.getColumns()) {
         System.out.println("Column:" + getIndexOfColumn(column));
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
               System.out.println("Row " + index + " is -.");
            }
            index = riddle.getHeight() - 1;
            while (index > -1 && matrix[index][getIndexOfColumn(column)] == '-') {
               height--;
               index--;
               System.out.println("Row " + index + " is -.");
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
      System.out.println("fillRowsWhenFullWithBlock");
      for (Row row : riddle.getRows()) {
         if (!row.isGone()) {
            System.out.println("Row:" + getIndexOfRow(row));
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
                  System.out.println("Row " + index + " is -.");
               }
               index = riddle.getWidth() - 1;
               while (index > -1 && matrix[getIndexOfRow(row)][index] == '-') {
                  width--;
                  index--;
                  System.out.println("Row " + index + " is -.");
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
      System.out.println("fillEmptyRowAndColumn");
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
      System.out.println("fillAreaInColumnWithChar");
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
      System.out.println("fillAreaInRowWithChar");
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
      showBlockGoneTrue(matrix);
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
