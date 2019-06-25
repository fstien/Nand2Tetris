package Analyzer;

public class SymbolLookupResult {
    public boolean Found;
    public Symbol Symbol;

    public SymbolLookupResult(boolean found, Symbol symbol) {
        this.Found = found;
        this.Symbol = symbol;
    }
}
