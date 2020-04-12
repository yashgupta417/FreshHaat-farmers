package com.example.farmerapp.ViewModels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.farmerapp.Data.Cart;
import com.example.farmerapp.Data.Crop;
import com.example.farmerapp.Repositories.CartRepository;
import com.example.farmerapp.Utils.LocalCart;

import java.util.ArrayList;

public class CartViewModel extends AndroidViewModel {
    CartRepository repository;
    SharedPreferences preferences;
    public CartViewModel(@NonNull Application application) {
        super(application);
        repository=new CartRepository(application);
        preferences=getApplication().getSharedPreferences(getApplication().getPackageName(), Context.MODE_PRIVATE);
    }
    public void generateBill(){
        String userId=preferences.getString("userId",null);
        ArrayList<String> productIds=LocalCart.getProductIds(getApplication());
        ArrayList<Integer> quantites=LocalCart.getQuantities(getApplication());
        ArrayList<Crop> products=new ArrayList<Crop>();
        for(int i=0;i<productIds.size();i++){
            Crop product=new Crop();
            product.setId(productIds.get(i));
            product.setQuantity(quantites.get(i));
            products.add(product);
            Log.i("******",products.get(i).getId());
        }
        Cart localCart=new Cart();
        localCart.setProducts(products);
        Log.i("*****size",Integer.toString(localCart.getProducts().size()));
        repository.generateBill(localCart,userId);
    }
    public LiveData<Cart> getBill(){
        return repository.getBill();
    }
}
