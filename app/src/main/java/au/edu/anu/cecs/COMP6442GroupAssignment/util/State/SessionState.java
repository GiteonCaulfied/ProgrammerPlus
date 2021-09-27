package au.edu.anu.cecs.COMP6442GroupAssignment.util.State;

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

import au.edu.anu.cecs.COMP6442GroupAssignment.MainActivity;
import au.edu.anu.cecs.COMP6442GroupAssignment.R;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserPostDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Post;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.TimelinePostAdapter;

public class SessionState implements UserState{
    private MainActivity main;
    private DatabaseReference myRef;
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
        UserPostDAO userPostDAO = UserPostDAO.getInstance(main);
        userPostDAO.getData();

        timelinePostView.setAdapter(userPostDAO.getPostsAdapter());
        timelinePostView.setLayoutManager(new LinearLayoutManager(main));
    }
}
