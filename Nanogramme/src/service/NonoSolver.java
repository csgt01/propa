package service;

import java.io.IOException;

import de.feu.propra.nonogramme.interfaces.INonogramSolver;

public class NonoSolver implements INonogramSolver {

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
        // TODO Auto-generated method stub

    }

}
