package au.edu.anu.cecs.COMP6442GroupAssignment.util;

import java.util.HashMap;

public class FriendRequest {
    /**
     * This friend request class. We wrote it for using Firebase
     */
    private String uid;
    private String name;

    public FriendRequest() {}

    public FriendRequest(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    /**
     * Transform the FriendRequest to a Map to upload to the Firebase.
     */
    public HashMap<String, Object> toMap() {
        HashMap<String, Object> results = new HashMap<>();
        results.put(uid, name);
        return results;
    }
}
