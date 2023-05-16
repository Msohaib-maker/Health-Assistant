package com.example.smd_project;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.Serializable;
import java.util.ArrayList;

public class PillAdapter extends RecyclerView.Adapter<PillAdapter.PillHolder> {

    private Context context;
    private ArrayList<Pill> pills;
    DataSaver dataSaver;

    public PillAdapter(Context context1,ArrayList<Pill> pillArrayList)
    {
        this.context = context1;
        this.pills = pillArrayList;
        dataSaver = new DataSaver(context);
    }

    @NonNull
    @Override
    public PillHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_pill,null);
        return new PillHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PillHolder holder, int position) {
        holder.pillName.setText(pills.get(position).getPill_Name());
        int Desc_Length = pills.get(position).getDescription().length();
        if(Desc_Length > 50)
        {
            String output =  pills.get(position).getDescription().substring(0,50) + "...";
            holder.pillDescription.setText(output);
        }
        else
        {
            holder.pillDescription.setText(pills.get(position).getDescription());
        }

        ArrayList<String> MyPillRemindTimes = pills.get(position).getRemindTimes();
        StringBuilder formTimeStr = new StringBuilder();
        int j = 0;
        for(String PRT :MyPillRemindTimes)
        {
            if(j != MyPillRemindTimes.size()-1)
            {
                formTimeStr.append(PRT).append(",");
            }
            else
            {
                formTimeStr.append(PRT);
            }

            j++;
        }

        holder.displayRemindTimes.setText(formTimeStr.toString());

    }

    @Override
    public int getItemCount() {
        return pills.size();
    }

    public class PillHolder extends RecyclerView.ViewHolder
    {

        TextView pillName;
        TextView displayRemindTimes;
        TextView pillDescription;
        ImageView more;
        public PillHolder(@NonNull View itemView) {
            super(itemView);
            pillName = itemView.findViewById(R.id.pillNameDisplayId);
            pillDescription = itemView.findViewById(R.id.pillDescriptionId);
            displayRemindTimes = itemView.findViewById(R.id.displayRemindTimesId);
            more = itemView.findViewById(R.id.moreId);

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int pos = getAdapterPosition();
                    ShowPopUp(view,pos);
                }
            });


        }
    }

    private void ShowPopUp(View v,int pos)
    {

        PopupMenu popupMenu = new PopupMenu(context,v);
        popupMenu.getMenuInflater().inflate(R.menu.dots_menu,popupMenu.getMenu());


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint({"NotifyDataSetChanged", "NonConstantResourceId"})
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.MoreDelId:
                        //Toast.makeText(context, "Delete at " + String.valueOf(pos) + " id : " + pills.get(pos).getPill_Id(), Toast.LENGTH_SHORT).show();

                        dataSaver.DeletePillFromPillTable(pills.get(pos).getPill_Id());
                        for(int i=0;i<pills.get(pos).getReminderTimesId().length;i++)
                        {
                            CancelAlarm(pills.get(pos).getReminderTimesId()[i]);
                        }
                        pills.remove(pos);
                        notifyDataSetChanged();
                        break;
                    case R.id.MoreUpdateId:
                        //Toast.makeText(context, "Update", Toast.LENGTH_SHORT).show();
                        Pill the_pill = pills.get(pos);
                        OpenUpdateBottomSheet(the_pill,pos);


                }

                return false;
            }
        });


        popupMenu.show();

    }

    private void CancelAlarm(int id)
    {
        Intent intent = new Intent(context, PillAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_IMMUTABLE);


        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);


    }

    int num = 0;
    ReminderTimeAdapter reminderTimeAdapter;
    RecyclerView PillRemindRecycleView;

    private void OpenUpdateBottomSheet(Pill MyPill,int pos)
    {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);

        // Inflate your bottom sheet layout here
        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_update, null);


        Button Update_pill_data = bottomSheetView.findViewById(R.id.update_PillBtnBottomSheet);
        EditText GetPillName = bottomSheetView.findViewById(R.id.UpdatePillNameId);
        GetPillName.setText(MyPill.getPill_Name());
        EditText GetPillDescription = bottomSheetView.findViewById(R.id.UpdatePillDescriptionId);
        GetPillDescription.setText(MyPill.getDescription());


        Update_pill_data.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show();
                String pill_new_name = GetPillName.getText().toString();
                String pill_new_description = GetPillDescription.getText().toString();

                pills.get(pos).setPill_Name(pill_new_name);
                pills.get(pos).setDescription(pill_new_description);

                DataSaver dataSaver = new DataSaver(context);

                dataSaver.UpdatePillData(pill_new_name,pill_new_description,String.valueOf(MyPill.getPill_Id()));

                notifyDataSetChanged();

                bottomSheetDialog.dismiss();




            }
        });


        // Set up your bottom sheet content here
        bottomSheetDialog.setContentView(bottomSheetView);

        // Show the bottom sheet dialog
        bottomSheetDialog.show();
    }


}
