package com.example.smd_project;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ReminderTimeAdapter extends RecyclerView.Adapter<ReminderTimeAdapter.RemindHolder> {

    private Context context;
    private ArrayList<String> arrayListForTextTimes;
    private ArrayList<Long> arrayListForAlarmManger;
    int RemindTimes;

    public ReminderTimeAdapter(Context c1,int number)
    {
        context = c1;
        arrayListForAlarmManger = new ArrayList<>();
        arrayListForTextTimes = new ArrayList<>();
        RemindTimes = number;
        if(number > 0)
        {
            for(int i=0;i<number;i++)
            {
                arrayListForTextTimes.add("Na");
                arrayListForAlarmManger.add(System.currentTimeMillis());
            }
        }
    }


    public ArrayList<String> getArrayListForTextTimes() {
        return arrayListForTextTimes;
    }

    public ArrayList<Long> getArrayListForAlarmManger() {
        return arrayListForAlarmManger;
    }

    @NonNull
    @Override
    public RemindHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_reminder_time,null);
        return new RemindHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RemindHolder holder, int position) {
        holder.Display_time.setText(arrayListForTextTimes.get(position));
    }

    @Override
    public int getItemCount() {
        return RemindTimes;
    }

    public class RemindHolder extends RecyclerView.ViewHolder
    {

        TextView Display_time;
        Button SetTime;
        public RemindHolder(@NonNull View itemView) {
            super(itemView);
            Display_time = itemView.findViewById(R.id.TimeOfPillId);
            SetTime = itemView.findViewById(R.id.SetReminderTimeBtnId);

            SetTime.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onClick(View view) {

                    OpenTimeDialog(getAdapterPosition());
                }
            });

        }
    }

    private void OpenTimeDialog(int pos)
    {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a new TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String amPm = (hourOfDay < 12) ? "AM" : "PM";
                int hour = (hourOfDay > 12) ? hourOfDay - 12 : hourOfDay;
                String selectedTime = String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, amPm);
                arrayListForTextTimes.set(pos,selectedTime);
                notifyDataSetChanged();

                Calendar NewCalender = Calendar.getInstance();
                NewCalender.set(Calendar.HOUR_OF_DAY,hourOfDay);
                NewCalender.set(Calendar.MINUTE,minute);
                NewCalender.set(Calendar.SECOND,0);

                long TimeInMillis = NewCalender.getTimeInMillis();
                arrayListForAlarmManger.set(pos,TimeInMillis);
            }
        }, hour, minute, false);

        // Show the TimePickerDialog
        timePickerDialog.show();
    }

}
