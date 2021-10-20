package au.edu.anu.cecs.COMP6442GroupAssignment.util.DataGenerator;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.UserManager;

public class ActGenerator {
    FirebaseFirestore db;
    Context context;

    public ActGenerator(Context context) {
        db = FirebaseFirestore.getInstance();
        this.context = context;
    }

    /**
     * Let two users be friends with each other and upload the data to the Firebase.
     *
     * @param uid1
     * @param uid2
     */
    public void makeFriends(String uid1, String uid2) {

        db.collection("user-profiles")
                .document(uid1)
                .update("friends", FieldValue.arrayUnion(uid2));
        db.collection("user-profiles")
                .document(uid2)
                .update("friends", FieldValue.arrayUnion(uid1));
    }

    /**
     * User liked a Post, upload the data to the Firebase.
     *
     * @param uid ID of the user who gives a like
     * @param pid Post liked by the user
     */
    public void likePosts(String uid, String pid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user-data").document(uid)
                .update("posts", FieldValue.arrayUnion(pid))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("user-like-post", "123");
                        }
                        else {
                            Log.d("user-like-post", task.getException().toString());
                        }
                    }
                });
        db.collection("user-posts").document(pid)
                .update("usersWhoLike", FieldValue.arrayUnion(uid));
    }

    public void generateFriends() {
        UserManager userManager = UserManager.getInstance();
        for (int i = 0; i < 10000; i++) {
            String u1 = userManager.getRandomID();
            String u2 = userManager.getRandomID();
            if (!u1.equals(u2)) {
                makeFriends(u1, u2);
            }
        }
    }

    public void generateLikes() {
        UserManager userManager = UserManager.getInstance();
        List<String> pids = readPIDs();
        int l = pids.size();
        Random random = new Random();

        for (int i = 0; i < 10000; i++) {
            System.out.println(i);
            String uid = userManager.getRandomID();
            likePosts(uid, pids.get(random.nextInt(l)));
        }
    }

    /**
     * Read the ProfileID from the local file and put them to a List.
     *
     * @return a List of usernames
     */
    public List<String> readPIDs() {
        List<String> pids = new ArrayList<>();
        try {
            // Read usernames
            InputStreamReader reader = new InputStreamReader(
                    context.getResources().getAssets().open("profileID"));
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            while (line != null) {
                pids.add(line);
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pids;
    }

    /**
     * Update the data in the UserManager with ID and upload to the Firebase.
     */
    public void warmUp() {
        UserManager userManager = UserManager.getInstance();

//        values.put("posts", new ArrayList<>());
        for (String id : userManager.getIDList()) {
            Map<String, Object> values = new HashMap<>();
            values.put("id", id);
            db.collection("user-data").document(id)
                    .update(values);
            System.out.println(id);
        }
    }
}
