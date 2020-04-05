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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);

    }
    private View initView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view= LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);
        }
        String place=getItem(position);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(place);
        return view;
    }

}
