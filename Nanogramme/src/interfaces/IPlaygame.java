package interfaces;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import models.Riddle;

/**
 * Interface stellt Methoden zum Öffnen oder Erstellen eines Rätsels bereit.
 * 
 * @author csgt
 * 
 */
public interface IPlaygame extends ActionListener, MouseListener {

   /**
    * Lädt ein Rätsel aus einer Datei heraus.
    * 
    * @param filename
    * @return true falls die Datei korrekt geöffnet wurde.
    */
   boolean openRiddleFromFile(String filename);

   /**
    * Holt die Lösung des Rätseln vom Nonosolver, informiert den Listener über
    * das Rätsel bzw. lässt beim Listener eine Fehlermeldung ausgeben
    * 
    * @param riddle
    */
   void setupIt(Riddle riddle);

   /**
    * Erstellt ein Rätsel aus einem Foto.
    * 
    * @param image
    * @return das Rätsel
    */
   Riddle createRiddle(BufferedImage image);

}
