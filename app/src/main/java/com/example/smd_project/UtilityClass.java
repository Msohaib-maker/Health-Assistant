package com.example.smd_project;

import java.io.Serializable;

public class UtilityClass implements Serializable {

    private float Calories;
    private int Steps;

    public UtilityClass() {

    }

    public UtilityClass(float cal,int steps) {
        this.Calories = cal;
        this.Steps = steps;
    }



    public float getCalories() {
        return Calories;
    }

    public void setCalories(float calories) {
        Calories = calories;
    }

    public int getSteps() {
        return Steps;
    }

    public void setSteps(int steps) {
        Steps = steps;
    }
}
