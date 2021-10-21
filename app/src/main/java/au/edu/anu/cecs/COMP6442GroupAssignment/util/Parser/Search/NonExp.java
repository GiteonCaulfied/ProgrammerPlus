package au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.Search;

import java.util.ArrayList;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.Post;

public class NonExp extends Exp {
    public NonExp() {
    }

    /**
     * this Exp represent an illegal expression when evaluate it return a null
     * @return
     */
    @Override
    public ArrayList<Post> evaluate() {
        return null;
    }
}
