package com.example.self_made;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

import android.widget.TextView;
import android.widget.TimePicker;

public class SetMealHours extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    //    private EditText breakfast_hour, snack1_hour, lunch_hour, snack2_hour, dinner_hour;
//    private Button save_plan_button;
    private Button analysePhotoButton;
    private Button editProfileButton;

    private Button breakfast_button, snack1_button, lunch_button, snack2_button, dinner_button;
    private TextView showHourBreakfast, showHourSnack1, showHourLunch, showHourSnack2, showHourDinner;

    private Button save_meal_plan_button, show_meal_plan_button;

    public static String meal_plan_popup;

    int hour_x, minute_x;
    int breakfast_hour, breakfast_minute, snack1_hour, snack1_minute, lunch_hour, lunch_minute,
            snack2_hour, snack2_minute, dinner_hour, dinner_minute;
    int flag=0; //decide which time is set
    /*
        0-nothing set
        1-breakfast
        2-snack 1
        3-lunch
        4-snack 2
        5-dinner
    */

    private final int NOTIFICATION_ID = 001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_meal_hours);
        onProfileButtonClick();
        onCameraButtonClick();
        setHours();
        showMealPlan();
        sendBreakfastNotification();
    }

    public void onProfileButtonClick() {
        editProfileButton = (Button) findViewById(R.id.profile_button);
        editProfileButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish(); //return to Main Activity before opening Profile Editing Activity
                        openProfileEditingActivity();
                    }
                }
        );
    }

    public void openProfileEditingActivity() {
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);

        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    }

    public void onCameraButtonClick() {
        analysePhotoButton = (Button) findViewById(R.id.photo_button);
        analysePhotoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        openPhotoAnalsisActivity();
                    }
                }
        );
    }

    public void openPhotoAnalsisActivity() {
        Intent intent = new Intent(this, AnalyzePhoto.class);
        startActivity(intent);

        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    }

    public void setHours(){
        breakfast_button = (Button)findViewById(R.id.breakfast_button);
        //showHourBreakfast = (TextView)findViewById(R.id.breakfast_view);

        snack1_button = (Button)findViewById(R.id.snack1_button);
        //showHourSnack1 = (TextView)findViewById(R.id.snack1_view);

        lunch_button = (Button)findViewById(R.id.lunch_button);
        //showHourLunch = (TextView)findViewById(R.id.lunch_view);

        snack2_button = (Button)findViewById(R.id.snack2_button);
        //showHourSnack2 = (TextView)findViewById(R.id.snack2_view);

        dinner_button = (Button)findViewById(R.id.dinner_button);
        //showHourDinner = (TextView)findViewById(R.id.dinner_view);

        breakfast_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                breakfast_hour=c.get(Calendar.HOUR);
                breakfast_minute=c.get(Calendar.MINUTE);
                flag=1;
                TimePickerDialog timePickerDialog = new TimePickerDialog(SetMealHours.this,
                        SetMealHours.this,breakfast_hour,breakfast_minute,true);
                timePickerDialog.show();
            }
        });

        snack1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                snack1_hour=c.get(Calendar.HOUR);
                snack1_minute=c.get(Calendar.MINUTE);
                flag=2;
                TimePickerDialog timePickerDialog = new TimePickerDialog(SetMealHours.this,
                        SetMealHours.this,snack1_hour,snack1_minute,true);
                timePickerDialog.show();
            }
        });

        lunch_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                lunch_hour=c.get(Calendar.HOUR);
                lunch_minute=c.get(Calendar.MINUTE);
                flag=3;
                TimePickerDialog timePickerDialog = new TimePickerDialog(SetMealHours.this,
                        SetMealHours.this,lunch_hour,lunch_minute,true);
                timePickerDialog.show();
            }
        });

        snack2_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                snack2_hour=c.get(Calendar.HOUR);
                flag=4;
                snack2_minute=c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(SetMealHours.this,
                        SetMealHours.this,snack2_hour,snack2_minute,true);
                timePickerDialog.show();
            }
        });

        dinner_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                dinner_hour=c.get(Calendar.HOUR);
                dinner_minute=c.get(Calendar.MINUTE);
                flag=5;
                TimePickerDialog timePickerDialog = new TimePickerDialog(SetMealHours.this,
                        SetMealHours.this,dinner_hour,dinner_minute,true);
                timePickerDialog.show();
            }
        });
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hour_x=hourOfDay;
        minute_x=minute;

        if(flag==1)
        {
            breakfast_hour=hour_x;
            breakfast_minute=minute_x;
         //   showHourBreakfast.setText("hour: " + breakfast_hour + "\nminute: " + breakfast_minute);
        }

        if(flag==2)
        {
            snack1_hour=hour_x;
            snack1_minute=minute_x;
         //   showHourSnack1.setText("hour: " + snack1_hour + "\nminute: " + snack1_minute);
        }

        if(flag==3)
        {
            lunch_hour=hour_x;
            lunch_minute=minute_x;
          //  showHourLunch.setText("hour: " + lunch_hour + "\nminute: " + lunch_minute);
        }

        if(flag==4)
        {
            snack2_hour=hour_x;
            snack2_minute=minute_x;
           // showHourSnack2.setText("hour: " + snack2_hour + "\nminute: " + snack2_minute);
        }

        if(flag==5)
        {
            dinner_hour=hour_x;
            dinner_minute=minute_x;
           // showHourDinner.setText("hour: " + dinner_hour + "\nminute: " + dinner_minute);
        }

        meal_plan_popup="Breakfast: " + breakfast_hour + ":" + breakfast_minute + "\n" +
                "Snack 1: " + snack1_hour + ":" + snack1_minute + "\n" +
                "Lunch: " + lunch_hour + ":" + lunch_minute + "\n" +
                "Snack 2: " + snack2_hour + ":" + snack2_minute + "\n" +
                "Dinner: " + dinner_hour + ":" + dinner_minute + "\n" ;
    }

    public void showMealPlan(){

        show_meal_plan_button = (Button)findViewById(R.id.show_meal_plan_button);

        show_meal_plan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMealPlan showMealPlan = new ShowMealPlan();
                showMealPlan.show(getSupportFragmentManager(), "Meal Planning");
            }
        });
    }


    public void sendBreakfastNotification(){

        save_meal_plan_button = (Button)findViewById(R.id.save_meal_plan_button);

        save_meal_plan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.HOUR,1);
                calendar.set(Calendar.MINUTE,27);

                Intent intent = new Intent(SetMealHours.this, NotificationReceiver.class);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(SetMealHours.this,NOTIFICATION_ID, intent,PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);


            }
        });

    }


}

