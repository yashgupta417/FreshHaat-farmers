package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.farmerapp.Adapters.SellCropAdapter;
import com.example.farmerapp.Data.Crop;
import com.example.farmerapp.R;
import com.example.farmerapp.ViewModels.SellViewModel;

import java.util.ArrayList;
import java.util.List;

public class SellActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SellCropAdapter adapter;
    SellViewModel viewModel;
    public Integer NO_OF_COLUMNS=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        String productType=getIntent().getStringExtra(MainActivity.PRODUCT_TYPE);

        recyclerView=findViewById(R.id.recyler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,NO_OF_COLUMNS));
        recyclerView.setHasFixedSize(true);

        viewModel= ViewModelProviders.of(this).get(SellViewModel.class);
        viewModel.getProducts(productType).observe(this, new Observer<List<Crop>>() {
            @Override
            public void onChanged(List<Crop> crops) {
                adapter=new SellCropAdapter((ArrayList<Crop>) crops,getApplication(),SellCropAdapter.GRID);
                recyclerView.setAdapter(adapter);
            }
        });



    }
}
