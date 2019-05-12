package VMTranslator;

import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {

    private FileWriter FW;

    private int jumpCounter = 0;

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

    private void initialise() {
        this.write("// INITIALISE");
        this.write("@256");
        this.write("D=A");
        this.write("@0");
        this.write("M=D");
        this.write("A=M");
        this.write("M=0");
        for(int i = 0; i < 20; i++) {
            this.write("A=A+1");
            this.write("M=0");
        }
        this.write("@0");
    }

    private void write(String stringToWrite) {
        try {
            FW.write(stringToWrite + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void PCMin2Min1InD() {
        this.write("@0");
        this.write("D=M");
        this.write("D=D-1");
        this.write("A=D");
        this.write("D=M");
        this.write("A=A-1");
    }

    private void PcDecrement() {
        this.write("@0");
        this.write("M=M-1");
    }

    private void CompMain(String Jump) {
        this.PCMin2Min1InD();
        this.write("M=M-D");
        this.write("D=M");
        this.write("M=0");
        this.write("@DI" + jumpCounter);
        this.write("D;" + Jump);
        this.write("@0");
        this.write("D=M");
        this.write("D=D-1");
        this.write("D=D-1");
        this.write("A=D");
        this.write("M=M-1");
        this.write("(DI" + jumpCounter + ")");
        this.PcDecrement();
    }

    private void NegNotSetup() {
        this.write("@0");
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
                System.out.println("writeArithmetic: ar1 not found.");
        }

        this.jumpCounter++;
    }

    public void writePushPop(String commandType, String arg1, int arg2) {
        if(commandType == "C_PUSH") {
            this.write("// push " + arg1 + " " + arg2);

            this.write("@" + arg2);
            this.write("D=A");
            this.write("@0");
            this.write("A=M");
            this.write("M=D");
            this.write("@0");
            this.write("M=M+1");
        }
        else if(commandType == "C_POP") {
            this.write("// pop " + arg1 + " " + arg2);
        }
        else {
            System.out.println("commandType not found.");
        }
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
