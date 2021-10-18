package au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.anu.cecs.COMP6442GroupAssignment.DetailedPostActivity;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.FirebaseRef;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.MyApplication;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.Search.Exp;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.Search.Parser;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.Search.Tokenizer;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Post;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Adapter.TimelinePostAdapter;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.TimelineCreation.CreatorFactory;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.TimelineCreation.TimelineCreator;

public class UserPostDAO {
    private static UserPostDAO instance;
    private final FirebaseFirestore db;
    private FirebaseUser currentUser;
    private TimelinePostAdapter timelinePostAdapter;
    private ArrayList<Post> posts;
    private String mode;
    private TimelineCreator creator;
    private AppCompatActivity act;

    private Exp temp_exp;

    boolean isLoading = false;


    public UserPostDAO(AppCompatActivity act) {

        FirebaseRef firebaseRef = FirebaseRef.getInstance();
        this.act = act;
        currentUser = firebaseRef.getFirebaseAuth().getCurrentUser();
        db = firebaseRef.getFirestore();
        posts = new ArrayList<>();
        mode = "Time";
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
        ,this);
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public static UserPostDAO getInstance(AppCompatActivity act) {
        if (instance == null) {
            instance = new UserPostDAO(act);
        }
        return instance;
    }

    public static UserPostDAO getInstance(){
        // need fix the Dao getInstance method should not contain so many parameters
        // pretty hard to invoke in other places fixme
        return instance;
    }

    public  ArrayList<Post> getPostList (String field , String key,Parser parser){
        ArrayList<Post> postArrayList = new ArrayList<>();

        db.collection("user-posts")
                .whereEqualTo(field, key)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String a = field;
                            String b = key;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                postArrayList.add(new Post(document.getData()));
                            }
                            parser.inc();
                            if (parser.whetherFinished()){
                                List<Post> result =temp_exp.evaluate();
                                if (result==null){
                                    posts.addAll(new ArrayList<Post>());
                                }else {
                                    posts.addAll(result);
                                }

                                timelinePostAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("Read posts", "Current data: null");
                        }
                    }
                });
        return postArrayList;
    }

    public void searchPost(String text){// Author=123
        /**
         * really redundant need fix fixme
         */
//        timelinePostAdapter = new TimelinePostAdapter(act.getApplicationContext(),
//                posts,
//                new TimelinePostAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(Post item) {
//                        Intent intent = new Intent(act.getApplicationContext(), DetailedPostActivity.class);
//                        intent.putExtra("pid",item.getPid());
//                        act.startActivity(intent);
//                    }
//                }
//                ,this);
        try {
            posts.clear();
            Tokenizer tokenizer = new Tokenizer(text);


            Parser parser = new Parser(tokenizer);
            temp_exp = parser.parseExp();
        }catch (Exception e){
            Toast.makeText(MyApplication.context,
                    "Illegal syntax", Toast.LENGTH_SHORT).show();
        }

    }

    public void getData(RecyclerView timelinePostView) {
        CreatorFactory factory = new CreatorFactory();
        posts = new ArrayList<>();
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
                ,this);
        timelinePostView.setAdapter(timelinePostAdapter);
        creator = factory.creatorFac(mode, posts, timelinePostAdapter);
        creator.getData();
    }

    public void update(String key, Map<String, Object> newValues) {
        db.collection("user-posts").document(key)
                .update(newValues)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Post", "Star suc!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Post", "Star err", e);
                    }
                });
        if (!((String) newValues.get("authorID")).equals(currentUser.getUid())) {
            ArrayList<String> usersWhoLike = (ArrayList<String>) newValues.get("usersWhoLike");
            if (usersWhoLike != null && usersWhoLike.contains(currentUser.getUid()))
                db.collection("user-data").document(currentUser.getUid())
                        .update("posts", FieldValue.arrayUnion(key));
            else
                db.collection("user-data").document(currentUser.getUid())
                        .update("posts", FieldValue.arrayRemove(key));
        }
    }

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
        db.collection("user-data").document(currentUser.getUid())
                .update("posts", FieldValue.arrayUnion(key));
    }

    public void loadMore() {
        creator.loadMore();
    }

    public TimelinePostAdapter getPostsAdapter() {
        return timelinePostAdapter;
    }

    public ArrayList<Post> getPosts(){
        return posts;
    }

    public boolean isLoading(){
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
