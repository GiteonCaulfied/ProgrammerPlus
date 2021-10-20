package au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.FirebaseRef;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Message;

public class MessageDAO {
    private DatabaseReference myRef;

    private static MessageDAO instance;

    public MessageDAO() {
        FirebaseRef firebaseRef = FirebaseRef.getInstance();
        myRef = firebaseRef.getDatabaseRef();
    }

    /**
     * Send a new Message and upload to the Firebase.
     */
    public void sendMessage(String user1, String user2,
                            String message, String whoSent) {
        Message mes = new Message(message, whoSent);
        myRef.child("user-chat")
                .child(user1)
                .child(user2)
                .push().setValue(mes.toMap());
    }

    /**
     * Send a new admin Message and upload to the Firebase.
     */
    public void sendAdminMessage(String user1, String user2,
                                 String message, String whoSent,String pid) {
        Message mes = new Message(message, whoSent,"adminMessage",pid);
        myRef.child("user-chat")
                .child(user1)
                .child(user2)
                .push().setValue(mes.toMap2());
    }

    /**
     * Delete a Message and update the Firebase.
     */
    public void deleteChat(String user1, String user2) {
        Map<String, Object> postValues = new HashMap<>();
        postValues.put(user2, null);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("user-chat/" + user1, postValues);

        myRef.updateChildren(childUpdates);
    }

    /**
     * Singleton Pattern
     */
    public static MessageDAO getInstance() {
        if (instance == null)
            instance = new MessageDAO();
        return instance;
    }
}
