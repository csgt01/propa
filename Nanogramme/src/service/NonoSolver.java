package service;

import java.io.IOException;
import java.util.List;

import de.feu.propra.nonogramme.interfaces.INonogramSolver;

public class NonoSolver implements INonogramSolver {
    
    private RiddleLoader riddleLoader;
    
    public NonoSolver() {
        
    }

    @Override
    public String getEmail() {
        return "csgt01@gmail.com";
    }

    @Override
    public String getMatrNr() {
        return "8352437";
    }

    @Override
    public String getName() {
        return "Christian Schulte genannt Trux";
    }

    @Override
    public char[][] getSolution() {
        return new char[1][2];
    }

    @Override
    public void openFile(String arg0) throws IOException {

        System.out.println("openFile:" + arg0);
        riddleLoader = new RiddleLoader();
        List<String> lines = riddleLoader.readFile(arg0);
        
    }

}
