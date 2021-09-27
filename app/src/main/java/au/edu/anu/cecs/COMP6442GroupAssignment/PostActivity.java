package au.edu.anu.cecs.COMP6442GroupAssignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserPostDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Post;

public class PostActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private EditText title, content;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        database = FirebaseDatabase.getInstance();
        title = findViewById(R.id.post_title);
        content = findViewById(R.id.post_content);
        myRef = database.getReference();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void newPost(View v) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = myRef.child("posts").push().getKey();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        Post post = new Post(key,
                uid,
                title.getText().toString(),
                content.getText().toString(),
                "");
        Map<String, Object> postValues = post.toMap();

        UserPostDAO userPostDAO = UserPostDAO.getInstance(this);
        userPostDAO.create(key, postValues);
        Toast.makeText(getApplicationContext(),
                "Post successfully.", Toast.LENGTH_SHORT).show();

        finish();
    }
}