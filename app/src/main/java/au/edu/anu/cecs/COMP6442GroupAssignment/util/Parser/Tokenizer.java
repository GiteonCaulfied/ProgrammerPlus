package au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Tokenizer {

    LinkedList<String> tokens;
    private Token currentToken;     // The current token. The next token is extracted when next() is called.

    public Tokenizer(String text) {
        String temp = text.replaceAll(" ","");
        tokens.addAll( Arrays.asList(text.split(";")));
        next();                 // extracts the first token.
    }


    public void next() {



        if (tokens.isEmpty()) {
            currentToken = null;    // if there's no string left, set currentToken null and return
            return;
        }
    }
}
