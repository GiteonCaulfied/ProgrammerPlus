package au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO;

import java.util.Map;

public interface UserActivityDaoInterface {
    public void getData();

    public void update();

    public void create(String key, Map<String, Object> postValues);

    public void delete();

    public void clear();
}
