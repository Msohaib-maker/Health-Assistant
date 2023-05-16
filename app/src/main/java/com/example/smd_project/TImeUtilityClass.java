package com.example.smd_project;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TImeUtilityClass implements Serializable {

    public static String currentDateToString() {
        Date currentDate = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(currentDate);
    }

    // Function to convert current time to string
    public static String currentTimeToString() {
        Date currentTime = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(currentTime);
    }


}
