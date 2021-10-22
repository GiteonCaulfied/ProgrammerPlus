package au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.DataStructure.AVLTree;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DataGenerator.JsonUtils;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.Search.MyApplication;

public class SwearWordsDAO {

    private AVLTree<String> swearWords;
    private static SwearWordsDAO instance;

    private SwearWordsDAO() {
        this.swearWords = JsonUtils.getInstance().getSwearWordsTree(MyApplication.context);
    }

    /**
     * Singleton Pattern
     */
    public static SwearWordsDAO getInstance() {
        if (instance == null) {
            instance = new SwearWordsDAO();
        }
        return instance;
    }

    /**
     * check whether the given word is in the swearword tree
     * @param s word
     * @return
     */
    public boolean contains(String s) {
        if (swearWords.search(s) != null) {
            return true;
        }
        return false;

    }

    /**
     * Delete a word from the swearword tree
     * @param s word
     * @return whether it was deleted
     */
    public boolean delete (String s){
        if (swearWords.delete(s)){
            JsonUtils.getInstance().saveSwearWordsTree(MyApplication.context,swearWords);
            return true;
        }else {
            return false;
        }
    }

    /**
     * Add a new word into the swearword tree
     * @param s word
     * @return whether it was added successfully
     */
    public boolean add (String s){
        if (contains(s)){
            return false;
        }else {
            swearWords.insert(s);
            JsonUtils.getInstance().saveSwearWordsTree(MyApplication.context,swearWords);
            return true;
        }

    }
}
