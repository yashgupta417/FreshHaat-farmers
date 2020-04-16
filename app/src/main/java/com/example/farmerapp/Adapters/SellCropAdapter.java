package com.example.farmerapp.Adapters;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.farmerapp.Data.Crop;
import com.example.farmerapp.R;
import com.example.farmerapp.Utils.LocalCart;

import java.util.ArrayList;

public class SellCropAdapter extends RecyclerView.Adapter<SellCropAdapter.SellCropViewModel> {
    public ArrayList<Crop> crops;
    public Context context;
    private onItemClickListener mlistener;
    public String type="type";
    public static String GRID="grid";
    public static String NORMAL="normal";
    public SellCropAdapter(ArrayList<Crop> crops, Context context,String type) {
        this.crops = crops;
        this.context = context;
        this.type=type;
    }

    public interface onItemClickListener{
        void onItemClick(int position);
        void onIncrementClick(int position);
        void onDecrementClick(int position);
        void onAddToCartClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mlistener=listener;
    }

    public static class SellCropViewModel extends RecyclerView.ViewHolder{
        ImageView image,plus,minus;
        TextView name,price,unit,count;
        ConstraintLayout parent;
        RelativeLayout addRL,plusMinusRL;
        public SellCropViewModel(@NonNull View itemView,final onItemClickListener listener){
            super(itemView);
            image=itemView.findViewById(R.id.image);
            name=itemView.findViewById(R.id.name);
            price=itemView.findViewById(R.id.price);
            plus=itemView.findViewById(R.id.plus);
            minus=itemView.findViewById(R.id.minus);
            unit=itemView.findViewById(R.id.unit);
            count=itemView.findViewById(R.id.count);
            parent=itemView.findViewById(R.id.parent);
            addRL=itemView.findViewById(R.id.add_to_cart_parent);
            plusMinusRL=itemView.findViewById(R.id.plus_minus_parent);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onIncrementClick(position);
                        }
                    }
                }
            });
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDecrementClick(position);
                        }
                    }
                }
            });
            addRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onAddToCartClick(position);
                        }
                    }
                }
            });


        }
    }


    @NonNull
    @Override
    public SellCropViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(type.equals(NORMAL)) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sell_crops_item_layout, parent, false);
        }else{
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sell_crops_item_layout_grid, parent, false);
        }
        SellCropViewModel myViewHolder=new SellCropViewModel(v,mlistener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SellCropViewModel holder, int position) {
        Crop crop=crops.get(position);
        if(crop.getImage()!=null){
            Glide.with(context).load(crop.getImage()).into(holder.image);
        }

        holder.name.setText(crop.getName().substring(0,1).toUpperCase()+crop.getName().substring(1).toLowerCase());
        holder.price.setText(context.getResources().getString(R.string.Rs)+" "+Float.toString(crop.getPrice()));
        holder.unit.setText(" per "+crop.getUnit());
        if(crop.getQuantity()>0){
            holder.count.setText(Integer.toString(crop.getQuantity()));
            holder.count.setVisibility(View.VISIBLE);
            holder.minus.setEnabled(true);
            holder.minus.setAlpha(1f);
            holder.addRL.setVisibility(View.GONE);
            holder.plusMinusRL.setVisibility(View.VISIBLE);
            holder.plusMinusRL.setClickable(true);
            holder.addRL.setClickable(false);
        }else{
            holder.count.setVisibility(View.INVISIBLE);
            holder.minus.setEnabled(false);
            holder.minus.setAlpha(0.3f);
            holder.plusMinusRL.setVisibility(View.GONE);
            holder.addRL.setVisibility(View.VISIBLE);
            holder.plusMinusRL.setClickable(false);
            holder.addRL.setClickable(true);
        }
    }
    @Override
    public int getItemCount() {
        return crops.size();
    }

    @Override
    public int getItemViewType(int position) {
        return  position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Crop getItem(int position){
        return  crops.get(position);
    }
}
