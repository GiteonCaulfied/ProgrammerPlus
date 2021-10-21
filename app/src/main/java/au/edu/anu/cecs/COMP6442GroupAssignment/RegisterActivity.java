package au.edu.anu.cecs.COMP6442GroupAssignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Map;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.SwearWordsDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserProfileDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.FirebaseRef;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Profile;

public class RegisterActivity extends AppCompatActivity {
    /**
     * The activity where a new user can register
     *
     * When writing the code w.r.t Firebase, we refer to the
     * Firebase document
     * https://firebase.google.com/docs/auth/android/firebaseui?authuser=0
     */

    private final int PICK_IMAGE_REQUEST = 21;
    private Uri filePath;

    private EditText email, name, password, confirm, intro,maskedWord;
    private ImageView profile;
    private FirebaseAuth mAuth;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private Button selectPortraitBtn, signUp, addWord, deleteWord;
    private Switch onlyFriends;
    private boolean onlyFriMess;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private SwearWordsDAO swearWords = (SwearWordsDAO) SwearWordsDAO.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Find elements that we need
        email = findViewById(R.id.email_signup);
        name = findViewById(R.id.name_signup);
        password = findViewById(R.id.password_signup);
        confirm = findViewById(R.id.confirm_signup);
        intro = findViewById(R.id.intro_signup);
        signUp = findViewById(R.id.signUp);
        maskedWord = findViewById(R.id.MaskedWords);

        addWord = findViewById(R.id.add_word);
        deleteWord=findViewById(R.id.delete_word);

        profile = findViewById(R.id.portrait_signup);
        selectPortraitBtn = findViewById(R.id.selectImage_signup);

        onlyFriends = findViewById(R.id.friend_switch);
        onlyFriMess = false;
        onlyFriends.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                onlyFriMess = b;
            }
        });

//        deleteWord.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });


//        addWord.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        mAuth = FirebaseAuth.getInstance();

        // get the Firebase  storage reference
        FirebaseRef fb = FirebaseRef.getInstance();
        storage = fb.getStorage();
        storageReference = fb.getStorageReference();

        // on pressing btnSelect SelectImage() is called
        selectPortraitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        signUp.setOnClickListener(this::signUp);
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

    /**
     * Select Image from user's Phone.
     */
    private void SelectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    /**
     * When the image is selected, show the image in a image view to the user.
     */
    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(getContentResolver(),
                                filePath);
                profile.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    /**
     * Register with the Email and Password, upload the data to the Firebase and
     * go to the main page.
     *
     * @param v View
     */
    public void signUp(View v) {
        String email_str = email.getText().toString();
        String pass_str = password.getText().toString();
        String conf_str = confirm.getText().toString();
        String name_str = name.getText().toString();
        String intro_str = intro.getText().toString();

        // if the password is not consistent
        if (!pass_str.equals(conf_str)) {
            Toast.makeText(getApplicationContext(),
                    "Passwords inconsistent!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a User Profile and upload to the Firebase
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
                            onlyFriMess);
                    Map<String, Object> postValues = profile.toMap();

                    UserProfileDAO userProfileDao = UserProfileDAO.getInstance();
                    userProfileDao.create(uid, postValues);

                    if (filePath != null) {



                        // Defining the child of storageReference
                        StorageReference ref
                                = storageReference
                                .child(
                                        "portrait/"
                                                + uid);

                        // adding listeners on upload
                        // or failure of image
                        ref.putFile(filePath)
                                .addOnSuccessListener(
                                        new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                            @Override
                                            public void onSuccess(
                                                    UploadTask.TaskSnapshot taskSnapshot) {

                                                // Image uploaded successfully
                                                // Dismiss dialog
                                                //


                                                ref.child("portrait/"
                                                        + uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        Log.e("==>", uri.toString());
                                                    }
                                                });


                                            }
                                        })

                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                    }

                    Toast.makeText(getApplicationContext(),
                            "Register successfully.", Toast.LENGTH_SHORT).show();

                    // Go to the Home page
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    public void addWord(View view) {
//        int i =111;
//        String word = maskedWord.getText().toString();
//        if (!swearWords.add(word)){
//            Toast
//                    .makeText(getApplicationContext(),
//                            "already exist",
//                            Toast.LENGTH_SHORT)
//                    .show();
//        }else {
//            Toast
//                    .makeText(getApplicationContext(),
//                            "added",
//                            Toast.LENGTH_SHORT)
//                    .show();
//        }
//    }
//
//    public void deleteWord(View view) {
//        String word = maskedWord.getText().toString();
//        if (!swearWords.delete(word)){
//            Toast
//                    .makeText(getApplicationContext(),
//                            "no such word",
//                            Toast.LENGTH_SHORT)
//                    .show();
//        }else {
//            Toast
//                    .makeText(getApplicationContext(),
//                            "deleted",
//                            Toast.LENGTH_SHORT)
//                    .show();
//        }
//
//    }
}