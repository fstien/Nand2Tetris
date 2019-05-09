package VMTranslator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser {

    private String baseDir = "/Users/francois.stiennon/Desktop/nand2tetris/projects/VMTranslator/src/main/java/VMTranslator/";
    private Scanner file;

    private String currentLine;

    public String commandType;
    public String arg1;
    public int arg2;

    public Parser(String fileName) {
        try {
            file = new Scanner(new File(baseDir + fileName));
            if (this.hasMoreCommands()) {
                currentLine = file.nextLine();
                this.parseCommand();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void advance() {
        if(this.hasMoreCommands()) {
            currentLine = file.nextLine();
            this.parseCommand();
        }
        else {
            System.out.println("File has run out of commands.");
        }
    }

    public boolean hasMoreCommands() {
        return file.hasNextLine();
    }

    private void parseCommand() {


    }


    public void closeFile () {
        file.close();
    }
}
