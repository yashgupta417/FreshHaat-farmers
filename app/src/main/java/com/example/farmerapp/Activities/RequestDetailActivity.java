package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.farmerapp.Adapters.OrderItemAdapter;
import com.example.farmerapp.Data.Date;
import com.example.farmerapp.Data.Order;
import com.example.farmerapp.R;
import com.example.farmerapp.ViewModels.RequestDetailViewModel;

public class RequestDetailActivity extends AppCompatActivity {
    TextView requestStatus,slotStatus,date,slot;
    RecyclerView recyclerView;
    RequestDetailViewModel viewModel;
    public static String ORDER_ID="orderId";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        String orderId=getIntent().getStringExtra(ORDER_ID);
        requestStatus=findViewById(R.id.sell_request_status);
        slotStatus=findViewById(R.id.slot_status);
        date=findViewById(R.id.date);
        slot=findViewById(R.id.slot);
        recyclerView=findViewById(R.id.recyler_view);
        viewModel= ViewModelProviders.of(this).get(RequestDetailViewModel.class);
        viewModel.getSellRequest(orderId).observe(this, new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                setUI(order);
            }
        });
    }
    public void setUI(Order order){
        String o_status=order.getOrderStatus(),s_status=order.getSlotStatus();
        requestStatus.setText(o_status);
        slotStatus.setText(s_status);
        setStatusColour(requestStatus,o_status.toLowerCase());
        setStatusColour(slotStatus,s_status.toLowerCase());
        Date d=order.getPickupDate();
        date.setText(d.getDay()+"-"+d.getMonth()+"-"+d.getYear());
        slot.setText("Slot "+order.getSlotNumber());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        OrderItemAdapter adapter=new OrderItemAdapter(order.getProducts(),this);
        recyclerView.setAdapter(adapter);
    }
    public void setStatusColour(TextView textView,String status){
        if(status.equals("confirmed")){
            textView.setTextColor(getResources().getColor(R.color.confirmed));
        }else if(status.equals("pending")){
            textView.setTextColor(getResources().getColor(R.color.pending));
        }else if(status.equals("rejected")){
            textView.setTextColor(getResources().getColor(R.color.rejected));
        }else if(status.equals("changed")){
            textView.setTextColor(getResources().getColor(R.color.changed));
        }
    }
}
