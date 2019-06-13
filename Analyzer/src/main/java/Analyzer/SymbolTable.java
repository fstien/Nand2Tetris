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

    public void define(String name, String type, VarKind kind) {
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

    private Symbol getSymbol(String name) throws Exception {
        Symbol retSym = subroutineScore.get(name);
        if(retSym != null) {
            return retSym;
        }
        else {
            retSym = classScope.get(name);
            if(retSym != null) {
                return retSym;
            }
            else {
                throw new Exception("Symbol not found!");
            }
        }
    }

    public VarKind KindOf(String name) throws Exception {
        Symbol sym = this.getSymbol(name);
        return sym.Kind;
    }

    public String TypeOf(String name) throws Exception {
        Symbol sym = this.getSymbol(name);
        return sym.Type;
    }

    public int IndexOf(String name) throws Exception {
        Symbol sym = this.getSymbol(name);
        return sym.Index;
    }

}
