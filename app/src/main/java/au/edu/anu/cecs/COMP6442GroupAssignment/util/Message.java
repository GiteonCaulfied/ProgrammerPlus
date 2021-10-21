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

    /**
     * Constructor when creating a Message.
     */
    public Message(String text, String whoSent) {
        this.time = System.currentTimeMillis();
        this.text = text;
        this.whoSent = whoSent;
    }

    /**
     * Constructor when creating a Message.(With messageType and pid)
     */
    public Message(String text, String whoSent, String messageType, String pid) {
        this.time = System.currentTimeMillis();
        this.text = text;
        this.whoSent = whoSent;
        this.messageType = messageType;
        this.pid = pid;
    }

    /**
     * Transform the Message to a Map to upload to the Firebase.
     */
    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("time", time);
        map.put("text", text);
        map.put("whoSent", whoSent);
        return map;
    }

    /**
     * Transform the Message to a Map to upload to the Firebase.(With messageType and pid)
     */
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
