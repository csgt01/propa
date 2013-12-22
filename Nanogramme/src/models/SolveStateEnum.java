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

	SOLVING("Such Lösung"), 
	SOLVED("Gelöst"),
	MULTIPLE_SOLUTIONS("Mehrere Lösungen gefunden"),
	NO_SOLUTION("Keine Lösung gefunden"),
	ERROR("Error"),
	FOUND_SOLUTION_WITH_STACK("Found Solution with Stack"),
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

	public String getMessage() {
		return message;
	}

}
