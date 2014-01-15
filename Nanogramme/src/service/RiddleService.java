package service;

import interfaces.IUIListener;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Scanner;

import models.Block;
import models.Colour;
import models.Column;
import models.Riddle;
import models.Row;

/**
 * Diese Klasse beinhaltet Methoden, um Raetsel aus Dateien zu laden und zu
 * speichern. Beim Laden wird eine nono-Datei geparsed und die Informationen in
 * einem {@link Riddle} gespeichert. Beim Speichern wird das {@link Riddle} und
 * zusaetzlich der Inhalt der MAtrix in eine Datei zurueckgespeichert.
 * 
 * @author csgt
 * 
 */
public class RiddleService {

   /**
    * Das Raetsel.
    */
   private Riddle riddle;

   /**
    * Hilfsvariable beim Parsen.
    */
   private int parsingState = 0;

   /**
    * Hilfsvariable beim Parsen.
    */
   private int contentRow;
   /**
    * Hilfsvariable beim Parsen.
    */
   private int contentColumn;

   /**
    * Hilfsvariable zum Setzen des Index bei den Reihen.
    */
   int rowInt = 0;

   /**
    * Hilfsvariable zum Setzen des Index bei den Spalten.
    */
   int columnInt = 0;

   /**
    * Spielmatrix.
    */
   private char[][] matrix;

   /**
    * Verbundene UI.
    */
   private IUIListener listener;

   /**
    * Konstruktor.
    * 
    * @param listener
    */
   public RiddleService(IUIListener listener) {
      this.listener = listener;

   }

