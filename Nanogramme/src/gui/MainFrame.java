package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;

import models.Block;
import models.Colour;
import models.Column;
import models.IPlayGame;
import models.PlayGame;
import models.Riddle;
import models.Row;
import picture.PictureService;

public class MainFrame extends JFrame implements ActionListener, IPlayGame {

	/**
    * 
    */
	private static final long serialVersionUID = -8026416994513756565L;

	private MyMenuBar menuBar = new MyMenuBar();
	public JFrame applikation;
	public Container container;
	public JMenuBar menueLeiste;

	private static File lastSelectedDir = null;

	private JPanel panelTop;

	private PlayGame playGame;

	private JToolBar toolbar;

	private JLabel[][] labels;

	private JPanel panelLeft;

	private Color backgroungColor = Color.WHITE;

	private PictureService ps = new PictureService();

	public MainFrame() {
		playGame = new PlayGame(this);
		init();
	}

	private void init() {

		applikation = new JFrame("Main");
		container = applikation.getContentPane();

		// Menüleiste erzeugen
		menueLeiste = new MyMenuBar(this, playGame);

		applikation.add(menueLeiste, BorderLayout.NORTH);
		// applikation.add(new JScrollPane(textarea), BorderLayout.CENTER);

		applikation.setSize(600, 600);
		applikation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		toolbar = new JToolBar();
		applikation.add(toolbar, BorderLayout.SOUTH);

		applikation.setVisible(true);
	}

	Border border = LineBorder.createGrayLineBorder();

