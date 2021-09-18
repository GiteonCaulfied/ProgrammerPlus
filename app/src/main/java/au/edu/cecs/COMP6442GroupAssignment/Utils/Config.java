package au.edu.cecs.COMP6442GroupAssignment.Utils;

import java.util.HashMap;
import java.util.Map;

public class Config {
    private HashMap<String, Profile> profiles;//package the map

    public HashMap<String, Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(HashMap<String, Profile> profiles) {
        this.profiles = profiles;
    }
}



