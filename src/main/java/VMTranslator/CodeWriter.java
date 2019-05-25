package VMTranslator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class CodeWriter {

    private FileWriter FW;

    private int jumpCounter = 0;
    private Map<String, Integer> Pointers = new HashMap<>();
    private int lineNum = 0;

    private Map<String, Integer> StaticOffset = new LinkedHashMap<>();

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
        Pointers.put("SP", 0);
        Pointers.put("local", 1);
        Pointers.put("argument", 2);
        Pointers.put("pointer", 3);
        Pointers.put("this", 3);
        Pointers.put("that", 4);
        Pointers.put("temp", 5);

        // Write the bootstrap code
        this.writeComment("// INITIALISE");
        this.write("@256");
        this.write("D=A");
        this.write("@0");
        this.write("M=D");

        // call Sys.init
        this.writeCall("Sys.init", 0);
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
        this.lineNum++;
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
        this.writeLabel("DI" + jumpCounter);
        this.PcDecrement();
    }

    private void NegNotSetup() {
        this.write("@SP");
        this.write("D=M");
        this.write("D=D-1");
        this.write("A=D");
    }

    public void writeArithmetic(String arg1) {
        this.writeComment("// " + arg1);

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


    public void writePushPop(String commandType, String arg1, int arg2, String className) {

        if(commandType.equals("C_PUSH")) {
            this.writeComment("// push " + arg1 + " " + arg2);

            if(arg1.equals("constant")) {
                this.write("@" + arg2);

                this.write("D=A");
            }
            else {

                if(arg1.equals("static")) {
                    this.write("@" + className);
                    this.write("D=A");

                    Integer shiftStatics = 0;
                    Boolean stop = false;
                    for(String currentKey : this.StaticOffset.keySet()){
                        if(!currentKey.equals(className) || stop) {
                            shiftStatics += this.StaticOffset.get(currentKey) - 1;
                            stop = true;
                        }
                    }

                    if(shiftStatics > 0) {
                        this.write("@" + (shiftStatics-1));
                        this.write("D=D+A");
                    }
                }
                else {
                    this.write("@" + Pointers.get(arg1));
                    if (arg1.equals("pointer")) {
                        this.write("D=A");
                    } else {
                        this.write("D=M");
                    }
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

            if(arg1.equals("static")) {
                if(this.StaticOffset.containsKey(className)) {
                    this.StaticOffset.put(className, this.StaticOffset.get(className) + 1);
                }
                else {
                    this.StaticOffset.put(className, 1);
                }
            }

            // Pop the value from the stack
            this.write("@SP");
            this.write("M=M-1");
            this.write("A=M");
            this.write("D=M");

            // store it in R14
            this.write("@R14");
            this.write("M=D");

            // Get the address in which to pop
            if(arg1.equals("static")) {
                this.write("@" + className);
                this.write("D=A");

                Integer addedStatics = 0;
                for(String currentKey : this.StaticOffset.keySet()){
                    if(!currentKey.equals(className)) {
                        addedStatics += this.StaticOffset.get(currentKey);
                    }
                }
                if(addedStatics > 0) {
                    this.write("@" + (addedStatics - 1));
                    this.write("D=D+A");
                }
            }
            else {
                this.write("@" + Pointers.get(arg1));
                if(arg1.equals("pointer") || arg1.equals("temp")) {
                    this.write("D=A");
                }
                else {
                    this.write("D=M");
                }
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
        this.write("(" + label + ")");
        this.lineNum--;
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
        this.write("D;JNE");
    }

    public void writeFunction(String functionName, int numVars) {
        this.writeComment("// function " + functionName + " " + numVars);

        this.writeLabel(functionName);
        for(int i = 0; i < numVars; i++) {
            this.writePushPop("C_PUSH", "constant", 0, "");
        }
    }

    public void writeCall(String functionName, int numArgs) {
        this.writeComment("// call " + functionName + " " + numArgs);

        // push return-address
        this.write("@" + (this.lineNum + 47));
        this.write("D=A");
        this.write("@SP");
        this.write("A=M");
        this.write("M=D");
        this.write("@SP");
        this.write("M=M+1");

        // push local
        this.write("@local");
        this.write("D=M");
        this.write("@SP");
        this.write("A=M");
        this.write("M=D");
        this.write("@SP");
        this.write("M=M+1");

        // push argument
        this.write("@argument");
        this.write("D=M");
        this.write("@SP");
        this.write("A=M");
        this.write("M=D");
        this.write("@SP");
        this.write("M=M+1");

        // push this
        this.write("@this");
        this.write("D=M");
        this.write("@SP");
        this.write("A=M");
        this.write("M=D");
        this.write("@SP");
        this.write("M=M+1");

        // push that
        this.write("@that");
        this.write("D=M");
        this.write("@SP");
        this.write("A=M");
        this.write("M=D");
        this.write("@SP");
        this.write("M=M+1");

        // reposition argument to SP-n-5
        this.write("@SP");
        this.write("D=M");
        this.write("@" + (numArgs + 5));
        this.write("D=D-A");
        this.write("@argument");
        this.write("M=D");

        // LCL = SP
        this.write("@SP");
        this.write("D=M");
        this.write("@local");
        this.write("M=D");

        // transfer control
        this.write("@" + functionName);
        this.write("0;JMP");
    }

    public void writeReturn() {
        this.writeComment("// return");

        // pop the returned value into R12
        this.write("@SP");
        this.write("A=M");
        this.write("A=A-1");
        this.write("D=M");
        this.write("@R12");
        this.write("M=D");

        // Save frame pointer to temp R15
        this.write("@local");
        this.write("D=M");
        this.write("@R15");
        this.write("M=D");

        // Save return address to R14
        this.write("@local");
        this.write("A=M");
        this.movePointer(-5);
        this.write("D=M");
        this.write("@R14");
        this.write("M=D");

        // Push the returned value R12 into arg[0]
        this.write("@R12");
        this.write("D=M");
        this.write("@argument");
        this.write("A=M");
        this.write("M=D");

        // SP = ARG+1
        this.write("@argument");
        this.write("D=M");
        this.write("D=D+1");
        this.write("@SP");
        this.write("M=D");


        // Restore that, this, argument and local to caller
        this.write("@R15");
        this.write("A=M");
        this.movePointer(-1);
        this.write("D=M");
        this.write("@that");
        this.write("M=D");

        this.write("@R15");
        this.write("A=M");
        this.movePointer(-2);
        this.write("D=M");
        this.write("@this");
        this.write("M=D");

        this.write("@R15");
        this.write("A=M");
        this.movePointer(-3);
        this.write("D=M");
        this.write("@argument");
        this.write("M=D");

        this.write("@R15");
        this.write("A=M");
        this.movePointer(-4);
        this.write("D=M");
        this.write("@local");
        this.write("M=D");

        // goto return address
        this.write("@R14");
        this.write("A=M");
        this.write("0;JMP");
    }

    private void movePointer(int pos) {
        this.writeComment("// Moving: " + pos + " pos");

        if(pos > 0) {
            for(int i = 0; i < pos; i++) {
                this.write("A=A+1");
            }
        }
        else if(pos < 0){
            for(int i = 0; i < -pos; i++) {
                this.write("A=A-1");
            }
        }
    }

    private void writeInfLoop() {
        this.writeLabel("END");
        this.writeGoto("END");
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
