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

	public Riddle() {
		colours = new ArrayList<Colour>();
	}

	public Riddle(List<Colour> colours, int width, int height,
			ArrayList<Row> rows, ArrayList<Column> columns, String nono) {
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
	 *            the nono to set
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
			rows = new ArrayList<Row>();
		}
		rows.add(row);
	}

	public void addColumn(Column column) {
		if (null == columns) {
			columns = new ArrayList<Column>();
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

	public ArrayList<Row> getRows() {
		return rows;
	}

	public void setRows(ArrayList<Row> rows) {
		this.rows = rows;
	}

	public ArrayList<Column> getColumns() {
		return columns;
	}

	public void setColumns(ArrayList<Column> columns) {
		this.columns = columns;
	}

	@Override
	public String toString() {
		return "Riddle [colours=" + colours + ", \nwidth=" + width
				+ ", height=" + height + ", \nrows=" + rows + ", \ncolumns="
				+ columns + "]";
	}

}
