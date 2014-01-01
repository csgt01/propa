/**
 * 
 */
package models;

/**
 * Dieses Enum zeigt den Zustand beim Lösen des Rätsels an.
 * 
 * @author cschulte
 * 
 */
public enum SolveStateEnum {

   /**
    * Suchend
    */
   SOLVING("Such Lösung"),
   /**
    * Gelöst
    */
   SOLVED("Gelöst"),
   /**
    * Mehrere Lösungen vorhanden
    */
   MULTIPLE_SOLUTIONS("Mehrere Lösungen gefunden.\nEs wird eine Lösung ausgewählt und einige Felder vorausgefüllt, die dann nicht mehr klickbar sind."),
   /**
    * Keine Lösung vorhanden
    */
   NO_SOLUTION("Keine Lösung gefunden"),
   /**
    * Fehler
    */
   ERROR("Error"),
   /**
    * Lösung gefunden, aber mit Stack gefüllt
    */
   FOUND_SOLUTION_WITH_STACK("Found Solution with Stack"),
   /**
    * Keine logischen Fortschritte also raten!
    */
   MUST_GUESS("Raten");

   /**
    * Nachricht, die bei dem jeweiligen Zusatnd angezeigt weden soll.
    */
   private final String message;

   /**
    * Konstruktor
    * 
    * @param message
    */
   private SolveStateEnum(String message) {
      this.message = message;
   }

   /**
    * Gibt die Nachricht für das Enum zurück.
    * 
    * @return Nachricht
    */
   public String getMessage() {
      return message;
   }

}
