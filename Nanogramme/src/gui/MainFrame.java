package gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import models.IPlayGame;
import models.PlayGame;

public class MainFrame extends JFrame implements ActionListener, IPlayGame {
   
   /**
    * 
    */
   private static final long serialVersionUID = -8026416994513756565L;

   MyMenuBar menuBar = new MyMenuBar();
   public JFrame applikation;
   public Container container;
   public JMenuBar menueLeiste;
   
   private PlayGame playGame;

   public MainFrame() {
      playGame = new PlayGame(this);
      init();
   }

   private void init() {

      applikation = new JFrame("Main");
      container = applikation.getContentPane();

      // Menüleiste erzeugen
      menueLeiste = new MyMenuBar(this);

      applikation.add(menueLeiste, BorderLayout.NORTH);
      // applikation.add(new JScrollPane(textarea), BorderLayout.CENTER);

      applikation.setSize(400, 300);
      applikation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      JPanel panel = new JPanel(new GridLayout(7, 7, -1, -1));
      panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

      for (int i = 0; i < (7 * 7); i++) {
         Button button = new Button(""+i);
         button.addActionListener(playGame);
         panel.add(button);
      }

      container.add(panel, BorderLayout.CENTER);
     
      
      JPanel panelLeft = new JPanel(new GridLayout(7, 1));
      for (int i = 0; i < 7; i++) {
         panelLeft.add(new JLabel("A"));
      }
      applikation.add(panelLeft, BorderLayout.WEST);
      
      JPanel panelTop = new JPanel(new GridLayout(1, 8));
      for (int i = 0; i < 7; i++) {
         panelTop.add(new JLabel("A"));
      }
      applikation.add(panelTop, BorderLayout.NORTH);
      
      applikation.setVisible(true);
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      System.out.println("Action");
      System.out.println(e.getActionCommand());
      File file = getFileOrDirectryFromChooser(applikation, JFileChooser.OPEN_DIALOG);
      System.out.println(file.getAbsoluteFile());
      try {
         playGame.openFile(file.getAbsoluteFile().toString());
      } catch (IOException e1) {
         // TODO Auto-generated catch block
         e1.printStackTrace();
      }
      
   }
   
   private static File lastSelectedDir = null;
   
   public File getFileOrDirectryFromChooser(Component parent, int type) {
           JFileChooser chooser = null;
           if (lastSelectedDir != null)
               chooser = new JFileChooser(lastSelectedDir);
           else
               chooser = new JFileChooser();       
           chooser.setFileSelectionMode(type);
           int ret = chooser.showSaveDialog(parent);
           if(ret == JFileChooser.APPROVE_OPTION){
               File selected = chooser.getSelectedFile();
               if(selected.isDirectory())
                   lastSelectedDir = selected;
               else
                   lastSelectedDir = selected.getParentFile();
               return selected;
           }
           return null;
       }

   @Override
   public boolean placeAField(int row, int column) {
      // TODO Auto-generated method stub
      return false;
   }

}
