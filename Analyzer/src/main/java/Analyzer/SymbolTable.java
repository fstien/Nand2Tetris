package Analyzer;

public class SymbolTable {

    public SymbolTable() {}

    public void define(String name, String type, VarKind kind) {

    }

    public VarKind KindOf(String name) {
        return VarKind.ARG;
    }

    public String TypeOf(String name) {
        return "Hello world";
    }

    public int IndexOf(String name) {
        return 0;
    }

}
