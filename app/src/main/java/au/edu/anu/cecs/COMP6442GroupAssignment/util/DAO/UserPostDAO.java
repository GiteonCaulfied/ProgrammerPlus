package au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
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

public class UserPostDAO implements UserActivityDaoInterface {
    private static UserPostDAO instance;
    private final FirebaseFirestore db;
    private FirebaseUser currentUser;
    private final TimelinePostAdapter timelinePostAdapter;
    private final ArrayList<Post> posts;

    private Exp temp_exp;

    boolean isLoading = false;


    public UserPostDAO(AppCompatActivity act) {

        FirebaseRef firebaseRef = FirebaseRef.getInstance();
        db = firebaseRef.getFirestore();
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
        ,this);
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
                    /**
                     * there could be a bug which is searched post will be replaced by update？
                     * fixme
                     */

                    Collections.reverse(posts);
                    timelinePostAdapter.notifyDataSetChanged();
                } else {
                    Log.d("Read posts", "Current data: null");
                }
            }
        });
    }

    @Override
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

    public void loadMore() {
        posts.add(null);
        timelinePostAdapter.notifyItemInserted(posts.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                posts.remove(posts.size() - 1);
                int scrollPosition = posts.size();
                timelinePostAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                Query query = db.collection("user-posts").orderBy("date").limitToLast(nextLimit);
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
                            /**
                             * there could be a bug which is searched post will be replaced by update？
                             * fixme
                             */

                            Collections.reverse(posts);
                            timelinePostAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("Read posts", "Current data: null");
                        }
                    }
                });

                currentSize = nextLimit + 1;

                timelinePostAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);


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

    public ArrayList<Post> getPosts(){
        return posts;
    }

    public boolean isLoading(){
        return isLoading;
    }

    public void setLoading(){
        isLoading = true;
    }
}
