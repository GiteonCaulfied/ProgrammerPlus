package au.edu.anu.cecs.COMP6442GroupAssignment.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class UserManager {
    /**
     * This is a class to manage users.
     * We will store three hashmaps here:
     *  (1) from id to email;
     *  (2) from email to id;
     *  (3) from id to name;
     *
     * This class is not mandatory, but it will make our
     * app bandwidth-friendly.
     */

    private static UserManager instance;
    private final HashMap<String, String> id_email_map;
    private final HashMap<String, String> email_id_map;
    private final HashMap<String, String> id_name_map;
    private final HashMap<String, Boolean> id_por_map;

    public UserManager() {
        id_email_map = new HashMap<>();
        id_name_map = new HashMap<>();
        email_id_map = new HashMap<>();
        id_por_map = new HashMap<>();
    }

    /**
     * Singleton Pattern
     */
    public static UserManager getInstance() {
        if (instance == null)
            instance = new UserManager();
        return instance;
    }

    /**
     * Add the user to the UserManager using Profile.
     *
     * @param p Profile of the user
     */
    public void addUser(Profile p) {
        id_email_map.put(p.getUid(), p.getEmail());
        if (p.getName() == null)
            id_name_map.put(p.getUid(), "userAA");
        else
            id_name_map.put(p.getUid(), p.getName());
        email_id_map.put(p.getEmail(), p.getUid());
        id_por_map.put(p.getUid(), p.isPortraitUploaded());
    }

    public String getEmailFromID(String id) {
        return id_email_map.get(id);
    }

    public String getNameFromID(String id) {
        return id_name_map.get(id);
    }

    public String getIDFromEmail(String email) {
        return email_id_map.get(email);
    }

    public boolean getPorFromID(String id) {
        return id_por_map.get(id);
    }

    public boolean emailIsValid(String email) {
        return email_id_map.containsKey(email);
    }

    /**
     * Randomly get an ID from the UserManager.
     */
    public String getRandomID() {
        // Random Number to randomly select ID
        int min = 0;
        int max = id_name_map.keySet().toArray().length - 1;

        int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
        return (String) id_name_map.keySet().toArray()[random_int];
    }

    /**
     * Get a list of ID from the UserManager.
     *
     * @return a set of id
     */
    public Set<String> getIDList() {
        Set<String> res = id_name_map.keySet();
        return res;
    }

    /**
     * Get a random ID list, we wrote this for generating
     * data for our app. Now that we have already done that,
     * the function is not called.
     *
     * @return a list of id
     */
    public ArrayList<String> getRandomIDList() {
        Random random = new Random();
        int num = random.nextInt(100);
        Set<String> ids = new HashSet<>();
        for (int i = 0; i < num; i++) {
            ids.add(getRandomID());
        }
        ArrayList<String> res = new ArrayList<>(ids);

        return res;
    }
}
