package au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO;

import android.util.Log;
import android.widget.Switch;
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
import com.google.firebase.database.GenericTypeIndicator;
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
    private Switch profile_only_fri;

    private UserProfileDAO() {
        FirebaseRef firebaseRef = FirebaseRef.getInstance();
        db = firebaseRef.getFirestore();
        currentUser = firebaseRef.getFirebaseAuth().getCurrentUser();
    }

    /**
     * Singleton Pattern
     */
    public static UserProfileDAO getInstance() {
        if (instance == null) {
            instance = new UserProfileDAO();
        }
        return instance;
    }

    public void updateViews(TextView name, TextView email, TextView intro, Switch profile_only_fri) {
        this.name = name;
        this.email = email;
        this.intro = intro;
        this.profile_only_fri = profile_only_fri;
    }

    /**
     * Load the Profile data from Firebase and show them in Profile page.
     */
    public void getDataInProfileFrag() {
        final DocumentReference docRef = db.collection("user-profiles").document(currentUser.getUid());
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
                    userprofile = new Profile(snapshot.getData());
                    name.setText(userprofile.getName());
                    email.setText(userprofile.getEmail());
                    intro.setText(userprofile.getIntro());
                    profile_only_fri.setChecked(userprofile.isOnlyFriMess());
                } else {
                    Log.d("Read profile", "Current data: null");
                }
            }
        });
    }

    /**
     * Read the Profile data from Firebase.
     */
    public void getData() {
        final DocumentReference docRef = db.collection("user-profiles").document(currentUser.getUid());
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
                    userprofile = new Profile(snapshot.getData());
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

    /**
     * Update the Portrait Uploaded Status to true and update in the Firebase.
     */
    public void updatePortraitUploadedStatus() {
        userprofile.setPortraitUploadedStatus();
        String uid = currentUser.getUid();
        Map<String, Object> postValues = userprofile.toMap();
        writeNewProfile(uid, postValues);
    }

    /**
     * Update user Profile information in the Firebase.
     */
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

        Map<String, Object> c = new HashMap<>();
        c.put("id", key);
        db.collection("user-data").document(key)
                .set(c);
    }

    /**
     * Write new Profile and upload to the Firebase.
     */
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

    /**
     * Add friend in the Profile and update in the Firebase.
     */
    public void addNewFriend(String uid) {
        userprofile.addNewFriend(uid);
        db.collection("user-profiles")
                .document(currentUser.getUid())
                .update("friends", FieldValue.arrayUnion(uid));
        db.collection("user-profiles")
                .document(uid)
                .update("friends", FieldValue.arrayUnion(currentUser.getUid()));
    }

    /**
     * Add blocked user in the Profile and update in the Firebase.
     */
    public void addNewBlocked(String uid) {
        userprofile.addNewBlock(uid);
        db.collection("user-profiles")
                .document(currentUser.getUid())
                .update("blocked", FieldValue.arrayUnion(uid));
    }

    /**
     * Cancel blocked user in the Profile and update in the Firebase.
     */
    public void cancelBlocked(String uid) {
        userprofile.cancelBlock(uid);
        db.collection("user-profiles")
                .document(currentUser.getUid())
                .update("blocked", FieldValue.arrayRemove(uid));
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
