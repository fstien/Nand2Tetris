package VMTranslator;

public class Translator {
    public static void main(String[] args) {


        Parser p = new Parser(args[0]);

        System.out.println(p.hasMoreCommands());


        p.closeFile();
    }

}
