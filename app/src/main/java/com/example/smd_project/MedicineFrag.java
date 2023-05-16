package com.example.smd_project;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.search.SearchBar;

import java.util.ArrayList;


public class MedicineFrag extends Fragment {

    BottomSheetDialog bottomSheetDialog;
    CardView Pill_Btn;
    PillAdapter pillAdapter;
    ArrayList<Pill> pillArrayList;

    private static int PillAlarmId = 0;
    private static int PillId = 0;

    DataSaver dataSaver;
    TextView dee;

    EditText searchBar;

    public MedicineFrag() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View MedView = inflater.inflate(R.layout.fragment_medicine, container, false);
        Pill_Btn = MedView.findViewById(R.id.Add_PillId);
        RecyclerView MedRecycleView = MedView.findViewById(R.id.pills_recycleViewId);
        searchBar = MedView.findViewById(R.id.searchOfMed);

        dataSaver = new DataSaver(getContext()); // data saver initialization

        pillArrayList = dataSaver.ReadPillTableData();



        if(dataSaver.CheckPillTableDataIfExists())
        {
            PillId = dataSaver.ReadLatestPillId();
            PillAlarmId = dataSaver.ReadLatestPillReminderId();
            PillId++;
            PillAlarmId++;
        }






        pillAdapter = new PillAdapter(getContext(),pillArrayList);

        MedRecycleView.setLayoutManager(new GridLayoutManager(getContext(),2));
        MedRecycleView.setAdapter(pillAdapter);

        bottomSheetDialog = new BottomSheetDialog(getContext());
        CreateDialog();

        Pill_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pill_Btn.setVisibility(View.GONE);
                bottomSheetDialog.show();
            }
        });


        bottomSheetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ArrayList<Pill> pillsData = new ArrayList<>();
                for(Pill p : pillArrayList)
                {
                    if(p.getPill_Name().contains(charSequence))
                    {
                        pillsData.add(p);
                    }
                }

                pillAdapter = new PillAdapter(getContext(),pillsData);

                MedRecycleView.setLayoutManager(new GridLayoutManager(getContext(),2));
                MedRecycleView.setAdapter(pillAdapter);


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };



        searchBar.addTextChangedListener(textWatcher);


        return MedView;
    }

    int NumberTime = 0;
    ReminderTimeAdapter reminderTimeAdapter;

    private void CreateDialog()
    {
        View bottomView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet,null,false);

        RecyclerView PillRemindRecycleView = bottomView.findViewById(R.id.reminderTime_recycleView_Id);

        Button Enter_pill_data = bottomView.findViewById(R.id.add_PillBtnBottomSheet);
        EditText GetPillName = bottomView.findViewById(R.id.InputPillNameId);
        EditText GetPillDescription = bottomView.findViewById(R.id.InputPillDescriptionId);

        reminderTimeAdapter = new ReminderTimeAdapter(getContext(),0);
        PillRemindRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        PillRemindRecycleView.setAdapter(reminderTimeAdapter);

        EditText RemindNumber = bottomView.findViewById(R.id.EnterRemindTimesId);
        RemindNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called before the text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try
                {
                    NumberTime = Integer.parseInt(s.toString());
                }
                catch (Exception e)
                {
                    NumberTime = 0;
                }

                reminderTimeAdapter = new ReminderTimeAdapter(getContext(),NumberTime);
                PillRemindRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
                PillRemindRecycleView.setAdapter(reminderTimeAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is called after the text is changed
            }
        });


        Enter_pill_data.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {

                Pill_Btn.setVisibility(View.VISIBLE);
                String pill_name = GetPillName.getText().toString();
                String pill_description = GetPillDescription.getText().toString();
                Pill med = new Pill();
                med.setPill_Id(PillId);
                med.setPill_Name(pill_name);
                med.setDescription(pill_description);
                PillId++;


                ArrayList<String> remindTimes = reminderTimeAdapter.getArrayListForTextTimes();
                ArrayList<Long> remindTimesForAlarmManger = reminderTimeAdapter.getArrayListForAlarmManger();

                if(remindTimes.size() > 0)
                {
                    //Toast.makeText(getContext(),remindTimes.get(0),Toast.LENGTH_SHORT).show();
                    // track valid entries
                    int count = 0;
                    for(String RT : remindTimes)
                    {
                        if(!RT.equals("Na"))
                        {
                            count++;
                        }
                    }

                    med.setReminderNumber(count); // assign the value to pill dose recorder

                    // puts entries in ids array
                    int[] the_ids = new int[count];
                    for(int i=0;i<count;i++)
                    {
                        the_ids[i] = PillAlarmId;
                        PillAlarmId++;
                    }


                    // call alarm manger to generate notification
                    int j = 0,k=0;
                    for(String RT : remindTimes)
                    {
                        if(!RT.equals("Na"))
                        {
                            AlarmSetForPills(the_ids[j],remindTimesForAlarmManger.get(k),pill_name,RT);
                            j++;
                        }
                        k++;
                    }



                    med.setReminderTimesAndIds(remindTimes,the_ids);

                }
                else
                {
                    med.setReminderNumber(0);
                }

                dataSaver.InsertIntoPillTable(med);
                pillArrayList.add(med);


                // reset all pill data in bottom sheet
                GetPillName.setText("");
                GetPillDescription.setText("");
                RemindNumber.setText("");

                reminderTimeAdapter = new ReminderTimeAdapter(getContext(),0);
                PillRemindRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
                PillRemindRecycleView.setAdapter(reminderTimeAdapter);
                // ------------


                pillAdapter.notifyDataSetChanged();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(bottomView);


    }


    @SuppressLint("ShortAlarm")
    private void AlarmSetForPills(int pillAlarm_Id, Long AlarmTimings, String pillName,String AlarmTime)
    {
        Intent intent = new Intent(getActivity(), PillAlarmReceiver.class);
        intent.putExtra("AlarmId",String.valueOf(pillAlarm_Id));
        intent.putExtra("NameOfPill",pillName);
        intent.putExtra("SetDate",String.valueOf(AlarmTimings));
        intent.putExtra("TrueDate",AlarmTime);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), pillAlarm_Id, intent, PendingIntent.FLAG_IMMUTABLE);


        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);

        long intervalMillis = 24 * 60 * 60 * 1000L; // repeat after this time

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            //alarmManager.setExact(AlarmManager.RTC_WAKEUP, AlarmTimings + (1000L), pendingIntent);
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,AlarmTimings,pendingIntent);
        }
        else {

            //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,AlarmTimings + (1000L),intervalMillis,pendingIntent);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,AlarmTimings,pendingIntent);
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,AlarmTimings,intervalMillis,pendingIntent);
            }
        },500);


    }



}