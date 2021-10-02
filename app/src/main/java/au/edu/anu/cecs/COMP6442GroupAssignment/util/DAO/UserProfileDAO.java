package au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.FirebaseRef;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Profile;

public class UserProfileDAO implements UserActivityDaoInterface {

    private DatabaseReference myRef;
    private FirebaseUser currentUser;
    private FirebaseRef fb;
    private Profile userprofile;
    private TextView name, email, intro;

    private static UserProfileDAO instance;

    private UserProfileDAO() {
        fb = FirebaseRef.getInstance();
        myRef = fb.getDatabaseRef();
        currentUser = fb.getFirebaseAuth().getCurrentUser();
    }

    private UserProfileDAO(TextView name, TextView email, TextView intro) {
        fb = FirebaseRef.getInstance();
        myRef = fb.getDatabaseRef();
        currentUser = fb.getFirebaseAuth().getCurrentUser();
        this.name = name;
        this.email = email;
        this.intro = intro;
    }

    public static UserProfileDAO getInstance() {
        if (instance == null) {
            instance = new UserProfileDAO();
        }
        return instance;
    }

    public static UserProfileDAO getInstance(TextView name, TextView email, TextView intro) {
        if (instance == null) {
            instance = new UserProfileDAO(name, email, intro);
        }
        return instance;
    }


    @Override
    public void getData() {
        myRef.child("user-profile").child(currentUser.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    userprofile = Objects.requireNonNull(task.getResult()).getValue(Profile.class);
                    name.setText(userprofile.getName());
                    email.setText(userprofile.getEmail());
                    intro.setText(userprofile.getIntro());
                }
            }
        });
    }

    @Override
    public void update() {

    }

    @Override
    public void create(String key, Map<String, Object> newValues) {
        currentUser = fb.getFirebaseAuth().getCurrentUser();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/user-profile/" + key + "/", newValues);

        myRef.updateChildren(childUpdates);

        // Update Firebase the user name
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName((String) newValues.get("name"))
                .build();
        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Register", "User profile updated.");
                        }
                    }
                });
    }

    @Override
    public void delete() {

    }

    public void clear() {
        instance = null;
    }

    public Profile getUserprofile() {
        if (userprofile == null)
            getData();
        return userprofile;
    }
}
