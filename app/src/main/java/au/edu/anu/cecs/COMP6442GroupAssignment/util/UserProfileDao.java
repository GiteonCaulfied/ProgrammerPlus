package au.edu.anu.cecs.COMP6442GroupAssignment.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileDao {

    private DatabaseReference myRef;
    private FirebaseUser currentUser;

    private Profile userprofile;


    private static UserProfileDao instance;

    private UserProfileDao() { // use constructor to initialize
        myRef = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser.getEmail() != null) {
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // if detected that the realtime updated the userprofile then update local userprofile
                    userprofile = dataSnapshot.getValue(Profile.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
//     Log.w("Get profile", "loadPost:onCancelled", databaseError.toException());
                }
            };

            myRef.child("user-profile").child(currentUser.getUid()).addValueEventListener(postListener);

//   myRef.child("users").child(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//    @Override
//    public void onComplete(@NonNull Task<DataSnapshot> task) {
//     if (!task.isSuccessful()) {
//
//
//      Log.e("firebase", "Error getting data", task.getException());
//     }
//     else {
//      Log.d("firebase", String.valueOf(task.getResult().getValue()));
////      userprofile = task.getResult().getValue(Profile.class);
//     }
//    }
//   });


        }
    }

    public boolean profile_init (){
        if (userprofile != null){
            return true;
        }
        return false;
    }

    public static UserProfileDao getInstance() {
        if (instance == null) {
            instance = new UserProfileDao();
        }
        return instance;
    }

    public void updateProfile(Profile profile) {
        userprofile = profile;
    }

    public Profile getUserprofile() {
        return userprofile;
    }

    public void clear() {
        instance = null;
    }


}
