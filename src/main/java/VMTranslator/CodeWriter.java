package VMTranslator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CodeWriter {

    private FileWriter FW;

    private int jumpCounter = 0;
    private Map<String, Integer> Pointers = new HashMap<>();
    private Map<String, Integer> Segments = new HashMap<>();
    private int lastPush;

    public CodeWriter(String fileName) {
        String baseDir = "/Users/francois.stiennon/Desktop/nand2tetris/projects/VMTranslator/src/main/java/VMTranslator/assembly/";

        String extensionRemoved = fileName.split("\\.")[0];

        try {
            FW = new FileWriter(baseDir + extensionRemoved + ".asm");
            this.initialise();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeInit() {
        // Write the bootstrap code
    }

    private void setPointer(String segment, int pointer, int address) {
        Pointers.put(segment, pointer);
        Segments.put(segment, address);

        this.write("@" + address);
        this.write("D=A");
        this.write("@" + pointer);
        this.write("M=D");
    }

    private void initialise() {
        this.write("// INITIALISE");

        this.setPointer("SP", 0, 256);
        this.setPointer("local",1, 300);
        this.setPointer("argument",2, 400);
        this.setPointer("this",3, 3000);
        this.setPointer("that",4, 3010);

        Segments.put("constant", 0);
        Segments.put("temp", 5);
        Segments.put("pointer", 3);
        Segments.put("static", 16);

        this.write("@256");
        this.write("D=A");
        this.write("@SP");
        this.write("M=D");

        this.write("A=M");
        this.write("M=0");
        for(int i = 0; i < 20; i++) {
            this.write("A=A+1");
            this.write("M=0");
        }
        this.write("@SP");
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

        int address = Segments.get(arg1) + arg2;

        if(commandType.equals("C_PUSH")) {
            this.writeComment("// push " + arg1 + " " + arg2);

            this.write("@" + address);

            if (arg1.equals("constant")) {
                this.write("D=A");
            }
            else {
                this.write("D=M");
            }

            this.write("@SP");
            this.write("A=M");
            this.write("M=D");
            this.write("@SP");
            this.write("M=M+1");

            this.lastPush = arg2;
        }
        else if(commandType.equals("C_POP")) {
            this.writeComment("// pop " + arg1 + " " + arg2);

            this.write("@SP");
            this.write("A=M");
            this.write("A=A-1");
            this.write("D=M");
            this.write("@" + address);
            this.write("M=D");
            this.write("@SP");
            this.write("M=M-1");

            if(arg1.equals("pointer") && arg2 == 0) {
                Segments.put("this", this.lastPush);
            }

            if(arg1.equals("pointer") && arg2 == 1) {
                Segments.put("that", this.lastPush);
            }

        }
        else {
            System.out.println("commandType not found.");
        }
    }

    public void writeLabel(String label) {

    }

    public void writeGoto(String label) {

    }

    public void writeIf(String label) {

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
