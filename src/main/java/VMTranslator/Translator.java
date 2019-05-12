package VMTranslator;


public class Translator {
    public static void main(String[] args) {

        Parser p = new Parser(args[0]);

        System.out.println(p);

        CodeWriter cw = new CodeWriter(args[0]);

        if(p.commandType == "C_ARITHMETIC") {
            cw.writeArithmetic(p.arg1);
        }
        else {
            cw.writePushPop(p.commandType, p.arg1, p.arg2);
        }

        while(p.hasMoreCommands()) {
            p.advance();

            if(p.commandType == "C_ARITHMETIC") {
                cw.writeArithmetic(p.arg1);
            }
            else {
                cw.writePushPop(p.commandType, p.arg1, p.arg2);
            }
        }

        cw.closeFile();

    }
}
