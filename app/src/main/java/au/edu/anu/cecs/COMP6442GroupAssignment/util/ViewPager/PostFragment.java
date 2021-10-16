package au.edu.anu.cecs.COMP6442GroupAssignment.util.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import au.edu.anu.cecs.COMP6442GroupAssignment.PostActivity;
import au.edu.anu.cecs.COMP6442GroupAssignment.R;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserPostDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.FirebaseRef;

public class PostFragment extends Fragment {


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater,
                @Nullable ViewGroup container,
                @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.layout_post, container, false);

            RecyclerView timelinePostView = view.findViewById(R.id.timelinePostView);

            //Data
            UserPostDAO userPostDAO = UserPostDAO.getInstance((AppCompatActivity) getActivity());
            userPostDAO.getData();

            timelinePostView.setAdapter(userPostDAO.getPostsAdapter());

            timelinePostView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                    if (!userPostDAO.isLoading()) {
                        if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == userPostDAO.getPosts().size() - 1) {
                            //bottom of list!
                            userPostDAO.loadMore();
                            userPostDAO.setLoading();
                        }
                    }
                }
            });
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            timelinePostView.setLayoutManager(layoutManager);

            TextView search_bar =view.findViewById(R.id.searchText);
            Button do_search = view.findViewById(R.id.search_posts);

            ImageView round_search_button = view.findViewById(R.id.searchPostBut);

            ListView history = view.findViewById(R.id.search_hist);
            TextView search_grammar = view.findViewById(R.id.search_grammar);
            final ArrayList<String> hist_arr = new ArrayList();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    getContext(),
                    android.R.layout.simple_list_item_1,
                    hist_arr);
            history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    search_bar.setText(hist_arr.get(i));
                }
            });
            history.setAdapter(arrayAdapter);

            FirebaseRef firebaseRef = FirebaseRef.getInstance();
            FirebaseFirestore db = firebaseRef.getFirestore();
            FirebaseUser currentUser = firebaseRef.getFirebaseAuth().getCurrentUser();
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

            round_search_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (do_search.getVisibility() == View.VISIBLE){
                        do_search.setVisibility(View.INVISIBLE);
                        search_bar.setVisibility(View.INVISIBLE);
                        history.setVisibility(View.INVISIBLE);
                        search_grammar.setVisibility(View.INVISIBLE);
                        timelinePostView.setVisibility(View.VISIBLE);
                    } else {
                        do_search.setVisibility(View.VISIBLE);
                        search_bar.setVisibility(View.VISIBLE);
                        history.setVisibility(View.VISIBLE);
                        arrayAdapter.notifyDataSetChanged();
                        search_grammar.setVisibility(View.VISIBLE);
                        timelinePostView.setVisibility(View.INVISIBLE);
                    }
                }
            });

            do_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text = search_bar.getText().toString();
                    if (text.length()!=0){
                        hist_arr.add(text);
                        if (hist_arr.size() > 5) {
                            hist_arr.remove(0);
                        }
                        Map<String, Object> docData = new HashMap<>();
                        docData.put("searchHistory", hist_arr);
                        db.collection("user-data").document(
                                currentUser.getUid()
                        )
                                .set(docData)
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
                        userPostDAO.searchPost(text);

                        do_search.setVisibility(View.INVISIBLE);
                        search_bar.setVisibility(View.INVISIBLE);
                        history.setVisibility(View.INVISIBLE);
                        search_grammar.setVisibility(View.INVISIBLE);
                        timelinePostView.setVisibility(View.VISIBLE);
                    }
                }
            });


            Button newPost = view.findViewById(R.id.new_post_button);
            newPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), PostActivity.class);
                    startActivity(intent);
                }
            });

            return view;
        }
}
