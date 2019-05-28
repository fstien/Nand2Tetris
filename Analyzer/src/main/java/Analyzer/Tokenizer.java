package Analyzer;

import com.sun.corba.se.impl.oa.toa.TOA;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
public class Tokenizer {

    private List<String> lines = new ArrayList<>();
    private List<Token> tokens = new ArrayList<Token>();

    private List<String> splitList = new ArrayList<>();

    private List<String> keywords = Arrays.asList("class", "constructor", "function", "method", "field", "static", "var", "int", "char", "boolean", "void", "true", "false", "null", "this", "let", "do", "if", "else", "while", "return");

    private List<Character> symbols = Arrays.asList('{', '}', '(', ')', '[', ']', '.', ',', ';', '+', '-', '*', '/', '&', '|', ',', '<', '>', '=', '~');

    private FileWriter FW;

    private boolean currentComment = false;

    public Tokenizer(String fileName) {
        String baseDir = "/Users/francois.stiennon/Desktop/nand2tetris/GitHub/Analyzer/src/main/java/Analyzer";
        try {
            File f = new File(baseDir + "/Jack/" + fileName + ".jack");
            Scanner file = new Scanner(f);
            this.parseLines(file);
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for(String line : this.lines) {
            this.splitLine(line);
        }

        this.parseTokens();

        try {
            this.FW = new FileWriter(baseDir + "/Out/" + fileName + ".xml");
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

    private void splitLine(String line) {
        List<String> spaceSplit = this.lineSplit(line);

        for(String spaceStr : spaceSplit) {
            String strAdd = "";

            for (int i = 0; i < spaceStr.length(); i++){
                char c = spaceStr.charAt(i);

                if(this.symbols.contains(c)) {

                    if(strAdd.length() > 0) {
                        this.splitList.add(strAdd);
                        strAdd = "";
                    }

                    this.splitList.add("" + c);
                    continue;
                }

                strAdd += c;
            }

            if(strAdd.length() > 0) {
                this.splitList.add(strAdd);
            }
        }

    }

    private List<String> lineSplit(String line) {
        List<String> list = new ArrayList<String>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(line);
        while (m.find())
            list.add(m.group(1));

        return list;
    }

    private void parseTokens() {
        for(String tokStr : this.splitList) {
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
