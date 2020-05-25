package com.example.self_made;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class CaloriesConsumedChart extends AppCompatActivity {

    private ArrayList<String> caloriesConsumedInfo;
    private ArrayList<String> dateInfo;

    private String value;

    private BarChart caloriesChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_calories_consumed_chart);

        showCaloriesChart();
    }

    public void showCaloriesChart(){
        caloriesChart = findViewById(R.id.calories_consumed_chart);

        caloriesConsumedInfo = (ArrayList<String>) getIntent().getSerializableExtra("CALORIES_INFO");
        dateInfo = (ArrayList<String>) getIntent().getSerializableExtra("DATE_INFO");

        int aux = Integer.parseInt(caloriesConsumedInfo.get(2));

        ArrayList<BarEntry> calories = new ArrayList<>();
        for(int i=0;i<caloriesConsumedInfo.size();i++)
        {
            calories.add(new BarEntry(i,Integer.parseInt(caloriesConsumedInfo.get(i))));
        }

        Log.d("tagg",String.valueOf(aux));

//
//
//        visitors.add(new BarEntry(2014,457));
//        visitors.add(new BarEntry(2015,876));
//        visitors.add(new BarEntry(2016,1234));
//        visitors.add(new BarEntry(2017,457));
//        visitors.add(new BarEntry(2018,876));
//        visitors.add(new BarEntry(2019,1234));
//        visitors.add(new BarEntry(2020,457));
//        visitors.add(new BarEntry(2021,876));
//        visitors.add(new BarEntry(2022,1234));
//        visitors.add(new BarEntry(2023,457));
//        visitors.add(new BarEntry(2025,876));
//        visitors.add(new BarEntry(2026,1234));

        BarDataSet barDataSet =  new BarDataSet(calories,"Calories consumed");
        barDataSet.setColor(Color.GREEN);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        caloriesChart.setFitBars(true);
        caloriesChart.setData(barData);
        caloriesChart.getDescription().setText("Bar chart");
        caloriesChart.animateY(1000);

    }
}
