package au.edu.anu.cecs.COMP6442GroupAssignment.util.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import au.edu.anu.cecs.COMP6442GroupAssignment.R;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Profile;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Profile> friends;

    public FriendsAdapter(Context context, ArrayList<Profile> friends) {
        this.context = context;
        this.friends = friends;
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
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.username);
            imageView = itemView.findViewById(R.id.portrait);
        }
    }
}
