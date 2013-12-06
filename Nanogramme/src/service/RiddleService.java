package service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Scanner;

import models.Block;
import models.Colour;
import models.Column;
import models.Riddle;
import models.Row;

public class RiddleService {

	private Riddle riddle = new Riddle();

	private int state = 0;

	private static int colorInt = 0;

	private int contentRow;
	private int contentColumn;

	public char[][] matrix;

	/**
	 * Lädt eine Datei ein und parst das Rätsel Reihe für Reihe in
	 * {@link #analyzeLine(String)}.
	 * 
	 * @param filename
	 * @return
	 */
	public Riddle readFile(String filename) {
		String nono = "";
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
			nono += (str + "\n");
		}
		System.out.println("Nono:\n" + nono);
		riddle.setNono(nono);
		// System.out.println("Riddle:" + riddle);
		showMatrix();
		return riddle;
	}

	/**
	 * Display the matrix.
	 * 
	 * @param matrix
	 * @throws Exception
	 */
	private void showMatrix() {
		String methodName = "showMatrix()";
		System.out.println(methodName);
		long startTime = new Date().getTime();
		for (int i = 0; i < riddle.getHeight(); i++) {
			String out = "";
			for (int j = 0; j < riddle.getWidth(); j++) {
				out += matrix[i][j];
				out += "  ";
			}
			System.out.println(out);
		}
		System.out.println();
		// showBlockGoneTrue(matrix);
		System.out.println("Time for " + methodName + ": "
				+ (new Date().getTime() - startTime) + " ms");
	}

	/**
	 * Analysiert eine Reihe in {@link #readFile(String)}. TODO: Content.
	 * 
	 * @param str
	 */
	public void analyzeLine(String str) {
		str = str.trim();
		if (!str.startsWith("#")) {
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
					colour.setNameInt(RiddleService.colorInt);
					colorInt++;
					riddle.addColour(colour);
				}
				break;
			case 2:
				if (str.startsWith("column")) {
					state = 3;
				} else {
					str = str.trim();
					Row row = new Row();
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
					riddle.addRow(row);
				}
				break;
			case 3:
				setupMatrix();
				if (str.startsWith("content")) {
					contentColumn = 0;
					contentRow = 0;
					state = 4;
				} else {
					str = str.trim();
					Column column = new Column();
					String[] blocks = str.split(",");
					for (int i = 0; i < blocks.length; i++) {
						Block cb = new Block();
						String block = blocks[i];
						block = block.trim();
						if (!block.equals("0") && !block.equals("")) {
							System.out.println(block);
							System.out.println(riddle.getColourByName(block
									.substring(block.length() - 1)));
							cb.setColour(riddle.getColourByName(block
									.substring(block.length() - 1)));
							cb.setHowMany(Integer.valueOf(block.substring(0,
									(block.length() - 1))));
							column.addBlock(cb);
						}
					}
					riddle.addColumn(column);
				}
				break;
			case 4:
				System.out.println(contentRow);
				System.out.println(contentColumn);
				System.out.println(str + "\n");
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
		// String methodName = "setupBlocks()";
		// System.out.println(methodName);
		// long startTime = new Date().getTime();
		matrix = new char[riddle.getHeight()][riddle.getWidth()];
		for (int i = 0; i < riddle.getHeight(); i++) {
			for (int j = 0; j < riddle.getWidth(); j++) {
				matrix[i][j] = '*';
			}
		}
		// System.out.println("Time for " + methodName + ": " + (new
		// Date().getTime() - startTime) + " ms");
	}

	// TODO: better and save twice!!!!
	/**
	 * Speichert das Rätsel als nono-Datei.
	 * @param matrix
	 */
	public boolean save(char[][] matrix) {
		BufferedWriter writer = null;
		boolean saved = false;
		try {
			writer = new BufferedWriter(new FileWriter("save.nono"));
			String nono = riddle.getNono();
			if (nono == null) {
				nono = createNono(riddle);
			}
			if (nono.contains("content")) {
				String[] nonoSplit = nono.split("content");
				nono = nonoSplit[0];
			}
			writer.write(nono);
			writer.write("content\n");
			for (int row = 0; row < riddle.getHeight(); row++) {
				String rowString = "";
				for (int column = 0; column < riddle.getWidth(); column++) {
					rowString += matrix[row][column];
				}
				System.out.println(rowString + "\n");
				writer.write(rowString + "\n");
			}
			saved = true;
		} catch (IOException e) {
			System.out.println(e);
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
	 * Erstellt das Rätsel.
	 * 
	 * @param image
	 *            Basisbild
	 */
	public Riddle createRiddle(BufferedImage image) {
		Riddle riddle = new Riddle();
		riddle.setHeight(image.getHeight());
		riddle.setWidth(image.getWidth());
		LinkedHashSet<Color> colors = new LinkedHashSet<Color>();
		// TODO: eine Farbe als Hintergrund!!!!!
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				System.out.println(j + "/" + i + ":"
						+ new Color(image.getRGB(j, i)));
				colors.add(new Color(image.getRGB(j, i)));
			}
		}
		System.out.println(colors.size());
		
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
		System.out.println(colors.size());
		LinkedList<Colour> col = new LinkedList<Colour>();
		int co = 0;
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
		LinkedList<Row> rows = new LinkedList<Row>();
		LinkedList<Column> columns = new LinkedList<Column>();
		Colour backgroundCol = col.get(0);
		for (int i = 0; i < image.getHeight(); i++) {
			Row row = new Row();
			ArrayList<Block> blocks = new ArrayList<Block>();
			Block block = null;
			Integer blockSize = null;
			for (int j = 0; j < image.getWidth(); j++) {
				Color c = new Color(image.getRGB(j, i));
				System.out.println(i + "/" + j + ":" + c);
				Colour currentColour = colarMap.get(c);
				System.out.println(currentColour);
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
				System.out.println(i + "/" + j + ":" + c);
				Colour currentColour = colarMap.get(c);
				System.out.println(currentColour);
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
		System.out.println(riddle);
		riddle.getColours().remove(backgroundCol);
		System.out.println(riddle);
		// setupMatrix(riddle.getHeight(), riddle.getWidth(),
		// riddle.getRows(), riddle.getColumns());
		return riddle;
	}

	/**
	 * Erstellt eine nono-Datei aus dem Rätsel.
	 * 
	 * @param riddle2
	 * @return
	 */
	private String createNono(Riddle riddle2) {
		return null;
	}

}
