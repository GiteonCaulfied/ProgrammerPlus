package au.edu.anu.cecs.COMP6442GroupAssignment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserPostDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.FirebaseRef;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.MyLocationManager;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Post;

public class PostActivity extends AppCompatActivity {

    // request code
    private final int PICK_IMAGE_REQUEST = 22;
    private FirebaseDatabase database;
    private EditText title, content, tags;
    private DatabaseReference myRef;
    private FirebaseStorage storage;
    private TextView location;
    private StorageReference storageReference;
    // view for image view
    private ImageView imageView;
    // Uri indicates, where the image will be picked from
    private Uri filePath;
    // views for button
    private Button btnSelect, shareMyLoc;
    private ProgressDialog progressDialog;
    private HashMap<String, Object> locationMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpost);
        FirebaseRef fb = FirebaseRef.getInstance();

        database = fb.getDatabase();
        title = findViewById(R.id.post_title);
        content = findViewById(R.id.post_content);
        tags = findViewById(R.id.post_tag);
        myRef = fb.getDatabaseRef();

        imageView = findViewById(R.id.post_uploaded_image);
        imageView.setImageResource(R.drawable.face_id_1);
        btnSelect = findViewById(R.id.post_select_button);

        // get the Firebase  storage reference
        storage = fb.getStorage();
        storageReference = fb.getStorageReference();

        // on pressing btnSelect SelectImage() is called
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        // Share the user's GPS information
        location = findViewById(R.id.LocationInfo);
        shareMyLoc = findViewById(R.id.ShareMyLoc);
        MyLocationManager mlm = new MyLocationManager(this);
        shareMyLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationMap = mlm.getLocation();
                if (locationMap != null) {
                    location.setText("GPS information - Longitude: " + locationMap.get("Longitude") +
                            "; Latitude: " + locationMap.get("Latitude") +
                            ";\nCity: " + locationMap.get("Address"));
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    // Select Image method
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

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
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
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }


    public void newPost(View v) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = myRef.child("posts").push().getKey();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        Post post = new Post(key,
                currentUser.getDisplayName(),
                uid,
                title.getText().toString(),
                content.getText().toString(),
                "",
                locationMap);

        if (tags.getText().toString().length() != 0){
            post.setTags(tags.getText().toString());
        }

        if (filePath != null) {

            // Set the Image Address
            post.setImageAddress(filePath.getPath());

            // Code for showing progressDialog while uploading
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + key);

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


                                    ref.child("images/"
                                            + key).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.e("==>", uri.toString());
                                        }
                                    });
                                    if (progressDialog != null) {

                                        progressDialog.dismiss();
                                        Toast
                                                .makeText(PostActivity.this,
                                                        "Image Uploaded!!",
                                                        Toast.LENGTH_SHORT)
                                                .show();
                                    }

                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(PostActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }

        Map<String, Object> postValues = post.toMap();

        UserPostDAO userPostDAO = UserPostDAO.getInstance(this);
        userPostDAO.create(key, postValues);
        Toast.makeText(getApplicationContext(),
                "Post successfully.", Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != progressDialog) {
            progressDialog.dismiss();
        }
    }

}