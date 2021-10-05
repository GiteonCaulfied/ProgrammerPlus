package au.edu.anu.cecs.COMP6442GroupAssignment.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

import au.edu.anu.cecs.COMP6442GroupAssignment.R;

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
        lp.width = 740;
        win.setAttributes(lp);

        view.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = findViewById(R.id.email_search_friend);

                FirebaseRef firebaseRef = FirebaseRef.getInstance();
                FirebaseUser currentUser = firebaseRef.getFirebaseAuth().getCurrentUser();
                FriendRequest friendRequest = new FriendRequest(currentUser.getUid(), currentUser.getDisplayName());
                FirebaseFirestore db = firebaseRef.getFirestore();
                db.collection("friend-request")
                        .document(email.getText().toString())
                        .set(friendRequest.toMap())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Friend request", "Sent new request!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Friend request", "Error: ", e);
                            }
                        });
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
}
