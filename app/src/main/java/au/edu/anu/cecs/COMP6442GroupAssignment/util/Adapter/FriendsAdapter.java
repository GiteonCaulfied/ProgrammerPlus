package au.edu.anu.cecs.COMP6442GroupAssignment.util.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;

import au.edu.anu.cecs.COMP6442GroupAssignment.MessageActivity;
import au.edu.anu.cecs.COMP6442GroupAssignment.R;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Profile;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {
    /**
     * An adapter to show friend list on RecyclerView
     * A friend item contains:
     * (1) friend name
     * (2) friend id
     * (3) friend portrait
     */

    private final StorageReference reference;

    private Context context;
    private ArrayList<Profile> friends;

    public FriendsAdapter(Context context, ArrayList<Profile> friends) {
        this.context = context;
        this.friends = friends;
        FirebaseStorage instance = FirebaseStorage.getInstance("gs://comp6442groupassignment.appspot.com");
        reference = instance.getReference();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_item,
                parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Profile friend = friends.get(position);
        holder.name.setText(friend.getName());
        //TODO ImageView

        if (friend.isPortraitUploaded()){
            //Display Portrait Image
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.face_id_1)
                    .error(R.drawable.face_id_1)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(false);
            reference.child("portrait/"+friend.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Glide.with(context)
                            .load(uri.toString())
                            .apply(options)
                            .into(holder.imageView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        } else {
            holder.imageView.setImageResource(R.drawable.face_id_1);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", friend.getUid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name; // Friend name
        public ImageView imageView; // Friend portrait

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.username);
            imageView = itemView.findViewById(R.id.portrait);
        }
    }
}
