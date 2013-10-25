package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MainFrame extends JFrame {

   /**
    * 
    */
   private static final long serialVersionUID = -8026416994513756565L;
   
   
   JFrame applikation;
   Container container;
   
   // Menüleiste
   JMenuBar menueLeiste;
   
   // Menüleiste Elemente
   JMenu datei;
   JMenu hilfe;
   
   // Datei
   JMenuItem oeffnen;
   JMenuItem beenden;
   
   // Hilfe
   JMenuItem faq;
   JMenuItem about;
   
   public MainFrame() {
      init();
   }

   private void init() {
      
      applikation = new JFrame("JMenuBar Beispiel");
      container = applikation.getContentPane();
      
      // Menüleiste erzeugen
      menueLeiste = new JMenuBar();
      
      // Menüelemente erzeugen
      datei = new JMenu("Datei");
      hilfe = new JMenu("Hilfe");
      
      // Untermenüelemente erzeugen
      oeffnen = new JMenuItem("öffnen");
      beenden = new JMenuItem("beenden");
      faq = new JMenuItem("F.A.Q.");
      about = new JMenuItem("Über");
      
      // Menüelemente hinzufügen
      menueLeiste.add(datei);
      menueLeiste.add(hilfe);
      
      // Untermenüelemente hinzufügen
      datei.add(oeffnen);
      datei.add(beenden);
      hilfe.add(faq);
      hilfe.add(about);

      
      applikation.add(menueLeiste, BorderLayout.NORTH);
//      applikation.add(new JScrollPane(textarea), BorderLayout.CENTER);

      applikation.setSize(400, 300);
      applikation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     
      
      JPanel panel = new JPanel(new GridLayout(7,7, -1, -1));
      panel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));

      for (int i =0; i<(7*7); i++){
          final JLabel label = new JLabel();
          label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
          panel.add(label);
      }
      
      container.add(panel);
      applikation.setVisible(true);
      
      
   }

}
