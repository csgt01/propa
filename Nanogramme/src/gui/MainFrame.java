package gui;

import interfaces.IPictureService;
import interfaces.IPlaygame;
import interfaces.IUIListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;

import models.Block;
import models.Colour;
import models.Column;
import models.Row;
import picture.PictureService;
import service.PlayGame;

/**
 * Enthält die GUI des Programms.
 * 
 * @author cschulte
 * 
 */
public class MainFrame extends JFrame implements ActionListener, IUIListener {

   /**
    * 
    */
   private static final long serialVersionUID = -8026416994513756565L;

   /**
    * 
    */
   private Border border = LineBorder.createGrayLineBorder();
   
   /**
    * Die Applikation
    */
   private JFrame applikation;
   
   /**
    * Container
    */
   private Container container;
   
   /**
    * Das Menu
    */
   private JMenuBar menuLeiste;
   
   /**
    * Die Bottom-Toolbar
    */
   private JToolBar toolbar;
   
   /**
    * Alle Felder der Matrix
    */
   private JLabel[][] labels;
   
   /**
    * Gemerktes Verzeichnis fuer das Erstellen eines Raetsels
    */
   private static File lastSelectedDirForPicture = null;
   
   /**
    * Gemerktes Verzeichnis fuer das Laden eines Raetsels
    */
   private static File lastSelectedDirForLoad = null;
   
   // Services und Listener
   /**
    * Nutze diesen PictureService
    */
   private IPictureService ps;
   
   /**
    * Nutze diesen PlayGame
    */
   private IPlaygame playGame;
   
   /**
    * Scrollen fuer die Matrix
    */
   private JScrollPane scrollbar;

   /**
    * Konstruktor
    */
   public MainFrame() {

   }

   /**
    * Laedt den JFrame.
    */
   protected void init() {
      playGame = new PlayGame(this);
      ps = new PictureService();
      applikation = new JFrame("Main");
      container = applikation.getContentPane();

      // Menueleiste erzeugen
      menuLeiste = new MyMenuBar(this, playGame);

      applikation.add(menuLeiste, BorderLayout.NORTH);
      // applikation.add(new JScrollPane(textarea), BorderLayout.CENTER);

      applikation.setSize(600, 600);
      applikation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      toolbar = new JToolBar();
      applikation.add(toolbar, BorderLayout.SOUTH);
      applikation.setVisible(true);
   }

   @Override
   public Colour getBackgroundColour(LinkedList<Colour> colours) {
      setColours(colours);
      String[] buttons = new String[colours.size()];
      for (int i = 0; i < colours.size(); i++) {
         buttons[i] = String.valueOf(colours.get(i).getName());
      }
      int returnValue = JOptionPane.showOptionDialog(null, "Bitte waehlen Sie aus den Farben in der Bottombar eine als Hintergrund aus.", "Hintergrundauswahl", JOptionPane.WARNING_MESSAGE, 0, null,
            buttons, buttons[0]);
      if (returnValue == -1) {
         return colours.get(0);
      }
      return colours.get(returnValue);
   }

   @Override
   public void setupUIMatrix(int rowInt, int columnInt, List<Row> rows, List<Column> columns) {
      if (scrollbar != null) {
         container.remove(scrollbar);
         container.validate();
         container.repaint();
      }
      labels = new JLabel[rowInt][columnInt];

      JPanel panel = new JPanel(new GridBagLayout());

      panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
      GridBagConstraints c = new GridBagConstraints();
      c.gridx = 0;
      c.gridy = 0;

      c.fill = GridBagConstraints.NONE;
      setupRowAndColumns(rowInt, columnInt, rows, columns, panel, c);
      scrollbar = new JScrollPane(panel);
      container.add(scrollbar, BorderLayout.CENTER);
   }

