package com.example.self_made;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {

    private EditText emailid, password;
    private Button loginButton;
    private TextView forgotPassword, signUp;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.login_layout);

        emailid = (EditText) findViewById(R.id.login_emailid);
        password = (EditText) findViewById(R.id.login_password);
        loginButton = (Button) findViewById(R.id.login_button);
        forgotPassword = (TextView) findViewById(R.id.forgot_password);
        signUp = (TextView) findViewById(R.id.createAccount);

        firebaseAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String txt_email = emailid.getText().toString();
                String txt_password = password.getText().toString();
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(Login.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                } else
                {
                    Pattern p = Pattern.compile(Utils.regEx);
                    Matcher m = p.matcher(txt_email);
                    if (!m.find())
                        new ErrorCustomToast().Show_Toast(Login.this, view, "Your Email Id is Invalid.");
                    else
                        loginUser(txt_email , txt_password);
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(Login.this, SignUp.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(Login.this, ForgotPassword.class));
            }
        });
    }

    private void loginUser(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Login.this, "Login with success!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this ,EditProfile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}