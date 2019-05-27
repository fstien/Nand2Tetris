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

public class Tokenizer {

    private String baseDir = "/Users/francois.stiennon/Desktop/nand2tetris/GitHub/Analyzer/src/main/java/Analyzer";

    private List<String> lines = new ArrayList<>();
    private List<Token> tokens = new ArrayList<Token>();

    private List<String> keywords = Arrays.asList("class", "constructor", "function", "method", "field", "static", "var", "int", "char", "boolean", "void", "true", "false", "null", "this", "let", "do", "if", "else", "while", "return");

    private List<String> symbols = Arrays.asList("{", "}", "(", ")", "[", "]", ".", "," , ";" , "+" , "-" , "*" , "/" , "&" , ", " , "<" , ">" , "=" , "~");

    private FileWriter FW;

    public Tokenizer(String fileName) throws FileNotFoundException {
        try {
            File f = new File(baseDir + "/Jack/" + fileName + ".jack");
            Scanner file = new Scanner(f);
            this.parseLines(file);
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for(String line : this.lines) {
            this.parseTokensFromLine(line);
        }

        try {
            this.FW = new FileWriter(baseDir  + "/Out/" + fileName + ".xml");
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
        if (line.length() > 0) {
            if (line.contains("//")) {
                if(line.equals("//")) return false;
                String[] split = line.split("//");
                if(split[0].trim().length() == 0) return false;
                return split[0].length() > 0;
            }
            else if(line.contains("/**")) {
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

    private void parseTokensFromLine(String line) {
        String[] spaceSplit = line.split(" ");
        for(String tokStr : spaceSplit) {
            this.parseTokensFromString(tokStr);
        }
    }

    private void parseTokensFromString(String tokString) {

        if(Character.isDigit(tokString.charAt(0))) {
            this.tokens.add(new Token(TokenType.intConstant, Integer.parseInt(tokString)));
            return;
        }

        if(tokString.charAt(0) == '"') {
            this.tokens.add(new Token(TokenType.stringConstant, tokString));
            return;
        }

        String currentString = "";
        for (int i = 0; i < tokString.length(); i++) {
            char c = tokString.charAt(i);
            currentString += c;

            String finalCurrentString = currentString;
            if(keywords.stream().anyMatch(str -> str.equals(finalCurrentString))) {
                this.tokens.add(new Token(TokenType.keyword, finalCurrentString));
                currentString = "";
            }
            else if(symbols.contains(c)) {
                this.tokens.add(new Token(TokenType.symbol, c));
                currentString = "";
            }

        }
        // push the currentString as an identifier if length > 0
        if (currentString.length() > 0) {
            this.tokens.add(new Token(TokenType.identifier, currentString));
        }
    }


    private void writeXml() {
        this.appendToFile("<tokens>");


        this.appendToFile("</tokens>");
    }

    private void appendToFile(String line) {
        try {
            FW.write(line + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    public boolean hasMoreTokens() {
        return false;
    }

    public void advance() {

    }

    public String tokenType() {

    }

    public String keyWord() {

    }

    public char symbol() {

    }

    public String identifier() {

    }

    public Integer intVal() {

    }

    public String stringVal() {

    }
    */

}
