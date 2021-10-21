package au.edu.anu.cecs.COMP6442GroupAssignment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Random;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.MessageDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserPostDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.FirebaseRef;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.HeatSpeechParser.Parser;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.HeatSpeechParser.Tokenizer;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Post;

public class DetailedPostActivity extends AppCompatActivity {

    private ImageView image, portrait;
    private TextView author, title, body, textView4, tags, loc;
    private String uid;
    private Post p;
    private UserPostDAO instance;
    private Button message;
    private MessageDAO messageDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_post);

        author = findViewById(R.id.post_page_author);
        textView4 = findViewById(R.id.TakeStars);
        title = findViewById(R.id.post_page_title);
        body = findViewById(R.id.post_page_body);
        tags = findViewById(R.id.post_page_tags);
        image = findViewById(R.id.post_page_image);
        portrait = findViewById(R.id.post_page_portrait);
        loc = findViewById(R.id.post_page_loc);
        message = findViewById(R.id.post_page_mess);
        instance = UserPostDAO.getInstance(this);
        Intent from_intent = getIntent();
        String pid = from_intent.getStringExtra("pid");
        messageDAO = MessageDAO.getInstance();
        FirebaseRef fb = FirebaseRef.getInstance();
        DocumentReference myRef = fb.getFirestore().collection("user-posts").document(pid);
        uid = FirebaseAuth.getInstance().getUid();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference sto_ref = storage.getReference();

        // Add the Detailed Post Data to the Page
        myRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        p = new Post(document.getData());
                        author.setText(p.getAuthor());
                        Parser parser1 = new Parser(new Tokenizer(p.getTitle()));
                        title.setText(parser1.parse());

                        Parser parser2 = new Parser(new Tokenizer(p.getBody()));
                        body.setText(parser2.parse());


                        // Show Tags (if any)
                        if (p.getTags().size() == 0) {
                            tags.setText("No Tags");
                        } else {
                            String tagString = p.getTags().toString().replace("[", "").replace("]", "");
                            Parser parser3 = new Parser(new Tokenizer(tagString));
                            tags.setText(parser3.parse());
                        }

                        //Display Portrait of the Author
                        RequestOptions optionsPortrait = new RequestOptions()
                                .override(800, 600)
                                .centerCrop()
                                .placeholder(R.drawable.face_id_1)
                                .error(R.drawable.face_id_1)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(false);
                        sto_ref.child("portrait/" + p.getAuthorID()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Glide.with(getApplicationContext())
                                        .load(uri.toString())
                                        .apply(optionsPortrait)
                                        .into(portrait);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });

                        //Display Image of the Post (if Any)
                        Random random = new Random();
                        String image_url;
                        if (p.getImageAddress().length() == 0) {
                            image_url = "images/default" + (random.nextInt(8) + 1) + ".jpg";
                        } else {
                            image_url = "images/" + p.getPid();
                        }

                        RequestOptions optionsPost = new RequestOptions()
                                .override(800, 600)
                                .centerCrop()
                                .placeholder(R.mipmap.ic_launcher_round)
                                .error(R.mipmap.ic_launcher_round)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(false);
                        sto_ref.child(image_url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(getApplicationContext())
                                        .load(uri.toString())
                                        .apply(optionsPost)
                                        .into(image);
                                image.setVisibility(View.VISIBLE);
                            }
                        });


                        // Star of the Post
                        textView4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (p.getUsersWhoLike().contains(uid)) {
                                    ArrayList<String> usersWhoLike = p.getUsersWhoLike();
                                    usersWhoLike.remove(uid);
                                    p.setUsersWhoLike(usersWhoLike);
                                    instance.update(p.getPid(), p.toMap());
                                } else {
                                    ArrayList<String> usersWhoLike = p.getUsersWhoLike();
                                    usersWhoLike.add(uid);
                                    p.setUsersWhoLike(usersWhoLike);
                                    instance.update(p.getPid(), p.toMap());

                                    messageDAO.sendAdminMessage(p.getAuthorID(),
                                            "by0wvrHLp8gNlD103LAM6Il2xzX2", "{" + p.getTitle() + "} Get a like！"
                                            , "by0wvrHLp8gNlD103LAM6Il2xzX2", pid);
                                    messageDAO.sendAdminMessage("by0wvrHLp8gNlD103LAM6Il2xzX2",
                                            p.getAuthorID(), "{" + p.getTitle() + "} Get a like！"
                                            , "by0wvrHLp8gNlD103LAM6Il2xzX2", pid);

                                }
                                textView4.setTextColor(p.getUsersWhoLike().contains(uid) ? getApplicationContext().getResources().getColor(R.color.red) :
                                        getApplicationContext().getResources().getColor(R.color.gray));
                                textView4.setText(p.getUsersWhoLike().contains(uid) ? ("Take Back" + " (" + p.getUsersWhoLike().size() + "stars)")
                                        : ("Give Star!" + " (" + p.getUsersWhoLike().size() + "stars)"));
                            }
                        });

                        textView4.setTextColor(p.getUsersWhoLike().contains(uid) ? getApplicationContext().getResources().getColor(R.color.red) :
                                getApplicationContext().getResources().getColor(R.color.gray));
                        textView4.setText(p.getUsersWhoLike().contains(uid) ? ("Take Back" + " (" + p.getUsersWhoLike().size() + "stars)")
                                : ("Give Star!" + " (" + p.getUsersWhoLike().size() + "stars)"));

                        loc.setText("GPS information - \nLongitude: " + p.getLongitude() +
                                "; Latitude: " + p.getLatitude() +
                                ";\nCity: " + p.getAddress());

                    } else {
                        System.out.println("No such document!");
                    }
                } else {
                    System.out.println("No such document!");
                }
            }
        });

        // Message the Author of the Post
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (p.getAuthorID().equals(currentUser.getUid())) {
                    Toast.makeText(getApplicationContext(),
                            "Don't send a message to yourself!",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                intent.putExtra("userid", p.getAuthorID());
                startActivity(intent);
            }
        });
    }
}
