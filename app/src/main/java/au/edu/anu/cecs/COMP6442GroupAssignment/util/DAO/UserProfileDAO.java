package au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.FirebaseRef;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Profile;

public class UserProfileDAO {

    private static UserProfileDAO instance;
    private final FirebaseUser currentUser;
    private final FirebaseFirestore db;
    private Profile userprofile;
    private TextView name, email, intro;

    private UserProfileDAO() {
        FirebaseRef firebaseRef = FirebaseRef.getInstance();
        db = firebaseRef.getFirestore();
        currentUser = firebaseRef.getFirebaseAuth().getCurrentUser();
    }

    private UserProfileDAO(TextView name, TextView email, TextView intro) {
        FirebaseRef firebaseRef = FirebaseRef.getInstance();
        db = firebaseRef.getFirestore();
        currentUser = firebaseRef.getFirebaseAuth().getCurrentUser();
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

    public void updateViews(TextView name, TextView email, TextView intro) {
        this.name = name;
        this.email = email;
        this.intro = intro;
    }

    public void getDataInProfileFrag() {
        final DocumentReference docRef = db.collection("user-profile").document(currentUser.getUid());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Read profile", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("Read profile", "Current data: " + snapshot.getData());
                    userprofile = snapshot.toObject(Profile.class);
                    name.setText(userprofile.getName());
                    email.setText(userprofile.getEmail());
                    intro.setText(userprofile.getIntro());
                } else {
                    Log.d("Read profile", "Current data: null");
                }
            }
        });
    }

    public void getData() {
        final DocumentReference docRef = db.collection("user-profile").document(currentUser.getUid());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Read profile", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("Read profile", "Current data: " + snapshot.getData());
                    userprofile = snapshot.toObject(Profile.class);
                } else {
                    Log.d("Read profile", "Current data: null");
                }
            }
        });
    }


    public void updateIntro(String intro) {
        userprofile.setIntro(intro);
        String uid = currentUser.getUid();
        Map<String, Object> postValues = userprofile.toMap();
        writeNewProfile(uid, postValues);
    }

    public void updatePortraitUploadedStatus() {
        userprofile.setPortraitUploadedStatus();
        String uid = currentUser.getUid();
        Map<String, Object> postValues = userprofile.toMap();
        writeNewProfile(uid, postValues);
    }


    public void create(String key, Map<String, Object> newValues) {
        writeNewProfile(key, newValues);

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

    private void writeNewProfile(String key, Map<String, Object> newValues) {
        db.collection("user-profiles").document(key)
                .set(newValues)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Profile", "New profile successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Profile", "Error writing profile", e);
                    }
                });
    }

    public void addNewFriend(String uid) {
        db.collection("user-profiles")
                .document(currentUser.getUid())
                .update("friends", FieldValue.arrayUnion(uid));
        db.collection("user-profiles")
                .document(uid)
                .update("friends", FieldValue.arrayUnion(currentUser.getUid()));
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
