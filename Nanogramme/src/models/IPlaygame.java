package models;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public interface IPlaygame extends ActionListener, MouseListener {

	/**
	 * Lädt ein Rätsel aus einer Datei heraus.
	 * @param filename
	 * @return
	 */
	boolean openRiddleFromFile(String filename);

	/**
	 * Holt die Lösung des Rätseln vom Nonosolver, informiert den Listener über
	 * das Rätsel bzw. lässt beim Listener eine Fehlermeldung ausgeben
	 * 
	 * @param riddle
	 */
   void setupIt(Riddle riddle);
   
   Riddle createRiddle(BufferedImage image);

}
