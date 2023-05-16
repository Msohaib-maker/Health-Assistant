package com.example.smd_project;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.MyHolder> {

    private ArrayList<Article> articleArrayList;
    private Context context;


    public ArticleAdapter(Context context1,ArrayList<Article> ListOfArticle)
    {
        context = context1;
        articleArrayList = ListOfArticle;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_article,null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.Title.setText(articleArrayList.get(position).getTitle());
        Picasso.get().load(articleArrayList.get(position).getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return articleArrayList.size();
    }


    private static final String Url = "https://www.everydayhealth.com/fitness/all-articles/";

    public class MyHolder extends RecyclerView.ViewHolder
    {

        TextView Title;
        ImageView imageView;
        RelativeLayout article_container;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.titleId);
            imageView = itemView.findViewById(R.id.imageViewUrlId);
            article_container = itemView.findViewById(R.id.ArticleContainerId);


            article_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
                    context.startActivity(intent);
                }
            });


        }
    }
}
