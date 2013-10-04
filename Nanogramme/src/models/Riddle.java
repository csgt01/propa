package models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class Riddle {

    private List<Colour> colours;
    
    private int width;
    private int height;
    
    private int rowCount = 0;
    private int columnCount = 0;
    
    private LinkedHashMap<Integer, Row> rows;
    private LinkedHashMap<Integer, Column> columns;
    
    public Riddle() {
        colours = new ArrayList<Colour>();
    }

    public List<Colour> getColours() {
        return colours;
    }

    public void setColours(List<Colour> colours) {
        this.colours = colours;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
    
    public void addColour(Colour colour) {
        if (null == colours) {
            colours = new ArrayList<Colour>();
        }
        colours.add(colour);
    }
    
    public void addRow(Row row) {
        if (null == rows) {
            rows = new LinkedHashMap<Integer, Row>();
        }
        rows.put(rowCount, row);
        rowCount++;
    }
    
    public void addColumn(Column column) {
        if (null == columns) {
            columns = new LinkedHashMap<Integer, Column>();
        }
        columns.put(columnCount, column);
        columnCount++;
    }
    
    public Colour getColourByName(String name) {
        Colour returnColour = null;
        for (Colour colour : colours) {
            if (colour.getName().equals(name)) {
                returnColour = colour;
            }
        }
        return returnColour;
    }
    
    

    public LinkedHashMap<Integer, Row> getRows() {
        return rows;
    }

    public void setRows(LinkedHashMap<Integer, Row> rows) {
        this.rows = rows;
    }

    public LinkedHashMap<Integer, Column> getColumns() {
        return columns;
    }

    public void setColumns(LinkedHashMap<Integer, Column> columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        return "Riddle [colours=" + colours + ", width=" + width
                + ", height=" + height + ", rowCount=" + rowCount
                + ", columnCount=" + columnCount + ", rows=" + rows
                + ", columns=" + columns + "]";
    }
    
    
    
}
