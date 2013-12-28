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
 * 
 * @author csgt
 *
 */
public class RiddleService {

	/**
	 * Das Rätsel.
	 */
	private Riddle riddle = new Riddle();

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
	 * Spielmatrix.
	 */
	public char[][] matrix;

	/**
	 * Verbundene UI.
	 */
	private IUIListener listener;

	/**
	 * Konstruktor.
	 * @param listener
	 */
	public RiddleService(IUIListener listener) {
		this.listener = listener;
	}

	/**
	 * Lädt eine Datei ein und parst das Rätsel Reihe für Reihe in
	 * {@link #analyzeLine(String)}.
	 * 
	 * @param filename Pfad zum Rätsel.
	 * @return Riddle
	 */
	public Riddle readFile(String filename) {
		String nono = "";
		Scanner scanner = null;
		ArrayList<String> lines = new ArrayList<String>();
		try {
			scanner = new Scanner(new File(filename), "UTF-8");
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
			nono += (str + "\n");
		}
//		System.out.println("Nono:\n" + nono);
		riddle.setNono(nono);
		// System.out.println("Riddle:" + riddle);
		showMatrix();
		return riddle;
	}

	/**
	 * Display the matrix.
	 * 
	 * @throws Exception
	 */
	private void showMatrix() {
//		String methodName = "showMatrix()";
//		System.out.println(methodName);
//		long startTime = new Date().getTime();
//		for (int i = 0; i < riddle.getHeight(); i++) {
//			String out = "";
//			for (int j = 0; j < riddle.getWidth(); j++) {
//				out += matrix[i][j];
//				out += "  ";
//			}
//			System.out.println(out);
//		}
//		System.out.println();
//		// showBlockGoneTrue(matrix);
//		System.out.println("Time for " + methodName + ": "
//				+ (new Date().getTime() - startTime) + " ms");
	}
	
	int rowInt = 0;
	int columnInt = 0;

	/**
	 * Analysiert eine Reihe in {@link #readFile(String)}.
	 * 
	 * @param str
	 */
	private void analyzeLine(String str) {
		str = str.trim();
		if (!str.startsWith("#")) {
			// TODO: regex wgitespace and tab
			switch (parsingState) {
			case 0:
				if (str.startsWith("width")) {
					String splitted = str.split("width ")[1];
					riddle.setWidth(Integer.valueOf(splitted));
				} else if (str.startsWith("height")) {
					riddle.setHeight(Integer.valueOf(str.split("height ")[1]));
				} else if (str.startsWith("color")) {
					parsingState = 1;
				}
				break;
			case 1:
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
				break;
			case 2:
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
							cb.setColour(riddle.getColourByName(block
									.substring(block.length() - 1)));
							cb.setHowMany(Integer.valueOf(block.substring(0,
									(block.length() - 1))));
							row.addBlock(cb);
						}
					}
					rowInt++;
					riddle.addRow(row);
				}
				break;
			case 3:
				setupMatrix();
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
//							System.out.println(block);
//							System.out.println(riddle.getColourByName(block
//									.substring(block.length() - 1)));
							cb.setColour(riddle.getColourByName(block
									.substring(block.length() - 1)));
							cb.setHowMany(Integer.valueOf(block.substring(0,
									(block.length() - 1))));
							column.addBlock(cb);
						}
					}
					column.setIndex(columnInt);
//					System.out.println(columnInt);
					columnInt++;
					riddle.addColumn(column);
				}
				break;
			case 4:
