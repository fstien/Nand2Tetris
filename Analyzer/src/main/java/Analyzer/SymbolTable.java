package Analyzer;

import java.util.Hashtable;

public class SymbolTable {

    private Hashtable<String, Symbol> classScope = new Hashtable();
    private Hashtable<String, Symbol> subroutineScore = new Hashtable();

    private int StaticCounter = 0;
    private int FieldCounter = 0;
    private int ArgumentCounter = 0;
    private int VarCounter = 0;

    public SymbolTable() {}

    public void define(VarKind kind, String type, String name) {
        if(kind == VarKind.STATIC) {
            this.classScope.put(name, new Symbol(type, kind, this.StaticCounter));
            this.StaticCounter++;
        }
        else if(kind == VarKind.FIELD) {
            this.classScope.put(name, new Symbol(type, kind, this.FieldCounter));
            this.FieldCounter++;
        }
        else if(kind == VarKind.ARG) {
            this.subroutineScore.put(name, new Symbol(type, kind, this.ArgumentCounter));
            this.ArgumentCounter++;
        }
        else if(kind == VarKind.VAR) {
            this.subroutineScore.put(name, new Symbol(type, kind, this.VarCounter));
            this.VarCounter++;
        }
    }

    public void define(SymbolBuilder builder) {
        this.define(builder.kind, builder.type, builder.name);
    }

    private SymbolLookupResult getSymbol(String name) {
        Symbol retSym = subroutineScore.get(name);
        if(retSym != null) {
            return new SymbolLookupResult(true, retSym);
        }
        else {
            retSym = classScope.get(name);
            if(retSym != null) {
                return new SymbolLookupResult(true, retSym);
            }
            else {
                return new SymbolLookupResult(false, null);
            }
        }
    }

    public VarKind KindOf(String name) {
        return this.getSymbol(name).Symbol.Kind;
    }

    public String TypeOf(String name) {
        return this.getSymbol(name).Symbol.Type;
    }

    public int IndexOf(String name) {
        return this.getSymbol(name).Symbol.Index;
    }

}