	/**
    * 
    */
	@Override
	public void setupMatrix(int rowInt, int columnInt, List<Row> rows,
			List<Column> columns) {
		System.out.println("setupMatrix in MainFrame");
		labels = new JLabel[rowInt][columnInt];
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		GridBagConstraints c = new GridBagConstraints();
		// We need to define the GridBagConstraints.
		// This defines the widget to go in grid (0,0)

		c.gridx = 0;
		c.gridy = 0;

		// This tells the widget not to change size
		// to fit the cell.

		c.fill = GridBagConstraints.NONE;
		for (int i = 0; i < rowInt; i++) {

			if (i == 0) {
				c.gridy = 0;
				JLabel leer = new JLabel("Anzahl");
				leer.setSize(170, 170);
				panel.add(leer);
				c.gridx += 1;
				GridBagConstraints c1 = new GridBagConstraints();
				c1.gridx = 0;
				c1.gridy = 0;
				c1.fill = GridBagConstraints.NONE;
				for (Column column : columns) {
					c1.gridx = 0;
					c1.gridy = 0;
					JPanel rowPanel = new JPanel(new GridBagLayout());
					rowPanel.setBorder(border);
					if (column.getBlocks() != null
							&& column.getBlocks().size() > 0) {
						for (Block block : column.getBlocks()) {
							JLabel comp = new JLabel(block.getHowMany() + " "
									+ block.getColor());
							comp.setForeground(new Color(block.getColour()
									.getRed(), block.getColour().getGreen(),
									block.getColour().getBlue()));
							// rowPanel.add(comp);
							comp.setSize(30, 30);
							comp.setBorder(border);
							comp.setSize(170, 30);
							rowPanel.add(comp, c1);
							c1.gridy += 1;
						}
						panel.add(rowPanel, c);
					} else {
						JLabel jLabel = new JLabel("Leer");
						jLabel.setSize(30, 170);
						panel.add(jLabel, c);
					}
					c.gridx += 1;
				}
			}
			c.gridy = (i + 1);
			GridBagConstraints c2 = new GridBagConstraints();
			c2.gridx = 0;
			c2.gridy = 0;
			c2.fill = GridBagConstraints.NONE;
			for (int j = 0; j < columnInt; j++) {
				if (j == 0) {
					JPanel rowPanel = new JPanel(new GridBagLayout());
					rowPanel.setBorder(border);
					c.gridx = 0;
					// JPanel rowPanel = new JPanel();
					// rowPanel.setBorder(border);
					JPanel columnPanel = new JPanel(new GridBagLayout());
					columnPanel.setBorder(border);
					if (rows.get(i).getBlocks() != null
							&& rows.get(i).getBlocks().size() > 0) {
						for (Block block : rows.get(i).getBlocks()) {

							JLabel comp = new JLabel(block.getHowMany() + " "
									+ block.getColor());
							comp.setForeground(new Color(block.getColour()
									.getRed(), block.getColour().getGreen(),
									block.getColour().getBlue()));
							comp.setBorder(border);
							comp.setSize(170, 30);
							columnPanel.add(comp, c2);
							c2.gridx += 1;
						}

					} else {
						JLabel jLabel = new JLabel("Leer");
						jLabel.setSize(170, 30);
						columnPanel.add(jLabel, c2);
					}
					panel.add(columnPanel, c);
				}
				c.gridx = (j + 1);
				JLabel button = new JLabel("" + i + "--" + j);
				button.addMouseListener(playGame);
				button.setOpaque(true);
				button.setForeground(Color.LIGHT_GRAY);
				button.setBackground(Color.LIGHT_GRAY);
				button.setBorder(border);
				button.setPreferredSize(new Dimension(30, 30));
				panel.add(button, c);
				labels[i][j] = button;
			}
		}
		container.add(panel, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Action");
		System.out.println(e.getActionCommand());
		if (e.getActionCommand().equalsIgnoreCase("Rätsel laden")) {

			File file = getFileOrDirectryFromChooser(applikation,
					JFileChooser.OPEN_DIALOG);
			System.out.println(file.getAbsoluteFile());
			backgroungColor = null;
			toolbar = new JToolBar();
			applikation.add(toolbar, BorderLayout.SOUTH);
			try {
				playGame.openFile(file.getAbsoluteFile().toString());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			File file = getFileOrDirectryFromChooser(applikation,
					JFileChooser.OPEN_DIALOG);
			System.out.println(file.getAbsoluteFile());
			String height = JOptionPane.showInputDialog(null, "Höhe:",
					"Eine Eingabeaufforderung", JOptionPane.PLAIN_MESSAGE);
			String width = JOptionPane.showInputDialog(null, "Breite",
					"Eine Eingabeaufforderung", JOptionPane.PLAIN_MESSAGE);

			String numberOfColors = JOptionPane.showInputDialog(null,
					"Anzahl der Farben", "Eine Eingabeaufforderung",
					JOptionPane.PLAIN_MESSAGE);
			BufferedImage image = ps.loadAndDowColorPicture(file.getAbsoluteFile()
					.toString(), Integer.valueOf(height), Integer
					.valueOf(width), Integer.valueOf(numberOfColors));
			JDialog dialog = new JDialog(applikation);
			dialog.setTitle("Bild");
			ImageIcon ii = new ImageIcon(image);
			JLabel label = new JLabel("", ii, JLabel.CENTER);
			dialog.add(label, BorderLayout.CENTER);
			dialog.setVisible(true);
			System.out.println(image.getHeight());
			System.out.println(image.getData().getHeight());
			System.out.println(image.getTileHeight());
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
					System.out.println(i + "/" + j + ":"
							+ c);
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
					System.out.println(i + "/" + j + ":"
							+ c);
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
//			setupMatrix(riddle.getHeight(), riddle.getWidth(), riddle.getRows(), riddle.getColumns());
			backgroungColor = null;
			toolbar = new JToolBar();
			applikation.add(toolbar, BorderLayout.SOUTH);
			playGame.setupIt(riddle);
		}

		// String eingabe =
		// JOptionPane.showInputDialog(null,"Geben Sie Ihren Namen ein",
		// "Eine Eingabeaufforderung",
		// JOptionPane.PLAIN_MESSAGE);
	}

	public File getFileOrDirectryFromChooser(Component parent, int type) {
		JFileChooser chooser = null;
		if (lastSelectedDir != null) {
			chooser = new JFileChooser(lastSelectedDir);
		} else {
			chooser = new JFileChooser();
		}
		chooser.setFileSelectionMode(type);
		chooser.addChoosableFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean accept(File file) {
				String filename = file.getName();
				return (filename.endsWith(".jgp") || filename.endsWith(".JPG")
						|| filename.endsWith(".gif")
						|| filename.endsWith(".GIF") || file.isDirectory());
			}
		});
		int ret = chooser.showOpenDialog(parent);
		if (ret == JFileChooser.APPROVE_OPTION) {
			File selected = chooser.getSelectedFile();
			if (selected.isDirectory())
				lastSelectedDir = selected;
			else
				lastSelectedDir = selected.getParentFile();
			return selected;
		}
		return null;
	}

