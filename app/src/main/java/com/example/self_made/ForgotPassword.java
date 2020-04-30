package com.example.self_made;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {

    private View view;
    private EditText emailId;
    private TextView submit, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword_layout);

        emailId = (EditText) view.findViewById(R.id.registered_emailid);
        submit = (TextView) view.findViewById(R.id.forgot_button);
        back = (TextView) view.findViewById(R.id.backToLoginBtn);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailId.getText().toString().trim();

                if(email.equals("")){
                    Toast.makeText(ForgotPassword.this, "Please enter your registered email ID", Toast.LENGTH_SHORT).show();
                }
                else{
                    submitButtonTask();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPassword.this, Login.class));
            }
        });

    }

    private void submitButtonTask() {
        String getEmailId = emailId.getText().toString();

        // Pattern for email id validation
        Pattern p = Pattern.compile(Utils.regEx);

        // Match the pattern
        Matcher m = p.matcher(getEmailId);

        // First check if email id is not null else show error toast
        if (getEmailId.equals("") || getEmailId.length() == 0)

            new ErrorCustomToast().Show_Toast(ForgotPassword.this, view,
                    "Please enter your Email Id.");

            // Check if email id is valid or not
        else if (!m.find())
            new ErrorCustomToast().Show_Toast(ForgotPassword.this, view,
                    "Your Email Id is Invalid.");

            // Else submit email id and fetch passwod or do your stuff
        else
            Toast.makeText(ForgotPassword.this, "Get Forgot Password.",
                    Toast.LENGTH_SHORT).show();
    }
}