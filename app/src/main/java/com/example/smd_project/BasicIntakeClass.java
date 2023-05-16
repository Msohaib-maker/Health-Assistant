package com.example.smd_project;

import java.io.Serializable;

public class BasicIntakeClass implements Serializable {

    private String date;
    private int intake;
    private String goal;

    public BasicIntakeClass()
    {

    }

    public BasicIntakeClass(String date, int intake, String goal) {
        this.date = date;
        this.intake = intake;
        this.goal = goal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIntake() {
        return intake;
    }

    public void setIntake(int intake) {
        this.intake = intake;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}
