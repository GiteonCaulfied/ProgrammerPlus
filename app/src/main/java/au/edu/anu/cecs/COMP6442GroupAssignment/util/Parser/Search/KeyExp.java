package au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.Search;



import static android.app.PendingIntent.getActivity;

import java.util.ArrayList;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserPostDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Post;

public class KeyExp extends Exp{

    private String keyword;
    private String field;

    private ArrayList postList;
    Parser parent;

    public KeyExp( String field,String key ,Parser parent ){
        keyword = key;
        this.field=field;

        this.parent =parent;
        if (parent!=null){
            UserPostDAO postDAO = UserPostDAO.getInstance();
            postList=postDAO.getPostList(field,keyword,parent);
        }


    }

    /**
     * for test
     * @param postList
     */
    public void setPostList(ArrayList<Post> postList) {
        this.postList = postList;
    }

    /**
     * base case return the post lists
     * @return
     */
    @Override
    public ArrayList<Post> evaluate() {
        if (postList==null){
            return new ArrayList<Post>();
        }
        return postList;

    }
}
