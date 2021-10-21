package au.edu.anu.cecs.COMP6442GroupAssignment.util.DataGenerator;

import java.util.HashMap;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.Profile;

public class Config {
    private HashMap<String, Profile> profiles;//package the map

    public HashMap<String, Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(HashMap<String, Profile> profiles) {
        this.profiles = profiles;
    }
}



