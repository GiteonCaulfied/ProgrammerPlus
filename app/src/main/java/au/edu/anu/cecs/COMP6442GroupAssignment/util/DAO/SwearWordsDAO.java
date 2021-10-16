package au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO;

import java.util.Map;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.DataStructure.AVLTree;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.JsonUtils;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.MyApplication;

public class SwearWordsDAO implements UserActivityDaoInterface {

    private AVLTree<String> swearWords;
    private static SwearWordsDAO instance;

    private SwearWordsDAO() {
        this.swearWords = JsonUtils.getInstance().getSwearWordsTree(MyApplication.context);
    }

    //    @Override
    public static UserActivityDaoInterface getInstance() {
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

    @Override
    public void getData() {

    }

    @Override
    public void update(String key, Map<String, Object> newValues) {

    }

    @Override
    public void create(String key, Map<String, Object> newValues) {

    }

    @Override
    public void delete() {

    }

    @Override
    public void clear() {

    }
}
