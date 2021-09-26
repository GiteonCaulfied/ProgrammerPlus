package au.edu.anu.cecs.COMP6442GroupAssignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import au.edu.cecs.COMP6442GroupAssignment.Utils.*;

public class ProfileActivity extends AppCompatActivity {

    private TextView name, email, intro;
    private DatabaseReference myRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        myRef = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        name = findViewById(R.id.email_Text);
        email = findViewById(R.id.email_Text);
        intro = findViewById(R.id.intro_Text);

        if (currentUser.getEmail() != null) {
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    Profile profile = dataSnapshot.getValue(Profile.class);
                    name.setText(profile.getName());
                    email.setText(profile.getEmail());
                    intro.setText(profile.getIntro());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w("Get profile", "loadPost:onCancelled", databaseError.toException());
                }
            };

            myRef.child("user-profile").child(currentUser.getUid()).addValueEventListener(postListener);
        }
    }

    public void logOut(View v) {
        FirebaseAuth.getInstance().signOut();
    }
}