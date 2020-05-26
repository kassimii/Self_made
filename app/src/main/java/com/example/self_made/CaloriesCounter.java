package com.example.self_made;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class CaloriesCounter extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NewMealDialogListener{

    private Button editProfileButton,setMealsButton;
    private Button saveMealButton, addNewMealButton, showCaloriesConsumedChartButton;
    private Spinner foodCategorySpinner, foodTypeSpinner;
    private ProgressBar caloriesProgressBar;
    private TextView caloriesConsumedTextView, caloriesLeftTextView;

    private ArrayList<String> foodCategoryList, breadsCerealsList, meatFishList, fruitsVegetablesList, milkAndDairyList, fatsSugarsList, otherFoodTypeList;
    private ArrayAdapter<String> foodCategoryAdapter, foodTypeAdapter;

    private int BMR, AMR, caloriesNeeded;
    private int caloriesConsumed=-1; //flag for saving calories consumed to db
    public int caloriesNeededDb, caloriesConsumedDb;
    private int caloriesPerFoodType =-1; //variable for holding the calories each food type selected that have to be added to the daily calories consumed
    private int foodCategorySelected=-1; //variable to find which one of the food categories was selected used to extract calories from db

    private Calendar cDate, cTime;
    private int currentDate, dateFromDB;

    private DatabaseReference databaseRef;


    @Override
    public void addFoodType(String foodType, String calories) {
        FirebaseDatabase.getInstance().getReference().child("Calories").child("Others").child(foodType).setValue(calories);
    }

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

        if(FirebaseAuth.getInstance().getCurrentUser()!= null)
        {
            String userUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
            databaseRef=FirebaseDatabase.getInstance().getReference("Users").child(userUid);

            getCaloriesValuesFromDb();
            updateDailyConsumedCalories();
            saveCaloriesOfMeal();
            showCaloriesConsumed();
            showCaloriesLeft();
            setFoodCategoryList();
            calculateNeededCalories();
            getFoodTypesFromDatabase();
            chooseFoodCategory();
            addNewFoodType();
            showCaloriesConsumedChart();
            if(caloriesConsumedDb!=0)
              updateCaloriesOnProgressBar();
        }


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

        this.overridePendingTransition(0,0);
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

        this.overridePendingTransition(0,0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    }

    public void getCaloriesValuesFromDb(){
        DatabaseReference reference = databaseRef.child("Profile");

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

    public void updateDailyConsumedCalories(){
        Calendar calendar = Calendar.getInstance();
        currentDate = calendar.get(Calendar.DAY_OF_YEAR);
        String currentDateString = String.valueOf(currentDate);


        DatabaseReference reference = databaseRef.child("Profile");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               String date = dataSnapshot.child("Current Day").getValue().toString();
               dateFromDB = Integer.parseInt(date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(dateFromDB!=currentDate && dateFromDB!=0)
        {
            caloriesConsumedDb = 0;
            databaseRef.child("Profile").child("Calories Consumed").setValue(caloriesConsumedDb);
            databaseRef.child("Profile").child("Current Day").setValue(currentDateString);
        }

        if(dateFromDB!=0)
        {
            databaseRef.child("Daily Calories").child(currentDateString).setValue(caloriesConsumedDb);
        }



    }

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
                int age,height,weight, activityLevel, weightGoal;
                double heightInches, weightPounds;
                double BMRDouble=0;
                double AMRDouble=0;

                String gender = dataSnapshot.child("Gender").getValue().toString();
                String weightString = dataSnapshot.child("Weight").getValue().toString();
                String heightString = dataSnapshot.child("Height").getValue().toString();
                String ageString = dataSnapshot.child("Age").getValue().toString();
                String activityLevelString = dataSnapshot.child("Activity Level").getValue().toString();
                String weightGoalString = dataSnapshot.child("Weight Goal").getValue().toString();


                age = Integer.parseInt(ageString);
                weight = Integer.parseInt(weightString);
                height = Integer.parseInt(heightString);
                activityLevel = Integer.parseInt(activityLevelString);
                weightGoal = Integer.parseInt(weightGoalString);

                weightPounds = weight*2.2;
                heightInches = height*0.39;

                if (gender.equals("male")) {
                        BMRDouble = 66 + (6.23*weightPounds) + (12.7*heightInches) - (6.8*age);
                }

                if (gender.equals("female")) {
                    BMRDouble = 655 + (4.35*weightPounds) + (4.7*heightInches) - (4.7*age);
                }

                BMR = (int)BMRDouble;

                if(activityLevel==1){
                    AMRDouble = BMR * 1.2;
                }

                if(activityLevel==2){
                    AMRDouble = BMR * 1.375;
                }

                if(activityLevel==3){
                    AMRDouble = BMR * 1.55;
                }

                if(activityLevel==4){
                    AMRDouble = BMR * 1.725;
                }

                if(activityLevel==5){
                    AMRDouble = BMR * 1.9;
                }

                AMR=(int)AMRDouble;

                if(weightGoal==1){
                    caloriesNeeded = AMR - 500;
                }

                if(weightGoal==2){
                    caloriesNeeded = AMR;
                }

                if(weightGoal==3){
                    caloriesNeeded = AMR + 300;
                }

                databaseRef.child("Profile").child("Calories Needed").setValue(caloriesNeeded);
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
                       databaseRef.child("Profile").child("Calories Consumed").setValue(caloriesConsumedDb);
                        caloriesConsumed=-1;
                   }
                    showCaloriesConsumed();
                    showCaloriesLeft();
                    updateCaloriesOnProgressBar();
                    updateDailyConsumedCalories();
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
        DatabaseReference reference = databaseRef.child("Profile").child("Calories Needed");
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
                openDialog();
            }
        });
    }

    public void openDialog(){
        NewMealDialog  newMealDialog = new NewMealDialog();
        newMealDialog.show(getSupportFragmentManager(),"add meal dialog");
    }

    public void showCaloriesConsumedChart(){
        showCaloriesConsumedChartButton = (Button)findViewById(R.id.show_calories_consumed_chart_button);
        showCaloriesConsumedChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),CaloriesChart.class));
//
//                Intent intent = new Intent(getApplicationContext(),CaloriesConsumedChart.class);
//                intent.putExtra("CALORIES_INFO",caloriesConsumedInfo);
//                intent.putExtra("DATE_INFO",dateInfo);
//                startActivity(intent);
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
