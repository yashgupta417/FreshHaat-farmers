package com.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.farmerapp.Adapters.CartItemAdapter;
import com.farmerapp.Data.Cart;
import com.farmerapp.Data.Crop;
import com.example.farmerapp.R;
import com.farmerapp.Utils.CheckInternet;
import com.farmerapp.Utils.LocalCart;
import com.farmerapp.ViewModels.CartViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    Boolean canChange=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageActivity.loadSavedLocale(this);
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
        startTimer();
    }
    private Integer Connection_Time_Out=10*60*1000;
    public void startTimer(){
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent=new Intent(getApplication(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, Connection_Time_Out);
    }
    public void handleVisibility(Integer emptyCartVisibility,Integer bodyVisibility,Integer loadVisibility){
        emptyCartRL.setVisibility(emptyCartVisibility);
        bodyLL.setVisibility(bodyVisibility);
        loadCenter.setVisibility(loadVisibility);
    }

    public void updateUI(Float bookSlotAlpha, Boolean bookSlotEnabled, Integer totalPriceVisibility, Integer loadVisibility){
        bookSlot.setAlpha(bookSlotAlpha);
        bookSlot.setEnabled(bookSlotEnabled);
        totalAmount2.setVisibility(totalPriceVisibility);
        load.setVisibility(loadVisibility);
    }
    public Float getTotalPrice(ArrayList<Crop> crops){
        Float totalPrice=0f;
        for(Crop crop:crops){
            totalPrice+=crop.getQuantity()*crop.getOfferPrice();
        }
        return totalPrice;
    }
    public void activateAdapter(Cart cart){
        //To maintain order of items
        ArrayList<Crop> crops=cart.getProducts();
        Float totalPrice=getTotalPrice(crops);
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
        totalAmount1.setText(getResources().getString(R.string.Rs)+" "+Float.toString(totalPrice));
        totalAmount2.setText(getResources().getString(R.string.Rs)+" "+Float.toString(totalPrice));
        if(totalPrice>0){
            updateUI(1f,true,View.VISIBLE,View.GONE);
        }else if(totalPrice==0){
            updateUI(0.3f,false,View.VISIBLE,View.GONE);
        }
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

    public void onBackClick(View view){
        finish();
    }
    CartItemAdapter.onItemClickListener listener=new CartItemAdapter.onItemClickListener() {
        @Override
        public void onItemClick(int position) {

        }

        @Override
        public void onIncrementClick(int position) {
            if(!adapter.isClickAble)
                return;
            adapter.isClickAble=false;
            if(!CheckInternet.isConnected(CartActivity.this)){
                Snackbar snackbar=Snackbar.make(parent,"No Internet Connection",Snackbar.LENGTH_SHORT);
                snackbar.show();
                adapter.isClickAble=true;
                return;
            }
            Crop crop=adapter.crops.get(position);
            if(crop.getQuantity()==crop.getMaxQuantity()){
                Toast.makeText(CartActivity.this, getResources().getString(R.string.cart_maximum_message), Toast.LENGTH_SHORT).show();
                adapter.isClickAble=true;
                return;
            }
            crop.setQuantity(crop.getQuantity()+1);
            adapter.notifyDataSetChanged();
            if(crop.getQuantity()==1)
                LocalCart.count++;
            LocalCart.update(getApplication(),crop.getId(),Integer.toString(crop.getQuantity()));
            generateBill();
        }

        @Override
        public void onDecrementClick(int position) {
            if(!adapter.isClickAble)
                return;
            adapter.isClickAble=false;
            if(!CheckInternet.isConnected(CartActivity.this)){
                Snackbar snackbar=Snackbar.make(parent,"No Internet Connection",Snackbar.LENGTH_SHORT);
                snackbar.show();
                adapter.isClickAble=true;
                return;
            }
            Crop crop=adapter.crops.get(position);
            if(crop.getQuantity()==crop.getMinQuantity()){
                crop.setQuantity(0);
                LocalCart.count--;
                LocalCart.update(getApplication(), crop.getId(), Integer.toString(crop.getQuantity()));
                adapter.notifyDataSetChanged();
                Toast.makeText(CartActivity.this, getResources().getString(R.string.cart_item_removed), Toast.LENGTH_SHORT).show();
                generateBill();
            }
            else if(crop.getQuantity()>crop.getMinQuantity()) {
                crop.setQuantity(crop.getQuantity() - 1);
                adapter.notifyDataSetChanged();
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
