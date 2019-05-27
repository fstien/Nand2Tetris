package Analyzer;

public class Token<T> {
    public TokenType Type;
    public T Value;

    public Token(TokenType type, T value) {
        this.Type = type;
        this.Value = value;
    }

    public String toString() {
        return "<" + this.Type.toString() + "> " + this.Value.toString() + " </" + this.Type.toString() + ">";
    }
}
