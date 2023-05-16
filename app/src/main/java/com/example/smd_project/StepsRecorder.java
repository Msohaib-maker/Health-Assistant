package com.example.smd_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StepsRecorder extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor StepSensor;
    private int stepCount = 0;
    TextView stepsRecordOnTextView;
    private double Magnitude_prev = 0;
    EditText WeightInput;
    TextView CaloriesNum;
    TextView CalorieText;
    Button EnterWeight;
    private static final String SHARED_PREFS = "Shared_Prefs";
    private static final String WEIGHT = "weight";

    private boolean isSensorWorking = false;

    private int weight = 0;
    private float caloriesBurnt = 0;

    private boolean oneTime = false;

    String CurrentDate = "";
    DataSaver dataSaver;
    UtilityClass utilityClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_recorder);

        dataSaver = new DataSaver(this); // initialise data base object
        CurrentDate = getDate();

        if(!dataSaver.CheckDataInExerciseTableIfExists(CurrentDate))
        {
            dataSaver.InsertIntoExerciseTable(CurrentDate);
        }

        utilityClass = dataSaver.ReadInfoFromExerciseTable(CurrentDate);





        stepsRecordOnTextView = findViewById(R.id.stepsCountId);
        CardView startSensor,StopSensor,BackBtn;
        startSensor = findViewById(R.id.StartRecordStepsId);
        StopSensor = findViewById(R.id.StopRecordStepsId);
        BackBtn = findViewById(R.id.BackRecordStepsId);
        Button CheckCalories = findViewById(R.id.CheckCaloriesId);
        // for checking calorie
        WeightInput = findViewById(R.id.WeightId);
        EnterWeight = findViewById(R.id.EnterWeightBtnId);
        CaloriesNum = findViewById(R.id.CalorieNumberId);
        CalorieText = findViewById(R.id.CalorieText);


        WeightInput.setVisibility(View.GONE);
        EnterWeight.setVisibility(View.GONE);
        CaloriesNum.setVisibility(View.VISIBLE);
        CalorieText.setVisibility(View.VISIBLE);


        if(utilityClass != null)
        {
            //Toast.makeText(StepsRecorder.this, "enter hua", Toast.LENGTH_SHORT).show();
            caloriesBurnt = utilityClass.getCalories();
            stepCount = utilityClass.getSteps();

            CaloriesNum.setText(String.valueOf(caloriesBurnt));
            stepsRecordOnTextView.setText(String.valueOf(stepCount));

        }




        // initial 0 value is set to weight
        WeightInput.setText(String.valueOf(weight));



        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);



        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null)
        {
            StepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


            startSensor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Toast.makeText(StepsRecorder.this, "Started", Toast.LENGTH_SHORT).show();
                        if(StepSensor != null && !isSensorWorking)
                        {
                            sensorManager.registerListener(StepsRecorder.this,StepSensor, SensorManager.SENSOR_DELAY_NORMAL);
                            isSensorWorking = true;
                        }
                    }
                    catch (Exception e)
                    {

                    }

                }
            });

            StopSensor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try
                    {
                        Toast.makeText(StepsRecorder.this, "Stopped", Toast.LENGTH_SHORT).show();
                        if(StepSensor != null && isSensorWorking)
                        {
                            sensorManager.unregisterListener(StepsRecorder.this,StepSensor);
                            isSensorWorking = false;
                        }
                    }
                    catch (Exception e)
                    {

                    }

                }
            });
        }
        else
        {
            Toast.makeText(this, "Your Device does not have a sensor", Toast.LENGTH_SHORT).show();
        }


        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StepsRecorder.this,MainActivity.class)
                        .putExtra("CurrentFragment","ExerciseFrag"));
            }
        });


        CheckCalories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //WeightInput.setVisibility(View.VISIBLE);
                //EnterWeight.setVisibility(View.VISIBLE);
                CaloriesNum.setVisibility(View.VISIBLE);
                CalorieText.setVisibility(View.VISIBLE);

                /*utilityClass = dataSaver.ReadInfoFromExerciseTable(CurrentDate);

                if(!oneTime)
                {
                    caloriesBurnt = (float) ((Integer.parseInt(LoadWeight()) * 0.49) * stepCount);

                    CaloriesNum.setText(String.valueOf(caloriesBurnt));
                    oneTime = true;
                }
                else
                {
                    caloriesBurnt = (float) ((Integer.parseInt(LoadWeight()) * 0.49) * stepCount);

                    CaloriesNum.setText(String.valueOf(caloriesBurnt));

                }


                if(utilityClass != null)
                {
                    //Toast.makeText(StepsRecorder.this, "enter hua", Toast.LENGTH_SHORT).show();
                    caloriesBurnt = utilityClass.getCalories();
                    stepCount = utilityClass.getSteps();

                    CaloriesNum.setText(String.valueOf(caloriesBurnt));
                    stepsRecordOnTextView.setText(String.valueOf(stepCount));

                }*/





            }
        });

        EnterWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String w8 = WeightInput.getText().toString();
                weight = Integer.parseInt(w8);

                caloriesBurnt = (float) ((Integer.parseInt(WeightInput.getText().toString()) * 0.49) * stepCount);

                CaloriesNum.setText(String.valueOf(caloriesBurnt));
                dataSaver.UpdateCalorieInExerciseTable(CurrentDate,String.valueOf(caloriesBurnt));


            }
        });



        // below code is for testing purpose
        /*Button testBtn = findViewById(R.id.testingBtnId);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepCount++;
                dataSaver.UpdateStepsInExerciseTable(CurrentDate,String.valueOf(stepCount));
            }
        });*/




    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        try
        {
            if (sensorEvent.sensor == StepSensor) {
                // Calculate acceleration magnitude
                float x = (float) sensorEvent.values[0];
                float y = (float) sensorEvent.values[1];
                float z = (float) sensorEvent.values[2];

                double Magnitude = Math.sqrt(x*x + y+y + z*z);
                double MagnitudeDelta = Magnitude - Magnitude_prev;
                MagnitudeDelta = Math.abs(MagnitudeDelta);
                //Toast.makeText(this, String.valueOf(MagnitudeDelta), Toast.LENGTH_SHORT).show();
                Magnitude_prev = Magnitude;
                if (MagnitudeDelta >3.7f && MagnitudeDelta < 6.5f)
                {
                    stepCount++;

                    caloriesBurnt = (float) ((Integer.parseInt(LoadWeight()) * 0.008993) * stepCount);

                    CaloriesNum.setText(String.valueOf(caloriesBurnt));

                    dataSaver.UpdateStepsInExerciseTable(CurrentDate,String.valueOf(stepCount));
                    dataSaver.UpdateCalorieInExerciseTable(CurrentDate,String.valueOf(caloriesBurnt));
                    stepsRecordOnTextView.setText(String.valueOf(stepCount));
                }
            }
        }
        catch (Exception e)
        {

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    protected void onPause() {
        super.onPause();
        if(StepSensor != null && isSensorWorking)
        {
            sensorManager.unregisterListener(StepsRecorder.this,StepSensor);
            isSensorWorking = false;
        }

    }

    private String getDate()
    {
        java.util.Date currentDate = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(currentDate);
    }

    private String LoadWeight()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        return sharedPreferences.getString(WEIGHT, "0");
    }





}