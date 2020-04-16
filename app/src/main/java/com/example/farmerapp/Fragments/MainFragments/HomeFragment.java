package com.example.farmerapp.Fragments.MainFragments;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmerapp.Activities.CartActivity;
import com.example.farmerapp.Activities.MainActivity;
import com.example.farmerapp.Activities.SearchActivity;
import com.example.farmerapp.Activities.SellActivity;
import com.example.farmerapp.Adapters.SellCropAdapter;
import com.example.farmerapp.Data.Crop;
import com.example.farmerapp.Data.Farmer;
import com.example.farmerapp.R;
import com.example.farmerapp.Utils.LocalCart;
import com.example.farmerapp.Utils.LocationUtil;
import com.example.farmerapp.ViewModels.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class HomeFragment extends Fragment {
    public HomeFragment() {

    }
    RecyclerView recyclerView;
    SellCropAdapter adapter;
    MainViewModel viewModel;
    GifImageView load;
    RelativeLayout sellFruitsRL,sellVegetablesRL;
    FloatingActionButton cartButton;
    ArrayList<Crop> products;
    TextView suggestText,badge;
    RelativeLayout vegOfferRl,fruitOfferRl,cropOfferRl;
    LinearLayout searchParent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=v.findViewById(R.id.suggested_crops_recylerview);
        load=v.findViewById(R.id.load);
        cartButton=v.findViewById(R.id.cart);
        badge=v.findViewById(R.id.badge);
        products=new ArrayList<>();
        suggestText=v.findViewById(R.id.suggest_text);
        suggestText.setVisibility(View.GONE);
        searchParent=v.findViewById(R.id.search_ll);
        setClickListeners(v);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setHasFixedSize(true);
        updateBadge();
        viewModel= ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFarmer().observe(this, new Observer<Farmer>() {
            @Override
            public void onChanged(Farmer farmer) {
                load.setVisibility(View.GONE);
                products=farmer.getCrops();
                if(products.size()>0)
                    suggestText.setVisibility(View.VISIBLE);
                activateAdapter();
            }
        });
        MainActivity.showLocation();
        MainActivity.setTitle("");
        return v;
    }

    public void activateAdapter(){
        products=LocalCart.syncQuantities(products,getActivity().getApplication());
        adapter=new SellCropAdapter(products,getContext(),SellCropAdapter.NORMAL);
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
                LocalCart.update(getActivity().getApplication(),crop.getId(),Integer.toString(crop.getQuantity()));
            }

            @Override
            public void onDecrementClick(int position) {
                Crop crop=adapter.getItem(position);
                if(crop.getQuantity()>0) {
                    crop.setQuantity(crop.getQuantity() - 1);
                    adapter.notifyItemChanged(position);
                    if(crop.getQuantity()==0) {
                        LocalCart.count--;
                        updateBadge();
                    }
                    LocalCart.update(getActivity().getApplication(), crop.getId(), Integer.toString(crop.getQuantity()));
                }
            }

            @Override
            public void onAddToCartClick(int position) {
                Crop crop=adapter.getItem(position);
                crop.setQuantity(1);
                adapter.notifyItemChanged(position);
                LocalCart.count++;
                updateBadge();
                LocalCart.update(getActivity().getApplication(), crop.getId(), Integer.toString(crop.getQuantity()));
            }
        });
    }
    public void updateBadge(){
        if(LocalCart.count==0) {
            badge.setVisibility(View.GONE);
        }else{
            badge.setVisibility(View.VISIBLE);
            badge.setText(Integer.toString(LocalCart.count));
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        updateBadge();
        activateAdapter();
    }

    public void setClickListeners(View v){
     sellFruitsRL=v.findViewById(R.id.sell_fruits);
     sellVegetablesRL=v.findViewById(R.id.sell_vegetables);
     vegOfferRl=v.findViewById(R.id.veg_offer);
     fruitOfferRl=v.findViewById(R.id.fruit_offer);
     vegOfferRl.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent=new Intent(getActivity().getApplicationContext(),SellActivity.class);
             intent.putExtra(MainActivity.PRODUCT_TYPE,MainActivity.VEGETABLES);
             getActivity().startActivity(intent);
         }
     });
     fruitOfferRl.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent=new Intent(getActivity().getApplicationContext(), SellActivity.class);
             intent.putExtra(MainActivity.PRODUCT_TYPE,MainActivity.FRUITS);
             getActivity().startActivity(intent);
         }
     });
     sellFruitsRL.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent=new Intent(getActivity().getApplicationContext(), SellActivity.class);
             intent.putExtra(MainActivity.PRODUCT_TYPE,MainActivity.FRUITS);
             getActivity().startActivity(intent);
         }
     });
     sellVegetablesRL.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent=new Intent(getActivity().getApplicationContext(),SellActivity.class);
             intent.putExtra(MainActivity.PRODUCT_TYPE,MainActivity.VEGETABLES);
             getActivity().startActivity(intent);
         }
     });
     cartButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent=new Intent(getActivity().getApplicationContext(), CartActivity.class);
             getActivity().startActivity(intent);
         }
     });
     searchParent.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent=new Intent(getActivity(),SearchActivity.class);
             startActivity(intent);
         }
     });

    }
}
