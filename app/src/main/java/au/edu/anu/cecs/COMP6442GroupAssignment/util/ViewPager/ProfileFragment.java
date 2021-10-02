package au.edu.anu.cecs.COMP6442GroupAssignment.util.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import au.edu.anu.cecs.COMP6442GroupAssignment.MainActivity;
import au.edu.anu.cecs.COMP6442GroupAssignment.R;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserProfileDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Profile;

public class ProfileFragment extends Fragment {
    private TextView name, email, intro;
    private Button logOut;
    private DatabaseReference myRef;
    private FirebaseUser currentUser;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_profile, container, false);
        myRef = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        name = view.findViewById(R.id.name_Text);
        email = view.findViewById(R.id.email_Text);
        intro = view.findViewById(R.id.intro_Text);
        logOut = view.findViewById(R.id.LogOut);


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

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                UserProfileDAO userProfileDao = UserProfileDAO.getInstance();
                userProfileDao.clear();
                Intent intent = new Intent();
                intent.setClass(view.getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        return view;
    }
}
