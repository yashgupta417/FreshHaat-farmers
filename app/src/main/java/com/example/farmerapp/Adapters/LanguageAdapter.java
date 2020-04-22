package com.example.farmerapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmerapp.R;
import com.example.farmerapp.ViewModels.LoginViewModel;

import java.util.ArrayList;
import java.util.Arrays;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {
    Context context;
    public ArrayList<Boolean> selected;
    public ArrayList<String> languages;
    public ArrayList<String> languageCodes;
    public Integer selectedIndex=-1;
    private onItemClickListener mlistener;
    public LanguageAdapter(Context context,String langCode) {
        this.context = context;
        languages=new ArrayList<String>(Arrays.asList(new String[]{"English","हिन्दी"}));
        languageCodes=new ArrayList<String>(Arrays.asList(new String[]{"en","hi"}));
        selected=new ArrayList<Boolean>();
        for(int i=0;i<languages.size();i++){
            if(langCode!=null && langCode.equals(languageCodes.get(i))) {
                selected.add(true);
                selectedIndex=i;
            }
            else
                selected.add(false);
        }
    }
    public void updateSelectedLang(Integer newIndex){
        if(selectedIndex!=-1){
            selected.set(selectedIndex,false);
            notifyItemChanged(selectedIndex);
        }
        selected.set(newIndex,true);
        notifyItemChanged(newIndex);
        selectedIndex=newIndex;
    }
    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mlistener=listener;
    }

    public static class LanguageViewHolder extends RecyclerView.ViewHolder{
        TextView lang;
        RelativeLayout parent;
        public LanguageViewHolder(@NonNull View itemView,final onItemClickListener listener) {
            super(itemView);
            lang=itemView.findViewById(R.id.lang);
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
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.language_item,parent,false);
        LanguageViewHolder myViewHolder=new LanguageViewHolder(v,mlistener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
        holder.lang.setText(languages.get(position));
        if(selected.get(position)){
            holder.lang.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.lang.setTypeface(null,Typeface.BOLD);
        }else{
            holder.lang.setTextColor(context.getResources().getColor(R.color.black));
            holder.lang.setTypeface(null,Typeface.NORMAL);
        }
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
        return languages.size();
    }
}
