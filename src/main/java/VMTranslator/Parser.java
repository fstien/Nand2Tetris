package VMTranslator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Math.*;

public class Parser {

    private List<String> commands = new ArrayList<>();
    private List<String> classNames = new ArrayList<>();

    private int commandIndex = 0;

    private int commandLength;
    public String commandType;
    public String arg1;
    public Integer arg2;
    public String className;


    public Parser(String addrName) {
        try {
            String baseDir = "/Users/francois.stiennon/Desktop/nand2tetris/projects/VMTranslator/src/main/java/VMTranslator/bytecode/";

            String path = baseDir + addrName;

            File f = new File(path + ".vm");
            File d = new File(path);

            if(f.isFile()) {
                Scanner file = new Scanner(f);
                this.PopulateList(file, f.getName());
                file.close();
            }
            else if(d.isDirectory()) {
                File[] listOfFiles = d.listFiles((dir, name) -> name.toLowerCase().endsWith(".vm"));

                for (File vmF : listOfFiles) {
                    System.out.println("File " + vmF.getName());
                    Scanner file = new Scanner(vmF);
                    this.PopulateList(file, vmF.getName());
                    file.close();
                }
            }
            else {
                System.out.println("Code not found");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean lineIncludesCommand(String line) {
        if (line.length() > 0) {
            if (line.contains("//")) {
                if(line.equals("//")) return false;
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

    private void PopulateList(Scanner file, String fileName)
    {
        String currentLine;
        while (file.hasNextLine()) {
            currentLine = file.nextLine();
            if (this.lineIncludesCommand(currentLine)) {
                commands.add(cleanCommand(currentLine));
                classNames.add(fileName.substring(0, fileName.lastIndexOf('.')));
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
        this.className = this.classNames.get(commandIndex);

        String command = this.commands.get(commandIndex);

        String[] cSplit = command.split(" ");
        this.commandLength = cSplit.length;

        if (this.commandLength == 1) {
            if (cSplit[0].equals("return")) {
                this.commandType = "C_RETURN";
                this.arg1 = cSplit[0];
                this.arg2 = null;
            } else {
                this.commandType = "C_ARITHMETIC";
                this.arg1 = cSplit[0];
                this.arg2 = null;
            }
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
                case "function":
                    this.commandType = "C_FUNCTION";
                    this.arg1 = cSplit[1];
                    this.arg2 = Integer.parseInt(cSplit[2]);
                    break;
                case "call":
                    this.commandType = "C_CALL";
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
