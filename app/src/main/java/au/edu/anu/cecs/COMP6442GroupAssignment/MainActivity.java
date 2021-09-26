package au.edu.anu.cecs.COMP6442GroupAssignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import au.edu.cecs.COMP6442GroupAssignment.Utils.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        DataBase.getProfiles(this);
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