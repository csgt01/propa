/**
 * 
 */
package models;

/**
 * 
 * @author cschulte
 * 
 */
public enum SolveStateEnum {

	SOLVING("Such Lösung"), 
	SOLVED("Gelöst"),
	MULTIPLE_SOLUTIONS("Mehrere Lösungen gefunden"),
	NO_SOLUTION("Keine Lösung gefunden"),
	STATE_2("Keiunden"),
	STATE_4("Keiunden"),
	ERROR("Keine Lösung gefunden");
	

	private final String message;

	private SolveStateEnum(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
