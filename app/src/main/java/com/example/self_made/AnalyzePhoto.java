package com.example.self_made;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.URI;

public class AnalyzePhoto extends AppCompatActivity {

    private static final int PERMISION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE =  1001;

    private Button editProfileButton,setMealsButton;
    private Button capture_image_button;
    ImageView image_captured;

    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze_photo);
        onProfileButtonClick();
        onMealButtonClick();

        captureImage();
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

    public void captureImage(){
        image_captured = (ImageView)findViewById(R.id.image_captured);
        capture_image_button = (Button)findViewById(R.id.capture_image_button);


        capture_image_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //if system >= Marshmallow, request runtime permission
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                                //request permission if not given
                                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                //show popup to request permission
                                requestPermissions(permission,PERMISION_CODE);
                            }
                            else{
                                //permission aready granted
                                openCamera();
                            }

                        }

                        else
                        {
                            //system < Marshmallow
                            openCamera();
                        }
                    }
                }
        );
    }

    public void openCamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    //handling permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //method called when user presses Allow/Deny from permission request popup
        switch (requestCode){
            case PERMISION_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission was granted
                    openCamera();
                }
                else{
                    //permision denied
                    Toast.makeText(this,"Permision denied..",Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(PERMISION_CODE,IMAGE_CAPTURE_CODE,null);
        //called when image was captured from camera
        if(resultCode == RESULT_OK){
            //set the image capture to our imageView
            image_captured.setImageURI(image_uri);

        }
    }
}
