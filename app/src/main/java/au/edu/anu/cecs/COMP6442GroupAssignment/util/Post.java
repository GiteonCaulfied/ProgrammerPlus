package au.edu.anu.cecs.COMP6442GroupAssignment.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Post {

    private String pid;
    private String author;
    private String authorID;
    private String title;
    private String imageAddress;
    private ArrayList<String> tags;
    private String body;
    private long date;
    private ArrayList<String> usersWhoLike;
    private ArrayList<String> usersNotLike;
    private long longitude;
    private long latitude;
    private String address;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String pid, String author, String authorID,
                String title, String body, String imageAddress,
                HashMap<String, Object> locationMap) {
        this.pid = pid;
        this.author = author;
        this.authorID = authorID;
        this.title = title;
        this.tags = new ArrayList<>();
        this.body = body;
        this.date = System.currentTimeMillis();
        this.usersWhoLike = new ArrayList<>();
        this.usersNotLike = new ArrayList<>();
        this.imageAddress = imageAddress;
        this.longitude = (long) locationMap.get("Longitude");
        this.latitude = (long) locationMap.get("Latitude");
        this.address = (String) locationMap.get("Address");
    }

    public Post(Map<String, Object> value) {
        this.pid = (String) value.get("pid");
        this.author = (String) value.get("author");
        this.authorID = (String) value.get("authorID");
        this.title = (String) value.get("title");
        this.tags = (ArrayList<String>) value.get("tags");;
        this.body = (String) value.get("body");
        this.date = (long) value.get("date");
        this.usersWhoLike = (ArrayList<String>) value.get("usersWhoLike");
        this.usersNotLike = (ArrayList<String>) value.get("usersNotLike");
        this.imageAddress = (String) value.get("imageAddress");
        this.longitude = (long) value.get("Longitude");
        this.latitude = (long) value.get("Latitude");
        this.address = (String) value.get("Address");
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("pid", pid);
        result.put("author", author);
        result.put("authorID", authorID);
        result.put("title", title);
        result.put("body", body);
        result.put("date", date);
        result.put("imageAddress", imageAddress);
        result.put("tags", tags);
        result.put("usersWhoLike", usersWhoLike);
        result.put("usersNotLike", usersNotLike);
        result.put("Longitude", longitude);
        result.put("Latitude", latitude);
        result.put("Address", address);

        return result;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getPid(){
        return pid;
    }

    public ArrayList<String> getUsersWhoLike(){
        return usersWhoLike;
    }
    public void setUsersWhoLike(ArrayList<String> usersWhoLike){
        this.usersWhoLike = usersWhoLike;
    }
    public void setImageAddress(String address){
        this.imageAddress = address;
    }

    public String getAuthorID(){
        return authorID;
    }

    public void setTags(String raw){
        String[] raw_arr = raw.split(";");
        tags = new ArrayList<>(Arrays.asList(raw_arr));
    }

    public ArrayList<String> getTags(){
        return tags;
    }

    public long getLongitude() {
        return longitude;
    }

    public long getLatitude() {
        return latitude;
    }

    public String getAddress() {
        return address;
    }
}