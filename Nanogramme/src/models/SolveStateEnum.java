/**
 * 
 */
package models;

/**
 * Dieses Enum zeigt den Zustand beim Loesen des Raetsels an.
 * 
 * @author cschulte
 * 
 */
public enum SolveStateEnum {

   /**
    * Suchend
    */
   SOLVING("Such Loesung"),
   /**
    * Geloest
    */
   SOLVED("Geloest"),
   /**
    * Mehrere Loesungen vorhanden
    */
   MULTIPLE_SOLUTIONS("Mehrere Loesungen gefunden.\nEs wird eine Loesung ausgewaehlt und einige Felder vorausgefuellt, die dann nicht mehr klickbar sind."),
   /**
    * Keine Loesung vorhanden
    */
   NO_SOLUTION("Keine Loesung gefunden"),
   /**
    * Fehler
    */
   ERROR("Error"),
   /**
    * Loesung gefunden, aber mit Stack gefuellt
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
    * Gibt die Nachricht fuer das Enum zurueck.
    * 
    * @return Nachricht
    */
   public String getMessage() {
      return message;
   }

}
