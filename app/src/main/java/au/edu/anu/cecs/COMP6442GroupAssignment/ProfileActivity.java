package au.edu.anu.cecs.COMP6442GroupAssignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;

import au.edu.cecs.COMP6442GroupAssignment.Utils.*;

public class ProfileActivity extends AppCompatActivity {


    private TextView name,posts,intro;
    private Profile profile ;//adm01@gg.com


    @Override
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent from_intent = getIntent();
        String account = from_intent.getStringExtra("account");
        HashMap<String,Profile> map = DataBase.getProfiles(this);
        profile = map.get(account);
        name = findViewById(R.id.name_Text);
        posts = findViewById(R.id.age_Text);
        intro = findViewById(R.id.Intro_content);
        name.setText(profile.getName());
        posts.setText(profile.getPosts().toString());
        intro.setText(profile.getIntro().toString());



    }
}