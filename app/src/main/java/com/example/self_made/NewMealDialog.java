package com.example.self_made;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;


public class NewMealDialog extends AppCompatDialogFragment {
    private EditText editFoodType;
    private EditText editCalories;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            listener = (NewMealDialogListener) context;
        }catch(ClassCastException e){
           throw new ClassCastException(context.toString() + "must implement NewMealDialogListener");
        }
    }

    private NewMealDialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);

        builder.setView(view)
                .setTitle("Add new meal type")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String foodType = editFoodType.getText().toString();
                        String calories = editCalories.getText().toString();

                        listener.addFoodType(foodType,calories);
                    }
                });

        editFoodType = view.findViewById(R.id.edit_food_type);
        editCalories = view.findViewById(R.id.edit_calories);

       return builder.create();
    }


}
