package com.example.farmerapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.farmerapp.R;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<String> {
    ArrayList<String> places;

    public SpinnerAdapter(@NonNull Context context, ArrayList<String> places) {
        super(context,0);
        this.places = places;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder")
                // idk why is this happening
        View view =  View.inflate(getContext(), R.layout.spinner_item, null);
        TextView textView =view.findViewById(R.id.textView);
        textView.setText(places.get(0));
        return textView;
    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public String getItem(int position) {
        return places.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        view =  View.inflate(getContext(), R.layout.spinner_row, null);
        final TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(places.get(position));
        return view;
    }

}
