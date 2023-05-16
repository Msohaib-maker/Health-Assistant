package com.example.smd_project;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class WaterFrag extends Fragment {


    RecyclerView Remind_RecycleView;
    ReminderAdapter reminderAdapter;
    ArrayList<Remind> reminders;
    DataSaver dataSaver;
    TextView IntakesTracker;
    private static int glassesIntake = 0;

    private static int remind_id = 0;

    String MyGoal = "";

    public WaterFrag() {
        // Required empty public constructor
    }

    // this module reminds the user of his water intake

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View waterView = inflater.inflate(R.layout.fragment_water, container, false);
        CardView module1 = waterView.findViewById(R.id.DailyIntakeActivityId);
        CardView module2 = waterView.findViewById(R.id.showAnalyticsId);
        ImageView add_reminder = waterView.findViewById(R.id.add_reminder_Id);
        TextView displayDate = waterView.findViewById(R.id.displayDateId);
        TextView displayRatioOfIntakeToGoal = waterView.findViewById(R.id.glassesDisplayId);
        CardView addBtn,MinusBtn;

        addBtn = waterView.findViewById(R.id.addGlassId2);
        MinusBtn = waterView.findViewById(R.id.minusGlassId2);
        IntakesTracker = waterView.findViewById(R.id.glassesTrackId2);

        Remind_RecycleView = waterView.findViewById(R.id.reminder_recycleView_Id);

        String CurrentDate = getDate();
        displayDate.setText(CurrentDate);

        dataSaver = new DataSaver(getContext());

        BasicIntakeClass basicIntakeClass = dataSaver.Load_Intake_Data(CurrentDate);
        if(!dataSaver.checkIfDateExists(CurrentDate))
        {
            dataSaver.Insert(CurrentDate);
        }





        reminders = new ArrayList<>();

        if(dataSaver.CheckDataInRemindIfExists())
        {
            remind_id = dataSaver.ReadLatestRemindId();
            remind_id++;
            reminders = dataSaver.ReadLatestRemindData();
        }

        if(basicIntakeClass != null)
        {
            IntakesTracker.setText(String.valueOf( basicIntakeClass.getIntake()));
            MyGoal = basicIntakeClass.getGoal();

            glassesIntake = basicIntakeClass.getIntake();

            if(MyGoal != null)
            {
                if(!MyGoal.equals(""))
                {
                    String displayRatioText = String.valueOf(glassesIntake) + " out of " + MyGoal;
                    displayRatioOfIntakeToGoal.setText(displayRatioText);
                }
            }
            else
            {
                String displayRatioText = String.valueOf(glassesIntake) + " out of " + "0";
                displayRatioOfIntakeToGoal.setText(displayRatioText);
            }





        }



        reminderAdapter = new ReminderAdapter(getActivity(),reminders);

        Remind_RecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Remind_RecycleView.setAdapter(reminderAdapter);

        add_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        module2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LaunchModuleTwo();
            }
        });
        

        module1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LaunchModuleOne();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                glassesIntake++;

                if(MyGoal != null)
                {
                    if(!MyGoal.equals(""))
                    {
                        String displayRatioText = String.valueOf(glassesIntake) + " out of " + MyGoal;
                        displayRatioOfIntakeToGoal.setText(displayRatioText);
                    }
                }
                else
                {
                    String displayRatioText = String.valueOf(glassesIntake) + " out of " + "0";
                    displayRatioOfIntakeToGoal.setText(displayRatioText);
                }


                IntakesTracker.setText(String.valueOf(glassesIntake));
                dataSaver.UpdateIntake(CurrentDate,String.valueOf(glassesIntake));
            }
        });

        MinusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                glassesIntake--;

                if(MyGoal != null)
                {
                    if(!MyGoal.equals(""))
                    {
                        String displayRatioText = String.valueOf(glassesIntake) + " out of " + MyGoal;
                        displayRatioOfIntakeToGoal.setText(displayRatioText);
                    }
                }
                else
                {
                    //Toast.makeText(getContext(), "Set goal first", Toast.LENGTH_SHORT).show();
                    String displayRatioText = String.valueOf(glassesIntake) + " out of " + "0";
                    displayRatioOfIntakeToGoal.setText(displayRatioText);
                }

                IntakesTracker.setText(String.valueOf(glassesIntake));
                dataSaver.UpdateIntake(CurrentDate,String.valueOf(glassesIntake));
            }
        });


        return waterView;
    }

    private void LaunchModuleOne()
    {
        Intent i = new Intent(getActivity(),WaterMod1.class);
        startActivity(i);
    }

    private void LaunchModuleTwo()
    {
        Intent i = new Intent(getActivity(),Analytics.class);
        startActivity(i);
    }

    private void showTimePickerDialog() {
        // Get the current time
        final Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        // Create a new TimePickerDialog instance
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Convert the selected time to a string
                String selectedTime = hourOfDay + ":" + minute;
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                calendar.set(Calendar.SECOND,0);

                long time_inMillis = calendar.getTimeInMillis();
                Intent intent = new Intent(getActivity(), AlarmReceiver.class);
                intent.putExtra("reminder_id", remind_id); // Pass the ID of the reminder to the receiver
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), remind_id, intent, PendingIntent.FLAG_IMMUTABLE);


                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time_inMillis, pendingIntent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time_inMillis, pendingIntent);

                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, time_inMillis, pendingIntent);
                    }
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP,time_inMillis, pendingIntent);
                }





                Remind remind = new Remind(remind_id,selectedTime,1);
                dataSaver.Insert_Remind(remind);
                reminders.add(remind);
                reminderAdapter.notifyDataSetChanged();
                remind_id++;

                // Do something with the selected time, such as setting an alarm
            }
        }, hour, minute, false);

        // Show the time picker dialog
        timePickerDialog.show();
    }

    private String getDate()
    {
        Date currentDate = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(currentDate);
    }

}