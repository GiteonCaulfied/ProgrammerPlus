package au.edu.anu.cecs.COMP6442GroupAssignment.util.State;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import au.edu.anu.cecs.COMP6442GroupAssignment.DetailedPostActivity;
import au.edu.anu.cecs.COMP6442GroupAssignment.MainActivity;
import au.edu.anu.cecs.COMP6442GroupAssignment.R;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Post;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.TimelinePostAdapter;

public class SessionState implements UserState{
    private MainActivity main;
    private DatabaseReference myRef;
    private List<Post> allPosts;
    private TimelinePostAdapter timelinePostAdapter;
    private FirebaseUser currentUser;

    public SessionState(MainActivity main) {
        this.main = main;
    }

    @Override
    public void setContent() {
        main.setContentView(R.layout.activity_homepage);
    }

    @Override
    public void onCreate() {
        RecyclerView timelinePostView = (RecyclerView) main.findViewById(R.id.timelinePostView);
        myRef = FirebaseDatabase.getInstance().getReference();

        //Data
        allPosts = new ArrayList<>();

        //Adapter
        timelinePostAdapter = new TimelinePostAdapter(main.getApplicationContext(), allPosts,
                new TimelinePostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Post item) {
                Intent intent = new Intent(main.getApplicationContext(), DetailedPostActivity.class);
                intent.putExtra("pid",item.pid);
                main.startActivity(intent);
            }
        });
        timelinePostView.setAdapter(timelinePostAdapter);
        timelinePostView.setLayoutManager(new LinearLayoutManager(main));

        // Query
        Query postsQuery = myRef.child("user-posts")
                .orderByChild("date").limitToLast(10);
        postsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> postsMap = (HashMap<String, Object>) snapshot.getValue();
                if (postsMap == null) return;
                allPosts.clear();
                for (Object v: postsMap.values()) {
                    allPosts.add(new Post((HashMap<String, Object>) v));
                }
                timelinePostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Post", "loadPost:onCancelled", error.toException());
            }
        });
    }
}
