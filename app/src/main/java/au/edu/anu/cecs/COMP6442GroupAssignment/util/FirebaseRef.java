package au.edu.anu.cecs.COMP6442GroupAssignment.util;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseRef {
    private static FirebaseRef instance;
    private DatabaseReference myRef;
    private FirebaseDatabase realtimeDatabase;

    public FirebaseRef() {
        realtimeDatabase = FirebaseDatabase.getInstance();
        myRef = realtimeDatabase.getReference();
    }

    public FirebaseRef getInstance() {
        if (instance == null) {
            instance = new FirebaseRef();
        }
        return instance;
    }
}
