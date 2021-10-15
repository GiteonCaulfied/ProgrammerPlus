package au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.Search;



import static android.app.PendingIntent.getActivity;

import java.util.ArrayList;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserPostDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Post;

public class KeyExp extends Exp{

    private String keyword;
    private String field;

    private ArrayList<Post> postList;
    Parser parent;

    public KeyExp( String field,String key ,Parser parent ){
        keyword = key;
        this.field=field;
        UserPostDAO postDAO = UserPostDAO.getInstance();
        this.parent =parent;
        postList=postDAO.getPostList(field,keyword,parent);

    }

    @Override
    public ArrayList<Post> evaluate() {
        if (postList==null){
            return new ArrayList<Post>();
        }
        return postList;

    }
}
