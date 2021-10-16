package au.edu.anu.cecs.COMP6442GroupAssignment.util;

import java.util.HashMap;

public class Message implements Comparable {
    private long time;
    private String text;
    private String whoSent;
    private String messageType;
    private String pid;

    public Message() {
    }

    public Message(String text, String whoSent) {
        this.time = System.currentTimeMillis();
        this.text = text;
        this.whoSent = whoSent;
    }

    public Message(String text, String whoSent, String messageType, String pid) {
        this.time = System.currentTimeMillis();
        this.text = text;
        this.whoSent = whoSent;
        this.messageType = messageType;
        this.pid = pid;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("time", time);
        map.put("text", text);
        map.put("whoSent", whoSent);
        return map;
    }

    public HashMap<String, Object> toMap2() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("time", time);
        map.put("text", text);
        map.put("whoSent", whoSent);
        map.put("messageType", messageType);
        map.put("pid", pid);
        return map;
    }

    public long getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

    public String getPid() {
        return pid;
    }

    public String isWhoSent() {
        return whoSent;
    }

    @Override
    public int compareTo(Object o) {
        if (o.getClass() != this.getClass()) return 0;
        Message that = (Message) o;
        return Long.compare(this.getTime(), that.getTime());
    }
}
