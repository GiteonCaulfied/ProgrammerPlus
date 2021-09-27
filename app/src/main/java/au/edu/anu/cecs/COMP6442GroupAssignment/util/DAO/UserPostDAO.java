package au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import au.edu.anu.cecs.COMP6442GroupAssignment.DetailedPostActivity;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Post;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.TimelinePostAdapter;

public class UserPostDAO implements UserActivityDaoInterface {
    private static UserPostDAO instance;
    private final FirebaseFirestore db;
    private FirebaseUser currentUser;
    private final TimelinePostAdapter timelinePostAdapter;
    private final ArrayList<Post> posts;

    public UserPostDAO(AppCompatActivity act) {
        db = FirebaseFirestore.getInstance();
        posts = new ArrayList<>();
        //Adapter
        timelinePostAdapter = new TimelinePostAdapter(act.getApplicationContext(),
                posts,
                new TimelinePostAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Post item) {
                        Intent intent = new Intent(act.getApplicationContext(), DetailedPostActivity.class);
                        intent.putExtra("pid",item.getPid());
                        act.startActivity(intent);
                    }
                }
        );
    }

    public static UserPostDAO getInstance(AppCompatActivity act) {
        if (instance == null) {
            instance = new UserPostDAO(act);
        }
        return instance;
    }

    @Override
    public void getData() {
        Query query = db.collection("user-posts").orderBy("date").limitToLast(10);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("Read posts", "Listen failed.", error);
                    return;
                }

                if (value != null) {
                    Log.d("Read posts", "Current data: " + value);
                    posts.clear();
                    for (DocumentSnapshot document : value.getDocuments()) {
                        posts.add(new Post(document.getData()));
                    }
                    Collections.reverse(posts);
                    timelinePostAdapter.notifyDataSetChanged();
                } else {
                    Log.d("Read posts", "Current data: null");
                }
            }
        });
    }

    @Override
    public void update() {

    }

    @Override
    public void create(String key, Map<String, Object> newValues) {
        db.collection("user-posts").document(key)
                .set(newValues)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Post", "Post successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Post", "Error writing document", e);
                    }
                });
    }

    @Override
    public void delete() {

    }

    @Override
    public void clear() {

    }

    public TimelinePostAdapter getPostsAdapter() {
        return timelinePostAdapter;
    }
}
