package excetions;

/**
 * Exception wird verwandt, wenn in einer Matrix ein Feld bereits mit einem
 * anderen Wert gesetzt ist und neu gesetzt werden soll
 * 
 * @author csgt
 * 
 */
public class DataCollisionException extends Exception {

   /**
    * Konstruktor
    * 
    * @param string
    */
   public DataCollisionException(String string) {
      super(string);
   }

   /**
    * 
    */
   private static final long serialVersionUID = -607217861789570465L;

}
