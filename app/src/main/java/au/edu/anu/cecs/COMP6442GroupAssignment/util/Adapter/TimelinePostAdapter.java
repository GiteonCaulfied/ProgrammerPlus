package au.edu.anu.cecs.COMP6442GroupAssignment.util.Adapter;

import android.content.Context;
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

import java.util.List;

import au.edu.anu.cecs.COMP6442GroupAssignment.R;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Post;


public class TimelinePostAdapter extends RecyclerView.Adapter<TimelinePostAdapter.TimelinePostViewHolder>{

    private final StorageReference reference;

    public interface OnItemClickListener {
        void onItemClick(Post item);
    }

    private final Context context;
    private final List<Post> posts;
    private final OnItemClickListener listener;


    public TimelinePostAdapter(Context context, List<Post> posts, OnItemClickListener listener) {
        this.context = context;
        this.posts = posts;
        this.listener = listener;
        FirebaseStorage instance = FirebaseStorage.getInstance("gs://comp6442groupassignment.appspot.com");
        reference = instance.getReference();
    }

    @NonNull
    @Override
    public TimelinePostAdapter.TimelinePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_timeline_post,parent,false);

        return new TimelinePostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelinePostAdapter.TimelinePostViewHolder holder, int position) {
        int max = 100;
        int min = 0;
        int id = (int) (Math.random()*(max-min+1));
        holder.getText_username().setText(posts.get(position).getAuthor());
        holder.getText_title().setText(posts.get(position).getTitle());


        //Display Image of the Post
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false);
        reference.child("images/"+posts.get(position).getPid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(context)
                        .load(uri.toString())
                        .apply(options)
                        .into(holder.getImage());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


        holder.bind(posts.get(position), listener);
    }



    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class TimelinePostViewHolder extends RecyclerView.ViewHolder{

        private final ImageView image;
        private final TextView text_username;
        private final TextView text_title;

        public TimelinePostViewHolder(@NonNull View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.tv_post_image);
            text_username = (TextView) itemView.findViewById(R.id.tv_post_username);
            text_title = (TextView) itemView.findViewById(R.id.tv_post_title);
        }

        public ImageView getImage() {
            return image;
        }

        public TextView getText_username() {
            return text_username;
        }

        public TextView getText_title() {
            return text_title;
        }

        public void bind(final Post item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
