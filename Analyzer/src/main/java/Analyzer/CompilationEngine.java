package Analyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("Duplicates")
public class CompilationEngine {
    private String baseDir = "/Users/francois.stiennon/Desktop/nand2tetris2/GitHub/Analyzer/src/main/java/Analyzer";
    private String fileName;

    private Tokenizer tk;
    private int indentation = 0;

    private boolean matchIdentifiers = false;
    private boolean varDec = false;

    private SymbolTable symbolTable;
    private SymbolBuilder symbolBuilder;

    public CompilationEngine(String inputFile) throws Exception {
        this.fileName = inputFile.split("\\.")[0];

        tk = new Tokenizer(this.fileName);

        String fileN = baseDir + "/Out/" + fileName + ".xml";
        File f = new File(fileN);
        f.createNewFile();

        String vmFileN = baseDir + "/Comp/" + fileName + ".vm";
        File vmF = new File(vmFileN);
        vmF.createNewFile();

        symbolTable = new SymbolTable();
        symbolBuilder = new SymbolBuilder();

        this.CompileClass();

        // System.out.println(this.symbolTable);
    }

    private void CompileClass() throws Exception {
        // Compiles a complete class.

        this.writeOpenNonTerm("class");

        this.writeTerm(this.tk.getToken());

        this.writeTokens(2);

        this.tk.advance();

        while(this.VarDecOrSubroutine() != "}") {
            if(this.VarDecOrSubroutine().equals("VarDec")) {
                this.CompileClassVarDec();
            }
            else if(this.VarDecOrSubroutine().equals("Subroutine")) {
                this.CompileSubroutine();
                this.symbolTable.clearSubroutineScope();
            }
        }

        this.writeTerm(this.tk.getToken());

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
        this.matchIdentifiers = true;
        this.varDec = true;

        this.writeTerm(this.tk.getToken());

        this.symbolBuilder.clear();
        this.symbolBuilder.ofKind(this.tk.getToken().StringValue());

        this.tk.advance();
        this.writeTerm(this.tk.getToken());

        this.symbolBuilder.ofType(this.tk.getToken().StringValue());

        this.tk.advance();

        this.symbolBuilder.ofName(this.tk.getToken().StringValue());
        this.symbolTable.define(this.symbolBuilder);

        this.writeTerm(this.tk.getToken());

        this.tk.advance();

        while(this.tk.getToken().StringValue().equals(",")) {
             this.writeTerm(this.tk.getToken());

             this.tk.advance();

             this.symbolBuilder.ofName(this.tk.getToken().StringValue());
             this.symbolTable.define(this.symbolBuilder);

             this.writeTerm(this.tk.getToken());

             this.tk.advance();
        }

        this.writeTerm(this.tk.getToken());
        this.tk.advance();

        this.varDec = false;
        this.matchIdentifiers = false;
        this.writeCloseNonTerm("classVarDec");
    }

    private void CompileSubroutine() throws Exception {
        // Compiles a complete method, function, or constructor.

        this.writeOpenNonTerm("subroutineDec");

        if(this.tk.keyword().equals("constructor")
            || this.tk.keyword().equals("method")
            || this.tk.keyword().equals("function")) {

            this.writeTerm(this.tk.getToken());

            this.tk.advance();
            this.writeTerm(this.tk.getToken());

            this.tk.advance();
            this.writeTerm(this.tk.getToken());
            String funcName = this.tk.getToken().StringValue();

            this.tk.advance();
            this.writeTerm(this.tk.getToken());

            this.tk.advance();

            this.compileParameterList();

            this.writeTerm(this.tk.getToken());

            this.writeOpenNonTerm("subroutineBody");
            this.matchIdentifiers = true;

            this.writeTokens(1);

            this.tk.advance();

            int varCount = this.compileVarDec();
            this.appendToVmFile("function " + fileName + "." + funcName + " " + varCount);

            this.compileStatements();

            this.writeTerm(this.tk.getToken());
            this.tk.advance();

            this.matchIdentifiers = false;
            this.writeCloseNonTerm("subroutineBody");
        }
        else {
            throw new Exception("Subroutine not found.");
        }

        this.writeCloseNonTerm("subroutineDec");
    }

    private void compileParameterList() throws Exception {
        // Compiles a (possibly empty) parameter list, not including the enclosing ‘‘()’’.
        this.writeOpenNonTerm("parameterList");
        this.matchIdentifiers = true;
        this.varDec = true;

        while(!this.tk.getToken().StringValue().equals(")")) {

            this.symbolBuilder.clear();
            this.symbolBuilder.ofKind("arg");

            this.symbolBuilder.ofType(this.tk.getToken().StringValue());

            this.writeTerm(this.tk.getToken());
            this.tk.advance();

            this.symbolBuilder.ofName(this.tk.getToken().StringValue());
            this.symbolTable.define(this.symbolBuilder);

            this.writeTerm(this.tk.getToken());
            this.tk.advance();

            if(this.tk.getToken().StringValue().equals(")")) {
                break;
            }
            else {
                this.writeTerm(this.tk.getToken());
                this.tk.advance();
            }
        }

        this.matchIdentifiers = false;
        this.varDec = false;
        this.writeCloseNonTerm("parameterList");
    }

