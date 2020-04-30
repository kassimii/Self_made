package com.example.self_made;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class Login extends AppCompatActivity{
    private View view;

    private EditText emailid, password;
    private Button loginButton;
    private TextView forgotPassword, signUp;
    private Animation shakeAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        emailid = (EditText) view.findViewById(R.id.login_emailid);
        password = (EditText) view.findViewById(R.id.login_password);
        loginButton = (Button) view.findViewById(R.id.loginBtn);
        forgotPassword = (TextView) view.findViewById(R.id.forgot_password);
        signUp = (TextView) view.findViewById(R.id.createAccount);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                validate(emailid.getText().toString(), password.getText().toString());
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

    // Check Validation before login
    private void validate(String userName, String userPassword) {
        // Get email id and password
        String getEmailId = emailid.getText().toString();
        String getPassword = password.getText().toString();

        // Check patter for email id
        Pattern p = Pattern.compile(Utils.regEx);

        Matcher m = p.matcher(getEmailId);

        // Check for both field is empty or not
        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0) {
            new ErrorCustomToast().Show_Toast(this, view,
                    "Enter both credentials.");

        }
        // Check if email id is valid or not
        else if (!m.find())
            new ErrorCustomToast().Show_Toast(this, view,
                    "Your Email Id is Invalid.");
            // Else do login and do your stuff
        else
            Toast.makeText(this, "Do Login.", Toast.LENGTH_SHORT)
                    .show();

    }
}