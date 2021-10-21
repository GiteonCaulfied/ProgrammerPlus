package au.edu.anu.cecs.COMP6442GroupAssignment.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Profile {
    /**
     * A user's profile, including his id, name, email, friends, intro and settings
     */

    private String uid;
    private String name;
    private String email;
    private long creation_time;
    private ArrayList<String> friends;
    private ArrayList<String> blocked;
    private String intro;
    private boolean portraitUploaded;
    private boolean onlyFriMess;

    public Profile() {
        // Default constructor required for calls to DataSnapshot.getValue(Profile.class)
    }

    /**
     * Default Constructor when creating a new profile.
     */
    public Profile(String uid, String email, String name, String intro, Boolean onlyFriMess) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.creation_time = System.currentTimeMillis();
        this.friends = new ArrayList<>();
        this.blocked = new ArrayList<>();
        this.intro = intro;
        this.portraitUploaded = false;
        this.onlyFriMess = onlyFriMess;
    }

    /**
     * Constructor using map (used when retrieve profiles from Firebase).
     */
    public Profile(Map<String, Object> m) {
        this.uid = (String) m.get("uid");
        this.email = (String) m.get("email");
        this.name = (String) m.get("name");
        this.creation_time = (long) m.get("date");
        this.friends = (ArrayList<String>) m.get("friends");
        this.blocked = (ArrayList<String>) m.get("blocked");
        this.intro = (String) m.get("intro");
        this.portraitUploaded = (boolean) m.get("portraitUploaded");
        this.onlyFriMess = (boolean) m.get("onlyFriMess");
    }

    /**
     * Transform the Profile to a Map to upload to the Firebase.
     */
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("email", email);
        result.put("name", name);
        result.put("date", creation_time);
        result.put("friends", friends);
        result.put("blocked", blocked);
        result.put("intro", intro);
        result.put("portraitUploaded",portraitUploaded);
        result.put("onlyFriMess", onlyFriMess);

        return result;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getIntro() {
        return intro;
    }

    public String getUid() {
        return uid;
    }

    public boolean isOnlyFriMess() {
        return onlyFriMess;
    }

    // Set some fields
    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIntro(String new_intro ){
        this.intro = new_intro;
    }

    public void setOnlyFriMess(boolean onlyFriMess) {
        this.onlyFriMess = onlyFriMess;
    }

    public boolean addNewFriend(String uid) {
        return friends.add(uid);
    }

    public boolean addNewBlock(String uid) {
        return blocked.add(uid);
    }

    public boolean cancelBlock(String uid) {
        return blocked.remove(uid);
    }

    public Boolean isPortraitUploaded(){
        return portraitUploaded;
    }

    // Determine whether a name is my friend
    public Boolean friendContain(String name) {
        if (friends == null) return false;
        return friends.contains(name);
    }

    // Determine whether someone is blocked by me
    public Boolean blockContain(String name) {
        if (blocked == null) return false;
        return blocked.contains(name);
    }

    // Determine whether I uploaded my portrait
    public void setPortraitUploadedStatus(){
        this.portraitUploaded = true;
    }
}
