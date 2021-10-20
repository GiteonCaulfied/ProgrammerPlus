package au.edu.anu.cecs.COMP6442GroupAssignment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.Adapter.MessageAdapter;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.MessageDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserProfileDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Dialog.TemplateMessDialog;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.FirebaseRef;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Message;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Profile;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Dialog.SearchFriendDialog;

public class MessageActivity extends AppCompatActivity {
    private TextView username;
    private ImageView imageView;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private DatabaseReference myRef;
    private String whoSent;
    private MessageDAO messageDAO;
    private String userId;
    private Profile me;
    private UserProfileDAO userProfileDAO;
    private MenuItem item;

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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.inflateMenu(R.menu.menu_chat);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userid");
        whoSent = currentUser.getUid();

        db = firebaseRef.getFirestore();
        db.collection("user-profiles")
                .document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                user = new Profile(value.getData());
                username.setText(user.getName());

                if (user.isPortraitUploaded()){
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
                } else {
                    imageView.setImageResource(R.drawable.face_id_1);
                }
            }
        });

        // Messages
        message = findViewById(R.id.messageText);
        sendBut = findViewById(R.id.sendMess);
        messageDAO = MessageDAO.getInstance();

        // Send the Message If possible
        sendBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = message.getText().toString();
                if (!text.equals("")) {
                    if (user.blockContain(currentUser.getUid())) {
                        Toast.makeText(MessageActivity.this,
                                "Cannot send a message! You have been blocked!",
                                Toast.LENGTH_LONG).show();
                        message.setText("");
                        return;
                    }
                    if (user.isOnlyFriMess() && !user.friendContain(currentUser.getUid())) {
                        Toast.makeText(MessageActivity.this,
                                "Only friends can send a message to this user!",
                                Toast.LENGTH_LONG).show();
                        message.setText("");
                        return;
                    }
                    messageDAO.sendMessage(currentUser.getUid(),
                            userId, text, whoSent);
                    messageDAO.sendMessage(userId,
                            currentUser.getUid(), text, whoSent);
                } else {
                    Toast.makeText(MessageActivity.this,
                            "Cannot send empty message!", Toast.LENGTH_LONG).show();
                }
                message.setText("");
            }
        });

        // Block or not?
        userProfileDAO = UserProfileDAO.getInstance();
        me = userProfileDAO.getUserprofile();
        if (me.blockContain(userId)) {
            item = toolbar.getMenu().findItem(R.id.block_him);
            item.setTitle("Unblocked");
        }

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
                .child(currentUser.getUid()).child(userId)
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_chat, menu);
    }

    /**
     * Block or Unblock a user.
     *
     * @param item MenuItem
     */
    public void block(MenuItem item) {
        if (me.blockContain(userId)) {
            userProfileDAO.cancelBlocked(userId);
            Toast.makeText(getApplicationContext(), "Unblocked!",
                    Toast.LENGTH_LONG).show();
            item.setTitle("Block");
        }
        else {
            userProfileDAO.addNewBlocked(userId);
            Toast.makeText(getApplicationContext(), "Blocked!",
                    Toast.LENGTH_LONG).show();
            item.setTitle("Unblock");
        }
    }

    /**
     * Delete the chat with a user.
     *
     * @param item MenuItem
     */
    public void deleteChat(MenuItem item) {
        messageDAO.deleteChat(currentUser.getUid(), userId);
        finish();
    }

    /**
     * Show the template message dialog.
     *
     * @param item MenuItem
     */
    public void templateMess(MenuItem item) {
        long lastTime = messages.isEmpty() ? System.currentTimeMillis() : messages.get(messages.size() - 1).getTime();
        TemplateMessDialog dialog = new TemplateMessDialog(
                MessageActivity.this,
                R.style.Dialog, this,
                me, user, lastTime);
        dialog.show();
    }
}