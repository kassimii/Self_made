package com.example.self_made;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Time;
import android.widget.Toast;
public class SetMealHours extends AppCompatActivity {

    //    private EditText breakfast_hour, snack1_hour, lunch_hour, snack2_hour, dinner_hour;
//    private Button save_plan_button;
    private Button analysePhotoButton;
    private Button editProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_meal_hours);
        onProfileButtonClick();
        onCameraButtonClick();
    }

    public void onProfileButtonClick() {
        editProfileButton = (Button) findViewById(R.id.profile_button);
        editProfileButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish(); //return to Main Activity before opening Profile Editing Activity
                        openProfileEditingActivity();
                    }
                }
        );
    }

    public void openProfileEditingActivity() {
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);

        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    }

    public void onCameraButtonClick() {
        analysePhotoButton = (Button) findViewById(R.id.photo_button);
        analysePhotoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        openPhotoAnalsisActivity();
                    }
                }
        );
    }

    public void openPhotoAnalsisActivity() {
        Intent intent = new Intent(this, AnalyzePhoto.class);
        startActivity(intent);

        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    }
}

