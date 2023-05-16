package com.example.smd_project;

import java.io.Serializable;

public class Remind implements Serializable {

    private int RemindId;
    private String alarmTime;
    private int switch_state;

    public Remind(int remindId, String alarmTime, int switch_state) {
        RemindId = remindId;
        this.alarmTime = alarmTime;
        this.switch_state = switch_state;
    }

    public int getRemindId() {
        return RemindId;
    }

    public void setRemindId(int remindId) {
        RemindId = remindId;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public int getSwitch_state() {
        return switch_state;
    }

    public void setSwitch_state(int switch_state) {
        this.switch_state = switch_state;
    }
}
