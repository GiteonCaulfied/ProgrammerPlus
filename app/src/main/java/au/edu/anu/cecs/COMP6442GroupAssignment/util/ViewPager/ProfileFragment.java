package au.edu.anu.cecs.COMP6442GroupAssignment.util.ViewPager;

import static android.app.Activity.RESULT_OK;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import au.edu.anu.cecs.COMP6442GroupAssignment.EditProfile;
import au.edu.anu.cecs.COMP6442GroupAssignment.MainActivity;
import au.edu.anu.cecs.COMP6442GroupAssignment.PostActivity;
import au.edu.anu.cecs.COMP6442GroupAssignment.R;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserProfileDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.FirebaseRef;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Profile;

public class ProfileFragment extends Fragment {
    private final int PICK_IMAGE_REQUEST = 21;
    private Uri filePath;
    private ProgressDialog progressDialog;

    private TextView name, email, intro, intro_edit;
    private ImageView NowImage, selectedImage;
    private Button logOut, edit, save, uploadImageBtn;
    private DatabaseReference myRef;
    private FirebaseUser currentUser;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_profile, container, false);
        FirebaseRef firebaseRef = FirebaseRef.getInstance();
        myRef = firebaseRef.getDatabaseRef();
        currentUser = firebaseRef.getFirebaseAuth().getCurrentUser();

        name = view.findViewById(R.id.name_Text);
        email = view.findViewById(R.id.email_Text);
        intro = view.findViewById(R.id.intro_Text);
        intro_edit = view.findViewById(R.id.intro_edit);

        logOut = view.findViewById(R.id.LogOut);
        edit = view.findViewById(R.id.edit);
        save = view.findViewById(R.id.save);
        uploadImageBtn = view.findViewById(R.id.profile_image_upload);

        NowImage = view.findViewById(R.id.profile_image_now);
        selectedImage = view.findViewById(R.id.profile_image_select);

        // get the Firebase  storage reference
        FirebaseRef fb = FirebaseRef.getInstance();
        storage = fb.getStorage();
        storageReference = fb.getStorageReference();

        UserProfileDAO userProfileDao = UserProfileDAO.getInstance();
        userProfileDao.updateViews(name, email, intro);
        userProfileDao.getDataInProfileFrag();

        //Display Portrait Image
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.face_id_1)
                .error(R.drawable.face_id_1)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false);
        storageReference.child("portrait/"+currentUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(getContext())
                        .load(uri.toString())
                        .apply(options)
                        .into(NowImage);
                userProfileDao.updatePortraitUploadedStatus();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                userProfileDao.clear();
                Intent intent = new Intent();
                intent.setClass(view.getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfile.class);
                startActivity(intent);
            }
        });

        // on pressing btnSelect SelectImage() is called
        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_intro = intro_edit.getText().toString();
                intro.setText(new_intro);
                userProfileDao.updateIntro(new_intro);

                if (filePath != null) {

                    // Code for showing progressDialog while uploading
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    // Defining the child of storageReference
                    StorageReference ref
                            = storageReference
                            .child(
                                    "portrait/"
                                            + currentUser.getUid());

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
                                                    + currentUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Log.e("==>", uri.toString());
                                                }
                                            });
                                            if (progressDialog != null) {

                                                progressDialog.dismiss();
                                                Toast
                                                        .makeText(getContext(),
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
                                            .makeText(getContext(),
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

                // Update the User Profile
                userProfileDao.updatePortraitUploadedStatus();

                save.setVisibility(View.GONE);
                uploadImageBtn.setVisibility(View.GONE);
                intro_edit.setVisibility(View.GONE);
                selectedImage.setVisibility(View.GONE);
                intro.setVisibility(View.VISIBLE);

            }
        });


        return view;
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
                        .getBitmap(getActivity().getContentResolver(),
                                filePath);
                selectedImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
}
