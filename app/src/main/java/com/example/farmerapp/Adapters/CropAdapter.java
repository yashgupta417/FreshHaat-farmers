package com.example.farmerapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.farmerapp.R;
import com.example.farmerapp.Retrofit.Crop;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CropAdapter extends RecyclerView.Adapter<CropAdapter.CropViewHolder> {
    ArrayList<Crop> crops;
    Context context;
    private onItemClickListener mlistener;

    public CropAdapter(ArrayList<Crop> crops, Context context) {
        this.crops = crops;
        this.context = context;
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
        public CropViewHolder(@NonNull View itemView,final onItemClickListener listener) {
            super(itemView);
            cropImage=itemView.findViewById(R.id.crop_image);
            cropName=itemView.findViewById(R.id.crop_name);
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
        Glide.with(context).load(crop.getImage()).into(holder.cropImage);
        holder.cropName.setText(crop.getName());
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
