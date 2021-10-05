package au.edu.anu.cecs.COMP6442GroupAssignment.util.ViewPager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import au.edu.anu.cecs.COMP6442GroupAssignment.R;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Adapter.FriendsAdapter;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Adapter.RequestsAdapter;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserPostDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserProfileDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.FirebaseRef;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Profile;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.SearchFriendDialog;

public class FriendFragment extends Fragment {
    private ArrayList<Profile> friends;
    private FriendsAdapter friendsAdapter;
    private Profile me;
    private ArrayList<Profile> allUsers;
    private ArrayList<String> req_emails;
    private ArrayList<String> req_uids;
    private HashMap<String, Object> dataSnapShot;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_friend, container, false);
        RecyclerView recyclerFriends = view.findViewById(R.id.friendRecycler);
        recyclerFriends.setHasFixedSize(true);
        recyclerFriends.setLayoutManager(new LinearLayoutManager(getContext()));

        //Data
        FirebaseRef firebaseRef = FirebaseRef.getInstance();
        FirebaseFirestore db = firebaseRef.getFirestore();
        FirebaseUser currentUser = firebaseRef.getFirebaseAuth().getCurrentUser();

        friends = new ArrayList<>();
        friendsAdapter = new FriendsAdapter(getContext(), friends);
        recyclerFriends.setAdapter(friendsAdapter);

        final CollectionReference colRef = db.collection("user-profiles");
        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("Read users", "Listen failed.", error);
                    return;
                }

                friends.clear();
                allUsers = new ArrayList<>();

                for (DocumentSnapshot ss: value.getDocuments()) {
                    Profile p = new Profile(ss.getData());
                    assert p != null;
                    if (!p.getUid().equals(currentUser.getUid()))
                        allUsers.add(p);
                    else
                        me = p;
                }

                for (Profile p: allUsers) {
                    if (me.friendContain(p.getUid())) {
                        friends.add(p);
                    }
                }
                friendsAdapter.notifyDataSetChanged();
            }
        });

        // Friend request
        req_emails = new ArrayList<>();
        req_uids = new ArrayList<>();
        dataSnapShot = new HashMap<>();
        RequestsAdapter requestsAdapter = new RequestsAdapter(getContext(), req_emails, dataSnapShot, req_uids);
        RecyclerView recyclerRequest = view.findViewById(R.id.requestRecycler);
        recyclerRequest.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerRequest.setAdapter(requestsAdapter);

        final DocumentReference friRef = db.collection("friend-request")
                .document(currentUser.getEmail());
        friRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                req_emails.clear();
                req_uids.clear();
                dataSnapShot = (HashMap<String, Object>) value.getData();
                if (dataSnapShot != null) {
                    for (String key : dataSnapShot.keySet()) {
                        Object o = dataSnapShot.get(key);
                        req_emails.add((String) o);
                        req_uids.add(key);
                    }
                }
                requestsAdapter.notifyDataSetChanged();
            }
        });

        ImageView search = view.findViewById(R.id.searchFriend);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFriendDialog dialog = new SearchFriendDialog(Objects.requireNonNull(getContext()), R.style.Dialog);
                dialog.show();
            }
        });

        return view;
    }
}
