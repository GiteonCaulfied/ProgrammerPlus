package au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.HeatSpeechParser;



public class Parser {
    public static class IllegalProductionException extends IllegalArgumentException {
        public IllegalProductionException(String errorMessage) {
            super(errorMessage);
        }
    }

    // The tokenizer (class field) this parser will use.
    Tokenizer tokenizer;

    public Parser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    /**
     * to parse the given String if the given token is illegal replace it with "***"
     * @return the fixed String
     */
    public String parse(){
        StringBuilder output = new StringBuilder();
        while (tokenizer.hasNext()) {
            if (tokenizer.current().getType() == Token.Type.Illegal){
                output.append("***");
                tokenizer.next();
            }else {
                output.append(tokenizer.current().getToken());
                tokenizer.next();
            }
        }
        return output.toString();
    }
}
