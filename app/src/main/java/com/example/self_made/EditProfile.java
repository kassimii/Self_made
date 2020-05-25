package com.example.self_made;


import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditProfile extends AppCompatActivity {

    private Button setMealsButton, analysePhotoButton, saveButton;
    private ImageButton femaleButton, maleButton, eggsButton, dairyButton, fishButton, glutenButton, peanutButton;
    private EditText age, height, weight, idealWeight;
    private RadioGroup goalRadio;
    private Spinner spinner;
    private String spinnerRes;


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


        setMealsButton = (Button) findViewById(R.id.meals_button);
        analysePhotoButton = (Button) findViewById(R.id.photo_button);
        saveButton = (Button) findViewById(R.id.save_button);

        femaleButton = (ImageButton) findViewById(R.id.button_female);
        maleButton = (ImageButton) findViewById(R.id.button_male);
        age = (EditText) findViewById(R.id.age);
        height = (EditText) findViewById(R.id.height);
        weight = (EditText) findViewById(R.id.weight);
        idealWeight = (EditText) findViewById(R.id.ideal_weight);
        eggsButton = (ImageButton) findViewById(R.id.button_eggs);
        dairyButton = (ImageButton) findViewById(R.id.button_dairy);
        fishButton = (ImageButton) findViewById(R.id.button_fish);
        glutenButton = (ImageButton) findViewById(R.id.button_gluten);
        peanutButton = (ImageButton) findViewById(R.id.button_peanut);
        goalRadio = (RadioGroup) findViewById(R.id.radioGroup);
        spinner = (Spinner) findViewById(R.id.activity_spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerRes = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(EditProfile.this, "Please select an option!", Toast.LENGTH_SHORT).show();
            }
        });

        setMealsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(EditProfile.this, SetMealHours.class));
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

                });

                    }
                });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUserProfile();
            }
        });
    }


    public void openCaloriesCounterActivity() {
        Intent intent = new Intent(this, CaloriesCounter.class);
        startActivity(intent);

        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    }

    private void setUserProfile()
    {
        String txtAge = age.getText().toString().trim();
        String txtHeight = height.getText().toString().trim();
        String txtWeight = weight.getText().toString().trim();
        String txtIdealWeight = idealWeight.getText().toString().trim();
        int radioId = goalRadio.getCheckedRadioButtonId();

        final HashMap<String , Object> usersProfileMap = new HashMap<>();
        usersProfileMap.put("Age", txtAge);
        usersProfileMap.put("Height", txtHeight);
        usersProfileMap.put("Weight", txtWeight);
        usersProfileMap.put("Ideal weight", txtIdealWeight);
        if(radioId==1)
            usersProfileMap.put("Goal", "lose weight");
        if(radioId==2)
            usersProfileMap.put("Goal", "maintain weight");
        if(radioId==3)
            usersProfileMap.put("Goal", "gain weight");
        usersProfileMap.put("Activity level", spinnerRes);
        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {usersProfileMap.put("Gender", "female");}
        });
        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {usersProfileMap.put("Gender", "male");}
        });
        eggsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {usersProfileMap.put("Allergies", "eggs");}
        });
        dairyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {usersProfileMap.put("Allergies", "dairy");}
        });
        fishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {usersProfileMap.put("Allergies", "fish");}
        });
        glutenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {usersProfileMap.put("Allergies", "gluten");}
        });
        peanutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {usersProfileMap.put("Allergies", "peanut");}
        });

        FirebaseDatabase.getInstance().getReference().child("Profile").setValue(usersProfileMap);

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

