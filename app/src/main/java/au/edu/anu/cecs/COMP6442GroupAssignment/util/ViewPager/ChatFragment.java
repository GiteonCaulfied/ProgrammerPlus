package au.edu.anu.cecs.COMP6442GroupAssignment.util.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import au.edu.anu.cecs.COMP6442GroupAssignment.R;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Adapter.ChatsAdapter;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Adapter.FriendsAdapter;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.FirebaseRef;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Profile;

public class ChatFragment extends Fragment {
    private FriendsAdapter friendsAdapter;
    private ArrayList<String> friends;
    private ArrayList<String> latestMess;
    private FirebaseRef firebaseRef;
    private FirebaseUser currentUser;
    private DatabaseReference myRef;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_chat, container, false);

        firebaseRef = FirebaseRef.getInstance();
        currentUser = firebaseRef.getFirebaseAuth().getCurrentUser();
        myRef = firebaseRef.getDatabaseRef();

        recyclerView = view.findViewById(R.id.chatRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        friends = new ArrayList<>();
        latestMess = new ArrayList<>();
        ChatsAdapter chatsAdapter = new ChatsAdapter(getContext(), friends, latestMess);
        recyclerView.setAdapter(chatsAdapter);

        myRef.child("user-chat").child(currentUser.getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friends.clear();
                latestMess.clear();
                HashMap<String, Object> chats = (HashMap<String, Object>) snapshot.getValue();
                // Loop for all chats

                for (String key: chats.keySet()) {
                    friends.add(key);

                    HashMap<String, Object> messages = (HashMap<String, Object>) chats.get(key);
                    long bt = -1;
                    String last = "";
                    for (Object value: messages.values()){
                        HashMap<String, Object> message = (HashMap<String, Object>) value;
                        if ((long) message.get("time") > bt) {
                            bt = (long) message.get("time");
                            last = (String) message.get("text");
                        }
                    }
                    latestMess.add(last);
                }
                chatsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}
