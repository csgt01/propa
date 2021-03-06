package interfaces;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import models.Colour;
import models.Column;
import models.Row;

/**
 * Interface stellt Methoden fuer die Anzeige in einer GUI bereit.
 * 
 * @author cschulte
 * 
 */
public interface IUIListener {

   /**
    * Setzt die Farben als BottomBar im Listener.
    * 
    * @param colours
    *           Die zu setzenden Farben.
    */
   void setColours(List<Colour> colours);

   /**
    * Setzt ein Feld mit einer Farbe.
    * 
    * @param row
    *           Nummer der Reihe
    * @param column
    *           Nummer der Spalte
    * @param colour
    *           Die Farbe
    * @param enabled
    *           soll das Feld aktiviert werden
    */
   void placeAField(int row, int column, Colour colour, boolean enabled);

   /**
    * Informiert den Listener, ob die Loesung richtig oder falsch war.
    * 
    * @param isRight
    *           True, falls die Loesung richtig ist.
    * @param message
    *           zusaetzliche Nachricht zur Anzeige
    */
   void wasRight(boolean isRight, String message);

   /**
    * Erstellt und fuellt die UI-Matrix fuer das Raetsel.
    * 
    * @param rowInt
    *           Anzahl der Reihen.
    * @param columnInt
    *           Anzahl der Spalten.
    * @param rows
    *           Liste der Reihen.
    * @param columns
    *           Liste der Spalten.
    */
   void setupUIMatrix(int rowInt, int columnInt, List<Row> rows, List<Column> columns);

   /**
    * Zeigt eine Meldung an.
    * 
    * @param string
    */
   void showAlert(String string);

   /**
    * 
    * @param col
    * @return Gibt die Colour aus der Liste zurueck, die als Hintergrund verwandt
    *         werden soll.
    */
   Colour getBackgroundColour(LinkedList<Colour> col);

   /**
    * Liefert ein File zum speichern des Nonogramms.
    * 
    * @return ausgewaehlten File.
    */
   File getSaveFile();

}
