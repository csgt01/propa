package service;

import java.io.IOException;
import java.util.LinkedList;

import models.Block;
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
         findOverlappingAreasInRow(matrix);
         checkBlocksInColumnsToBeTTrue(matrix);
         checkBlocksInRowsToBeTTrue(matrix);
         showMatrix(matrix);
         findOverlappingAreasInColumn(matrix);
         showMatrix(matrix);
         checkBlocksInColumnsToBeTTrue(matrix);
         checkBlocksInRowsToBeTTrue(matrix);
         for (int i = 0; i < 10; i++) {
            matrix = fillColumnsWhenFullWithBlock(matrix);
            showMatrix(matrix);
            checkBlocksInColumnsToBeTTrue(matrix);
            checkBlocksInRowsToBeTTrue(matrix);
            // showBlockGoneTrue(matrix);
            matrix = fillRowsWhenFullWithBlock(matrix);
            checkBlocksInColumnsToBeTTrue(matrix);
            checkBlocksInRowsToBeTTrue(matrix);
            showMatrix(matrix);
            // showBlockGoneTrue(matrix);
            matrix = fillColumnIfAllBlocksAreGone(matrix);
            checkBlocksInColumnsToBeTTrue(matrix);
            checkBlocksInRowsToBeTTrue(matrix);
            showMatrix(matrix);
            // showBlockGoneTrue(matrix);
            matrix = fillRowIfAllBlocksAreGone(matrix);
            checkBlocksInColumnsToBeTTrue(matrix);
            checkBlocksInRowsToBeTTrue(matrix);
            showMatrix(matrix);
            // showBlockGoneTrue(matrix);
            matrix = fillBlocksOnBeginningOfColumn(matrix);
            checkBlocksInColumnsToBeTTrue(matrix);
            checkBlocksInRowsToBeTTrue(matrix);
            showMatrix(matrix);
            // showBlockGoneTrue(matrix);
            matrix = fillBlocksOnEndOfColumn(matrix);
            checkBlocksInColumnsToBeTTrue(matrix);
            checkBlocksInRowsToBeTTrue(matrix);
            showMatrix(matrix);
            // showBlockGoneTrue(matrix);
            matrix = fillBlocksOnBeginningOfRow(matrix);
            checkBlocksInColumnsToBeTTrue(matrix);
            checkBlocksInRowsToBeTTrue(matrix);
            showMatrix(matrix);
            // showBlockGoneTrue(matrix);
            matrix = fillBlocksOnEndOfRow(matrix);
            checkBlocksInColumnsToBeTTrue(matrix);
            checkBlocksInRowsToBeTTrue(matrix);
            showMatrix(matrix);
            // showBlockGoneTrue(matrix);
            matrix = checkIfBeginningAndEndOfRowCanBeWhite(matrix);
            checkBlocksInColumnsToBeTTrue(matrix);
            checkBlocksInRowsToBeTTrue(matrix);
            showMatrix(matrix);
            matrix = checkIfBeginningAndEndOfColumnCanBeWhite(matrix);
            checkBlocksInColumnsToBeTTrue(matrix);
            checkBlocksInRowsToBeTTrue(matrix);
            showMatrix(matrix);
            matrix = checkRowForWhitespacesBetweenSameBlockFromBeginning(matrix);
            checkBlocksInColumnsToBeTTrue(matrix);
            checkBlocksInRowsToBeTTrue(matrix);
            showMatrix(matrix);
            matrix = checkRowForWhitespacesBetweenSameBlockFromEnd(matrix);
            checkBlocksInColumnsToBeTTrue(matrix);
            checkBlocksInRowsToBeTTrue(matrix);
            showMatrix(matrix);
            matrix = checkIfThereAreSpacesBigger5AndBlockBigger5InRow(matrix);
            checkBlocksInColumnsToBeTTrue(matrix);
            checkBlocksInRowsToBeTTrue(matrix);
            showMatrix(matrix);
            matrix = checkInIfThereAreSpacesBigger5AndBlockBigger5InColumn(matrix);
            showMatrix(matrix);
            checkBlocksInColumnsToBeTTrue(matrix);
            checkBlocksInRowsToBeTTrue(matrix);
            matrix = checkRowForOverlappingsWithSingleBlocks(matrix);
            showMatrix(matrix);
            checkBlocksInColumnsToBeTTrue(matrix);
            checkBlocksInRowsToBeTTrue(matrix);
            matrix = checkColumnForOverlappingsWithSingleBlocks(matrix);
            showMatrix(matrix);
            checkBlocksInColumnsToBeTTrue(matrix);
            checkBlocksInRowsToBeTTrue(matrix);
            // matrix = checkRowsForMatchingBlocksInStarBlocks(matrix);
            // showMatrix(matrix);
            // checkBlocksInColumnsToBeTTrue(matrix);
            // checkBlocksInRowsToBeTTrue(matrix);
            // matrix = checkRowsForMatchingBlocksInStarBlocks(matrix);
            // showMatrix(matrix);
            // checkBlocksInColumnsToBeTTrue(matrix);
            // checkBlocksInRowsToBeTTrue(matrix);

            matrix = fillBlocksOnEndOfColumn(matrix);

            checkBlocksInColumnsToBeTTrue(matrix);
            checkBlocksInRowsToBeTTrue(matrix);

            // matrix = checkRowForOverlappingsWithSingleBlocks(matrix);
            // checkBlocksInColumnsToBeTTrue(matrix);
            // checkBlocksInRowsToBeTTrue(matrix);
         }

      } catch (Exception e) {
         try {
            showMatrix(matrix);
         } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
         }
         // showBlockGoneTrue(matrix);
         e.printStackTrace();
      }
      // showBlockGoneTrue(matrix);
      try {
         showMatrix(matrix);
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      // showBlockGoneTrue(matrix);
      return matrix;
   }

   /**
    * gehe columns durch, bis zum 1. stern. Zähle dann die nächsten Sterne.
    * falls 1. nicht stern und nicht - ist gucken, ob 1. block gone ist, dann
    * bei index + getHowMany weiter sonst zu sternen dazuzählen! in eine
    * LinkedHashMap mit 1. index schreiben Dann blöcke !gone in Hashmap mit
    * position und länge. vergleich der beiden Listen.
    * 
    * @param matrix
    * @return
    * @throws Exception
    */
   private char[][] checkRowsForMatchingBlocksInStarBlocks(char[][] matrix) throws Exception {
      System.out.println("checkRowsForMatchingBlocksInStarBlocks");
      for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
         System.out.println(rowInt);
         Row row = riddle.getRows().get(rowInt);
         if (!row.isGone()) {
            Block biggestBlock = null;
            boolean twoBiggestBlocks = false;
            LinkedList<Block> blocks = row.getBlocks();
            if (blocks != null) {
               // finde den Größten nicht fertigen Block.
               for (Block block : blocks) {
                  if (!block.isGone()) {
                     if (biggestBlock == null) {
                        biggestBlock = block;
                     } else if (block.getHowMany() > biggestBlock.getHowMany()) {
                        biggestBlock = block;
                        twoBiggestBlocks = false;
                     } else if (block.getHowMany() == biggestBlock.getHowMany()) {
                        twoBiggestBlocks = true;
                     }
                  }
               }
               if (!twoBiggestBlocks) {
                  Integer positionOfBiggestStarArea = null;
                  Integer lengthOfBiggestStarArea = 0;

                  Integer positionOfActuallStarArea = null;
                  Integer lengthOfActuallStarArea = 0;

                  boolean twoBiggestStarAreas = false;

                  for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
                     if (matrix[rowInt][columnInt] == '*') {
                        if (null == positionOfActuallStarArea) {
                           positionOfActuallStarArea = columnInt;
                           lengthOfActuallStarArea++;
                        } else {
                           lengthOfActuallStarArea++;
                        }
                        if (columnInt == (riddle.getWidth() - 1)) {
                           if (null == positionOfBiggestStarArea) {
                              positionOfBiggestStarArea = positionOfActuallStarArea;
                              lengthOfBiggestStarArea = lengthOfActuallStarArea;
                              positionOfActuallStarArea = null;
                              lengthOfActuallStarArea = 0;
                           } else {
                              if (lengthOfActuallStarArea > lengthOfBiggestStarArea) {
                                 positionOfBiggestStarArea = positionOfActuallStarArea;
                                 lengthOfBiggestStarArea = lengthOfActuallStarArea;
                              } else if (lengthOfActuallStarArea == lengthOfBiggestStarArea) {
                                 twoBiggestStarAreas = true;
                              }
                              positionOfActuallStarArea = null;
                              lengthOfActuallStarArea = 0;
                           }
                        }
                     } else {
                        if (null == positionOfBiggestStarArea) {
                           positionOfBiggestStarArea = positionOfActuallStarArea;
                           lengthOfBiggestStarArea = lengthOfActuallStarArea;
                           positionOfActuallStarArea = null;
                           lengthOfActuallStarArea = 0;
                        } else {
                           if (lengthOfActuallStarArea > lengthOfBiggestStarArea) {
                              positionOfBiggestStarArea = positionOfActuallStarArea;
                              lengthOfBiggestStarArea = lengthOfActuallStarArea;
                           } else if (lengthOfActuallStarArea == lengthOfBiggestStarArea) {
                              twoBiggestStarAreas = true;
                           }
                           positionOfActuallStarArea = null;
                           lengthOfActuallStarArea = 0;
                        }
                     }
                     if (columnInt == (riddle.getWidth() - 1)) {

                     }
                  }
                  if (!twoBiggestStarAreas && biggestBlock.getHowMany() == lengthOfBiggestStarArea) {
                     fillAreaInRowWithChar(matrix, rowInt, positionOfBiggestStarArea, (positionOfBiggestStarArea + lengthOfBiggestStarArea), biggestBlock.getColour().getName());
                  } // TODO: checken ob eine der möglichkeiten raussfällt!
               }
            }
         }
      }

      return matrix;
   }

   /**
    * Die Methode entspricht der "Verschiebtechnik". Geht die Reihe zuerst von
    * vorne nach hinten durch und merkt sich den ersten * (startIndex). Dann
    * wird die Reihe von hinten nach vorne durchschritten und ebenfalls die
    * Position des ersten * gemerkt (endIndex). Dann werden die Farben der
    * unfertigen Blöcke unter Berücksitigung ihrer Länge in eine Liste
    * geschrieben. Diese Liste wird hinten noch mit - aufgefüllt, bis sie die
    * Länge von (endIndex - startIndex +1) erreicht hat. Die Positionen der
    * "Farb-chars" werden in einer ResultList gespeichert. Die entstandene Liste
    * wird kopiert und solange die hinterste Position nach vorne verschoben, bis
    * hinten kein - mehr ist. In jedem Schritt wird die Liste mit der
    * Ausgangsliste verglichen und die nicht passenden Positionen aus der
    * resultList gelöscht. Die verbleibenden Positionen werden in die Matrix
    * geschrieben. Es muss {@link #checkBlocksInColumnsToBeTTrue(char[][])}
    * vorher aufgerufen werden.
    * 
    * @param matrix
    *           Die alte Matrix.
    * @return Die neue Matrix.
    * @throws Exception
    *            Falls ein char in die Matrix geschrieben werden soll, obwohl an
    *            der Stelle kein * oder dieser char steht.
    */
   private char[][] checkRowForOverlappingsWithSingleBlocks(char[][] matrix) throws Exception {
      System.out.println("checkRowForOverlappingsWithSingleBlocks");
      for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
         if (!riddle.getRows().get(rowInt).isGone()) {
            if (!riddle.getRows().get(rowInt).isGone()) {
               System.out.println(rowInt);
               showMatrix(matrix);
               if (!riddle.getRows().get(rowInt).isGone()) {
                  int startIndex = 0;
                  int endIndex = 0;
                  // TODO: block von sternchen finden und in indeces schreicben.
                  boolean foundStar = false;
                  for (int i = 0; i < riddle.getWidth(); i++) {
                     if (matrix[rowInt][i] == '*') {
                        if (!foundStar) {
                           startIndex = i;
                           foundStar = true;
                        }
                     } else {
                        if (foundStar) {
                           break;
                        }
                     }
                  }
                  foundStar = false;
                  for (int i = riddle.getWidth() - 1; i > -1; i--) {
                     if (matrix[rowInt][i] == '*') {
                        if (!foundStar) {
                           endIndex = i;
                           foundStar = true;
                        }
                     } else {
                        if (foundStar) {
                           break;
                        }
                     }
                  }
                  LinkedList<String> asd = new LinkedList<String>();
                  LinkedList<String> first = new LinkedList<String>();
                  Row row = riddle.getRows().get(rowInt);
                  System.out.println("startIndex:" + startIndex + " endIndex:" + endIndex);
                  LinkedList<Block> blocks = row.getBlocks();
                  if (blocks == null) {
                     return matrix;
                  }
                  Block lastBlock = null;
                  int resultIndex = 0;
                  for (Block block : blocks) {
                     if (null != lastBlock && lastBlock.getColour().getName() == block.getColour().getName()) {
                        asd.add("-");
                        resultIndex++;
                     }

                     if (!block.isGone()) {
                        for (int i = 0; i < block.getHowMany(); i++) {
                           asd.add(String.valueOf(block.getColour().getName()));
                        }
                        resultIndex += block.getHowMany();
                        lastBlock = block;
                     }
                  }
                  int indexX = endIndex + 1 - startIndex;
                  for (int i = resultIndex; i < indexX; i++) {
                     asd.add("-");
                  }
                  first.addAll(asd);
                  System.out.println("ASD:\n" + asd);

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

                  // 0 = 5, 1= 6, 2=7 , 3=8
                  if (result.size() <= (1 + endIndex - startIndex)) {
                     for (Integer column : result) {
                        column = new Integer(column + startIndex);
                        if (column > -1 && column < (riddle.getWidth() - 1)) {
                           char charAt = first.get(column - startIndex).charAt(0);
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
                  }
               }
            }
         }
      }
      return matrix;
   }

   /**
    * Die Methode entspricht der "Verschiebtechnik". Geht die Reihe zuerst von
    * vorne nach hinten durch und merkt sich den ersten * (startIndex). Dann
    * wird die Reihe von hinten nach vorne durchschritten und ebenfalls die
    * Position des ersten * gemerkt (endIndex). Dann werden die Farben der
    * unfertigen Blöcke unter Berücksitigung ihrer Länge in eine Liste
    * geschrieben. Diese Liste wird hinten noch mit - aufgefüllt, bis sie die
    * Länge von (endIndex - startIndex +1) erreicht hat. Die Positionen der
    * "Farb-chars" werden in einer ResultList gespeichert. Die entstandene Liste
    * wird kopiert und solange die hinterste Position nach vorne verschoben, bis
    * hinten kein - mehr ist. In jedem Schritt wird die Liste mit der
    * Ausgangsliste verglichen und die nicht passenden Positionen aus der
    * resultList gelöscht. Die verbleibenden Positionen werden in die Matrix
    * geschrieben. Es muss {@link #checkBlocksInColumnsToBeTTrue(char[][])}
    * vorher aufgerufen werden.
    * 
    * @param matrix
    *           Die alte Matrix.
    * @return Die neue Matrix.
    * @throws Exception
    *            Falls ein char in die Matrix geschrieben werden soll, obwohl an
    *            der Stelle kein * oder dieser char steht.
    */
   private char[][] checkColumnForOverlappingsWithSingleBlocks(char[][] matrix) throws Exception {
      System.out.println("checkColumnForOverlappingsWithSingleBlocks");
      for (int coIntInt = 0; coIntInt < riddle.getWidth(); coIntInt++) {
         if (!riddle.getColumns().get(coIntInt).isGone()) {
            showBlockGoneTrue(matrix);
            if (!riddle.getColumns().get(coIntInt).isGone()) {
               System.out.println(coIntInt);
               showMatrix(matrix);
               if (!riddle.getColumns().get(coIntInt).isGone()) {
                  int startIndex = 0;
                  int endIndex = 0;
                  // TODO: block von sternchen finden und in indeces schreicben.
                  boolean foundStar = false;
                  for (int i = 0; i < riddle.getHeight(); i++) {
                     if (matrix[i][coIntInt] == '*') {
                        if (!foundStar) {
                           startIndex = i;
                           foundStar = true;
                        }
                     } else {
                        if (foundStar) {
                           break;
                        }
                     }
                  }
                  foundStar = false;
                  for (int i = riddle.getHeight() - 1; i > -1; i--) {
                     if (matrix[i][coIntInt] == '*') {
                        if (!foundStar) {
                           endIndex = i;
                           foundStar = true;
                        }
                     } else {
                        if (foundStar) {
                           break;
                        }
                     }
                  }
                  LinkedList<String> asd = new LinkedList<String>();
                  LinkedList<String> first = new LinkedList<String>();
                  Column columnRef = riddle.getColumns().get(coIntInt);
                  System.out.println("startIndex:" + startIndex + " endIndex:" + endIndex);
                  LinkedList<Block> blocks = columnRef.getBlocks();
                  if (blocks == null) {
                     return matrix;
                  }
                  Block lastBlock = null;
                  int resultIndex = 0;
                  for (Block block : blocks) {
                     if (null != lastBlock && lastBlock.getColour().getName() == block.getColour().getName()) {
                        asd.add("-");
                        resultIndex++;
                     }

                     if (!block.isGone()) {
                        for (int i = 0; i < block.getHowMany(); i++) {
                           asd.add(String.valueOf(block.getColour().getName()));
                        }
                        resultIndex += block.getHowMany();
                        lastBlock = block;
                     }
                  }
                  int indexX = endIndex + 1 - startIndex;
                  for (int i = resultIndex; i < indexX; i++) {
                     asd.add("-");
                  }
                  first.addAll(asd);
                  System.out.println("ASD:\n" + asd);

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

                  // 0 = 5, 1= 6, 2=7 , 3=8

                  if (result.size() <= (1 + endIndex - startIndex)) {
                     for (Integer rowRef : result) {
                        rowRef = new Integer(rowRef + startIndex);
                        char charAt = first.get(rowRef - startIndex).charAt(0);
                        if (matrix[rowRef][coIntInt] != '*' && matrix[rowRef][coIntInt] != charAt) {
                           throw new Exception("Fehler: column:" + coIntInt + " row:" + rowRef + " " + charAt + " ungleich " + matrix[coIntInt][rowRef]);
                        }
                        if (matrix[rowRef][coIntInt] != charAt) {
                           matrix[rowRef][coIntInt] = charAt;
                           if (matrix[coIntInt][rowRef] != '-') {
                              riddle.getRows().get(rowRef).setEntriesSet();
                              riddle.getColumns().get(coIntInt).setEntriesSet();
                           }
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
    * Nochnötig?
    * 
    * @param matrix
    * @return
    * @throws Exception
    */
   private char[][] findOverlappingAreasInRow(char[][] matrix) throws Exception {
      System.out.println("findOverlappingAreasInRow");
      for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
         LinkedList<String> asd = new LinkedList<String>();
         LinkedList<String> first = new LinkedList<String>();
         Row row = riddle.getRows().get(rowInt);
         System.out.println(row);
         LinkedList<Block> blocks = row.getBlocks();
         if (blocks == null) {
            return matrix;
         }
         Block lastBlock = null;
         int resultIndex = 0;
         for (Block block : blocks) {
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

   /**
    * Noch nötog?
    * 
    * @param matrix
    * @return
    * @throws Exception
    */
   private char[][] findOverlappingAreasInColumn(char[][] matrix) throws Exception {
      System.out.println("findOverlappingAreasInColumn");
      for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
         LinkedList<String> asd = new LinkedList<String>();
         LinkedList<String> first = new LinkedList<String>();
         Column column = riddle.getColumns().get(columnInt);
         System.out.println(column);
         LinkedList<Block> blocks = column.getBlocks();
         if (blocks == null) {
            return matrix;
         }
         Block lastBlock = null;
         int resultIndex = 0;
         for (Block block : blocks) {
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
    * und setzt diese true. sind.
    * 
    * @param matrix
    * @throws Exception
    */
   private char[][] checkBlocksInColumnsToBeTTrue(char[][] matrix) throws Exception {
      // Zuerst nur erste.
      System.out.println("checkBlocksInColumnsToBeTTrue");
      for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
         System.out.println("Columdsafasf:" + columnInt);
         Column column = riddle.getColumns().get(columnInt);

         LinkedList<Block> blocks = column.getBlocks();
         if (blocks != null) {
            int roInt = 0;
            int blockInt = 0;
            for (int rowInt = roInt; rowInt < riddle.getHeight();) {
               if (matrix[rowInt][columnInt] == '-') {
                  rowInt++;
               } else if (rowInt < riddle.getHeight() && matrix[rowInt][columnInt] != '*') {
                  Block colourBlock = blocks.get(blockInt);
                  if (!colourBlock.isGone()) {
                     boolean fillIt = true;
                     if (!((rowInt + colourBlock.getHowMany() - 1) < riddle.getHeight())) {
                        break;
                     }
                     for (int index = rowInt; index < rowInt + colourBlock.getHowMany(); index++) {
                        if (matrix[index][columnInt] != colourBlock.getColour().getName()) {
                           fillIt = false;
                        }
                     }
                     if (fillIt) {
                        if ((riddle.getHeight() - rowInt) >= getSpaceNeededForBlocksFromBeginningToEnd(blocks, blockInt)) {
//                           colourBlock.setGone(true);
                           rowInt += colourBlock.getHowMany();
                        } else {
                           break;
                        }

                        // if ((blockInt + 1) < blocks.size() &&
                        // colourBlock.getColour().getName() ==
                        // blocks.get(blockInt + 1).getColour().getName()) {
                        // fillAreaInColumnWithChar(matrix, columnInt, rowInt,
                        // rowInt + 1, '-');
                        // }

                     } else {
                        break;
                     }

                  } else {
                     rowInt += colourBlock.getHowMany();
                  }
               } else {
                  if ((rowInt + 1 < riddle.getHeight() && (matrix[rowInt + 1][columnInt] == '*' || !(matrix[rowInt + 1][columnInt] == '-')))) {
                     rowInt++;
                  } else {
                     break;
                  }
               }
            }
         }
      }

      // Zuerst nur erste.
      for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
         Column column = riddle.getColumns().get(columnInt);
         LinkedList<Block> blocks = column.getBlocks();
         if (blocks != null) {
            int roInt = riddle.getHeight() - 1;
            int blockInt = blocks.size() - 1;
            for (int rowInt = roInt; rowInt > -1;) {
               if (matrix[rowInt][columnInt] == '-') {
                  rowInt--;
               } else if (rowInt > -1 && matrix[rowInt][columnInt] != '*') {
                  Block colourBlock = blocks.get(blockInt);
                  if (!colourBlock.isGone()) {
                     boolean fillIt = true;
                     if (!(rowInt - colourBlock.getHowMany() > -1)) {
                        break;
                     }
                     for (int index = rowInt; index > rowInt - colourBlock.getHowMany(); index--) {
                        if (matrix[index][columnInt] != colourBlock.getColour().getName()) {
                           fillIt = false;
                        }
                     }
                     if (fillIt) {
                        if (rowInt + 1 >= getSpaceNeededForBlocksFromEndToBeginning(blocks, blockInt)) {
//                           colourBlock.setGone(true);
                           rowInt -= colourBlock.getHowMany();
                           // if ((blockInt - 1) > -1 &&
                           // colourBlock.getColour().getName() ==
                           // blocks.get(blockInt - 1).getColour().getName()) {
                           // fillAreaInColumnWithChar(matrix, columnInt,
                           // rowInt,
                           // rowInt + 1, '-');
                           // }
                        } else {
                           break;
                        }

                     } else {
                        break;
                     }

                  } else {
                     rowInt -= colourBlock.getHowMany();
                  }
               } else {
                  if (rowInt - 1 > -1 && (matrix[rowInt - 1][columnInt] == '*' || !(matrix[rowInt - 1][columnInt] == '-'))) {
                     rowInt--;
                  } else {
                     break;
                  }
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
            for (Block block : column.getBlocks()) {
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
               for (Block block : column.getBlocks()) {
//                  block.setGone(true);
               }
            }
         }
      }
      return matrix;
   }

   private int getSpaceNeededForBlocksFromEndToBeginning(LinkedList<Block> blocks, int blockInt) {
      int spaceNeeded = 0;
      for (int index = 0; index < (blockInt + 1); index++) {
         spaceNeeded += blocks.get(index).getHowMany();
         if (index != 0 && blocks.get(index).getColour().getName() == blocks.get(index - 1).getColour().getName()) {
            spaceNeeded++;
         }
      }

      return spaceNeeded;
   }

   private int getSpaceNeededForBlocksFromBeginningToEnd(LinkedList<Block> blocks, int blockInt) {
      int spaceNeeded = 0;
      for (int index = blockInt; index < blocks.size(); index++) {
         spaceNeeded += blocks.get(index).getHowMany();
         if (index != blockInt && blocks.get(index).getColour().getName() == blocks.get(index - 1).getColour().getName()) {
            spaceNeeded++;
         }
      }

      return spaceNeeded;
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
      for (int roInt = 0; roInt < riddle.getHeight(); roInt++) {
         Row row = riddle.getRows().get(roInt);
         LinkedList<Block> blocks = row.getBlocks();
         if (blocks != null) {
            int columnInt = 0;
            int blockInt = 0;
            while (columnInt < riddle.getWidth() && matrix[roInt][columnInt] == '-') {
               columnInt++;
            }
            if (columnInt < riddle.getWidth() && matrix[roInt][columnInt] != '*') {
               Block colourBlock = blocks.get(blockInt);
               if (!colourBlock.isGone()) {
                  boolean fillIt = true;
                  if (!((columnInt + colourBlock.getHowMany() - 1) < riddle.getWidth())) {
                     break;
                  }
                  for (int index = columnInt; index < columnInt + colourBlock.getHowMany(); index++) {
                     if (matrix[roInt][index] != colourBlock.getColour().getName()) {
                        fillIt = false;
                     }
                  }
                  if (fillIt) {
//                     colourBlock.setGone(true);
                     columnInt += colourBlock.getHowMany();
                     // if ((blockInt + 1) < blocks.size() &&
                     // colourBlock.getColour().getName() == blocks.get(blockInt
                     // + 1).getColour().getName()) {
                     // fillAreaInRowWithChar(matrix, roInt, columnInt,
                     // columnInt + 1, '-');
                     // }

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
      for (int roInt = 0; roInt < riddle.getHeight(); roInt++) {
         Row row = riddle.getRows().get(roInt);
         LinkedList<Block> blocks = row.getBlocks();
         if (blocks != null) {
            int columnInt = riddle.getWidth() - 1;
            int blockInt = blocks.size() - 1;
            while (columnInt > -1 && matrix[roInt][columnInt] == '-') {
               columnInt--;
            }
            if (columnInt > -1 && matrix[roInt][columnInt] != '*') {
               Block colourBlock = blocks.get(blockInt);
               if (!colourBlock.isGone()) {
                  boolean fillIt = true;
                  if (!((columnInt - colourBlock.getHowMany()) > -1)) {
                     break;
                  }
                  for (int index = columnInt; index > columnInt - colourBlock.getHowMany(); index--) {
                     if (matrix[roInt][index] != colourBlock.getColour().getName()) {
                        fillIt = false;
                     }
                  }
                  if (fillIt) {
//                     colourBlock.setGone(true);
                     columnInt -= colourBlock.getHowMany();
                     // if ((blockInt - 1) > -1 &&
                     // colourBlock.getColour().getName() == blocks.get(blockInt
                     // - 1).getColour().getName()) {
                     // fillAreaInRowWithChar(matrix, roInt, columnInt - 1,
                     // columnInt, '-');
                     // }
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
            for (Block block : column.getBlocks()) {
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
               for (Block block : column.getBlocks()) {
//                  block.setGone(true);
               }
            }
         }
      }
      return matrix;
   }

   /**
    * Wenn es eine Folge * größer als 5 und einen Block größer 5 gibt und die
    * Größen gleich sind muss der Block in die Lücke.
    * 
    * @param matrix
    * @return
    * @throws Exception
    */
   private char[][] checkIfThereAreSpacesBigger5AndBlockBigger5InRow(char[][] matrix) throws Exception {
      System.out.println("checkIfThereAreSpacesBigger5AndBlockBigger5InRow");
      for (Row row : riddle.getRows()) {
         int rowInt = getIndexOfRow(row);
         if (!row.isGone()) {
            int sizeOfBiggestStarsInRow = 0;
            int positionOfBiggestStarsInRow = 0;
            Block biggestBlock = null;
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

            if (sizeOfBiggestStarsInRow > riddle.getHeight() / 2) {
               LinkedList<Block> blocks = row.getBlocks();
               for (Block block : blocks) {
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

   /**
    * Wenn es eine Folge * größer als 5 und einen Block größer 5 gibt und die
    * Größen gleich sind muss der Block in die Lücke.
    * 
    * @param matrix
    * @return
    * @throws Exception
    */
   private char[][] checkInIfThereAreSpacesBigger5AndBlockBigger5InColumn(char[][] matrix) throws Exception {
      System.out.println("checkInIfThereAreSpacesBigger5AndBlockBigger5InColumn");
      for (Column column : riddle.getColumns()) {
         int columnInt = getIndexOfColumn(column);
         if (!column.isGone()) {
            int sizeOfBiggestStarsInColumn = 0;
            int positionOfBiggestStarsInColumn = 0;
            Block biggestBlock = null;
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

            if (sizeOfBiggestStarsInColumn > riddle.getWidth() / 2) {
               LinkedList<Block> blocks = column.getBlocks();
               for (Block block : blocks) {
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

   /**
    * Sucht nach Blöcken in der Matrix mit Lücken und füllt diese. Beginnt am
    * Anfang.
    * 
    * @param matrix
    * @return
    * @throws Exception
    */
   private char[][] checkRowForWhitespacesBetweenSameBlockFromBeginning(char[][] matrix) throws Exception {
      System.out.println("checkRowForWhitespacesBetweenSameBlockFromBeginning");
      // showMatrix(matrix);
      // gehe Rows durch
      for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
         Row row = riddle.getRows().get(rowInt);
         if (row.getBlocks() == null) {
            return matrix;
         } else {
            int columnInt = 0;
            boolean goOn = true;
            for (Block block : row.getBlocks()) {
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
//                           block.setGone(true);
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

   /**
    * Sucht nach Blöcken in der Matrix mit Lücken und füllt diese. Beginnt am
    * Ende.
    * 
    * @param matrix
    * @return
    * @throws Exception
    */
   private char[][] checkRowForWhitespacesBetweenSameBlockFromEnd(char[][] matrix) throws Exception {
      System.out.println("checkRowForWhitespacesBetweenSameBlockFromEnd");
      showMatrix(matrix);
      // gehe Rows durch

      for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
         Row row = riddle.getRows().get(rowInt);
         System.out.println("row:" + rowInt);
         if (row.getBlocks() == null) {
            return matrix;
         } else {

            boolean goOn = true;
            int columnIn = riddle.getWidth() - 1;
            for (int blockInt = row.getBlocks().size() - 1; blockInt > -1; blockInt--) {
               Block block = row.getBlocks().get(blockInt);
               if (goOn && columnIn > -1) {
                  while (columnIn > -1 && matrix[rowInt][columnIn] == '-') {
                     columnIn--;
                  }
                  if (columnIn > -1) {
                     if (matrix[rowInt][columnIn] != block.getColour().getName()) {
                        goOn = false;
                        // blockInt--;
                     } else {
                        if ((columnIn - block.getHowMany() + 1) > -1) {
                           if (matrix[rowInt][(columnIn - block.getHowMany() + 1)] != block.getColour().getName() && matrix[rowInt][(columnIn - block.getHowMany() + 1)] != '*') {
                              throw new Exception("must be !!" + rowInt + " " + columnIn + " " + matrix[rowInt][(columnIn - block.getHowMany() + 1)] + " " + block.getColour().getName());
                           }
                           fillAreaInRowWithChar(matrix, rowInt, (columnIn + 1 - block.getHowMany()), columnIn + 1, block.getColour().getName());
//                           block.setGone(true);
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
            LinkedList<Block> blocks = row.getBlocks();
            if (starCounter > 0 && blocks != null) {
               Block block = blocks.get(0);
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
               Block block = blocks.get(blocks.size() - 1);
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
      showBlockGoneTrue(matrix);
      for (Column column : riddle.getColumns()) {
         int columnInt = getIndexOfColumn(column);
         if (!column.isGone()) {
            int starCounter = 0;
            int whatToDo = 0;
            for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
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
            LinkedList<Block> blocks = column.getBlocks();
            if (starCounter > 0 && blocks != null) {
               Block block = blocks.get(0);
               if (whatToDo == 1 && (starCounter < block.getHowMany() || block.isGone())) {
                  fillAreaInColumnWithChar(matrix, columnInt, 0, starCounter, '-');
               } else {
                  // TODO: Think about it.
               }
            }
            starCounter = 0;
            whatToDo = 0;
            for (int rowInt = (riddle.getHeight() - 1); rowInt > -1; rowInt--) {
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
               Block block = blocks.get(blocks.size() - 1);
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
      showBlockGoneTrue(matrix);
      showMatrix(matrix);
      for (Column column : riddle.getColumns()) {
         if (!column.isGone()) {
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
               LinkedList<Block> blocks = column.getBlocks();
               if (blocks != null) {
                  Block colourBlock = blocks.get(0);
                  if ((rowInt + colourBlock.getHowMany()) < riddle.getHeight() && matrix[rowInt][getIndexOfColumn(column)] == colourBlock.getColour().getName()) {
                     fillAreaInColumnWithChar(matrix, getIndexOfColumn(column), rowInt, (rowInt + colourBlock.getHowMany()), colourBlock.getColour().getName());
                     if (blocks.size() > 1) {
                        Block nextBlock = blocks.get(1);
                        if (nextBlock.getColour().getName() == colourBlock.getColour().getName()) {
                           fillAreaInColumnWithChar(matrix, getIndexOfColumn(column), rowInt + colourBlock.getHowMany(), rowInt + colourBlock.getHowMany() + 1, '-');
                        }
                     }
                  } else {
                     if ((rowInt + colourBlock.getHowMany()) < riddle.getHeight()) {
                        throw new Exception("char " + matrix[rowInt][getIndexOfColumn(column)] + " at" + rowInt + "/" + getIndexOfColumn(column) + " ungleich " + colourBlock.getColour().getName());
                     }
                  }

               }
            }
         }
      }
      return matrix;
   }

   private char[][] fillBlocksOnEndOfColumn(char[][] matrix) throws Exception {
      // TODO: nicht nur für den ersten block!!
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
            LinkedList<Block> blocks = column.getBlocks();
            if (blocks != null) {
               Block colourBlock = blocks.get(blocks.size() - 1);
               if (matrix[rowInt][getIndexOfColumn(column)] == colourBlock.getColour().getName()) {
                  rowInt += 1;
                  fillAreaInColumnWithChar(matrix, getIndexOfColumn(column), (rowInt - colourBlock.getHowMany()), (rowInt), colourBlock.getColour().getName());
                  if (blocks.size() > 1) {
                     Block nextBlock = blocks.get(blocks.size() - 2);
                     if (nextBlock.getColour().getName() == colourBlock.getColour().getName()) {
                        fillAreaInColumnWithChar(matrix, getIndexOfColumn(column), rowInt - colourBlock.getHowMany() - 1, rowInt - colourBlock.getHowMany(), '-');
                     }
                  }
               } else {
                  throw new Exception("char " + matrix[getIndexOfColumn(column)][rowInt] + " ungleich " + colourBlock.getColour().getName());
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
            LinkedList<Block> blocks = row.getBlocks();
            if (blocks != null) {
               Block colourBlock = blocks.get(0);
               if (matrix[getIndexOfRow(row)][columnInt] == colourBlock.getColour().getName()) {
                  fillAreaInRowWithChar(matrix, getIndexOfRow(row), columnInt, (columnInt + colourBlock.getHowMany()), colourBlock.getColour().getName());
                  if (blocks.size() > 1) {
                     Block nextBlock = blocks.get(1);
                     if (nextBlock.getColour().getName() == colourBlock.getColour().getName()) {
                        fillAreaInRowWithChar(matrix, getIndexOfRow(row), columnInt + colourBlock.getHowMany() + 1, columnInt + colourBlock.getHowMany() + 1, '-');
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
            LinkedList<Block> blocks = row.getBlocks();
            if (blocks != null) {
               Block colourBlock = blocks.get(blocks.size() - 1);
               if (matrix[getIndexOfRow(row)][columnInt] == colourBlock.getColour().getName()) {
                  columnInt += 1;
                  fillAreaInRowWithChar(matrix, getIndexOfRow(row), (columnInt - colourBlock.getHowMany()), (columnInt), colourBlock.getColour().getName());
                  if (blocks.size() > 1) {
                     Block nextBlock = blocks.get(blocks.size() - 2);
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
            for (Block block : column.getBlocks()) {
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
            for (Block block : row.getBlocks()) {
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
            Block lastBlock = null;
            // Blocks in column durchgehen und Größe der Blocks
            // zusammenrechnen.
            for (Block block : column.getBlocks()) {
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
               for (Block block : column.getBlocks()) {
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
//                  block.setGone(true);
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
               Block lastBlock = null;
               // Blocks in row durchgehen und Größe der Blocks
               // zusammenrechnen.
               for (Block block : row.getBlocks()) {
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
                  for (Block block : row.getBlocks()) {
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
//                     block.setGone(true);
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
    * @throws Exception
    */
   private void showMatrix(char[][] matrix) throws Exception {
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
   }

   private void showBlockGoneTrue(char[][] matrix) {
      System.out.println("showBlockGoneTrue");
      for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
         Row row = riddle.getRows().get(rowInt);
         System.out.println("Row:" + rowInt + " -- " + row.isGone());
         LinkedList<Block> blocks = row.getBlocks();
         if (null != blocks) {
            for (Block block : blocks) {
               System.out.println(block.getHowMany() + "--" + block.isGone());
            }
         }
      }
      for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
         Column column = riddle.getColumns().get(columnInt);
         System.out.println("Column:" + columnInt + " -- " + column.isGone());
         LinkedList<Block> blocks = column.getBlocks();
         if (null != blocks) {
            for (Block block : blocks) {
               System.out.println(block.getHowMany() + "--" + block.isGone());
            }
         }
      }
   }
   
   private void findDuplicatePossInColumn() {
      getCol
   }

}
