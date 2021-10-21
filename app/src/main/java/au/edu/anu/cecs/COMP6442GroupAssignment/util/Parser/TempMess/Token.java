package au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.TempMess;

public class Token {
    // Fields of the class Token.
    private final String token; // Token representation in String form.
    private final Type type;    // Type of the token.

    public Token(String token, Token.Type type) {
        this.token = token;
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public Token.Type getType() {
        return type;
    }


    public enum Type {TEXT, FRI_USERNAME, MESSTIME_TO_CURR, MY_USERNAME, MY_EMAILADDRESS}

    public static class IllegalTokenException extends IllegalArgumentException {
        public IllegalTokenException(String errorMessage) {
            super(errorMessage);
        }
    }
}
