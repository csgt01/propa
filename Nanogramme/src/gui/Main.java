package gui;

import service.NonoSolver;
import de.feu.propra.nonogramme.interfaces.INonogramSolver;
import de.feu.propra.nonogramme.tester.Tester;

public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length > 0 && "-t".equals(args[0])) {
            INonogramSolver solver = new NonoSolver();
            Tester tester = new Tester(args, solver);
            System.out.println(tester.test());
        } else {
            // Starten der Benutzeroberflaeche
        }

    }

}