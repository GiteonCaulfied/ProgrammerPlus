package au.edu.anu.cecs.COMP6442GroupAssignment.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class UserManager {
    private static UserManager instance;
    private final HashMap<String, String> id_email_map;
    private final HashMap<String, String> email_id_map;
    private final HashMap<String, String> id_name_map;

    public UserManager() {
        id_email_map = new HashMap<>();
        id_name_map = new HashMap<>();
        email_id_map = new HashMap<>();
    }

    public static UserManager getInstance() {
        if (instance == null)
            instance = new UserManager();
        return instance;
    }

    public void addUser(Profile p) {
        id_email_map.put(p.getUid(), p.getEmail());
        if (p.getName() == null)
            id_name_map.put(p.getUid(), "userAA");
        else
            id_name_map.put(p.getUid(), p.getName());
        email_id_map.put(p.getEmail(), p.getUid());
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

    public boolean emailIsValid(String email) {
        return email_id_map.containsKey(email);
    }

    public String getRandomID() {
        // Random Number to randomly select ID
        int min = 0;
        int max = id_name_map.keySet().toArray().length - 1;

        int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
        return (String) id_name_map.keySet().toArray()[random_int];
    }

    public Set<String> getIDList() {
        Set<String> res = id_name_map.keySet();
        return res;
    }

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
