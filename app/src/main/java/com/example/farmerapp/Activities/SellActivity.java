package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.farmerapp.Adapters.SellCropAdapter;
import com.example.farmerapp.Data.Crop;
import com.example.farmerapp.Data.Farmer;
import com.example.farmerapp.R;
import com.example.farmerapp.Utils.LocalCart;
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
    ArrayList<Crop> products;
    SearchView searchView;
    String productType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        productType=getIntent().getStringExtra(MainActivity.PRODUCT_TYPE);
        products=new ArrayList<Crop>();

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title=findViewById(R.id.title);
        title.setText("Sell "+productType);
        load=findViewById(R.id.load);
        searchView=findViewById(R.id.search);

        recyclerView=findViewById(R.id.recyler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,NO_OF_COLUMNS));
        recyclerView.setHasFixedSize(true);
        activateSearch();
        viewModel= ViewModelProviders.of(this).get(SellViewModel.class);
        viewModel.getProducts(productType).observe(this, new Observer<List<Crop>>() {
            @Override
            public void onChanged(List<Crop> crops) {
                load.setVisibility(View.GONE);
                products=(ArrayList<Crop>) crops;
                activateAdapter();
            }
        });

    }
    public void onBackClick(View view){
        finish();
    }
    public void activateSearch(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.queryProducts(productType,query);
                InputMethodManager methodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                methodManager.hideSoftInputFromWindow(searchView.getWindowToken(),0);
                return  true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.queryProducts(productType,newText);
                return true;
            }
        });
    }
    public void activateAdapter(){
        products=LocalCart.syncQuantities(products,getApplication());
        adapter=new SellCropAdapter(products,this,SellCropAdapter.GRID);
        recyclerView.setAdapter(adapter);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter.setOnItemClickListener(new SellCropAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onIncrementClick(int position) {
                Crop crop=adapter.getItem(position);
                crop.setQuantity(crop.getQuantity()+1);
                adapter.notifyItemChanged(position);
                LocalCart.update(getApplication(),crop.getId(),Integer.toString(crop.getQuantity()));
            }

            @Override
            public void onDecrementClick(int position) {
                Crop crop=adapter.getItem(position);
                if(crop.getQuantity()>0) {
                    crop.setQuantity(crop.getQuantity() - 1);
                    adapter.notifyItemChanged(position);
                    LocalCart.update(getApplication(), crop.getId(), Integer.toString(crop.getQuantity()));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        activateAdapter();
    }

    public void goToCartActivity(View v){
        Intent intent=new Intent(getApplicationContext(),CartActivity.class);
        startActivity(intent);
    }
}
