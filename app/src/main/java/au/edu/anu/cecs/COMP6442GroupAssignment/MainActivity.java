package au.edu.anu.cecs.COMP6442GroupAssignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.DataBase;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.State.NoSessionState;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.State.SessionState;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.State.UserState;

public class MainActivity extends AppCompatActivity {
//    private ScheduledExecutorService mScheduledExecutorService = Executors.newScheduledThreadPool(4);
    private UserState currentState;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null)
            currentState = new NoSessionState(this);
        else
            currentState = new SessionState(this);

        currentState.setContent();
        currentState.onCreate();

//        DataBase.getProfiles(this);
//
//        mScheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
//            @Override
//            public void run() {
//                Log.e("lzp", "Stream updated" + System.currentTimeMillis() / 1000);
////                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, 1, 1, TimeUnit.SECONDS);
//

    }

    public void signIn(View v) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void register(View v) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    // New Post Button is Clicked, Send the account message and turn to PostActivity
    public void newPostButtonClick(View v){
        Intent intent = new Intent(MainActivity.this, PostActivity.class);
        startActivity(intent);
    }

    public void goToProfile(View view) {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}