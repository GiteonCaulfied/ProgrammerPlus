package au.edu.anu.cecs.COMP6442GroupAssignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

        DocumentReference myRef = FirebaseFirestore.getInstance().collection("user-posts").document(pid);

        myRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Post p = new Post(document.getData());
                        author.setText(p.getAuthor());
                        title.setText(p.getTitle());
                        body.setText(p.getBody());
                    } else {
                        System.out.println("No such document!");
                    }
                } else {

                }
            }
        });


}
}
