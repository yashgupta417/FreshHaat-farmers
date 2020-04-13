package com.example.farmerapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmerapp.Data.Date;
import com.example.farmerapp.Data.Order;
import com.example.farmerapp.R;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder>{
    ArrayList<Order> requests;
    Context context;
    public RequestAdapter(ArrayList<Order> requests, Context context) {
        this.requests = requests;
        this.context = context;
    }
    private onItemClickListener mlistener;
    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mlistener=listener;
    }
    public static class RequestViewHolder extends RecyclerView.ViewHolder{
        TextView date,status,requestId,requestType,slot;
        public RequestViewHolder(@NonNull View itemView,final onItemClickListener listener){
            super(itemView);
            date=itemView.findViewById(R.id.date);
            status=itemView.findViewById(R.id.status);
            requestId=itemView.findViewById(R.id.req_id);
            requestType=itemView.findViewById(R.id.request_type);
            slot=itemView.findViewById(R.id.slot);
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
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item_layout,parent,false);
        RequestViewHolder myViewHolder=new RequestViewHolder(v,mlistener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Order request=requests.get(position);
        Date date=request.getPickupDate();
        holder.date.setText(date.getDay()+"-"+date.getMonth()+"-"+date.getYear());//change it
        holder.status.setText(request.getOrderStatus());
        holder.requestId.setText(request.getOrderId());
        holder.requestType.setText(request.getOrderType());
        holder.slot.setText(request.getSlotNumber());//change it
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
        return requests.size();
    }
}
