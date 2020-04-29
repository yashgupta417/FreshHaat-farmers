package com.farmerapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.farmerapp.Activities.LanguageActivity;
import com.farmerapp.Data.Crop;
import com.example.farmerapp.R;

import java.util.ArrayList;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.SearchItemViewHolder>{
    public ArrayList<Crop> products;
    public Context context;
    private onItemClickListener mlistener;
    public SearchItemAdapter(ArrayList<Crop> products, Context context) {
        this.products = products;
        this.context = context;
    }
    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mlistener=listener;
    }
    public static  class SearchItemViewHolder extends RecyclerView.ViewHolder{
        TextView name,price;
        public SearchItemViewHolder(@NonNull View itemView,final onItemClickListener listener) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            price=itemView.findViewById(R.id.price);
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
    public SearchItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_layout,parent,false);
        SearchItemViewHolder myViewHolder=new SearchItemViewHolder(v,mlistener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchItemViewHolder holder, int position) {
        Crop product=products.get(position);
        String locale= LanguageActivity.getSavedLocale(context);
        if(locale!=null && locale.equals("hi"))
            holder.name.setText(product.getNameHindi());
        else
            holder.name.setText(product.getName().substring(0,1).toUpperCase()+product.getName().substring(1).toLowerCase());
        holder.price.setText(context.getResources().getString(R.string.Rs)+" "+Float.toString(product.getOfferPrice())+"/"+product.getBatchSize()+" "+product.getUnit());
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
        return products.size();
    }

}
