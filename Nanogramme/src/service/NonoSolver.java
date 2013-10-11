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
         System.out.println("for:" + i);
         
         checkRowForMathingBlocks(matrix);
         checkColumnForMathingBlocks(matrix);
         
         checkBlocksInRowsToBeTTrue(matrix);
         checkBlocksInColumnsToBeTTrue(matrix);
         
         checkBeginAndEndOfColumnsForBlocks(matrix);
         checkBeginAndEndOfRowsForBlocks(matrix);
         
         checkBeginnigOfRowForExactSpaceForFirstBlock(matrix);
         checkBeginnigOfColumnForExactSpaceForFirstBlock(matrix);
         
         checkEndOfRowForExactSpaceForLastBlock(matrix);
         checkEndOfColumnForExactSpaceForLastBlock(matrix);
         
         checkRowForWhitespacesBetweenSameColour(matrix);
         
         checkBlocksInColumnsToBeTTrue(matrix);
         checkBlocksInRowsToBeTTrue(matrix);
         
         checkIfBeginningAndEndOfRowCanBeWhite(matrix);
         checkIfBeginningAndEndOfColumnCanBeWhite(matrix);
         
         checkIfSpaceInColumnForExactLastBlock(matrix);
         checkIfSpaceInRowForExactLastBlock(matrix);
         
         checkInColumnIfBetweenTwoSameColourBlocksComesWhite(matrix);
         
         checkBlocksInColumnsToBeTTrue(matrix);
         checkBlocksInRowsToBeTTrue(matrix);
         showMatrix(matrix);
      }
      showBlockGoneTrue(matrix);
      return matrix;
   }

   private char[][] checkInColumnIfBetweenTwoSameColourBlocksComesWhite(char[][] matrix) {
      System.out.println("checkInColumnIfBetweenTwoSameColourBlocksComesWhite");
      for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
         Column column = riddle.getColumns().get(columnInt);
         int blockNumber = 0;
         LinkedList<ColourBlock> blocks = column.getBlocks();
         ColourBlock block;
         if (null != blocks) {
            for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
               if (matrix[rowInt][columnInt] != '*' && matrix[rowInt][columnInt] != '-') {
                  if (blockNumber < blocks.size()) {
                     block = blocks.get(blockNumber);
                     int newRowInt = rowInt;
                     rowInt += block.getHowMany();
                     if ((blockNumber + 1) < blocks.size()) {
                        boolean fillIt = true;
                        for (int index = newRowInt; index < (newRowInt + block.getHowMany()) && index < riddle.getWidth(); index++) {
                           if (matrix[index][columnInt] != block.getColour().getName()) {
                              fillIt = false;
                           }
                        }
                        if (fillIt && rowInt < riddle.getHeight() && block.getColour().getName() == (blocks.get(blockNumber + 1).getColour().getName())) {
                           matrix[rowInt][columnInt] = '-';
                        }
                     }
                     blockNumber++;
                  }
               }
            }
         }
      }

      for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
         Column column = riddle.getColumns().get(columnInt);
         LinkedList<ColourBlock> blocks = column.getBlocks();
         ColourBlock block;
         if (null != blocks) {
            int blockNumber = blocks.size() - 1;
            for (int rowInt = riddle.getHeight() - 1; rowInt > 0; rowInt--) {
               if (matrix[rowInt][columnInt] != '*' && matrix[rowInt][columnInt] != '-')
                  ;
               {
                  if (blockNumber > -1) {
                     block = blocks.get(blockNumber);
                     int newRowInt = rowInt;
                     rowInt -= block.getHowMany();
                     if ((blockNumber - 1) > -1) {
                        boolean fillIt = true;
                        for (int index = newRowInt; index > -1; index--) {
                           if (matrix[index][columnInt] != block.getColour().getName()) {
                              fillIt = false;
                           }
                        }
                        
                        
                        if (fillIt  && block.getColour().getName() == (blocks.get(blockNumber - 1).getColour().getName())) {
                           matrix[rowInt][columnInt] = '-';
                        }
                     }
                     blockNumber--;
                  }
               }
            }
         }
      }

      return matrix;
   }

   private char[][] checkIfSpaceInColumnForExactLastBlock(char[][] matrix) {
      System.out.println("checkIfSpaceInColumnForExactLastBlock");
      for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
         Column column = riddle.getColumns().get(columnInt);
         int unfinishedBlockes = 0;
         int blockSum = 0;
         LinkedList<ColourBlock> blocks = column.getBlocks();
         if (null != blocks) {
            ColourBlock onlyOne = null;
            String lastColour = null;
            boolean fillIt = true;
            for (ColourBlock block : blocks) {
               if (null == lastColour) {
                  lastColour = String.valueOf(block.getColour().getName());
               } else if (!lastColour.equals(block.getColour().getName())) {
                  fillIt = false;
               }
               blockSum += block.getHowMany();
               if (!block.isGone()) {
                  unfinishedBlockes++;
                  onlyOne = block;
               }
            }
            if (unfinishedBlockes == 1) {
               int spaceCounter = 0;
               int colourCounter =0;
               for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
                  if (matrix[rowInt][columnInt] == '*') {
                     spaceCounter++;
                  } else if (matrix[rowInt][columnInt] != '-') {
                     colourCounter++;
                  }
               }
               if (onlyOne != null && spaceCounter + colourCounter == blockSum) {
                  for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
                     if (matrix[rowInt][columnInt] == '*') {
                        matrix[rowInt][columnInt] = onlyOne.getColour().getName();
                     }
                  }
                  onlyOne.setGone(true);
               }
            } else {
               int spaceCounter = 0;
               int colourCounter =0;
               for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
                  if (matrix[rowInt][columnInt] == '*') {
                     spaceCounter++;
                  } else if (matrix[rowInt][columnInt] != '-') {
                     colourCounter++;
                  }
               }
               if (fillIt && spaceCounter + colourCounter == blockSum) {
                  for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
                     if (matrix[rowInt][columnInt] == '*') {
                        matrix[rowInt][columnInt] = blocks.get(0).getColour().getName();
                     }
                  }
                  for (ColourBlock block : blocks) {
                     block.setGone(true);
                  }
               }
            }
         }
      }

      return matrix;
   }

   private char[][] checkIfSpaceInRowForExactLastBlock(char[][] matrix) {
      System.out.println("checkIfSpaceInRowForExactLastBlock");
      for (int roInt = 0; roInt < riddle.getHeight(); roInt++) {
         Row row = riddle.getRows().get(roInt);
         int unfinishedBlockes = 0;
         LinkedList<ColourBlock> blocks = row.getBlocks();
         if (null != blocks) {
            ColourBlock onlyOne = null;
            String lastColour = null;
            boolean fillIt = true;
            int blockSum = 0;
            for (ColourBlock block : blocks) {
               blockSum += block.getHowMany();
               if (null == lastColour) {
                  lastColour = String.valueOf(block.getColour().getName());
               } else if (!lastColour.equals(block.getColour().getName())) {
                  fillIt = false;
               }
               if (!block.isGone()) {
                  unfinishedBlockes++;
                  onlyOne = block;
               }
            }
            if (unfinishedBlockes == 1) {
               int spaceCounter = 0;
               int colourCounter = 0;
               for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
                  if (matrix[roInt][columnInt] == '*') {
                     spaceCounter++;
                  } else if (matrix[roInt][columnInt] != '-') {
                     colourCounter++;
                  }
               }
               if (onlyOne != null && spaceCounter + colourCounter == blockSum) {
                  for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
                     if (matrix[roInt][columnInt] == '*') {
                        matrix[roInt][columnInt] = onlyOne.getColour().getName();
                     }
                  }
                  onlyOne.setGone(true);
               }
            } else {
               int spaceCounter = 0;
               int colourCounter = 0;
               for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
                  if (matrix[roInt][columnInt] == '*') {
                     spaceCounter++;
                  } else if (matrix[roInt][columnInt] != '-') {
                     colourCounter++;
                  }
               }
               if (fillIt && spaceCounter + colourCounter == blockSum) {
                  for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
                     if (matrix[roInt][columnInt] == '*') {
                        matrix[roInt][columnInt] = blocks.get(0).getColour().getName();
                     }
                  }
                  for (ColourBlock block : blocks) {
                     block.setGone(true);
                  }
               }
            }
         }
      }
      return matrix;
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

   private char[][] checkIfBeginningAndEndOfRowCanBeWhite(char[][] matrix) {
      System.out.println("checkIfBeginningAndEndOfRowCanBeWhite");
      for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
         Row row = riddle.getRows().get(rowInt);
         int starCounter = 0;
         int whatToDo = 0;
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
         LinkedList<ColourBlock> blocks = row.getBlocks();
         if (starCounter > 0 && blocks != null) {
            ColourBlock block = blocks.get(blocks.size() - 1);
            if (whatToDo == 1 && starCounter < block.getHowMany()) {
               for (int index = riddle.getWidth() - 1; index > riddle.getWidth() - block.getHowMany(); index--) {
                  matrix[rowInt][index] = '-';
               }
            } else {

            }
         }
      }
      return matrix;
   }
   
   private char[][] checkIfBeginningAndEndOfColumnCanBeWhite(char[][] matrix) {
      System.out.println("checkIfBeginningAndEndOfColumnCanBeWhite");
      for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
         Column column = riddle.getColumns().get(columnInt);
         int starCounter = 0;
         int whatToDo = 0;
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
         LinkedList<ColourBlock> blocks = column.getBlocks();
         if (starCounter > 0 && blocks != null) {
            ColourBlock block = blocks.get(blocks.size() - 1);
            if (whatToDo == 1 && starCounter < block.getHowMany()) {
               for (int index = riddle.getHeight() - 1; index > riddle.getHeight() - block.getHowMany(); index--) {
                  matrix[index][columnInt] = '-';
               }
            } else {

            }
         }
      }
      return matrix;
   }

   
   private char[][] checkRowForWhitespacesBetweenSameColour(char[][] matrix) {
      System.out.println("checkRowForWhitespacesBetweenSameColour");
      // showMatrix(matrix);
      // gehe Rows durch
      for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
         Row row = riddle.getRows().get(rowInt);
         if (row.getBlocks() == null) {
            return matrix;
         } else {
            int columnInt = 0;
            for (ColourBlock block : row.getBlocks()) {
               if (columnInt < riddle.getWidth()) {
                  // if (block.getHowMany() > 2) {
                  while (columnInt < riddle.getWidth() && (matrix[rowInt][columnInt] == '*' || matrix[rowInt][columnInt] == '-')) {
                     columnInt++;
                  }
                  if (columnInt < riddle.getWidth()) {
                     if (block.isGone()) {
                        columnInt += block.getHowMany();
                     } else if (matrix[rowInt][columnInt] != block.getColour().getName()) {

                     } else {
                        if ((columnInt + block.getHowMany() - 1) < riddle.getWidth()) {
                           boolean toFill = true;
                           if (matrix[rowInt][(columnInt + block.getHowMany() - 1)] != block.getColour().getName()) {
                              toFill = false;
                           }
                           if (toFill) {
                              for (int index = (columnInt + 1); index < (columnInt + block.getHowMany() - 1); index++) {
                                 if (matrix[rowInt][index] != '*') {
                                    toFill = false;
                                 }
                              }
                           }
                           if (toFill) {
                              fillWhitespacesBetweenInRow(rowInt, columnInt, block.getHowMany(), block.getColour().getName(), matrix);
                              block.setGone(true);
                           }
                        }
                     }
                     // }
                  }
               }
            }
         }

      }
      // showMatrix(matrix);
      return matrix;
   }

   private void fillWhitespacesBetweenInRow(int rowInt, int columnInt, Integer howMany, char color, char[][] matrix) {
      System.out.println("fillWhitespacesBetweenInRow");
      for (int index = columnInt; index < columnInt + howMany; index++) {
         matrix[rowInt][index] = color;
      }
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
               // überprüfen, ob Feld * ist.
               if (matrix[roInt][columnInt] == '*') {
                  boolean toFill = true;
                  // überprüfe, ob alle Felder * sind (bis Breite Block)
                  for (int index = columnInt; index < (columnInt + block.getHowMany()); index++) {
                     if (matrix[roInt][index] != '*' && matrix[roInt][index] != block.getColour().getName()) {
                        toFill = false;
                     }
                  }

                  if (toFill) {
                     if ((columnInt + block.getHowMany()) >= riddle.getWidth() || matrix[roInt][columnInt + block.getHowMany()] == '*'
                           || matrix[roInt][columnInt + block.getHowMany()] == block.getColour().getName()) {
                        toFill = false;
                     }
                     for (int index = columnInt; index < riddle.getWidth(); index++) {
                        if (matrix[roInt][index] == block.getColour().getName()) {
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
                        matrix[roInt][index] = block.getColour().getName();
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
   private char[][] checkBeginnigOfColumnForExactSpaceForFirstBlock(char[][] matrix) {
      System.out.println("checkBeginnigOfColumnForExactSpaceForFirstBlock");
      // showMatrix(matrix);
      // gehe Rows durch
      for (int columnInt = 0; columnInt < riddle.getHeight(); columnInt++) {
         Column column = riddle.getColumns().get(columnInt);
         ColourBlock block = null;
         if (column != null && column.getBlocks() != null) {
            block = column.getBlocks().get(0);

            if (null != block && !block.isGone() && block.getHowMany() > 1) {
               int rowInt = 0;
               // vorhangeln bis ersten *
               while (rowInt < riddle.getHeight() && matrix[columnInt][rowInt] == '-') {
                  rowInt++;
               }
               // überprüfen, ob Feld * ist.
               if (matrix[rowInt][columnInt] == '*') {
                  boolean toFill = true;
                  // überprüfe, ob alle Felder * sind (bis Breite Block)
                  for (int index = rowInt; index < (rowInt + block.getHowMany()); index++) {
                     if (matrix[index][columnInt] != '*' && matrix[index][columnInt] != block.getColour().getName()) {
                        toFill = false;
                     }
                  }

                  if (toFill) {
                     if ((rowInt + block.getHowMany()) >= riddle.getHeight() || matrix[rowInt + block.getHowMany()][columnInt] == '*'
                           || matrix[rowInt + block.getHowMany()][columnInt] == block.getColour().getName()) {
                        toFill = false;
                     }
                     for (int index = rowInt; index < riddle.getHeight(); index++) {
                        if (matrix[index][columnInt] == block.getColour().getName()) {
                           toFill = false;
                           break;
                        }
                        if (matrix[index][columnInt] == '-' || matrix[index][columnInt] != '*') {
                           break;
                        }
                     }
                  }
                  if (toFill) {
                     for (int index = rowInt; index < (rowInt + block.getHowMany()); index++) {
                        matrix[index][columnInt] = block.getColour().getName();
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
      for (int roInt = riddle.getHeight() - 1; roInt > -1; roInt--) {
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
                     if (matrix[roInt][index] != '*' && matrix[roInt][index] != block.getColour().getName()) {
                        toFill = false;
                     }
                  }
                  if (toFill) {
                     if ((columnInt - block.getHowMany()) < 0 || matrix[roInt][columnInt - block.getHowMany()] == '*'
                           || matrix[roInt][columnInt - block.getHowMany()] == block.getColour().getName()) {
                        toFill = false;
                     }
                     for (int index = columnInt; index > columnInt - block.getHowMany(); index--) {
                        if (matrix[roInt][index] == block.getColour().getName()) {
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
                        matrix[roInt][index] = block.getColour().getName();
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
   private char[][] checkEndOfColumnForExactSpaceForLastBlock(char[][] matrix) {
      System.out.println("checkEndOfColumnForExactSpaceForLastBlock");
      // showMatrix(matrix);
      // gehe Rows durch
      for (int columnInt = riddle.getWidth() - 1; columnInt > -1; columnInt--) {
         Column column = riddle.getColumns().get(columnInt);
         ColourBlock block = null;
         if (column != null && column.getBlocks() != null) {
            block = column.getBlocks().get(column.getBlocks().size() - 1);

            if (null != block && !block.isGone() && block.getHowMany() > 1) {
               int rowInt = riddle.getHeight() - 1;
               // vorhangeln bis ersten *
               while (rowInt > -1 && matrix[rowInt][columnInt] == '-') {
                  rowInt--;
               }
               if (matrix[rowInt][columnInt] == '*') {
                  boolean toFill = true;
                  for (int index = rowInt; index > (rowInt - block.getHowMany()); index--) {
                     if (matrix[index][columnInt] != '*' && matrix[index][columnInt] != block.getColour().getName()) {
                        toFill = false;
                     }
                  }
                  if (toFill) {
                     if ((rowInt - block.getHowMany()) < 0 || matrix[rowInt - block.getHowMany()][columnInt] == '*'
                           || matrix[rowInt - block.getHowMany()][columnInt] == block.getColour().getName()) {
                        toFill = false;
                     }
                     for (int index = rowInt; index > rowInt - block.getHowMany(); index--) {
                        if (matrix[index][columnInt] == block.getColour().getName()) {
                           toFill = false;
                           break;
                        }
                        if (matrix[index][columnInt] == '-' || matrix[index][columnInt] != '*') {
                           break;
                        }
                     }
                  }
                  if (toFill) {
                     for (int index = rowInt; index > (rowInt - block.getHowMany()); index--) {
                        matrix[index][columnInt] = block.getColour().getName();
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
    * �berpr�ft den Anfang und das Ende der Columns, ob ein angefangener Block
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
            if (column.getBlocks() != null && column.getBlocks().get(0).getColour().getName() == matrix[0][columnInt]) {
               for (int index = 0; index < column.getBlocks().get(0).getHowMany(); index++) {
                  matrix[index][columnInt] = column.getBlocks().get(0).getColour().getName();
               }
               column.getBlocks().get(0).setGone(true);
            }
         }

         if (matrix[riddle.getHeight() - 1][columnInt] != '-' && matrix[riddle.getHeight() - 1][columnInt] != '*') {
            if (column.getBlocks() != null && column.getBlocks().get(column.getBlocks().size() - 1).getColour().getName() == matrix[riddle.getHeight() - 1][columnInt]) {
               for (int index = riddle.getHeight() - 1; index > riddle.getHeight() - column.getBlocks().get(column.getBlocks().size() - 1).getHowMany() - 1; index--) {
                  matrix[index][columnInt] = column.getBlocks().get(column.getBlocks().size() - 1).getColour().getName();
               }
               column.getBlocks().get(column.getBlocks().size() - 1).setGone(true);
            }
         }

      }
      return matrix;
   }

   /**
    * �berpr�ft den Anfang und das Ende der Columns, ob ein angefangener Block
    * eingetragen ist.
    * 
    * @param matrix
    * @return
    */
   private char[][] checkBeginAndEndOfRowsForBlocks(char[][] matrix) {
      System.out.println("checkBeginAndEndOfRowsForBlocks");
      for (int roInt = 0; roInt < riddle.getHeight(); roInt++) {
         Row row = riddle.getRows().get(roInt);
         if (matrix[roInt][0] != '-' && matrix[roInt][0] != '*') {
            if (row.getBlocks() != null && row.getBlocks().get(0).getColour().getName() == matrix[roInt][0]) {
               for (int index = 0; index < row.getBlocks().get(0).getHowMany(); index++) {
                  matrix[roInt][index] = row.getBlocks().get(0).getColour().getName();
               }
               row.getBlocks().get(0).setGone(true);
            }
         }

         if (matrix[roInt][riddle.getWidth() - 1] != '-' && matrix[roInt][riddle.getWidth() - 1] != '*') {
            if (row.getBlocks() != null && row.getBlocks().get(row.getBlocks().size() - 1).getColour().getName() == matrix[roInt][riddle.getWidth() - 1]) {
               for (int index = riddle.getWidth() - 1; index > riddle.getWidth() - row.getBlocks().get(row.getBlocks().size() - 1).getHowMany() - 1; index--) {
                  matrix[roInt][index] = row.getBlocks().get(row.getBlocks().size() - 1).getColour().getName();
               }
               row.getBlocks().get(row.getBlocks().size() - 1).setGone(true);
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
         for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
            if (matrix[rowInt][columnInt] == '-' && (columnInt == 0 || columnInt == (riddle.getWidth() - 1))) {
               count++;
            }
         }
         if (null != row && null != row.getBlocks()) {
            for (int blockInt = 0; blockInt < row.getBlocks().size(); blockInt++) {
               ColourBlock block = row.getBlocks().get(blockInt);
               count += block.getHowMany();
               if ((blockInt + 1) < row.getBlocks().size() && block.getColour().getName() == (row.getBlocks().get(blockInt + 1).getColour().getName())) {
                  count++;
               }
            }
         }
         if (count == riddle.getWidth()) {
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
                     matrix[rowInt][k] = block.getColour().getName();
                  }
                  block.setGone(true);
                  newBlocks.remove(block);
                  columnInt += block.getHowMany();
               }
               if ((blockInt + 1) < row.getBlocks().size() && block.getColour().getName() == (row.getBlocks().get(blockInt + 1).getColour().getName())) {
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
               if ((blockInt + 1) < column.getBlocks().size() && block.getColour().getName() == (column.getBlocks().get(blockInt + 1).getColour().getName())) {
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
                     matrix[k][columnInt] = block.getColour().getName();
                  }
                  block.setGone(true);
                  newBlocks.remove(block);
                  rowInt += block.getHowMany();
               }
               if ((blockInt + 1) < column.getBlocks().size() && block.getColour().getName() == (column.getBlocks().get(blockInt + 1).getColour().getName())) {
                  matrix[rowInt][columnInt] = '-';
                  rowInt++;
               }
            }
            blocks = newBlocks;
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
   }
   
   private char[][] checkRowForRealyMatchingSpaces(char[][] matrix) {
      System.out.println("checkRowForRealyMatchingSpaces");
      for (int rowInt = 0; rowInt < riddle.getWidth(); rowInt++) {
         Row row = riddle.getRows().get(rowInt);
         LinkedList<ColourBlock> blocks = row.getBlocks();
         int columnInt = 0;
         if (null != row && null != blocks) {
            for (ColourBlock block : blocks) {
               if (matrix[rowInt][columnInt] == '-') {
                  columnInt++;
               } else if (block.isGone()) {
                  columnInt += block.getHowMany();
               } else {
                  if (matrix[rowInt][(columnInt + block.getHowMany() - 1)] != '*' && matrix[rowInt][(columnInt + block.getHowMany() + 1)] != block.getColour().getName()) {
                     for (int index = columnInt; index < (columnInt + block.getHowMany()); index++) {
                        matrix[rowInt][index] = block.getColour().getName();
                     }
                     block.setGone(true);
                     columnInt += block.getHowMany();
                  }
               }
            }
         }
      }
      return matrix;
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
               if (blockBefore != null && blockBefore.getColour().getName() == (block.getColour().getName())) {
                  count++;
               }
               blockBefore = block;
               count += block.getHowMany();
            }
            if (count == riddle.getHeight()) {
//               matrix = fillColumn(j, column, matrix);
            }
         } else {
//            matrix = fillColumnWithEmpty(j, matrix);

         }
      }
      return matrix;
   }

   /**
    * �berpr�ft, ob es �berhaupt Bl�cke in der Row gibt. Wenn nicht wird die Row
    * mit "-" aufgef�llt. Wenn ja, dann wird die Gr��e der Bl�cke aufaddiert
    * (wenn gleich Farben aufeinanderfolgen wird zus�tzlich 1 addiert. Wenn dann
    * die Anzahl = der Breite ist, werden die Bl�cke in die Matrix �bertragen
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
               if (blockBefore != null && blockBefore.getColour().getName() == (block.getColour().getName())) {
                  count++;
               }
               blockBefore = block;
               count += block.getHowMany();
            }
            if (count == riddle.getHeight()) {
//               matrix = fillRow(j, row, matrix);
            }
         } else {
//            matrix = fillRowWithEmpty(j, matrix);
         }
      }
      return matrix;
   }

   /**
    * F�llt die komplette Row mit "-".
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
    * F�llt die komplette Column mit "-".
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
    * F�llt die Column mit den Bl�cken und etwaigen "-",
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
         if (lastBlock != null && lastBlock.getColour().getName() == (block.getColour().getName())) {
            matrix[row][number] = '-';
            row++;
         }
         for (int times = 0; times < block.getHowMany(); times++) {
            matrix[row][number] = block.getColour().getName();
            row++;
         }
         lastBlock = block;
      }
      System.out.println(newMatrix);
      return newMatrix;
   }

   /**
    * F�llt die Row mit den Bl�cken und etwaigen "-",
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
         if (lastBlock != null && lastBlock.getColour().getName() == (block.getColour().getName())) {
            matrix[number][column] = '-';
            column++;
         }
         for (int times = 0; times < block.getHowMany(); times++) {
            matrix[number][column] = block.getColour().getName();
            column++;
         }
         lastBlock = block;
      }
      System.out.println(newMatrix);
      return newMatrix;
   }

}
