package com.example.self_made;


import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class EditProfile extends AppCompatActivity {

    private Button setMealsButton, caloriesCounterButton, saveProfileButton;
    private ImageButton femaleButton, maleButton, eggsButton, dairyButton, fishButton, glutenButton, peanutButton;

    private Spinner activitySpinner, goalSpinner;
    private ArrayAdapter<String> activityAdapter, goalAdapter;


    private EditText ageInput, heightInput, weightInput, idealWeightInput;

    private String age, height, weight, idealWeight;
    private boolean isFemale, isMale, isAllergicToEggs = false, isAllergicToDiary = false, isAllergicToFish = false, isAllergicToGluten = false, isAllergicToPeanut = false;
    private String allergies = "";

    private ArrayList<String> activityList, goalList;

    private int activityLevelSelected=-1;//variable for activity level to store to db
    private int goalSelected=-1; //variable for weight goal to store to db
    private DatabaseReference databaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_edit_profile);

        onMealButtonClick();
        onCaloriesButtonCLick();

        if(FirebaseAuth.getInstance().getCurrentUser()!= null)
        {
            String userUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
            databaseRef=FirebaseDatabase.getInstance().getReference("Users").child(userUid);

            setActivityList();
            setGoalList();
            onSaveProfileButtonClick();
        }
    }

    public void onMealButtonClick() {
        setMealsButton = (Button) findViewById(R.id.meals_button);
        setMealsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(EditProfile.this, SetMealHours.class));
                    }
                }
        );
    }

    public void onCaloriesButtonCLick() {
        caloriesCounterButton = (Button) findViewById(R.id.calories_button);
        caloriesCounterButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(EditProfile.this, CaloriesCounter.class);
                        startActivity(intent);
                    }
                });

    }



    private void setActivityList(){
        activityList = new ArrayList<>();
        activityList.add("--Please select activity level");
        activityList.add("Sedentary (little or no exercise)");
        activityList.add("Light active (exercise 1-3 days/week)");
        activityList.add("Moderate active (exercise 3-5 days/week)");
        activityList.add("Active (exercise 6-7 days/week)");
        activityList.add("Very active (hard exercise 6-7 days/week)");
    }

    private void setGoalList(){
        goalList = new ArrayList<>();
        goalList.add("--Please select weight goal");
        goalList.add("Lose weight");
        goalList.add("Maintain weight");
        goalList.add("Gain weight");
    }


    public void onSaveProfileButtonClick() {
        ageInput = (EditText) findViewById(R.id.age_input);
        heightInput = (EditText) findViewById(R.id.height_input);
        weightInput = (EditText) findViewById(R.id.weight_input);
        idealWeightInput = (EditText)findViewById(R.id.ideal_weight);

        femaleButton = (ImageButton) findViewById(R.id.female_button);
        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFemale = true;
                isMale = false;
            }
        });

        maleButton = (ImageButton) findViewById(R.id.male_button);
        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFemale = false;
                isMale = true;
            }
        });

        eggsButton = (ImageButton) findViewById(R.id.eggs_button);
        eggsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllergicToEggs = true;
            }
        });

        dairyButton = (ImageButton) findViewById(R.id.dairy_button);
        dairyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllergicToDiary = true;
            }
        });

        fishButton = (ImageButton) findViewById(R.id.fish_button);
        fishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllergicToFish = true;
            }
        });

        glutenButton = (ImageButton) findViewById(R.id.gluten_button);
        glutenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllergicToGluten = true;
            }
        });

        peanutButton = (ImageButton) findViewById(R.id.peanut_button);
        peanutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllergicToPeanut = true;
            }
        });



        activitySpinner = (Spinner)findViewById(R.id.activity_spinner);
        activityAdapter =  new ArrayAdapter<String>(this, R.layout.style_spinner,activityList);
        activitySpinner.setAdapter(activityAdapter);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==1){
                    activityLevelSelected=1;
                }

                if(position==2){
                    activityLevelSelected=2;
                }

                if(position==3){
                    activityLevelSelected=3;
                }

                if(position==4){
                    activityLevelSelected=4;
                }

                if(position==5){
                    activityLevelSelected=5;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        goalSpinner = (Spinner)findViewById(R.id.goal_spinner);
        goalAdapter =  new ArrayAdapter<String>(this, R.layout.style_spinner,goalList);
        goalSpinner.setAdapter(goalAdapter);
        goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        goalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==1){
                    goalSelected=1;
                }

                if(position==2){
                    goalSelected=2;
                }

                if(position==3){
                    goalSelected=3;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        saveProfileButton = (Button) findViewById(R.id.save_profile_button);
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                age = ageInput.getText().toString();
                height = heightInput.getText().toString();
                weight = weightInput.getText().toString();
                idealWeight = idealWeightInput.getText().toString();

                if (age.length() != 0) {
                    databaseRef.child("Profile").child("Age").setValue(age);
                }
                if (height.length() != 0) {
                    databaseRef.child("Profile").child("Height").setValue(height);
                }
                if (weight.length() != 0) {
                    databaseRef.child("Profile").child("Weight").setValue(weight);
                }

                if(idealWeight.length() !=0){
                    databaseRef.child("Profile").child("Ideal Weight").setValue(idealWeight);
                }

                if (isMale == true) {
                    databaseRef.child("Profile").child("Gender").setValue("male");
                }

                if (isFemale == true) {
                    databaseRef.child("Profile").child("Gender").setValue("female");
                }

                if (isAllergicToEggs == true) {
                    allergies += "eggs ";
                }

                if (isAllergicToDiary == true) {
                    allergies += "diary ";
                }

                if (isAllergicToFish == true) {
                    allergies += "fish ";
                }

                if (isAllergicToGluten == true) {
                    allergies += "gluten ";
                }

                if (isAllergicToPeanut == true) {
                    allergies += "peanut ";
                }

                if (allergies.length() != 0) {
                    databaseRef.child("Profile").child("Allergies").setValue(allergies);
                    allergies = "";
                }

                if(activityLevelSelected!=-1){
                    databaseRef.child("Profile").child("Activity Level").setValue(activityLevelSelected);
                }

                if(goalSelected!=-1){
                    databaseRef.child("Profile").child("Weight Goal").setValue(goalSelected);
                }

//                String zeroValue="1";
//                databaseRef.child("Profile").child("Calories Consumed").setValue(zeroValue);
//                databaseRef.child("Profile").child("Calories Needed").setValue(zeroValue);
//                databaseRef.child("Profile").child("Current Day").setValue(zeroValue);

                Toast.makeText(EditProfile.this, "Profile saved!", Toast.LENGTH_SHORT).show();
            }
        });

    }


}





