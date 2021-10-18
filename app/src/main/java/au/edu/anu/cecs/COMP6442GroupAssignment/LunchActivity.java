package au.edu.anu.cecs.COMP6442GroupAssignment;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LunchActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        new Thread( new Runnable( ) {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(2000);
                        if (currentUser!=null){
                            intent = new Intent(LunchActivity.this, MainActivity.class);
                        }else {
                            intent = new Intent(LunchActivity.this, LoginActivity.class);
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        LunchActivity.this.finish();
                    }
                });
            }
        } ).start();
    }

}
