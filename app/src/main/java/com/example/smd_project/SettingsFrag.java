package com.example.smd_project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;


public class SettingsFrag extends Fragment {


    DataSaver dataSaver;
    private static final String SHARED_PREFS = "Shared_Prefs";
    private static final String WEIGHT = "weight";
    private static final String NAME = "name";

    private static final String MEASURE_UNITS = "units";


    public SettingsFrag() {
        // Required empty public constructor
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View SettingsView = inflater.inflate(R.layout.fragment_settings, container, false);
        TextView stepsDisplay = SettingsView.findViewById(R.id.SettingsStepDisplayId);
        Button SetWeight = SettingsView.findViewById(R.id.setWeightBtnId);
        Button SetName = SettingsView.findViewById(R.id.setNameSettingsId);
        EditText NameInput = SettingsView.findViewById(R.id.PersonNameId);
        EditText Input_weight = SettingsView.findViewById(R.id.WeightInputSystemId);
        dataSaver = new DataSaver(getContext());
        String enter_weight = "";
        String getName = "";

        TextView calDisplay = SettingsView.findViewById(R.id.CaloriesDisplayId);


        calDisplay.setText(String.valueOf(dataSaver.RetrieveAllCalories()) + " cals");


        String stepsTotal = dataSaver.ReadAllStepsFromExerciseTable() + " Steps";
        stepsDisplay.setText(stepsTotal);

        enter_weight = LoadWeight();
        Input_weight.setText(enter_weight);
        getName = LoadName();
        NameInput.setText(getName);

        SetWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String MyWeight = Input_weight.getText().toString();
                SaveWeight(MyWeight);
            }
        });

        SetName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String my_Name = NameInput.getText().toString();
                SaveName(my_Name);
            }
        });



        Spinner spinner = SettingsView.findViewById(R.id.SpinnerId);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.SpinnerItems, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner.setAdapter(adapter);


        // Load spinner selected text
        int selected = LoadUnit();
        spinner.setSelection(selected);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = adapterView.getItemAtPosition(i).toString();
                SaveUnit(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        return SettingsView;
    }

    private void SaveWeight(String w)
    {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(WEIGHT,w);
        editor.apply();

    }

    private String LoadWeight()
    {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);

        return sharedPreferences.getString(WEIGHT, "0");
    }


    private void SaveName(String name)
    {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(NAME,name);
        editor.apply();

    }

    private String LoadName()
    {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);

        return sharedPreferences.getString(NAME, "");
    }

    private void SaveUnit(int unit_pos)
    {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(MEASURE_UNITS,unit_pos);
        editor.apply();


    }

    private int LoadUnit()
    {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);

        return sharedPreferences.getInt(MEASURE_UNITS, 0);
    }

}