package com.example.smd_project;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;


public class HomeFrag extends Fragment {

    private static final String SHARED_PREFS = "Shared_Prefs";
    private static final String WEIGHT = "weight";
    private static final String NAME = "name";
    AdView adView;
    DataSaver dataSaver;

    public HomeFrag() {
        // Required empty public constructor
    }



    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);

        RelativeLayout waterFrag,MedFrag,HeartFrag;
        TextView myName = homeView.findViewById(R.id.myNameHomeId);


        myName.setText("Hi, " + LoadName());


        waterFrag = homeView.findViewById(R.id.waterWalaId);
        MedFrag = homeView.findViewById(R.id.MedicineWalaId);
        HeartFrag = homeView.findViewById(R.id.HeartWalaId);


        waterFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "Water", Toast.LENGTH_SHORT).show();

            }
        });
        MedFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "Med", Toast.LENGTH_SHORT).show();
            }
        });
        HeartFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "Heart", Toast.LENGTH_SHORT).show();
            }
        });


        dataSaver = new DataSaver(getContext());
        adView = homeView.findViewById(R.id.adViewId);
        TextView DayIntakes = homeView.findViewById(R.id.homeParIntakes);
        TextView TotalIntakes = homeView.findViewById(R.id.homeIntakesTotal);
        TextView LeftReminders = homeView.findViewById(R.id.ReminderHomeToday);
        TextView Total_Meds = homeView.findViewById(R.id.TotalMedsId);

        TextView Weight = homeView.findViewById(R.id.WeightHomeId);
        TextView AverageSteps = homeView.findViewById(R.id.StepsHomeId);
        TextView TotalSteps = homeView.findViewById(R.id.StepsTotalHomeId);

        TextView DailyCals = homeView.findViewById(R.id.DailyCaloriesHomeId);
        TextView TotalCals = homeView.findViewById(R.id.TotalCaloriesHomeId);


        UtilityClass utilityClass = dataSaver.ReadInfoFromExerciseTable(TImeUtilityClass.currentDateToString());
        if(utilityClass != null)
        {
            DailyCals.setText("Daily Calories : " +  String.valueOf(utilityClass.getCalories()));
        }
        else
        {
            DailyCals.setText("Daily Calories : " +  "0");
        }


        TotalCals.setText("Total Calories : " + String.valueOf(dataSaver.RetrieveAllCalories()));
        DayIntakes.setText("Today Intakes : " + String.valueOf(TodayIntakes()));
        TotalIntakes.setText("Total Intakes : " + String.valueOf(TotalIntakes()));
        Total_Meds.setText("Total  Medicines : " + String.valueOf(TotalMeds()));


        Weight.setText("Weight : " + LoadWeight());
        AverageSteps.setText("Average daily Steps : " + String.valueOf(getAverageSteps()));
        TotalSteps.setText("Total Steps : " + String.valueOf(dataSaver.ReadAllStepsFromExerciseTable()));

        // mobile ads
        MobileAds.initialize(getContext());
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        // ----------




        return homeView;
    }

    private int TodayIntakes()
    {
        int num = 0;
        String currentDate = TImeUtilityClass.currentDateToString();

        if(dataSaver.checkIfDateExists(currentDate))
        {
            BasicIntakeClass basicIntakeClass = dataSaver.Load_Intake_Data(currentDate);
            if(basicIntakeClass != null)
            {
                num = basicIntakeClass.getIntake();
                return num;
            }
        }




        return num;
    }

    private int TotalIntakes()
    {
        int num = 0;
        ArrayList<BasicIntakeClass> basicIntakeClasses = dataSaver.LoadIntakeAllData();


        for(BasicIntakeClass basicIntakeClass : basicIntakeClasses)
        {
            num = num + basicIntakeClass.getIntake();
        }
        return num;
    }

    private int AlarmsYetToTrigger()
    {


        return 0;
    }

    private int TotalMeds()
    {

        if(dataSaver.CheckPillTableDataIfExists())
        {
            ArrayList<Pill> pills = dataSaver.ReadPillTableData();
            return pills.size();
        }
        else
        {
            return 0;
        }
    }

    private String LoadWeight()
    {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        return sharedPreferences.getString(WEIGHT, "0");
    }

    private int getAverageSteps()
    {
        String currentDate = TImeUtilityClass.currentDateToString();
        ArrayList<UtilityClass> utilityClassArrayList = dataSaver.ReadExerciseALLData();

        if(utilityClassArrayList.size() != 0)
        {
            int steps = 0;
            for(UtilityClass utilityClass1 : utilityClassArrayList)
            {
                steps = steps + utilityClass1.getSteps();
            }

            steps = steps/utilityClassArrayList.size();
            return steps;
        }
        else
        {
            return 0;
        }



    }

    private String LoadName()
    {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);

        return sharedPreferences.getString(NAME, "");
    }

}