	@Override
	public boolean placeAField(int row, int column, Colour colour) {
		if (null != colour) {
			// labels[row][column].setOpaque(true);
			labels[row][column].setForeground(new Color(colour.getRed(), colour
					.getGreen(), colour.getBlue()));
			labels[row][column].setBackground(new Color(colour.getRed(), colour
					.getGreen(), colour.getBlue()));
		} else {
			labels[row][column].setForeground(Color.LIGHT_GRAY);
			labels[row][column].setBackground(Color.LIGHT_GRAY);
		}
		return false;
	}

	@Override
	public void setColours(List<Colour> colours) {
		toolbar.setName("Farben");
		JButton backgroundButton = new JButton("-");
		backgroundButton.setForeground(Color.DARK_GRAY);
		backgroundButton.addActionListener(playGame);
		backgroundButton.setToolTipText("Leerfeld setzen wird ausgewählt.");
		toolbar.add(backgroundButton);
		JButton resetButton = new JButton("Reset");
		resetButton.setForeground(Color.DARK_GRAY);
		resetButton.addActionListener(playGame);
		resetButton
				.setToolTipText("Zurücksetzen eines Feldes wird ausgewählt.");
		toolbar.add(resetButton);
		for (Colour colour : colours) {
			Color color = new Color(colour.getRed(), colour.getGreen(),
					colour.getBlue());
			JButton comp = new JButton(String.valueOf(colour.getName()));
			comp.setForeground(color);
			comp.addActionListener(playGame);
			comp.setToolTipText("Diese Farbe auswählen um ein Feld zu setzen.");
			toolbar.add(comp);
		}
	}

	@Override
	public void setLeftPAnel(List<Row> rows) {
		panelLeft = new JPanel(new GridLayout(rows.size(), 1));
		for (Row row : rows) {
			JPanel rowPanel = new JPanel();
			rowPanel.setBorder(border);
			if (row.getBlocks() != null && row.getBlocks().size() > 0) {
				for (Block block : row.getBlocks()) {
					JLabel comp = new JLabel(block.getHowMany() + " "
							+ block.getColor());
					comp.setForeground(new Color(block.getColour().getRed(),
							block.getColour().getGreen(), block.getColour()
									.getBlue()));
					rowPanel.add(comp);
				}
			} else {
				rowPanel.add(new JLabel("Leer"));
			}
			panelLeft.add(rowPanel);
		}

		applikation.add(panelLeft, BorderLayout.WEST);
	}

	@Override
	public void setTopPanel(List<Column> columns) {
		// TODO Auto-generated method stub

	}

	@Override
	public void wasRight(boolean isRight) {
		System.out.println("wasRight:" + isRight);
		if (isRight) {
			applikation.setBackground(Color.GREEN);
		}

	}

	@Override
	public void showAlert(String string) {
		System.out.println("showAlert");
		JOptionPane.showMessageDialog(applikation, string, string, JOptionPane.WARNING_MESSAGE);
	}
}
