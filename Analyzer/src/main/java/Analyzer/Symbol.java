package Analyzer;

public class Symbol {
    public VarKind Kind;
    public String Type;
    public int Index;

    public Symbol(String type, VarKind kind, int index) {
        this.Kind = kind;
        this.Type = type;
        this.Index = index;
    }

    public String toString() {
        return this.Kind.toString() + " " + this.Type + " " +  Index;
    }
}
