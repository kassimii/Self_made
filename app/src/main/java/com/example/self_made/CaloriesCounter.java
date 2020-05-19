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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CaloriesCounter extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button editProfileButton,setMealsButton;
    private Spinner foodCategorySpinner, foodTypeSpinner;
    private ProgressBar caloriesProgressBar;
    private TextView caloriesConsumedTextView;

    private Button breadsCerealButton;
    private String caloriesPerIngredient;
    private ArrayList<String> foodCategoryList, breadsCerealsList, meatFishList, fruitsVegetablesList, milkAndDairyList, fatsSugarsList, otherFoodTypeList;
    private ArrayAdapter<String> foodCategoryAdapter, foodTypeAdapter, breadsCerealsAdapter, meatFishAdapter, fruitsVegetablesAdapter,milkAndDairyAdapter, fatsSugarsAdapter, otherFoodTypeAdapter;

    private int BMR, caloriesNeeded, caloriesConsumed=0;
    private int foodTypeCalories=-1; //variable for holding the calories each food type selected that have to be added to the daily calories consumed
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

        setFoodCategoryList();
        onProfileButtonClick();
        onMealButtonClick();
        calculateNeededCalories();
        getFoodTypesFromDatabase();
        chooseFoodCategory();
        saveCaloriesOfMeal();
        showCaloriesConsumed();

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

    public void setFoodCategoryList(){
        foodCategoryList =  new ArrayList<String>();
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
                    foodCategorySelected=0;
                    foodTypeAdapter = new ArrayAdapter<String>(CaloriesCounter.this,R.layout.style_spinner, breadsCerealsList);
                }

                if(position==1){
                    foodCategorySelected=1;
                    foodTypeAdapter = new ArrayAdapter<String>(CaloriesCounter.this,R.layout.style_spinner, meatFishList);
                }

                if(position==2){
                    foodCategorySelected=2;
                    foodTypeAdapter = new ArrayAdapter<String>(CaloriesCounter.this,R.layout.style_spinner, fruitsVegetablesList);
                }

                if(position==3){
                    foodCategorySelected=3;
                    foodTypeAdapter = new ArrayAdapter<String>(CaloriesCounter.this,R.layout.style_spinner, milkAndDairyList);
                }

                if(position==4){
                    foodCategorySelected=4;
                    foodTypeAdapter = new ArrayAdapter<String>(CaloriesCounter.this,R.layout.style_spinner, fatsSugarsList);
                }

                if(position==5){
                    foodCategorySelected=5;
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

                getCaloriesForFoodTypeFromDb(foodTypeSelected);
            }

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
                foodTypeCalories=caloricValue;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void saveCaloriesOfMeal(){

    }

    public void showCaloriesConsumed(){
        caloriesConsumedTextView = (TextView)findViewById(R.id.calories_consumed_textview);
        String caloriesConsumedString = String.valueOf(caloriesConsumed);
        caloriesConsumedTextView.setText(caloriesConsumedString);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
