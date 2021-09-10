package au.edu.anu.cecs.COMP6442GroupAssignment;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import au.edu.cecs.COMP6442GroupAssignment.Utils.*;

public class ProfileActivity extends AppCompatActivity {


    private TextView name,age;
    private Profile profile ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profile = DataBase.getProfiles(this).get(0);
        name = findViewById(R.id.name_Text);
        age = findViewById(R.id.age_Text);
        name.setText(profile.getName());
        age.setText(profile.getAge().toString());
    }
}