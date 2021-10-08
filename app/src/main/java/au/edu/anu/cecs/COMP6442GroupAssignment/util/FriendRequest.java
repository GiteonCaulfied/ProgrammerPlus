package au.edu.anu.cecs.COMP6442GroupAssignment.util;

import java.util.HashMap;

public class FriendRequest {
    private String uid;
    private String name;

    public FriendRequest() {}

    public FriendRequest(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> results = new HashMap<>();
        results.put(uid, name);
        return results;
    }
}
