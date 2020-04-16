package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.farmerapp.Adapters.CartItemAdapter;
import com.example.farmerapp.Data.Cart;
import com.example.farmerapp.Data.Crop;
import com.example.farmerapp.R;
import com.example.farmerapp.Utils.CheckInternet;
import com.example.farmerapp.Utils.LocalCart;
import com.example.farmerapp.ViewModels.CartViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class CartActivity extends AppCompatActivity {
    CartViewModel viewModel;
    RecyclerView recyclerView;
    CartItemAdapter adapter;
    TextView totalAmount1,totalAmount2,bookSlot;
    GifImageView load,loadCenter;
    ConstraintLayout parent;
    RelativeLayout emptyCartRL;
    LinearLayout bodyLL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        totalAmount1=findViewById(R.id.total_amount);
        totalAmount2=findViewById(R.id.total_amount_2);
        load=findViewById(R.id.load);
        loadCenter=findViewById(R.id.load_center);
        bookSlot=findViewById(R.id.book_slot);
        parent=findViewById(R.id.parent);
        emptyCartRL=findViewById(R.id.empty_cart_rl);
        bodyLL=findViewById(R.id.body_parent);

        recyclerView=findViewById(R.id.recyler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        handleVisibility(View.GONE,View.GONE,View.VISIBLE);
        viewModel= ViewModelProviders.of(this).get(CartViewModel.class);
        generateBill();
        viewModel.getBill().observe(this, new Observer<Cart>() {
            @Override
            public void onChanged(Cart cart){
                activateAdapter(cart);
                if(cart.getProducts().size()>0)
                    handleVisibility(View.GONE,View.VISIBLE,View.GONE);
                else
                    handleVisibility(View.VISIBLE,View.GONE,View.GONE);
            }
        });

    }
    public void handleVisibility(Integer emptyCartVisibility,Integer bodyVisibility,Integer loadVisibility){
        emptyCartRL.setVisibility(emptyCartVisibility);
        bodyLL.setVisibility(bodyVisibility);
        loadCenter.setVisibility(loadVisibility);
    }
    public void generateBill(){
        if(adapter!=null){//adapter will be not null except first case
            adapter.setOnItemClickListener(null);
        }
        updateUI(0.3f,false,View.GONE,View.VISIBLE);
        viewModel.generateBill();
    }

    @Override
    protected void onResume() {
        super.onResume();
        generateBill();
    }

    public void updateUI(Float bookSlotAlpha, Boolean bookSlotEnabled, Integer totalPriceVisibility, Integer loadVisibility){
        bookSlot.setAlpha(bookSlotAlpha);
        bookSlot.setEnabled(bookSlotEnabled);
        totalAmount2.setVisibility(totalPriceVisibility);
        load.setVisibility(loadVisibility);
    }
    public void activateAdapter(Cart cart){
        //To maintain order of items
        ArrayList<Crop> crops=cart.getProducts();
        Collections.sort(crops, new Comparator<Crop>() {
            @Override
            public int compare(Crop o1, Crop o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        cart.setProducts(crops);
        adapter=new CartItemAdapter(cart.getProducts(),this);
        recyclerView.setAdapter(adapter);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter.setOnItemClickListener(listener);
        totalAmount1.setText(getResources().getString(R.string.Rs)+" "+Float.toString(cart.getTotalPrice()));
        totalAmount2.setText(getResources().getString(R.string.Rs)+" "+Float.toString(cart.getTotalPrice()));
        if(cart.getTotalPrice()>0){
            updateUI(1f,true,View.VISIBLE,View.GONE);
        }else if(cart.getTotalPrice()==0){
            updateUI(0.3f,false,View.VISIBLE,View.GONE);
        }
    }
    public void onBackClick(View view){
        finish();
    }
    CartItemAdapter.onItemClickListener listener=new CartItemAdapter.onItemClickListener() {
        @Override
        public void onItemClick(int position) {

        }

        @Override
        public void onIncrementClick(int position) {
            if(!CheckInternet.isConnected(CartActivity.this)){
                Snackbar snackbar=Snackbar.make(parent,"No Internet Connection",Snackbar.LENGTH_SHORT);
                snackbar.show();
                return;
            }
            Crop crop=adapter.getItem(position);
            crop.setQuantity(crop.getQuantity()+1);
            adapter.notifyItemChanged(position);
            if(crop.getQuantity()==1)
                LocalCart.count++;
            LocalCart.update(getApplication(),crop.getId(),Integer.toString(crop.getQuantity()));
            generateBill();
        }

        @Override
        public void onDecrementClick(int position) {
            if(!CheckInternet.isConnected(CartActivity.this)){
                Snackbar snackbar=Snackbar.make(parent,"No Internet Connection",Snackbar.LENGTH_SHORT);
                snackbar.show();
                return;
            }
            Crop crop=adapter.getItem(position);
            if(crop.getQuantity()>0) {
                crop.setQuantity(crop.getQuantity() - 1);
                adapter.notifyItemChanged(position);
                if(crop.getQuantity()==0)
                    LocalCart.count--;
                LocalCart.update(getApplication(), crop.getId(), Integer.toString(crop.getQuantity()));
                generateBill();
            }
        }
    };
    public void bookSlot(View view){
        Intent intent=new Intent(getApplicationContext(),BookSlotActivity.class);
        startActivity(intent);
    }
}
