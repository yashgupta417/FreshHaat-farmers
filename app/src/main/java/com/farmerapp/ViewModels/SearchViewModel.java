package com.farmerapp.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.farmerapp.Data.Crop;
import com.farmerapp.Repositories.SearchRepository;

import java.util.List;

public class SearchViewModel extends AndroidViewModel{
    SearchRepository repository;
    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository=new SearchRepository(application);
    }
    public void queryProducts(String query){
        repository.queryProducts(query);
    }
    public LiveData<List<Crop>> getResults(){
        return repository.getResults();
    }
}
