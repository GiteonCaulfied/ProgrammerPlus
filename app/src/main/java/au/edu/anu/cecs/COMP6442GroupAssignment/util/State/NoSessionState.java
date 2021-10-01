package au.edu.anu.cecs.COMP6442GroupAssignment.util.State;

import au.edu.anu.cecs.COMP6442GroupAssignment.MainActivity;
import au.edu.anu.cecs.COMP6442GroupAssignment.R;

public class NoSessionState implements UserState {

    private final MainActivity main;

    public NoSessionState(MainActivity main) {
        this.main = main;
    }

    @Override
    public void setContent() {
        this.main.setContentView(R.layout.activity_main);
    }

    @Override
    public void onCreate() {

    }
}
