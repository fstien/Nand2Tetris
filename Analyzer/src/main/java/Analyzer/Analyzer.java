/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package Analyzer;

import java.io.FileNotFoundException;

public class Analyzer {
    public static void main(String[] args) throws FileNotFoundException {
        // arg[1] is either a filename or a directory

        // if arg[1] is a filename
        System.out.println(args[0]);

        Tokenizer tk = new Tokenizer(args[0] + ".jack");

        // foreach file
            // create tokenizer
            // creates a compilationEngine and pass it the tokenizer instance to write to an xml file

    }
}