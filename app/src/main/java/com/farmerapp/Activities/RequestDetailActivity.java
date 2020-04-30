package com.farmerapp.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.farmerapp.Adapters.OrderItemAdapter;
import com.farmerapp.Data.Date;
import com.farmerapp.Data.Order;
import com.example.farmerapp.R;
import com.farmerapp.Utils.CheckInternet;
import com.farmerapp.ViewModels.RequestDetailViewModel;

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
    Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageActivity.loadSavedLocale(this);
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
            public void onChanged(Order o) {
                order=o;
                setUI();
            }
        });
    }
    public void onBackClick(View view){
        finish();
    }
    public void setUI(){
        handleVisibility(View.GONE,View.VISIBLE,true);
        String o_status=order.getOrderStatus(),s_status=order.getSlotStatus();
        requestStatus.setText(o_status);
        slotStatus.setText(s_status);
        setStatusColour(requestStatus,o_status.toLowerCase(),this);
        setStatusColour(slotStatus,s_status.toLowerCase(),this);
        if(o_status.equals("Cancelled"))
            buttonWork(false,0.3f,getResources().getString(R.string.request_cancel_request));

        Date d=order.getPickupDate();
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,Integer.parseInt(d.getYear()));
        calendar.set(Calendar.MONTH,Integer.parseInt(d.getMonth())-1);
        calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(d.getDay()));
        SimpleDateFormat dayformat=new SimpleDateFormat("MMM dd");
        date.setText(dayformat.format(calendar.getTime()));

        ArrayList<String> slotTimes=new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.slot_times)));
        String slotTime=slotTimes.get(Math.max(Integer.parseInt(order.getSlotNumber())-1,0));
        slot.setText(getResources().getString(R.string.request_slot)+" "+slotTime);

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
    public static void setStatusColour(TextView textView, String status, Context context){
        if(status.equals("confirmed")|| status.equals("accepted")){
            textView.setTextColor(context.getResources().getColor(R.color.confirmed));
        }else if(status.equals("pending")){
            textView.setTextColor(context.getResources().getColor(R.color.pending));
        }else if(status.equals("rejected")){
            textView.setTextColor(context.getResources().getColor(R.color.rejected));
        }else if(status.equals("changed")){
            textView.setTextColor(context.getResources().getColor(R.color.changed));
        }else if(status.equals("cancelled")){
            textView.setTextColor(context.getResources().getColor(R.color.rejected));
        }
    }
    public void cancelRequest(){
        if(!CheckInternet.isConnected(this)){
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }
        buttonWork(false,0.3f,getResources().getString(R.string.request_cancelling));
        viewModel.cancelRequest(order.getDatabaseId()).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer==1){
                    Toast.makeText(RequestDetailActivity.this, "Request Cancelled Successfully", Toast.LENGTH_SHORT).show();
                    buttonWork(false,0.3f,getResources().getString(R.string.request_cancel_request));
                    String cancelled="Cancelled";
                    requestStatus.setText(cancelled);
                    slotStatus.setText(cancelled);
                    setStatusColour(requestStatus,cancelled.toLowerCase(),getApplication());
                    setStatusColour(slotStatus,cancelled.toLowerCase(),getApplication());
                }else if(integer==-1){
                    Toast.makeText(RequestDetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    buttonWork(true,1f,getResources().getString(R.string.request_cancel_request));
                }
            }
        });

    }
    public void showConfirmationDialog(View view){
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.request_cancel_title))
                .setMessage(getResources().getString(R.string.request_cancel_message))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.request_cancel_positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelRequest();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.request_cancel_negative), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    public void buttonWork(Boolean bool,Float alpha,String text){
        button.setEnabled(bool);
        button.setAlpha(alpha);
        button.setText(text);
    }

}
