package au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.HateSpeechParser;

public class Token {

    public enum Type {Legal,Illegal,Space}


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

}