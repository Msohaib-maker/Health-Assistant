package com.example.smd_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String CURRENT_FRAGMENT_KEY = "CurrentFragment";
    String currentFrag;

    String FragState = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navId);


        currentFrag = getIntent().getStringExtra(CURRENT_FRAGMENT_KEY);

        if(savedInstanceState !=null)
        {
            currentFrag = savedInstanceState.getString(CURRENT_FRAGMENT_KEY);
        }

        if(currentFrag != null)
        {
            if(currentFrag.equals("waterFrag"))
            {
                bottomNavigationView.setSelectedItemId(R.id.waterFragId);
                ReplaceFragment(new WaterFrag(),0);
                FragState = "2";
            }
            else if(currentFrag.equals("ExerciseFrag"))
            {
                bottomNavigationView.setSelectedItemId(R.id.heartFragId);
                ReplaceFragment(new HeartFrag(),0);
                FragState = "3";
            }
        }
        else
        {
            bottomNavigationView.setSelectedItemId(R.id.HomeFragId);
            ReplaceFragment(new HomeFrag(),1);
            FragState = "1";
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // Handle item selection events here
                        return true;
                    }
                });



        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.HomeFragId:
                        ReplaceFragment(new HomeFrag(),0);
                        FragState = "1";
                        break;
                    case R.id.heartFragId:
                        ReplaceFragment(new HeartFrag(),0);
                        FragState = "2";
                        break;
                    case R.id.waterFragId:
                        ReplaceFragment(new WaterFrag(),0);
                        FragState = "3";
                        break;
                    case R.id.MedicationFragId:
                        ReplaceFragment(new MedicineFrag(),0);
                        FragState = "4";
                        break;
                    case R.id.settingsFragId:
                        ReplaceFragment(new SettingsFrag(),0);
                        FragState = "5";
                }

                return true;
            }
        });



    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("FragKey",FragState);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        FragState = savedInstanceState.getString("FragKey");
        if(FragState.equals("1"))
        {
            ReplaceFragment(new HomeFrag(),0);
        }
        else if(FragState.equals("2"))
        {
            ReplaceFragment(new HeartFrag(),0);
        }
        else if(FragState.equals("3"))
        {
            ReplaceFragment(new WaterFrag(),0);
        }
        else if(FragState.equals("4"))
        {
            ReplaceFragment(new MedicineFrag(),0);
        }
        else if(FragState.equals("5"))
        {
            ReplaceFragment(new SettingsFrag(),0);
        }
    }

    private void ReplaceFragment(Fragment f, int flag)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(flag == 1)
        {
            fragmentTransaction.add(R.id.containerId,f);
        }
        else
        {
           fragmentTransaction.replace(R.id.containerId,f);
        }

        fragmentTransaction.commit();

    }
}