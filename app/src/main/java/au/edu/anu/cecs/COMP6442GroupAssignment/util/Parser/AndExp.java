package au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser;

import static java.util.stream.Collectors.toList;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.Post;

public class AndExp extends Exp{
    private Exp term;
    private Exp exp;

    public AndExp(Exp term, Exp exp) {
        this.term = term;
        this.exp = exp;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public ArrayList<Post> evaluate() {
        ArrayList<Post>list1 = term.evaluate();
        ArrayList<Post>list2 = exp.evaluate();
        ArrayList<Post> intersection = (ArrayList<Post>) list1.stream().filter(item -> list2.contains(item)).collect(toList());
        return intersection;

    }
}
