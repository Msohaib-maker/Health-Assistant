package com.example.smd_project;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataSaver extends SQLiteOpenHelper {

    private static final int DB_VERSION = 4;
    private static final String DB_NAME = "HealthData.db";

    // daily intake table and its columns
    private static final String DAILY_INTAKE_TABLE = "DailyIntake";
    private static final String ID_COL = "ID";
    private static final String DATE_COL = "DATE";
    private static final String SET_GOAL = "SetGoal";
    private static final String INTAKE_TRACK = "Intake";

    // Reminder table and its columns
    private static final String REMIND_TABLE = "RemindTable";
    private static final String ID1_COL = "ID";
    private static final String TIME_COL = "Time";
    private static final String STATUS_COl = "Status";

    // pill table and its column
    private static final String PILL_TABLE = "PillTable";
    private static final String ID2_COL = "ID";
    private static final String PILL_ID = "PillId";
    private static final String PILL_NAME = "PillName";
    private static final String PILL_DESC = "PillDesc";
    private static final String PILL_REMINDERS = "PillReminds";
    private static final String PILL_REMIND_IDS = "PillRemindsIds";
    private static final String PILL_DOSE = "PillDose";
    private static final String PILL_REMINDERS_NUMBERS = "PillRemindNum";

    // Exercise table
    private static final String EXERCISE_TABLE = "ExerciseTable";
    private static final String ID3_COL = "Id";
    private static final String EXERCISE_DATE_COL = "date";
    private static final String CALORIES_COL = "Calories";
    private static final String STEPS_COl = "Steps";

    private Context context1;


    public DataSaver(@Nullable Context context) {

        super(context, DB_NAME, null, DB_VERSION);
        context1 = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "Create table " + DAILY_INTAKE_TABLE +
                " ( " + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DATE_COL + " TEXT, " +
                SET_GOAL + " TEXT, " +
                INTAKE_TRACK + " TEXT)";

        String query1 = "Create table " + REMIND_TABLE +
                " ( " + ID1_COL + " TEXT, " +
                TIME_COL + " TEXT, " +
                STATUS_COl + " TEXT)";

        String query2 = "Create table " + PILL_TABLE +
                " ( " + ID2_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PILL_ID + " TEXT, " +
                PILL_NAME + " TEXT, " +
                PILL_DESC + " TEXT, " +
                PILL_REMINDERS + " TEXT, " +
                PILL_REMIND_IDS + " TEXT, " +
                PILL_DOSE + " TEXT)";

        String query3 = "Create table " + EXERCISE_TABLE +
                " ( " + ID3_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EXERCISE_DATE_COL + " TEXT, " +
                CALORIES_COL + " TEXT, "
                + STEPS_COl + " TEXT)";


        sqLiteDatabase.execSQL(query2);
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.execSQL(query1);
        sqLiteDatabase.execSQL(query3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i<1)
        {
            onCreate(sqLiteDatabase);
        }
        if(i<2)
        {
            String query1 = "Create table " + REMIND_TABLE +
                    " ( " + ID1_COL + " TEXT, " +
                    TIME_COL + " TEXT, " +
                    STATUS_COl + " TEXT)";
            sqLiteDatabase.execSQL(query1);
        }
        if(i<3)
        {
            String query2 = "Create table " + PILL_TABLE +
                    " ( " + ID2_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + PILL_ID + " TEXT, " +
                    PILL_NAME + " TEXT, " +
                    PILL_DESC + " TEXT, " +
                    PILL_REMINDERS + " TEXT, " +
                    PILL_REMIND_IDS + " TEXT, " +
                    PILL_DOSE + " TEXT)";
             sqLiteDatabase.execSQL(query2);
        }
        if(i<4)
        {
            String query3 = "Create table " + EXERCISE_TABLE +
                    " ( " + ID3_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    EXERCISE_DATE_COL + " TEXT, " +
                    CALORIES_COL + " TEXT, "
                    + STEPS_COl + " TEXT)";

            sqLiteDatabase.execSQL(query3);
        }



    }

    public void Insert(String Date)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE_COL,Date);

        db.insert(DAILY_INTAKE_TABLE,null,contentValues);

    }

    public void InsertIntoExerciseTable(String Date)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE_COL,Date);
        contentValues.put(CALORIES_COL,"0");
        contentValues.put(STEPS_COl,"0");

        db.insert(EXERCISE_TABLE,null,contentValues);

    }

    public void Insert_Remind(Remind remind)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ID1_COL,String.valueOf(remind.getRemindId()));
        contentValues.put(TIME_COL,remind.getAlarmTime());
        contentValues.put(STATUS_COl,String.valueOf(remind.getSwitch_state()));

        db.insert(REMIND_TABLE,null,contentValues);

    }

    public boolean CheckDataInRemindIfExists()
    {
        try
        {
            String query = "Select * from " + REMIND_TABLE;
            SQLiteDatabase db = getReadableDatabase();
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query,null);
            return cursor.moveToFirst();
        }
        catch (Exception e)
        {
            return false;
        }


    }

    public boolean CheckDataInExerciseTableIfExists(String date)
    {
        try
        {
            String query = "Select * from " + EXERCISE_TABLE + " where " + EXERCISE_DATE_COL + " = '" + date + "'";
            SQLiteDatabase db = getReadableDatabase();
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query,null);
            return cursor.moveToFirst();
        }
        catch (Exception e)
        {
            return false;
        }


    }

    public UtilityClass ReadInfoFromExerciseTable(String date)
    {
        try
        {
            String query = "Select * from " + EXERCISE_TABLE + " where " + EXERCISE_DATE_COL + " = '" + date + "'";
            SQLiteDatabase db = getReadableDatabase();
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query,null);
            if(cursor.moveToFirst())
            {
                //Log.d("exercise", "ReadInfoFromExerciseTable: ");
                int index = cursor.getColumnIndex(CALORIES_COL);
                String calorie = cursor.getString(index);
                index = cursor.getColumnIndex(STEPS_COl);
                String steps = cursor.getString(index);

                float cal = Float.parseFloat(calorie);
                int Steps = Integer.parseInt(steps);

                return new UtilityClass(cal,Steps);
            }
            else
            {
                return null;
            }

        }
        catch (SQLException e)
        {
            return null;
        }
    }

    public void UpdateCalorieInExerciseTable(String date,String calorie)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CALORIES_COL,calorie);

        db.update(EXERCISE_TABLE,contentValues,EXERCISE_DATE_COL + " =?",new String[]{date});
        db.close();
    }

    public void UpdateStepsInExerciseTable(String date,String steps)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(STEPS_COl,steps);

        db.update(EXERCISE_TABLE,contentValues,EXERCISE_DATE_COL + " =?",new String[]{date});
        db.close();
    }


    public int ReadLatestRemindId()
    {
        try
        {
            String query = "Select * from " + REMIND_TABLE;
            SQLiteDatabase db = getReadableDatabase();
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query,null);
            String id = "";
            if(cursor.moveToFirst())
            {
                do
                {
                    int index = cursor.getColumnIndex(ID1_COL);
                    id = cursor.getString(index);
                }
                while (cursor.moveToNext());

                return Integer.parseInt(id);
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public ArrayList<Remind> ReadLatestRemindData()
    {
        try
        {
            String query = "Select * from " + REMIND_TABLE;
            SQLiteDatabase db = getReadableDatabase();
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query,null);
            ArrayList<Remind> remindArrayList = new ArrayList<>();
            if(cursor.moveToFirst())
            {
                do
                {
                    int index = cursor.getColumnIndex(ID1_COL);
                    String id = cursor.getString(index);
                    index = cursor.getColumnIndex(TIME_COL);
                    String time = cursor.getString(index);
                    index = cursor.getColumnIndex(STATUS_COl);
                    String stat = cursor.getString(index);

                    Remind r = new Remind(Integer.parseInt(id),time,Integer.parseInt(stat));
                    remindArrayList.add(r);
                }
                while (cursor.moveToNext());

                return remindArrayList;

            }
            else
            {
                return new ArrayList<>();
            }
        }
        catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    public void UpdateGoal(String date,String goal)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SET_GOAL,goal);

        db.update(DAILY_INTAKE_TABLE,contentValues,DATE_COL + " =?",new String[]{date});
        db.close();
    }

    public void UpdateIntake(String date,String intake)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(INTAKE_TRACK,intake);

        db.update(DAILY_INTAKE_TABLE,contentValues,DATE_COL + " =?",new String[]{date});
        db.close();
    }

    public boolean checkIfDateExists(String date)
    {
        try
        {
            String query = "Select * from " + DAILY_INTAKE_TABLE + " where " + DATE_COL + " = '" + date + "'";
            SQLiteDatabase db = getReadableDatabase();
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query,null);
            return cursor.moveToFirst();
        }
        catch (Exception e)
        {
            return false;
        }


    }

    public BasicIntakeClass Load_Intake_Data(String date)
    {
        try
        {
            SQLiteDatabase db = getReadableDatabase();
            String query = "Select * from " + DAILY_INTAKE_TABLE + " where " + DATE_COL + " = '" + date + "'";
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query,null);

            if(cursor.moveToFirst())
            {
                int index = cursor.getColumnIndex(SET_GOAL);
                String goal = cursor.getString(index);
                index = cursor.getColumnIndex(INTAKE_TRACK);
                String intake = cursor.getString(index);

                int Intake = Integer.parseInt(intake);
                BasicIntakeClass basicIntakeClass = new BasicIntakeClass(date,Intake,goal);

                return basicIntakeClass;
            }
            else
            {
                return null;
            }


        }
        catch (Exception e)
        {
            return null;
        }



    }


    public void InsertIntoPillTable(Pill pill)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String pillId = String.valueOf(pill.getPill_Id());
        String pillName = pill.getPill_Name();
        String pillDesc = pill.getDescription();
        // use utility function to convert remind array and remind Ids , each, into one string
        String PillTimes = ConvertRemindTimesIntoStr(pill.getRemindTimes());
        String PillTimesIds = ConvertRemindIdsToStr(pill.getReminderTimesId());
        String PillDose = String.valueOf(pill.getReminderNumber());

        if(Integer.parseInt(PillDose) == 0)
        {
            PillTimes = "";
            PillTimesIds = "";
        }


        contentValues.put(PILL_ID,pillId);
        contentValues.put(PILL_NAME,pillName);
        contentValues.put(PILL_DESC,pillDesc);
        contentValues.put(PILL_REMINDERS,PillTimes);
        contentValues.put(PILL_REMIND_IDS,PillTimesIds);
        contentValues.put(PILL_DOSE,PillDose);

        db.insert(PILL_TABLE,null,contentValues);


    }


    private String ConvertRemindTimesIntoStr(ArrayList<String> times)
    {
        String delimiter = ",";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times.size(); i++) {
            sb.append(times.get(i));
            if (i < times.size() - 1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    private String ConvertRemindIdsToStr(int[] Ids)
    {
        String delimiter = ",";
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<Ids.length;i++)
        {
            sb.append(String.valueOf(Ids[i]));
            if (i < Ids.length - 1) {
                sb.append(delimiter);
            }
        }

        return sb.toString();
    }

    public int ReadLatestPillId()
    {
        try
        {
            String query = "Select * from " + PILL_TABLE;
            SQLiteDatabase db = getReadableDatabase();
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query,null);

            if(cursor.moveToFirst())
            {
                String pill_id;
                do
                {
                    int index = cursor.getColumnIndex(PILL_ID);
                    pill_id = cursor.getString(index);
                }
                while (cursor.moveToNext());

                return Integer.parseInt(pill_id);
            }
            else
            {
                return 0;
            }


        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public int ReadLatestPillReminderId()
    {
        try
        {
            String query = "Select * from " + PILL_TABLE;
            SQLiteDatabase db = getReadableDatabase();
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query,null);

            if(cursor.moveToFirst())
            {
                String pill_reminder_id = "";
                String readAll = "";
                do
                {
                    int index = cursor.getColumnIndex(PILL_REMIND_IDS);
                    readAll = cursor.getString(index);

                    if(readAll != null)
                    {
                        pill_reminder_id = readAll;
                    }

                }
                while (cursor.moveToNext());

                if(pill_reminder_id.equals(""))
                {
                    return  0;
                }
                else
                {
                    return convertIdsIntoComps(pill_reminder_id);
                }
            }
            else
            {
                return 0;
            }


        }
        catch (Exception e)
        {
            return 0;
        }
    }

    private int convertIdsIntoComps(String idsOfPill)
    {

        // Split the string into individual components using the delimiter ","
        String[] components = idsOfPill.split(",");

        // Convert the last component to an integer using the parseInt() method
        return Integer.parseInt(components[components.length - 1]);
    }


    public ArrayList<Pill> ReadPillTableData()
    {
        try
        {
            ArrayList<Pill> pillArrayList = new ArrayList<>();
            String query = "Select * from " + PILL_TABLE;
            SQLiteDatabase db = getReadableDatabase();
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query,null);
            if(cursor.moveToFirst())
            {
                do
                {
                    // code here
                    int index = cursor.getColumnIndex(PILL_NAME);
                    String pill_name = cursor.getString(index);
                    index = cursor.getColumnIndex(PILL_DESC);
                    String pill_desc = cursor.getString(index);
                    index = cursor.getColumnIndex(PILL_ID);
                    String pill_id = cursor.getString(index);
                    index = cursor.getColumnIndex(PILL_REMINDERS);
                    String pill_reminders = cursor.getString(index);
                    index = cursor.getColumnIndex(PILL_REMIND_IDS);
                    String pill_reminder_ids = cursor.getString(index);
                    index = cursor.getColumnIndex(PILL_DOSE);
                    String pill_dose = cursor.getString(index);




                    Pill NewPill = new Pill();
                    NewPill.setPill_Id(Integer.parseInt(pill_id));
                    NewPill.setPill_Name(pill_name);
                    NewPill.setDescription(pill_desc);

                    if(Integer.parseInt(pill_dose) == 0)
                    {
                        NewPill.setReminderTimes(new String[0]);
                        NewPill.setReminderTimesId(new int[0]);
                    }
                    else
                    {
                        NewPill.setReminderTimes(convertRemindTimesIntoStringArray(pill_reminders));
                        NewPill.setReminderTimesId(convertRemindTimesIdsIntoIntArray(pill_reminder_ids));
                    }
                    NewPill.setReminderNumber(Integer.parseInt(pill_dose));

                    pillArrayList.add(NewPill);


                    // needs to use some utility functions to convert reminder times and ids into string and int arrays


                }
                while (cursor.moveToNext());
            }

            return pillArrayList;
        }
        catch (Exception e)
        {
            return new ArrayList<>();
        }
    }


    public boolean CheckPillTableDataIfExists()
    {
        try
        {
            String query = "Select * from " + PILL_TABLE;
            SQLiteDatabase db = getReadableDatabase();
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query,null);
            return cursor.moveToFirst();
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public String[] convertRemindTimesIntoStringArray(String times)
    {
        return times.split(",");
    }

    public int[] convertRemindTimesIdsIntoIntArray(String times_ids)
    {
        String[] components = times_ids.split(",");
        int length = components.length;
        int[] ids = new int[length];
        for(int i=0;i<length;i++)
        {
            ids[i] = Integer.parseInt(components[i]);
        }

        return ids;
    }

    public String ReadAllStepsFromExerciseTable()
    {
        try
        {
            String query = "Select * from " + EXERCISE_TABLE;
            SQLiteDatabase db = getReadableDatabase();
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query,null);

            if(cursor.moveToFirst())
            {
                int IncSteps = 0;
                do
                {
                    int index = cursor.getColumnIndex(STEPS_COl);
                    String steps = cursor.getString(index);

                    if(steps != null)
                    {
                        IncSteps += Integer.parseInt(steps);
                    }
                }
                while (cursor.moveToNext());

                return String.valueOf(IncSteps);
            }
            else
            {
                return "0";
            }

        }
        catch (Exception e)
        {
            return "0";
        }
    }


    public ArrayList<BasicIntakeClass> LoadIntakeAllData()
    {

        try
        {
            ArrayList<BasicIntakeClass> basicIntakeClassArrayList = new ArrayList<>();
            String query = "Select * from " + DAILY_INTAKE_TABLE;
            SQLiteDatabase db = getReadableDatabase();
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query,null);

            if(cursor.moveToFirst())
            {
                do
                {
                    int index = cursor.getColumnIndex(DATE_COL);
                    String date = cursor.getString(index);
                    index = cursor.getColumnIndex(SET_GOAL);
                    String goal = cursor.getString(index);
                    index = cursor.getColumnIndex(INTAKE_TRACK);
                    String Intake = cursor.getString(index);

                    BasicIntakeClass basicIntakeClass = new BasicIntakeClass();
                    basicIntakeClass.setDate(date);

                    if(goal == null)
                    {
                        basicIntakeClass.setGoal("0");
                    }
                    else
                    {
                        basicIntakeClass.setGoal(goal);
                    }

                    if(Intake == null)
                    {
                        basicIntakeClass.setIntake(0);
                    }
                    else
                    {
                        basicIntakeClass.setIntake(Integer.parseInt(Intake));
                    }

                    basicIntakeClassArrayList.add(basicIntakeClass);



                }
                while (cursor.moveToNext());
            }


            return basicIntakeClassArrayList;


        }
        catch (Exception e)
        {
            return new ArrayList<>();
        }
    }


    public void DeleteWaterReminder(String id)
    {
        try
        {
            SQLiteDatabase db = getWritableDatabase();
            String whereClause = ID1_COL + "=?";
            String[] args = new String[]{id};

            db.delete(REMIND_TABLE,whereClause,args);
            //db.close();

        }
        catch (Exception e)
        {

        }
    }

    public void DeletePillFromPillTable(int id)
    {
        try
        {
            SQLiteDatabase db = getWritableDatabase();
            String whereClause = PILL_ID + "=?";
            String[] args = new String[]{String.valueOf(id)};

            db.delete(PILL_TABLE,whereClause,args);
            //db.close();

        }
        catch (Exception e)
        {

        }
    }

    public void UpdatePillData(String pillName,String pillDescription,String pillId)
    {
        try
        {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(PILL_NAME,pillName);
            contentValues.put(PILL_DESC,pillDescription);

            db.update(PILL_TABLE,contentValues,PILL_ID + "=?",new String[]{pillId});
            db.close();
        }
        catch (Exception e)
        {

        }
    }

    public ArrayList<UtilityClass> ReadExerciseALLData()
    {
        try {
            ArrayList<UtilityClass> utilityClasses = new ArrayList<>();
            String query = "Select * from " + EXERCISE_TABLE;
            SQLiteDatabase db = getReadableDatabase();
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query,null);

            if(cursor.moveToFirst())
            {

                do
                {
                    int index = cursor.getColumnIndex(STEPS_COl);
                    String steps = cursor.getString(index);
                    index = cursor.getColumnIndex(CALORIES_COL);
                    String cals = cursor.getString(index);

                    UtilityClass utilityClass = new UtilityClass();
                    if(steps != null)
                    {
                        utilityClass.setSteps(Integer.parseInt(steps));
                    }
                    else
                    {
                        utilityClass.setSteps(0);
                    }
                    if(cals != null)
                    {
                        utilityClass.setCalories(Float.parseFloat(cals));
                    }
                    else
                    {
                        utilityClass.setCalories(0f);
                    }

                    utilityClasses.add(utilityClass);


                }
                while (cursor.moveToNext());

            }
            return utilityClasses;
        }
        catch (Exception e)
        {
            return new ArrayList<>();
        }
    }


    public float RetrieveAllCalories()
    {
        try
        {
            String query = "Select * from " + EXERCISE_TABLE;
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(query,null);

            float Calories = 0;
            if(cursor.moveToFirst())
            {
                do
                {
                    int index = cursor.getColumnIndex(CALORIES_COL);
                    String calories = cursor.getString(index);

                    if(calories != null)
                    {
                        Calories += Float.parseFloat(calories);
                    }
                }
                while (cursor.moveToNext());

                return Calories;
            }
            else
            {
                return 0f;
            }

        }
        catch (Exception e)
        {
            return 0f;
        }
    }






    

}
