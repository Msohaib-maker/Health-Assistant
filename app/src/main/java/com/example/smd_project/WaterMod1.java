package com.example.smd_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WaterMod1 extends AppCompatActivity {

    private static int glassesIntake = 0;
    DataSaver dataSaver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_mod1);
        dataSaver = new DataSaver(this);
        TextView displayDate = findViewById(R.id.DateId);
        String CurrentDate = getDate();
        BasicIntakeClass basicIntakeClass = dataSaver.Load_Intake_Data(CurrentDate);
        if(!dataSaver.checkIfDateExists(CurrentDate))
        {
            dataSaver.Insert(CurrentDate);
        }
        displayDate.setText(CurrentDate);
        EditText IntakeGoal = findViewById(R.id.IntakeGoalEdittext);
        RelativeLayout setBtn = findViewById(R.id.setBtnId);
        TextView MyIntakes = findViewById(R.id.glassesTrackId);
        /*CardView addBtn,minusBtn;
        addBtn = findViewById(R.id.addGlassId);
        minusBtn = findViewById(R.id.minusGlassId);*/

        if(basicIntakeClass != null)
        {
            IntakeGoal.setText(basicIntakeClass.getGoal());
            //MyIntakes.setText(String.valueOf(basicIntakeClass.getIntake()));

            glassesIntake = basicIntakeClass.getIntake();

        }


        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String intake_goal = IntakeGoal.getText().toString();
                dataSaver.UpdateGoal(CurrentDate,intake_goal);

            }
        });

        /*addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                glassesIntake++;
                MyIntakes.setText(String.valueOf(glassesIntake));
                dataSaver.UpdateIntake(CurrentDate,String.valueOf(glassesIntake));
            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                glassesIntake--;
                MyIntakes.setText(String.valueOf(glassesIntake));
                dataSaver.UpdateIntake(CurrentDate,String.valueOf(glassesIntake));
            }
        });*/


        // Back button implemented using relative layout
        RelativeLayout BackBtn = findViewById(R.id.backBtn);
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WaterMod1.this,MainActivity.class)
                        .putExtra("CurrentFragment","waterFrag"));
            }
        });


    }

    private String getDate()
    {
        Date currentDate = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(currentDate);
    }
}