package com.farmerapp.Fragments.MainFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farmerapp.Activities.MainActivity;
import com.farmerapp.Activities.RequestDetailActivity;
import com.farmerapp.Adapters.RequestAdapter;
import com.farmerapp.Data.Order;
import com.example.farmerapp.R;
import com.farmerapp.ViewModels.MainViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class RequestFragment extends Fragment {
    public RequestFragment() {
    }
    RecyclerView recyclerView;
    MainViewModel viewModel;
    GifImageView load;
    TextView noRequestsText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_request, container, false);
        MainActivity.hideLocation();
        MainActivity.setTitle(getResources().getString(R.string.requests));
        recyclerView=v.findViewById(R.id.recyler_view);
        load=v.findViewById(R.id.load);
        noRequestsText=v.findViewById(R.id.no_requests);
        viewModel= ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getAllRequests().observe(this, new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                load.setVisibility(View.GONE);
                setUpRecyclerView((ArrayList<Order>) orders);
            }
        });
        return v;
    }
    public void setUpRecyclerView(ArrayList<Order> requests){
        if(requests.size()==0){
            noRequestsText.setVisibility(View.VISIBLE);
            return;
        }
        Collections.reverse(requests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        RequestAdapter adapter=new RequestAdapter(requests,getContext());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RequestAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(getActivity(), RequestDetailActivity.class);
                intent.putExtra(RequestDetailActivity.ORDER_ID,adapter.requests.get(position).getDatabaseId());
                startActivity(intent);

            }
        });

    }
}
