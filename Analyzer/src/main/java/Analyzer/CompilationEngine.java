package Analyzer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

public class CompilationEngine {

    private Tokenizer tk;

    private FileWriter FW;
    private int indentation = 0;


    public CompilationEngine(String inputFile, String outputFile) throws Exception {
        String baseDir = "/Users/francois.stiennon/Desktop/nand2tetris/GitHub/Analyzer/src/main/java/Analyzer";
        tk = new Tokenizer(inputFile);

        try {
            this.FW = new FileWriter(baseDir + "/Out/" + outputFile + "Comp.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.CompileClass();

        this.FW.close();
    }

    private void CompileClass() throws Exception {
        // Compiles a complete class.
        this.writeOpenNonTerm("class");

        if(this.tk.tokenType() == TokenType.keyword && this.tk.keyword().equals("class")) {
            this.writeTerm(this.tk.getToken());
        }
        else {
            throw new Exception("Incorrect grammar.");
        }

        this.tk.advance();

        if(this.tk.tokenType() == TokenType.identifier) {
            this.writeTerm(this.tk.getToken());
        }
        else {
            throw new Exception("Incorrect grammar.");
        }

        this.tk.advance();

        if(this.tk.tokenType() == TokenType.symbol && this.tk.symbol() == '{') {
            this.writeTerm(this.tk.getToken());
        }
        else {
            throw new Exception("Incorrect grammar.");
        }

        this.tk.advance();

        while(this.VarDecOrSubroutine() != "}") {
            if(this.VarDecOrSubroutine().equals("VarDec")) {
                this.CompileClassVarDec();
            }
            else if(this.VarDecOrSubroutine().equals("Subroutine")) {
                this.CompileSubroutine();
            }
        }

        this.appendToFile("}");

        this.writeCloseNonTerm("class");
    }

    private String VarDecOrSubroutine() throws Exception {

        if(this.tk.tokenType() == TokenType.symbol && this.tk.symbol() == '}') {
            return "}";
        }

        if(this.tk.keyword().equals("static") || this.tk.keyword().equals("field")) {
            return "VarDec";
        }
        else if(this.tk.keyword().equals("constructor") || this.tk.keyword().equals("function") || this.tk.keyword().equals("method")) {
            return "Subroutine";
        }
        else {
            throw new Exception("Expecting a var dec or a subroutine.");
        }
    }

    private void CompileClassVarDec() throws Exception {
        // Compiles a static declaration or a field declaration.
        this.writeOpenNonTerm("classVarDec");

        this.writeTerm(this.tk.getToken());

        this.tk.advance();
        this.writeTerm(this.tk.getToken());

        this.tk.advance();
        this.writeTerm(this.tk.getToken());

        this.tk.advance();

        while(this.tk.getToken().StringValue().equals(",")) {
             this.writeTerm(this.tk.getToken());

             this.tk.advance();
             this.writeTerm(this.tk.getToken());

             this.tk.advance();
        }

        this.writeTerm(this.tk.getToken());
        this.tk.advance();

        this.writeCloseNonTerm("classVarDec");
    }

    private void CompileSubroutine() throws Exception {
        // Compiles a complete method, function, or constructor.
        this.writeOpenNonTerm("subroutineDec");

        switch (this.tk.keyword()) {
            case "constructor":


                break;
            case "function":
                this.writeTerm(this.tk.getToken());

                this.tk.advance();
                this.writeTerm(this.tk.getToken());

                this.tk.advance();
                this.writeTerm(this.tk.getToken());

                this.tk.advance();
                this.writeTerm(this.tk.getToken());

                this.tk.advance();

                this.compileParameterList();

                this.writeTerm(this.tk.getToken());

                this.tk.advance();
                this.writeTerm(this.tk.getToken());

                this.compileVarDec();

                this.compileStatements();

                this.writeTerm(this.tk.getToken());
                break;

            case "method":

                break;
            default:
                throw new Exception("Subroutine not found.");
        }

        this.writeCloseNonTerm("subroutineDec");
    }

    private void compileParameterList() {
        // Compiles a (possibly empty) parameter list, not including the enclosing ‘‘()’’.
        this.writeOpenNonTerm("parameterList");

        this.writeCloseNonTerm("parameterList");
    }

    private void compileVarDec() {
        // Compiles a var declaration.
        this.writeOpenNonTerm("varDec");

        while(this.tk.getToken().StringValue().equals("var")) {

        }

        this.writeCloseNonTerm("varDec");
    }

    private void compileStatements() {
        // Compiles a sequence of state- ments, not including the enclosing ‘‘{}’’.
        this.writeOpenNonTerm("statements");

        while(!this.tk.getToken().StringValue().equals("return")) {

        }

        this.writeCloseNonTerm("statements");
    }

    private void compileDo() {
        // Compiles a do statement.

    }

    private void compileLet() {
        // Compiles a let statement.

    }

    private void compileWhile() {
        // Compiles a while statement.
    }

    private void compileReturn() {
        // Compiles a return statement.
    }

    private void compileIf() {
        // Compiles an if statement, pos- sibly with a trailing else clause.
    }

    private void compileExpression() {
        // Compiles an expression.
    }

    private void compileTerm() {
        // Compiles a term. This routine is faced with a slight difficulty when trying to decide between some of the alternative parsing rules. Specifically, if the current token is an identifier, the routine must distinguish between a variable, an array entry, and a subroutine call. A single look- ahead token, which may be one of ‘‘[’’, ‘‘(’’, or ‘‘.’’ suffices to dis- tinguish between the three possi- bilities. Any other token is not part of this term and should not be advanced over.

    }

    private void CompileExpressionList() {
        // Compiles a ( possibly empty) comma-separated list of expressions.
    }

    private void writeOpenNonTerm(String identifier) {
        this.appendToFile("<" + identifier + ">");
        this.indentation++;
    }

    private void writeCloseNonTerm(String identifier) {
        this.indentation--;
        this.appendToFile("</" + identifier + ">");
    }

    private void writeTerm(Token token) {
        this.appendToFile(token.toString());
    }

    private void appendToFile(String line) {
        try {
            FW.write(String.join("", Collections.nCopies(indentation, " ")) + line + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
