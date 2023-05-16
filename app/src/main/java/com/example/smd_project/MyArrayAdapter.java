package com.example.smd_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyArrayAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> list;

    public MyArrayAdapter(Context context, ArrayList<String> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        String item = list.get(position);

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(item);

        return convertView;
    }
}