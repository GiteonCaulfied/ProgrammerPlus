package au.edu.anu.cecs.COMP6442GroupAssignment.util.DataGenerator;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.UserManager;

public class ActGenerator {

    public void makeFriends(String uid1, String uid2) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user-profiles")
                .document(uid1)
                .update("friends", FieldValue.arrayUnion(uid2));
        db.collection("user-profiles")
                .document(uid2)
                .update("friends", FieldValue.arrayUnion(uid1));
    }

    public void likePosts() {

    }

    public void generateFriends() {
        UserManager userManager = UserManager.getInstance();
        userManager.getRandomID();
    }
}
