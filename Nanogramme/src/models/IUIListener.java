package models;

import java.util.List;

/**
 * Interface für die Kommunikation zum MainFrame.
 * 
 * @author cschulte
 * 
 */
public interface IUIListener {

	/**
	 * Setzt die Farben als BottomBar im Listener.
	 * 
	 * @param colours
	 *            Die zu setzenden Farben.
	 */
	void setColours(List<Colour> colours);

	/**
	 * Setzt ein Feld mit einer Farbe.
	 * 
	 * @param row
	 *            Nummer der Reihe
	 * @param column
	 *            Nummer der Spalte
	 * @param colour
	 *            Die Farbe
	 * @return
	 */
	boolean placeAField(int row, int column, Colour colour);

	/**
	 * Informiert den Listener, ob die Lösung richtig oder falsch war.
	 * 
	 * @param isRight
	 *            True, falls die Lösung richtig ist.
	 */
	void wasRight(boolean isRight);

	/**
	 * Erstellt und füllt die UI-Matrix für das Rätsel.
	 * 
	 * @param rowInt
	 *            Anzahl der Reihen.
	 * @param columnInt
	 *            Anzahl der Spalten.
	 * @param rows
	 *            Liste der Reihen.
	 * @param columns
	 *            Liste der Spalten.
	 */
	void setupUIMatrix(int rowInt, int columnInt, List<Row> rows,
			List<Column> columns);

	/**
	 * Zeigt eine Meldung an.
	 * 
	 * @param string
	 */
	void showAlert(String string);

}
