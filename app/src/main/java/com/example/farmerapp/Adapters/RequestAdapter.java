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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder>{
    public ArrayList<Order> requests;
    Context context;
    public ArrayList<String> slotTimes;
    public RequestAdapter(ArrayList<Order> requests, Context context) {
        this.requests = requests;
        this.context = context;
        slotTimes=new ArrayList<String>(Arrays.asList(context.getResources().getStringArray(R.array.slot_times)));
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

        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,Integer.parseInt(date.getYear()));
        calendar.set(Calendar.MONTH,Integer.parseInt(date.getMonth()));
        calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(date.getDay()));
        SimpleDateFormat dayformat=new SimpleDateFormat("MMM dd");
        holder.date.setText(dayformat.format(calendar.getTime()));

        holder.status.setText(request.getOrderStatus());
        holder.requestId.setText(request.getOrderId());
        holder.requestType.setText(request.getOrderType().substring(0,1).toUpperCase()+request.getOrderType().substring(1).toLowerCase());

        String slotTime=slotTimes.get(Math.max(Integer.parseInt(request.getSlotNumber())-1,0));
        holder.slot.setText(slotTime);//change it
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
