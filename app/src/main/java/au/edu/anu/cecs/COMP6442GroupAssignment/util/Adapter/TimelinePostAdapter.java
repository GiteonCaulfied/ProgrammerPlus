package au.edu.anu.cecs.COMP6442GroupAssignment.util.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import au.edu.anu.cecs.COMP6442GroupAssignment.R;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.MessageDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserPostDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Post;


public class TimelinePostAdapter extends RecyclerView.Adapter<TimelinePostAdapter.TimelinePostViewHolder>{

    private final StorageReference reference;
    private final String uid;
    private final UserPostDAO instance1;
    private final MessageDAO messageDAO;

    public interface OnItemClickListener {
        void onItemClick(Post item);
    }

    private final Context context;
    private final List<Post> posts;
    private final OnItemClickListener listener;
    AppCompatActivity appCompatActivity;

    public TimelinePostAdapter(Context context, List<Post> posts, OnItemClickListener listener, UserPostDAO instance1) {
        this.context = context;
        this.posts = posts;
        this.listener = listener;
        this.appCompatActivity = appCompatActivity;
        FirebaseStorage instance = FirebaseStorage.getInstance("gs://comp6442groupassignment.appspot.com");
        reference = instance.getReference();
        uid = FirebaseAuth.getInstance().getUid();
        this.instance1 = instance1;
        messageDAO = MessageDAO.getInstance();
    }

    @NonNull
    @Override
    public TimelinePostAdapter.TimelinePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_timeline_post,parent,false);

        return new TimelinePostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelinePostAdapter.TimelinePostViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int max = 100;
        int min = 0;
        int id = (int) (Math.random()*(max-min+1));
        holder.getText_username().setText(posts.get(position).getAuthor());
        holder.getText_title().setText(posts.get(position).getTitle());

        //Display the Tags (if Any)
        if (posts.get(position).getTags().size() == 0){
            holder.getText_tags().setText("No Tags");
        } else {
            String tagString = posts.get(position).getTags().toString().replace("[", "").replace("]", "");
            holder.getText_tags().setText(tagString);
        }

        if (posts.get(position).getImageAddress().length() != 0){
            //Display Image of the Post (If any)
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
                    holder.getImage().setVisibility(View.GONE);
                }
            });
        } else {
            holder.getImage().setVisibility(View.GONE);
        }


        holder.getText_star().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (posts.get(position).getUsersWhoLike().contains(uid)){
                    ArrayList<String> usersWhoLike = posts.get(position).getUsersWhoLike();
                    usersWhoLike.remove(uid);
                    posts.get(position).setUsersWhoLike(usersWhoLike);
                    instance1.update(posts.get(position).getPid(),posts.get(position).toMap());
                }else {
                    ArrayList<String> usersWhoLike = posts.get(position).getUsersWhoLike();
                    usersWhoLike.add(uid);
                    posts.get(position).setUsersWhoLike(usersWhoLike);
                    instance1.update(posts.get(position).getPid(),posts.get(position).toMap());
                    messageDAO.sendAdminMessage(posts.get(position).getAuthorID(),
                            "Upc8rDC8f0NlePlQCW2D2m7Bqin2", "{"+posts.get(position).getTitle()+"} Get a like！"
                            , "Upc8rDC8f0NlePlQCW2D2m7Bqin2",posts.get(position).getPid());
                    messageDAO.sendAdminMessage("Upc8rDC8f0NlePlQCW2D2m7Bqin2",
                            posts.get(position).getAuthorID(), "{"+posts.get(position).getTitle()+"} Get a like！"
                            , "Upc8rDC8f0NlePlQCW2D2m7Bqin2",posts.get(position).getPid());

                }
            }
        });

        holder.getText_star().setTextColor(posts.get(position).getUsersWhoLike().contains(uid)?context.getResources().getColor(R.color.red):
                context.getResources().getColor(R.color.gray));
        holder.getText_star().setText(posts.get(position).getUsersWhoLike().contains(uid)?("Take Back"+" ("+posts.get(position).getUsersWhoLike().size()+"stars)")
                :("Give Star!"+" ("+posts.get(position).getUsersWhoLike().size()+"stars)"));
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
        private final TextView text_tags;
        private final TextView star;

        public TimelinePostViewHolder(@NonNull View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.tv_post_image);
            text_username = (TextView) itemView.findViewById(R.id.tv_post_username);
            text_title = (TextView) itemView.findViewById(R.id.tv_post_title);
            text_tags = (TextView) itemView.findViewById(R.id.tv_post_tags);
            star = (TextView) itemView.findViewById(R.id.textView3);
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

        public TextView getText_tags(){
            return text_tags;
        }

        public TextView getText_star() {
            return star;
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
