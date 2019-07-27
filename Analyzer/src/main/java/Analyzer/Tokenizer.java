package Analyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
public class Tokenizer {

    private List<String> lines = new ArrayList<>();
    private List<String> stringSplit = new ArrayList<>();
    private List<String> spaceSplit = new ArrayList<>();
    private List<String> charSplit = new ArrayList<>();

    private List<Token> tokens = new ArrayList<Token>();
    private Token[] tokenArray;

    private int index = 0;

    private List<String> keywords = Arrays.asList("class", "constructor", "function", "method", "field", "static", "var", "int", "char", "boolean", "void", "true", "false", "null", "this", "let", "do", "if", "else", "while", "return");

    private List<Character> symbols = Arrays.asList('{', '}', '(', ')', '[', ']', '.', ',', ';', '+', '-', '*', '/', '&', '|', ',', '<', '>', '=', '~');

    private FileWriter FW;

    private boolean currentComment = false;

    public Tokenizer(String fileName) {
        String baseDir = "/Users/francois.stiennon/Desktop/nand2tetris2/GitHub/Analyzer/src/main/java/Analyzer";
        try {
            File f = new File(baseDir + "/Jack/" + fileName + ".jack");
            Scanner file = new Scanner(f);
            this.parseLines(file);
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.parseStringSplit();

        this.parseSpaceSplit();

        this.parseCharSplit();

        this.parseTokens();

        this.tokenArray = new Token[tokens.size()];
        this.tokenArray = tokens.toArray(tokenArray);

        try {
            this.FW = new FileWriter(baseDir + "/Out/" + fileName + "Tokens.xml");
            this.writeXml();
            this.FW.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseLines(Scanner file) {
        String currentLine;
        while (file.hasNextLine()) {
            currentLine = file.nextLine();
            if (this.includesTokens(currentLine)) {
                lines.add(cleanCode(currentLine));
            }
        }
    }

    private boolean includesTokens(String line) {

        if(line.equals("\t")) return false;
        if(line.trim().length() == 0) return false;

        if(line.contains("*/")) {
            this.currentComment = false;
            return false;
        }

        if(this.currentComment) return false;

        if (line.length() > 0) {
            if (line.contains("//")) {
                if(line.equals("//")) return false;
                String[] split = line.split("//");
                if(split[0].trim().length() == 0) return false;
                return split[0].length() > 0;
            }
            else if(line.contains("/**")) {
                this.currentComment = true;

                if(line.equals("/**")) return false;
                String[] split = line.split(Pattern.quote("/**"));
                if(split[0].trim().length() == 0) return false;
                return split[0].length() > 0;
            }
            else return true;
        }
        return false;
    }

    private String cleanCode(String line) {
        if (line.contains("//")) {
            String[] split = line.split("//");
            line = split[0];
        }
        return line.trim();
    }

    private void parseStringSplit() {
        for(String line : this.lines) {
            this.stringSplitLine(line);
        }
    }

    private void stringSplitLine(String line) {
        if(line.contains("\"")) {
            String[] quoteSplit = line.split("((?<=\")|(?=\"))");

            boolean isString = quoteSplit.equals("\"");

            for(String str : quoteSplit) {
                if(str.equals("\"")) {
                    isString = !isString;
                    continue;
                }

                if(isString) this.stringSplit.add("\"" + str + "\"");
                else this.stringSplit.add(str);
            }
        }
        else {
            this.stringSplit.add(line);
        }
    }

    private void parseSpaceSplit() {
        for(String lineOrString : stringSplit) {
            if(lineOrString.charAt(0) == '"') this.spaceSplit.add(lineOrString);
            else this.spaceSplit.addAll(Arrays.asList(lineOrString.split(" ")));
        }
    }

    private void parseCharSplit() {
        for(String line : this.spaceSplit) {
            this.splitLine(line);
        }
    }

    private void splitLine(String line) {

        String strAdd = "";

        for (int i = 0; i < line.length(); i++){
            char c = line.charAt(i);

            if(this.symbols.contains(c)) {

                if(strAdd.length() > 0) {
                    this.charSplit.add(strAdd);
                    strAdd = "";
                }

                this.charSplit.add("" + c);
                continue;
            }

            strAdd += c;
        }

        if(strAdd.length() > 0) {
            this.charSplit.add(strAdd);
        }

    }

    private void parseTokens() {
        for(String tokStr : this.charSplit) {
            this.tokens.add(this.TokenParse(tokStr));
        }
    }

    private Token TokenParse(String tokStr) {

        if(keywords.stream().anyMatch(str -> str.equals(tokStr)))
            return new Token(TokenType.keyword, tokStr);

        if(tokStr.length() == 1 && symbols.contains(tokStr.charAt(0)))
            return new Token(TokenType.symbol, tokStr.charAt(0));


        if(Character.isDigit(tokStr.charAt(0)))
            return new Token(TokenType.integerConstant, Integer.parseInt(tokStr));

        if(tokStr.charAt(0) == '"')
            return new Token(TokenType.stringConstant, tokStr.replace("\"", ""));

        return new Token(TokenType.identifier, tokStr);

    }

    private void writeXml() {
        this.appendToFile("<tokens>");

        for(Token t : tokens) {
            this.appendToFile(t.toString());
        }

        this.appendToFile("</tokens>");
    }

    private void appendToFile(String line) {
        try {
            FW.write(line + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasMoreTokens() {
        return this.index + 1 < this.tokenArray.length;
    }

    public void advance() {
        this.index++;
    }

    public void goBack() { this.index--; }

    public Token getToken() {
        return this.tokenArray[index];
    }

    public TokenType tokenType() {
        return this.tokenArray[index].Type;
    }

    public String keyword() throws Exception {
        if(this.tokenArray[index].Type == TokenType.keyword) {
            return (String) this.tokenArray[index].Value;
        }
        else {
            throw new Exception("Not a keyword");
        }
    }

    public char symbol() throws Exception {
        if(this.tokenArray[index].Type == TokenType.symbol) {
            return (char) this.tokenArray[index].Value;
        }
        else {
            throw new Exception("Not a symbol");
        }
    }

    public String identifier() throws Exception {
        if(this.tokenArray[index].Type == TokenType.identifier) {
            return (String) this.tokenArray[index].Value;
        }
        else {
            throw new Exception("Not an identifier");
        }
    }

    public Integer intVal() throws Exception {
        if(this.tokenArray[index].Type == TokenType.integerConstant) {
            return (Integer) this.tokenArray[index].Value;
        }
        else {
            throw new Exception("Not an integerConstant");
        }
    }

    public String stringVal() throws Exception {
        if(this.tokenArray[index].Type == TokenType.stringConstant) {
            return (String) this.tokenArray[index].Value;
        }
        else {
            throw new Exception("Not an stringConstant");
        }
    }

}
