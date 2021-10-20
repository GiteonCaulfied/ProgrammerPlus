package au.edu.anu.cecs.COMP6442GroupAssignment.util.TimelineCreation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserLog implements Comparable<UserLog> {
    private static final double EARTH_RADIUS = 6378137;
    private String userID;
    private long latitude;
    private long longitude;
    private Double distanceToMe;
    private final ArrayList<String> posts;
    private double[] postCode;

    public UserLog(String id) {
        userID = id;
        latitude = 181;
        longitude = 181;
        posts = new ArrayList<>();
        distanceToMe = (double) 0;
    }

    public UserLog(String id, Map<String, Object> values) {
        userID = id;

        if (values.containsKey("Latitude")) {
            latitude = (long) values.get("Latitude");
        } else {
            latitude = 181;
        }

        if (values.containsKey("Longitude")) {
            longitude = (long) values.get("Longitude");
        } else {
            longitude = 181;
        }

        if (values.containsKey("posts")) {
            posts = (ArrayList<String>) values.get("posts");
        } else {
            posts = new ArrayList<>();
        }

        distanceToMe = (double) 0;
    }

    public String getUserID() {
        return userID;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static double locationDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s;
    }

    public double calDistance(UserLog that) {
        double locDist = 1000.0;
        if (this.latitude != 181 && that.latitude != 181) {
            locDist = locationDistance(this.longitude, this.latitude,
                    that.getLongitude(), that.getLatitude()) / 1e10;
        }

        double postDist = 1000.0;
        if (!this.posts.isEmpty() && !that.posts.isEmpty()) {
            double i = 0;
            for (String s: this.posts) {
                if (that.posts.contains(s)) i++;
            }
            postDist =  - i / (this.posts.size() + that.posts.size());
        }

        return locDist + postDist;
    }

    public long getLatitude() {
        return latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setDistanceToMe(Double distanceToMe) {
        this.distanceToMe = distanceToMe;
    }

    public Double getDistanceToMe() {
        return distanceToMe;
    }

    public ArrayList<String> getPosts() {
        return posts;
    }

    @Override
    public int compareTo(UserLog userLog) {
        return this.distanceToMe.compareTo(userLog.getDistanceToMe());
    }

    public void generateHotPosts(List<String> hotPosts) {
        postCode = new double[1000];
        for (String pid: this.posts) {
            if (hotPosts.contains(pid))
                postCode[hotPosts.indexOf(pid)] = 1.0;
        }
    }

    public int findCluster(List<double[]> centers) {
        double best = -1;
        int best_c = -1;
        for (int i = 0; i < centers.size(); i++) {
            double[] center = centers.get(i);
            double n = calDistanceCluster(center);
            if (n > best) {
                best = n;
                best_c = i;
            }
        }

        return best_c;
    }

    public double calDistanceCluster(double[] center) {
        double res = 0;
        for (int i = 0; i < 1000; i++) {
            res += (center[i] - postCode[i]) * (center[i] - postCode[i]);
        }
        return res;
    }
}
