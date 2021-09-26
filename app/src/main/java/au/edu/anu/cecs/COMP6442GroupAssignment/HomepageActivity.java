package au.edu.anu.cecs.COMP6442GroupAssignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import au.edu.cecs.COMP6442GroupAssignment.Utils.Post;
import au.edu.cecs.COMP6442GroupAssignment.Utils.TimelinePostAdapter;

public class HomepageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        RecyclerView timelinePostView = (RecyclerView) findViewById(R.id.timelinePostView);

        //Data
        List<Post> allPosts = new ArrayList<>();
        allPosts.add(new Post("u10000","Xuzeng","Why the Parcel Question should be deleted from MidExam","blah blah blah"));
        allPosts.add(new Post("u20000","QinYu","Mark I Evaluation","blah blah blah"));
        allPosts.add(new Post("u30000","TomG","Gonna Start Over","blah blah blah"));
        allPosts.add(new Post("u40000","HXY","Let's do the Data Stream!","blah blah blah"));
        allPosts.add(new Post("u50000","Yuqi Ge","I'll do the UI!","blah blah blah"));

        //Adapter
        TimelinePostAdapter timelinePostAdapter = new TimelinePostAdapter(getApplicationContext(), allPosts, new TimelinePostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Post item) {
                Toast.makeText(getApplicationContext(), "Post Clicked: " + item.title, Toast.LENGTH_LONG).show();
            }

        });
        timelinePostView.setAdapter(timelinePostAdapter);
        timelinePostView.setLayoutManager(new LinearLayoutManager(this));

    }

    // New Post Button is Clicked, Send the account message and turn to PostActivity
    public void newPostButtonClick(View v){
        Intent from_intent = getIntent();
        Intent intent = new Intent(HomepageActivity.this,PostActivity.class);
        intent.putExtra("account",from_intent.getStringExtra("account"));
        startActivity(intent);
    }

    public void goToProfile(View view) {
        Intent from_intent = getIntent();
        Intent intent = new Intent(HomepageActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}