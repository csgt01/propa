package service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import models.Colour;
import models.Block;
import models.Column;
import models.Riddle;
import models.Row;

public class RiddleLoader {

   private Riddle riddle = new Riddle();

   private int state = 0;

   /**
    * Lädt eine Datei ein und parst das Rätsel Reihe für Reihe in
    * {@link #analyzeLine(String)}. 
    * 
    * @param filename
    * @return
    */
   public Riddle readFile(String filename) {
      Scanner scanner = null;
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
         // System.out.println(index);
         if (str.length() > 0) {
            analyzeLine(str);
         }
      }
      System.out.println("Riddle:" + riddle);
      return riddle;
   }

   /**
    * Analysiert eine Reihe in {@link #readFile(String)}.
    * TODO: Content.
    * @param str
    */
   public void analyzeLine(String str) {
      // TODO: regex wgitespace and tab
      switch (state) {
      case 0:
         if (str.startsWith("width")) {
            String splitted = str.split("width ")[1];
            riddle.setWidth(Integer.valueOf(splitted));
         } else if (str.startsWith("height")) {
            riddle.setHeight(Integer.valueOf(str.split("height ")[1]));
         } else if (str.startsWith("color")) {
            state = 1;
         }
         break;
      case 1:
         if (str.startsWith("rows")) {
            state = 2;
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
         break;
      case 2:
         if (str.startsWith("column")) {
            state = 3;
         } else {
            str = str.trim();
            Row row = new Row(riddle.getWidth());
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
            riddle.addRow(row);
         }
         break;
      case 3:
         if (str.startsWith("bliblablub")) {
            state = 4;
         } else {
            str = str.trim();
            Column column = new Column(riddle.getHeight());
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
            riddle.addColumn(column);
         }
         break;
      default:
         break;
      }
   }

}
