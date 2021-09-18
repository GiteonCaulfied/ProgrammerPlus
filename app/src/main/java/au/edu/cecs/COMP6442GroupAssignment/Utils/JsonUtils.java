package au.edu.cecs.COMP6442GroupAssignment.Utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    private static JsonUtils jsonUtils; // singleton

    private JsonUtils(){}

    /**
     * singleton method
     * @return
     */
    public static JsonUtils getInstance(){

        if (jsonUtils == null){

            jsonUtils = new JsonUtils();
        }
        return jsonUtils;
    }


    /**
     * read the json file
     * @param is
     * @return
     */
    private String read (InputStream is){
        BufferedReader reader = null ;
        StringBuilder sb = null;

        String line = null;

        try {
            sb = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null){
                sb.append(line);
                sb.append("\n");
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            try {
                if (is!=null) is.close();
                if (reader!=null) reader.close();
                }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return sb.toString();

    }

    public HashMap<String,Profile> getProfiles (Context context){
        HashMap<String,  Profile> profiles = null;
        InputStream is = null;
        try {
            is = context.getResources().getAssets().open("profile.json");
            String json = read(is); // invoke the read method to transfer the json file to String

            Gson gson = new Gson();
//
//            Type MapType = new TypeToken<HashMap<String,Profile>>(){}.getType();
//
//
//            profiles=gson.fromJson(json,MapType);
            Config c = gson.fromJson(json, Config.class);

            profiles = c.getProfiles();

//            Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
//                    .create();
//
//            profiles = gson.fromJson(
//                    json,
//                    new TypeToken<HashMap<String, Profile>>() {
//                    }.getType());


        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            try {
                if (is != null) is.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return profiles ;
    }



}
