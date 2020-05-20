package com.example.self_made;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
        setContentView(R.layout.activity_edit_profile);

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
                });
        analysePhotoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(EditProfile.this, AnalyzePhoto.class));
                    }
                });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUserProfile();
            }
        });
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
}

