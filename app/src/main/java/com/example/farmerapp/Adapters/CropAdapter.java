package com.example.farmerapp.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.farmerapp.R;
import com.example.farmerapp.Data.Crop;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CropAdapter extends RecyclerView.Adapter<CropAdapter.CropViewHolder> {
    public ArrayList<Crop> crops;
    public ArrayList<Boolean> selected;
    Context context;
    private onItemClickListener mlistener;

    public CropAdapter(ArrayList<Crop> crops, Context context) {
        this.crops = crops;
        this.context = context;
        selected=new ArrayList<Boolean>();
        for(int i=0;i<crops.size();i++) {
            selected.add(false);
        }

    }

    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mlistener=listener;
    }

    public static class CropViewHolder extends RecyclerView.ViewHolder{
        CircleImageView cropImage;
        TextView cropName;
        ImageView tick;
        public CropViewHolder(@NonNull View itemView,final onItemClickListener listener) {
            super(itemView);
            cropImage=itemView.findViewById(R.id.crop_image);
            cropName=itemView.findViewById(R.id.crop_name);
            tick=itemView.findViewById(R.id.check);
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
        }
    }

    @NonNull
    @Override
    public CropViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.crop_item_layout,parent,false);
        CropViewHolder myViewHolder=new CropViewHolder(v,mlistener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CropViewHolder holder, int position) {
        Crop crop=crops.get(position);
        if(selected.get(position)){
            holder.cropImage.setBorderColor(context.getResources().getColor(R.color.blueLight));
            holder.cropImage.setBorderWidth(6);
            holder.cropName.setTextColor(context.getResources().getColor(R.color.black));
            holder.cropName.setTypeface(null,Typeface.BOLD);
            holder.tick.setVisibility(View.VISIBLE);
        }else{
            holder.cropImage.setBorderColor(context.getResources().getColor(R.color.grey));
            holder.cropImage.setBorderWidth(2);
            holder.cropName.setTextColor(context.getResources().getColor(R.color.black));
            holder.cropName.setTypeface(null,Typeface.NORMAL);
           holder.tick.setVisibility(View.INVISIBLE);
        }
        Glide.with(context).load(crop.getImage()).into(holder.cropImage);
        holder.cropName.setText(crop.getName().substring(0,1).toUpperCase()+crop.getName().substring(1).toLowerCase());
    }

    @Override
    public int getItemViewType(int position) {
        return  position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return crops.size();
    }

}
