package com.farmerapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.farmerapp.R;
import com.farmerapp.Data.Banner;

import java.util.ArrayList;

public class BannersAdapter extends RecyclerView.Adapter<BannersAdapter.BannersViewHolder> {
    public ArrayList<Banner> banners;
    private Context context;
    private onItemClickListener mlistener;

    public BannersAdapter(ArrayList<Banner> banners, Context context) {
        this.banners = banners;
        this.context = context;
    }

    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mlistener=listener;
    }
    public static class BannersViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title;
        public BannersViewHolder(@NonNull View itemView,final onItemClickListener listener) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            title=itemView.findViewById(R.id.title);
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
    public BannersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_item_layout,parent,false);
        BannersViewHolder myViewHolder=new BannersViewHolder(v,mlistener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BannersViewHolder holder, int position) {
        Banner banner=banners.get(position);
        if(banner.getImage()!=null){
            Glide.with(context).load(banner.getImage()).into(holder.image);
        }
        holder.title.setText(banner.getTitle());
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
        return banners.size();
    }
}