//				System.out.println(contentRow);
//				System.out.println(contentColumn);
//				System.out.println(str + "\n");
				for (int i = 0; i < str.length(); i++) {
					if (str.charAt(i) != ' ') {
						matrix[contentRow][contentColumn] = str.charAt(i);
						contentColumn++;
					}
				}
				if (str.length() > 0 && !str.contains("content")) {
					contentRow++;
				}
				contentColumn = 0;
				break;
			default:
				break;
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
	 * Speichert das Rätsel als nono-Datei.
	 * 
	 * @param matrix
	 */
	protected boolean save(char[][] matrix) {
		BufferedWriter writer = null;
		boolean saved = false;
		 File file = listener.getSaveFile();
//		System.out.println(riddle);
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file + ".nono"), "UTF-8"));
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
//				System.out.println(rowString + "\n");
				writer.write(rowString + System.getProperty("line.separator"));
			}
			saved = true;
		} catch (IOException e) {
//			System.out.println(e);
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {

			}
		}
		if (saved) {
        
      }
      return saved;
	}

	/**
	 * Erstellt das Rätsel.
	 * 
	 * @param image
	 *            Basisbild
	 * @return das Rätsel
	 */
	public Riddle createRiddle(BufferedImage image) {
		Riddle riddle = new Riddle();
		riddle.setHeight(image.getHeight());
		riddle.setWidth(image.getWidth());
		LinkedHashSet<Color> colors = new LinkedHashSet<Color>();
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
//				System.out.println(j + "/" + i + ":"
//						+ new Color(image.getRGB(j, i)));
				colors.add(new Color(image.getRGB(j, i)));
			}
		}
//		System.out.println(colors.size());

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
//		System.out.println(colors.size());
		LinkedList<Colour> col = new LinkedList<Colour>();
		int co = 0;
		// awt.Color zu models.Colour mappen
		HashMap<Color, Colour> colarMap = new HashMap<Color, Colour>();
		for (Color color : colors) {
			Colour colour = new Colour();
			colour.setRed(color.getRed());
			colour.setGreen(color.getGreen());
			colour.setBlue(color.getBlue());
			colour.setName(mapping.get(co).charAt(0));
			co++;
			col.add(colour);
			colarMap.put(color, colour);
		}
		riddle.setColours(col);
		ArrayList<Row> rows = new ArrayList<Row>();
		ArrayList<Column> columns = new ArrayList<Column>();
		
		// setzen der Hintergrundfarbe
		Colour backgroundCol = listener.getBackgroundColour(col);

		// setzen der Blöcke in Reihe und Spalte
		for (int i = 0; i < image.getHeight(); i++) {
			Row row = new Row();
			ArrayList<Block> blocks = new ArrayList<Block>();
			Block block = null;
			Integer blockSize = null;
			for (int j = 0; j < image.getHeight(); j++) {
				Color c = new Color(image.getRGB(j, i));
				// System.out.println(i + "/" + j + ":" + c);
				Colour currentColour = colarMap.get(c);
				// System.out.println(currentColour);
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
			row.setBlocks(blocks);
			rows.add(row);
		}
		riddle.setRows(rows);
		for (int i = 0; i < image.getWidth(); i++) {
			Column column = new Column();
			ArrayList<Block> blocks = new ArrayList<Block>();
			Block block = null;
			Integer blockSize = null;
			for (int j = 0; j < image.getHeight(); j++) {
				Color c = new Color(image.getRGB(i, j));
				// System.out.println(i + "/" + j + ":" + c);
				Colour currentColour = colarMap.get(c);
				// System.out.println(currentColour);
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
		riddle.setColumns(columns);
		riddle.getColours().remove(backgroundCol);
//		System.out.println("riddle in create in rs:" + riddle);
		this.riddle = riddle;
		return riddle;
	}

	/**
	 * Erstellt eine nono-Datei aus dem Rätsel.
	 * 
	 * @param riddle2
	 * @return neue nono.
	 */
	private String createNono(Riddle riddle2) {
//		System.out.println("CREATENONO:" + riddle2);
		String nono = "";
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("width %1d", riddle.getWidth()));
		sb.append(System.getProperty("line.separator"));
		sb.append(String.format("height %1d", riddle.getHeight()));
		sb.append(System.getProperty("line.separator"));
		sb.append("colors");
		sb.append(System.getProperty("line.separator"));
		for (Colour colour : riddle.getColours()) {
			sb.append(String.format("%1s %d,%d,%d",
					String.valueOf(colour.getName()), colour.getRed(),
					colour.getGreen(), colour.getBlue()));
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
					sb.append(String.format("%d%s", block.getHowMany(),
							block.getColourString()));
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
					sb.append(String.format("%d%s", block.getHowMany(),
							block.getColourString()));
				}
				sb.append(System.getProperty("line.separator"));
			} else {
				sb.append("0");
				sb.append(System.getProperty("line.separator"));
			}
		}
		nono = sb.toString();
//		System.out.println("nono:" + nono);
		riddle.setNono(nono);
		return nono;
	}

}
