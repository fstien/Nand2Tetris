package Analyzer;

public class SymbolBuilder {

    public VarKind kind;
    public String type;
    public String name;

    public SymbolBuilder ofKind(String kind) throws Exception {
        switch (kind) {
            case "static":
                this.kind = VarKind.STATIC;
                break;
            case "field":
                this.kind = VarKind.FIELD;
                break;
            case "arg":
                this.kind = VarKind.ARG;
                break;
            case "var":
                this.kind = VarKind.VAR;
                break;
            default:
                throw new Exception("Symbol kind not found");
        }

        return this;
    }

    public SymbolBuilder ofType(String type) {
        this.type = type;
        return this;
    }

    public SymbolBuilder ofName(String name) {
        this.name = name;
        return this;
    }

    public void clear() {
        this.kind = null;
        this.type = null;
        this.name = null;
    }
}
