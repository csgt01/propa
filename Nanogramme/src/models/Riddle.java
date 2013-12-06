package models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Riddle {

    private List<Colour> colours;
    
    private int width;
    private int height;
    
    private LinkedList<Row> rows;
    private LinkedList<Column> columns;
    
    private String nono;
    
    public Riddle() {
        colours = new ArrayList<Colour>();
    }
    
    

    public Riddle(List<Colour> colours, int width, int height,
			LinkedList<Row> rows, LinkedList<Column> columns, String nono) {
		super();
		this.colours = colours;
		this.width = width;
		this.height = height;
		this.rows = rows;
//				new LinkedList<Row>();
//		for (Row row : rows) {
//			this.rows.add(row);
//		}
		this.columns = columns;
//				new LinkedList<Column>();
//		for (Column column : columns) {
//			this.columns.add(column);
//		}
		this.nono = nono;
	}



	/**
	 * @return the nono
	 */
	public String getNono() {
		return nono;
	}



	/**
	 * @param nono the nono to set
	 */
	public void setNono(String nono) {
		this.nono = nono;
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
            rows = new LinkedList<Row>();
        }
        rows.add(row);
    }
    
    public void addColumn(Column column) {
        if (null == columns) {
            columns = new LinkedList<Column>();
        }
        columns.add(column);
    }
    
    public Colour getColourByName(String name) {
        Colour returnColour = null;
        for (Colour colour : colours) {
            if (colour.getName() == (name.charAt(0))) {
                returnColour = colour;
            }
        }
        return returnColour;
    }
    
    

    public LinkedList<Row> getRows() {
        return rows;
    }

    public void setRows(LinkedList<Row> rows) {
        this.rows = rows;
    }

    public LinkedList<Column> getColumns() {
        return columns;
    }

    public void setColumns(LinkedList<Column> columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        return "Riddle [colours=" + colours + ", \nwidth=" + width
                + ", height=" + height + ", \nrows=" + rows
                + ", \ncolumns=" + columns + "]";
    }
    
    
    
}