   /**
    * Erstellt und fuellt die Matrix (Spielfeld) des UI.
    * 
    * @param rowInt
    *           Anzahl der Reihen
    * @param columnInt
    *           Anzahl der Spalten
    * @param rows
    *           Liste der Reihen
    * @param columns
    *           Liste der Spalten
    * @param panel
    *           das Panel, in das die MAtrix geschrieben wird
    * @param c
    *           GridConstraints
    */
   private void setupRowAndColumns(int rowInt, int columnInt, List<Row> rows, List<Column> columns, JPanel panel, GridBagConstraints c) {
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
            insertColumns(columns, panel, c, c1);
         }
         c.gridy = (i + 1);
         GridBagConstraints c2 = new GridBagConstraints();
         c2.gridx = 0;
         c2.gridy = 0;
         c2.fill = GridBagConstraints.NONE;
         insertRows(columnInt, rows, panel, c, i, c2);
      }
   }

   /**
    * Erstellt die Spalten der Matrix.
    * 
    * @param columns
    * @param panel
    * @param c
    * @param c1
    */
   private void insertColumns(List<Column> columns, JPanel panel, GridBagConstraints c, GridBagConstraints c1) {
      for (Column column : columns) {
         c1.gridx = 0;
         c1.gridy = 0;
         JPanel rowPanel = new JPanel(new GridBagLayout());
         rowPanel.setBorder(border);
         if (column.getBlocks() != null && column.getBlocks().size() > 0) {
            insertColumnInUIMatrix(panel, c, c1, column, rowPanel);
         } else {
            JLabel jLabel = new JLabel("Leer");
            jLabel.setSize(30, 170);
            panel.add(jLabel, c);
         }
         c.gridx += 1;
      }
   }

   /**
    * Erstellt eine Reihe in der UI-MAtrix.
    * 
    * @param columnInt
    * @param rows
    * @param panel
    * @param c
    * @param i
    * @param c2
    */
   private void insertRows(int columnInt, List<Row> rows, JPanel panel, GridBagConstraints c, int i, GridBagConstraints c2) {
      for (int j = 0; j < columnInt; j++) {
         if (j == 0) {
            JPanel rowPanel = new JPanel(new GridBagLayout());
            rowPanel.setBorder(border);
            c.gridx = 0;
            // JPanel rowPanel = new JPanel();
            // rowPanel.setBorder(border);
            JPanel columnPanel = new JPanel(new GridBagLayout());
            columnPanel.setBorder(border);
            if (rows.get(i).getBlocks() != null && rows.get(i).getBlocks().size() > 0) {
               insertRowInUIMatrix(rows, i, c2, columnPanel);
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

   /**
    * Setzt eine Beschriftung wie viele Bloecke es in der Reihe gibt in die UI
    * ein.
    * 
    * @param rows
    *           Liste der Reihen
    * @param i
    *           Index der Reihe.
    * @param c2
    *           constraints
    * @param columnPanel
    *           das Panel
    */
   private void insertRowInUIMatrix(List<Row> rows, int i, GridBagConstraints c2, JPanel columnPanel) {
      for (Block block : rows.get(i).getBlocks()) {

         JLabel comp = new JLabel(block.getHowMany() + " " + block.getColourString());
         comp.setForeground(new Color(block.getColour().getRed(), block.getColour().getGreen(), block.getColour().getBlue()));
         comp.setBorder(border);
         comp.setSize(170, 30);
         columnPanel.add(comp, c2);
         c2.gridx += 1;
      }
   }

   /**
    * Erstellt eine Spalte in der Matrix der UI.
    * 
    * @param panel
    * @param c
    * @param c1
    * @param column
    * @param rowPanel
    */
   private void insertColumnInUIMatrix(JPanel panel, GridBagConstraints c, GridBagConstraints c1, Column column, JPanel rowPanel) {
      for (Block block : column.getBlocks()) {
         JLabel comp = new JLabel(block.getHowMany() + " " + block.getColourString());
         comp.setForeground(new Color(block.getColour().getRed(), block.getColour().getGreen(), block.getColour().getBlue()));
         // rowPanel.add(comp);
         comp.setSize(30, 30);
         comp.setBorder(border);
         comp.setSize(170, 30);
         rowPanel.add(comp, c1);
         c1.gridy += 1;
      }
      panel.add(rowPanel, c);
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      // Ein Raetsel soll geladen werden.
      if (e.getActionCommand().equalsIgnoreCase("Rätsel laden")) {
         // Datei auswaehlen.
         File file = getFileOrDirectryFromChooser(applikation, JFileChooser.OPEN_DIALOG, true);
         if (file != null && file.getAbsoluteFile() != null) {
            if (!file.getName().endsWith("nono")) {
               showAlert("Nur .nono Dateien laden.");
               return;
            }
            if (!playGame.openRiddleFromFile(file.getAbsoluteFile().toString())) {
               showAlert("Fehler beim Laden!");
               return;
            }

         } else {
            // Cancelled!
         }
         // nicht Laden, also Erstellen!
      } else {
         try {
            createRiddle();
         } catch (Exception e1) {
            showAlert("Fehler beim Erstellen.");
         }
      }

      // String eingabe =
      // JOptionPane.showInputDialog(null,"Geben Sie Ihren Namen ein",
      // "Eine Eingabeaufforderung",
      // JOptionPane.PLAIN_MESSAGE);
   }

   /**
    * oeffnet Dialoge und ruft Methoden auf, um aus einem Foto ein Raetsel zu
    * erstellen.
    */
   private void createRiddle() {
      File file = getFileOrDirectryFromChooser(applikation, JFileChooser.OPEN_DIALOG, false);
      if (file != null && file.getAbsoluteFile() != null) {
         // Abfrage der Groesse
         String height = JOptionPane.showInputDialog(null, "Hoehe:", "Eine Eingabeaufforderung", JOptionPane.PLAIN_MESSAGE);
         String width = JOptionPane.showInputDialog(null, "Breite", "Eine Eingabeaufforderung", JOptionPane.PLAIN_MESSAGE);
         String numberOfColors = JOptionPane.showInputDialog(null, "Anzahl der Farben (inklusive Hintergrundfarbe)", "Eine Eingabeaufforderung", JOptionPane.PLAIN_MESSAGE);

         // Laden, Verkleinern und Farben herrunterrechnen
         BufferedImage image = ps.loadAndDownColorPicture(file.getAbsoluteFile().toString(), Integer.valueOf(height), Integer.valueOf(width), Integer.valueOf(numberOfColors));

         // Das Raetsel aufbauen
         playGame.createRiddle(image);
      }
   }

   /**
    * Erstellt einen Filechooser-Dialog und gibt das ausgewaehlte File zurueck.
    * 
    * @param parent
    * @param type
    * @param forPicture
    *           Bild oder nono laden.
    * @return gewaehlte Datei
    */
   private File getFileOrDirectryFromChooser(Component parent, int type, boolean forPicture) {
      JFileChooser chooser = null;
      if (forPicture) {
         if (lastSelectedDirForPicture != null) {
            chooser = new JFileChooser(lastSelectedDirForPicture);
         } else {
            chooser = new JFileChooser();
         }
      } else {
         if (lastSelectedDirForLoad != null) {
            chooser = new JFileChooser(lastSelectedDirForLoad);
         } else {
            chooser = new JFileChooser();
         }
      }
      chooser.setFileSelectionMode(type);
      chooser.addChoosableFileFilter(new FileFilter() {

         @Override
         public String getDescription() {
            return null;
         }

         @Override
         public boolean accept(File file) {
            String filename = file.getName();
            return (filename.endsWith(".jgp") || filename.endsWith(".JPG") || filename.endsWith(".gif") || filename.endsWith(".GIF") || filename.endsWith(".png") || filename.endsWith(".PNG") || file
                  .isDirectory());
         }
      });
      int ret = chooser.showOpenDialog(parent);
      if (ret == JFileChooser.APPROVE_OPTION) {
         File selected = chooser.getSelectedFile();
         if (selected.isDirectory()) {
            if (forPicture) {
               lastSelectedDirForPicture = selected;
            } else {
               lastSelectedDirForLoad = selected;
            }
         } else {
            if (forPicture) {
               lastSelectedDirForPicture = selected.getParentFile();
            } else {
               lastSelectedDirForLoad = selected.getParentFile();
            }
         }
         return selected;
      }
      return null;
   }

   @Override
   public void placeAField(int row, int column, Colour colour, boolean enabled) {
      if (null != colour) {
         labels[row][column].setForeground(new Color(colour.getRed(), colour.getGreen(), colour.getBlue()));
         labels[row][column].setBackground(new Color(colour.getRed(), colour.getGreen(), colour.getBlue()));
         if (!enabled) {
            labels[row][column].removeMouseListener(playGame);
         }
      } else {
         labels[row][column].setForeground(Color.LIGHT_GRAY);
         labels[row][column].setBackground(Color.LIGHT_GRAY);
         if (!enabled) {
            labels[row][column].removeMouseListener(playGame);
         }
      }
   }

   @Override
   public void setColours(List<Colour> colours) {
      if (toolbar != null) {
         applikation.remove(toolbar);
         applikation.validate();
         applikation.repaint();
      }
      toolbar = new JToolBar();
      applikation.add(toolbar, BorderLayout.SOUTH);
      toolbar.setName("Farben");
      toolbar.setBackground(Color.white);
      JButton backgroundButton = new JButton("-");
      backgroundButton.setForeground(Color.DARK_GRAY);
      backgroundButton.addActionListener(playGame);
      backgroundButton.setToolTipText("Leerfeld setzen wird ausgewählt.");
      toolbar.add(backgroundButton);
      JButton resetButton = new JButton("Reset");
      resetButton.setForeground(Color.DARK_GRAY);
      resetButton.addActionListener(playGame);
      resetButton.setToolTipText("Zurücksetzen eines Feldes wird ausgewählt.");
      toolbar.add(resetButton);
      for (Colour colour : colours) {
         Color color = new Color(colour.getRed(), colour.getGreen(), colour.getBlue());
         JButton comp = new JButton(String.valueOf(colour.getName()));
         comp.setForeground(color);
         comp.addActionListener(playGame);
         comp.setToolTipText("Diese Farbe auswählen, um ein Feld zu setzen.");
         toolbar.add(comp);
      }
   }

   @Override
   public void wasRight(boolean isRight, String message) {
      if (isRight) {
         showAlert("Richtig gelöst!");
      } else {
         showAlert("Nicht komplett gelöst!\n" + message);
      }

   }

   @Override
   public void showAlert(String string) {
      JOptionPane.showMessageDialog(applikation, string, string, JOptionPane.WARNING_MESSAGE);
   }

   @Override
   public File getSaveFile() {
      JFileChooser chooser = new JFileChooser();
      int retrival = chooser.showSaveDialog(this);
      if (retrival == JFileChooser.APPROVE_OPTION) {
         return chooser.getSelectedFile();
      }
      return null;
   }
}
