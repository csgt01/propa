package gui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import models.PlayGame;

public class MyMenuBar extends JMenuBar {

   MainFrame mainFrame;

   /**
    * 
    */
   private static final long serialVersionUID = -5971290065582479552L;
   public JMenu datei;
   public JMenu riddle;
   public JMenu hilfe;
   public JMenuItem oeffnen;
   public JMenuItem laden;
   public JMenuItem check;
   public JMenuItem faq;
   public JMenuItem about;
   private PlayGame playGame;

   public MyMenuBar() {

      init();
   }

   public MyMenuBar(MainFrame mainFrame, PlayGame playGame) {
      this.mainFrame = mainFrame;
      this.playGame = playGame;
      init();
      laden.addActionListener(mainFrame);
   }

   /**
    * 
    */
   private void init() {
      // Menüelemente erzeugen
      datei = new JMenu("Datei");
      hilfe = new JMenu("Hilfe");
      riddle = new JMenu("Rätsel");

      // Untermenüelemente erzeugen
      oeffnen = new JMenuItem("Speichern");
      oeffnen.addActionListener(playGame);
      faq = new JMenuItem("F.A.Q.");
      about = new JMenuItem("Über");
      laden = new JMenuItem("Rätsel laden");
      check = new JMenuItem("check");
      check.addActionListener(playGame);
      
      // Menüelemente hinzufügen
      this.add(datei);
      this.add(riddle);
      this.add(hilfe);

      // Untermenüelemente hinzufügen
      datei.add(oeffnen);
      hilfe.add(faq);
      riddle.add(laden);
      riddle.add(check);
      hilfe.add(about);
   }

}