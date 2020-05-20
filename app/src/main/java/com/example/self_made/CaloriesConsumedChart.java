package com.example.self_made;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class CaloriesConsumedChart extends AppCompatActivity {

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

        ArrayList<BarEntry> visitors = new ArrayList<>();
        visitors.add(new BarEntry(2014,457));
        visitors.add(new BarEntry(2015,876));
        visitors.add(new BarEntry(2016,1234));
        visitors.add(new BarEntry(2017,457));
        visitors.add(new BarEntry(2018,876));
        visitors.add(new BarEntry(2019,1234));
        visitors.add(new BarEntry(2020,457));
        visitors.add(new BarEntry(2021,876));
        visitors.add(new BarEntry(2022,1234));
        visitors.add(new BarEntry(2023,457));
        visitors.add(new BarEntry(2025,876));
        visitors.add(new BarEntry(2026,1234));

        BarDataSet barDataSet =  new BarDataSet(visitors,"Visitors");
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