    private int compileVarDec() throws Exception {
        // Compiles a var declaration.
        int varCount = 0;

        this.varDec = true;
        while(this.tk.getToken().StringValue().equals("var")) //noinspection Duplicates
        {
            varCount++;

            this.writeOpenNonTerm("varDec");

            this.writeTerm(this.tk.getToken());

            this.symbolBuilder.clear();
            this.symbolBuilder.ofKind(this.tk.getToken().StringValue());

            this.tk.advance();
            this.writeTerm(this.tk.getToken());

            this.symbolBuilder.ofType(this.tk.getToken().StringValue());

            this.tk.advance();

            this.symbolBuilder.ofName(this.tk.getToken().StringValue());
            this.symbolTable.define(this.symbolBuilder);

            this.writeTerm(this.tk.getToken());

            this.tk.advance();

            while(!this.tk.getToken().StringValue().equals(";")) {
                varCount++;
                this.writeTerm(this.tk.getToken());

                this.tk.advance();

                this.symbolBuilder.ofName(this.tk.getToken().StringValue());
                this.symbolTable.define(this.symbolBuilder);

                this.writeTerm(this.tk.getToken());

                this.tk.advance();
            }

            this.writeTerm(this.tk.getToken());

            this.writeCloseNonTerm("varDec");

            this.tk.advance();
        }
        this.varDec = false;

        return varCount;
    }

    private void compileStatements() throws Exception {
        // Compiles a sequence of state- ments, not including the enclosing ‘‘{}’’.
        this.writeOpenNonTerm("statements");

        while(!this.tk.getToken().StringValue().equals("return") && !this.tk.getToken().StringValue().equals("}")) {
            switch (this.tk.getToken().StringValue()) {
                case "if":
                    this.compileIf();
                    break;
                case "while":
                    this.compileWhile();
                    break;
                case "let":
                    this.compileLet();
                    break;
                case "do":
                    this.compileDo();
                    break;
                case "return":
                    this.compileReturn();
                    break;
                default:
                    throw new Exception("Statement not found.");
            }
        }

        if(this.tk.getToken().StringValue().equals("return")) this.compileReturn();

        this.writeCloseNonTerm("statements");
    }

    private void compileDo() {
        // Compiles a do statement.
        this.writeOpenNonTerm("doStatement");

        this.writeTerm(this.tk.getToken());
        this.tk.advance();

        this.subroutineCall();

        this.writeTerm(this.tk.getToken());
        this.tk.advance();

        this.appendToVmFile("pop temp 0");

        this.writeCloseNonTerm("doStatement");
    }

    private void compileLet() {
        // Compiles a let statement.
        this.writeOpenNonTerm("letStatement");

        this.writeTerm(this.tk.getToken());
        this.tk.advance();

        this.writeTerm(this.tk.getToken());

        Token assigned = this.tk.getToken();
        SymbolLookupResult symbol = this.symbolTable.getSymbol(assigned.StringValue());

        if(!symbol.Found) {
            System.out.println("Symbol not found: " + assigned.StringValue());
        }

        this.tk.advance();

        if(this.tk.getToken().StringValue().equals("[")){ // If the value is an array
            this.writeTerm(this.tk.getToken());
            this.tk.advance();

            this.compileExpression();

            this.writeTerm(this.tk.getToken());
            this.tk.advance();

            this.writeTerm(this.tk.getToken());
            this.tk.advance();

            this.compileExpression();
        }
        else {
            this.writeTerm(this.tk.getToken());
            this.tk.advance();
            this.compileExpression();
        }

        this.appendToVmFile("pop local " + symbol.Symbol.Index);

        this.writeTerm(this.tk.getToken());
        this.tk.advance();

        this.writeCloseNonTerm("letStatement");
    }

    private void compileWhile() throws Exception {
        // Compiles a while statement.
        this.writeOpenNonTerm("whileStatement");

        this.appendToVmFile("label WHILE_EXP0");

        this.writeTerm(this.tk.getToken());
        this.tk.advance();

        this.writeTerm(this.tk.getToken());
        this.tk.advance();

        this.compileExpression();

        this.appendToVmFile("not");
        this.appendToVmFile("if-goto WHILE_END0");

        this.writeTerm(this.tk.getToken());
        this.tk.advance();

        this.writeTerm(this.tk.getToken());
        this.tk.advance();

        this.compileStatements();

        this.appendToVmFile("goto WHILE_EXP0");
        this.appendToVmFile("label WHILE_END0");

        this.writeTerm(this.tk.getToken());
        this.tk.advance();

        this.writeCloseNonTerm("whileStatement");
    }

