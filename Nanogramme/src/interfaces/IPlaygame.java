package interfaces;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import models.Riddle;

/**
 * Interface stellt Methoden zum oeffnen oder Erstellen eines Raetsels bereit.
 * 
 * @author csgt
 * 
 */
public interface IPlaygame extends ActionListener, MouseListener {

   /**
    * Laedt ein Raetsel aus einer Datei heraus.
    * 
    * @param filename
    * @return true falls die Datei korrekt geoeffnet wurde.
    */
   boolean openRiddleFromFile(String filename);

   /**
    * Holt die Loesung des Raetseln vom Nonosolver, informiert den Listener ueber
    * das Raetsel bzw. laesst beim Listener eine Fehlermeldung ausgeben
    * 
    * @param riddle
    *           das Raetsle
    * @param matrix
    *           die erstellte Matrix des Raetsels
    */
   void setupIt(Riddle riddle, char[][] matrix);

   /**
    * Erstellt ein Raetsel aus einem Foto.
    * 
    * @param image
    * @return das Raetsel
    */
   Riddle createRiddle(BufferedImage image);

}
