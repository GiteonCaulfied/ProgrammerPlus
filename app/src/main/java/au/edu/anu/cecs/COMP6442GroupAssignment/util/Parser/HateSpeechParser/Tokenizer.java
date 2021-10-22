package au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.HateSpeechParser;

import java.util.Locale;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.SwearWordsDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DataStructure.AVLTree;

public class Tokenizer {

    private String buffer;          // String to be transformed into tokens each time next() is called.
    private Token currentToken;     // The current token. The next token is extracted when next() is called.
    private SwearWordsDAO swearWords;


    /**
     * Tokenizer class constructor
     * The constructor extracts the first token and save it to currentToken
     */
    public Tokenizer(String text) {
        swearWords = (SwearWordsDAO) SwearWordsDAO.getInstance();
        buffer = text;          // save input text (string)
        next();                 // extracts the first token.
    }

    public Tokenizer(AVLTree<String> swearWordsTree, String text){
        swearWords= SwearWordsDAO.getInstanceOffline(swearWordsTree);
        buffer = text;          // save input text (string)
        next();
    }

    /**
     * This function will find and extract a next token from {@code _buffer} and
     * save the token to {@code currentToken}.
     */
    public void next() {
//        buffer = buffer.trim();     // remove whitespace

        if (buffer.isEmpty()) {
            currentToken = null;    // if there's no string left, set currentToken null and return
            return;
        }

        char firstChar = buffer.charAt(0);
        if (firstChar == ' ')
            currentToken = new Token(" ", Token.Type.Space);


        int pos = 0;
        StringBuffer wordBF = new StringBuffer();
//        buffer = buffer.trim();
        while (pos < buffer.length() && buffer.charAt(pos) != ' ') {

            wordBF.append(buffer.charAt(pos));
            pos++;
        }
        String word = wordBF.toString();
        if (wordBF.length() != 0) {
                String temp = new String(word);
                String test = word.toLowerCase(Locale.ROOT);
                test = test.
           replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");

            if (swearWords.contains(test)) {
                currentToken = new Token(word, Token.Type.Illegal);
            } else {
                currentToken = new Token(word, Token.Type.Legal);
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
