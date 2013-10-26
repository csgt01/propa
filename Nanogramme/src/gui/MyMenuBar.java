package gui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

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
   public JMenuItem faq;
   public JMenuItem about;

   public MyMenuBar() {

      init();
   }

   public MyMenuBar(MainFrame mainFrame) {
      this.mainFrame = mainFrame;
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
      faq = new JMenuItem("F.A.Q.");
      about = new JMenuItem("Über");
      laden = new JMenuItem("Rätsel laden");

      // Menüelemente hinzufügen
      this.add(datei);
      this.add(riddle);
      this.add(hilfe);

      // Untermenüelemente hinzufügen
      datei.add(oeffnen);
      hilfe.add(faq);
      riddle.add(laden);
      hilfe.add(about);
   }

}