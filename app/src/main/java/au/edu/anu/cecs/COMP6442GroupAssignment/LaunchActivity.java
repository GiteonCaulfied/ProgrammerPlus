package au.edu.anu.cecs.COMP6442GroupAssignment;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.appcompat.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity {
    /**
     * This is the launch activity. We will display a small
     * animation and welcome the user to use our app.
     */

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        // an opening Picture for the app
        new Thread( new Runnable( ) {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(1200);
                        intent = new Intent(LaunchActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        LaunchActivity.this.finish();
                    }
                });
            }
        } ).start();
    }

}
