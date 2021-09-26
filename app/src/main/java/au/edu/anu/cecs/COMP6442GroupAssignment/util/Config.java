package au.edu.anu.cecs.COMP6442GroupAssignment.util;

import java.util.HashMap;

public class Config {
    private HashMap<String, Profile> profiles;//package the map

    public HashMap<String, Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(HashMap<String, Profile> profiles) {
        this.profiles = profiles;
    }
}



