package au.edu.anu.cecs.COMP6442GroupAssignment.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    private String longitude;
    private String latitude;
    private String address;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    /**
     * Default Constructor when creating a post.
     */
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
        if (locationMap != null & locationMap.size() > 0) {
            this.longitude = locationMap.get("Longitude").toString();
            this.latitude = locationMap.get("Latitude").toString();
            this.address = (String) locationMap.get("Address");
        } else {
            this.longitude = "Unknown";
            this.latitude = "Unknown";
            this.address = "Unknown";
        }
    }

    /**
     * Constructor using map (used when retrieve posts from Firebase).
     */
    public Post(Map<String, Object> value) {
        this.pid = (String) value.get("pid");
        this.author = (String) value.get("author");
        if (this.author == null)
            this.author = "Qinyu Zhao";
        this.authorID = (String) value.get("authorID");
        this.title = (String) value.get("title");
        this.tags = (ArrayList<String>) value.get("tags");;
        this.body = (String) value.get("body");
        this.date = (long) value.get("date");
        this.usersWhoLike = (ArrayList<String>) value.get("usersWhoLike");
        this.usersNotLike = (ArrayList<String>) value.get("usersNotLike");
        this.imageAddress = (String) value.get("imageAddress");
        this.longitude = (String) value.get("Longitude");
        this.latitude = (String) value.get("Latitude");
        this.address = (String) value.get("Address");
    }

    /**
     * Transform the Post to a Map to upload to the Firebase.
     */
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

    public String getImageAddress(){
        return imageAddress;
    }

    public String getAuthorID(){
        return authorID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return date == post.date && Objects.equals(pid, post.pid) && Objects.equals(author, post.author) && Objects.equals(authorID, post.authorID) && Objects.equals(title, post.title) && Objects.equals(imageAddress, post.imageAddress) && Objects.equals(tags, post.tags) && Objects.equals(body, post.body) && Objects.equals(usersWhoLike, post.usersWhoLike) && Objects.equals(usersNotLike, post.usersNotLike) && Objects.equals(longitude, post.longitude) && Objects.equals(latitude, post.latitude) && Objects.equals(address, post.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid, author, authorID, title, imageAddress, tags, body, date, usersWhoLike, usersNotLike, longitude, latitude, address);
    }

    /**
     * Set the tag of the Post using the raw tag String.
     *
     * @param raw raw String separating tags with semicolon (e.g. "ANU;BotTalk")
     */
    public void setTags(String raw){
        String[] raw_arr = raw.split(";");
        tags = new ArrayList<>(Arrays.asList(raw_arr));
    }

    public ArrayList<String> getTags(){
        return tags;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getAddress() {
        return address;
    }
}