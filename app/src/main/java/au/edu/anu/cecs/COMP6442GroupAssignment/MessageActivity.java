package au.edu.anu.cecs.COMP6442GroupAssignment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.Adapter.MessageAdapter;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.MessageDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.FirebaseRef;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Message;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Profile;

public class MessageActivity extends AppCompatActivity {
    private TextView username;
    private ImageView imageView;
    private FirebaseUser currentUser;
    private DatabaseReference myRef;
    private String user1;
    private String user2;
    private String whoSent;

    private RecyclerView histMessages;
    private EditText message;
    private ImageView sendBut;
    private Profile user;
    private MessageAdapter messageAdapter;
    private ArrayList<Message> messages;
    private StorageReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        FirebaseRef firebaseRef = FirebaseRef.getInstance();
        myRef = firebaseRef.getDatabaseRef();
        currentUser = firebaseRef.getFirebaseAuth().getCurrentUser();

        imageView = findViewById(R.id.fri_image);
        username = findViewById(R.id.fri_name);

        FirebaseStorage instance = FirebaseStorage.getInstance("gs://comp6442groupassignment.appspot.com");
        reference = instance.getReference();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userid");
        whoSent = currentUser.getUid();
        if (currentUser.getUid().compareTo(userId) < 0) {
            user1 = currentUser.getUid();
            user2 = userId;
        } else {
            user1 = userId;
            user2 = currentUser.getUid();
        }

        myRef.child("user-profile").child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(Profile.class);
                        username.setText(user.getName());

                        //Display Portrait Image When Messaging
                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.face_id_1)
                                .error(R.drawable.face_id_1)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(false);
                        reference.child("portrait/"+ user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Glide.with(getApplicationContext())
                                        .load(uri.toString())
                                        .apply(options)
                                        .into(imageView);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // Messages
        message = findViewById(R.id.messageText);
        sendBut = findViewById(R.id.sendMess);

        sendBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = message.getText().toString();
                if (!text.equals("")) {
                    MessageDAO messageDAO = MessageDAO.getInstance();
                    messageDAO.sendMessage(user1,
                            user2, text, whoSent);
                } else {
                    Toast.makeText(MessageActivity.this,
                            "Cannot send empty message!", Toast.LENGTH_LONG);
                }
                message.setText("");
            }
        });

        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(getApplicationContext(), messages,
                currentUser.getUid());
        histMessages = findViewById(R.id.histMessages);
        histMessages.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        histMessages.setLayoutManager(linearLayoutManager);
        histMessages.setAdapter(messageAdapter);

        // What we said
        myRef.child("user-chat")
                .child(user1).child(user2)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot ss : snapshot.getChildren()) {
                            Message m = ss.getValue(Message.class);
                            messages.add(m);
                        }

                        Collections.sort(messages);
                        messageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void setSupportActionBar(Toolbar toolbar) {
    }
}