package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.os.Bundle;
import android.util.Log;

import com.example.farmerapp.Adapters.CartItemAdapter;
import com.example.farmerapp.Data.Cart;
import com.example.farmerapp.Data.Crop;
import com.example.farmerapp.R;
import com.example.farmerapp.Utils.LocalCart;
import com.example.farmerapp.ViewModels.CartViewModel;

public class CartActivity extends AppCompatActivity {
    CartViewModel viewModel;
    RecyclerView recyclerView;
    CartItemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView=findViewById(R.id.recyler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModel= ViewModelProviders.of(this).get(CartViewModel.class);
        viewModel.generateBill().observe(this, new Observer<Cart>() {
            @Override
            public void onChanged(Cart cart) {
                activateAdapter(cart);
            }
        });
    }
    public void activateAdapter(Cart cart){
        adapter=new CartItemAdapter(cart.getProducts(),this);
        recyclerView.setAdapter(adapter);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter.setOnItemClickListener(new CartItemAdapter.onItemClickListener() {
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
                    Log.i("****",Integer.toString(crop.getQuantity()));
                }
            }
        });
    }
}
