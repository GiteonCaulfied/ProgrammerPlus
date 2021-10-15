package au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.Search;

import java.util.ArrayList;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.Post;

public class NonExp extends Exp {
    public NonExp() {
    }

    @Override
    public ArrayList<Post> evaluate() {
        return new ArrayList<Post>();
    }
}
