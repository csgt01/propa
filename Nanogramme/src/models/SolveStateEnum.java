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
	ERROR("Error"),
	FOUND_SOLUTION_WITH_STACK("Found Solution with Stack"),
	MUST_GUESS("Raten");
	

	private final String message;

	private SolveStateEnum(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
