package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;

import models.Block;
import models.Colour;
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
      String methodName = "openFile(" + arg0 + ")";
      System.out.println(methodName);
      riddleLoader = new RiddleService(null);
      riddle = riddleLoader.readFile(arg0);
      matrix = riddleLoader.matrix;
   }

   @Override
   public char[][] getSolution() {
      System.out.println(stacks.size());
      String methodName = "getSolution()";
      System.out.println(methodName);
      if (riddle == null) {
         return null;
      }
      solveState = SolveStateEnum.SOLVING;
      stacks = new LinkedList<StackHolder>();
      solutionsFromGuising = new ArrayList<char[][]>();
      showMatrix();
      if (matrix == null) {
         setupMatrix();
      }
      setupBlocks();
      System.out.println(solveState);
      handle();
      showMatrix();
      return matrix;
   }

   /**
    * Steuert den Lösungsprozess. Je nach Status von {@link #solveState} wird
    * {@link #solve()}, {@link #setFirstStarToSomething()} oder
    * {@link #changeLastStacksMember()} aufgerufen oder die Methode abgebrochen.
    * 
    */
   private void handle() {
      while (solveState != SolveStateEnum.SOLVED) {
         // System.out.println("state:" + solveState);
         // ein Fehler wurde in solve erkannt dann entweder Farbe von letzten
         // StackHolder ändern oder keine Lösung
         if (solveState == SolveStateEnum.ERROR) {
            if (stacks != null && stacks.size() > 0) {
               // wenn alle Möglichkeiten im Stack getestet wurden:
               if (!changeLastStacksMember()) {
                  switch (solutionsFromGuising.size()) {
                  // keine Lösung gefunden
                  case 0:
                     solveState = SolveStateEnum.NO_SOLUTION;
                     return;
                     // 1 Lösung gefunden
                  case 1:
                     solveState = SolveStateEnum.SOLVED;
                     matrix = solutionsFromGuising.get(0);
                     return;
                     // mehrere Lösungen gefunden
                  default:
                     solveState = SolveStateEnum.MULTIPLE_SOLUTIONS;
                     return;
                  }
               }
               // Fehler gefunden und stack ist leer oder null
            } else {
               if (solutionsFromGuising == null || solutionsFromGuising.size() != 1) {
                  solveState = SolveStateEnum.NO_SOLUTION;
                  // return matrix;
                  return;
               } else {
                  solveState = SolveStateEnum.SOLVED;
                  matrix = solutionsFromGuising.get(0);
                  // return solutionsFromGuising.get(0);
                  return;
               }
            }
            // es wurden noch nicht alle * besetzt und logischer Ansatz findet
            // nichts mehr --> Raten
         } else if (solveState == SolveStateEnum.MUST_GUESS) {
            try {
               setFirstStarToSomething();
               solveState = SolveStateEnum.SOLVING;
            } catch (Exception e) {
//               e.printStackTrace();
            }
            // Mögliche Lösung gefunden, aber mit gefüllten stack --> andere
            // Möglichkeiten prüfen um mehrere Lösungen auszuschließen
         } else if (solveState == SolveStateEnum.FOUND_SOLUTION_WITH_STACK) {
            if (!changeLastStacksMember()) {
               switch (solutionsFromGuising.size()) {
               case 0:
                  showMatrix();
                  solveState = SolveStateEnum.NO_SOLUTION;
                  return;
               case 1:
                  matrix = solutionsFromGuising.get(0);
                  solveState = SolveStateEnum.SOLVED;
                  return;
               default:
                  solveState = SolveStateEnum.MULTIPLE_SOLUTIONS;
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
         while (run1 && solveState != SolveStateEnum.ERROR) {
            int starCount = getStarCountInRiddle();
            checkByBlock();
            if (starCount <= getStarCountInRiddle()) {
               solveState = SolveStateEnum.MUST_GUESS;
               run1 = false;
            }
            // mögliche Lösung gefunden
            if (getStarCountInRiddle() == 0) {
               // direkte Lösung, da vorher nicht geraten wurde
               if (stacks == null || stacks.size() == 0) {
                  solveState = SolveStateEnum.SOLVED;
                  run1 = false;
                  if (isSolutionOk()) {
                     solutionsFromGuising.add(matrix);
                  } else {
                     solveState = SolveStateEnum.ERROR;
                  }
                  // andere Möglichkeiten beim Raten testen!
               } else {
                  run1 = false;
                  if (isSolutionOk()) {
                     solutionsFromGuising.add(matrix);
                     solveState = SolveStateEnum.FOUND_SOLUTION_WITH_STACK;
                  } else {
                     solveState = SolveStateEnum.ERROR;
                  }
               }
            }
         }
      } catch (Exception e) {
//          e.printStackTrace();
         solveState = SolveStateEnum.ERROR;
      } finally {
         // showMatrix();
         // showBlockGoneTrue();
      }
      return solveState;
   }

   /**
    * Überprüft die Matrix auf Korrektheit.
    * 
    * @return true wenn die Matrix korrekt ist.
    */
   private boolean isSolutionOk() {
      boolean isOk = true;
      int rowIndex = 0;
      int columnIndex = 0;
      while (isOk && rowIndex < riddle.getHeight()) {
         while (isOk && columnIndex < riddle.getWidth()) {
            if (matrix[rowIndex][columnIndex] != '-') {
               Colour colour = riddle.getColourByName(String.valueOf(matrix[rowIndex][columnIndex]));
               isOk = isColorInThisRowAndColumn(rowIndex, columnIndex, riddle.getColours().indexOf(colour));
            }
            columnIndex++;
         }
         columnIndex = 0;
         rowIndex++;
      }
      System.out.println("isSolutionOk:" + isOk);
      return isOk;
   }

   /**
    * Holt den zuletzt hinzugefügten Stackholder und ändert die Farbe, falls die
    * neue Farbe in der Reihe und der Spalte, in der der StackHolder gesetzt
    * wurde, vorkommt. Falls dies nicht der Fall ist, wird die nächste Farbe
    * genommen.Das Riddle und die Matrix werden auf den Stand, der im
    * Stackholder gespeichert ist zurückgesetzt. Falls alle Farben getestet
    * wurden, wird der Stackholder entfernt und diese Methode nochmal
    * aufgerufen, um den vorherigen Stackholder zu ändern.
    * 
    * @return false, wenn stacks null oder leer ist, also alle Möglichkeiten
    *         getestet wurden.
    */
   private boolean changeLastStacksMember() {
      if (stacks != null && stacks.size() > 0) {
         // letzten StackHolder holen
         StackHolder lastStackHolder = stacks.get(stacks.size() - 1);
         int indexOfColor = lastStackHolder.getIndexOfColor();
         // Farbe ändern
         indexOfColor--;
         // wenn die Farbe an der Stelle in der Matrix niht vorkommen kann,
         // nächste Farbe testen
         while (indexOfColor > -1 && !isColorInThisRowAndColumn(lastStackHolder.getRow(), lastStackHolder.getColumn(), indexOfColor)) {
            indexOfColor--;
         }
         // alle Farben getestet, also diesen StackHolder löschen und nächsten
         // aufrufen
         if (indexOfColor < -1) {
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
            // Matrix und Riddle auf den Stand des StackHolders zurücksetzen
            // neue Farbe in Matrix eintragen
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
                  // eine Farbe setzen
                  writeCharInMatrix(lastStackHolder.getRow(), riddle.getColours().get(indexOfColor).getName(), lastStackHolder.getColumn());
               } else {
                  // ein Leerfeld setzen
                  writeCharInMatrix(lastStackHolder.getRow(), '-', lastStackHolder.getColumn());
               }
            } catch (Exception e) {
               // e.printStackTrace();
            }
         }
         return true;
      } else {
         // es wurden bereits alle Möglicheiten getestet
         return false;
      }
   }

   /**
    * Setzt den ersten "*" in der Matrix auf "-". Des weiteren wird ein
    * StackHolder-Objekt angelegt, um den Status des Lösungsweges zu speichern.
    * Überprüft nun auch, ob es die Farbe, die geraten werden soll, in der Reihe
    * und Spalte gibt.
    * 
    * @throws DataCollisionException
    * 
    */
   private void setFirstStarToSomething() throws DataCollisionException {
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
    * Ruft mehrere Methoden auf, die versuchen das Rätsel auf Blockebene zu
    * lösen. Dabei wird der minIndex und maxIndex der Blöcke möglichst weit
    * eingeschränkt, um bei der Methode {@link #overlapBlocks(row)} viele
    * überlappende Bereiche zu haben. Es wird auch versucht, Felder (leer, mit
    * Farbe gesetzt oder ungesetzt) bestimmten Blöcken zuzuordnet, um dann
    * weiter zu reagieren.
    * 
    * @throws NotSolvableException
    * @throws DataCollisionException
    */
   private void checkByBlock() throws NotSolvableException, DataCollisionException {
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
    * Prüft für jeden Block in dieser Reihe, ob es einen Block in der jeweiligen
    * Spalte gibt, der sich mit diesem überschneidet. Dabei wird jeder Block in
    * jeder Splate zwischen minIndex und maxIndex betrachtet. Wenn es keinen
    * Block gibt, kann unter Vorraussetzungen minIndex oder maxIndex geändert
    * werden.
    * 
    * @param row
    *           Reihe.
    */
   private void checkBlocksIfIndexIsOk(Row row) {
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
            for (int columnInt = block.getMinIndex(); columnInt <= block.getMaxIndex(); columnInt++) {
               Column column = getColumns().get(columnInt);
               int index = 0;
               int size2 = column.getBlocks().size();
               boolean isPresent = false;
               // Wenn es einen Block in der Column gibt, der sich mit dieser
               // Stelle überschneidet abbrechen.
               while (index < size2 && !isPresent) {
                  Block columnBlock = column.getBlocks().get(index);
                  if (block.getColorChar() == columnBlock.getColorChar()) {
                     if (rowInt >= columnBlock.getMinIndex() && rowInt <= columnBlock.getMaxIndex()) {
                        isPresent = true;
                     }
                  }
                  index++;
               }
               // wenn es keine Überschneidungen gibt prüfen, ob sich Bereiche
               // am Anfang oder am Ende ergeben, die kleiner sind als howMany
               if (!isPresent) {
                  if ((columnInt - block.getMinIndex() + 1) < block.getHowMany()) {
                     block.setMinIndex((columnInt + 1));
                  }
                  if ((block.getMaxIndex() - columnInt) < block.getHowMany()) {
                     block.setMaxIndex((columnInt - 1));
                  }
               }
            }
         }
      }
   }

   /**
    * Prüft für jeden Block in dieser Spalte, ob es einen Block in der
    * jeweiligen Reihe gibt, der sich mit diesem überschneidet. Dabei wird jeder
    * Block in jeder Splate zwischen minIndex und maxIndex betrachtet. Wenn es
    * keinen Block gibt, kann unter Vorraussetzungen minIndex oder maxIndex
    * geändert werden.
    * 
    * @param column
    *           Spalte
    * @throws DataCollisionException
    * 
    */
   private void checkBlocksIfIndexIsOk(Column column) {
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
            for (int rowInt = block.getMinIndex(); rowInt <= block.getMaxIndex(); rowInt++) {
               Row row = getRows().get(rowInt);
               int index = 0;
               int size2 = row.getBlocks().size();
               boolean isPresent = false;
               // gibt es einen passenden Block
               while (index < size2 && !isPresent) {
                  Block rowBlock = row.getBlocks().get(index);
                  if (block.getColorChar() == rowBlock.getColorChar()) {
                     if (columnInt >= rowBlock.getMinIndex() && columnInt <= rowBlock.getMaxIndex()) {
                        isPresent = true;
                     }
                  }
                  index++;
               }
               // wenn es keine Überschneidungen gibt prüfen, ob sich Bereiche
               // am Anfang oder am Ende ergeben, die kleiner sind als howMany
               if (!isPresent) {
                  if ((rowInt - block.getMinIndex() + 1) < block.getHowMany()) {
                     block.setMinIndex((rowInt + 1));
                  }
                  if ((block.getMaxIndex() - rowInt) < block.getHowMany()) {
                     block.setMaxIndex((rowInt - 1));
                  }
               }
            }
         }
      }
   }

   /**
    * Überprüft, ob es in der Reihe und der Spalte einen Block mit der Farbe
    * gibt.
    * 
    * @param rowInt
    *           Reihenindex
    * @param columnInt
    *           Spaltenindex
    * @param indexOfColor
    *           Farbindex aus riddle.getColours()
    * @return true, falls in Reihe und Spalte die Farbe in einem Block vorkommt.
    */
   private boolean isColorInThisRowAndColumn(int rowInt, int columnInt, int indexOfColor) {
      Row row = getRows().get(rowInt);
      boolean isPresent = false;
      int index = 0;
      int size = row.getBlocks().size();
      // alle Blöcke der Reihe durchgehen, bis ein Block gefunden ist, der die
      // richtige Farbe hat und columnInt zwischen dessen minIndex und maxIndex
      // ist.
      while (index < size && !isPresent) {
         Block block = row.getBlocks().get(index);
         if (block.getColorChar() == riddle.getColours().get(indexOfColor).getName()) {
            if (columnInt >= block.getMinIndex() && columnInt <= block.getMaxIndex()) {
               isPresent = true;
            }
         }
         index++;
      }
      if (!isPresent) {
         return false;
      }
      // alle Blöcke der Spalte durchgehen, bis ein Block gefunden ist, der die
      // richtige Farbe hat und rowInt zwischen dessen minIndex und maxIndex
      // ist.
      Column column = getColumns().get(columnInt);
      index = 0;
      size = column.getBlocks().size();
      isPresent = false;
      while (index < size && !isPresent) {
         Block block = column.getBlocks().get(index);
         if (block.getColorChar() == riddle.getColours().get(indexOfColor).getName()) {
            if (rowInt >= block.getMinIndex() && rowInt <= block.getMaxIndex()) {
               isPresent = true;
            }
         }
         index++;
      }
      return isPresent;
   }

   /**
    * Überprüft, ob ein "*" nur zu einem Block gehört. Falls dies der Fall ist,
    * wird überprüft, ob row.getMaxEntries() - row.getEntriesSet() - starCount
    * == 0 ist. Dies bedeutet, dass nur noch ein Farbfeld zu setzen ist und es
    * mit der Farbe des Blocks gesetzt werden kann.
    * 
    * @param row
    * @throws DataCollisionException
    */
   private void checkStarBelongingToBlock(Row row) throws DataCollisionException {
      ArrayList<Block> blocks = row.getBlocks();
      if (blocks == null || blocks.size() == 0) {
         return;
      }
      int rowInt = row.getIndex();
      for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
         if (matrix[rowInt][columnInt] == '*') {
            LinkedList<Integer> blockIndeces = new LinkedList<Integer>();
            for (int index = 0; index < blocks.size(); index++) {
               Block block = blocks.get(index);
               if (columnInt >= block.getMinIndex() && columnInt <= block.getMaxIndex()) {
                  blockIndeces.add(blocks.indexOf(block));
               }
            }
            int blockIndecesSize = blockIndeces.size();
            if (blockIndecesSize == 1) {
               int starCount = 0;
               for (int i = 0; i < riddle.getWidth(); i++) {
                  if (matrix[rowInt][i] == '*') {
                     starCount++;
                  }
               }
               if ((row.getMaxEntries() - row.getEntriesSet() - starCount) == 0) {
                  fillAreaInRowWithChar(rowInt, columnInt, columnInt + 1, blocks.get(blockIndeces.get(0)).getColorChar());
               }
            } else if (blockIndecesSize == 0) {
               // // System.out.println("EMPTYEMPTYEMPTY");
               fillAreaInRowWithChar(rowInt, columnInt, columnInt + 1, '-');
            } else if (blockIndecesSize > 1) {
               // Wenn es keinen Block in der Spalte gibt mit derselben
               // Farbe und
               // rowInt >= block2.getMinStartIndexNew() && rowInt <=
               // block2.getMaxEndIndexNew() dann mit leer füllen
               boolean fillIt = true;
               for (Integer i : blockIndeces) {
                  Block block = blocks.get(i);
                  ArrayList<Block> blocks2 = getColumns().get(columnInt).getBlocks();
                  int size = blocks2.size();
                  for (int index = 0; index < size; index++) {
                     Block block2 = blocks2.get(index);
                     if (((block.getColorChar() == block2.getColorChar()) && rowInt >= block2.getMinIndex() && rowInt <= block2.getMaxIndex())) {
                        fillIt = false;
                     }
                  }
               }
               if (fillIt) {
                  fillAreaInRowWithChar(rowInt, columnInt, columnInt + 1, '-');
               }
            }
         }
      }
   }

   /**
    * Prüft, ob eine gesetzte Farbe zu einem Block gehört. Wenn ja wird der
    * Index in indeces von Block geschrieben und die maximale und minimale
    * Ausdehnung geändert.
    * 
    * @param row
    * @throws NotSolvableException
    *            Wenn die gesetzte Farbe zu keinem Block gehört
    */
   private void checkColorBelongingToBlock(Row row) throws NotSolvableException {
      ArrayList<Block> blocks = row.getBlocks();
      if (blocks == null || blocks.size() == 0) {
         return;
      }
      int rowInt = row.getIndex();
      int width = riddle.getWidth();
      for (int columnInt = 0; columnInt < width; columnInt++) {
         char c = matrix[rowInt][columnInt];
         // color
         if (c != '*' && c != '-') {
            LinkedList<Integer> blockIndeces = new LinkedList<Integer>();
            for (int index = 0; index < blocks.size(); index++) {
               Block block = blocks.get(index);
               if (columnInt >= block.getMinIndex() && columnInt <= block.getMaxIndex() && c == block.getColorChar()) {
                  blockIndeces.add(blocks.indexOf(block));
               }
            }
            // gehört nur zu einem Block
            int blockIndecesSize = blockIndeces.size();
            if (blockIndecesSize == 1) {
               Block block = blocks.get(blockIndeces.get(0));
               int min = columnInt - block.getHowMany() + 1;
               if (min < 0) {
                  min = 0;
               }
               int max = columnInt + block.getHowMany() - 1;
               if (max >= width) {
                  max = width - 1;
               }
               block.setMaxIndex(max);
               block.setMinIndex(min);
            } else if (blockIndecesSize == 0) {
               throw new NotSolvableException("Farbe gehört zu keinem Block:" + row.getIndex() + "/" + columnInt + ":" + c);
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
    * @throws NotSolvableException
    *            Wenn die gesetzte Farbe zu keinem Block gehört.
    */
   private void checkColorBelongingToBlock(Column column) throws NotSolvableException {
      ArrayList<Block> blocks = column.getBlocks();
      if (blocks == null || blocks.size() == 0) {
         return;
      }
      int columnInt = column.getIndex();
      for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
         char c = matrix[rowInt][columnInt];
         // color
         if (c != '*' && c != '-') {
            LinkedList<Integer> blockIndeces = new LinkedList<Integer>();
            // alle Blöcke, die an der Stelle gesetzt werden könnten in die
            // Liste schreiben
            for (int index = 0; index < blocks.size(); index++) {
               Block block = blocks.get(index);
               if (rowInt >= block.getMinIndex() && rowInt <= block.getMaxIndex() && c == block.getColorChar()) {
                  blockIndeces.add(blocks.indexOf(block));
               }
            }
            int blockIndecesSize = blockIndeces.size();
            // nur ein Block --> minIndex und maxIndex ändern
            if (blockIndecesSize == 1) {
               Block block = blocks.get(blockIndeces.get(0));
               int min = rowInt - block.getHowMany() + 1;
               if (min < 0) {
                  min = 0;
               }
               int max = rowInt + block.getHowMany() - 1;
               if (max >= riddle.getHeight()) {
                  max = riddle.getHeight() - 1;
               }
               block.setMaxIndex(max);
               block.setMinIndex(min);
            } else if (blockIndecesSize == 0) {
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
    * @throws DataCollisionException
    */
   private void checkStarBelongingToBlock(Column column) throws DataCollisionException {
      ArrayList<Block> blocks = column.getBlocks();
      if (blocks == null || blocks.size() == 0) {
         return;
      }
      int columnInt = column.getIndex();
      // alle Reihen der Spalte durchgehen und auf '*' prüfen
      for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
         if (matrix[rowInt][columnInt] == '*') {
            LinkedList<Integer> blockIndeces = new LinkedList<Integer>();
            // alle Blöcke, die an der Stelle gesetzt werden könnten in die
            // Liste schreiben
            for (int index = 0; index < blocks.size(); index++) {
               Block block = blocks.get(index);
               if (rowInt >= block.getMinIndex() && rowInt <= block.getMaxIndex()) {
                  blockIndeces.add(blocks.indexOf(block));
               }
            }
            int blockIndecesSize = blockIndeces.size();
            // nur ein Block passt
            if (blockIndecesSize == 1) {
               int starCount = 0;
               for (int i = 0; i < riddle.getHeight(); i++) {
                  if (matrix[i][columnInt] == '*') {
                     starCount++;
                  }
               }
               if ((column.getMaxEntries() - column.getEntriesSet() - starCount) == 0) {
                  fillAreaInColumnWithChar(columnInt, rowInt, rowInt + 1, blocks.get(blockIndeces.get(0)).getColorChar());
               }
            } else if (blockIndecesSize == 0) {
               fillAreaInColumnWithChar(columnInt, rowInt, rowInt + 1, '-');
            } else if (blockIndecesSize > 1) {
               // Wenn es keinen Block in der Reihe gibt mit derselben
               // Farbe und
               // columnInt >= block2.getMinStartIndexNew() && columnInt <=
               // block2.getMaxEndIndexNew() dann mit leer füllen
               boolean fillIt = true;
               for (Integer i : blockIndeces) {
                  Block block = blocks.get(i);
                  ArrayList<Block> blocks2 = getRows().get(rowInt).getBlocks();
                  for (int index = 0; index < blocks2.size(); index++) {
                     Block block2 = blocks2.get(index);
                     if (((block.getColorChar() == block2.getColorChar()) && columnInt >= block2.getMinIndex() && columnInt <= block2.getMaxIndex())) {
                        fillIt = false;
                     }
                  }
               }
               if (fillIt) {
                  fillAreaInColumnWithChar(columnInt, rowInt, rowInt + 1, '-');
               }
            }
         }
      }
   }

   /**
    * Überprüft, ob ein Leerfeld zu einem Block gehört. Wenn der Index gleich
    * dem minIndex oder maxIndex ist, werden diese in/dekrementiert. Wird in
    * {@link #checkByBlock()} aufgerufen.
    * 
    * @param row
    */
   private void checkEmptyBelongingToBlock(Row row) {
      ArrayList<Block> blocks = row.getBlocks();
      if (blocks == null || blocks.size() == 0) {
         return;
      }
      int rowInt = row.getIndex();
      for (int columnInt = 0; columnInt < riddle.getWidth(); columnInt++) {
         if (matrix[rowInt][columnInt] == '-') {
            for (int index = 0; index < blocks.size(); index++) {
               Block block = blocks.get(index);
               // Leeres Feld am Anfang des Blocks --> minIndex ändern
               if (!block.isGone() && columnInt == block.getMinIndex()) {
                  block.setMinIndex(block.getMinIndex() + 1);
                  // Leeres Feld am Ende des Blocks --> maxIndex ändern
               } else if (!block.isGone() && columnInt == block.getMaxIndex()) {
                  block.setMaxIndex(block.getMaxIndex() - 1);
                  // Leeres Feld innerhalb des möglichen Blocks.
               } else if (block.getMinIndex() < columnInt && block.getMaxIndex() > columnInt) {
                  checkSizesBeforeAndAfterEmptyInBlockRange(block, columnInt);
               }
            }
         }
      }
   }

   /**
    * Prüft, ob nach bzw. vor dem leeren Feld noch genug Platz für den Block
    * ist. falls nicht, wird minIndex bzw maxIndex angepasst. Wird in
    * {@link #checkEmptyBelongingToBlock(Row)} und
    * {@link #checkEmptyBelongingToBlock(Column)} aufgerufen.
    * 
    * @param block
    *           .
    * @param columnInt
    *           .
    */
   private void checkSizesBeforeAndAfterEmptyInBlockRange(Block block, int columnInt) {
      if ((columnInt - block.getMinIndex()) < block.getHowMany()) {
         block.setMinIndex(columnInt + 1);
      } else if (block.getMaxIndex() - columnInt < block.getHowMany()) {
         block.setMaxIndex(columnInt - 1);
      }
   }

   /**
    * Überprüft, ob ein Leerfeld zu einem Block gehört. Wenn der Index gleich
    * dem minIndex oder maxIndex ist, werden diese in/dekrementiert. Wird in
    * {@link #checkByBlock()} aufgerufen.
    * 
    * @param column
    */
   private void checkEmptyBelongingToBlock(Column column) {
      ArrayList<Block> blocks = column.getBlocks();
      if (blocks == null || blocks.size() == 0) {
         return;
      }
      int columnInt = column.getIndex();
      for (int rowInt = 0; rowInt < riddle.getHeight(); rowInt++) {
         if (matrix[rowInt][columnInt] == '-') {
            for (int index = 0; index < blocks.size(); index++) {
               Block block = blocks.get(index);
               // Leeres Feld am Anfang des Blocks --> minIndex ändern
               if (!block.isGone() && rowInt == block.getMinIndex()) {
                  block.setMinIndex(block.getMinIndex() + 1);
                  // Leeres Feld am Ende des Blocks --> maxIndex ändern
               } else if (!block.isGone() && rowInt == block.getMaxIndex()) {
                  block.setMaxIndex(block.getMaxIndex() - 1);
                  // Leeres Feld innerhalb des möglichen Blocks.
               } else if (block.getMinIndex() < rowInt && block.getMaxIndex() > rowInt) {
                  checkSizesBeforeAndAfterEmptyInBlockRange(block, rowInt);
               }
            }
         }
      }
   }

   /**
    * Schreibt die bereits gesetzten Felder des Blocks, die in
    * {@link Block#indeces} gespeichert sind, in die Matrix.
    * 
    * @param row
    * @throws DataCollisionException
    */
   private void fillEntriesFromBlockIntoMatrix(Row row) throws DataCollisionException {
      ArrayList<Block> blocks = row.getBlocks();
      int size = blocks.size();
      if (blocks == null || size == 0) {
         return;
      }
      for (int index = 0; index < size; index++) {
         Block block = blocks.get(index);
         if (!block.isGone()) {
            TreeSet<Integer> indeces = block.getIndeces();
            if (indeces.size() > 1) {
               for (int column : indeces) {
                  writeCharInMatrix(row.getIndex(), block.getColorChar(), column);
               }
            }
         }
      }

   }

   /**
    * Schreibt die bereits gesetzten Felder des Blocks, die in
    * {@link Block#indeces} in die Matrix.
    * 
    * @param column
    * @throws DataCollisionException
    */
   private void fillEntriesFromBlockIntoMatrix(Column column) throws DataCollisionException {

      ArrayList<Block> blocks = column.getBlocks();
      int size = blocks.size();
      if (blocks == null || size == 0) {
         return;
      }
      for (int index = 0; index < size; index++) {
         Block block = blocks.get(index);
         if (!block.isGone()) {
            TreeSet<Integer> indeces = block.getIndeces();
            if (indeces.size() > 1) {
               for (int row : indeces) {
                  writeCharInMatrix(row, block.getColorChar(), column.getIndex());
               }
            }
         }
      }

   }

   /**
    * Füllt den Bereich zwischen {@link Block#minIndex} und
    * {@link Block#maxIndex} des Blockes, falls die Differenz gleich der Größe
    * des Blocks ({@link Block#howMany}) ist
    * 
    * @param row
    * @throws DataCollisionException
    */
   private void fillIfMinMaxEqualToHowMany(Row row) throws DataCollisionException {
      ArrayList<Block> blocks = row.getBlocks();
      int size = blocks.size();
      if (blocks == null || size < 2) {
         return;
      }
      for (int index = 0; index < size; index++) {
         Block block = blocks.get(index);
         // nur wenn minIndexNew und
         // maxIndexNew sich geändert haben. Dies wird mit doOverlapping
         // geprüft
         if (block.doOverlapping) {
            if (block.getMaxIndex() + 1 - block.getMinIndex() == block.getHowMany()) {
               fillAreaInRowWithChar(row.getIndex(), block.getMinIndex(), block.getMaxIndex() + 1, block.getColorChar());
               block.setGone(true, block.getMinIndex());
            }
         }
      }
   }

   /**
    * Füllt den Bereich zwischen {@link Block#minIndex} und
    * {@link Block#maxIndex} des Blockes, falls die Differenz gleich der Größe
    * des Blocks ({@link Block#howMany}) ist
    * 
    * @param column
    * @throws DataCollisionException
    */
   private void fillIfMinMaxEqualToHowMany(Column column) throws DataCollisionException {
      ArrayList<Block> blocks = column.getBlocks();
      int size = blocks.size();
      if (blocks == null || size < 2) {
         return;
      }
      for (int index = 0; index < size; index++) {
         Block block = blocks.get(index);
         // nur wenn block noch nicht fertig und wenn minIndexNew und
         // maxIndexNew sich geändert haben. Dies wird mit doOverlapping
         // geprüft
         if (block.doOverlapping) {
            if (block.getMaxIndex() + 1 - block.getMinIndex() == block.getHowMany()) {
               fillAreaInColumnWithChar(column.getIndex(), block.getMinIndex(), block.getMaxIndex() + 1, block.getColorChar());
               block.setGone(true, block.getMinIndex());
            }
            // letzte Methode, die in diesem Durchgang von solve() doOverlapping
            // abfragt, also wieder auf false setzen
            block.doOverlapping = false;
         }
      }
   }

   /**
    * Wenn ein Block in dieser Reihe gesetzt ist ({@link Block#gone} == true),
    * wird davor bzw. danach ein Leerfeld gesetzt wenn der vorherige bzw.
    * folgende Block dieselbe Farbe hat.
    * 
    * @param row
    * @throws DataCollisionException
    */
   private void fillWithEmptyAfterGone(Row row) throws DataCollisionException {
      ArrayList<Block> blocks = row.getBlocks();
      int size = blocks.size();
      if (blocks == null || size < 2) {
         return;
      }
      int indexOfRow = row.getIndex();
      for (int blockInt = 0; blockInt < size; blockInt++) {
         Block block = blocks.get(blockInt);
         if (block.isGone()) {
            // erster Block
            if (blockInt == 0) {
               if (block.getColorChar() == blocks.get(blockInt + 1).getColorChar() && block.getEndIndex() != null && (block.getEndIndex() + 1) < riddle.getWidth()) {
                  writeCharInMatrix(indexOfRow, '-', block.getEndIndex() + 1);
               }
               // letzter Block
            } else if (blockInt == size - 1) {
               if (block.getColorChar() == (blocks.get(blockInt - 1).getColorChar()) && block.getStartIndex() != null && (block.getStartIndex() - 1) > -1) {
                  writeCharInMatrix(indexOfRow, '-', block.getStartIndex() - 1);
               }
            } else {
               if (block.getColorChar() == (blocks.get(blockInt + 1).getColorChar()) && block.getEndIndex() != null && (block.getEndIndex() + 1) < riddle.getWidth()) {
                  writeCharInMatrix(indexOfRow, '-', block.getEndIndex() + 1);
               }
               if (block.getColorChar() == (blocks.get(blockInt - 1).getColorChar()) && block.getStartIndex() != null && (block.getStartIndex() - 1) > -1) {
                  writeCharInMatrix(indexOfRow, '-', block.getStartIndex() - 1);
               }
            }
         }
      }
   }

   /**
    * Wenn ein Block in dieser Reihe gesetzt ist ({@link Block#gone} == true),
    * wird davor bzw. danach ein Leerfeld gesetzt wenn der vorherige bzw.
    * folgende Block dieselbe Farbe hat.
    * 
    * @param column
    * @throws DataCollisionException
    */
   private void fillWithEmptyAfterGone(Column column) throws DataCollisionException {
      ArrayList<Block> blocks = column.getBlocks();
      int size = blocks.size();
      if (blocks == null || size < 2) {
         return;
      }
      int indexOfColumn = column.getIndex();
      for (int blockInt = 0; blockInt < size; blockInt++) {
         Block block = blocks.get(blockInt);
         if (block.isGone()) {
            if (blockInt == 0) {
               if (block.getColorChar() == blocks.get(blockInt + 1).getColorChar() && block.getEndIndex() != null && (block.getEndIndex() + 1) < riddle.getHeight()) {
                  writeCharInMatrix(block.getEndIndex() + 1, '-', indexOfColumn);
               }
            } else if (blockInt == size - 1) {
               if (block.getColorChar() == (blocks.get(blockInt - 1).getColorChar()) && block.getStartIndex() != null && (block.getStartIndex() - 1) > -1) {
                  writeCharInMatrix(block.getStartIndex() - 1, '-', indexOfColumn);
               }
            } else {
               if (block.getColorChar() == (blocks.get(blockInt + 1).getColorChar()) && block.getEndIndex() != null && (block.getEndIndex() + 1) < riddle.getHeight()) {
                  writeCharInMatrix(block.getEndIndex() + 1, '-', indexOfColumn);
               }
               if (block.getColorChar() == (blocks.get(blockInt - 1).getColorChar()) && block.getStartIndex() != null && (block.getStartIndex() - 1) > -1) {
                  writeCharInMatrix(block.getStartIndex() - 1, '-', indexOfColumn);
               }
            }
         }
      }
   }

   /**
    * Wenn es ein '-' innerhalb von minIndex und maxIndex gibt, wird überprüft,
    * ob davor oder danach noch genug Platz für den Block ist. Wenn nicht können
    * die Werte neu gesetzt werden
    * 
    * @param row
    */
   private void checkEmptyInBetweenBlock(Row row) {
      ArrayList<Block> blocks = row.getBlocks();
      if (blocks == null || blocks.size() == 0) {
         return;
      }
      int indexOfRow = row.getIndex();
      for (int index = 0; index < blocks.size(); index++) {
         Block block = blocks.get(index);
         for (int i = block.getMinIndex(); i <= block.getMaxIndex(); i++) {
            if (matrix[indexOfRow][i] == '-') {
               if ((i - block.getMinIndex()) < block.getHowMany()) {
                  block.setMinIndex(i + 1);
               }
            }
         }
         for (int i = block.getMaxIndex(); i >= block.getMinIndex(); i--) {
            if (matrix[indexOfRow][i] == '-') {
               if ((block.getMaxIndex() - i) < block.getHowMany()) {
                  block.setMaxIndex(i - 1);
               }
            }
         }
      }
   }

   /**
    * Wenn es ein - innerhalb von {@link Block#minIndex} und
    * {@link Block#maxIndex} gibt, wird überprüft, ob davor oder danach noch
    * genug Platz für den Block ist, wenn nicht können die Werte neu gesetzt
    * werden
    * 
    * @param column
    */
   private void checkEmptyInBetweenBlock(Column column) {

      ArrayList<Block> blocks = column.getBlocks();
      if (blocks == null || blocks.size() == 0) {
         return;
      }
      int indexOfColumn = column.getIndex();
      for (int index = 0; index < blocks.size(); index++) {
         Block block = blocks.get(index);
         for (int i = block.getMinIndex(); i <= block.getMaxIndex(); i++) {
            if (matrix[i][indexOfColumn] == '-') {
               if ((i - block.getMinIndex()) < block.getHowMany()) {
                  block.setMinIndex(i + 1);
               }
            }
         }
         for (int i = block.getMaxIndex(); i >= block.getMinIndex(); i--) {
            if (matrix[i][indexOfColumn] == '-') {
               if ((block.getMaxIndex() - i) < block.getHowMany()) {
                  block.setMaxIndex(i - 1);
               }
            }
         }
      }
   }

   /**
    * Testet, ob Felder innerhalb von Blöcken gesetzt werden können. Z.B. wenn
    * in {@link Block#indeces} 2 und 5 eingetragen sind, müssen 3 und 4 auch
    * gesetzt werden.
    * 
    * @param row
    * @throws DataCollisionException
    */
   private void checkIfEntriesNotSetInBlock(Row row) throws DataCollisionException {

      ArrayList<Block> blocks = row.getBlocks();
      int size = blocks.size();
      if (blocks == null || size == 0) {
         return;
      }
      for (int index = 0; index < size; index++) {
         Block block = blocks.get(index);
         if (!block.isGone()) {
            TreeSet<Integer> indeces = block.getIndeces();
            if (indeces.size() > 1) {
               for (int column = indeces.first() + 1; column < indeces.last(); column++) {
                  if (!indeces.contains(column)) {
                     block.increaseEntriesSet(column);
                     writeCharInMatrix(row.getIndex(), block.getColorChar(), column);
                  }
               }
            }
         }
      }
   }

   /**
    * Testet, ob Felder innerhalb von Blöcken gesetzt werden können. Z.B. wenn
    * in {@link Block#indeces} 2 und 5 eingetragen sind, müssen 3 und 4 auch
    * gesetzt werden.
    * 
    * @param column
    * @throws DataCollisionException
    */
   private void checkIfEntriesNotSetInBlock(Column column) throws DataCollisionException {
      ArrayList<Block> blocks = column.getBlocks();
      int size = blocks.size();
      if (blocks == null || size == 0) {
         return;
      }
      for (int index = 0; index < size; index++) {
         Block block = blocks.get(index);
         if (!block.isGone()) {
            TreeSet<Integer> indeces = block.getIndeces();
            if (indeces.size() > 1) {
               for (int row = indeces.first() + 1; row < indeces.last(); row++) {
                  if (!indeces.contains(row)) {
                     block.increaseEntriesSet(row);
                     writeCharInMatrix(row, block.getColorChar(), column.getIndex());
                  }
               }
            }
         }
      }
   }

   /**
    * Setzt {@link Block#minIndex}/{@link Block#maxIndex} von Blöcken neu, falls
    * der vorherige bzw. nachfolgende gone ist. Somit schränkt sich der Bereich,
    * in dem der Block gesetzt werden kann, ein. Überprüft auch, ob bei nicht
    * fertigen Blöcken das nächste/vorherige von maxIndex/minIndex gleich der
    * Blockfarbe ist und passt dann die Werte an.
    * 
    * @param row
    * @throws DataCollisionException
    */
   private void updateMinAndMaxIndexOfBlocks(Row row) throws DataCollisionException {
      ArrayList<Block> blocks = row.getBlocks();
      int size = blocks.size();
      if (blocks != null && size > 1) {
         for (int blockIndex = 0; blockIndex < size; blockIndex++) {
            Block block = blocks.get(blockIndex);
            if (block.isGone()) {
               int minIndex = block.getMinIndex();
               int maxIndex = block.getMaxIndex();
               // erster Block, also nur nachfolgende updaten!
               if (blockIndex == 0) {
                  updateBlocksAfterThisBlock(blocks, blockIndex, block, maxIndex);
                  // letzer Block, also nur davor updaten
               } else if ((blockIndex + 1) == size) {
                  updateBlocksBeforeThisBlock(blocks, blockIndex, block, minIndex);
               } else {
                  updateBlocksAfterThisBlock(blocks, blockIndex, block, maxIndex);
                  updateBlocksBeforeThisBlock(blocks, blockIndex, block, minIndex);
               }
            } else {
               // wenn der vorherige/nachfolgende char von Min/Max (in der
               // Matrix) gleich
               // block.char dann erhöhen/erniedrigen, da ein Feld leer
               // sein muss.
               int minIndex = block.getMinIndex();
               int maxIndex = block.getMaxIndex();
               while ((minIndex - 1 > -1) && matrix[row.getIndex()][minIndex - 1] == block.getColorChar()) {
                  minIndex++;
                  block.setMinIndex(minIndex);
               }
               while ((maxIndex + 1 < riddle.getWidth()) && matrix[row.getIndex()][maxIndex + 1] == block.getColorChar()) {
                  maxIndex--;
                  block.setMaxIndex(maxIndex);
               }
            }
         }
      }
   }

   /**
    * Geht durch die Blöcke der Reihe. Wenn es mehr als einen Block gibt, werden
    * immer zwei aufeinander folgende Blöcke überprüft, ob der
    * {@link Block#maxIndex} - {@link Block#howMany} des 2. Blocks kleiner ist,
    * als der {@link Block#maxIndex} des ersten Blocks. Dann muss der Index des
    * 1. Blocks angepasst werden. Gleiches geschieht für den
    * {@link Block#minIndex} , nur dass hier der 2. Block angepasst wird.
    * 
    * @param row
    * @throws DataCollisionException
    */
   private void updateMinAndMaxIndexOfBlocks2(Row row) throws DataCollisionException {
      ArrayList<Block> blocks = row.getBlocks();
      int size = blocks.size();
      if (blocks != null && size > 1) {
         for (int blockIndex = 0; blockIndex < size; blockIndex++) {
            Block block = blocks.get(blockIndex);
            // prüfen, ob min/maxIndex gleich einer anderen Farbe ist
            if (matrix[row.getIndex()][block.getMaxIndex()] != '*' && matrix[row.getIndex()][block.getMaxIndex()] != block.getColorChar()) {
               block.setMaxIndex(block.getMaxIndex() - 1);
            }
            if (matrix[row.getIndex()][block.getMinIndex()] != '*' && matrix[row.getIndex()][block.getMinIndex()] != block.getColorChar()) {
               block.setMinIndex(block.getMinIndex() + 1);
            }
            // es gibt noch einen nächsten Block
            if (blockIndex + 1 < size) {
               Block nextBlock = blocks.get(blockIndex + 1);
               // maxIndex des ersten Blocks anpassen
               if (block.getColorChar() != nextBlock.getColorChar()) {
                  if (nextBlock.getMaxIndex() - nextBlock.getHowMany() < block.getMaxIndex()) {
                     block.setMaxIndex(nextBlock.getMaxIndex() - nextBlock.getHowMany());
                  }
               } else {
                  if (nextBlock.getMaxIndex() - nextBlock.getHowMany() - 1 < block.getMaxIndex()) {
                     block.setMaxIndex(nextBlock.getMaxIndex() - nextBlock.getHowMany() - 1);
                  }
               }
               // minIndex des zweiten Blocks anpassen
               if (block.getColorChar() != nextBlock.getColorChar()) {
                  if (block.getMinIndex() + block.getHowMany() > nextBlock.getMinIndex()) {
                     nextBlock.setMinIndex(block.getMinIndex() + block.getHowMany());
                  }
               } else {
                  if (block.getMinIndex() + block.getHowMany() + 1 > nextBlock.getMinIndex()) {
                     nextBlock.setMinIndex(block.getMinIndex() + block.getHowMany() + 1);
                  }
               }
               // kein nächster, also nur vorherigen anpassen!
            } else {
               if (blockIndex - 1 > -1) {
                  Block previousBlock = blocks.get(blockIndex - 1);
                  if (block.getColorChar() != previousBlock.getColorChar()) {
                     if (previousBlock.getMinIndex() + previousBlock.getHowMany() > block.getMinIndex()) {
                        block.setMinIndex(previousBlock.getMinIndex() + previousBlock.getHowMany());
                     }
                  } else {
                     if (previousBlock.getMinIndex() + previousBlock.getHowMany() + 1 > block.getMinIndex()) {
                        block.setMinIndex(previousBlock.getMinIndex() + previousBlock.getHowMany() + 1);
                     }
                  }
               }
            }
         }
      }
   }

   /**
    * Geht durch die Blöcke der Reihe. Wenn es mehr als einen Block gibt, werden
    * immer zwei aufeinander folgende Blöcke überprüft, ob der
    * {@link Block#maxIndex} - der Größe des 2. Blocks kleiner ist, als der
    * {@link Block#maxIndex} des ersten Blocks. Dann muss der Index des 1.
    * Blocks angepasst werden. Gleiches geschieht für den {@link Block#minIndex}
    * , nur dass hier der 2. Block angepasst wird.
    * 
    * @param column
    */
   private void updateMinAndMaxIndexOfBlocks2(Column column) {
      ArrayList<Block> blocks = column.getBlocks();
      int size = blocks.size();
      if (blocks != null && size > 1) {
         for (int blockIndex = 0; blockIndex < size; blockIndex++) {
            Block block = blocks.get(blockIndex);
            // prüfen, ob min/maxIndex gleich einer anderen Farbe ist
            if (matrix[block.getMaxIndex()][column.getIndex()] != '*' && matrix[block.getMaxIndex()][column.getIndex()] != block.getColorChar()) {
               block.setMaxIndex(block.getMaxIndex() - 1);
            }
            if (matrix[block.getMinIndex()][column.getIndex()] != '*' && matrix[block.getMinIndex()][column.getIndex()] != block.getColorChar()) {
               block.setMinIndex(block.getMinIndex() + 1);
            }
            // es gibt noch einen nächsten Block
            if (blockIndex + 1 < size) {
               Block nextBlock = blocks.get(blockIndex + 1);
               // maxIndex des ersten Block anpassen
               if (block.getColorChar() != nextBlock.getColorChar()) {
                  if (nextBlock.getMaxIndex() - nextBlock.getHowMany() < block.getMaxIndex()) {
                     block.setMaxIndex(nextBlock.getMaxIndex() - nextBlock.getHowMany());
                  }
               } else {
                  if (nextBlock.getMaxIndex() - nextBlock.getHowMany() - 1 < block.getMaxIndex()) {
                     block.setMaxIndex(nextBlock.getMaxIndex() - nextBlock.getHowMany() - 1);
                  }
               }
               // minIndex des zweitsn Block anpassen
               if (block.getColorChar() != nextBlock.getColorChar()) {
                  if (block.getMinIndex() + block.getHowMany() > nextBlock.getMinIndex()) {
                     nextBlock.setMinIndex(block.getMinIndex() + block.getHowMany());
                  }
               } else {
                  if (block.getMinIndex() + block.getHowMany() + 1 > nextBlock.getMinIndex()) {
                     nextBlock.setMinIndex(block.getMinIndex() + block.getHowMany() + 1);
                  }
               }
               // kein nächster, also nur vorherigen anpassen!
            } else {
               if (blockIndex - 1 > -1) {
                  Block previousBlock = blocks.get(blockIndex - 1);
                  if (block.getColorChar() != previousBlock.getColorChar()) {
                     if (previousBlock.getMinIndex() + previousBlock.getHowMany() > block.getMinIndex()) {
                        block.setMinIndex(previousBlock.getMinIndex() + previousBlock.getHowMany());
                     }
                  } else {
                     if (previousBlock.getMinIndex() + previousBlock.getHowMany() + 1 > block.getMinIndex()) {
                        block.setMinIndex(previousBlock.getMinIndex() + previousBlock.getHowMany() + 1);
                     }
                  }
               }
            }
         }
      }
   }

   /**
    * Setzt {@link Block#maxIndex} der vorherigen Blöcke neu, falls
    * {@link Block#maxIndex} des vorherigen Blocks >= {@link Block#minIndex} des
    * Blocks ist. Wird aufgerufen, wenn ein Block abgeschlossen wurde.
    * 
    * @param blocks
    *           zu überprüfende Blöcke
    * @param blockIndex
    *           index des Blocks, dessen Vorgänger geprüft werden sollen
    * @param block
    *           Block, dessen Vorgänger geprüft werden sollen
    * @param minIndex
    *           minIndex des Blocks
    */
   private void updateBlocksBeforeThisBlock(ArrayList<Block> blocks, int blockIndex, Block block, int minIndex) {
      for (int newIndex = (blockIndex - 1); newIndex > -1; newIndex--) {
         Block checkBlock = blocks.get(newIndex);
         if (!checkBlock.isGone()) {
            if (checkBlock.getMaxIndex() >= minIndex) {
               if (block.getColorChar() == checkBlock.getColorChar()) {
                  checkBlock.setMaxIndex(minIndex - 2);
               } else {
                  checkBlock.setMaxIndex(minIndex - 1);
               }
            }
         }
      }
   }

   /**
    * Setzt {@link Block#minIndex} der vorherigen Blöcke neu, falls
    * {@link Block#minIndex} des vorherigen Blocks <= {@link Block#maxIndex} des
    * Blocks ist. Wird aufgerufen, wenn ein Block abgeschlossen wurde.
    * 
    * @param blocks
    *           zu überprüfende Blöcke
    * @param blockIndex
    *           index des Blocks, dessen Nachfolger geprüft werden sollen
    * @param block
    *           Block, dessen Nachfolger geprüft werden sollen
    * @param maxIndex
    *           minIndex des Blocks
    */
   private void updateBlocksAfterThisBlock(ArrayList<Block> blocks, int blockIndex, Block block, int maxIndex) {
      for (int newIndex = (blockIndex + 1); newIndex < blocks.size(); newIndex++) {
         Block checkBlock = blocks.get(newIndex);
         if (!checkBlock.isGone()) {
            if (checkBlock.getMinIndex() <= maxIndex) {
               if (block.getColorChar() == checkBlock.getColorChar()) {
                  checkBlock.setMinIndex(maxIndex + 2);
               } else {
                  checkBlock.setMinIndex(maxIndex + 1);
               }
            }
         }
      }
   }

   /**
    * Setzt {@link Block#minIndex}/{@link Block#maxIndex} von Blöcken neu, falls
    * der vorherige oder nachfolgende gone ist. Somit schränkt sich der Bereich,
    * in dem der Block gesetzt werden kann ein. Überprüft auch, ob bei nicht
    * fertigen Blöcken das nächste/vorherige von maxIndex/minIndex gleich der
    * Blockfarbe ist und passt dann die Werte an.
    * 
    * @param column
    */
   private void updateMinAndMaxIndexOfBlocks(Column column) {
      ArrayList<Block> blocks = column.getBlocks();
      if (blocks != null && blocks.size() > 1) {
         for (int blockIndex = 0; blockIndex < blocks.size(); blockIndex++) {
            Block block = blocks.get(blockIndex);
            if (block.isGone()) {
               int minIndex = block.getMinIndex();
               int maxIndex = block.getMaxIndex();
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
               // sein muss.
               int minIndex = block.getMinIndex();
               int maxIndex = block.getMaxIndex();
               while ((minIndex - 1 > -1) && matrix[minIndex - 1][column.getIndex()] == block.getColorChar()) {
                  minIndex++;
                  block.setMinIndex(minIndex);
               }
               while ((maxIndex + 1 < riddle.getHeight()) && matrix[maxIndex + 1][column.getIndex()] == block.getColorChar()) {
                  maxIndex--;
                  block.setMaxIndex(maxIndex);
               }
            }
         }
      }
   }

   /**
    * Erstellt alle Möglichkeiten, den Block innerhalb von
    * {@link Block#minIndex} und {@link Block#maxIndex} zu platzieren und setzt
    * die Felder in der Matrix, die bei allen Möglichkeiten gesetzt sind.
    * 
    * @param row
    * @throws DataCollisionException
    */
   private void overlapBlocks(Row row) throws DataCollisionException {
      ArrayList<Block> blocks = row.getBlocks();
      if (blocks != null && blocks.size() > 0) {
         for (int index = 0; index < blocks.size(); index++) {
            Block block = blocks.get(index);
            // nur wenn block noch nicht fertig und wenn minIndex und
            // maxIndex sich geändert haben. Dies wird mit doOverlapping
            // geprüft
            if (!block.isGone() && block.doOverlapping) {
               int start = block.getMinIndex();
               // Liste mit der Startbelegung
               LinkedList<String> first = initializeBlockList(block);
               LinkedList<String> workingList = new LinkedList<String>();
               workingList.addAll(first);
               // Liste mit indeces für "first"
               LinkedList<Integer> result = new LinkedList<Integer>();
               for (int i = 0; i < first.size(); i++) {
                  if (!first.get(i).equals("-")) {
                     result.add(i);
                  }
               }
               String removed;
               // so lange nach rechts verschieben, bis Ende erreicht und
               // zu workingList hinzufügen.
               while (workingList.getLast().equals("-")) {
                  removed = workingList.removeLast();
                  workingList.addFirst(removed);
                  int size = result.size();
                  // Wenn eine Position von workingList nicht mit first
                  // übereinstimmt den Index aus result entfernen
                  for (int i = size - 1; i > -1; i--) {
                     Integer index2 = result.get(i);
                     if (!first.get(index2).equals(workingList.get(index2))) {
                        result.remove(result.get(i));
                     }
                  }
               }
               // überlappende Bereiche in die MAtrix schreiben
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
    * {@link #overlapBlocksInColumn} und {@link #overlapInRow} aufgerufen.
    * 
    * @param block
    *           zu bearbeitende Block
    * @return LinkedList<String> mit der Startbesetzung der Blöcke
    */
   private LinkedList<String> initializeBlockList(Block block) {
      int end = block.getMaxIndex();
      int start = block.getMinIndex();
      int colorsSet = 0;
      // start liste anlegen und mit so vielen chars füllen
      LinkedList<String> first = new LinkedList<String>();
      for (int i = 0; i < block.getHowMany(); i++) {
         first.add(String.valueOf(block.getColorChar()));
         colorsSet++;
      }
      // Wenn maxIndex - minIndex > howMany dann mit '-' auffüllen
      int fillWithEmpty = end - start + 1 - colorsSet;
      while (fillWithEmpty > 0) {
         first.add("-");
         fillWithEmpty--;
      }
      return first;
   }

   /**
    * Erstellt alle Möglichkeiten, den Block innerhalb von minIndex und maxIndex
    * zu platzieren und setzt die Felder in der Matrix, die bei allen
    * Möglichkeiten gesetzt sind.
    * 
    * @param column
    * @throws DataCollisionException
    */
   private void overlapBlocks(Column column) throws DataCollisionException {
      ArrayList<Block> blocks = column.getBlocks();
      if (blocks != null && blocks.size() > 0) {
         for (int index = 0; index < blocks.size(); index++) {
            Block block = blocks.get(index);
            // nur wenn block noch nicht fertig und wenn minIndexNew und
            // maxIndexNew sich geändert haben. Dies wird mit doOverlapping
            // geprüft
            if (!block.isGone() && block.doOverlapping) {
               int start = block.getMinIndex();
               LinkedList<String> first = initializeBlockList(block);
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
               // zu workingList hinzufügen.
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
    * Setzt die initialen Werte für minIndex und maxIndex bei den Blöcken.
    */
   private void setupBlocks() {
      // alle Reihen durchgehen.
      int size = getRows().size();
      for (int index = 0; index < size; index++) {
         Row row = getRows().get(index);
         ArrayList<Block> blocks = row.getBlocks();
         if (blocks != null && blocks.size() > 0) {
            // Blöcke durchgehen.
            setupBlocksInRowAndColumn(blocks, riddle.getWidth());
         }
      }
      size = getColumns().size();
      for (int index = 0; index < size; index++) {
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
    *           Liste, in der die Einträge geschoben werden
    * @param firstList
    *           Vergleichsliste
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

   // TODO

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
    * Füllt alle '*' der Reihe oder Spalte mit '-'. Kann aufgerufen werden, wenn
    * eine Reihe gone ist.
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
    * Setzt die initialen Werte für minIndex und maxIndex für alle Blöcke in
    * blocks.
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
            block.setMinIndex(0);
            block.setMaxIndex(size - 1);
         } else {
            if (i == 0) {
               block.setMinIndex(0);
               block.setMaxIndex(getMaxEndIndexOfBlock(blocks, i, size));
            } else if (i == size2 - 1) {
               block.setMinIndex(getMinStartIndexOfBlock(blocks, i, size));
               block.setMaxIndex(size - 1);
            } else {
               block.setMinIndex(getMinStartIndexOfBlock(blocks, i, size));
               block.setMaxIndex(getMaxEndIndexOfBlock(blocks, i, size));
            }
         }
      }
   }

   /**
    * Füllt den Bereich in der column zwischen rowBegin (inklusive) und rowEnd
    * (exklusive) mit dem char c.
    * 
    * @param columnIndex
    * @param rowBegin
    * @param rowEnd
    * @param c
    * @throws DataCollisionException
    */
   private void fillAreaInColumnWithChar(int columnIndex, int rowBegin, int rowEnd, char c) throws DataCollisionException {
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
    * @throws DataCollisionException
    */
   private void fillAreaInRowWithChar(int rowIndex, int columnBegin, int columnEnd, char c) throws DataCollisionException {
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
    * @throws DataCollisionException
    *            falls an der Stelle bereits ein anderer char als c oder '*'
    *            steht.
    */
   private void writeCharInMatrix(int rowIndex, char c, int columnIndex) throws DataCollisionException {
      if (matrix[rowIndex][columnIndex] != '*' && matrix[rowIndex][columnIndex] != c) {
         throw new DataCollisionException("Fehler: row:" + rowIndex + " column:" + columnIndex + " " + c + " ungleich " + matrix[rowIndex][columnIndex]);
      }
      if (matrix[rowIndex][columnIndex] != c) {
         matrix[rowIndex][columnIndex] = c;
         if (matrix[rowIndex][columnIndex] != '-') {
            Row row = getRows().get(rowIndex);
            if (row.setEntriesSet(columnIndex, true)) {
               fillWithFree(true, rowIndex);
            }
            Column column = getColumns().get(columnIndex);
            if (column.setEntriesSet(rowIndex, true)) {
               fillWithFree(false, columnIndex);
            }
         }
      }
   }

   /**
    * Gibt die Anzahl der nicht belegten Felder zurück.
    * 
    * @return Anzahl der noch nicht belegten Felder in der Matrix
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
    * Display the matrix. TODO delete
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
    * @return the solveState
    */
   public SolveStateEnum getSolveState() {
      return solveState;
   }

}
