package com.example.self_made;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CaloriesCounter extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button editProfileButton,setMealsButton;
    private Spinner foodSpiner;
    private ProgressBar caloriesProgressBar;

    private int BMR,caloriesNeeded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_calories_counter);
        onProfileButtonClick();
        onMealButtonClick();
        calculateNeededCalories();
        chooseFood();
    }

    public void onProfileButtonClick(){
        editProfileButton = (Button) findViewById(R.id.profile_button);
        editProfileButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
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
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
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

    public void calculateNeededCalories(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Profile");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int age,height,weight;
                double heightInches, weightPounds;
                double BMRDouble;

                String gender = dataSnapshot.child("Gender").getValue().toString();
                String weightString = dataSnapshot.child("Weight").getValue().toString();
                String heightString = dataSnapshot.child("Height").getValue().toString();
                String ageString = dataSnapshot.child("Age").getValue().toString();


                age = Integer.parseInt(ageString);
                weight = Integer.parseInt(weightString);
                height = Integer.parseInt(heightString);

                weightPounds = weight*2.2;
                heightInches = height*0.39;

                if (gender.equals("male")) {
                        BMRDouble = 66 + (6.23*weightPounds) + (12.7*heightInches) - (6.8*age);
                        BMR = (int)BMRDouble;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void chooseFood(){
        foodSpiner = (Spinner)findViewById(R.id.food_spinner);
        foodSpiner.setOnItemSelectedListener(this);
        caloriesProgressBar=(ProgressBar)findViewById(R.id.calories_progress_bar);
        caloriesProgressBar.setProgress(50);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final String selectedItem = parent.getSelectedItem().toString();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("FoodCalories").child("BreadAndCereals");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String calories = dataSnapshot.child(selectedItem).child("Calories").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
