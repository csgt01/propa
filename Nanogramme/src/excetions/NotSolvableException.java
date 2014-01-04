package excetions;

/**
 * Exception wird geworfen, wenn zu einem Zeitpunkt das Raetsel nicht mehr losbar
 * ist.
 * 
 * @author csgt
 * 
 */
public class NotSolvableException extends Exception {

   /**
    * Konstruktor.
    * 
    * @param string
    */
   public NotSolvableException(String string) {
      super(string);
   }

   /**
    * 
    */
   private static final long serialVersionUID = 3602531136754293725L;

}
