package au.edu.anu.cecs.COMP6442GroupAssignment.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Post {

    public String pid;
    public String author;
    public String title;
    public String body;
    public Date date;
    public int starCount = 0;
    public HashMap<String, Boolean> stars;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String pid, String author, String title, String body) {
        this.pid = pid;
        this.author = author;
        this.title = title;
        this.body = body;
        this.date = new Date();
        this.stars = new HashMap<String, Boolean>();
    }

    public Post(HashMap<String, Object> value) {
        this.pid = (String) value.get("pid");
        this.author = (String) value.get("author");
        this.title = (String) value.get("title");
        this.body = (String) value.get("body");
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("pid", pid);
        result.put("author", author);
        result.put("title", title);
        result.put("body", body);
        result.put("date", date);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }
}