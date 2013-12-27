package gui;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import interfaces.IPlaygame;

/**
 * Menu für den Nonosolver
 * 
 * @author csgt
 * 
 */
public class MyMenuBar extends JMenuBar {

   ActionListener mainFrame;

   /**
    * 
    */
   private static final long serialVersionUID = -5971290065582479552L;
   /**
    * Menueintrag
    */
   public JMenu datei;
   /**
    * Menueintrag
    */
   public JMenu riddle;
   /**
    * Menueintrag
    */
   public JMenuItem oeffnen;
   /**
    * Menueintrag
    */
   public JMenuItem laden;
   /**
    * Menueintrag
    */
   public JMenuItem erstellen;
   /**
    * Menueintrag
    */
   public JMenuItem check;
   /**
    * Menueintrag
    */
   private IPlaygame playGame;

   /**
    * Konstruktor
    */
   public MyMenuBar() {

      init();
   }

   /**
    * Kontruktor
    * 
    * @param mainFrame
    * @param playGame
    */
   public MyMenuBar(ActionListener mainFrame, IPlaygame playGame) {
      this.mainFrame = mainFrame;
      this.playGame = playGame;
      init();
      laden.addActionListener(mainFrame);
      erstellen.addActionListener(mainFrame);
   }

   /**
    * Initialisieren des Menus
    */
   private void init() {
      // Menüelemente erzeugen
      datei = new JMenu("Datei");
      // hilfe = new JMenu("Hilfe");
      riddle = new JMenu("Rätsel");

      // Untermenüelemente erzeugen
      oeffnen = new JMenuItem("Speichern");
      oeffnen.addActionListener(playGame);
      // faq = new JMenuItem("F.A.Q.");
      // about = new JMenuItem("Über");
      laden = new JMenuItem("Rätsel laden");
      erstellen = new JMenuItem("Rätsel erstellen");
      check = new JMenuItem("check");
      check.addActionListener(playGame);

      // Menüelemente hinzufügen
      this.add(datei);
      this.add(riddle);
      // this.add(hilfe);

      // Untermenüelemente hinzufügen
      datei.add(oeffnen);
      // hilfe.add(faq);
      riddle.add(laden);
      riddle.add(erstellen);
      riddle.add(check);
      // hilfe.add(about);
   }

}