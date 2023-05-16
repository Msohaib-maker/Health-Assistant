package com.example.smd_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class HeartFrag extends Fragment {


    TextView textView;
    private SensorManager sensorManager;
    DataSaver dataSaver;
    RecyclerView ArticleRecycleView;
    private ProgressBar progressBar;
    private ArrayList<Article> articles = new ArrayList<>();
    private ArticleAdapter articleAdapter;

    public HeartFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View heartView =  inflater.inflate(R.layout.fragment_heart, container, false);
        TextView caloriesDisplay = heartView.findViewById(R.id.displayCaloriesId);
        progressBar = heartView.findViewById(R.id.ProgressBarId);
        ArticleRecycleView = heartView.findViewById(R.id.ArticlesRecycleViewId);

        progressBar.setVisibility(View.VISIBLE);



        articleAdapter = new ArticleAdapter(getContext(),articles);
        ArticleRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArticleRecycleView.setAdapter(articleAdapter);



        dataSaver = new DataSaver(getContext());
        String CurrentDate = getDate();
        if(dataSaver.CheckDataInExerciseTableIfExists(CurrentDate))
        {
            UtilityClass utilityClass = dataSaver.ReadInfoFromExerciseTable(CurrentDate);
            if(utilityClass != null)
            {
                String calories = String.valueOf(utilityClass.getCalories());
                String displayStr = "Calories burnt today : " + calories + " cal";
                caloriesDisplay.setText(displayStr);
            }
        }


        RelativeLayout StepsActivity = heartView.findViewById(R.id.RecordStepsId);

        StepsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),StepsRecorder.class));
            }
        });

        RelativeLayout PushUp = heartView.findViewById(R.id.PushUpsId);

        PushUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),PushUps.class));
            }
        });

        Content content = new Content();
        content.execute();

        return heartView;
    }

    private String getDate()
    {
        java.util.Date currentDate = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(currentDate);
    }


    private class Content extends AsyncTask<Void,Void,ArrayList<Article>>
    {

        @Override
        protected ArrayList<Article> doInBackground(Void... voids) {
            ArrayList<Article> Newarticles = new ArrayList<>();
            try
            {

                String url = "https://www.everydayhealth.com/fitness/all-articles/";
                Document document = Jsoup.connect(url).get();
                Elements elements = document.select("div.category-index-article__container");
                int size = elements.size();
                for(int i=0;i<size;i++)
                {
                    String imgUrl = elements.select("figure.category-index-article__image")
                            .select("img")
                            .eq(i)
                            .attr("src");

                    String title = elements.select("h2.category-index-article__title")
                            .select("a")
                            .eq(i)
                            .text();

                    Newarticles.add(new Article(imgUrl,title));

                    Log.d("BackgroundMein", title);




                }




            }
            catch (Exception e)
            {
                //Toast.makeText(getContext(), "Nahi hua", Toast.LENGTH_SHORT).show();
            }
            return Newarticles;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void onPostExecute(ArrayList<Article> arrayList) {
            super.onPostExecute(arrayList);
            progressBar.setVisibility(View.GONE);
            articles.addAll(arrayList);
            articleAdapter.notifyDataSetChanged();
        }
    }
}