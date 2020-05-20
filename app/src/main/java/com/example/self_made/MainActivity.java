package com.example.self_made;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends Activity {

    private static FragmentManager fragmentManager;

    private Button setMealsButton;
    private Button caloriesCounterButton;
    private Button editProfileButton;
    private Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fragmentManager = getSupportFragmentManager();

//        // If savedinstnacestate is null then replace login fragment
//        if (savedInstanceState == null) {
//            fragmentManager
//                    .beginTransaction()
//                    .replace(R.id.frameContainer, new Login(),
//                            Utils.Login_Fragment).commit();
//        }

        onLoginButtonClick();
        onProfileButtonClick();
        onMealButtonClick();
        onCaloriesButtonCLick();
    }

    public boolean onLoginButtonClick(){
        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openLoginActivity();

                    }
                }
        );

        return true;
    }

    public void openLoginActivity(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);

        overridePendingTransition(0,0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    }

    public void onProfileButtonClick(){
        editProfileButton = (Button) findViewById(R.id.profile_button);
        editProfileButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openProfileEditingActivity();
                    }
                }
        );
    }

    public void openProfileEditingActivity()
    {
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);

        overridePendingTransition(0,0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); // no animations between activity change
    }

    public void onMealButtonClick()
    {
        setMealsButton = (Button) findViewById(R.id.meals_button);
        setMealsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openMealHoursSettingActiviy();
                    }
                }
        );
    }

    public void openMealHoursSettingActiviy(){
        Intent intent = new Intent(this, SetMealHours.class);
        startActivity(intent);

        overridePendingTransition(0,0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    }

    public void onCaloriesButtonCLick() {
        caloriesCounterButton = (Button) findViewById(R.id.calories_button);
        caloriesCounterButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openCaloriesCounterActivity();
                    }
                }
        );
    }

    public void openCaloriesCounterActivity() {
        Intent intent = new Intent(this, CaloriesCounter.class);
        startActivity(intent);

        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    }


}
