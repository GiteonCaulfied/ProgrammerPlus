package au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.Search;

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

    /**
     * take the intersection of the term's list and exp's list
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public ArrayList<Post> evaluate() {

        ArrayList<Post>list1 = term.evaluate();
        ArrayList<Post>list2 = exp.evaluate();
        if (list1==null){
            return list2;
        }else if (list2==null){
            return list1;
        }
        ArrayList<Post> intersection = (ArrayList<Post>) list1.stream().filter(item -> list2.contains(item)).collect(toList());
        return intersection;

    }
}
