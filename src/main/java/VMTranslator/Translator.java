package VMTranslator;


public class Translator {

    public static void main(String[] args) {

        Parser p = new Parser(args[0]);

        System.out.println(p);

        CodeWriter cw = new CodeWriter(args[0]);

        writeCommand(p, cw);

        while(p.hasMoreCommands()) {
            p.advance();
            writeCommand(p, cw);
        }

        cw.closeFile();
    }

    private static void writeCommand(Parser p, CodeWriter cw) {
        if(p.commandType.equals("C_ARITHMETIC")) {
            cw.writeArithmetic(p.arg1);
        }
        else if(p.commandType.equals("C_LABEL")) {
            cw.writeLabel(p.arg1);
        }
        else if(p.commandType.equals("C_GOTO")) {
            cw.writeGoto(p.arg1);
        }
        else if(p.commandType.equals("C_IFGOTO")) {
            cw.writeIf(p.arg1);
        }
        else if(p.commandType.equals("C_PUSH") || p.commandType.equals("C_POP")) {
            cw.writePushPop(p.commandType, p.arg1, p.arg2);
        }
        else if(p.commandType.equals("C_FUNCTION")) {
            cw.writeFunction(p.arg1, p.arg2);
        }
        else if(p.commandType.equals("C_RETURN")) {
            cw.writeReturn();
        }
        else if(p.commandType.equals("C_CALL")) {
            cw.writeCall(p.arg1, p.arg2);
        }
        else {
            System.out.println("Command type not found.");
        }
    }
}
