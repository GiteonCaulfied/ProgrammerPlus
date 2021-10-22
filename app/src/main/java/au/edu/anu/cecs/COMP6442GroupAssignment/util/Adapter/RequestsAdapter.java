package au.edu.anu.cecs.COMP6442GroupAssignment.util.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import au.edu.anu.cecs.COMP6442GroupAssignment.R;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserProfileDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.FirebaseRef;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.UserManager;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {
    /**
     * An adapter to show friend request to the current user on RecyclerView
     * A request item contains:
     * (1) that user name
     * (2) that user email
     * (3) that user id
     * (4) decline button
     * (5) accept button
     */

    private final Context context;
    private final ArrayList<String> req_emails;
    private final HashMap<String, Object> dataSnapShot;
    private final ArrayList<String> req_uids;
    private final UserManager userManager;

    public RequestsAdapter(Context context, ArrayList<String> req_emails,
                           HashMap<String, Object> dataSnapShot, ArrayList<String> req_uids) {
        this.context = context;
        this.req_emails = req_emails;
        this.dataSnapShot = dataSnapShot;
        this.req_uids = req_uids;
        this.userManager = UserManager.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_request,
                parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String email = req_emails.get(position);
        String uid = req_uids.get(position);
        holder.email.setText(email);
        holder.name.setText(userManager.getNameFromID(uid));
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserProfileDAO userProfileDAO = UserProfileDAO.getInstance();
                userProfileDAO.addNewFriend(uid);
                deleteRequest(uid);
            }
        });

        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRequest(uid);
            }
        });
    }

    private void deleteRequest(String uid) {
        FirebaseRef firebaseRef = FirebaseRef.getInstance();
        FirebaseFirestore db = firebaseRef.getFirestore();
        FirebaseUser currentUser = firebaseRef.getFirebaseAuth().getCurrentUser();
        DocumentReference docRef = db.collection("friend-request")
                .document(currentUser.getEmail());

        // Remove the 'capital' field from the document
        Map<String, Object> updates = new HashMap<>();
        updates.put(uid, FieldValue.delete());
        docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //
            }
        });
    }

    @Override
    public int getItemCount() {
        return req_emails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, email;
        public Button accept, decline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.req_name);
            email = itemView.findViewById(R.id.req_mail);
            accept = itemView.findViewById(R.id.req_accpet);
            decline = itemView.findViewById(R.id.req_decline);
        }
    }
}
