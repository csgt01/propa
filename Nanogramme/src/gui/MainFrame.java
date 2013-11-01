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
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import models.Block;
import models.Colour;
import models.Column;
import models.IPlayGame;
import models.PlayGame;
import models.Row;

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
					 JPanel rowPanel = new JPanel(new
					 GridBagLayout());
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
				   JPanel rowPanel = new JPanel(new
		                GridBagLayout());
		                rowPanel.setBorder(border);
					c.gridx = 0;
					// JPanel rowPanel = new JPanel();
					// rowPanel.setBorder(border);
					JPanel columnPanel = new JPanel(new
                     GridBagLayout());
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
		File file = getFileOrDirectryFromChooser(applikation,
				JFileChooser.OPEN_DIALOG);
		System.out.println(file.getAbsoluteFile());
		try {
			playGame.openFile(file.getAbsoluteFile().toString());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public File getFileOrDirectryFromChooser(Component parent, int type) {
		JFileChooser chooser = null;
		if (lastSelectedDir != null)
			chooser = new JFileChooser(lastSelectedDir);
		else
			chooser = new JFileChooser();
		chooser.setFileSelectionMode(type);
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
}
