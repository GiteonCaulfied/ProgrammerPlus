package au.edu.anu.cecs.COMP6442GroupAssignment.util.DataGenerator;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserProfileDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Profile;

public class UserGenerator {
    private FirebaseAuth mAuth;
    private Context context;

    public UserGenerator(Context context) {
        mAuth = FirebaseAuth.getInstance();
        this.context = context;
    }

    public void generateOne(String email_str, String pass_str,
                            String name_str, String intro_str) {
        mAuth.createUserWithEmailAndPassword(
                email_str, pass_str)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Register", "createUserWithEmail:success");
                            String uid = mAuth.getCurrentUser().getUid();
                            Profile profile = new Profile(
                                    uid,
                                    email_str,
                                    name_str,
                                    intro_str,
                                    false);
                            Map<String, Object> postValues = profile.toMap();

                            UserProfileDAO userProfileDao = UserProfileDAO.getInstance();
                            userProfileDao.create(uid, postValues);
                        }
                    }
                });
    }

    public Set<String> readUserNames() {
        Set<String> usernames = new HashSet<>();
        try {
            // Read usernames
            InputStreamReader reader = new InputStreamReader(
                    context.getResources().getAssets().open("usernames"));
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            while (line != null) {
                usernames.add(line);
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return usernames;
    }
}
