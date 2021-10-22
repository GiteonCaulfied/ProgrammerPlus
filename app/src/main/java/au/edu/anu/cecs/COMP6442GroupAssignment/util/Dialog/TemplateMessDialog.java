package au.edu.anu.cecs.COMP6442GroupAssignment.util.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import au.edu.anu.cecs.COMP6442GroupAssignment.R;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.TempMess.Parser;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.TempMess.Tokenizer;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Profile;

public class TemplateMessDialog extends Dialog {
    /**
     * A dialog where the user can choose template messages
     */

    private String temp;
    private final Profile currentUser;
    private final Profile friend;
    private final long lastTime;
    private final AppCompatActivity messageAct;

    public TemplateMessDialog(@NonNull Context context, int themeResId, AppCompatActivity messageAct,
                              Profile currentUser, Profile friend, long lastTime) {
        super(context, themeResId);
        this.messageAct = messageAct;
        this.currentUser = currentUser;
        this.friend = friend;
        this.lastTime = lastTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this.getContext(), R.layout.dialog_temp_mess, null);
        setContentView(view);

        setCanceledOnTouchOutside(false);

        // Create the diaglog
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = 920;
        win.setAttributes(lp);

        RadioGroup radio_temp_mess = view.findViewById(R.id.radio_temp_mess);

        // A user can choose a template from provided ones
        radio_temp_mess.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(i);
                temp = rb.getText().toString();
            }
        });

        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use tokenizer and parser to modify the template
                EditText text = messageAct.findViewById(R.id.messageText);
                Tokenizer tokenizer = new Tokenizer(temp);
                Parser parser = new Parser(tokenizer, currentUser, friend, lastTime);
                text.setText(parser.parse());
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
