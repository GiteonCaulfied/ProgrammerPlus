package au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Map;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.FirebaseRef;

public class UserActivityDAO {
    private static UserActivityDAO instance;
    private final FirebaseRef firebaseRef = FirebaseRef.getInstance();
    private final FirebaseFirestore db = firebaseRef.getFirestore();
    private final FirebaseUser currentUser = firebaseRef.getFirebaseAuth().getCurrentUser();

    /**
     * Singleton Pattern
     */
    public static UserActivityDAO getInstance() {
        if (instance == null)
            instance = new UserActivityDAO();
        return instance;
    }

    /**
     * Load the search History from the Firebase and show them.
     */
    public void getSearchHistory(ArrayList<String> hist_arr, ArrayAdapter<String> arrayAdapter) {

        db.collection("user-data")
                .document(currentUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Object his = value.get("searchHistory");
                if (his != null) {
                    hist_arr.clear();
                    hist_arr.addAll((ArrayList<String>) his);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * Add the search History and update the Firebase.
     */
    public void addSearchHistory(Map<String, Object> docData) {
        db.collection("user-data").document(
                currentUser.getUid()
        )
                .update(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Search History", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Search History", "Error writing document", e);
                    }
                });
    }

    /**
     * Update the user location in the Firebase
     */
    public void updateLocation(Map<String, Object> docData) {
        db.collection("user-data").document(currentUser.getUid())
                .update(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Location", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Location", "Error writing document", e);
                    }
                });
    }
}
