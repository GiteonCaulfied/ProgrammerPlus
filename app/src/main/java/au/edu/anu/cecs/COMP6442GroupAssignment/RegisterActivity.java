package au.edu.anu.cecs.COMP6442GroupAssignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import au.edu.cecs.COMP6442GroupAssignment.Utils.Profile;

public class RegisterActivity extends AppCompatActivity {

    private EditText email, name, password, confirm, intro;
    private ImageView profile;
    private FirebaseAuth mAuth;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email_signup);
        name = findViewById(R.id.name_signup);
        password = findViewById(R.id.password_signup);
        confirm = findViewById(R.id.confirm_signup);
        intro = findViewById(R.id.intro_signup);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            mAuth.signOut();
        }
    }

    public void signUp(View v) {
        String email_str = email.getText().toString();
        String pass_str = password.getText().toString();
        String conf_str = confirm.getText().toString();
        if (!pass_str.equals(conf_str)) {
            Toast.makeText(getApplicationContext(),
                    "Passwords inconsistent!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(
                email_str, pass_str)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Register", "createUserWithEmail:success");
                    myRef = FirebaseDatabase.getInstance().getReference();

                    /*myRef.child("user-profile").child("number-of-users").get()
                            .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("firebase", "Error getting data", task.getException());
                                    } else {
                                        Log.d("firebase", "Get profile data");
                                        int userNum = (int) Objects.requireNonNull(task.getResult()).getValue();
                                        System.out.println(userNum);
                                        userNum ++;


                                    }
                                }});*/
                    String uid = mAuth.getCurrentUser().getUid();
                    Profile profile = new Profile(
                            uid,
                            email_str,
                            name.getText().toString(),
                            intro.getText().toString());
                    Map<String, Object> postValues = profile.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();

                    childUpdates.put("/user-profile/" + uid + "/", postValues);

                    myRef.updateChildren(childUpdates);

                    Toast.makeText(getApplicationContext(),
                            "Register successfully.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, HomepageActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}