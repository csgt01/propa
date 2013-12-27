package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;

import models.Block;
import models.Column;
import models.Riddle;
import models.Row;
import models.SolveStateEnum;
import models.StackHolder;
import de.feu.propra.nonogramme.interfaces.INonogramSolver;
import excetions.DataCollisionException;
import excetions.NotSolvableException;

/**
 * Klasse, die das Lösen eines Nonogramms übernimmt. Um eine Lösung zu erhalten
 * muss nur {@link #getSolution()} aufgerufen werden und gegebenfalls
 * {@link #solveState} betrachtet werden.
 * 
 * @author csgt
 * 
 */
public class NonoSolver implements INonogramSolver {

   /**
    * Zu nutzende {@link RiddleService}
    */
   private RiddleService riddleLoader;

   /**
    * Das zu lösende {@link Riddle}.
    */
   private Riddle riddle;

   /**
    * Die Matrix mit dem aktuellen Stand der Lösung.
    */
   private char[][] matrix;

   /**
    * Der Status beim Lösen.
    */
   private SolveStateEnum solveState = SolveStateEnum.SOLVING;
   /**
    * Lösungen, die beim raten der Lösung gefunden wurden.
    */
   ArrayList<char[][]> solutionsFromGuising = new ArrayList<char[][]>();
   /**
    * Liste der StackHolder, die bei jedem Raten hinzugefügt werden.
    */
   LinkedList<StackHolder> stacks = new LinkedList<StackHolder>();

   /**
    * Konstruktor. Er kann mit einer bereits gefüllten matrix und einem
    * bestehenden Rätsel aufgerufen werden. Sonst wird eine matrix
    * initialisiert.
    * 
    * @param matrix
    *           .
    * @param riddle
    *           .
    */
   public NonoSolver(char[][] matrix, Riddle riddle) {

      if (matrix == null) {

      } else {
         this.matrix = matrix;
      }

      this.riddle = riddle;

   }

