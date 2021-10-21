package au.edu.anu.cecs.COMP6442GroupAssignment.util.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import au.edu.anu.cecs.COMP6442GroupAssignment.PostActivity;
import au.edu.anu.cecs.COMP6442GroupAssignment.R;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserActivityDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserPostDAO;

public class PostFragment extends Fragment {
    /**
     * The post fragement, showing the posts based on three modes.
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_post, container, false);

        RecyclerView timelinePostView = view.findViewById(R.id.timelinePostView);

        // Back to the TimeLine before Searching by restart the activity
        ImageView back_button = view.findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().recreate();
            }
        });

        // Data
        UserPostDAO userPostDAO = UserPostDAO.getInstance((AppCompatActivity) getActivity());
        userPostDAO.getData(timelinePostView);

        // When the Page is Scrolled, Show more Posts
        timelinePostView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!userPostDAO.isLoading() && back_button.getVisibility() == View.INVISIBLE) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == userPostDAO.getPosts().size() - 1) {
                        //bottom of list!
                        userPostDAO.loadMore();
                        userPostDAO.setLoading(true);
                    }
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        timelinePostView.setLayoutManager(layoutManager);


        // Different Modes
        RadioGroup radioGroup = view.findViewById(R.id.radio_post_mode);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.TimeMode:
                        userPostDAO.setMode("Time");
                        userPostDAO.getData(timelinePostView);
                        Toast.makeText(getContext(), "Time Mode", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.LikeMode:
                        userPostDAO.setMode("Like");
                        userPostDAO.getData(timelinePostView);
                        Toast.makeText(getContext(), "Like Mode", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.ExploreMode:
                        userPostDAO.setMode("Uncomfortable");
                        userPostDAO.getData(timelinePostView);
                        Toast.makeText(getContext(), "Uncomfortable Mode", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        TextView search_bar = view.findViewById(R.id.searchText);
        Button do_search = view.findViewById(R.id.search_posts);

        ImageView round_search_button = view.findViewById(R.id.searchPostBut);


        // Load the Search History
        ListView history = view.findViewById(R.id.search_hist);
        TextView search_grammar = view.findViewById(R.id.search_grammar);
        final ArrayList<String> hist_arr = new ArrayList();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
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

        UserActivityDAO userActivityDAO = UserActivityDAO.getInstance();
        userActivityDAO.getSearchHistory(hist_arr, arrayAdapter);



        // Show the Search Page when the Round Search Button is Clicked
        round_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (do_search.getVisibility() == View.VISIBLE) {
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


        // Search the Posts and add to the search history
        do_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = search_bar.getText().toString();
                search_bar.setText("");
                if (text.length() != 0) {
                    hist_arr.add(text);
                    if (hist_arr.size() > 5) {
                        hist_arr.remove(0);
                    }
                    Map<String, Object> docData = new HashMap<>();
                    docData.put("searchHistory", hist_arr);
                    userActivityDAO.addSearchHistory(docData);
                    userPostDAO.searchPost(text);

                    do_search.setVisibility(View.INVISIBLE);
                    search_bar.setVisibility(View.INVISIBLE);
                    history.setVisibility(View.INVISIBLE);
                    search_grammar.setVisibility(View.INVISIBLE);
                    timelinePostView.setVisibility(View.VISIBLE);
                    back_button.setVisibility(View.VISIBLE);
                    round_search_button.setVisibility(View.INVISIBLE);
                }
            }
        });


        // Go to another page to Publish a new post
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
