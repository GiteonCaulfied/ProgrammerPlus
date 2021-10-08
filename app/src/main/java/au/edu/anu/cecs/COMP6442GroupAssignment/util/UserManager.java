package au.edu.anu.cecs.COMP6442GroupAssignment.util;

import java.util.HashMap;

public class UserManager {
    private HashMap<String, String> id_email_map;
    private HashMap<String, String> email_id_map;
    private HashMap<String, String> id_name_map;
    private static UserManager instance;

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
}
