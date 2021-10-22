package au.edu.anu.cecs.COMP6442GroupAssignment.util.TimelineCreation;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.Adapter.TimelinePostAdapter;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.FirebaseRef;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Post;

public class ExploreModeCreator extends TimelineCreator {
    /**
     * The explore mode also needs to calculate the user distance.
     * But this mode will recommend posts from the furthest users,
     * in contrast to the like mode. In case that our uses will
     * have partial ideas and knowledge, we add the explore mode.
     * In this mode, the users will learn something from the furthest
     * users. That means they will some posts that they may not see
     * in daily life.
     */
    private FirebaseUser currentUser;
    private Random random;

    public ExploreModeCreator(ArrayList<Post> posts, TimelinePostAdapter timelinePostAdapter) {
        super(posts, timelinePostAdapter);
        currentUser = FirebaseRef.getInstance().getFirebaseAuth().getCurrentUser();
        random = new Random();
    }

    /**
     * Get 10 posts that the user may not see in daily life
     */
    @Override
    public void getData() {
        getDislikePosts(10);
    }

    private void getDislikePosts(int postNum) {
        db.collection("user-data").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("Read users", "Current data: " + task);
                    UserLog me = null;
                    ArrayList<UserLog> userLogs = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        if (document.getId().equals(currentUser.getUid())) {
                            me = new UserLog(document.getId(), document.getData());
                        }
                        else
                            userLogs.add(new UserLog(document.getId(), document.getData()));
                    }
                    if (me == null)
                        me = new UserLog(currentUser.getUid());

                    for (UserLog userLog: userLogs) {
                        userLog.setDistanceToMe(me.calDistance(userLog));
                    }
                    userLogs.add(me);
                    Collections.sort(userLogs);
                    Collections.reverse(userLogs); // reverse the user list

                    Query query = db.collection("user-posts").orderBy("date").limitToLast(postNum * 10);
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
                                for (int i = 0; i < userLogs.size(); i++) {
                                    for (DocumentSnapshot document : value.getDocuments()) {
                                        Post post = new Post(document.getData());
                                        if (userLogs.get(i).getPosts().contains(post.getPid())) {
                                            double r = random.nextDouble();
                                            if (r <= (userLogs.size() - 1.0 - i) / (userLogs.size() - 1.0)) {
                                                posts.add(post);
                                                break;
                                            }
                                        }
                                    }
                                    if (posts.size() >= postNum)
                                        break;
                                }

                                timelinePostAdapter.notifyDataSetChanged();
                            } else {
                                Log.d("Read posts", "Current data: null");
                            }
                        }
                    });

                } else {
                    Log.d("Read users", "Current data: null");
                }
            }
        });
    }

    /**
     * An easy implementation to load more posts
     */
    @Override
    public void loadMore() {
        posts.remove(posts.size() - 1);
        int scrollPosition = posts.size();
        timelinePostAdapter.notifyItemRemoved(scrollPosition);
        getDislikePosts(posts.size() + 10);
    }
}
