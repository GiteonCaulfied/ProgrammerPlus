package au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

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
        buffer = buffer.trim();     // remove whitespace

        if (buffer.isEmpty()) {
            currentToken = null;    // if there's no string left, set currentToken null and return
            return;
        }
        Character[] chars = {'&','|','(',')'};
        ArrayList<Character> operators = new ArrayList<Character>(Arrays.asList(chars));
//        char t1 = buffer.charAt(0);
//        if (t1 != '+' && t1!='-' && t1!= '*'&& t1!='/'&& t1!=' '
//                && t1!='(' && t1!= ')' && !Character.isDigit(t1)){
//            throw new Token.IllegalTokenException("");
//        }

        char firstChar = buffer.charAt(0);
        if (firstChar == '&')
            currentToken = new Token("+", Token.Type.AND);
        if (firstChar == '|')
            currentToken = new Token("-", Token.Type.OR);
        if (firstChar == '(')
            currentToken = new Token("(", Token.Type.LBRA);
        if (firstChar == ')')
            currentToken = new Token(")", Token.Type.RBRA);


        int pos = 0;
        StringBuffer instructions = new StringBuffer();
        buffer = buffer.trim();
        while ( pos < buffer.length() && !operators.contains(buffer.charAt(pos))){

            instructions.append(buffer.charAt(pos));
            pos++;
        }
        String instruction = instructions.toString();
        if (instructions.length()!=0){
            if (instruction.startsWith("Author=")){
                currentToken = new Token(instruction, Token.Type.Author);
            }else if (instruction.startsWith("Title=")){
                currentToken = new Token(instruction, Token.Type.Title);
            }else if (instruction.startsWith("Tag=")){
                currentToken = new Token(instruction, Token.Type.Tag);
            }else if (instruction.startsWith("Id=")){
                currentToken = new Token(instruction, Token.Type.Id);
             }else {
                throw new Token.IllegalTokenException("");
            }
        }

        int tokenLen = currentToken.getToken().length();

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
