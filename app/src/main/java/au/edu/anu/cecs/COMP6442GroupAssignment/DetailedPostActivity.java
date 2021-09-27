package au.edu.anu.cecs.COMP6442GroupAssignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.Post;

public class DetailedPostActivity extends AppCompatActivity {

    private ImageView image;
    private TextView author, title, body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_post);

        author = (TextView) findViewById(R.id.post_page_author);
        title = (TextView) findViewById(R.id.post_page_title);
        body = (TextView) findViewById(R.id.post_page_body);

        Intent from_intent = getIntent();
        String pid = from_intent.getStringExtra("pid");

        List<Post> allPost = new ArrayList<>();

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("user-posts").child(pid);

        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, Object> post = (HashMap<String, Object>) task.getResult().getValue();
                Post p = new Post(post);
                author.setText(p.author);
                title.setText(p.title);
                body.setText(p.body);

                }
            });




    }
}