   /**
    * Konstruktor.
    */
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
      // // String methodName = "openFile(" + arg0 + ")";
      // // // System.out.println(methodName);
      riddleLoader = new RiddleService(null);
      riddle = riddleLoader.readFile(arg0);
      matrix = riddleLoader.matrix;
   }

   @Override
   public char[][] getSolution() {
      if (matrix == null) {
         setupMatrix();
      }
      setupBlocks();
      handle();
      showMatrix();
      return matrix;
   }

   /**
    * Steuert den Lösungsprozess. Je nach Status von {@link #solveState} wird
    * {@link #solve()}, {@link #setFirstStarToSomething()} oder
    * {@link #changeLastStacksMember()} aufgerufen oder die Methode abgebrochen.
    * 
    * 
    */
   private void handle() {
      while (solveState != SolveStateEnum.SOLVED) {
         if (solveState == SolveStateEnum.ERROR) {
            if (stacks != null && stacks.size() > 0) {
               if (!changeLastStacksMember()) {
                  switch (solutionsFromGuising.size()) {
                  case 0:
                     solveState = SolveStateEnum.NO_SOLUTION;
                     // return matrix;
                     return;
                  case 1:
                     matrix = solutionsFromGuising.get(0);
                     solveState = SolveStateEnum.SOLVED;
                     showMatrix();
                     matrix = solutionsFromGuising.get(0);
                     // return solutionsFromGuising.get(0);
                     return;
                  default:
                     solveState = SolveStateEnum.MULTIPLE_SOLUTIONS;
                     showMatrix();
                     return;
                  }
               }
            } else {
               if (solutionsFromGuising == null || solutionsFromGuising.size() != 1) {
                  showMatrix();
                  solveState = SolveStateEnum.NO_SOLUTION;
                  // return matrix;
                  return;
               } else {
                  matrix = solutionsFromGuising.get(0);
                  solveState = SolveStateEnum.SOLVED;
                  showMatrix();
                  matrix = solutionsFromGuising.get(0);
                  // return solutionsFromGuising.get(0);
                  return;
               }
            }
         } else if (solveState == SolveStateEnum.MUST_GUESS) {
            try {
               setFirstStarToSomething();
               solveState = SolveStateEnum.SOLVING;
            } catch (Exception e) {
               e.printStackTrace();
            }
         } else if (solveState == SolveStateEnum.FOUND_SOLUTION_WITH_STACK) {
            if (!changeLastStacksMember()) {
               switch (solutionsFromGuising.size()) {
               case 0:
                  showMatrix();
                  solveState = SolveStateEnum.NO_SOLUTION;
                  // return n1ull;
                  return;
               case 1:
                  matrix = solutionsFromGuising.get(0);
                  solveState = SolveStateEnum.SOLVED;
                  showMatrix();
                  matrix = solutionsFromGuising.get(0);
                  // return solutionsFromGuising.get(0);
                  return;
               default:
                  solveState = SolveStateEnum.MULTIPLE_SOLUTIONS;
                  showMatrix();
                  // return null;
                  return;
               }
            }
            solveState = SolveStateEnum.SOLVING;
         }
         solveState = SolveStateEnum.SOLVING;
         solve();
      }
   }

   /**
    * Ruft die eigentlichen Methoden zum Lösen solange auf, bis sich keine
    * Veränderung mehr in einem Durchlauf ergibt. Dann wird der
    * {@link #solveState} entweder auf {@link SolveStateEnum#MUST_GUESS}
    * gesetzt, falls noch '*' in der matrix sind. Anderenfalls auf
    * {@link SolveStateEnum#SOLVED} wenn der Stack leer ist oder auf
    * {@link SolveStateEnum#FOUND_SOLUTION_WITH_STACK} falls im Stack
    * Stackholder sind.
    * 
    * @return solveState:
    */
   private SolveStateEnum solve() {
      // System.out.println("solve()");
      try {
         boolean run1 = true;
         while (run1) {
            int starCount = getStarCountInRiddle();
            fillBlocksOnBeginning();
            fillBlocksOnEnd();
            checkRowsAndColumnsForGone();
            checkByBlock();
            if (starCount <= getStarCountInRiddle()) {
               solveState = SolveStateEnum.MUST_GUESS;
               // solveState = SolveStateEnum.ERROR;
               // showMatrix();
               // showBlockGoneTrue();
               run1 = false;
            }
            // mögliche Lösung gefunden
            if (getStarCountInRiddle() == 0) {
               // direkte Lösung, da vorher nicht geraten wurde
               if (stacks == null || stacks.size() == 0) {
                  solveState = SolveStateEnum.SOLVED;
                  run1 = false;
                  if (solutionFromTryingOk()) {
                     // TODO: testen ob Möglichkeit ok!
                     solutionsFromGuising.add(matrix);
                     // andere Möglichkeiten beim Raten testen!
                  }
               } else {
                  run1 = false;
                  // TODO: testen ob Möglichkeit ok!
                  solutionsFromGuising.add(matrix);
                  solveState = SolveStateEnum.FOUND_SOLUTION_WITH_STACK;
               }
            }
         }
      } catch (Exception e) {
         // e.printStackTrace();
         solveState = SolveStateEnum.ERROR;
      } finally {
         // showMatrix();
         // showBlockGoneTrue();
      }
      return solveState;
   }

   /**
    * Prüft für jeden Block in dieser Reihe, ob einen Block gibt, der sich mit
    * diesem überschneidet. Dabei wird jeder Block in jeder Splate zwischen
    * minStartIndexNew und maxEndIndexNew betrachtet. Wenn es keinen Block gibt,
    * kann unter Vorraussetzungen minStartIndexNew oder maxEndIndexNEw geändert
    * werden.
    * 
    * @param row
    *           Reihe.
    * @throws Exception
    *            in Block
    */
   private void checkBlocksIfIndexIsOk(Row row) throws Exception {
      if (row.isGone()) {
         return;
      }
      int rowInt = row.getIndex();
      int size = row.getBlocks().size();
      // jeden Block durchgehen
      for (int blockInt = 0; blockInt < size; blockInt++) {
         Block block = row.getBlocks().get(blockInt);
         if (!block.isGone()) {
            // für jede Column zwischen min und max prüfen
            for (int columnInt = block.getMinStartIndexNew(); columnInt <= block.getMaxEndIndexNew(); columnInt++) {
               Column column = getColumns().get(columnInt);
               int index = 0;
               int size2 = column.getBlocks().size();
               boolean isPresent = false;
               // Wenn es einen Block in der Column gibt, der sich mit dieser
               // Stelle überschneidet abbrechen.
               while (index < size2 && !isPresent) {
                  Block columnBlock = column.getBlocks().get(index);
                  if (block.getColorChar() == columnBlock.getColorChar()) {
                     if (rowInt >= columnBlock.getMinStartIndexNew() && rowInt <= columnBlock.getMaxEndIndexNew()) {
                        isPresent = true;
                     }
                  }
                  index++;
               }
               // wenn es keine Überschneidungen gibt prüfen, ob sich Bereiche
               // am Anfang oder am Ende ergeben, die kleiner sind als howMany
               if (!isPresent) {
                  if ((columnInt - block.getMinStartIndexNew() + 1) < block.getHowMany()) {
                     block.setMinStartIndexNew((columnInt + 1));
                  }
                  if ((block.getMaxEndIndexNew() - columnInt) < block.getHowMany()) {
                     block.setMaxEndIndexNew((columnInt - 1));
                  }
               }
            }
         }
      }
   }

   /**
    * Prüft für jeden Block in dieser Spalte, ob es einen Block gibt, der sich
    * mit diesem überschneidet. Dabei wird jeder Block in jeder Splate zwischen
    * minStartIndexNew und maxEndIndexNew betrachtet. Wenn es keinen Block gibt,
    * kann unter Vorraussetzungen minStartIndexNew oder maxEndIndexNEw geändert
    * werden.
    * 
    * @param column
    *           Spalte
    * @throws Exception
    *            in Block
    */
   private void checkBlocksIfIndexIsOk(Column column) throws Exception {
      if (column.isGone()) {
         return;
      }
      int columnInt = column.getIndex();
      int size = column.getBlocks().size();
      // jeden Block durchgehen
      for (int blockInt = 0; blockInt < size; blockInt++) {
         Block block = column.getBlocks().get(blockInt);
         if (!block.isGone()) {
            // für jede Row zwischen min und max prüfen
            for (int rowInt = block.getMinStartIndexNew(); rowInt <= block.getMaxEndIndexNew(); rowInt++) {
               Row row = getRows().get(rowInt);
               int index = 0;
               int size2 = row.getBlocks().size();
               boolean isPresent = false;
               while (index < size2 && !isPresent) {
                  Block rowBlock = row.getBlocks().get(index);
                  if (block.getColorChar() == rowBlock.getColorChar()) {
                     if (columnInt >= rowBlock.getMinStartIndexNew() && columnInt <= rowBlock.getMaxEndIndexNew()) {
                        isPresent = true;
                     }
                  }
                  index++;
               }
               // wenn es keine Überschneidungen gibt prüfen, ob sich Bereiche
               // am Anfang oder am Ende ergeben, die kleiner sind als howMany
               if (!isPresent) {
                  if ((rowInt - block.getMinStartIndexNew() + 1) < block.getHowMany()) {
                     block.setMinStartIndexNew((rowInt + 1));
                  }
                  if ((block.getMaxEndIndexNew() - rowInt) < block.getHowMany()) {
                     block.setMaxEndIndexNew((rowInt - 1));
                  }
               }
            }
         }
      }
   }

   private boolean solutionFromTryingOk() {
      // System.out.println("solutionFromTryingOk()");

      return true;
   }

   /**
    * Prüft ob die Matrix korrekt ist.
    * 
    * @param checkMatrix
    * @param rowIn
    * @return true, wenn die Reihe korrekt ist
    */
   protected boolean checkStateOfWrittenMatrixByRow(char[][] checkMatrix, int rowIn) {
      // String methodName = "checkStateOfWrittenMatrix()";
      // // System.out.println(methodName);
      // Date startTime = new Date();
      StringBuilder out = new StringBuilder("");
      showRow(out, rowIn);
      // System.out.println(rowIn);
      // System.out.println(out);
      int rowInt = rowIn;
      char empty = '-';
      Row row = getRows().get(rowInt);
      ArrayList<Block> blocks = row.getBlocks();
      if (blocks != null && blocks.size() > 0) {
         int columnInt = 0;
         int blockInt = 0;
         while (columnInt < riddle.getWidth()) {
            if (checkMatrix[rowInt][columnInt] == empty) {
               columnInt++;
            } else {
               // Ist eine Farbe, aber keine Blöcke mehr!
               if (blockInt >= blocks.size()) {
                  System.out.println("false");
                  return false;
               } else {
                  // Block prüfen
                  Block block = blocks.get(blockInt);
                  for (int i = 0; i < block.getHowMany(); i++) {
                     if (!(checkMatrix[rowInt][columnInt] == block.getColorChar())) {
                        System.out.println("false");
                        return false;
                     }
                  }
                  columnInt += block.getHowMany();
                  // Nächster Block ist gleiche Farbe, also muss -
                  // sein!
                  if ((blockInt + 1) < blocks.size() && block.getColourString().equals(blocks.get(blockInt + 1).getColourString()) && !(checkMatrix[rowInt][columnInt] == empty)) {
                     System.out.println("false");
                     return false;
                  }
                  blockInt++;
               }
            }
         }
      } else {
         // nur - !!!
         for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
            if (!(checkMatrix[rowInt][columnInt] == empty)) {
               // // System.out.println("false4: row: " + rowInt
               // + " column: " + columnInt);
               System.out.println("false");
               return false;
            }
         }
      }
      return true;
   }

   /**
    * Überprüft, ob es in Reihe und Spalte einen Block mit der Farbe gibt.
    * 
    * @param rowInt
    *           Reihenindex
    * @param columnInt
    *           Spaltenindex
    * @param indexOfColor
    *           Farbindex aus riddle.getColours()
    * @return true, falls in Reihe und Spalte die Farbe in einem Block vorkimmt.
    */
   protected boolean isColorInThisRowAndColumn(int rowInt, int columnInt, int indexOfColor) {
      Row row = getRows().get(rowInt);
      boolean isPresent = false;
      int index = 0;
      int size = row.getBlocks().size();
      while (index < size && !isPresent) {
         Block block = row.getBlocks().get(index);
         if (block.getColorChar() == riddle.getColours().get(indexOfColor).getName()) {
            if (columnInt >= block.getMinStartIndexNew() && columnInt <= block.getMaxEndIndexNew()) {
               isPresent = true;
            }

         }
         index++;
      }
      if (!isPresent) {
         return false;
      }
      Column column = getColumns().get(columnInt);
      index = 0;
      size = column.getBlocks().size();
      isPresent = false;
      while (index < size && !isPresent) {
         Block block = column.getBlocks().get(index);
         if (block.getColorChar() == riddle.getColours().get(indexOfColor).getName()) {
            if (rowInt >= block.getMinStartIndexNew() && rowInt <= block.getMaxEndIndexNew()) {
               isPresent = true;
            }
         }
         index++;
      }
      return isPresent;
   }

   /**
    * Holt den zuletzt hinzugefügten Stackholder und ändert die Farbe. Das
    * Riddle und die Matrix werden auf den Stand, der im Stackholder gespeichert
    * ist zurückgesetzt. Falls alle Farben getestet wurden, wird der Stackholder
    * entfernt und diese Methode nochmal aufgerufen, um den vorherigen
    * Stackholder zu ändern.
    * 
    * @return false, wenn stacks null oder leer ist, also alle Möglichkeiten
    *         getestet wurden.
    */
   private boolean changeLastStacksMember() {
      // System.out.println("changeLastStacksMember");
      // System.out.println("stacks size:" + stacks.size());
      // showMatrix();
      if (stacks != null && stacks.size() > 0) {
         StackHolder lastStackHolder = stacks.get(stacks.size() - 1);
         int indexOfColor = lastStackHolder.getIndexOfColor();
         indexOfColor--;
         // indexOfColor++;
         while (indexOfColor > -1 && !isColorInThisRowAndColumn(lastStackHolder.getRow(), lastStackHolder.getColumn(), indexOfColor)) {
            indexOfColor--;
         }
         if (indexOfColor < -1) {
            // if (indexOfColor >= riddle.getColours().size()) {
            Riddle stackRiddle = lastStackHolder.getRiddle();
            riddle = new Riddle(stackRiddle.getColours(), stackRiddle.getWidth(), stackRiddle.getHeight(), stackRiddle.getRows(), stackRiddle.getColumns(), stackRiddle.getNono());
            matrix = new char[riddle.getHeight()][riddle.getWidth()];
            for (int i = 0; i < riddle.getHeight(); i++) {
               // str += "\n";
               for (int j = 0; j < riddle.getWidth(); j++) {
                  // str += matrix[i][j] + " ";
                  matrix[i][j] = lastStackHolder.getMatrix()[i][j];
               }
            }
            stacks.removeLast();
            if (!changeLastStacksMember()) {
               return false;
            }
         } else {
            lastStackHolder.setIndexOfColor(indexOfColor);
            Riddle stackRiddle = lastStackHolder.getRiddle();
            riddle = new Riddle(stackRiddle.getColours(), stackRiddle.getWidth(), stackRiddle.getHeight(), stackRiddle.getRows(), stackRiddle.getColumns(), stackRiddle.getNono());
            matrix = new char[riddle.getHeight()][riddle.getWidth()];
            for (int i = 0; i < riddle.getHeight(); i++) {
               for (int j = 0; j < riddle.getWidth(); j++) {
                  matrix[i][j] = lastStackHolder.getMatrix()[i][j];
               }
            }
            try {
               if (indexOfColor > -1) {
                  writeCharInMatrix(lastStackHolder.getRow(), riddle.getColours().get(indexOfColor).getName(), lastStackHolder.getColumn());
               } else {
                  writeCharInMatrix(lastStackHolder.getRow(), '-', lastStackHolder.getColumn());
               }
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
         return true;
      } else {
         return false;
      }
   }

   /**
    * Setzt den ersten "*" in der Matrix auf "-". Des weiteren wird ein
    * StackHolder-Objekt angelegt, um den Status des Lösungsweges zu speichern.
    * Überprüft nun auch, ob es die Farbe, die geraten werden soll, in der Reihe
    * und Spalte gibt.
    * 
    * @throws Exception
    *            kann nicht passieren.
    */
   private void setFirstStarToSomething() throws Exception {
      // System.out.println("setFirstStarToSomething()");
      // System.out.println("stacks size:" + stacks.size());
      // showMatrix();
      for (int row = 0; row < riddle.getHeight(); row++) {
         for (int column = 0; column < riddle.getWidth(); column++) {
            if (matrix[row][column] == '*') {
               int colorIndex = (riddle.getColours().size() - 1);
               // int colorIndex = (-1);
               while (colorIndex > -1 && !isColorInThisRowAndColumn(row, column, colorIndex)) {
                  colorIndex--;
               }
               StackHolder stack = new StackHolder();
               stack.setRiddle(new Riddle(riddle.getColours(), riddle.getWidth(), riddle.getHeight(), riddle.getRows(), riddle.getColumns(), riddle.getNono()));
               stack.setMatrix(matrix, riddle.getHeight(), riddle.getWidth());
               stack.setRow(row);
               stack.setColumn(column);
               stack.setIndexOfColor(colorIndex);
               stacks.add(stack);
               if (colorIndex != -1) {
                  writeCharInMatrix(row, riddle.getColours().get(stack.getIndexOfColor()).getName(), column);
               } else {
                  writeCharInMatrix(row, '-', column);
               }
               return;
            }
         }
      }

   }

   /**
    * Überprüft, ob ein "*" nur zu einem Block gehört. Falls dies der Fall ist,
    * wird überprüft, ob row.getMaxEntries() - row.getEntriesSet() - starCount)
    * == 0 ist. Dies bedeutet, dass nur noch ein Farbfeld zu setzen ist und es
    * mit der Farbe des Blocks gesetzt werden kann.
    * 
    * @param row
    * @throws Exception
    */
   private void checkStarBelongingToBlock(Row row) throws Exception {
      ArrayList<Block> blocks = row.getBlocks();
      if (blocks == null || blocks.size() == 0) {
         return;
      }
      int indexOfRow = row.getIndex();
      int rowInt = indexOfRow;
      // // System.out.println();
      for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
         // showMatrix();// System.out.println();
         if (matrix[rowInt][columnInt] == '*') {
            LinkedList<Integer> blockInts = new LinkedList<Integer>();
            for (int index = 0; index < blocks.size(); index++) {
               Block block = blocks.get(index);
               if (columnInt >= block.getMinStartIndexNew() && columnInt <= block.getMaxEndIndexNew()) {
                  blockInts.add(blocks.indexOf(block));
               }
            }
            if (blockInts.size() == 1) {
               int starCount = 0;
               for (int i = 0; i < riddle.getWidth(); i++) {
                  if (matrix[indexOfRow][i] == '*') {
                     starCount++;
                  }
               }
               if ((row.getMaxEntries() - row.getEntriesSet() - starCount) == 0) {
                  // // System.out.println("QQQQQrow:" + rowInt +
                  // "columnInt: "
                  // + columnInt);
                  fillAreaInRowWithChar(rowInt, columnInt, columnInt + 1, blocks.get(blockInts.get(0)).getColorChar());
               }
            } else if (blockInts.size() == 0) {
               // // System.out.println("EMPTYEMPTYEMPTY");
               fillAreaInRowWithChar(rowInt, columnInt, columnInt + 1, '-');
            } else if (blockInts.size() > 1) {
               // // System.out.println("> 1 Row:" + rowInt + " col:" +
               // columnInt);

               // Wenn es keinen Block in der Spalte gibt mit derselben
               // Farbe und
               // rowInt >= block2.getMinStartIndexNew() && rowInt <=
               // block2.getMaxEndIndexNew() dann mit leer füllen
               boolean bo = true;
               for (Integer i : blockInts) {
                  Block block = blocks.get(i);
                  ArrayList<Block> blocks2 = getColumns().get(columnInt).getBlocks();
                  int size = blocks2.size();
                  for (int index = 0; index < size; index++) {
                     Block block2 = blocks2.get(index);
                     if (((block.getColorChar() == block2.getColorChar()) && rowInt >= block2.getMinStartIndexNew() && rowInt <= block2.getMaxEndIndexNew())) {
                        bo = false;
                     }
                  }
               }
               if (bo) {
                  fillAreaInRowWithChar(rowInt, columnInt, columnInt + 1, '-');
               }
            }
            // // System.out.println("JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ");
         }
      }
   }

   /**
    * Prüft, ob eine gesetzte Farbe zu einem Block gehört. Wenn ja wird der
    * Index in indeces von Block geschrieben und die maximale und minimale
    * Ausdehnung geändert.
    * 
    * @param row
    * @throws Exception
    */
   private void checkColorBelongingToBlock(Row row) throws Exception {
      ArrayList<Block> blocks = row.getBlocks();
      if (blocks == null || blocks.size() == 0) {
         return;
      }
      int rowInt = row.getIndex();
      for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
         char c = matrix[rowInt][columnInt];
         // color
         if (c != '*' && c != '-') {
            LinkedList<Integer> blockInts = new LinkedList<Integer>();
            for (int index = 0; index < blocks.size(); index++) {
               Block block = blocks.get(index);
               if (columnInt >= block.getMinStartIndexNew() && columnInt <= block.getMaxEndIndexNew() && c == block.getColorChar()) {
                  blockInts.add(blocks.indexOf(block));
               }
            }
            // "*" gehört nur zu einem Block
            if (blockInts.size() == 1) {
               Block block = blocks.get(blockInts.get(0));
               int min = columnInt - block.getHowMany() + 1;
               if (min < 0) {
                  min = 0;
               }
               int max = columnInt + block.getHowMany() - 1;
               if (max >= riddle.getWidth()) {
                  max = riddle.getWidth() - 1;
               }
               block.setMaxEndIndexNew(max);
               block.setMinStartIndexNew(min);
            } else if (blockInts.size() == 0) {
               throw new Exception("Farbe gehört zu keinem Block:" + row.getIndex() + "/" + columnInt + ":" + c);
            }
         }
      }
   }

   /**
    * Prüft, ob eine gesetzte Farbe zu einem Block gehört. Wenn ja wird der
    * Index in indeces von Block geschrieben und die maximale und minimale
    * Ausdehnung geändert.
    * 
    * @param column
    * @throws Exception
    */
   private void checkColorBelongingToBlock(Column column) throws Exception {
      ArrayList<Block> blocks = column.getBlocks();
      if (blocks == null || blocks.size() == 0) {
         return;
      }
      int columnInt = column.getIndex();
      for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
         char c = matrix[rowInt][columnInt];
         // color
         if (c != '*' && c != '-') {
            LinkedList<Integer> blockInts = new LinkedList<Integer>();
            for (int index = 0; index < blocks.size(); index++) {
               Block block = blocks.get(index);
               if (rowInt >= block.getMinStartIndexNew() && rowInt <= block.getMaxEndIndexNew() && c == block.getColorChar()) {
                  blockInts.add(blocks.indexOf(block));

               }
               // TODO check what to do
               // else
               // if (rowInt >= block.getMinStartIndexNew()
               // && rowInt <= block.getMaxEndIndexNew()) {
               // // System.out.println("row:" + rowInt + " column:" +
               // columnInt +
               // " other color:" + c + " block:" + block);
               //
               // }
            }
            if (blockInts.size() == 1) {
               Block block = blocks.get(blockInts.get(0));
               int min = rowInt - block.getHowMany() + 1;
               if (min < 0) {
                  min = 0;
               }
               int max = rowInt + block.getHowMany() - 1;
               if (max >= riddle.getHeight()) {
                  max = riddle.getHeight() - 1;
               }
               block.setMaxEndIndexNew(max);
               block.setMinStartIndexNew(min);
            } else if (blockInts.size() == 0) {
               throw new NotSolvableException("Farbe gehört zu keinem Block:" + rowInt + "/" + column.getIndex() + ":" + c);
            }
         }
      }
   }

   /**
    * Überprüft, ob ein "*" nur zu einem Block gehört. Falls dies der Fall ist,
    * wird überprüft, ob row.getMaxEntries() - row.getEntriesSet() - starCount)
    * == 0 ist. Dies bedeutet, dass nur noch ein Farbfeld zu setzen ist und es
    * mit der Farbe des Blocks gesetzt werden kann.
    * 
    * @param column
    * @throws Exception
    */
   private void checkStarBelongingToBlock(Column column) throws Exception {
      ArrayList<Block> blocks = column.getBlocks();
      if (blocks == null || blocks.size() == 0) {
         return;
      }
      int columnInt = column.getIndex();
      for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
         if (matrix[rowInt][columnInt] == '*') {
            LinkedList<Integer> blockInts = new LinkedList<Integer>();
            for (int index = 0; index < blocks.size(); index++) {
               Block block = blocks.get(index);
               if (rowInt >= block.getMinStartIndexNew() && rowInt <= block.getMaxEndIndexNew()) {
                  blockInts.add(blocks.indexOf(block));
               }
            }
            if (blockInts.size() == 1) {
               int starCount = 0;
               for (int i = 0; i < riddle.getHeight(); i++) {
                  if (matrix[i][columnInt] == '*') {
                     starCount++;
                  }
               }
               if ((column.getMaxEntries() - column.getEntriesSet() - starCount) == 0) {
                  fillAreaInColumnWithChar(columnInt, rowInt, rowInt + 1, blocks.get(blockInts.get(0)).getColourString().charAt(0));
               }
            } else if (blockInts.size() == 0) {
               fillAreaInColumnWithChar(columnInt, rowInt, rowInt + 1, '-');
            } else if (blockInts.size() > 1) {
               // Wenn es keinen Block in der Reihe gibt mit derselben
               // Farbe und
               // columnInt >= block2.getMinStartIndexNew() && columnInt <=
               // block2.getMaxEndIndexNew() dann mit leer füllen
               boolean bo = true;
               for (Integer i : blockInts) {
                  Block block = blocks.get(i);
                  ArrayList<Block> blocks2 = getRows().get(rowInt).getBlocks();
                  for (int index = 0; index < blocks2.size(); index++) {
                     Block block2 = blocks2.get(index);
                     if (((block.getColorChar() == block2.getColorChar()) && columnInt >= block2.getMinStartIndexNew() && columnInt <= block2.getMaxEndIndexNew())) {
                        bo = false;
                     }
                  }
               }
               if (bo) {
                  fillAreaInColumnWithChar(columnInt, rowInt, rowInt + 1, '-');
               }
            }
         }
      }
   }

   /**
    * Überprüft, ob ein Leerfeld zu einem Block gehört. Wenn der Index gleich
    * dem MinStart oder MaxEnd ist, werden diese in/dekrementiert. Wird in
    * {@link #checkByBlock()} aufgerufen.
    * 
    * @param row
    * @throws Exception
    */
   private void checkEmptyBelongingToBlock(Row row) throws Exception {
      ArrayList<Block> blocks = row.getBlocks();
      if (blocks == null || blocks.size() == 0) {
         return;
      }
      int rowInt = row.getIndex();
      for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
         if (matrix[rowInt][columnInt] == '-') {
            for (int index = 0; index < blocks.size(); index++) {
               Block block = blocks.get(index);
               if (!block.isGone() && columnInt == block.getMinStartIndexNew()) {
                  block.setMinStartIndexNew(block.getMinStartIndexNew() + 1);
               } else if (!block.isGone() && columnInt == block.getMaxEndIndexNew()) {
                  block.setMaxEndIndexNew(block.getMaxEndIndexNew() - 1);
                  // Leeres Feld innerhalb des möglichen Blocks.
               } else if (block.getMinStartIndexNew() < columnInt && block.getMaxEndIndexNew() > columnInt) {
                  checkSizesBeforeAndAfterEmptyInBlockRange(block, columnInt);
               }
            }
         }
      }
   }

   /**
    * TODO why not dom Prüft, ob nach bzw. vor dem leeren Feld noch genug Platz
    * für den Block ist. falls nicht, wird minStartIndexNey bzw maxEndIndexNew
    * angepasst. Wird in {@link #checkEmptyBelongingToBlock(Row)} und
    * {@link #checkEmptyBelongingToBlock(Column)} aufgerufen.
    * 
    * @param block .
    * @param columnInt .
    * @throws Exception
    */
   private void checkSizesBeforeAndAfterEmptyInBlockRange(Block block, int columnInt) throws Exception {
      if ((columnInt - block.getMinStartIndexNew()) < block.getHowMany()) {
         block.setMinStartIndexNew(columnInt + 1);
      } else if (block.getMaxEndIndexNew() - columnInt < block.getHowMany()) {
         block.setMaxEndIndexNew(columnInt - 1);
      }
   }

   /**
    * Überprüft, ob ein Leerfeld zu einem Block gehört. Wenn der Index gleich
    * dem MinStart oder MaxEnd ist, werden diese in/dekrementiert. Wird in
    * {@link #checkByBlock()} aufgerufen.
    * 
    * @param column
    * @throws Exception
    */
   private void checkEmptyBelongingToBlock(Column column) throws Exception {
      ArrayList<Block> blocks = column.getBlocks();
      if (blocks == null || blocks.size() == 0) {
         return;
      }
      int columnInt = column.getIndex();
      for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
         if (matrix[rowInt][columnInt] == '-') {
            for (int index = 0; index < blocks.size(); index++) {
               Block block = blocks.get(index);
               if (!block.isGone() && rowInt == block.getMinStartIndexNew()) {
                  block.setMinStartIndexNew(block.getMinStartIndexNew() + 1);
               } else if (!block.isGone() && rowInt == block.getMaxEndIndexNew()) {
                  block.setMaxEndIndexNew(block.getMaxEndIndexNew() - 1);
               } else if (block.getMinStartIndexNew() < rowInt && block.getMaxEndIndexNew() > rowInt) {
                  checkSizesBeforeAndAfterEmptyInBlockRange(block, rowInt);
               }
            }
         }
      }
   }

   /**
    * Ruft mehrere Methoden auf, die versuchen das Rätsel auf Blockebene zu
    * lösen.
    * 
    * @throws Exception
    */
   private void checkByBlock() throws Exception {
      for (int index = 0; index < getRows().size(); index++) {
         Row row = getRows().get(index);
         updateMinAndMaxIndexOfBlocks(row);
         updateMinAndMaxIndexOfBlocks2(row);
         checkStarBelongingToBlock(row);
         checkEmptyBelongingToBlock(row);
         overlapBlocks(row);
         checkColorBelongingToBlock(row);
         checkIfEntriesNotSetInBlock(row);
         checkEmptyInBetweenBlock(row);
         fillWithEmptyAfterGone(row);
         fillIfMinMaxEqualToHowMany(row);
         fillEntriesFromBlockIntoMatrix(row);

         checkBlocksIfIndexIsOk(row);

      }
      for (int index = 0; index < getColumns().size(); index++) {
         Column column = getColumns().get(index);
         updateMinAndMaxIndexOfBlocks(column);
         updateMinAndMaxIndexOfBlocks2(column);
         checkStarBelongingToBlock(column);
         checkEmptyBelongingToBlock(column);
         overlapBlocks(column);
         checkColorBelongingToBlock(column);
         checkIfEntriesNotSetInBlock(column);
         checkEmptyInBetweenBlock(column);
         fillWithEmptyAfterGone(column);
         fillIfMinMaxEqualToHowMany(column);
         fillEntriesFromBlockIntoMatrix(column);
         checkBlocksIfIndexIsOk(column);
      }
   }

   /**
    * Schreibt die bereits gesetzten Felder des Blocks in die Matrix.
    * 
    * @param row
    * @throws Exception
    */
   private void fillEntriesFromBlockIntoMatrix(Row row) throws Exception {

      ArrayList<Block> blocks = row.getBlocks();
      if (blocks == null || blocks.size() == 0) {
         return;
      }
      for (int index = 0; index < blocks.size(); index++) {
         Block block = blocks.get(index);
         if (!block.isGone()) {
            TreeSet<Integer> indeces = block.getIndeces();
            if (indeces.size() > 1) {
               for (int column : indeces) {
                  writeCharInMatrix(row.getIndex(), block.getColourString().charAt(0), column);
               }
            }
         }
      }

   }

   /**
    * Schreibt die bereits gesetzten Felder des Blocks in die Matrix.
    * 
    * @param column
    * @throws Exception
    */
   private void fillEntriesFromBlockIntoMatrix(Column column) throws Exception {

      ArrayList<Block> blocks = column.getBlocks();
      if (blocks == null || blocks.size() == 0) {
         return;
      }
      for (int index = 0; index < blocks.size(); index++) {
         Block block = blocks.get(index);
         if (!block.isGone()) {
            TreeSet<Integer> indeces = block.getIndeces();
            if (indeces.size() > 1) {
               for (int row : indeces) {
                  writeCharInMatrix(row, block.getColourString().charAt(0), column.getIndex());
               }
            }
         }
      }

   }

   /**
    * Füllt den Bereich zwischen MinStartIndexNew und MaxEndIndexNew des
    * Blockes, falls die Differenz gleich der Größe des Blocks ist
    * 
    * @param row
    * @throws Exception
    */
   private void fillIfMinMaxEqualToHowMany(Row row) throws Exception {
      ArrayList<Block> blocks = row.getBlocks();
      if (blocks == null || blocks.size() < 2) {
         return;
      }
      for (int index = 0; index < blocks.size(); index++) {
         Block block = blocks.get(index);
         if (block.doOverlapping) {
            if (block.getMaxEndIndexNew() + 1 - block.getMinStartIndexNew() == block.getHowMany()) {
               fillAreaInRowWithChar(row.getIndex(), block.getMinStartIndexNew(), block.getMaxEndIndexNew() + 1, block.getColorChar());
               block.setGone(true, block.getMinStartIndexNew());
            }
         }
      }
   }

   /**
    * Füllt den Bereich zwischen MinStartIndexNew und MaxEndIndexNew des
    * Blockes, falls die Differenz gleich der Größe des Blocks ist
    * 
    * @param column
    * @throws Exception
    */
   private void fillIfMinMaxEqualToHowMany(Column column) throws Exception {
      ArrayList<Block> blocks = column.getBlocks();
      if (blocks == null || blocks.size() < 2) {
         return;
      }
      for (int index = 0; index < blocks.size(); index++) {
         Block block = blocks.get(index);
         if (block.doOverlapping) {
            if (block.getMaxEndIndexNew() + 1 - block.getMinStartIndexNew() == block.getHowMany()) {
               fillAreaInColumnWithChar(column.getIndex(), block.getMinStartIndexNew(), block.getMaxEndIndexNew() + 1, block.getColorChar());
               block.setGone(true, block.getMinStartIndexNew());
            }
            block.doOverlapping = false;
         }
      }
   }

   /**
    * Falls die Reihe fertig ist, werden alle "*" mit "-" gefüllt.
    * 
    * @param row
    * @throws Exception
    */
   private void fillWithEmptyAfterGone(Row row) throws Exception {
      ArrayList<Block> blocks = row.getBlocks();
      if (blocks == null || blocks.size() < 2) {
         return;
      }
      int indexOfRow = row.getIndex();
      for (int blockInt = 0; blockInt < blocks.size(); blockInt++) {
         Block block = blocks.get(blockInt);
         if (block.isGone()) {
            if (blockInt == 0) {
               if (block.getColourString().equals(blocks.get(blockInt + 1).getColourString()) && block.getEndIndex() != null && (block.getEndIndex() + 1) < riddle.getWidth()) {
                  writeCharInMatrix(indexOfRow, '-', block.getEndIndex() + 1);
               }
            } else if (blockInt == blocks.size() - 1) {
               if (block.getColourString().equals(blocks.get(blockInt - 1).getColourString()) && block.getStartIndex() != null && (block.getStartIndex() - 1) > -1) {
                  writeCharInMatrix(indexOfRow, '-', block.getStartIndex() - 1);
               }
            } else {
               if (block.getColourString().equals(blocks.get(blockInt + 1).getColourString()) && block.getEndIndex() != null && (block.getEndIndex() + 1) < riddle.getWidth()) {
                  writeCharInMatrix(indexOfRow, '-', block.getEndIndex() + 1);
               }
               if (block.getColourString().equals(blocks.get(blockInt - 1).getColourString()) && block.getStartIndex() != null && (block.getStartIndex() - 1) > -1) {
                  writeCharInMatrix(indexOfRow, '-', block.getStartIndex() - 1);
               }
            }
         }
      }
   }

   /**
    * Falls die Spalte fertig ist, werden alle "*" mit "-" gefüllt.
    * 
    * @param column
    * @throws Exception
    */
   private void fillWithEmptyAfterGone(Column column) throws Exception {
      ArrayList<Block> blocks = column.getBlocks();
      if (blocks == null || blocks.size() < 2) {
         return;
      }
      int indexOfColumn = column.getIndex();
      for (int blockInt = 0; blockInt < blocks.size(); blockInt++) {
         Block block = blocks.get(blockInt);
         if (block.isGone()) {
            if (blockInt == 0) {
               if (block.getColourString().equals(blocks.get(blockInt + 1).getColourString()) && block.getEndIndex() != null && (block.getEndIndex() + 1) < riddle.getHeight()) {
                  writeCharInMatrix(block.getEndIndex() + 1, '-', indexOfColumn);
               }
            } else if (blockInt == blocks.size() - 1) {
               if (block.getColourString().equals(blocks.get(blockInt - 1).getColourString()) && block.getStartIndex() != null && (block.getStartIndex() - 1) > -1) {
                  writeCharInMatrix(block.getStartIndex() - 1, '-', indexOfColumn);
               }
            } else {
               if (block.getColourString().equals(blocks.get(blockInt + 1).getColourString()) && block.getEndIndex() != null && (block.getEndIndex() + 1) < riddle.getHeight()) {
                  writeCharInMatrix(block.getEndIndex() + 1, '-', indexOfColumn);
               }
               if (block.getColourString().equals(blocks.get(blockInt - 1).getColourString()) && block.getStartIndex() != null && (block.getStartIndex() - 1) > -1) {
                  writeCharInMatrix(block.getStartIndex() - 1, '-', indexOfColumn);
               }
            }
         }
      }
   }

   /**
    * 
    * @param row
    * @throws Exception
    */
   private void checkEmptyInBetweenBlock(Row row) throws Exception {

      ArrayList<Block> blocks = row.getBlocks();
      if (blocks == null || blocks.size() == 0) {
         return;
      }
      int indexOfRow = row.getIndex();
      for (int index = 0; index < blocks.size(); index++) {
         Block block = blocks.get(index);
         for (int i = block.getMinStartIndexNew(); i <= block.getMaxEndIndexNew(); i++) {
            if (matrix[indexOfRow][i] == '-') {
               if ((i - block.getMinStartIndexNew()) < block.getHowMany()) {
                  block.setMinStartIndexNew(i + 1);
               }
            }
         }
         for (int i = block.getMaxEndIndexNew(); i >= block.getMinStartIndexNew(); i--) {
            if (matrix[indexOfRow][i] == '-') {
               if ((block.getMaxEndIndexNew() - i) < block.getHowMany()) {
                  block.setMaxEndIndexNew(i - 1);
               }
            }
         }
      }
   }

   private void checkEmptyInBetweenBlock(Column column) throws Exception {

      ArrayList<Block> blocks = column.getBlocks();
      if (blocks == null || blocks.size() == 0) {
         return;
      }
      int indexOfColumn = column.getIndex();
      for (int index = 0; index < blocks.size(); index++) {
         Block block = blocks.get(index);
         for (int i = block.getMinStartIndexNew(); i <= block.getMaxEndIndexNew(); i++) {
            if (matrix[i][indexOfColumn] == '-') {
               if ((i - block.getMinStartIndexNew()) < block.getHowMany()) {
                  block.setMinStartIndexNew(i + 1);
               }
            }
         }
         for (int i = block.getMaxEndIndexNew(); i >= block.getMinStartIndexNew(); i--) {
            if (matrix[i][indexOfColumn] == '-') {
               if ((block.getMaxEndIndexNew() - i) < block.getHowMany()) {
                  block.setMaxEndIndexNew(i - 1);
               }
            }
         }
      }
   }

   /**
    * Testet, ob Felder innerhalb von Blöcken gesetzt werden können. Z.B. wenn
    * in indeces 2 und 5 eingetragen sind, ,müssen 3 und vier auch gesetzt
    * werden.
    * 
    * @param row
    * @throws Exception
    */
   private void checkIfEntriesNotSetInBlock(Row row) throws Exception {

      ArrayList<Block> blocks = row.getBlocks();
      if (blocks == null || blocks.size() == 0) {
         return;
      }
      for (int index = 0; index < blocks.size(); index++) {
         Block block = blocks.get(index);
         if (!block.isGone()) {
            TreeSet<Integer> indeces = block.getIndeces();
            if (indeces.size() > 1) {
               for (int column = indeces.first() + 1; column < indeces.last(); column++) {
                  if (!indeces.contains(column)) {
                     block.increaseEntriesSet(column);
                     writeCharInMatrix(row.getIndex(), block.getColourString().charAt(0), column);
                  }
               }
            }
         }
      }
   }

   /**
    * Testet, ob Felder innerhalb von Blöcken gesetzt werden können. Z.B. wenn
    * in indeces 2 und 5 eingetragen sind, ,müssen 3 und vier auch gesetzt
    * werden.
    * 
    * @param column
    * @throws Exception
    */
   private void checkIfEntriesNotSetInBlock(Column column) throws Exception {

      ArrayList<Block> blocks = column.getBlocks();
      if (blocks == null || blocks.size() == 0) {
         return;
      }
      for (int index = 0; index < blocks.size(); index++) {
         Block block = blocks.get(index);
         if (!block.isGone()) {
            TreeSet<Integer> indeces = block.getIndeces();
            if (indeces.size() > 1) {
               for (int row = indeces.first() + 1; row < indeces.last(); row++) {
                  if (!indeces.contains(row)) {
                     block.increaseEntriesSet(row);
                     writeCharInMatrix(row, block.getColourString().charAt(0), column.getIndex());
                  }
               }
            }
         }
      }
   }

   /**
    * Setzt Min/MaxIndex von Blöcken neu, falls der vorherige oder nachfolgende
    * gone ist. Somit schränkt sich der Bereich, in dem der Block gesetzt werden
    * kann ein. Überprüft auch, ob bei nicht fertigen Blöcken das
    * nächste/vorherige von maxEndIndexNew/minStartIndexNew gleich der
    * Blockfarbe ist und passt dann die Werte an.
    * 
    * @param row
    * @throws Exception
    */
   private void updateMinAndMaxIndexOfBlocks(Row row) throws Exception {
      // // System.out.println("updateMinAndMaxIndexOfBlocks:" +
      // row.getIndex());
      // showBlockGoneTrue();
      ArrayList<Block> blocks = row.getBlocks();
      if (blocks != null && blocks.size() > 1) {
         for (int blockIndex = 0; blockIndex < blocks.size(); blockIndex++) {
            Block block = blocks.get(blockIndex);
            if (block.isGone()) {
               int minIndex = block.getMinStartIndexNew();
               int maxIndex = block.getMaxEndIndexNew();
               // erster Block, also nur nachfolgende updaten!
               if (blockIndex == 0) {
                  updateBlocksAfterThisBlock(blocks, blockIndex, block, maxIndex);
                  // letzer Block, also nur davor updaten
               } else if ((blockIndex + 1) == blocks.size()) {
                  updateBlocksBeforeThisBlock(blocks, blockIndex, block, minIndex);
               } else {
                  updateBlocksAfterThisBlock(blocks, blockIndex, block, maxIndex);
                  updateBlocksBeforeThisBlock(blocks, blockIndex, block, minIndex);
               }
            } else {
               // wenn der vorherige/nachfolgende char von Min/Max gleich
               // block.char dann erhöhen/erniedrigen, da ein Feld leer
               // sein
               // muss.
               int minIndex = block.getMinStartIndexNew();
               int maxIndex = block.getMaxEndIndexNew();
               while ((minIndex - 1 > -1) && matrix[row.getIndex()][minIndex - 1] == block.getColorChar()) {
                  minIndex++;
                  block.setMinStartIndexNew(minIndex);
               }
               while ((maxIndex + 1 < riddle.getWidth()) && matrix[row.getIndex()][maxIndex + 1] == block.getColorChar()) {
                  maxIndex--;
                  block.setMaxEndIndexNew(maxIndex);
               }
            }
         }
      }
      // // System.out.println("bliblarow2:" + row.getIndex());
      // showBlockGoneTrue();
   }

   /**
    * Geht durch die Blöcke der Reihe. Wenn es mehr als einen Block gibt, werden
    * immer zwei aufeinander folgende Blöcke überprüft, ob der maxEndIndexNew -
    * der Größe des 2. Blocks kleiner ist, als der maxEndIndexNew des ersten
    * Blocks. Dann muss der Index des 1. Blocks angepasst werden. Gleiches
    * geschieht für den minStartIndexNew, nur dass hier der 2. Block angepasst
    * wird.
    * 
    * @param row
    * @throws Exception
    */
   private void updateMinAndMaxIndexOfBlocks2(Row row) throws Exception {
      // // System.out.println("updateMinAndMaxIndexOfBlocks2:");
      ArrayList<Block> blocks = row.getBlocks();
      if (blocks != null && blocks.size() > 1) {
         for (int blockIndex = 0; blockIndex < blocks.size(); blockIndex++) {
            Block block = blocks.get(blockIndex);
            // prüfen, ob min/maxIndex gleich einer anderen Farbe ist
            if (matrix[row.getIndex()][block.getMaxEndIndexNew()] != '*' && matrix[row.getIndex()][block.getMaxEndIndexNew()] != block.getColorChar()) {
               block.setMaxEndIndexNew(block.getMaxEndIndexNew() - 1);
            }
            if (matrix[row.getIndex()][block.getMinStartIndexNew()] != '*' && matrix[row.getIndex()][block.getMinStartIndexNew()] != block.getColorChar()) {
               block.setMinStartIndexNew(block.getMinStartIndexNew() + 1);
            }
            // es gibt noch einen nächsteb Block
            if (blockIndex + 1 < blocks.size()) {
               Block nextBlock = blocks.get(blockIndex + 1);
               if (block.getColorChar() != nextBlock.getColorChar()) {
                  if (nextBlock.getMaxEndIndexNew() - nextBlock.getHowMany() < block.getMaxEndIndexNew()) {
                     block.setMaxEndIndexNew(nextBlock.getMaxEndIndexNew() - nextBlock.getHowMany());
                  }
               } else {
                  if (nextBlock.getMaxEndIndexNew() - nextBlock.getHowMany() - 1 < block.getMaxEndIndexNew()) {
                     block.setMaxEndIndexNew(nextBlock.getMaxEndIndexNew() - nextBlock.getHowMany() - 1);
                  }
               }
               if (block.getColorChar() != nextBlock.getColorChar()) {
                  if (block.getMinStartIndexNew() + block.getHowMany() > nextBlock.getMinStartIndexNew()) {
                     nextBlock.setMinStartIndexNew(block.getMinStartIndexNew() + block.getHowMany());
                  }
               } else {
                  if (block.getMinStartIndexNew() + block.getHowMany() + 1 > nextBlock.getMinStartIndexNew()) {
                     nextBlock.setMinStartIndexNew(block.getMinStartIndexNew() + block.getHowMany() + 1);
                  }
               }
               // kein nächster, also nur vorherigen anpassen!
            } else {
               if (blockIndex - 1 > -1) {
                  Block previousBlock = blocks.get(blockIndex - 1);
                  if (block.getColorChar() != previousBlock.getColorChar()) {
                     if (previousBlock.getMinStartIndexNew() + previousBlock.getHowMany() > block.getMinStartIndexNew()) {
                        block.setMinStartIndexNew(previousBlock.getMinStartIndexNew() + previousBlock.getHowMany());
                     }
                  } else {
                     if (previousBlock.getMinStartIndexNew() + previousBlock.getHowMany() + 1 > block.getMinStartIndexNew()) {
                        block.setMinStartIndexNew(previousBlock.getMinStartIndexNew() + previousBlock.getHowMany() + 1);
                     }
                  }
               }
            }
         }
      }
   }

   /**
    * Geht durch die Blöcke der Spalte. Wenn es mehr als einen Block gibt,
    * werden immer zwei aufeinander folgende Blöcke überprüft, ob der
    * maxEndIndexNew - der Größe des 2. Blocks kleiner ist, als der
    * maxEndIndexNew des ersten Blocks. Dann muss der Index des 1. Blocks
    * angepasst werden. Gleiches geschieht für den minStartIndexNew, nur dass
    * hier der 2. Block angepasst wird.
    * 
    * @param column
    * @throws Exception
    */
   private void updateMinAndMaxIndexOfBlocks2(Column column) throws Exception {
      // // System.out.println("updateMinAndMaxIndexOfBlocks2:");
      ArrayList<Block> blocks = column.getBlocks();
      if (blocks != null && blocks.size() > 1) {
         for (int blockIndex = 0; blockIndex < blocks.size(); blockIndex++) {
            Block block = blocks.get(blockIndex);
            // maxIndex hat bereits eine andere Farbe!
            if (matrix[block.getMaxEndIndexNew()][column.getIndex()] != '*' && matrix[block.getMaxEndIndexNew()][column.getIndex()] != block.getColorChar()) {
               block.setMaxEndIndexNew(block.getMaxEndIndexNew() - 1);
            }
            if (matrix[block.getMinStartIndexNew()][column.getIndex()] != '*' && matrix[block.getMinStartIndexNew()][column.getIndex()] != block.getColorChar()) {
               block.setMinStartIndexNew(block.getMinStartIndexNew() + 1);
            }
            // TODO einrechnen, wenn gleiche Farben
            // es gibt noch einen nächsteb Block
            if (blockIndex + 1 < blocks.size()) {
               Block nextBlock = blocks.get(blockIndex + 1);
               if (block.getColorChar() != nextBlock.getColorChar()) {
                  if (nextBlock.getMaxEndIndexNew() - nextBlock.getHowMany() < block.getMaxEndIndexNew()) {
                     block.setMaxEndIndexNew(nextBlock.getMaxEndIndexNew() - nextBlock.getHowMany());
                  }
               } else {
                  if (nextBlock.getMaxEndIndexNew() - nextBlock.getHowMany() - 1 < block.getMaxEndIndexNew()) {
                     block.setMaxEndIndexNew(nextBlock.getMaxEndIndexNew() - nextBlock.getHowMany() - 1);
                  }
               }

               if (block.getColorChar() != nextBlock.getColorChar()) {
                  if (block.getMinStartIndexNew() + block.getHowMany() > nextBlock.getMinStartIndexNew()) {
                     nextBlock.setMinStartIndexNew(block.getMinStartIndexNew() + block.getHowMany());
                  }
               } else {
                  if (block.getMinStartIndexNew() + block.getHowMany() + 1 > nextBlock.getMinStartIndexNew()) {
                     nextBlock.setMinStartIndexNew(block.getMinStartIndexNew() + block.getHowMany() + 1);
                  }
               }
               // kein nächster, also nur vorherigen anpassen!
            } else {
               if (blockIndex - 1 > -1) {
                  Block previousBlock = blocks.get(blockIndex - 1);
                  if (block.getColorChar() != previousBlock.getColorChar()) {
                     if (previousBlock.getMinStartIndexNew() + previousBlock.getHowMany() > block.getMinStartIndexNew()) {
                        block.setMinStartIndexNew(previousBlock.getMinStartIndexNew() + previousBlock.getHowMany());
                     }
                  } else {
                     if (previousBlock.getMinStartIndexNew() + previousBlock.getHowMany() + 1 > block.getMinStartIndexNew()) {
                        block.setMinStartIndexNew(previousBlock.getMinStartIndexNew() + previousBlock.getHowMany() + 1);
                     }
                  }
               }
            }
         }
      }
   }

   /**
    * Setzt MaxEndIndex der vorherigen Blöcke.
    * 
    * 
    * @param blocks
    * @param blockIndex
    * @param block
    * @param minIndex
    * @throws Exception
    */
   private void updateBlocksBeforeThisBlock(ArrayList<Block> blocks, int blockIndex, Block block, int minIndex) throws Exception {
      for (int newIndex = (blockIndex - 1); newIndex > -1; newIndex--) {
         Block checkBlock = blocks.get(newIndex);
         if (!checkBlock.isGone()) {
            if (checkBlock.getMaxEndIndexNew() >= minIndex) {
               if (block.getColourString().equals(checkBlock.getColourString())) {
                  checkBlock.setMaxEndIndexNew(minIndex - 2);
               } else {
                  checkBlock.setMaxEndIndexNew(minIndex - 1);
               }
            }
         }
      }
   }

   /**
    * Setzt MinStartIndexNew der folgenden Blöcke.
    * 
    * @param blocks
    * @param blockIndex
    * @param block
    * @param maxIndex
    * @throws Exception
    */
   private void updateBlocksAfterThisBlock(ArrayList<Block> blocks, int blockIndex, Block block, int maxIndex) throws Exception {
      for (int newIndex = (blockIndex + 1); newIndex < blocks.size(); newIndex++) {
         Block checkBlock = blocks.get(newIndex);
         if (!checkBlock.isGone()) {
            if (checkBlock.getMinStartIndexNew() <= maxIndex) {
               if (block.getColourString().equals(checkBlock.getColourString())) {
                  checkBlock.setMinStartIndexNew(maxIndex + 2);
               } else {
                  checkBlock.setMinStartIndexNew(maxIndex + 1);
               }
            }
         }
      }
   }

   /**
    * Setzt Min/MaxIndex von Blöcken neu, falls der vorherige oder nachfolgende
    * gone ist. Somit schränkt sich der Bereich, in dem der Block gesetzt werden
    * kann ein. Überprüft auch, ob bei nicht fertigen Blöcken das
    * nächste/vorherige von maxEndIndexNew/minStartIndexNew gleich der
    * Blockfarbe ist und passt dann die Werte an.
    * 
    * @param column
    * @throws Exception
    */
   private void updateMinAndMaxIndexOfBlocks(Column column) throws Exception {
      ArrayList<Block> blocks = column.getBlocks();
      if (blocks != null && blocks.size() > 1) {
         for (int blockIndex = 0; blockIndex < blocks.size(); blockIndex++) {
            Block block = blocks.get(blockIndex);
            if (block.isGone()) {
               int minIndex = block.getMinStartIndexNew();
               int maxIndex = block.getMaxEndIndexNew();
               // erster Block, also nur nachfolgende updaten!
               if (blockIndex == 0) {
                  updateBlocksAfterThisBlock(blocks, blockIndex, block, maxIndex);
               } else if ((blockIndex + 1) == blocks.size()) {
                  updateBlocksBeforeThisBlock(blocks, blockIndex, block, minIndex);
               } else {
                  updateBlocksAfterThisBlock(blocks, blockIndex, block, maxIndex);
                  updateBlocksBeforeThisBlock(blocks, blockIndex, block, minIndex);
               }
            } else {
               // wenn der vorherige/nachfolgende char von Min/Max gleich
               // block.char dann erhöhen/erniedrigen, da ein Feld leer
               // sein
               // muss.
               int minIndex = block.getMinStartIndexNew();
               int maxIndex = block.getMaxEndIndexNew();
               while ((minIndex - 1 > -1) && matrix[minIndex - 1][column.getIndex()] == block.getColorChar()) {
                  minIndex++;
                  block.setMinStartIndexNew(minIndex);
               }
               while ((maxIndex + 1 < riddle.getHeight()) && matrix[maxIndex + 1][column.getIndex()] == block.getColorChar()) {
                  maxIndex--;
                  block.setMaxEndIndexNew(maxIndex);
               }
            }
         }
      }
   }

   /**
    * Erstellt alle Möglichkeiten, den Block innerhalb von minStartIndexNew und
    * maxEndIndexNew zu platzieren und setzt die Felder in der Matrix, die bei
    * allen Möglichkeiten gesetzt send.
    * 
    * @param row
    * @throws Exception
    */
   private void overlapBlocks(Row row) throws Exception {
      // // System.out.println("Row:" + row.getIndex());
      // showMatrix();
      // showBlockGoneTrue();
      ArrayList<Block> blocks = row.getBlocks();
      if (blocks != null && blocks.size() > 0) {
         for (int index = 0; index < blocks.size(); index++) {
            Block block = blocks.get(index);
            if (!block.isGone() && block.doOverlapping) {
               int start = block.getMinStartIndexNew();
               LinkedList<String> first = initializeBlockList(block, start);
               LinkedList<String> workingList = new LinkedList<String>();
               workingList.addAll(first);
               LinkedList<Integer> result = new LinkedList<Integer>();
               for (int i = 0; i < first.size(); i++) {
                  if (!first.get(i).equals("-")) {
                     result.add(i);
                  }
               }
               // // System.out.println(result);
               String removed;
               // so lange nach rechts verschieben, bis Ende erreicht und
               // zu
               // asd hinzufügen.
               while (workingList.getLast().equals("-")) {
                  removed = workingList.removeLast();
                  workingList.addFirst(removed);
                  int size = result.size();
                  for (int i = size - 1; i > -1; i--) {
                     Integer index2 = result.get(i);
                     if (!first.get(index2).equals(workingList.get(index2))) {
                        result.remove(result.get(i));
                     }
                  }
               }
               // // System.out.println(result);
               for (Integer column : result) {
                  char charAt = first.get(column).charAt(0);
                  writeCharInMatrix(row.getIndex(), charAt, (column + start));
               }
            }
         }
      }
   }

   /**
    * Erstellt die erste Möglichkeit den Block zu setzten. Wird in
    * overlapBlocksInColumn und overlapInRow aufgerufen.
    * 
    * @param block
    * @param start
    * @return LinkedList<String> mit der Startbesetzung der Blöcke
    */
   private LinkedList<String> initializeBlockList(Block block, int start) {
      int end = block.getMaxEndIndexNew();
      int colorsSet = 0;
      LinkedList<String> first = new LinkedList<String>();
      for (int i = 0; i < block.getHowMany(); i++) {
         first.add(String.valueOf(block.getColorChar()));
         colorsSet++;
      }
      int ij = end - start + 1 - colorsSet;
      while (ij > 0) {
         first.add("-");
         ij--;
      }
      return first;
   }

   /**
    * Erstellt alle Möglichkeiten, den Block innerhalb von minStartIndexNew und
    * maxEndIndexNew zu platzieren und setzt die Felder in der Matrix, die bei
    * allen Möglichkeiten gesetzt send.
    * 
    * @param column
    * @throws Exception
    */
   private void overlapBlocks(Column column) throws Exception {
      ArrayList<Block> blocks = column.getBlocks();
      if (blocks != null && blocks.size() > 0) {
         for (int index = 0; index < blocks.size(); index++) {
            Block block = blocks.get(index);
            if (!block.isGone() && block.doOverlapping) {
               int start = block.getMinStartIndexNew();
               LinkedList<String> first = initializeBlockList(block, start);
               LinkedList<String> workingList = new LinkedList<String>();
               workingList.addAll(first);
               LinkedList<Integer> result = new LinkedList<Integer>();
               for (int i = 0; i < first.size(); i++) {
                  if (!first.get(i).equals("-")) {
                     result.add(i);
                  }
               }
               // String removed;
               // so lange nach rechts verschieben, bis Ende erreicht und
               // zu
               // asd hinzufügen.
               while (workingList.getLast().equals("-")) {
                  moveBlocksInList(workingList, first, result);
               }
               for (Integer row : result) {
                  char charAt = first.get(row).charAt(0);
                  writeCharInMatrix((row + start), charAt, column.getIndex());
               }
            }
         }
      }
   }

   /**
    * Erstellt eine neue Matrix mit der Breite und Höhe des Rätsels und füllt
    * diese mit '*'.
    */
   private void setupMatrix() {
      matrix = new char[riddle.getHeight()][riddle.getWidth()];
      for (int i = 0; i < riddle.getHeight(); i++) {
         for (int j = 0; j < riddle.getWidth(); j++) {
            matrix[i][j] = '*';
         }
      }
   }

   /**
    * Setzt die initialen Werte für minStartIndex und maxStartIndex bei den
    * Blöcken.
    */
   private void setupBlocks() {
      // alle Reihen durchgehen.
      for (int index = 0; index < getRows().size(); index++) {
         Row row = getRows().get(index);
         ArrayList<Block> blocks = row.getBlocks();
         if (blocks != null && blocks.size() > 0) {
            // Blöcke durchgehen.
            setupBlocksInRowAndColumn(blocks, riddle.getWidth());
         }
      }

      for (int index = 0; index < getColumns().size(); index++) {
         Column column = getColumns().get(index);
         ArrayList<Block> blocks = column.getBlocks();
         // Blöcke durchgehen.
         if (blocks != null && blocks.size() > 0) {
            setupBlocksInRowAndColumn(blocks, riddle.getHeight());
         }
      }
   }


   /**
    * Verschiebt das letzte Zeichen (immer ein -) von der letzten an die erste
    * Position. Dann wird jede Position der workingList mit der Position in der
    * firstList verglichen. Falls die Posiitonen nicht gleich sind wird die
    * Position aus result gelöscht.
    * 
    * @param workingList
    * @param firstList
    * @param result
    *           Liste mit Positionen (aus firstList) die gesetzt werden können.
    */
   private void moveBlocksInList(LinkedList<String> workingList, LinkedList<String> firstList, LinkedList<Integer> result) {
      String removed = workingList.removeLast();
      workingList.addFirst(removed);
      int size = result.size();
      // der eigentliche Vergleich
      for (int i = size - 1; i > -1; i--) {
         Integer index = result.get(i);
         if (!firstList.get(index).equals(workingList.get(index))) {
            result.remove(index);
         }
      }
   }

   /**
    * Spalte und Reihe wird mit '-' aufgefüllt, falls gone == true ist.
    */
   private void checkRowsAndColumnsForGone() {
      int size = getRows().size();
      for (int index = 0; index < size; index++) {
         Row row = getRows().get(index);
         if (row.isGone()) {
            fillWithFree(true, index);
         }
      }
      size = getColumns().size();
      for (int index = 0; index < size; index++) {
         Column column = getColumns().get(index);
         if (column.isGone()) {
            fillWithFree(false, index);
         }
      }
   }

   /**
    * Füllt Blöcke am Ende der Spalten.
    * 
    * @throws Exception
    */
   private void fillBlocksOnEnd() throws Exception {
      int size = getColumns().size();
      for (int index = 0; index < size; index++) {
         Column column = getColumns().get(index);
         if (!column.isGone()) {
            fillBlocksOnEnd(column, index, column.getBlocks().size() - 1, riddle.getHeight() - 1);
         }
      }
      for (int index = 0; index < getRows().size(); index++) {
         Row row = getRows().get(index);
         if (!row.isGone()) {
            fillBlocksOnEnd(row, row.getBlocks().size() - 1, riddle.getWidth() - 1);
         }
      }
   }

   /**
    * Wenn eine Spalte mit einer Farbe endet wird der Block gefüllt. Auch
    * angefangene Blöcke danach werden gefüllt.
    * 
    * @throws Exception
    */
   private void fillBlocksOnEnd(Column column, int indexOfColumn, int blockIndex, int rowIndex) throws Exception {
      int block = blockIndex;
      int rowInt = rowIndex;
      boolean run = true;
      // bis char != - weiterlaufen
      while (run) {
         if (rowInt > -1 && matrix[rowInt][indexOfColumn] == '-') {
            rowInt--;
         } else {
            run = false;
         }
      }
      // nur wenn char eine Farbe ist Füllen
      if (rowInt > -1 && matrix[rowInt][indexOfColumn] != '*') {
         ArrayList<Block> blocks = column.getBlocks();
         if (blocks != null) {
            Block colourBlock = blocks.get(block);
            if (matrix[rowInt][indexOfColumn] == colourBlock.getColorChar()) {
               fillAreaInColumnWithChar(indexOfColumn, (rowInt - colourBlock.getHowMany() + 1), (rowInt + 1), colourBlock.getColorChar());
               rowInt = rowInt - colourBlock.getHowMany();
               colourBlock.setGone(true, rowInt + 1);
               // weiter wenn es noch mehr Blöcke gibt
               if (-1 < block - 1) {
                  block--;
                  Block nextBlock = blocks.get(block);
                  // wenn nächster Block gleiche Farbe hat - setzen
                  if (nextBlock != null && nextBlock.getColorChar() == colourBlock.getColorChar() && rowInt > -1) {
                     fillAreaInColumnWithChar(indexOfColumn, rowInt, rowInt + 1, '-');
                     rowInt--;
                  }
                  // nochmal aufrufen
                  fillBlocksOnEnd(column, indexOfColumn, block, rowInt);
               } else {
                  // keine Blöcke mehr, also mit - füllen
                  fillWithFree(false, indexOfColumn);
               }
            } else {
               solveState = SolveStateEnum.ERROR;
               throw new DataCollisionException("Char wurde nochmal in fillBlocksOnEndOfColumn gesetzt \nchar " + matrix[rowInt][indexOfColumn] + " ungleich " + colourBlock.getColorChar());
            }
         }
      }
   }

   /**
    * Füllt Blöcke am Anfang der Spalten.
    * 
    * @throws Exception
    */
   private void fillBlocksOnBeginning() throws Exception {
      int size = getColumns().size();
      for (int index = 0; index < size; index++) {
         Column column = getColumns().get(index);
         if (!column.isGone()) {
            fillBlocksOnBeginning(column, index, 0, 0);
         }
      }
      for (int index = 0; index < getRows().size(); index++) {
         Row row = getRows().get(index);
         if (!row.isGone()) {
            fillBlocksOnBeginning(row, 0, 0);
         }
      }
   }

   /**
    * Füllt Blöcke am Anfang einer Spalte. Ruft sich rekursiv auf.
    * 
    * @param column
    * @param blockIndex
    * @throws Exception
    */
   private void fillBlocksOnBeginning(Column column, int indexOfColumn, int blockIndex, int rowIndex) throws Exception {
      // String methodName = "fillBlocksOnBeginningOfColumn()";
      // // System.out.println(methodName);
      // long startTime = new Date().getTime();
      if (!column.isGone()) {
         int block = blockIndex;
         int rowInt = rowIndex;
         boolean run = true;
         // '-' überspringen
         while (run) {
            if (rowInt < riddle.getHeight() && matrix[rowInt][indexOfColumn] == '-') {
               rowInt++;
            } else {
               run = false;
            }
         }
         // Wenn kein '*' und nicht hinter dem Ende
         if (rowInt < riddle.getHeight() && matrix[rowInt][indexOfColumn] != '*') {
            ArrayList<Block> blocks = column.getBlocks();
            if (blocks != null) {
               Block colourBlock = blocks.get(block);
               // wenn die Farben übereinstimmen Matrix mit Block füllen
               Integer howMany = colourBlock.getHowMany();
               if ((rowInt + howMany) < riddle.getHeight() && matrix[rowInt][indexOfColumn] == colourBlock.getColorChar()) {
                  fillAreaInColumnWithChar(indexOfColumn, rowInt, (rowInt + howMany), colourBlock.getColorChar());
                  colourBlock.setGone(true, rowInt);
                  rowInt = rowInt + howMany;
                  // wenn dahinter noch ein Block Methode wieder aufrufen
                  if (blocks.size() > block + 1) {
                     block++;
                     Block nextBlock = blocks.get(block);
                     if (nextBlock != null && nextBlock.getColorChar() == colourBlock.getColorChar()) {
                        fillAreaInColumnWithChar(indexOfColumn, rowInt, rowInt + 1, '-');
                        rowInt++;
                     }
                     fillBlocksOnBeginning(column, indexOfColumn, block, rowInt);
                     // falls kein Block dahinter kann Spalte mit '-'
                     // gefüllt werden
                  } else {
                     fillWithFree(false, indexOfColumn);
                  }
               } else {
                  if ((rowInt + howMany) < riddle.getHeight()) {
                     solveState = SolveStateEnum.ERROR;
                     throw new DataCollisionException("Char wurde nochmal in fillBlocksOnEndOfColumn gesetzt \nchar " + matrix[rowInt][indexOfColumn] + " at" + rowInt + "/" + indexOfColumn
                           + " ungleich " + colourBlock.getColorChar());
                  }
               }

            }
         }
      }
   }

   /**
    * Wenn eine Spalte mit einer Farbe endet wird der Block gefüllt. Auch
    * angefangene Blöcke danach werden gefüllt.
    * 
    * @throws Exception
    */
   private void fillBlocksOnEnd(Row row, int blockIndex, int columnIndex) throws Exception {
      int block = blockIndex;
      int columnInt = columnIndex;
      boolean run = true;
      int indexOfRow = row.getIndex();
      while (run) {
         if (columnInt > -1 && matrix[indexOfRow][columnInt] == '-') {
            columnInt--;
         } else {
            run = false;
         }
      }
      if (columnInt > -1 && matrix[indexOfRow][columnInt] != '*') {
         ArrayList<Block> blocks = row.getBlocks();
         if (blocks != null) {
            Block colourBlock = blocks.get(block);
            if (matrix[indexOfRow][columnInt] == colourBlock.getColorChar()) {
               fillAreaInRowWithChar(indexOfRow, (columnInt - colourBlock.getHowMany() + 1), (columnInt + 1), colourBlock.getColorChar());
               columnInt = columnInt - colourBlock.getHowMany();
               colourBlock.setGone(true, columnInt + 1);
               if (-1 < block - 1) {
                  block--;
                  Block nextBlock = blocks.get(block);
                  if (nextBlock != null && nextBlock.getColorChar() == colourBlock.getColorChar()) {
                     fillAreaInRowWithChar(indexOfRow, columnInt, columnInt + 1, '-');
                     columnInt--;
                  }
                  fillBlocksOnEnd(row, block, columnInt);
               } else {
                  fillWithFree(true, indexOfRow);
               }
            } else {
               solveState = SolveStateEnum.ERROR;
               throw new DataCollisionException("char " + matrix[indexOfRow][columnInt] + " ungleich " + colourBlock.getColorChar());
            }

         }
      }
   }

   /**
    * Füllt Blöcke am Anfang einer Spalte. Ruft sich rekursiv auf. Wenn eine
    * Spalte mit einer Farbe beginnt, dann können die nächsten Felder auch
    * gesetzt werden (je nach Größe des ersen Blocks). Leere Felder am Anfang
    * der Spalte werden übersprungen. Wenn ein Block aufgefüllt wurde wird die
    * Methode erneut aufgerufen, um zu prüfen ob direkt wieder ein Block
    * beginnt, der aufgefüllt werden kann.
    * 
    * @param row
    * @param blockIndex
    * @throws Exception
    */
   private void fillBlocksOnBeginning(Row row, int blockIndex, int columnIndex) throws Exception {
      if (!row.isGone()) {
         int block = blockIndex;
         int columnInt = columnIndex;
         boolean run = true;
         // '-' überspringen
         int indexOfRow = row.getIndex();
         while (run) {
            if (columnInt < riddle.getWidth() && matrix[indexOfRow][columnInt] == '-') {
               columnInt++;
            } else {
               run = false;
            }
         }
         // Wenn kein '*' und nicht hinter dem Ende
         if (columnInt < riddle.getWidth() && matrix[indexOfRow][columnInt] != '*') {
            ArrayList<Block> blocks = row.getBlocks();
            if (blocks != null) {
               Block colourBlock = blocks.get(block);
               // wenn die Farben übereinstimmen Matrix mit Block füllen
               if ((columnInt + colourBlock.getHowMany()) <= riddle.getWidth() && matrix[indexOfRow][columnInt] == colourBlock.getColorChar()) {
                  fillAreaInRowWithChar(indexOfRow, columnInt, (columnInt + colourBlock.getHowMany()), colourBlock.getColorChar());
                  colourBlock.setGone(true, columnInt);
                  columnInt = columnInt + colourBlock.getHowMany();
                  // wenn dahinter noch ein Block Methode wieder aufrufen
                  if (blocks.size() > block + 1) {
                     block++;
                     Block nextBlock = blocks.get(block);
                     if (nextBlock != null && nextBlock.getColorChar() == colourBlock.getColorChar()) {
                        fillAreaInRowWithChar(indexOfRow, columnInt, columnInt + 1, '-');
                        columnInt++;
                     }
                     fillBlocksOnBeginning(row, block, columnInt);
                     // falls kein Block dahinter kann Spalte mit '-'
                     // gefüllt werden
                  } else {
                     // System.out.println("Hierjfjfj");
                     fillWithFree(true, indexOfRow);
                  }
               } else {
                  if ((columnInt + colourBlock.getHowMany()) < riddle.getWidth()) {
                     solveState = SolveStateEnum.ERROR;
                     throw new DataCollisionException("char " + matrix[columnInt][indexOfRow] + " at" + columnInt + "/" + indexOfRow + " ungleich " + colourBlock.getColorChar());
                  }
               }

            }
         }
      }
   }

   /**
    * Display the matrix.
    * 
    */
   private void showMatrix() {
      StringBuilder out = new StringBuilder("\n");
      ;
      int height = riddle.getHeight();
      for (int i = 0; i < height; i++) {
         showRow(out, i);
      }
      System.out.println(out.toString());
   }

   /**
    * Returns the Row of the current mfatrix as String.
    * 
    * @param out
    *           String in den die Reihe geschriben wird.
    * @param i
    * @return eine Reihe in der Matrix als StringBuilder
    */
   private StringBuilder showRow(StringBuilder out, int i) {
      for (int j = 0; j < riddle.getWidth(); j++) {
         out.append(matrix[i][j]);
         out.append("  ");
      }
      out.append("\n");
      return out;
   }

   // /**
   // * Zum debuggen.
   // */
   // private void showBlockGoneTrue() {
   // String methodName = "showBlockGoneTrue()";
   // System.out.println(methodName);
   // long startTime = new Date().getTime();
   // for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
   // Row row = getRows().get(rowInt);
   // System.out.println("Row:" + rowInt + " -- " + row.isGone());
   // ArrayList<Block> blocks = row.getBlocks();
   // if (null != blocks) {
   // for (Block block : blocks) {
   // System.out.println(block);
   // }
   // }
   // }
   // for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
   // Column column = getColumns().get(columnInt);
   // System.out.println("Column:" + columnInt + " -- " + column.isGone());
   // ArrayList<Block> blocks = column.getBlocks();
   // if (null != blocks) {
   // for (Block block : blocks) {
   // System.out.println(block);
   // }
   // }
   // }
   // System.out.println("Time for " + methodName + ": " + (new Date().getTime()
   // - startTime) + " ms");
   // }

   /**
    * Füllt alle '*' der Reihe oder Spalte mit '-'. Kann aufgerufen werden, wenn eine Reihe
    * gone ist.
    * 
    * @param forRow
    *           Soll eine Reihe gefüllt werden.
    * @param index
    *           Index der Reihe oder Spalte
    */
   private void fillWithFree(boolean forRow, int index) {
      if (forRow) {
         for (int column = 0; column < riddle.getWidth(); column++) {
            if (matrix[index][column] == '*') {
               matrix[index][column] = '-';
            }
         }
      } else {
         for (int row = 0; row < riddle.getHeight(); row++) {
            if (matrix[row][index] == '*') {
               matrix[row][index] = '-';
            }
         }
      }
   }

   /**
    * Setzt die initialen Werte für minStartIndex und maxStartIndex für eine
    * Reihe oder Spalte.
    * 
    * @param blocks
    *           Blöcke der Reihe oder Spalte.
    * @param size
    *           Höhe oder Breite des Rätsels.
    */
   private void setupBlocksInRowAndColumn(ArrayList<Block> blocks, int size) {
      int size2 = blocks.size();
      if (size2 == 0) {

      }
      for (int i = 0; i < size2; i++) {
         Block block = blocks.get(i);
         // wenn es nur ein Block ist, kann er sich über die gesamte Breite/Höhe
         // ziehen
         if (size2 == 1) {
            block.setMinStartIndex(0);
            block.setMaxEndIndex(size - 1);
         } else {
            if (i == 0) {
               block.setMinStartIndex(0);
               block.setMaxEndIndex(getMaxEndIndexOfBlock(blocks, i, size));
            } else if (i == size2 - 1) {
               block.setMinStartIndex(getMinStartIndexOfBlock(blocks, i, size));
               block.setMaxEndIndex(size - 1);
            } else {
               block.setMinStartIndex(getMinStartIndexOfBlock(blocks, i, size));
               block.setMaxEndIndex(getMaxEndIndexOfBlock(blocks, i, size));
            }
         }
      }
   }

   /**
    * Berechnet den minimalen Startindex eines Blockes, indem die Größen der
    * vorherigen Blöcke unter Berücksichtigung etwaiger Zwischenräume addiert
    * werden.
    * 
    * @param blocks
    *           alle Blöcke der Reihe / Spalte
    * @param indexOfBlock
    *           Index des zu betrachteten Blocks
    * @param size
    *           Breite / Höhe des Rätsels
    * @return den neuen minStartIndex
    */
   private int getMinStartIndexOfBlock(ArrayList<Block> blocks, int indexOfBlock, int size) {
      int index = 0;
      Block lastBlock = blocks.get(indexOfBlock);
      for (int i = indexOfBlock - 1; i >= 0; i--) {
         Block block = blocks.get(i);
         index += block.getHowMany();
         if (null != lastBlock && lastBlock.getColorChar() == block.getColour().getName()) {
            index++;
         }
         lastBlock = block;
      }
      return index;
   }

   /**
    * Berechnet den maximalen Index für einen Block.
    * 
    * @param blocks
    *           alle Blöcke dieser Reihe/Spalte
    * @param indexOfBlock
    *           Index des zu betrachteten Blocks.
    * @param size
    *           Breite / Höhe des Rätsels
    * @return den neuen maxEndIndex
    */
   private int getMaxEndIndexOfBlock(ArrayList<Block> blocks, int indexOfBlock, int size) {
      int index = 0;
      Block lastBlock = blocks.get(indexOfBlock);
      for (int i = indexOfBlock + 1; i < blocks.size(); i++) {
         Block block = blocks.get(i);
         index += block.getHowMany();
         if (lastBlock.getColorChar() == block.getColorChar()) {
            index++;
         }
         lastBlock = block;
      }
      return (size - 1 - index);
   }

   /**
    * Gibt alle Spalten des Rätsels zurück.
    * 
    * @return alle Spalten
    */
   private ArrayList<Column> getColumns() {
      return riddle.getColumns();
   }

   /**
    * Gibt alle Reihen des Rätsels zurück.
    * 
    * @return alle Reihen
    */
   private ArrayList<Row> getRows() {
      return riddle.getRows();
   }

   /**
    * Füllt den Bereich in der column zwischen rowBegin (inklusive) und rowEnd
    * (exklusive) mit dem char c.
    * 
    * @param columnIndex
    * @param rowBegin
    * @param rowEnd
    * @param c
    * @throws Exception
    */
   private void fillAreaInColumnWithChar(int columnIndex, int rowBegin, int rowEnd, char c) throws Exception {
      for (int row = rowBegin; row < rowEnd; row++) {
         writeCharInMatrix(row, c, columnIndex);
      }
   }

   /**
    * Füllt den Bereich in der row zwischen columnBegin (inklusive) und
    * columnEnd (exklusive) mit dem char c.
    * 
    * @param rowIndex
    * @param columnBegin
    * @param columnEnd
    * @param c
    * @throws Exception
    */
   private void fillAreaInRowWithChar(int rowIndex, int columnBegin, int columnEnd, char c) throws Exception {
      for (int column = columnBegin; column < columnEnd; column++) {
         writeCharInMatrix(rowIndex, c, column);
      }
   }

   /**
    * Füllt die Stelle in der Matrix.
    * 
    * @param rowIndex
    *           Nummer der Reihe in der Matrix.
    * @param c
    *           Farbe
    * @param columnIndex
    *           rowIndex Nummer der Reihe in der Matrix.
    * @throws Exception
    *            falls an der Stelle bereits ein anderer char als c oder '*'
    *            steht.
    */
   private void writeCharInMatrix(int rowIndex, char c, int columnIndex) throws Exception {
      if (matrix[rowIndex][columnIndex] != '*' && matrix[rowIndex][columnIndex] != c) {
         solveState = SolveStateEnum.MUST_GUESS;
         throw new DataCollisionException("Fehler: row:" + rowIndex + " column:" + columnIndex + " " + c + " ungleich " + matrix[rowIndex][columnIndex]);
      }
      if (matrix[rowIndex][columnIndex] != c) {
         matrix[rowIndex][columnIndex] = c;
         if (matrix[rowIndex][columnIndex] != '-') {
            Row row = getRows().get(rowIndex);
            if (row.setEntriesSet(columnIndex)) {
               fillWithFree(true, rowIndex);
            }
            Column column = getColumns().get(columnIndex);
            if (column.setEntriesSet(rowIndex)) {
               fillWithFree(false, columnIndex);
            }
         }
      }
   }

   /**
    * Gibt die Anzahl der nicht belegten Felder zurück.
    * 
    * @return Anzahl der noch nicht belegten Felder in der MAtrix
    */
   private int getStarCountInRiddle() {
      int starCount = 0;
      for (int index = 0; index < getRows().size(); index++) {
         Row row = getRows().get(index);
         for (int i = 0; i < riddle.getWidth(); i++) {
            int indexOfRow = row.getIndex();
            if (matrix[indexOfRow][i] == '*') {
               starCount++;
            }
         }
      }
      return starCount;
   }

   /**
    * @return the solveState
    */
   public SolveStateEnum getSolveState() {
      return solveState;
   }

   /**
    * @param solveState
    *           the solveState to set
    */
   public void setSolveState(SolveStateEnum solveState) {
      this.solveState = solveState;
   }

}
