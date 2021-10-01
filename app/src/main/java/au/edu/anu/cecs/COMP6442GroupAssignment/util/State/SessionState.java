package au.edu.anu.cecs.COMP6442GroupAssignment.util.State;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import au.edu.anu.cecs.COMP6442GroupAssignment.MainActivity;
import au.edu.anu.cecs.COMP6442GroupAssignment.R;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserPostDAO;

public class SessionState implements UserState {
    private final MainActivity main;

    public SessionState(MainActivity main) {
        this.main = main;
    }

    @Override
    public void setContent() {
        main.setContentView(R.layout.activity_homepage);
    }

    @Override
    public void onCreate() {
        RecyclerView timelinePostView = main.findViewById(R.id.timelinePostView);

        //Data
        UserPostDAO userPostDAO = UserPostDAO.getInstance(main);
        userPostDAO.getData();

        timelinePostView.setAdapter(userPostDAO.getPostsAdapter());
        LinearLayoutManager layoutManager = new LinearLayoutManager(main);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        timelinePostView.setLayoutManager(layoutManager);
    }
}
