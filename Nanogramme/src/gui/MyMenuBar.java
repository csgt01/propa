package gui;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import interfaces.IPlaygame;

/**
 * Menu fuer den Nonosolver
 * 
 * @author csgt
 * 
 */
public class MyMenuBar extends JMenuBar {

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
   public JMenuItem show;
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
      this.playGame = playGame;
      init();
      laden.addActionListener(mainFrame);
      erstellen.addActionListener(mainFrame);
   }

   /**
    * Initialisieren des Menus
    */
   private void init() {
      // Menueelemente erzeugen
      datei = new JMenu("Datei");
      // hilfe = new JMenu("Hilfe");
      riddle = new JMenu("Rätsel");

      // Untermenueelemente erzeugen
      oeffnen = new JMenuItem("Speichern");
      oeffnen.addActionListener(playGame);
      laden = new JMenuItem("Rätsel laden");
      erstellen = new JMenuItem("Raetsel erstellen");
      check = new JMenuItem("Rätsel prüfen");
      check.addActionListener(playGame);
      show = new JMenuItem("Rätsel lösen lassen");
      show.addActionListener(playGame);

      // Menueelemente hinzufuegen
      this.add(datei);
      this.add(riddle);

      // Untermenueelemente hinzufuegen
      datei.add(oeffnen);
      riddle.add(laden);
      riddle.add(erstellen);
      riddle.add(check);
      riddle.add(show);
   }

}