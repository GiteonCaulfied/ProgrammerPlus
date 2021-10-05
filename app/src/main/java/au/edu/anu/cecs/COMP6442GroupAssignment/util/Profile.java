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
    private Date creation_date;
    private ArrayList<Integer> posts;
    private AVLTree<String> friends;
    private AVLTree<String> blocked;
    private String intro;
    private boolean portraitUploaded;

    public Profile() {
        // Default constructor required for calls to DataSnapshot.getValue(Profile.class)
    }

    public Profile(String uid, String email, String name, String intro) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.creation_date = new Date();
        this.posts = new ArrayList<Integer>();
        this.friends = new AVLTree<>();
        this.blocked = new AVLTree<>();
        this.intro = intro;
        this.portraitUploaded = false;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("email", email);
        result.put("name", name);
        result.put("date", creation_date);
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

    public void setPortraitUploadedStatus(){
        this.portraitUploaded = true;
    }
}
