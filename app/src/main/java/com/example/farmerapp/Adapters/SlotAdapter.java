package com.example.farmerapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmerapp.R;

import java.util.ArrayList;

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.SlotViewHolder>{
    public ArrayList<String> slotNames,slotTimes;
    public ArrayList<Boolean> selected;
    public Integer selectedIndex;
    Context context;
    private onItemClickListener mlistener;

    public SlotAdapter(ArrayList<String> slotNames, ArrayList<String> slotTimes, Context context,Integer selectedIndex) {
        this.slotNames= slotNames;
        this.slotTimes = slotTimes;
        this.context = context;
        this.selectedIndex=selectedIndex;
        selected=new ArrayList<Boolean>();
        for(int i=0;i<slotNames.size();i++){
            if(i==selectedIndex)
                selected.add(true);
            else
                selected.add(false);
        }
    }
    public void updateSelctedItem(Integer newIndex){
        selected.set(selectedIndex,false);
        selected.set(newIndex,true);
        notifyDataSetChanged();
        //notifyItemChanged(selectedIndex);
        //notifyItemChanged(newIndex);
        selectedIndex=newIndex;
    }
    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mlistener=listener;
    }
    public static class SlotViewHolder extends RecyclerView.ViewHolder{
        TextView slotName,slotTime;
        CardView parent;
        public SlotViewHolder(@NonNull View itemView,final onItemClickListener listener) {
            super(itemView);
            slotName=itemView.findViewById(R.id.slot_name);
            slotTime=itemView.findViewById(R.id.slot_time);
            parent=itemView.findViewById(R.id.parent);
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
    public SlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_item_layout,parent,false);
        SlotViewHolder myViewHolder=new SlotViewHolder(v,mlistener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SlotViewHolder holder, int position) {

        if(selected.get(position)){
            holder.parent.setAlpha(1f);
            Log.i("****",Integer.toString(position)+"true");
        }else{
            holder.parent.setAlpha(0.3f);
            Log.i("****",Integer.toString(position)+"false");
        }
        holder.slotName.setText(slotNames.get(position));
        holder.slotTime.setText(slotTimes.get(position));
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
        return slotNames.size();
    }
}
