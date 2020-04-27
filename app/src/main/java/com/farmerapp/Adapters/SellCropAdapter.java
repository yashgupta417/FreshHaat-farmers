package com.farmerapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.farmerapp.Data.Crop;
import com.example.farmerapp.R;
import com.farmerapp.Utils.LocalCart;

import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class SellCropAdapter extends RecyclerView.Adapter<SellCropAdapter.SellCropViewModel> {
    public ArrayList<Crop> crops;
    public Activity activity;
    private onItemClickListener mlistener;
    public String type="type";
    public static String GRID="grid";
    public static String NORMAL="normal";
    public SellCropAdapter(ArrayList<Crop> crops, Activity activity, String type) {
        this.crops = crops;
        this.activity = activity;
        this.type=type;
    }
    public interface onItemClickListener{
        void onItemClick(int position);
        void onIncrementClick(int position);
        void onDecrementClick(int position);
        void onAddToCartClick(int position);
        void onQuantityClick(int position);
    }
    public void incrementCount(int position){
        Crop crop=getItem(position);
        if(crop.getQuantity()==crop.getMaxQuantity()){
            Toast.makeText(activity, activity.getResources().getString(R.string.main_max_limit_reached), Toast.LENGTH_SHORT).show();
            return;
        }
        crop.setQuantity(min(crop.getMaxQuantity(),crop.getQuantity()+crop.getIncrementValue()));
        notifyItemChanged(position);
        LocalCart.update(activity.getApplication(),crop.getId(),Integer.toString(crop.getQuantity()));
    }
    public void decrementCount(int position){
        Crop crop=getItem(position);
        if(crop.getQuantity()>crop.getMinQuantity()) {
            crop.setQuantity(Math.max(crop.getMinQuantity(),crop.getQuantity() -crop.getIncrementValue()));
        }else if(crop.getQuantity()==crop.getMinQuantity()){
                crop.setQuantity(0);
                LocalCart.count--;
        }
        notifyItemChanged(position);
        LocalCart.update(activity.getApplication(), crop.getId(), Integer.toString(crop.getQuantity()));
    }
    public void addToCart(int position){
        Crop crop=getItem(position);
        crop.setQuantity(crop.getMinQuantity());//assuming maxQuantity is greater or equal to than minQuantity
        LocalCart.count++;
        notifyItemChanged(position);
        LocalCart.update(activity.getApplication(), crop.getId(), Integer.toString(crop.getQuantity()));
    }
    public void changeQuantity(int position,int finalQuantity){
        //quantity should be in range of [min,max]
        Crop crop=getItem(position);
        int prevQuantity=crop.getQuantity();
        crop.setQuantity(finalQuantity);
        if(prevQuantity==0 && finalQuantity>=crop.getMinQuantity()) {
            LocalCart.count++;
        }else if(prevQuantity>=crop.getMinQuantity() && finalQuantity==0){
            LocalCart.count--;
        }
        notifyItemChanged(position);
        LocalCart.update(activity.getApplication(), crop.getId(), Integer.toString(crop.getQuantity()));
    }
    public void setOnItemClickListener(onItemClickListener listener){
        mlistener=listener;
    }

    public static class SellCropViewModel extends RecyclerView.ViewHolder{
        ImageView image,plus,minus;
        TextView name,price,unit,count,offerPrice,offer;
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
            offerPrice=itemView.findViewById(R.id.offer_price);
            offer=itemView.findViewById(R.id.offer);
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
            count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onQuantityClick(position);
                        }
                    }
                }
            });
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onQuantityClick(position);
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
            Glide.with(activity).load(crop.getImage()).into(holder.image);
        }

        holder.name.setText(crop.getName().substring(0,1).toUpperCase()+crop.getName().substring(1).toLowerCase());
        holder.price.setText(activity.getResources().getString(R.string.Rs)+crop.getPrice());
        holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.offerPrice.setText(activity.getResources().getString(R.string.Rs)+crop.getOfferPrice());
        holder.unit.setText(crop.getBatchSize()+" "+crop.getUnit());
        if(crop.getOffer()!=null && crop.getOffer()!=0f) {
            holder.offer.setText(crop.getOffer() + "% EXTRA");
        }else{
            holder.offer.setVisibility(View.GONE);
        }
        if(crop.getQuantity()>0){
            holder.count.setText(Integer.toString(crop.getQuantity()));
            holder.count.setVisibility(View.VISIBLE);
            if(crop.getQuantity()==crop.getMaxQuantity())
                holder.plus.setAlpha(0.3f);
            else
                holder.plus.setAlpha(1f);
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
