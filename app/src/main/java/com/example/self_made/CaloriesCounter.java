package com.example.self_made;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class CaloriesCounter extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button editProfileButton,setMealsButton;
    private Button saveMealButton, addNewMealButton, showMealLogButton;
    private Spinner foodCategorySpinner, foodTypeSpinner;
    private ProgressBar caloriesProgressBar;
    private TextView caloriesConsumedTextView, caloriesLeftTextView;

    private ArrayList<String> foodCategoryList, breadsCerealsList, meatFishList, fruitsVegetablesList, milkAndDairyList, fatsSugarsList, otherFoodTypeList;
    private ArrayAdapter<String> foodCategoryAdapter, foodTypeAdapter;

    private int BMR, caloriesNeeded;
    private int caloriesConsumed=-1; //flag for saving calories consumed to db
    public int caloriesNeededDb, caloriesConsumedDb;
    private int caloriesPerFoodType =-1; //variable for holding the calories each food type selected that have to be added to the daily calories consumed
    private int foodCategorySelected=-1; //variable to find which one of the food categories was selected used to extract calories from db

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_calories_counter);

        getCaloriesValuesFromDb();
        saveCaloriesOfMeal();
        showCaloriesConsumed();
        showCaloriesLeft();
        setFoodCategoryList();
        onProfileButtonClick();
        onMealButtonClick();
        calculateNeededCalories();
        getFoodTypesFromDatabase();
        chooseFoodCategory();
        addNewFoodType();
        //updateCaloriesOnProgressBar();
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


    public void getCaloriesValuesFromDb(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Profile");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String caloriesN = dataSnapshot.child("Calories Needed").getValue().toString();
                caloriesNeededDb = Integer.parseInt(caloriesN);

                String caloriesC = dataSnapshot.child("Calories Consumed").getValue().toString();
                caloriesConsumedDb = Integer.parseInt(caloriesC);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


//    public void setCaloriesConsumed(int CC) {
//        this.caloriesConsumedDb = CC;
//    }
//
//    public void setCaloriesNeeded(int CN){
//        this.caloriesNeededDb = CN;
//    }

    public void setFoodCategoryList(){
        foodCategoryList =  new ArrayList<String>();
        foodCategoryList.add("-Please select a food type-");
        foodCategoryList.add("Breads and Cereals");
        foodCategoryList.add("Meat and Fish");
        foodCategoryList.add("Fruits and Vegetables");
        foodCategoryList.add("Milk and Dairy");
        foodCategoryList.add("Fats and Sugars");
        foodCategoryList.add("Others");
    }

    public void calculateNeededCalories(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Profile");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int age,height,weight;
                double heightInches, weightPounds;
                double BMRDouble=0;

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
                }

                if (gender.equals("female")) {
                    BMRDouble = 655 + (4.35*weightPounds) + (4.7*heightInches) - (4.7*age);
                }

                BMR = (int)BMRDouble;
                caloriesNeeded = BMR;

                FirebaseDatabase.getInstance().getReference().child("Profile").child("Calories Needed").setValue(caloriesNeeded);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getFoodTypesFromDatabase(){

        breadsCerealsList = new ArrayList<String>();

        meatFishList = new ArrayList<String>();

        fruitsVegetablesList = new ArrayList<String>();

        milkAndDairyList = new ArrayList<String>();

        fatsSugarsList = new ArrayList<String>();

        otherFoodTypeList = new ArrayList<String>();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Calories");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                breadsCerealsList.clear();
                for(DataSnapshot snapshot: dataSnapshot.child("Breads and Cereals").getChildren()){
                    breadsCerealsList.add(snapshot.getKey());
                }


                meatFishList.clear();
                for(DataSnapshot snapshot: dataSnapshot.child("Meat and Fish").getChildren()){
                    meatFishList.add(snapshot.getKey());
                }

                fruitsVegetablesList.clear();
                for(DataSnapshot snapshot: dataSnapshot.child("Fruits and Vegetables").getChildren()){
                    fruitsVegetablesList.add(snapshot.getKey());
                }

                milkAndDairyList.clear();
                for(DataSnapshot snapshot: dataSnapshot.child("Milk and Dairy").getChildren()){
                    milkAndDairyList.add(snapshot.getKey());
                }

                fatsSugarsList.clear();
                for(DataSnapshot snapshot: dataSnapshot.child("Fats and Sugars").getChildren()){
                    fatsSugarsList.add(snapshot.getKey());
                }

                otherFoodTypeList.clear();
                for(DataSnapshot snapshot: dataSnapshot.child("Others").getChildren()){
                    otherFoodTypeList.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void chooseFoodCategory(){
        foodCategorySpinner = (Spinner)findViewById(R.id.food_catergory_spinner);
        foodTypeSpinner = (Spinner)findViewById(R.id.food_type_spinner);

        foodCategoryAdapter = new ArrayAdapter<String>(this, R.layout.style_spinner,foodCategoryList);
        foodCategorySpinner.setAdapter(foodCategoryAdapter);
        foodCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        foodCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    foodCategorySelected=-1;
                    foodTypeAdapter = new ArrayAdapter<String>(CaloriesCounter.this, R.layout.style_spinner, Collections.<String>emptyList());
                }

                if(position==1){
                    foodCategorySelected=1;
                    foodTypeAdapter = new ArrayAdapter<String>(CaloriesCounter.this,R.layout.style_spinner, breadsCerealsList);
                }

                if(position==2){
                    foodCategorySelected=2;
                    foodTypeAdapter = new ArrayAdapter<String>(CaloriesCounter.this,R.layout.style_spinner, meatFishList);
                }

                if(position==3){
                    foodCategorySelected=3;
                    foodTypeAdapter = new ArrayAdapter<String>(CaloriesCounter.this,R.layout.style_spinner, fruitsVegetablesList);
                }

                if(position==4){
                    foodCategorySelected=4;
                    foodTypeAdapter = new ArrayAdapter<String>(CaloriesCounter.this,R.layout.style_spinner, milkAndDairyList);
                }

                if(position==5){
                    foodCategorySelected=5;
                    foodTypeAdapter = new ArrayAdapter<String>(CaloriesCounter.this,R.layout.style_spinner, fatsSugarsList);
                }

                if(position==6){
                    foodCategorySelected=6;
                    foodTypeAdapter = new ArrayAdapter<String>(CaloriesCounter.this,R.layout.style_spinner, otherFoodTypeList);
                }

                foodTypeSpinner.setAdapter(foodTypeAdapter);
                foodTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        chooseFoodType();
    }

    public void chooseFoodType(){
        foodTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    final String foodTypeSelected = parent.getItemAtPosition(position).toString();
                    getCaloriesForFoodTypeFromDb(foodTypeSelected);}


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getCaloriesForFoodTypeFromDb(String foodTypeSelected){
        String foodCategory = foodCategoryList.get(foodCategorySelected);
        final String foodType = foodTypeSelected;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Calories").child(foodCategory);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String caloricValueString = dataSnapshot.child(foodType).getValue().toString();
                int caloricValue = Integer.parseInt(caloricValueString);
                caloriesPerFoodType = caloricValue;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void saveCaloriesOfMeal(){
        saveMealButton = (Button)findViewById(R.id.save_meal_button);
        saveMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(caloriesPerFoodType ==-1)
                {
                    Toast.makeText(CaloriesCounter.this, "Please select your meal!", Toast.LENGTH_SHORT).show();
                }
                else { 
                    caloriesConsumedDb+= caloriesPerFoodType;
                    caloriesConsumed=1;
                   if(caloriesConsumed!=-1){
                        FirebaseDatabase.getInstance().getReference().child("Profile").child("Calories Consumed").setValue(caloriesConsumedDb);
                        caloriesConsumed=-1;
                   }
                    showCaloriesConsumed();
                    showCaloriesLeft();
                    updateCaloriesOnProgressBar();
               }
            }
        });

    }

    public void showCaloriesConsumed(){
        caloriesConsumedTextView = (TextView)findViewById(R.id.calories_consumed_textview);
        String caloriesConsumedString = String.valueOf(caloriesConsumedDb);
        caloriesConsumedTextView.setText(caloriesConsumedString);
    }

    public void showCaloriesLeft(){
        caloriesLeftTextView = (TextView)findViewById(R.id.calories_left_textview2);
        int caloriesLeft = caloriesNeededDb-caloriesConsumedDb;
        if(caloriesLeft<0){
            caloriesLeftTextView.setText("0");
        }
        else{
            String txt = String.valueOf(caloriesLeft);
            caloriesLeftTextView.setText(txt);
        }


    }

    public void updateCaloriesOnProgressBar(){
        caloriesProgressBar = (ProgressBar)findViewById(R.id.calories_progress_bar);
        int percentage = calculateProgress();
        if(percentage!=-1){
            caloriesProgressBar.setProgress(percentage);
        }
    }

    public int calculateProgress(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Profile").child("Calories Needed");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue().toString();
                caloriesNeededDb = Integer.parseInt(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        int input_start=0, input_end=caloriesNeededDb, output_start=0, output_end=100;

        double mappedValueDouble;
        int mappedValue=-1;

            mappedValueDouble= (output_end*caloriesConsumedDb)/input_end;
            mappedValue = (int)mappedValueDouble;
        return mappedValue;
    }

    public void addNewFoodType(){
        addNewMealButton = (Button)findViewById(R.id.add_new_meal_type_button);
        addNewMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder addMealDialog = new AlertDialog.Builder(CaloriesCounter.this);
                addMealDialog.setTitle("Add new meal");

//                final Spinner foodCategoryInput=new Spinner(CaloriesCounter.this);
//                ArrayAdapter<String> foodCategoryInputAdapter = new ArrayAdapter<String>(CaloriesCounter.this,R.layout.style_spinner,foodCategoryList);
//                foodCategoryInput.setAdapter(foodCategoryInputAdapter);
//                addMealDialog.setView(foodCategoryInput);

//                final EditText typeInput = new EditText(CaloriesCounter.this);
//                typeInput.setInputType(InputType.TYPE_CLASS_TEXT);
//                addMealDialog.setView(typeInput);
//
//                final EditText calorieInput = new EditText(CaloriesCounter.this);
//                typeInput.setInputType(InputType.TYPE_CLASS_NUMBER);
//                addMealDialog.setView(calorieInput);
//
//                addMealDialog.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String myText=typeInput.getText().toString();
//                        Toast.makeText(CaloriesCounter.this, myText, Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                addMealDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//
//                addMealDialog.show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
