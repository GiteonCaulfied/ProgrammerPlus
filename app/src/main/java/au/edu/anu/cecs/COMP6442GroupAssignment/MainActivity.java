package au.edu.anu.cecs.COMP6442GroupAssignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.FirebaseRef;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.State.NoSessionState;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.State.SessionState;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.State.UserState;

public class MainActivity extends AppCompatActivity {
//    private ScheduledExecutorService mScheduledExecutorService = Executors.newScheduledThreadPool(4);
    private UserState currentState;
    private FirebaseUser currentUser;
    private FirebaseRef firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseRef = FirebaseRef.getInstance();
        currentUser = firebaseRef.getFirebaseAuth().getCurrentUser();
//        firebaseRef.getFirebaseAuth().signOut();
        if (currentUser == null)
            currentState = new NoSessionState(this);
        else
            currentState = new SessionState(this);

        currentState.setContent();
        currentState.onCreate();
    }

    public void signIn(View v) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void register(View v) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}