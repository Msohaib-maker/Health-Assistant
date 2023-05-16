package com.example.smd_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class Analytics extends AppCompatActivity {

    BarChart barChart;
    DataSaver dataSaver;
    ArrayList<BasicIntakeClass> basicIntakeClassArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        barChart = findViewById(R.id.barChart);
        RelativeLayout backBtn = findViewById(R.id.BackBtn2);
        dataSaver = new DataSaver(this);

        basicIntakeClassArrayList = dataSaver.LoadIntakeAllData();




        CreateBarChart();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Analytics.this,MainActivity.class)
                        .putExtra("CurrentFragment","waterFrag"));
            }
        });

    }

    private void CreateBarChart()
    {


        Description description = new Description();
        description.setText("My Bar Chart");
        barChart.setDescription(description);

// Set the legend text for the chart
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);

// Set the X-axis and Y-axis labels
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setAxisMinimum(0f);
        yAxis.setGranularity(1f);
        yAxis.setLabelCount(5, true);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        barChart.getAxisRight().setEnabled(false); // Disable the right Y-axis

// Set the data for the chart
        ArrayList<BarEntry> barEntries = new ArrayList<>();




        String[] DateLabels;
        int[] Intakes;
        if(basicIntakeClassArrayList.size() <= 7)
        {
            DateLabels = new String[basicIntakeClassArrayList.size()];
            Intakes = new int[basicIntakeClassArrayList.size()];
            int i = 0;
            float xi = 1f;
            for(BasicIntakeClass basicIntakeClass : basicIntakeClassArrayList)
            {
                DateLabels[i] = basicIntakeClass.getDate();
                Intakes[i] = basicIntakeClass.getIntake();

                float yi = (Intakes[i]);
                barEntries.add(new BarEntry(xi,yi));
                xi++;
                i++;
            }

        }
        else
        {
            int LastElement = basicIntakeClassArrayList.size()-1;
            int StartElement = LastElement - 7;

            DateLabels = new String[LastElement - StartElement + 1];
            Intakes = new int[LastElement - StartElement + 1];

            int j=0;
            float xi = 1f;

            for(int i=StartElement;i<=LastElement;i++)
            {
                DateLabels[j] = basicIntakeClassArrayList.get(i).getDate();
                Intakes[j] = basicIntakeClassArrayList.get(i).getIntake();

                float yi = (Intakes[j]);
                barEntries.add(new BarEntry(xi,yi));
                xi++;
                j++;
            }
        }



        BarDataSet barDataSet = new BarDataSet(barEntries,
                "Water Intake");
        barDataSet.setColor(Color.BLUE);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(12f);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.3f);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(DateLabels));
        xAxis.setTextSize(8f);

        barChart.setData(barData);
        barChart.invalidate();
    }
}