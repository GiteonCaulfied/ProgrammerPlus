package au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.Search;

import android.widget.Toast;

public class Parser {
    public static class IllegalProductionException extends IllegalArgumentException {
        public IllegalProductionException(String errorMessage) {
            super(errorMessage);
        }
    }

    // The tokenizer (class field) this parser will use.
    Tokenizer tokenizer;

    int bracket = 0;
    Integer keys =0;
    Integer finished=0;

    /**
     * Parser class constructor
     * Simply sets the tokenizer field.
     * **** please do not modify this part ****
     */
    public Parser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;

    }

    public void inc(){
        this.finished++;
    }
    public boolean whetherFinished(){
        if (finished.equals(keys)){
            return true;
        }
        return false;
    }


    /**
     * Adheres to the grammar rule:
     * <exp>    ::= <term> | <term> & <exp> | <term> | <exp>
     *
     * @return type: Exp.
     */
    public Exp parseExp() {

        Exp term = parseTerm();
        if (!tokenizer.hasNext()){
            if (bracket!=0){
                throw new IllegalProductionException("");
            }
            return term;
        }


        if (tokenizer.current().getType()== Token.Type.AND){
            tokenizer.next();

            Exp output = new AndExp(term,parseExp());
            if (!tokenizer.hasNext() && bracket!=0){
                throw new IllegalProductionException("");
            }
            return output;
        }
        if (tokenizer.current().getType()== Token.Type.OR){
            tokenizer.next();

            Exp output =new OrExp(term,parseExp());
            if (!tokenizer.hasNext() && bracket!=0){
                throw new IllegalProductionException("");
            }

            return output;
        }
        if ( tokenizer.current().getType()== Token.Type.RBRA){
            if (bracket <=0){
                throw new IllegalProductionException("");
            }
            bracket --;
            tokenizer.next();

            return term;
        }
        throw new IllegalProductionException("");


    }
    public Exp parseTerm() {

        if (tokenizer.current().getType()==Token.Type.LBRA){
            bracket++;
            tokenizer.next();
            if (!tokenizer.hasNext()){
                throw new IllegalProductionException("");
            }
            Exp exp =  parseExp();

            return exp;
        }
        String[] raw = tokenizer.current().getToken().split("=");
        Exp keyExp = null;
        if (raw.length==2) {
            switch (tokenizer.current().getType()){
                case Id:
                    keyExp = new KeyExp("pid",raw[1],this);
                    keys++;
                    break;
                case Title:
                    keyExp = new KeyExp("title",raw[1],this);
                    keys++;
                    break;
                case Author:
                    keyExp = new KeyExp("author",raw[1],this);
                    keys++;
                    break;
                case Tag:
                    keyExp = new KeyExp("tags",raw[1],this);
                    keys++;
                    break;
                case Illegal:
                    keyExp = new NonExp();
                    Toast.makeText(MyApplication.context,
                            "Illegal syntax "+tokenizer.current().getToken(), Toast.LENGTH_SHORT).show();
                    break;
            }

            tokenizer.next();
        }else {
            throw new IllegalProductionException("");
        }

        return keyExp;


    }

}
