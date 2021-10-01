package au.edu.anu.cecs.COMP6442GroupAssignment.util.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            timelinePostView.setLayoutManager(layoutManager);

            return view;
        }
}
