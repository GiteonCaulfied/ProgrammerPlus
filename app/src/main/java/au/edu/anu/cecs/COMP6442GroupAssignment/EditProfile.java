package au.edu.anu.cecs.COMP6442GroupAssignment;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Map;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.SwearWordsDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserProfileDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.FirebaseRef;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Profile;

public class EditProfile extends AppCompatActivity {
    /**
     * This is the activity where a user can edit his or her
     * profile. The trick is that we re-use the register
     * layout, with some EditText elements not enabled.
     * That means the user cannot change his email address
     * and his name.
     */

    private final int PICK_IMAGE_REQUEST = 21;
    private Uri filePath;
    private SwearWordsDAO swearWords = (SwearWordsDAO) SwearWordsDAO.getInstance();

    private EditText email, name, password, confirm, intro, maskedWord;
    private Switch profile_only_fri;
    private ImageView portrait;
    private FirebaseAuth mAuth;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Profile user;
    private UserProfileDAO userProfileDAO;
    private boolean onlyFriMess;

    private Button selectPortraitBtn, signUp, addWord, deleteWord;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email_signup);
        name = findViewById(R.id.name_signup);
        password = findViewById(R.id.password_signup);
        confirm = findViewById(R.id.confirm_signup);
        intro = findViewById(R.id.intro_signup);
        signUp = findViewById(R.id.signUp);
        profile_only_fri = findViewById(R.id.friend_switch);

        portrait = findViewById(R.id.portrait_signup);
        selectPortraitBtn = findViewById(R.id.selectImage_signup);

        maskedWord = findViewById(R.id.MaskedWords);
        addWord = findViewById(R.id.add_word);
        deleteWord = findViewById(R.id.delete_word);

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

        // Because it's modifying profile
        email.setEnabled(false);
        name.setEnabled(false);
        signUp.setText("SAVE");
        signUp.setOnClickListener(this::save);
        userProfileDAO = UserProfileDAO.getInstance();
        user = userProfileDAO.getUserprofile();

        email.setText(user.getEmail());
        name.setText(user.getName());
        password.setText("********");
        confirm.setText("********");
        intro.setText(user.getIntro());

        onlyFriMess = user.isOnlyFriMess();
        profile_only_fri.setChecked(onlyFriMess);
        profile_only_fri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                onlyFriMess = b;
            }
        });


        deleteWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = maskedWord.getText().toString();
                if (!swearWords.delete(word)) {
                    Toast
                            .makeText(getApplicationContext(),
                                    "no such word",
                                    Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast
                            .makeText(getApplicationContext(),
                                    "deleted",
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });


        addWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = maskedWord.getText().toString();
                if (!swearWords.add(word)) {
                    Toast
                            .makeText(getApplicationContext(),
                                    "already exist",
                                    Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast
                            .makeText(getApplicationContext(),
                                    "added",
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        //Display Portrait Image
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.face_id_1)
                .error(R.drawable.face_id_1)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false);
        storageReference.child("portrait/" + user.getUid())
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri.toString())
                        .apply(options)
                        .into(portrait);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
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
     *
     * @param requestCode
     * @param resultCode
     * @param data
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
                portrait.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    /**
     * Save the Edited information, upload the data to the Firebase and
     * go to the main page.
     *
     * @param v View
     */
    public void save(View v) {
        String uid = mAuth.getCurrentUser().getUid();
        String email_str = email.getText().toString();
        String pass_str = password.getText().toString();
        String conf_str = confirm.getText().toString();
        String name_str = name.getText().toString();
        String intro_str = intro.getText().toString();

        if (!pass_str.equals(conf_str)) {
            Toast.makeText(getApplicationContext(),
                    "Passwords inconsistent!", Toast.LENGTH_SHORT).show();
            return;
        }

        user.setName(name_str);
        user.setEmail(email_str);
        user.setIntro(intro_str);
        user.setOnlyFriMess(onlyFriMess);
        Map<String, Object> postValues = user.toMap();
        userProfileDAO.create(uid, postValues);

        // Upload the Edited information to the Firebase
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name_str)
                .build();
        FirebaseUser currentUser = FirebaseRef.getInstance().getFirebaseAuth().getCurrentUser();
        assert currentUser != null;
        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Edit profile", "User profile updated.");
                        }
                    }
                });

        if (!pass_str.equals("********"))
            currentUser.updatePassword(pass_str)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("Edit profile", "User password updated.");
                            }
                        }
                    });

        // Upload the Portrait (if any)
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
                "Edit profile successfully.", Toast.LENGTH_SHORT).show();

        // Go to the Home page
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

//    public void addWord(View view) {
//
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
