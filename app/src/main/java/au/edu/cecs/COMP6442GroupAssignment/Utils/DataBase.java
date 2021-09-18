package au.edu.cecs.COMP6442GroupAssignment.Utils;

import android.content.Context;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DataBase {
    private static HashMap<String ,Profile> profiles ;
    public static HashMap<String,Profile> getProfiles(Context context){

        if (profiles == null){

            profiles = JsonUtils.getInstance().getProfiles(context);
        }
        return profiles;
    }
}
