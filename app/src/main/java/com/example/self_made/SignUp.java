package com.example.self_made;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUp extends AppCompatActivity {

    private EditText fullName, emailId, password, confirmPassword;
    private TextView login;
    private Button signUpButton;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.signup_layout);

        fullName = (EditText) findViewById(R.id.fullName);
        emailId = (EditText) findViewById(R.id.userEmailId);
        password = (EditText) findViewById(R.id.password_signup);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword_signup);
        signUpButton = (Button) findViewById(R.id.signUpBtn);
        login = (TextView) findViewById(R.id.already_user);

        firebaseAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtName = fullName.getText().toString();
                String txtEmail = emailId.getText().toString().trim();
                String txtPassword = password.getText().toString().trim();
                String txtPassword2 = confirmPassword.getText().toString().trim();

                Pattern p = Pattern.compile(Utils.regEx);
                Matcher m = p.matcher(txtEmail);

                if (TextUtils.isEmpty(txtName) || TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)) {
                    Toast.makeText(SignUp.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
                } else if (txtPassword.length() < 6) {
                    Toast.makeText(SignUp.this, "Password too short!", Toast.LENGTH_SHORT).show();
                } else if (!txtPassword.equals(txtPassword2)) {
                    Toast.makeText(SignUp.this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
                } else if (!m.find()) {
                    new ErrorCustomToast().Show_Toast(SignUp.this, view, "Your Email Id is Invalid.");
                } else {
                    registerUser(txtName, txtEmail, txtPassword);
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, Login.class));
            }
        });
    }

    private void registerUser(final String name, final String email, final String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUp.this, "Sign up with success!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this, EditProfile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Name").setValue(name);
    }
}