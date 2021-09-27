package au.edu.anu.cecs.COMP6442GroupAssignment.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Post {

    public String pid;
    public String author;
    public String title;
    public String imageAddress;
    public ArrayList<String> tags;
    public String body;
    public long date;
    public ArrayList<String> usersWhoLike;
    public ArrayList<String> usersNotLike;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String pid, String author, String title, String body,
                String imageAddress) {
        this.pid = pid;
        this.author = author;
        this.title = title;
        this.tags = new ArrayList<>();
        this.body = body;
        this.date = System.currentTimeMillis();
        this.usersWhoLike = new ArrayList<>();
        this.usersNotLike = new ArrayList<>();
        this.imageAddress = imageAddress;
    }

    public Post(Map<String, Object> value) {
        this.pid = (String) value.get("pid");
        this.author = (String) value.get("author");
        this.title = (String) value.get("title");
        this.tags = (ArrayList<String>) value.get("tags");;
        this.body = (String) value.get("body");
        this.date = (long) value.get("date");
        this.usersWhoLike = (ArrayList<String>) value.get("usersWhoLike");
        this.usersNotLike = (ArrayList<String>) value.get("usersNotLike");
        this.imageAddress = (String) value.get("imageAddress");
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("pid", pid);
        result.put("author", author);
        result.put("title", title);
        result.put("body", body);
        result.put("date", date);
        result.put("imageAddress", imageAddress);
        result.put("tags", tags);
        result.put("usersWhoLike", usersWhoLike);
        result.put("usersNotLike", usersNotLike);

        return result;
    }
}