package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.farmerapp.Adapters.CropAdapter;
import com.example.farmerapp.R;
import com.example.farmerapp.Retrofit.Crop;
import com.example.farmerapp.ViewModels.SelectCropViewModel;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class SelectCropActivity extends AppCompatActivity {
    private static Integer NO_OF_COLUMN=3;
    RecyclerView recyclerView;
    SelectCropViewModel viewModel;
    GifImageView load;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_crop);
        load=findViewById(R.id.load);
        recyclerView=findViewById(R.id.crop_recylcer_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,NO_OF_COLUMN));
        recyclerView.setHasFixedSize(true);
        viewModel= ViewModelProviders.of(this).get(SelectCropViewModel.class);
        viewModel.getCrops().observe(this, new Observer<List<Crop>>() {
            @Override
            public void onChanged(List<Crop> crops) {
                load.setVisibility(View.GONE);
                CropAdapter adapter=new CropAdapter((ArrayList<Crop>) crops,getApplication());
                recyclerView.setAdapter(adapter);
            }
        });
    }
}
