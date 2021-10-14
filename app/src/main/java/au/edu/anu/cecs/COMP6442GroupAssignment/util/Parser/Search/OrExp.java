package au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.Search;

import static java.util.stream.Collectors.toList;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.Post;

public class OrExp extends Exp{

    private Exp term;
    private Exp exp;

    public OrExp(Exp term, Exp exp) {
        this.term = term;
        this.exp = exp;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public ArrayList<Post> evaluate() {
        ArrayList<Post>list1 = term.evaluate();
        ArrayList<Post>list2 = exp.evaluate();

        ArrayList<Post> listAll = (ArrayList<Post>) list1.parallelStream().collect(toList());
        ArrayList<Post> listAll2 = (ArrayList<Post>) list2.parallelStream().collect(toList());
        listAll.addAll(listAll2);
        return listAll;

    }
}
