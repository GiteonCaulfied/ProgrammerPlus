package au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.TempMess;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.Profile;

public class Parser {
    // The tokenizer (class field) this parser will use.
    Tokenizer tokenizer;
    Profile currentUser;
    Profile friend;
    long lastTime;
    /**
     * Parser class constructor
     * Simply sets the tokenizer field.
     * **** please do not modify this part ****
     */
    public Parser(Tokenizer tokenizer, Profile currentUser, Profile friend, long lastTime) {
        this.tokenizer = tokenizer;
        this.currentUser = currentUser;
        this.friend = friend;
        this.lastTime = lastTime;
    }

    public String parse() {
        StringBuilder res = new StringBuilder();
        while (tokenizer.hasNext()) {
            Token curr = tokenizer.current();
            tokenizer.next();
            switch (curr.getType()) {
                case TEXT:
                    res.append(curr.getToken());
                    break;
                case MY_USERNAME:
                    res.append(currentUser.getName());
                    break;
                case FRI_USERNAME:
                    res.append(friend.getName());
                    break;
                case MY_EMAILADDRESS:
                    res.append(currentUser.getEmail());
                    break;
                case MESSTIME_TO_CURR:
                    int d = (int) Math.round((int) (System.currentTimeMillis() - lastTime) / 1000.0 / 3600.0);
                    res.append(d + " hour(s)");
                    break;
            }
        }
        return res.toString();
    }
}
