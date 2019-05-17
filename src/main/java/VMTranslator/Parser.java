package VMTranslator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {

    private List<String> commands = new ArrayList<>();

    private int commandIndex = 0;

    private int commandLength;
    public String commandType;
    public String arg1;
    public Integer arg2;

    public Parser(String fileName) {
        try {
            String baseDir = "/Users/francois.stiennon/Desktop/nand2tetris/projects/VMTranslator/src/main/java/VMTranslator/bytecode/";
            Scanner file = new Scanner(new File(baseDir + fileName));
            this.PopulateList(file);
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean lineIncludesCommand(String line) {
        if (line.length() > 0) {
            if (line.contains("//")) {
                String[] split = line.split("//");
                return split[0].length() > 0;
            }
            else return true;
        }
        return false;
    }

    private String replace(String str) {
        return str.replace("\t", "");
    }

    private String cleanCommand(String line) {
        String[] commentSplit = line.split("//");
        String noComment = commentSplit[0];

        String[] eSplit = noComment.split(" ");
        String retString = eSplit[0] + " ";

        for(int i = 1; i < eSplit.length; i++) {
            retString = retString + replace(eSplit[i]) + " ";
        }

        String substring = retString.substring(0, retString.length() - 1);
        return substring;
    }

    private void PopulateList(Scanner file)
    {
        String currentLine;
        while (file.hasNextLine()) {
            currentLine = file.nextLine();
            if (this.lineIncludesCommand(currentLine)) {
                commands.add(cleanCommand(currentLine));
            }
        }

        if(commands.size() > 0) {
            this.parseCommand();
        }
    }

    public void advance() {
        if(this.hasMoreCommands()) {
            this.commandIndex++;
            this.parseCommand();
        }
        else {
            System.out.println("File has run out of commands.");
        }
    }

    public boolean hasMoreCommands() {
        return this.commandIndex + 1 < this.commands.size();
    }

    private void parseCommand() {
        String command = this.commands.get(commandIndex);

        String[] cSplit = command.split(" ");
        this.commandLength = cSplit.length;

        if (this.commandLength == 1) {
            this.commandType = "C_ARITHMETIC";
            this.arg1 = cSplit[0];
            this.arg2 = null;
        }
        else if(this.commandLength == 2) {
            switch (cSplit[0]) {
                case "label":
                    this.commandType = "C_LABEL";
                    break;
                case "goto":
                    this.commandType = "C_GOTO";
                    break;
                case "if-goto":
                    this.commandType = "C_IFGOTO";
                    break;
                default:
                    System.out.println("CommandType not found");
            }
            this.arg1 = cSplit[1];
            this.arg2 = null;
        }
        else if(this.commandLength == 3) {
            switch (cSplit[0]) {
                case "push":
                    this.commandType = "C_PUSH";
                    this.arg1 = cSplit[1];
                    this.arg2 = Integer.parseInt(cSplit[2]);
                    break;
                case "pop":
                    this.commandType = "C_POP";
                    this.arg1 = cSplit[1];
                    this.arg2 = Integer.parseInt(cSplit[2]);
                    break;
                default:
                    System.out.println("Command not found.");
            }
        }
        else {
            System.out.println("Invalid command line.");
        }
    }

    private String currentCommandString() {
        if (this.commandLength == 3) {
            return this.commandType + " " + this.arg1 + " " + this.arg2.toString();
        }
        else if(this.commandLength == 2) {
            return this.commandType + " " + this.arg1;
        }
        else if(this.commandLength == 1) {
            return this.commandType + " " + this.arg1;
        }
        else {
            System.out.println("Error in generating string.");
            return "";
        }
    }

    public String toString() {

        int index = this.commandIndex;

        this.commandIndex = 0;

        StringBuilder allCommands = new StringBuilder(this.currentCommandString()).append("\n");

        while(this.hasMoreCommands()) {
            this.advance();
            allCommands.append(this.currentCommandString()).append("\n");
        }

        this.commandIndex = index;
        this.parseCommand();

        return allCommands.toString();
    }
}
