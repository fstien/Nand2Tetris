package VMTranslator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CodeWriter {

    private FileWriter FW;

    private int jumpCounter = 0;
    private Map<String, Integer> Pointers = new HashMap<>();

    public CodeWriter(String fileName) {
        String baseDir = "/Users/francois.stiennon/Desktop/nand2tetris/projects/VMTranslator/src/main/java/VMTranslator/assembly/";

        String extensionRemoved = fileName.split("\\.")[0];

        try {
            FW = new FileWriter(baseDir + extensionRemoved + ".asm");
            this.writeInit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeInit() {
        // Write the bootstrap code
        this.write("// INITIALISE");
        this.write("@0");


        Pointers.put("SP", 0);
        Pointers.put("local", 1);
        Pointers.put("argument", 2);
        Pointers.put("pointer", 3);
        Pointers.put("this", 3);
        Pointers.put("that", 4);
    }

    private String repVariables(String line) {
        for (Map.Entry<String, Integer> segment : Pointers.entrySet()) {
            line = line.replace(segment.getKey(), Integer.toString(segment.getValue()));
        }
        return line;
    }

    private void appendToFile(String line) {
        try {
            FW.write(line + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(String stringToWrite) {
        stringToWrite = this.repVariables(stringToWrite);
        this.appendToFile(stringToWrite);
    }

    private void writeComment(String comment) {
        this.appendToFile(comment);
    }

    private void PCMin2Min1InD() {
        this.write("@SP");
        this.write("D=M");
        this.write("D=D-1");
        this.write("A=D");
        this.write("D=M");
        this.write("A=A-1");
    }

    private void PcDecrement() {
        this.write("@SP");
        this.write("M=M-1");
    }

    private void CompMain(String Jump) {
        this.PCMin2Min1InD();
        this.write("M=M-D");
        this.write("D=M");
        this.write("M=0");
        this.write("@DI" + jumpCounter);
        this.write("D;" + Jump);
        this.write("@SP");
        this.write("D=M");
        this.write("D=D-1");
        this.write("D=D-1");
        this.write("A=D");
        this.write("M=M-1");
        this.write("(DI" + jumpCounter + ")");
        this.PcDecrement();
    }

    private void NegNotSetup() {
        this.write("@SP");
        this.write("D=M");
        this.write("D=D-1");
        this.write("A=D");
    }

    public void writeArithmetic(String arg1) {
        this.write("// " + arg1);

        switch (arg1) {
            case "add":
                this.PCMin2Min1InD();
                this.write("M=D+M");
                this.PcDecrement();
                break;

            case "sub":
                this.PCMin2Min1InD();
                this.write("M=M-D");
                this.PcDecrement();
                break;

            case "neg":
                this.NegNotSetup();
                this.write("M=-M");
                break;

            case "not":
                this.NegNotSetup();
                this.write("M=!M");
                break;


            case "and":
                this.PCMin2Min1InD();
                this.write("M=D&M");
                this.PcDecrement();
                break;

            case "or":
                this.PCMin2Min1InD();
                this.write("M=D|M");
                this.PcDecrement();
                break;

            case "eq":
                this.CompMain("JNE");
                break;

            case "lt":
                this.CompMain("JGE");
                break;

            case "gt":
                this.CompMain("JLE");
                break;

            default:
                System.out.println("writeArithmetic: arg1 not found.");
        }

        this.jumpCounter++;
    }

    public void writePushPop(String commandType, String arg1, int arg2) {

        if(commandType.equals("C_PUSH")) {
            this.writeComment("// push " + arg1 + " " + arg2);

            if(arg1.equals("constant")) {
                this.write("@" + arg2);

                this.write("D=A");
            }
            else {
                this.write("@" + Pointers.get(arg1));
                if(arg1.equals("pointer")) {
                    this.write("D=A");
                }
                else {
                    this.write("D=M");
                }
                this.write("@" + arg2);
                this.write("D=D+A");
                this.write("A=D");

                this.write("D=M");
            }

            this.write("@SP");
            this.write("A=M");
            this.write("M=D");
            this.write("@SP");
            this.write("M=M+1");
        }
        else if(commandType.equals("C_POP")) {
            this.writeComment("// pop " + arg1 + " " + arg2);

            // Pop the value from the stack
            this.write("@SP");
            this.write("M=M-1");
            this.write("A=M");
            this.write("D=M");

            // store it in R14
            this.write("@R14");
            this.write("M=D");

            // Get the address in which to pop
            this.write("@" + Pointers.get(arg1));
            if(arg1.equals("pointer")) {
                this.write("D=A");
            }
            else {
                this.write("D=M");
            }
            this.write("@" + arg2);
            this.write("D=D+A");

            // Store it in R15
            this.write("@R15");
            this.write("M=D");

            // Move the content of R14 into the address pointed by R15
            this.write("@R14");
            this.write("D=M");
            this.write("@R15");
            this.write("A=M");
            this.write("M=D");
        }
        else {
            System.out.println("commandType not found.");
        }
    }

    public void writeLabel(String label) {
        this.writeComment("// label " + label);
        this.write("(" + label + ")");
    }

    public void writeGoto(String label) {
        this.writeComment("// goto " + label);
        this.write("@" + label);
        this.write("0;JMP");
    }

    public void writeIf(String label) {
        this.writeComment("// if-goto " + label);
        this.write("@SP");
        this.write("M=M-1");
        this.write("A=M");
        this.write("D=M");
        this.write("@" + label);
        this.write("D;JGT");
    }

    public void writeFunction(String functionName, int numVars) {
        this.writeComment("// function " + functionName + " " + numVars);
    }

    public void writeCall(String functionName, int numArgs) {
        this.writeComment("// call " + functionName + " " + numArgs);
    }

    public void writeReturn() {
        this.writeComment("// return");
    }

    private void writeInfLoop() {
        this.write("(END)");
        this.write("@END");
        this.write("0;JMP");
    }

    public void closeFile() {
        try {
            this.writeInfLoop();
            this.FW.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
