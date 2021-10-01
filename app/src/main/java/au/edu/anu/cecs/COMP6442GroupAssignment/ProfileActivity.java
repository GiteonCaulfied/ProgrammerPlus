package au.edu.anu.cecs.COMP6442GroupAssignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.Profile;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserProfileDAO;

public class ProfileActivity extends AppCompatActivity {

    private TextView name, email, intro;
    private DatabaseReference myRef;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_profile);
        myRef = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        name = findViewById(R.id.name_Text);
        email = findViewById(R.id.email_Text);
        intro = findViewById(R.id.intro_Text);


        UserProfileDAO userProfileDao = UserProfileDAO.getInstance();
        Profile userprofile = userProfileDao.getUserprofile();


        if (currentUser.getEmail() != null && userprofile == null) {
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    Profile profile = dataSnapshot.getValue(Profile.class);
//                    userProfileDao.updateProfile(profile);
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
        }else{
            name.setText(userprofile.getName());
            email.setText(userprofile.getEmail());
            intro.setText(userprofile.getIntro());
        }
    }

    public void logOut(View v) {
        FirebaseAuth.getInstance().signOut();
        UserProfileDAO userProfileDao = UserProfileDAO.getInstance();
        userProfileDao.clear();
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}