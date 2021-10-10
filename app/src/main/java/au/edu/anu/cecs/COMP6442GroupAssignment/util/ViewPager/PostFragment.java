package au.edu.anu.cecs.COMP6442GroupAssignment.util.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import au.edu.anu.cecs.COMP6442GroupAssignment.PostActivity;
import au.edu.anu.cecs.COMP6442GroupAssignment.R;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserPostDAO;

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
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            timelinePostView.setLayoutManager(layoutManager);

            TextView search_bar =view.findViewById(R.id.searchText);
            Button do_search = view.findViewById(R.id.search_posts);

            Button round_search_button = view.findViewById(R.id.round_search_button);

            round_search_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (do_search.getVisibility() == View.VISIBLE){
                        do_search.setVisibility(View.INVISIBLE);
                        search_bar.setVisibility(View.INVISIBLE);
                        timelinePostView.setVisibility(View.VISIBLE);
                    } else {
                        do_search.setVisibility(View.VISIBLE);
                        search_bar.setVisibility(View.VISIBLE);
                        timelinePostView.setVisibility(View.INVISIBLE);
                    }
                }
            });

            do_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text =search_bar.getText().toString();
                    if (text.length()!=0){
                        userPostDAO.searchPost(text);

                        do_search.setVisibility(View.INVISIBLE);
                        search_bar.setVisibility(View.INVISIBLE);
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
