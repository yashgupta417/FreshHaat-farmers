package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.farmerapp.Adapters.SearchItemAdapter;
import com.example.farmerapp.Data.Crop;
import com.example.farmerapp.R;
import com.example.farmerapp.ViewModels.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SearchItemAdapter adapter;
    SearchView searchView;
    SearchViewModel viewModel;
    ArrayList<Crop> products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageActivity.loadSavedLocale(this);
        setContentView(R.layout.activity_search);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchView=findViewById(R.id.search);
        recyclerView=findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        showKeyboard();
        activateSearch();
        viewModel= ViewModelProviders.of(this).get(SearchViewModel.class);
        viewModel.getResults().observe(this, new Observer<List<Crop>>() {
            @Override
            public void onChanged(List<Crop> crops) {
                products=(ArrayList<Crop>) crops;
                syncAdapter();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        showKeyboard();
    }

    public void showKeyboard(){
        searchView.requestFocus();
        InputMethodManager methodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        methodManager.showSoftInput(searchView,0);
    }

    public void onBackClick(View view){
        finish();
    }
    public void activateSearch(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.queryProducts(query);
                InputMethodManager methodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                methodManager.hideSoftInputFromWindow(searchView.getWindowToken(),0);
                return  true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.queryProducts(newText);
                return true;
            }
        });
    }
    public void syncAdapter(){
        adapter=new SearchItemAdapter(products,this);
        recyclerView.setAdapter(adapter);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter.setOnItemClickListener(new SearchItemAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(getApplicationContext(),SellActivity.class);
                String type=adapter.products.get(position).getType()+"s";
                if(type.toLowerCase().equals(MainActivity.FRUITS.toLowerCase()))
                    intent.putExtra(MainActivity.PRODUCT_TYPE,MainActivity.FRUITS);
                else
                    intent.putExtra(MainActivity.PRODUCT_TYPE,MainActivity.VEGETABLES);
                startActivity(intent);
            }
        });
    }
}
