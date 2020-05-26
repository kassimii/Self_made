package com.example.self_made;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPassword extends AppCompatActivity {
    private EditText emailId, password, confirfmPassword;
    private Button submit, back;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.forgotpassword_layout);

        emailId = (EditText) findViewById(R.id.registered_emailid);
        password = (EditText) findViewById(R.id.password_new);
        confirfmPassword = (EditText) findViewById(R.id.confirmPassword_new);
        submit = (Button) findViewById(R.id.forgot_sumit_button);
        back = (Button) findViewById(R.id.backToLoginBtn);

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
}