    private void compileReturn() {
        // Compiles a return statement.

        this.writeOpenNonTerm("returnStatement");

        this.writeTerm(this.tk.getToken());
        this.tk.advance();

        if(this.tk.getToken().StringValue().equals(";")) {
            this.writeTerm(this.tk.getToken());
            this.tk.advance();
        }
        else {
            this.compileExpression();

            this.writeTerm(this.tk.getToken());
            this.tk.advance();
        }

        this.appendToVmFile("push constant 0");
        this.appendToVmFile("return");

        this.writeCloseNonTerm("returnStatement");
    }

    private void compileIf() throws Exception {
        // Compiles an if statement, pos- sibly with a trailing else clause.
        this.writeOpenNonTerm("ifStatement");

        this.writeTerm(this.tk.getToken());

        this.tk.advance();
        this.writeTerm(this.tk.getToken());

        this.tk.advance();

        this.compileExpression();

        this.appendToVmFile("if-goto IF_TRUE0");
        this.appendToVmFile("goto IF_FALSE0");
        this.appendToVmFile("label IF_TRUE0");

        this.writeTerm(this.tk.getToken());
        this.tk.advance();

        this.writeTerm(this.tk.getToken());
        this.tk.advance();

        this.compileStatements();

        this.writeTerm(this.tk.getToken());
        this.tk.advance();

        if(this.tk.getToken().StringValue().equals("else")) {
            this.writeTerm(this.tk.getToken());
            this.tk.advance();

            this.writeTerm(this.tk.getToken());
            this.tk.advance();

            this.compileStatements();

            this.writeTerm(this.tk.getToken());
            this.tk.advance();
        }

        this.writeCloseNonTerm("ifStatement");
    }

    private void compileExpression() {
        // Compiles an expression.

        this.writeOpenNonTerm("expression");

        this.compileTerm();

        String operator = null;

        while("+-*/&|<>=".contains(this.tk.getToken().StringValue())) {

            this.writeTerm(this.tk.getToken());
            operator = this.tk.getToken().StringValue();
            this.tk.advance();

            this.compileTerm();

            switch (operator) {
                case "+":
                    this.appendToVmFile("add");
                    break;
                case "*":
                    this.appendToVmFile("call Math.multiply 2");
                    break;
                case ">":
                    this.appendToVmFile("gt");
                    break;
                case "<":
                    this.appendToVmFile("lt");
                    break;
                case "-":
                    this.appendToVmFile("sub");
                    break;
                case "/":
                    this.appendToVmFile("call Math.divide 2");
                    break;
                case "=":
                    this.appendToVmFile("eq");
                    break;
                case "&":
                    this.appendToVmFile("and");
                    break;
                default:
                    System.out.println("Operator not found: " + operator);
            }
        }

        this.writeCloseNonTerm("expression");
    }

    private void compileTerm() {
        this.writeOpenNonTerm("term");

        if(this.tk.getToken().Type == TokenType.integerConstant) {
            this.writeTerm(this.tk.getToken());

            this.appendToVmFile("push constant " + this.tk.getToken().StringValue());

            this.tk.advance();
        }

        else if(this.tk.getToken().Type == TokenType.keyword) {
            this.writeTerm(this.tk.getToken());

            if(this.tk.getToken().StringValue().equals("false")) {
                this.appendToVmFile("push constant 0");
            }
            if(this.tk.getToken().StringValue().equals("true")) {
                this.appendToVmFile("push constant 0");
                this.appendToVmFile("not");
            }

            this.tk.advance();
        }

        else {
            Token first = this.tk.getToken();
            this.tk.advance();
            Token second = this.tk.getToken();
            this.tk.goBack();

            if(second.StringValue().equals("[")) {
                this.writeTerm(this.tk.getToken());
                this.tk.advance();

                this.writeTerm(this.tk.getToken());
                this.tk.advance();

                this.compileExpression();

                this.writeTerm(this.tk.getToken());
                this.tk.advance();
            }
            else if(first.StringValue().equals("(")) {
                this.writeTerm(this.tk.getToken());
                this.tk.advance();

                this.compileExpression();

                this.writeTerm(this.tk.getToken());
                this.tk.advance();
            }
            else if(second.StringValue().equals(".")) {
                this.subroutineCall();
            }
            else if(first.StringValue().equals("-") || first.StringValue().equals("~")) {
                this.writeTerm(this.tk.getToken());
                this.tk.advance();

                this.compileTerm();

                if(first.StringValue().equals("-")) {
                    this.appendToVmFile("neg");
                }
                else if(first.StringValue().equals("~")) {
                    this.appendToVmFile("not");
                }
            }
            else {
                // varName
                this.writeTerm(this.tk.getToken());

                Token assigned = this.tk.getToken();
                SymbolLookupResult symbol = this.symbolTable.getSymbol(assigned.StringValue());

                if(!symbol.Found) {
                    System.out.println("Symbol not found: " + this.tk.getToken().StringValue());
                }

                switch (symbol.Symbol.Kind) {
                    case VAR:
                        this.appendToVmFile("push local " + symbol.Symbol.Index);
                        break;
                    case ARG:
                        this.appendToVmFile("push argument " + symbol.Symbol.Index);
                        break;
                    case FIELD:
                        this.appendToVmFile("push field " + symbol.Symbol.Index);
                        break;
                    case STATIC:
                        this.appendToVmFile("push static " + symbol.Symbol.Index);
                        break;
                }

                this.tk.advance();
            }
        }

        this.writeCloseNonTerm("term");
    }

