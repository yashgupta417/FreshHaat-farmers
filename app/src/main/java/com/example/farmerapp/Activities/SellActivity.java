package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.farmerapp.Adapters.SellCropAdapter;
import com.example.farmerapp.Data.Crop;
import com.example.farmerapp.R;
import com.example.farmerapp.ViewModels.SellViewModel;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class SellActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SellCropAdapter adapter;
    SellViewModel viewModel;
    public Integer NO_OF_COLUMNS=2;
    TextView title;
    GifImageView load;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        String productType=getIntent().getStringExtra(MainActivity.PRODUCT_TYPE);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title=findViewById(R.id.title);
        title.setText("Sell "+productType);
        load=findViewById(R.id.load);

        recyclerView=findViewById(R.id.recyler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,NO_OF_COLUMNS));
        recyclerView.setHasFixedSize(true);

        viewModel= ViewModelProviders.of(this).get(SellViewModel.class);
        viewModel.getProducts(productType).observe(this, new Observer<List<Crop>>() {
            @Override
            public void onChanged(List<Crop> crops) {
                load.setVisibility(View.GONE);
                adapter=new SellCropAdapter((ArrayList<Crop>) crops,getApplication(),SellCropAdapter.GRID);
                recyclerView.setAdapter(adapter);
            }
        });



    }
}
