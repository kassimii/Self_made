package com.example.self_made;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CaloriesChart extends AppCompatActivity {

    private ArrayList<String> caloriesConsumedInfo = new ArrayList<>();
    private ArrayList<String> dateInfo = new ArrayList<>();

    private ListView listViewCalories;

    private String[] caloriesArray;

    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_calories_chart);

        if(FirebaseAuth.getInstance().getCurrentUser()!= null) {
            String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(userUid);

            showLog();
        }
    }


    public void showLog(){
        listViewCalories = (ListView)findViewById(R.id.listview_calories);
       // caloriesArray = caloriesConsumedInfo.toArray(new String[0]);
        final ArrayAdapter<String> caloriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,caloriesConsumedInfo);
        listViewCalories.setAdapter(caloriesAdapter);

        DatabaseReference reference = databaseRef;

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dateInfo.clear();
                caloriesConsumedInfo.clear();
                for(DataSnapshot snapshot: dataSnapshot.child("Daily Calories").getChildren()) {
                    caloriesConsumedInfo.add(snapshot.getKey() + "th day of the year" + "\t\t\t\t" + snapshot.getValue().toString() + " calories consumed");
                    caloriesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
