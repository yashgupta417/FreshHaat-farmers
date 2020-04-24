package com.farmerapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmerapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.SlotViewHolder>{
    public ArrayList<String> slotNames,slotTimes;
    public ArrayList<Integer> selected;
    public Integer selectedIndex,startingIndex;//Our final slot number will be starting Index + selectedIndex + 1
    Context context;
    private onItemClickListener mlistener;
    Calendar c1;
    public SlotAdapter(Context context,Calendar c1) {
        this.context = context;
        this.c1=c1;
        this.slotNames=new ArrayList<String>(Arrays.asList(context.getResources().getStringArray(R.array.slot_names)));
        this.slotTimes =new ArrayList<String>(Arrays.asList(context.getResources().getStringArray(R.array.slot_times)));
        selected=new ArrayList<Integer>();
        initialize();
    }
    public Integer getSelectedSlotNumber(){
        return startingIndex+selectedIndex+1;
    }
    public void initialize(){
        Calendar c2=Calendar.getInstance();
        boolean indexSelected=false;
        selected.clear();
        for(int i=0;i<slotNames.size();i++){
            int slotNumber=i+1;
            c1.set(Calendar.HOUR_OF_DAY,6+(slotNumber-1)*3+2);
            c1.set(Calendar.MINUTE,59);
            c1.set(Calendar.SECOND,59);
            long diff=c1.getTimeInMillis()-c2.getTimeInMillis();
            if(diff<0){
                //selected.add(-1);
            }
            else{
                if(!indexSelected){
                    selected.add(1);
                    startingIndex=i;
                    indexSelected=true;
                }else{
                    selected.add(0);
                }
            }
        }
        slotNames=new ArrayList<String>(slotNames.subList(startingIndex,slotNames.size()));
        slotTimes=new ArrayList<String>(slotTimes.subList(startingIndex,slotTimes.size()));
        selectedIndex=0;
    }
    public void updateSelctedItem(Integer newIndex){
        selected.set(selectedIndex,0);
        selected.set(newIndex,1);
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
        if(selected.get(position).equals(1)){
            holder.parent.setAlpha(1f);
        }else{
            holder.parent.setAlpha(0.3f);
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