    private void subroutineCall() {
        String className = "";
        String subroutineName = "";
        int expressionCount = 0;

        this.tk.advance();
        String second = this.tk.getToken().StringValue();
        this.tk.goBack();

        if(second.equals("(")) {
            this.writeTerm(this.tk.getToken());
            subroutineName = this.tk.getToken().StringValue();
            this.tk.advance();

            this.writeTerm(this.tk.getToken());
            this.tk.advance();

            expressionCount = this.CompileExpressionList();

            this.writeTerm(this.tk.getToken());
            this.tk.advance();
        }
        else if(second.equals(".")) {
            this.writeTerm(this.tk.getToken());
            className = this.tk.getToken().StringValue();
            this.tk.advance();

            this.writeTerm(this.tk.getToken());
            this.tk.advance();

            this.writeTerm(this.tk.getToken());
            subroutineName = this.tk.getToken().StringValue();
            this.tk.advance();

            this.writeTerm(this.tk.getToken());
            this.tk.advance();

            expressionCount = this.CompileExpressionList();

            this.writeTerm(this.tk.getToken());
            this.tk.advance();
        }
        else {
            this.writeOpenNonTerm("ERROR");
        }

        if(className.equals("")) {
            this.appendToVmFile("call " + subroutineName + " " + expressionCount);
        }
        else {
            this.appendToVmFile("call " + className + "." + subroutineName + " " + expressionCount);
        }
    }

    private int CompileExpressionList() {
        // Compiles a ( possibly empty) comma-separated list of expressions.
        this.writeOpenNonTerm("expressionList");

        int expressionCount = 0;

        while(!this.tk.getToken().StringValue().equals(")")) {
            expressionCount++;

            this.compileExpression();

            if(this.tk.getToken().StringValue().equals(")")) {
                break;
            }
            else {
                this.writeTerm(this.tk.getToken());
                this.tk.advance();
            }
        }

        this.writeCloseNonTerm("expressionList");

        return expressionCount;
    }

    private void writeOpenNonTerm(String identifier) {
        this.appendToXmlFile("<" + identifier + ">");
        this.indentation++;
    }

    private void writeCloseNonTerm(String identifier) {
        this.indentation--;
        this.appendToXmlFile("</" + identifier + ">");
    }

    public void writeTokens(int numberOfTokens) {
        for(int i = 0; i < numberOfTokens; i++) {
            this.tk.advance();
            this.writeTerm(this.tk.getToken());
        }
    }

    private void writeTerm(Token token) {
        if(token.Type == TokenType.identifier
           && this.symbolTable.getSymbol(token.StringValue()).Found
           && matchIdentifiers) {
            String definedOrUsed = this.varDec ? "defined" : "used";
            this.appendToXmlFile(token.toString() + " "
                    + this.symbolTable.getSymbol(token.StringValue()).Symbol.toString() + " "
                    + definedOrUsed );
        }
        else {
            this.appendToXmlFile(token.toString());
        }
    }

    private void appendToXmlFile(String line) {
        String fileN = baseDir + "/Out/" + fileName + ".xml";
        String toWrite = String.join("", Collections.nCopies(indentation, "  ")) + line + "\n";

        try {
            Files.write(Paths.get(fileN), toWrite.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
            System.out.println("File not found: " + fileN);
        }
    }

    private void appendToVmFile(String line) {
        String fileN = baseDir + "/Comp/" + fileName + ".vm";

        try {
            Files.write(Paths.get(fileN), (line + "\n").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("File not found: " + fileN);
        }

    }
}
