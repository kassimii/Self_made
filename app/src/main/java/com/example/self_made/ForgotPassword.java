package com.example.self_made;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {

    private View view;
    private EditText emailId, password, confirfmPassword;
    private TextView submit, back;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword_layout);

        emailId = (EditText) view.findViewById(R.id.registered_emailid);
        password = (EditText) view.findViewById(R.id.password);
        confirfmPassword = (EditText) view.findViewById(R.id.confirmPassword);
        submit = (TextView) view.findViewById(R.id.forgot_button);
        back = (TextView) view.findViewById(R.id.backToLoginBtn);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailId.getText().toString().trim();
                String userPassword = password.getText().toString();
                String confPassword = confirfmPassword.getText().toString();

                if (email.equals("")) {
                    Toast.makeText(ForgotPassword.this, "Please enter your registered email ID", Toast.LENGTH_SHORT).show();
                } else {
                    if (!userPassword.equals(confPassword))
                        Toast.makeText(ForgotPassword.this, "Please make sure that the two passwords are the same", Toast.LENGTH_SHORT).show();
                    else {
                        submitButtonTask();
                        firebaseUser.updatePassword(userPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPassword.this, "Password Changed", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(ForgotPassword.this, "Password Update Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
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