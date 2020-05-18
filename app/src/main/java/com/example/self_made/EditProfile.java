package com.example.self_made;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfile extends AppCompatActivity {

    private Button setMealsButton, caloriesCounterButton, saveProfileButton;

    private EditText ageInput, heightInput, weightInput;
    private ImageButton femaleButton, maleButton, eggsButton,diaryButton, fishButton, glutenButton, peanutButton;

    private String age, height, weight;
    private boolean isFemale, isMale, isAllergicToEggs=false, isAllergicToDiary=false, isAllergicToFish=false, isAllergicToGluten=false, isAllergicToPeanut=false;
    private String allergies="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_edit_profile);
        onMealButtonClick();
        onCaloriesButtonCLick();
        onSaveProfileButtonClick();
    }

    public void onMealButtonClick()
    {
        setMealsButton = (Button) findViewById(R.id.meals_button);
        setMealsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
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
                        finish();
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

    public void onSaveProfileButtonClick(){
        ageInput = (EditText)findViewById(R.id.age_input);
        heightInput = (EditText)findViewById(R.id.height_input);
        weightInput = (EditText)findViewById(R.id.weight_input);

        femaleButton = (ImageButton)findViewById(R.id.female_button);
        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFemale=true;
                isMale=false;
            }
        });

        maleButton = (ImageButton)findViewById(R.id.male_button);
        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFemale=false;
                isMale=true;
            }
        });

        eggsButton = (ImageButton)findViewById(R.id.eggs_button);
        eggsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllergicToEggs=true;
            }
        });

        diaryButton = (ImageButton)findViewById(R.id.dairy_button);
        diaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllergicToDiary=true;
            }
        });

        fishButton = (ImageButton)findViewById(R.id.fish_button);
        fishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllergicToFish=true;
            }
        });

        glutenButton = (ImageButton)findViewById(R.id.gluten_button);
        glutenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllergicToGluten=true;
            }
        });

        peanutButton = (ImageButton)findViewById(R.id.peanut_button);
        peanutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllergicToPeanut=true;
            }
        });



        saveProfileButton = (Button)findViewById(R.id.save_profile_button);
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                age = ageInput.getText().toString();
                height = heightInput.getText().toString();
                weight = weightInput.getText().toString();

                if(age.length()!=0)
                    {FirebaseDatabase.getInstance().getReference().child("Profile").child("Age").setValue(age);}
                if(height.length()!=0)
                   { FirebaseDatabase.getInstance().getReference().child("Profile").child("Height").setValue(height);}
                if(weight.length()!=0)
                    {FirebaseDatabase.getInstance().getReference().child("Profile").child("Weight").setValue(weight);}

                if(isMale==true){
                    FirebaseDatabase.getInstance().getReference().child("Profile").child("Gender").setValue("male");
                }

                if(isFemale==true){
                    FirebaseDatabase.getInstance().getReference().child("Profile").child("Gender").setValue("female");
                }

                if(isAllergicToEggs==true){
                    allergies+="eggs ";
                }

                if(isAllergicToDiary==true){
                    allergies+="diary ";
                }

                if(isAllergicToFish==true){
                    allergies+="fish ";
                }

                if(isAllergicToGluten==true){
                    allergies+="gluten ";
                }

                if(isAllergicToPeanut==true){
                    allergies+="peanut ";
                }

                if(allergies.length()!=0){
                    FirebaseDatabase.getInstance().getReference().child("Profile").child("Allergies").setValue(allergies);
                    allergies="";
                }

                Toast.makeText(EditProfile.this, "Profile saved!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
