package com.example.smd_project;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.MyHolder> {

    private Context context;
    private ArrayList<Remind> remindArrayList;

    public ReminderAdapter(Context context1,ArrayList<Remind> reminds)
    {
        this.context = context1;
        this.remindArrayList = reminds;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_reminder,null);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.time.setText(remindArrayList.get(position).getAlarmTime());
        //holder.toggle.setChecked(remindArrayList.get(position).getSwitch_state() == 1);
    }

    @Override
    public int getItemCount() {
        return remindArrayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder
    {
        TextView time;
        SwitchCompat toggle;
        RelativeLayout reminderObj;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.timeId);
            toggle = itemView.findViewById(R.id.switchId);
            reminderObj = itemView.findViewById(R.id.reminderBgId);

            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(!b)
                    {
                        int pos = getAdapterPosition();
                        Remind remind = remindArrayList.get(pos);
                        CancelAlarm(remind.getRemindId());
                    }
                    else
                    {
                        Toast.makeText(context, "Set", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            reminderObj.setOnLongClickListener(new View.OnLongClickListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public boolean onLongClick(View view) {

                    final int pos = getAdapterPosition();
                    Remind remind = remindArrayList.get(pos);



                    ShowPopUp(view,remind.getRemindId(),pos);



                    return true;
                }
            });



        }
    }



    private void CancelAlarm(int  id)
    {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,id, intent, PendingIntent.FLAG_IMMUTABLE);


        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);
        Toast.makeText(context, "Cancelled the alarm", Toast.LENGTH_SHORT).show();
    }

    DataSaver dataSaver;


    private void ShowPopUp(View view,int id,int pos)
    {
        PopupWindow popupWindow = new PopupWindow(context);
        dataSaver = new DataSaver(context);

        // Set the content view of the popup window
        View popupView = LayoutInflater.from(context).inflate(R.layout.pop_up, null);

        Button deleteRemind = popupView.findViewById(R.id.deletereminderBtninPop);

        deleteRemind.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                CancelAlarm(id);
                dataSaver.DeleteWaterReminder(String.valueOf(id));
                remindArrayList.remove(pos);
                notifyDataSetChanged();
                popupWindow.dismiss();
            }
        });

        popupWindow.setContentView(popupView);

        // Set the size and position of the popup window

        popupWindow.setFocusable(true);

        // Show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);

    }


}