   /**
    * Laedt eine Datei ein und parst das Raetsel Reihe fuer Reihe in
    * {@link #analyzeLine(String)}.
    * 
    * @param filename
    *           Pfad zum Raetsel.
    * @return Riddle
    */
   protected Riddle readFile(String filename) {
      String nono = "";
      Scanner scanner = null;
      riddle = new Riddle();
      ArrayList<String> lines = new ArrayList<String>();
      try {
         scanner = new Scanner(new File(filename));
         while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
         }
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      } finally {
         if (null != scanner) {
            scanner.close();
         }
      }
      for (String str : lines) {
         if (str.length() > 0) {
            analyzeLine(str);
         }
         nono += (str + "\n");
      }
      riddle.setNono(nono);
      return riddle;
   }

   /**
    * Erstellt das Raetsel aus dem Bild.
    * 
    * @param image
    *           Basisbild
    * @return das Raetsel
    */
   protected Riddle createRiddle(BufferedImage image) {
      Riddle riddle = new Riddle();
      riddle.setHeight(image.getHeight());
      riddle.setWidth(image.getWidth());
      LinkedHashSet<Color> colors = new LinkedHashSet<Color>();
      for (int i = 0; i < image.getHeight(); i++) {
         for (int j = 0; j < image.getWidth(); j++) {
            colors.add(new Color(image.getRGB(j, i)));
         }
      }
      // Mapping erstellen, um den Farben Namen zu vergeben
      HashMap<Integer, String> colourNameMapping = fillNameHashmap();
      LinkedList<Colour> colorList = new LinkedList<Colour>();
      // awt.Color zu models.Colour mappen
      HashMap<Color, Colour> colarMap = createMapping(colors, colourNameMapping, colorList);
      riddle.setColours(colorList);
      // setzen der Hintergrundfarbe
      Colour backgroundCol = listener.getBackgroundColour(colorList);
      ArrayList<Row> rows = new ArrayList<Row>();
      ArrayList<Column> columns = new ArrayList<Column>();
      // setzen der Bloecke in Reihe und Spalte
      setRows(image, backgroundCol, colarMap, rows);
      riddle.setRows(rows);

      setColumns(image, backgroundCol, colarMap, columns);
      riddle.setColumns(columns);

      riddle.getColours().remove(backgroundCol);
      this.riddle = riddle;
      return riddle;
   }

   /**
    * Zu jeder Color wird eine neue Colour erstellt, in die colourList
    * eingetragen und ein MApping zwischen der Color und der Colour erstellt.
    * 
    * @param colors
    *           Farben
    * @param mapping
    *           Mapping zwischen Farbname und Farbnummer
    * @param colourList
    *           Liste der Colours
    * @return Mapping zwischen den Colors aus colors und den daraus neu
    *         erstellten Colours
    */
   public HashMap<Color, Colour> createMapping(LinkedHashSet<Color> colors, HashMap<Integer, String> mapping, LinkedList<Colour> colourList) {
      HashMap<Color, Colour> colarMap = new HashMap<Color, Colour>();
      int ccolorIndex = 0;
      for (Color color : colors) {
         Colour colour = new Colour();
         colour.setRed(color.getRed());
         colour.setGreen(color.getGreen());
         colour.setBlue(color.getBlue());
         colour.setName(mapping.get(ccolorIndex).charAt(0));
         ccolorIndex++;
         colourList.add(colour);
         colarMap.put(color, colour);
      }
      return colarMap;
   }

   /**
    * Erstellt die Reihen fuer das Raetsel.
    * 
    * @param image
    *           Das Bild
    * @param backgroundCol
    *           gewaehlte Hintergrundfarbe
    * @param colorMap
    *           Das Mapping zwischen Color und Colour
    * @param rows
    *           Liste der Reihen
    */
   private void setRows(BufferedImage image, Colour backgroundCol, HashMap<Color, Colour> colorMap, ArrayList<Row> rows) {
      for (int i = 0; i < image.getHeight(); i++) {
         Row row = new Row();
         row.setIndex(i);
         ArrayList<Block> blocks = new ArrayList<Block>();
         Block block = null;
         Integer blockSize = null;
         for (int j = 0; j < image.getWidth(); j++) {
            Color c = new Color(image.getRGB(j, i));
            Colour currentColour = colorMap.get(c);
            // noch kein Block, also neuen erstellen
            if (block == null) {
               if (!currentColour.equals(backgroundCol)) {
                  block = new Block();
                  block.setColour(currentColour);
                  blockSize = 1;
               }
               // bereits ein Block vorhanden
            } else {
               // nicht Hintergrundfarbe
               if (!currentColour.equals(backgroundCol)) {
                  // Farbe ist dieselbe, wie die des derzeitigen Blocks, also
                  // Groesse des Blocks erhoehen
                  if (currentColour.equals(block.getColour())) {
                     blockSize++;
                     // andere Farbe, also neuer Block
                  } else {
                     block.setHowMany(blockSize);
                     blocks.add(block);

                     block = new Block();
                     block.setColour(currentColour);
                     blockSize = 1;
                  }
                  // Hintergrundfarbe, also Block ist beendet
               } else {
                  block.setHowMany(blockSize);
                  blocks.add(block);
                  blockSize = null;
                  block = null;
               }
            }
         }
         if (block != null) {
            block.setHowMany(blockSize);
            blocks.add(block);
            block = null;
         }
         row.setBlocks(blocks);
         rows.add(row);
      }
   }

   /**
    * Erstellt die Spalten fuer das Raetsel.
    * 
    * @param image
    * @param backgroundCol
    * @param colorMap
    * @param columns
    */
   private void setColumns(BufferedImage image, Colour backgroundCol, HashMap<Color, Colour> colorMap, ArrayList<Column> columns) {
      for (int i = 0; i < image.getWidth(); i++) {
         Column column = new Column();
         column.setIndex(i);
         ArrayList<Block> blocks = new ArrayList<Block>();
         Block block = null;
         Integer blockSize = null;
         for (int j = 0; j < image.getHeight(); j++) {
            Color c = new Color(image.getRGB(i, j));
            Colour currentColour = colorMap.get(c);
            if (block == null) {
               if (!currentColour.equals(backgroundCol)) {
                  block = new Block();
                  block.setColour(currentColour);
                  blockSize = 1;
               }
            } else {
               if (!currentColour.equals(backgroundCol)) {
                  if (currentColour.equals(block.getColour())) {
                     blockSize++;
                  } else {
                     block.setHowMany(blockSize);
                     blocks.add(block);

                     block = new Block();
                     block.setColour(currentColour);
                     blockSize = 1;
                  }
               } else {
                  block.setHowMany(blockSize);
                  blocks.add(block);
                  blockSize = null;
                  block = null;
               }
            }
         }
         if (block != null) {
            block.setHowMany(blockSize);
            blocks.add(block);
            block = null;
         }
         column.setBlocks(blocks);
         columns.add(column);
      }

   }

   /**
    * @return Mapping mit allen Buchstaben zu zahlen.
    */
   public HashMap<Integer, String> fillNameHashmap() {
      HashMap<Integer, String> mapping = new HashMap<Integer, String>();
      mapping.put(0, "A");
      mapping.put(1, "B");
      mapping.put(2, "C");
      mapping.put(3, "D");
      mapping.put(4, "E");
      mapping.put(5, "F");
      mapping.put(6, "G");
      mapping.put(7, "H");
      mapping.put(8, "I");
      mapping.put(9, "J");
      mapping.put(10, "K");
      mapping.put(11, "L");
      mapping.put(12, "M");
      mapping.put(13, "N");
      mapping.put(14, "O");
      mapping.put(15, "P");
      mapping.put(16, "Q");
      mapping.put(17, "R");
      mapping.put(18, "S");
      mapping.put(19, "T");
      mapping.put(20, "T");
      mapping.put(21, "U");
      mapping.put(22, "V");
      mapping.put(23, "W");
      mapping.put(24, "X");
      mapping.put(25, "Y");
      mapping.put(26, "Z");
      mapping.put(0, "a");
      mapping.put(1, "b");
      mapping.put(2, "c");
      mapping.put(3, "d");
      mapping.put(4, "e");
      mapping.put(5, "f");
      mapping.put(6, "g");
      mapping.put(7, "h");
      mapping.put(8, "i");
      mapping.put(9, "j");
      mapping.put(10, "k");
      mapping.put(11, "l");
      mapping.put(12, "m");
      mapping.put(13, "n");
      mapping.put(14, "o");
      mapping.put(15, "p");
      mapping.put(16, "q");
      mapping.put(17, "r");
      mapping.put(18, "s");
      mapping.put(19, "t");
      mapping.put(20, "u");
      mapping.put(21, "v");
      mapping.put(22, "w");
      mapping.put(23, "x");
      mapping.put(24, "y");
      mapping.put(25, "z");
      mapping.put(26, "Z");
      return mapping;
   }

   /**
    * Speichert das Raetsel als nono-Datei.
    * 
    * @param matrix
    *           die zu speichernde Matrix
    * @return true wenn das Raetsel gespeichert wurde
    */
   protected boolean save(char[][] matrix) {
      BufferedWriter writer = null;
      boolean saved = false;
      File file = listener.getSaveFile();
      try {
         writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file + ".nono"), "UTF-8"));
         String nono = riddle.getNono();
         if (nono == null) {
            nono = createNono(riddle);
         }
         if (nono.contains("content")) {
            String[] nonoSplit = nono.split("content");
            nono = nonoSplit[0];
         }
         writer.write(nono);
         writer.write(System.getProperty("line.separator") + "content" + System.getProperty("line.separator"));
         for (int row = 0; row < riddle.getHeight(); row++) {
            String rowString = "";
            for (int column = 0; column < riddle.getWidth(); column++) {
               rowString += matrix[row][column];
            }
            writer.write(rowString + System.getProperty("line.separator"));
         }
         saved = true;
      } catch (IOException e) {

      } finally {
         try {
            if (writer != null)
               writer.close();
         } catch (IOException e) {

         }
      }
      return saved;
   }

   /**
    * @return the matrix
    */
   protected char[][] getMatrix() {
      return matrix;
   }

   /**
    * Analysiert eine Reihe in {@link #readFile(String)} und füllt das Riddle
    * Objekt mit Informationen.
    * 
    * @param str
    */
   private void analyzeLine(String str) {
      str = str.trim();
      if (!str.startsWith("#")) {
         switch (parsingState) {
         case 0:
            parseGeneralInformation(str);
            break;
         case 1:
            parseColours(str);
            break;
         case 2:
            parseRows(str);
            break;
         case 3:
            setupMatrix();
            parseColumns(str);
            break;
         case 4:
            parseContent(str);
            break;
         default:
            break;
         }
      }
   }

   /**
    * Parsed eine Zeile des Contents
    * 
    * @param str
    */
   public void parseContent(String str) {
      for (int i = 0; i < str.length(); i++) {
         if (str.charAt(i) != ' ') {
            matrix[contentRow][contentColumn] = str.charAt(i);
            if (str.charAt(i) != '*' && str.charAt(i) != '-') {
               Column column = riddle.getColumns().get(contentColumn);
               Row row = riddle.getRows().get(contentRow);
               try {
                  column.setEntriesSet(contentRow, false);
                  row.setEntriesSet(contentColumn, false);
               } catch (Exception e) {
                  e.printStackTrace();
               }
            }
            contentColumn++;
         }
      }
      if (str.length() > 0 && !str.contains("content")) {
         contentRow++;
      }
      contentColumn = 0;
   }

   /**
    * Parsed eine Zeile als Column
    * 
    * @param str
    */
   public void parseColumns(String str) {
      if (str.startsWith("content")) {
         contentColumn = 0;
         contentRow = 0;
         parsingState = 4;
      } else {
         str = str.trim();
         Column column = new Column();
         String[] blocks = str.split(",");
         for (int i = 0; i < blocks.length; i++) {
            Block cb = new Block();
            String block = blocks[i];
            block = block.trim();
            if (!block.equals("0") && !block.equals("")) {
               cb.setColour(riddle.getColourByName(block.substring(block.length() - 1)));
               cb.setHowMany(Integer.valueOf(block.substring(0, (block.length() - 1))));
               column.addBlock(cb);
            }
         }
         column.setIndex(columnInt);
         columnInt++;
         riddle.addColumn(column);
      }
   }

   /**
    * Parsed eine Zeile als Row
    * 
    * @param str
    */
   public void parseRows(String str) {
      if (str.startsWith("column")) {
         parsingState = 3;
      } else {
         str = str.trim();
         Row row = new Row();
         row.setIndex(rowInt);
         String[] blocks = str.split(",");
         for (int i = 0; i < blocks.length; i++) {
            Block cb = new Block();
            String block = blocks[i];
            block = block.trim();
            if (!block.equals("0") && !block.equals("")) {
               cb.setColour(riddle.getColourByName(block.substring(block.length() - 1)));
               cb.setHowMany(Integer.valueOf(block.substring(0, (block.length() - 1))));
               row.addBlock(cb);
            }
         }
         rowInt++;
         riddle.addRow(row);
      }
   }

   /**
    * Parse eine Zeile als Colour
    * 
    * @param str
    */
   public void parseColours(String str) {
      if (str.startsWith("rows")) {
         parsingState = 2;
      } else {
         String[] splits = str.split(" ");
         Colour colour = new Colour();
         colour.setName(splits[0].charAt(0));
         String rgbsString = splits[1];
         String[] rgbs = rgbsString.split(",");
         colour.setRed(Integer.valueOf(rgbs[0]));
         colour.setGreen(Integer.valueOf(rgbs[1]));
         colour.setBlue(Integer.valueOf(rgbs[2]));
         riddle.addColour(colour);
      }
   }

   /**
    * Parsed eine Zeile mit allgemeinen Informationen
    * 
    * @param str
    */
   public void parseGeneralInformation(String str) {
      if (str.startsWith("width")) {
         String splitted = str.split("width ")[1];
         riddle.setWidth(Integer.valueOf(splitted.trim()));
      } else if (str.startsWith("height")) {
         riddle.setHeight(Integer.valueOf(str.split("height ")[1].trim()));
      } else if (str.startsWith("color")) {
         parsingState = 1;
      }
   }

   /**
    * Erstellt eine neue Matrix mit der Breite und Hoehe des Raetsels und fuellt
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
    * Erstellt eine nono-Datei aus dem Raetsel.
    * 
    * @param riddle2
    * @return neue nono.
    */
   private String createNono(Riddle riddle2) {
      String nono = "";
      StringBuilder sb = new StringBuilder();
      sb.append(String.format("width %1d", riddle.getWidth()));
      sb.append(System.getProperty("line.separator"));
      sb.append(String.format("height %1d", riddle.getHeight()));
      sb.append(System.getProperty("line.separator"));
      sb.append("colors");
      sb.append(System.getProperty("line.separator"));
      for (Colour colour : riddle.getColours()) {
         sb.append(String.format("%1s %d,%d,%d", String.valueOf(colour.getName()), colour.getRed(), colour.getGreen(), colour.getBlue()));
         sb.append(System.getProperty("line.separator"));
      }
      sb.append("rows");
      sb.append(System.getProperty("line.separator"));
      for (Row row : riddle.getRows()) {
         if (row.getBlocks() != null && row.getBlocks().size() > 0) {
            for (int i = 0; i < row.getBlocks().size(); i++) {
               Block block = row.getBlocks().get(i);
               if (i > 0) {
                  sb.append(",");
               }
               sb.append(String.format("%d%s", block.getHowMany(), block.getColourString()));
            }
            sb.append(System.getProperty("line.separator"));
         } else {
            sb.append("0");
            sb.append(System.getProperty("line.separator"));
         }
      }
      sb.append("columns");
      sb.append(System.getProperty("line.separator"));
      for (Column column : riddle.getColumns()) {
         ArrayList<Block> blocks = column.getBlocks();
         if (blocks != null && blocks.size() > 0) {
            for (int i = 0; i < blocks.size(); i++) {
               Block block = blocks.get(i);
               if (i > 0) {
                  sb.append(",");
               }
               sb.append(String.format("%d%s", block.getHowMany(), block.getColourString()));
            }
            sb.append(System.getProperty("line.separator"));
         } else {
            sb.append("0");
            sb.append(System.getProperty("line.separator"));
         }
      }
      nono = sb.toString();
      riddle.setNono(nono);
      return nono;
   }

}
