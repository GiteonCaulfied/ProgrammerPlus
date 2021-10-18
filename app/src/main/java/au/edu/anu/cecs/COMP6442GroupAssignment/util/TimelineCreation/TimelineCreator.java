package au.edu.anu.cecs.COMP6442GroupAssignment.util.TimelineCreation;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.Adapter.TimelinePostAdapter;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.FirebaseRef;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Post;

public abstract class TimelineCreator {
    protected final FirebaseFirestore db;
    protected final ArrayList<Post> posts;
    protected final TimelinePostAdapter timelinePostAdapter;

    public TimelineCreator(ArrayList<Post> posts,
                           TimelinePostAdapter timelinePostAdapter) {
        db = FirebaseRef.getInstance().getFirestore();
        this.posts = posts;
        this.timelinePostAdapter = timelinePostAdapter;
    }

    public abstract void getData();

    public abstract void loadMore();
}
