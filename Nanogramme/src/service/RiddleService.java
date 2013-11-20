package service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
				for (int i = 0; i < str.length();  i++) {
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
	public void save(char[][] matrix) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("save.nono"));
			String nono = riddle.getNono();
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
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
			}
		}

	}

}
