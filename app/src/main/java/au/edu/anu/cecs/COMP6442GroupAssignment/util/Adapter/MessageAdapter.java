package au.edu.anu.cecs.COMP6442GroupAssignment.util.Adapter;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

import au.edu.anu.cecs.COMP6442GroupAssignment.DetailedPostActivity;
import au.edu.anu.cecs.COMP6442GroupAssignment.R;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Message;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    /**
     * An adapter to show messages with one friend on RecyclerView
     * A message item contains:
     * (1) friend name
     * (2) friend portrait
     * (3) my portrait
     * (4) messages
     */

    private final Context context;
    private final ArrayList<Message> messages;
    private final String userID;
    private final StorageReference reference;

    public MessageAdapter(Context context,
                          ArrayList<Message> messages,
                          String userID) {
        this.context = context;
        this.messages = messages;
        this.userID = userID;
        FirebaseStorage instance = FirebaseStorage.getInstance("gs://comp6442groupassignment.appspot.com");
        reference = instance.getReference();
    }

    /**
     * Messages have two kinds - the messages I sent, and the messages I received.
     * So we added an viewType to indicate that
     * @return
     */
    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1)
            // This is a message that I sent
            view = LayoutInflater.from(context).inflate(R.layout.message_sent,
                    parent,
                    false);
        else
            // This is a message that I received
            view = LayoutInflater.from(context).inflate(R.layout.message_received,
                    parent,
                    false);

        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.text.setText(message.getText());

        //Display Portrait Image When Messaging
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.face_id_1)
                .error(R.drawable.face_id_1)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false);
        reference.child("portrait/"+ message.isWhoSent()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(context)
                        .load(uri.toString())
                        .apply(options)
                        .into(holder.userImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Load Local Portrait if there are no Portrait in the Database
                holder.userImage.setImageResource(R.drawable.face_id_1);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (message.isWhoSent().equals("by0wvrHLp8gNlD103LAM6Il2xzX2")){
                    Intent intent = new Intent(context, DetailedPostActivity.class);
                    intent.putExtra("pid",message.getPid());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message m = messages.get(position);
        if (m.isWhoSent().equals(userID)) return 1;
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text; // Friend name
        public ImageView userImage; // Friend portrait

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.messRec);
            userImage = itemView.findViewById(R.id.user_image_mess);
        }
    }
}
