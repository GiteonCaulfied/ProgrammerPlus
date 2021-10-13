package au.edu.anu.cecs.COMP6442GroupAssignment.util.ViewPager;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

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
import com.google.firebase.storage.StorageReference;


import au.edu.anu.cecs.COMP6442GroupAssignment.EditProfile;
import au.edu.anu.cecs.COMP6442GroupAssignment.MainActivity;
import au.edu.anu.cecs.COMP6442GroupAssignment.R;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserProfileDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.FirebaseRef;

public class ProfileFragment extends Fragment {

    private TextView name, email, intro;
    private ImageView NowImage;
    private Button logOut, edit;
    private DatabaseReference myRef;
    private FirebaseUser currentUser;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Switch profile_only_fri;

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


        logOut = view.findViewById(R.id.LogOut);
        edit = view.findViewById(R.id.edit);
        profile_only_fri = view.findViewById(R.id.profile_only_fri);

        NowImage = view.findViewById(R.id.profile_image_now);


        // get the Firebase  storage reference
        FirebaseRef fb = FirebaseRef.getInstance();
        storage = fb.getStorage();
        storageReference = fb.getStorageReference();

        UserProfileDAO userProfileDao = UserProfileDAO.getInstance();
        userProfileDao.updateViews(name, email, intro, profile_only_fri);
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
        return view;
    }
}
