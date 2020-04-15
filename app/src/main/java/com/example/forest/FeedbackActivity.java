package com.example.forest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class FeedbackActivity extends AppCompatActivity {

    String mailTo = "support@vecmocon.com";
    String mailSub = "Feedback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        //set nav bar color
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        EditText editText = findViewById(R.id.txtTo);
        editText.setText(mailTo, TextView.BufferType.NORMAL);

        EditText editText2 = findViewById(R.id.txtSubject);
        editText2.setText(mailSub, TextView.BufferType.NORMAL);

        findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String to = mailTo;
                String sub = mailSub;
                String mess = ((EditText)findViewById(R.id.txtMessage)).getText().toString();
                Intent mail = new Intent(Intent.ACTION_SEND);
                mail.putExtra(Intent.EXTRA_EMAIL,new String[]{to});
                mail.putExtra(Intent.EXTRA_SUBJECT, sub);
                mail.putExtra(Intent.EXTRA_TEXT, mess);
                mail.setType("message/rfc822");
                startActivity(Intent.createChooser(mail, "Send email via:"));
            }
        });
    }
}
