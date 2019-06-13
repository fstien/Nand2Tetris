package Analyzer;

public class Symbol {
    public String Type;
    public VarKind Kind;
    public int Index;

    public Symbol(String type, VarKind kind, int index) {
        this.Type = type;
        this.Kind = kind;
        this.Index = index;
    }
}
