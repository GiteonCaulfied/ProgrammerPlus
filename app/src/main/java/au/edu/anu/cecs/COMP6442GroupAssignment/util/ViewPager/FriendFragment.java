package au.edu.anu.cecs.COMP6442GroupAssignment.util.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import au.edu.anu.cecs.COMP6442GroupAssignment.R;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Adapter.FriendsAdapter;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserPostDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserProfileDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.FirebaseRef;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Profile;

public class FriendFragment extends Fragment {
    private ArrayList<Profile> friends;
    private FriendsAdapter friendsAdapter;

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
        DatabaseReference myRef = firebaseRef.getDatabaseRef();
        FirebaseUser currentUser = firebaseRef.getFirebaseAuth().getCurrentUser();

        friends = new ArrayList<>();
        friendsAdapter = new FriendsAdapter(getContext(), friends);
        recyclerFriends.setAdapter(friendsAdapter);

        myRef.child("user-profile").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friends.clear();

                for (DataSnapshot ss: snapshot.getChildren()) {
                    Profile friend = ss.getValue(Profile.class);
                    assert friend != null;
                    assert currentUser != null;
                    if (!friend.getEmail().equals(currentUser.getEmail())) {
                        friends.add(friend);
                    }
                }
                friendsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}
