package au.edu.anu.cecs.COMP6442GroupAssignment.util.TimelineCreation;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.Adapter.TimelinePostAdapter;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserPostDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.FirebaseRef;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Post;

public class TimeModeCreator extends TimelineCreator{
    /**
     * The time mode is just to order the pasts according
     * to their publish time, and display the latest posts.
     */

    public TimeModeCreator(ArrayList<Post> posts, TimelinePostAdapter timelinePostAdapter) {
        super(posts, timelinePostAdapter);
    }

    /**
     * Load the 10 latest posts to initialize timeline
     */
    public void getData() {
        // Read the 10 latest posts as initialization
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

    /**
     * According to the current number of posts,
     * load more posts in the time order
     */
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
                int nextLimit = scrollPosition + 10;

                Query query = db.collection("user-posts").orderBy("date").limitToLast(nextLimit);
                query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
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
                                posts.add(new Post(Objects.requireNonNull(document.getData())));
                            }
                            /**
                             * there could be a bug which is searched post will be replaced by update？
                             * fixme
                             */

                            Collections.reverse(posts);

                            UserPostDAO userPostDAO = UserPostDAO.getInstance();
                            userPostDAO.setLoading(false);
                            timelinePostAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("Read posts", "Current data: null");
                        }
                    }
                });
            }
        }, 2000);
    }
}
