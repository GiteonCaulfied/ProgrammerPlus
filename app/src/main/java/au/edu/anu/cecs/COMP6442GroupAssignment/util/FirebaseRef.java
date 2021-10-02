package au.edu.anu.cecs.COMP6442GroupAssignment.util;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseRef {
    private static FirebaseRef instance;
    private DatabaseReference myRef;
    private FirebaseDatabase realtimeDatabase;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public FirebaseRef() {
        realtimeDatabase = FirebaseDatabase.getInstance();
        myRef = realtimeDatabase.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public static FirebaseRef getInstance() {
        if (instance == null) {
            instance = new FirebaseRef();
        }
        return instance;
    }

    public DatabaseReference getDatabaseRef() {
        return myRef;
    }

    public FirebaseDatabase getDatabase() {
        return realtimeDatabase;
    }

    public FirebaseFirestore getFirestore() {
        return firebaseFirestore;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }
}
