package com.example.smd_project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Pill implements Serializable {

    private int pill_Id;
    private String pill_Name;
    private String description;
    private String[] reminderTimes;
    private int[] reminderTimesId;
    private int reminderNumber;

    public Pill()
    {
        // Empty
        reminderNumber = 0;
    }

    public Pill(int pill_Id, String pill_Name, String description) {
        this.pill_Id = pill_Id;
        this.pill_Name = pill_Name;
        this.description = description;
    }

    public void setReminderTimes(String[] reminderTimes) {
        this.reminderTimes = reminderTimes;
    }

    public void setReminderTimesId(int[] reminderTimesId) {
        this.reminderTimesId = reminderTimesId;
    }

    public int getReminderNumber() {
        return reminderNumber;
    }

    public void setReminderNumber(int reminderNumber) {
        this.reminderNumber = reminderNumber;
    }

    public void setReminderTimesAndIds(ArrayList<String> the_times, int[] Ids)
    {
        int count = 0;
        for(String RT : the_times)
        {
            if(!RT.equals("Na"))
            {
                count++;
            }
        }

        reminderTimes = new String[count];
        reminderTimesId = new int[count];
        int i=0;
        for(String RT : the_times)
        {
            if(!RT.equals("Na"))
            {
                reminderTimes[i] = RT;
                i++;
            }

        }

        i=0;
        for(String RT : the_times)
        {
            if(!RT.equals("Na"))
            {
                reminderTimesId[i] = Ids[i];
                i++;
            }

        }



    }


    public ArrayList<String> getRemindTimes()
    {
        ArrayList<String> times = new ArrayList<>();

        times.addAll(Arrays.asList(reminderTimes));

        return times;
    }

    public int[] getReminderTimesId()
    {
        return reminderTimesId;
    }


    public int getPill_Id() {
        return pill_Id;
    }

    public void setPill_Id(int pill_Id) {
        this.pill_Id = pill_Id;
    }

    public String getPill_Name() {
        return pill_Name;
    }

    public void setPill_Name(String pill_Name) {
        this.pill_Name = pill_Name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
