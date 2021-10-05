package au.edu.anu.cecs.COMP6442GroupAssignment.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.Tree.AVLTree;

public class Profile {
    private String uid;
    private String name;
    private String email;
    private long creation_time;
    private ArrayList<String> posts;
    private ArrayList<String> friends;
    private ArrayList<String> blocked;
    private String intro;
    private boolean portraitUploaded;

    public Profile() {
        // Default constructor required for calls to DataSnapshot.getValue(Profile.class)
    }

    public Profile(String uid, String email, String name, String intro) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.creation_time = System.currentTimeMillis();
        this.posts = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.blocked = new ArrayList<>();
        this.intro = intro;
        this.portraitUploaded = false;
    }

    public Profile(Map<String, Object> m) {
        this.uid = (String) m.get("uid");
        this.email = (String) m.get("email");
        this.name = (String) m.get("name");
        this.creation_time = (long) m.get("date");
        this.posts = (ArrayList<String>) m.get("posts");
        this.friends = (ArrayList<String>) m.get("friends");
        this.blocked = (ArrayList<String>) m.get("blocked");
        this.intro = (String) m.get("intro");
        this.portraitUploaded = (boolean) m.get("portraitUploaded");
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("email", email);
        result.put("name", name);
        result.put("date", creation_time);
        result.put("posts", posts);
        result.put("friends", friends);
        result.put("blocked", blocked);
        result.put("intro", intro);
        result.put("portraitUploaded",portraitUploaded);

        return result;
    }

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

    public void setIntro( String new_intro ){
        this.intro = new_intro;
    }

    public Boolean isPortraitUploaded(){
        return portraitUploaded;
    }

    public Boolean friendContain(String name) {
        if (friends == null) return false;
        return friends.contains(name);
    }

    public void setPortraitUploadedStatus(){
        this.portraitUploaded = true;
    }
}
