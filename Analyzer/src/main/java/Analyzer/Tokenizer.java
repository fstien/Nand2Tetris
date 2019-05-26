package Analyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Tokenizer {

    private String baseDir = "/Users/francois.stiennon/Desktop/nand2tetris/GitHub/Analyzer/src/main/java/Analyzer";

    public Tokenizer(String fileName) throws FileNotFoundException {

        try {
            File f = new File(baseDir + "/Jack/" + fileName);
            Scanner file = new Scanner(f);


            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean hasMoreTokens() {
        return false;
    }

    public void advance() {

    }


    /*
    public String tokenType() {

    }

    public String keyWord() {

    }

    public char symbol() {

    }

    public String identifier() {

    }

    public Integer intVal() {

    }

    public String stringVal() {

    }
    */

}
