package au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.TempMess;


public class Tokenizer {

    private String buffer;          // String to be transformed into tokens each time next() is called.
    private Token currentToken;     // The current token. The next token is extracted when next() is called.


    /**
     * Tokenizer class constructor
     * The constructor extracts the first token and save it to currentToken
     */
    public Tokenizer(String text) {
        buffer = text;          // save input text (string)
        next();                 // extracts the first token.
    }

    /**
     * This function will find and extract a next token from {@code _buffer} and
     * save the token to {@code currentToken}.
     */
    public void next() {
        if (buffer.isEmpty()) {
            currentToken = null;    // if there's no string left, set currentToken null and return
            return;
        }


        int i = 0;
        char currChar = buffer.charAt(0);
        while (i < buffer.length() - 1 && currChar != '%') {
            i++;
            currChar = buffer.charAt(i);
        }

        if (i == buffer.length() - 1 && currChar != '%') {
            currentToken = new Token(buffer, Token.Type.TEXT);
        } else {
            String next = buffer.substring(0, i);
            switch (next) {
                case "FRI_USERNAME":
                    currentToken = new Token(next, Token.Type.FRI_USERNAME);
                    break;
                case "MESSTIME_TO_CURR":
                    currentToken = new Token(next, Token.Type.MESSTIME_TO_CURR);
                    break;
                case "MY_USERNAME":
                    currentToken = new Token(next, Token.Type.MY_USERNAME);
                    break;
                case "MY_EMAILADDRESS":
                    currentToken = new Token(next, Token.Type.MY_EMAILADDRESS);
                    break;
                default:
                    currentToken = new Token(next, Token.Type.TEXT);
            }
        }

        int tokenLen = currentToken.getToken().length();
        if (currChar == '%')
            buffer = buffer.substring(tokenLen + 1);
        else
            buffer = buffer.substring(tokenLen);
    }

    /**
     * Returns the current token extracted by {@code next()}
     * **** please do not modify this part ****
     *
     * @return type: Token
     */
    public Token current() {
        return currentToken;
    }

    /**
     * Check whether there still exists another tokens in the buffer or not
     * **** please do not modify this part ****
     *
     * @return type: boolean
     */
    public boolean hasNext() {
        return currentToken != null;
    }
}
