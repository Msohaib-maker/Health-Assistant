package com.example.smd_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PushUps extends AppCompatActivity implements SensorEventListener {

    TextView displayPushUps;
    TextView CaloriesCount;
    Sensor PushUpSensor;
    private static boolean isWorking = false;
    private static int PushCount = 0;
    private static boolean StartBtn = false;
    SensorManager sensorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_ups);
        Button start,stop;
        start = findViewById(R.id.startPushBtn);
        stop = findViewById(R.id.StopPushBtn);
        CaloriesCount = findViewById(R.id.caloriesWala);
        displayPushUps = findViewById(R.id.PushCountId);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if(sensorManager != null)
        {
            if(sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null)
            {
                PushUpSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            }
            else
            {
                Toast.makeText(this, "Sensor is not present in your device", Toast.LENGTH_SHORT).show();
            }

        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartBtn = true;
                if(PushUpSensor != null && !isWorking)
                {
                    Toast.makeText(PushUps.this, "start", Toast.LENGTH_SHORT).show();
                    assert sensorManager != null;
                    sensorManager.registerListener(PushUps.this,PushUpSensor,SensorManager.SENSOR_DELAY_NORMAL);
                    isWorking = true;
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartBtn = false;
                if(PushUpSensor != null && isWorking)
                {
                    Toast.makeText(PushUps.this, "stop", Toast.LENGTH_SHORT).show();
                    assert sensorManager != null;
                    sensorManager.unregisterListener(PushUps.this,PushUpSensor);
                    isWorking = false;
                }
            }
        });

        Button backBtn = findViewById(R.id.BackBtnId3);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PushUps.this,MainActivity.class)
                        .putExtra("CurrentFragment","ExerciseFrag"));
            }
        });



    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY)
        {
            if(sensorEvent.values[0] > 0)
            {
                Toast.makeText(this, String.valueOf(sensorEvent.values[0]), Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, String.valueOf(sensorEvent.values[0]), Toast.LENGTH_SHORT).show();
                PushCount++;
                displayPushUps.setText(String.valueOf(PushCount));
                float cal_brn = (float) ((PushCount * 0.5) / 200);
                CaloriesCount.setText(String.valueOf(cal_brn));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(PushUpSensor != null && isWorking)
        {
            assert sensorManager != null;
            sensorManager.unregisterListener(PushUps.this,PushUpSensor);
            isWorking = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        if(PushUpSensor != null && !isWorking && StartBtn)
        {
            assert sensorManager != null;
            sensorManager.unregisterListener(PushUps.this,PushUpSensor);
            isWorking = true;
        }


    }
}