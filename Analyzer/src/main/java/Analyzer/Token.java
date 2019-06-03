package Analyzer;

import java.util.HashMap;
import java.util.Map;

public class Token<T> {
    public TokenType Type;
    public T Value;

    private static final Map<Character, String> specialChar = new HashMap<Character, String>() {{
        put('<', "&lt;");
        put('>', "&gt;");
        put('"', "&quot;");
        put('&', "&amp;");
    }};

    public Token(TokenType type, T value) {
        this.Type = type;
        this.Value = value;
    }

    public String toString() {

        String value = this.Value.toString();
        if(specialChar.containsKey(value.charAt(0))) {
            value = specialChar.get(value.charAt(0));
        }

        return "<" + this.Type.toString() + ">" + value + "</" + this.Type.toString() + ">";
    }

    public String StringValue() {
        return this.Value.toString();
    }
}
