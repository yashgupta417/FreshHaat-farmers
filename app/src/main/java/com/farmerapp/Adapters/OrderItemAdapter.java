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

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder>{
    ArrayList<Crop> products;
    Context context;
    private onItemClickListener mlistener;

    public OrderItemAdapter(ArrayList<Crop> products, Context context) {
        this.products = products;
        this.context = context;
    }

    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mlistener=listener;
    }
    public static class OrderItemViewHolder extends RecyclerView.ViewHolder{
        TextView item,quantity,price;
        public OrderItemViewHolder(@NonNull View itemView,final onItemClickListener listener){
            super(itemView);
            item=itemView.findViewById(R.id.item);
            quantity=itemView.findViewById(R.id.quantity);
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
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout,parent,false);
        OrderItemViewHolder myViewHolder=new OrderItemViewHolder(v,mlistener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        Crop product=products.get(position);
        String locale= LanguageActivity.getSavedLocale(context);
        if(locale!=null && locale.equals("hi"))
            holder.item.setText(product.getNameHindi());
        else
            holder.item.setText(product.getName().substring(0,1).toUpperCase()+product.getName().substring(1).toLowerCase());
        holder.quantity.setText(Integer.toString(product.getQuantity())+"x"+product.getBatchSize()+" "+product.getUnit());
        holder.price.setText(context.getResources().getString(R.string.Rs)+Float.toString(product.getQuantity()*product.getOfferPrice()));
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
