package au.edu.anu.cecs.COMP6442GroupAssignment.util;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserProfileDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.TimelineCreation.UserLog;

public class FriRecom {
    Context context;

    public FriRecom(Context context) {
        this.context = context;
    }

    public void recommendation(EditText rec1, EditText rec2, EditText rec3) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseRef.getInstance().getFirebaseAuth().getCurrentUser();
        List<String> posts = readHotPosts();
        List<double[]> clusters = readClusters();

        db.collection("user-data").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("Recommend users", "Current data: " + task);
                    UserLog me = null;
                    ArrayList<UserLog> userLogs = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        if (document.getId().equals(currentUser.getUid())) {
                            me = new UserLog(document.getId(), document.getData());
                        }
                        else
                            userLogs.add(new UserLog(document.getId(), document.getData()));
                    }

                    me.generateHotPosts(posts);
                    int best_me_clu = me.findCluster(clusters);
                    UserProfileDAO profileDAO = UserProfileDAO.getInstance();
                    Profile user = profileDAO.getUserprofile();

                    List<String> res = new ArrayList<>();
                    for (UserLog userLog: userLogs) {
                        if (user.friendContain(userLog.getUserID()))
                            continue;
                        userLog.generateHotPosts(posts);
                        int c = userLog.findCluster(clusters);
                        if (c == best_me_clu) {
                            res.add(userLog.getUserID());
                            if (res.size() == 3)
                                break;
                        }
                    }

                    UserManager userManager = UserManager.getInstance();
                    rec1.setText(userManager.getEmailFromID(res.get(1)));
                    rec2.setText(userManager.getEmailFromID(res.get(2)));
                    rec3.setText(userManager.getEmailFromID(res.get(3)));
                } else {
                    Log.d("Recommend users", "Current data: null");
                }
            }
        });
    }

    public List<String> readHotPosts() {
        List<String> posts = new ArrayList<>();
        try {
            // Read usernames
            InputStreamReader reader = new InputStreamReader(
                    context.getResources().getAssets().open("HotPost.txt"));
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            while (line != null) {
                posts.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return posts;
    }

    public List<double[]> readClusters() {
        List<double[]> posts = new ArrayList<>();
        try {
            // Read usernames
            InputStreamReader reader = new InputStreamReader(
                    context.getResources().getAssets().open("cluster_centers_.txt"));
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            while (line != null) {
                String[] res = line.split(",");
                double[] ld = new double[1000];
                for (int i = 0; i < 1000; i++) {
                    ld[i] = Double.parseDouble(res[i]);
                }
                posts.add(ld);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return posts;
    }
}
