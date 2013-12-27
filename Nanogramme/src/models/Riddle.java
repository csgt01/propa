package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse hält alle Informationen über das Rätsel.
 * 
 * @author cschulte
 * 
 */
public class Riddle {

   /**
    * Liste der {@link Colour} des Rätsels.
    */
   private List<Colour> colours;

   private int width;
   private int height;

   private ArrayList<Row> rows;
   private ArrayList<Column> columns;

   private String nono;

   /**
    * Konstruktor
    */
   public Riddle() {
      colours = new ArrayList<Colour>();
   }

   /**
    * Konstruktor
    * 
    * @param colours
    * @param width
    * @param height
    * @param rows
    * @param columns
    * @param nono
    */
   public Riddle(List<Colour> colours, int width, int height, ArrayList<Row> rows, ArrayList<Column> columns, String nono) {
      super();
      this.colours = colours;
      this.width = width;
      this.height = height;
      this.rows = new ArrayList<Row>();
      for (Row row : rows) {
         this.rows.add(new Row(row));
      }
      this.columns = new ArrayList<Column>();
      for (Column column : columns) {
         this.columns.add(new Column(column));
      }
      this.nono = nono;
   }

   /**
    * @return the nono
    */
   public String getNono() {
      return nono;
   }

   /**
    * @param nono
    *           the nono to set
    */
   public void setNono(String nono) {
      this.nono = nono;
   }

   /**
    * @return Farben des Rätsels
    */
   public List<Colour> getColours() {
      return colours;
   }

   /**
    * @param colours
    */
   public void setColours(List<Colour> colours) {
      this.colours = colours;
   }

   /**
    * @return Breite des Rätsels
    */
   public int getWidth() {
      return width;
   }

   /**
    * @param width
    */
   public void setWidth(Integer width) {
      this.width = width;
   }

   /**
    * @return Höhe des Rätsels
    */
   public int getHeight() {
      return height;
   }

   /**
    * @param height
    */
   public void setHeight(Integer height) {
      this.height = height;
   }

   /**
    * @param colour
    */
   public void addColour(Colour colour) {
      if (null == colours) {
         colours = new ArrayList<Colour>();
      }
      colours.add(colour);
   }

   /**
    * @param row
    */
   public void addRow(Row row) {
      if (null == rows) {
         rows = new ArrayList<Row>();
      }
      rows.add(row);
   }

   /**
    * @param column
    */
   public void addColumn(Column column) {
      if (null == columns) {
         columns = new ArrayList<Column>();
      }
      columns.add(column);
   }

   /**
    * @param name
    * @return Name der Farbe
    */
   public Colour getColourByName(String name) {
      Colour returnColour = null;
      for (Colour colour : colours) {
         if (colour.getName() == (name.charAt(0))) {
            returnColour = colour;
         }
      }
      return returnColour;
   }

   /**
    * @return Reihen des Rätsels
    */
   public ArrayList<Row> getRows() {
      return rows;
   }

   /**
    * @param rows
    */
   public void setRows(ArrayList<Row> rows) {
      this.rows = rows;
   }

   /**
    * @return Spalten des Rätsels
    */
   public ArrayList<Column> getColumns() {
      return columns;
   }

   /**
    * @param columns
    */
   public void setColumns(ArrayList<Column> columns) {
      this.columns = columns;
   }

   @Override
   public String toString() {
      return "Riddle [colours=" + colours + ", \nwidth=" + width + ", height=" + height + ", \nrows=" + rows + ", \ncolumns=" + columns + "]";
   }

}
