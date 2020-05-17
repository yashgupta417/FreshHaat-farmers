package com.farmerapp.Fragments.MainFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farmerapp.Activities.CartActivity;
import com.farmerapp.Activities.CollectionCentreActivity;
import com.farmerapp.Activities.MainActivity;
import com.farmerapp.Activities.SearchActivity;
import com.farmerapp.Activities.SellActivity;
import com.farmerapp.Adapters.BannersAdapter;
import com.farmerapp.Adapters.SellCropAdapter;
import com.farmerapp.BottomSheets.ChangeQuantityBottomSheet;
import com.farmerapp.Data.Banner;
import com.farmerapp.Data.Crop;
import com.example.farmerapp.R;
import com.farmerapp.Utils.LocalCart;
import com.farmerapp.ViewModels.MainViewModel;
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
    ImageView locationButton;
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
        locationButton=v.findViewById(R.id.location_button);
        setClickListeners(v);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setHasFixedSize(true);
        updateBadge();
        viewModel= ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getSuggestedCrops().observe(this, new Observer<List<Crop>>() {
            @Override
            public void onChanged(List<Crop> crops) {
                load.setVisibility(View.GONE);
                products=(ArrayList<Crop>) crops;
                if(products.size()>0)
                    suggestText.setVisibility(View.VISIBLE);
                activateAdapter();
            }
        });
        fetchAndDisplayBanners(viewModel,v);
        MainActivity.showLocation();
        MainActivity.setTitle("");
        return v;
    }
    public void goToCollectionCentreActivity(){
     Intent intent=new Intent(getActivity(), CollectionCentreActivity.class);
     getActivity().startActivity(intent);
    }
    public void fetchAndDisplayBanners(MainViewModel viewModel,View view){
        viewModel.getBanners().observe(this, new Observer<List<Banner>>() {
            @Override
            public void onChanged(List<Banner> banners) {
                setBanners(view,new ArrayList<>(banners));
            }
        });
    }
    public void setBanners(View v,ArrayList<Banner> banners){
        RecyclerView recyclerView=v.findViewById(R.id.banner_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        BannersAdapter adapter=new BannersAdapter(banners,getActivity());
        recyclerView.setAdapter(adapter);

    }
    public void activateAdapter(){
        products=LocalCart.syncQuantities(products,getActivity().getApplication());
        adapter=new SellCropAdapter(products,getActivity(),SellCropAdapter.NORMAL);
        recyclerView.setAdapter(adapter);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter.setOnItemClickListener(new SellCropAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onIncrementClick(int position) {
                adapter.incrementCount(position);
            }

            @Override
            public void onDecrementClick(int position) {
                adapter.decrementCount(position);
                updateBadge();
            }

            @Override
            public void onAddToCartClick(int position) {
                adapter.addToCart(position);
                updateBadge();
            }

            @Override
            public void onQuantityClick(int position) {
                ChangeQuantityBottomSheet bottomSheet=new ChangeQuantityBottomSheet(adapter.crops.get(position));
                bottomSheet.show(getActivity().getSupportFragmentManager(),"changeQuantity");
                bottomSheet.setOnDoneListener(new ChangeQuantityBottomSheet.OnDoneListener() {
                    @Override
                    public void onDone(Integer quantity) {
                        adapter.changeQuantity(position,quantity);
                        updateBadge();
                    }
                });
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
     locationButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             goToCollectionCentreActivity();
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
             Intent intent=new Intent(getActivity(), SearchActivity.class);
             startActivity(intent);
         }
     });

    }
}
