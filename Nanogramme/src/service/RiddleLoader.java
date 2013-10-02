package service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import models.Colour;
import models.Riddle;

public class RiddleLoader {

    private Riddle riddle = new Riddle();

    private int state = 0;

    public List<String> readFile(String filename) {
        Scanner scanner = null;
        ArrayList<String> lines = new ArrayList<String>();
        try {
            scanner = new Scanner(new File(filename));
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (null != scanner) {
                scanner.close();
            }
        }
        for (String str : lines) {
            if (str.length() > 0) {
                analyzeLine(str);
            }
        }
        System.out.println(riddle);
        return lines;
    }

    public void analyzeLine(String str) {
        // TODO: regex wgitespace and tab
        switch (state) {
        case 0:
            if (str.startsWith("width")) {
                String splitted = str.split("width ")[1];
                riddle.setWidth(Integer.valueOf(splitted));
            } else if (str.startsWith("height")) {
                riddle.setHeight(Integer.valueOf(str.split("height ")[1]));
            } else if (str.startsWith("color")) {
                state = 1;
            }
            break;
        case 1:
            if (str.startsWith("rows")) {
                state = 2;
            } else {
                String[] splits = str.split(" ");
                Colour colour = new Colour();
                colour.setName(splits[0]);
                String rgbsString = splits[1];
                String[] rgbs = rgbsString.split(",");
                colour.setRed(Integer.valueOf(rgbs[0]));
                colour.setGreen(Integer.valueOf(rgbs[1]));
                colour.setBlue(Integer.valueOf(rgbs[2]));
                riddle.addColour(colour);
            }
            break;
        case 2:
            if (str.startsWith("column")) {
                state = 2;
            } else {
               
            }
            break;
        default:
            break;
        }
    }

}
