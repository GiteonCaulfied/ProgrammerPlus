package au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.DataStructure.AVLTree;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DataGenerator.JsonUtils;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.MyApplication;

public class SwearWordsDAO {

    private AVLTree<String> swearWords;
    private static SwearWordsDAO instance;

    private SwearWordsDAO() {
        this.swearWords = JsonUtils.getInstance().getSwearWordsTree(MyApplication.context);
    }

    public static SwearWordsDAO getInstance() {
        if (instance == null) {
            instance = new SwearWordsDAO();
        }
        return instance;
    }

    public boolean contains(String s) {
        if (swearWords.search(s) != null) {
            return true;
        }
        return false;

    }
}
