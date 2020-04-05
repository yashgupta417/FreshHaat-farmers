package com.example.farmerapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.farmerapp.R;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<String> {
    public SpinnerAdapter(@NonNull Context context, ArrayList<String> places) {
        super(context,0,places);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view= LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner, parent, false);
        }
        String place=getItem(position);
        TextView textView = (TextView) view.findViewById(R.id.selcted_place);
        if(textView!=null)
            textView.setText(place);
        return view;
    }


    @Override
    public View getDropDownView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view= LayoutInflater.from(getContext()).inflate(R.layout.spinner_row, parent, false);
        }
        String place=getItem(position);
        TextView textView = (TextView) view.findViewById(R.id.place_textview);
        if(textView!=null)
            textView.setText(place);
        return view;

    }


}
