package au.edu.anu.cecs.COMP6442GroupAssignment.util.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import au.edu.anu.cecs.COMP6442GroupAssignment.MessageActivity;
import au.edu.anu.cecs.COMP6442GroupAssignment.R;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.DAO.UserProfileDAO;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.FirebaseRef;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.FriRecom;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.FriendRequest;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Profile;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.UserManager;

public class SearchFriendDialog extends Dialog {
    public SearchFriendDialog(@NonNull Context context) {
        super(context);
    }

    public SearchFriendDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SearchFriendDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this.getContext(), R.layout.dialog_friend, null);
        setContentView(view);

        setCanceledOnTouchOutside(false);

        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = 1000;
        win.setAttributes(lp);

        FriRecom friRecom = new FriRecom(getContext());
        EditText rec1 = findViewById(R.id.recommend_1);
        EditText rec2 = findViewById(R.id.recommend_2);
        EditText rec3 = findViewById(R.id.recommend_3);
        rec1.setEnabled(false);
        rec2.setEnabled(false);
        rec3.setEnabled(false);
        Button conf2 = findViewById(R.id.confirm2);
        Button conf3 = findViewById(R.id.confirm3);
        Button conf4 = findViewById(R.id.confirm4);
        friRecom.recommendation(rec1, rec2, rec3);
        conf2.setOnClickListener(view1 -> {
            sendFriReq(rec1.getText().toString());
            dismiss();
        });
        conf3.setOnClickListener(view1 -> {
            sendFriReq(rec2.getText().toString());
            dismiss();
        });
        conf4.setOnClickListener(view1 -> {
            sendFriReq(rec3.getText().toString());
            dismiss();
        });

        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = findViewById(R.id.email_search_friend);
                String email_str = email.getText().toString();
                UserManager userManager = UserManager.getInstance();
                if (!userManager.emailIsValid(email_str)) {
                    Toast.makeText(getContext(),
                            "Cannot find such a user! Check your email!", Toast.LENGTH_LONG).show();
                    return;
                }

                UserProfileDAO userProfileDAO = UserProfileDAO.getInstance();
                Profile me = userProfileDAO.getUserprofile();
                String friendID = userManager.getIDFromEmail(email_str);
                if (me.friendContain(friendID)) {
                    Toast.makeText(getContext(),
                            "You guys are already friends!", Toast.LENGTH_LONG).show();
                    dismiss();
                    Intent intent = new Intent(getContext(), MessageActivity.class);
                    intent.putExtra("userid", friendID);
                    getContext().startActivity(intent);
                    return;
                }

                sendFriReq(email_str);
                dismiss();
            }
        });

        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void sendFriReq(String email_str) {
        FirebaseRef firebaseRef = FirebaseRef.getInstance();
        FirebaseUser currentUser = firebaseRef.getFirebaseAuth().getCurrentUser();
        FriendRequest friendRequest = new FriendRequest(currentUser.getUid(), currentUser.getDisplayName());
        FirebaseFirestore db = firebaseRef.getFirestore();
        db.collection("friend-request")
                .document(email_str)
                .set(friendRequest.toMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Friend request", "Sent new request!");
                        Toast.makeText(getContext(),
                                "Sent new request!", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Friend request", "Error: ", e);
                    }
                });
    }
}
