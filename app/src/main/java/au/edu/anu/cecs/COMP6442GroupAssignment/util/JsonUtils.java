package au.edu.anu.cecs.COMP6442GroupAssignment.util;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserPostDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DataStructure.AVLTree;

public class JsonUtils {

    private static JsonUtils jsonUtils;// singleton

    private DatabaseReference myRef;

    private Integer LocalPostCount;

    private JsonUtils(){
        FirebaseRef firebaseRef = FirebaseRef.getInstance();
        myRef = firebaseRef.getDatabaseRef();
        LocalPostCount = 0;
    }

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

    public AVLTree<String> getSwearWordsTree (Context context){

            Gson gson = new Gson();
//            JsonReader jsonReader = null;
            AVLTree<String> output = null;
            String json = null;
//        final Type CUS_LIST_TYPE = new TypeToken<AVLTree<String>>() {}.getType();
            //or TypeToken.getParameterized(ArrayList.class, PersonJSON.class).getType();

            try{
                json = read(context.getResources().getAssets().open("swear_words.json"));
                output = gson.fromJson(json, AVLTree.class);
//                jsonReader = new JsonReader(new FileReader());
            }catch (Exception e) {
                e.printStackTrace();
            }
//        Config c = gson.fromJson(jsonReader, AVLTree.class);
//        AVLTree<String> w = gson.fromJson(jsonReader, new TypeToken<AVLTree<String>>(){}.getType());
//        AVLTree<String> w = (AVLTree<String>) c.getWords();

            return output;

    }

    public void saveSwearWordsTree(Context context,AVLTree<String> words) {
        //Gson gson = new Gson();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try(FileWriter fw = new FileWriter(read(context.getResources().getAssets().open("swaer_words.json")))){
            gson.toJson(words, fw);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
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

    public void readLocalPosts(Context context){
        try {
            JSONObject object = new JSONObject(readJSON(context));
            JSONArray array = object.getJSONArray("data");

            UserManager userManager = UserManager.getInstance();

            if (LocalPostCount < array.length()){
                for (int i = LocalPostCount; i < LocalPostCount + 2; i++) {

                    JSONObject jsonObject = array.getJSONObject(i);
                    String user_id = userManager.getRandomID();
                    String name = userManager.getNameFromID(user_id);
                    String tweet = jsonObject.getString("tweet");

                    String key = myRef.child("posts").push().getKey();

                    Post post = new Post(key,
                            name,
                            user_id,
                            titleGenerator(tweet),
                            tweet,
                            "",
                            new HashMap<>());

                    post.setTags(tagStringGenerator());

                    Map<String, Object> postValues = post.toMap();

                    UserPostDAO userPostDAO = UserPostDAO.getInstance();
                    userPostDAO.create(key, postValues);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LocalPostCount = LocalPostCount + 2;

    }

    public String readJSON(Context context) {
        String json = null;
        try {
            // Opening data.json file
            InputStream inputStream = context.getResources().getAssets().open("sydney.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            // read values in the byte array
            inputStream.read(buffer);
            inputStream.close();
            // convert byte to string
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return json;
        }
        return json;
    }

    public String titleGenerator(String content){
        String out = "";

        // Random Number to randomly select titles
        int min = 1;
        int max = 100;

        int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);

        List<String> word_arr = Arrays.asList(content.split(" "));

        // Check If the content contains "@" symbol (mention someone)
        boolean atSomeone = content.contains("@");
        boolean questionMark = content.contains("?");
        boolean weblink = content.contains("http") || content.contains("www");

        List<String> personGotAted = new ArrayList<>();
        List<String> word_arr_noAt = new ArrayList<>();
        List<String> word_arr_noLink = new ArrayList<>();

        if (atSomeone){
            for (String s : word_arr){
                if (!s.contains("@")){
                    word_arr_noAt.add(s);
                } else {
                    personGotAted.add(s);
                }
            }
        }

        if (weblink){
            for (String s : word_arr){
                if (!(s.contains("http") || s.contains("www"))){
                    word_arr_noLink.add(s);
                }
            }
        }


        // If no Comma
        if (!(content.contains(","))){

            if (word_arr.size() < 10){

                out = content;

            } else {

                // If @Someone
                if (atSomeone){
                    String atString = "";
                    for (String p: personGotAted){
                        atString = atString + p + " ";
                    }

                    // If Both @Someone and question mark
                    if (questionMark){
                        if (random_int < 33){
                            out = "How's this?";
                        } else if (random_int < 66){
                            out = "Got a question for you guys";
                        } else {
                            out = "What's on your mind then?";
                        }
                    }

                    // If Both @Someone and contains link
                    else if (weblink){
                        if (random_int < 50){
                            out = "You Gotta see this";
                        } else {
                            out = "Better Check this man";
                        }

                    } else {

                        if (random_int < 33){
                            out = "Better Check this";
                        } else if (random_int < 66){
                            out = "Remember that?";
                        } else {
                            out = "About our last talk";
                        }

                    }

                    // No @Someone
                } else {
                    // If question mark
                    if (questionMark) {
                        if (random_int < 33) {
                            out = "Just Confused";
                        } else if (random_int < 66) {
                            out = "Got a question Here";
                        } else {
                            out = "Gotta figure this out";
                        }
                    }

                    // If contains link
                    else if (weblink) {

                        String noLinkString = "";
                        for (String w: word_arr_noLink){
                            noLinkString = noLinkString + w + " ";
                        }

                        out = noLinkString;

                    } else {

                        if (random_int < 33) {
                            out = "About Something";
                        } else if (random_int < 66) {
                            out = "Gotta say things about it";
                        } else {
                            out = "A random talk";
                        }
                    }

                }
            }

            // Has Comma
        } else {
            int firstEndIndex = Math.min(content.indexOf(','),content.indexOf('.'));
            String firstSentence = content.substring(0,firstEndIndex);

            if (firstSentence.length() < 10){
                out = firstSentence;
            } else {
                if (questionMark){
                    if (random_int < 33){
                        out = "Literally Questioning";
                    } else if (random_int < 66){
                        out = "Confusing";
                    } else {
                        out = "Deep Think (Maybe)";
                    }
                } else if (weblink){
                    if (random_int < 50){
                        out = "Some recommendations";
                    } else {
                        out = "Sharing!";
                    }
                } else {
                    if (random_int < 25){
                        out = "Tales to tell";
                    } else if (random_int < 50){
                        out = "Random Thoughts";
                    } else if (random_int < 75) {
                        out = "Something to say";
                    } else {
                        out = "Other things";
                    }
                }
            }

        }

        return out;
    }

    public String tagStringGenerator(){
        // Random Number to randomly select tags
        int min = 0;
        int max = 7;

        int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);

        String out = "";

        // Tag Array
        List<String> tags = new ArrayList<>();
        tags.addAll(Arrays.asList(new String[]{"ANU","JustDaily","ShiftPost","BotReview","BotTalk","TrueLife","NoThanks","Australia"}));

        out = tags.get(random_int);
        return out;
    }






}
