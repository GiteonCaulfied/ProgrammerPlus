package au.edu.anu.cecs.COMP6442GroupAssignment.util.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import au.edu.anu.cecs.COMP6442GroupAssignment.R;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Message;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Message> messages;
    private final String userID;

    public MessageAdapter(Context context,
                          ArrayList<Message> messages,
                          String userID) {
        this.context = context;
        this.messages = messages;
        this.userID = userID;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1)
            view = LayoutInflater.from(context).inflate(R.layout.message_sent,
                    parent,
                    false);
        else
            view = LayoutInflater.from(context).inflate(R.layout.message_received,
                    parent,
                    false);

        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.text.setText(message.getText());
        //TODO ImageView
        holder.userImage.setImageResource(R.drawable.face_id_1);
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
        public TextView text;
        public ImageView userImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.messRec);
            userImage = itemView.findViewById(R.id.user_image_mess);
        }
    }
}
