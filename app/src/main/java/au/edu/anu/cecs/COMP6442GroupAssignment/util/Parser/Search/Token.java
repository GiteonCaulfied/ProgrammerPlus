package au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.Search;

public class Token {

    // Fields of the class Token.
    private final String token; // Token representation in String form.
    private final Type type;    // Type of the token.

    public Token(String token, Type type) {
        this.token = token;
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public Type getType() {
        return type;
    }


    public enum Type {Title, Author, Tag, Id, LBRA, RBRA, AND, OR, Illegal}

    public static class IllegalTokenException extends IllegalArgumentException {
        public IllegalTokenException(String errorMessage) {
            super(errorMessage);
        }
    }

}
