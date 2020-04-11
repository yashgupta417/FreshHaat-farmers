package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.farmerapp.Adapters.CropAdapter;
import com.example.farmerapp.R;
import com.example.farmerapp.Data.Crop;
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
    ArrayList<Crop> selectedCrops;
    Button next;
    TextView skip;
    ConstraintLayout parent;
    CropAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_crop);
        selectedCrops=new ArrayList<Crop>();
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
        adapter=new CropAdapter((ArrayList<Crop>) crops,getApplication());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CropAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(!recyclerView.isEnabled())
                    return;
                adapter.selected.set(position,!adapter.selected.get(position));
                adapter.notifyItemChanged(position);
                Crop crop=new Crop();
                crop.setProductId(adapter.crops.get(position).get_id());
                if(adapter.selected.get(position)){
                    selectedCrops.add(crop);
                }else{
                    for(int i=0;i<selectedCrops.size();i++){
                        if(selectedCrops.get(i).getProductId().equals(crop.getProductId()))
                            selectedCrops.remove(i);
                    }
                }
                if(selectedCrops.size()>0){
                    updateUI(true,1f,true,true);
                }else{
                    updateUI(false,0.3f,true,true);
                }
            }
        });
    }
    public void nextWork(View view){
        updateUI(false,0.3f,false,false);
        viewModel.postSelectedCrops(selectedCrops).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer==1){
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if(integer==-1){
                    Snackbar snackbar=Snackbar.make(parent,"No Internet Connection",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    updateUI(true,1f,true,true);
                }
            }
        });
    }
    public void skipWork(View view){
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void updateUI(Boolean nextBool,Float nextAlpha,Boolean skipBool,Boolean recyclerViewBool){
        next.setEnabled(nextBool);
        next.setAlpha(nextAlpha);
        skip.setEnabled(skipBool);
        recyclerView.setEnabled(recyclerViewBool);
    }
}
