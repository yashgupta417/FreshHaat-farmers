package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.farmerapp.Adapters.CropAdapter;
import com.example.farmerapp.R;
import com.example.farmerapp.Retrofit.Crop;
import com.example.farmerapp.Retrofit.Farmer;
import com.example.farmerapp.ViewModels.SelectCropViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class SelectCropActivity extends AppCompatActivity {
    private static Integer NO_OF_COLUMN=3;
    RecyclerView recyclerView;
    SelectCropViewModel viewModel;
    GifImageView load;
    ArrayList<String> selectedCrops;
    Button next;
    TextView skip;
    ConstraintLayout parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_crop);
        selectedCrops=new ArrayList<String>();
        load=findViewById(R.id.load);
        next=findViewById(R.id.next);
        skip=findViewById(R.id.skip);
        parent=findViewById(R.id.parent);
        recyclerView=findViewById(R.id.crop_recylcer_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,NO_OF_COLUMN));
        recyclerView.setHasFixedSize(true);
        viewModel= ViewModelProviders.of(this).get(SelectCropViewModel.class);
        viewModel.getCrops().observe(this, new Observer<List<Crop>>() {
            @Override
            public void onChanged(List<Crop> crops) {
                setAdapter(crops);
                load.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
                skip.setVisibility(View.VISIBLE);
                skip.setEnabled(true);
            }
        });

    }
    public void setAdapter(List<Crop> crops){
        load.setVisibility(View.GONE);
        CropAdapter adapter=new CropAdapter((ArrayList<Crop>) crops,getApplication());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CropAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                adapter.selected.set(position,!adapter.selected.get(position));
                adapter.notifyItemChanged(position);
                if(adapter.selected.get(position)){
                    selectedCrops.add(adapter.crops.get(position).get_id());
                }else{
                    selectedCrops.remove(adapter.crops.get(position).get_id());
                }
                if(selectedCrops.size()>0){
                    next.setEnabled(true);
                    next.setAlpha(1f);
                }else{
                    next.setEnabled(false);
                    next.setAlpha(0.3f);
                }
            }
        });
    }
    public void nextWork(View view){
        viewModel.postSelectedCrops(selectedCrops).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer==1){
                    updateLocally();
                    Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else if(integer==-1){
                    Snackbar snackbar=Snackbar.make(parent,"No Internet Connection",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
    }
    public void skipWork(){
        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
        finish();
    }
    public void updateLocally(){
        SharedPreferences sharedPreferences=getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("crops_selected",true);
    }
}
