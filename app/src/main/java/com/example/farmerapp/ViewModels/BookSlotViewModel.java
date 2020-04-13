package com.example.farmerapp.ViewModels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.farmerapp.Data.Order;
import com.example.farmerapp.Repositories.BookSlotRepository;

import static com.example.farmerapp.Activities.SplashActivity.USER_ID;

public class BookSlotViewModel extends AndroidViewModel {
    BookSlotRepository repository;
    public BookSlotViewModel(@NonNull Application application) {
        super(application);
        repository=new BookSlotRepository(application);
    }
    public LiveData<Integer> placeSellRequest(Order order){
        SharedPreferences preferences=getApplication().getSharedPreferences(getApplication().getPackageName(), Context.MODE_PRIVATE);
        String userId=preferences.getString(USER_ID,null);
        return repository.placeSellRequest(order,userId);
    }
    public String getOrderDatabaseId(){
        return repository.getOrderDatabaseId();
    }
}
