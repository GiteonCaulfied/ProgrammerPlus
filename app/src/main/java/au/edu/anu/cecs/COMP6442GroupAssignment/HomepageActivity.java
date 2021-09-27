package au.edu.anu.cecs.COMP6442GroupAssignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.Post;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.TimelinePostAdapter;

public class HomepageActivity extends AppCompatActivity {
    private DatabaseReference myRef;
    private List<Post> allPosts;
    private TimelinePostAdapter timelinePostAdapter;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        RecyclerView timelinePostView = (RecyclerView) findViewById(R.id.timelinePostView);
        myRef = FirebaseDatabase.getInstance().getReference();

        //Data
        allPosts = new ArrayList<>();

        //Adapter
        timelinePostAdapter = new TimelinePostAdapter(getApplicationContext(), allPosts,
                new TimelinePostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Post item) {
                Toast.makeText(getApplicationContext(), "Post Clicked: " + item.title, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(HomepageActivity.this, DetailedPostActivity.class);
                intent.putExtra("pid",item.pid);
                startActivity(intent);
            }

        });
        timelinePostView.setAdapter(timelinePostAdapter);
        timelinePostView.setLayoutManager(new LinearLayoutManager(this));

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

    // New Post Button is Clicked, Send the account message and turn to PostActivity
    public void newPostButtonClick(View v){
        Intent intent = new Intent(HomepageActivity.this, PostActivity.class);
        startActivity(intent);
    }

    public void goToProfile(View view) {
        Intent intent = new Intent(HomepageActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}