package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.farmerapp.Adapters.OrderItemAdapter;
import com.example.farmerapp.Data.Date;
import com.example.farmerapp.Data.Order;
import com.example.farmerapp.R;
import com.example.farmerapp.ViewModels.RequestDetailViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import pl.droidsonroids.gif.GifImageView;

public class RequestDetailActivity extends AppCompatActivity {
    TextView requestStatus,slotStatus,date,slot,callExecutiveText;
    Button button;
    RecyclerView recyclerView;
    RequestDetailViewModel viewModel;
    public static String ORDER_ID="orderId";
    GifImageView load;
    RelativeLayout rl1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        String orderId=getIntent().getStringExtra(ORDER_ID);
        requestStatus=findViewById(R.id.sell_request_status);
        slotStatus=findViewById(R.id.slot_status);
        date=findViewById(R.id.date);
        slot=findViewById(R.id.slot);
        load=findViewById(R.id.load);
        recyclerView=findViewById(R.id.recyler_view);
        callExecutiveText=findViewById(R.id.call_exe_text);
        rl1=findViewById(R.id.rl_1);
        button=findViewById(R.id.cancel_request);
        handleVisibility(View.VISIBLE,View.INVISIBLE,false);
        viewModel= ViewModelProviders.of(this).get(RequestDetailViewModel.class);
        viewModel.getSellRequest(orderId).observe(this, new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                setUI(order);
            }
        });
    }
    public void onBackClick(View view){
        finish();
    }
    public void setUI(Order order){
        handleVisibility(View.GONE,View.VISIBLE,true);
        String o_status=order.getOrderStatus(),s_status=order.getSlotStatus();
        requestStatus.setText(o_status);
        slotStatus.setText(s_status);
        setStatusColour(requestStatus,o_status.toLowerCase());
        setStatusColour(slotStatus,s_status.toLowerCase());

        Date d=order.getPickupDate();
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,Integer.parseInt(d.getYear()));
        calendar.set(Calendar.MONTH,Integer.parseInt(d.getMonth()));
        calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(d.getDay()));
        SimpleDateFormat dayformat=new SimpleDateFormat("MMM dd");
        date.setText(dayformat.format(calendar.getTime()));

        ArrayList<String> slotTimes=new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.slot_times)));
        String slotTime=slotTimes.get(Math.max(Integer.parseInt(order.getSlotNumber())-1,0));
        slot.setText("Slot : "+slotTime);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        OrderItemAdapter adapter=new OrderItemAdapter(order.getProducts(),this);
        recyclerView.setAdapter(adapter);
    }
    public void handleVisibility(Integer loadVisibility,Integer otherViewsVisibility,Boolean buttonEnabled){
        load.setVisibility(loadVisibility);
        rl1.setVisibility(otherViewsVisibility);
        callExecutiveText.setVisibility(otherViewsVisibility);
        button.setVisibility(otherViewsVisibility);
        button.setEnabled(buttonEnabled);
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
