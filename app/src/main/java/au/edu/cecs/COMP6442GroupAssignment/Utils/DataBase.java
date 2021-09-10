package au.edu.cecs.COMP6442GroupAssignment.Utils;

import android.content.Context;

import java.util.List;

public class DataBase {
    private static List<Profile> profiles ;
    public static List<Profile> getProfiles( Context context){

        if (profiles == null){

            profiles = JsonUtils.getInstance().getProfiles(context);
        }
        return profiles;
    }
}
