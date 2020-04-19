package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmerapp.Adapters.SlotAdapter;
import com.example.farmerapp.BottomSheets.AddressBottomSheet;
import com.example.farmerapp.Data.Address;
import com.example.farmerapp.Data.Date;
import com.example.farmerapp.Data.Order;
import com.example.farmerapp.Fragments.DatePickerFragment;
import com.example.farmerapp.R;
import com.example.farmerapp.Utils.CheckInternet;
import com.example.farmerapp.Utils.LocalCart;
import com.example.farmerapp.ViewModels.BookSlotViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.santalu.maskedittext.MaskEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class BookSlotActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    RecyclerView recyclerView_pick,recyclerView_manual;
    RelativeLayout pickRL,manualRL;
    LinearLayout pickupDate,manualDate;
    Button placeRequest;
    TextView pickUpAddress,manualAddress;
    private Integer selected;
    private Boolean pickUpDateSelected=false,manualDateSelected=false;
    private static Integer PICKUP=0,MANUAL=1;
    SlotAdapter pickupSlotsAdapter,manualSlotsAdapter;
    BookSlotViewModel viewModel;
    TextView addNewAddress;
    String addressLines,landmark,pincode,city,state;
    Address address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_slot);
        recyclerView_manual=findViewById(R.id.recyler_view_cc);
        recyclerView_pick=findViewById(R.id.recyler_view_pickup);
        placeRequest=findViewById(R.id.place_request);
        pickUpAddress=findViewById(R.id.address);
        manualAddress=findViewById(R.id.cc_address);
        pickRL=findViewById(R.id.pickup_rl);
        manualRL=findViewById(R.id.manual_rl);
        pickupDate=findViewById(R.id.date);
        manualDate=findViewById(R.id.cc_date);
        addNewAddress=findViewById(R.id.add_new_address);
        selected=MANUAL;
        setAddress();
        Calendar c=Calendar.getInstance();
        setUpManualRecyclerView(c);
        setUpPickupRecyclerView(c);
        viewModel= ViewModelProviders.of(this).get(BookSlotViewModel.class);
    }
    public void addNewAddress(View view){
        if(selected==MANUAL) {
            onRLClick(pickRL);
            return;
        }
        AddressBottomSheet bottomSheet=new AddressBottomSheet(address);
        bottomSheet.show(getSupportFragmentManager(),"address");
        bottomSheet.setOnConfirmListener(new AddressBottomSheet.OnConfirmLocationListener() {
            @Override
            public void onConfirmLocation(Address changedAddress) {
                address=changedAddress;
                pickUpAddress.setText(address.getAddress());
            }
        });
    }
    public void setAddress(){
        SharedPreferences preferences=getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        addressLines=preferences.getString(SplashActivity.ADDRESS,null);
        landmark=preferences.getString(SplashActivity.LANDMARK,null);
        pincode=preferences.getString(SplashActivity.PINCODE,null);
        city=preferences.getString(SplashActivity.CITY,null);
        state=preferences.getString(SplashActivity.STATE,null);
        address=new Address(addressLines);
        address.setLandmark(landmark);
        address.setPin(pincode);
        address.setCity(city);
        address.setState(state);
        if(addressLines!=null){
            pickUpAddress.setText(addressLines);
        }
    }
    public void onRLClick(View view){
        Log.i("****","onRLClick");
        if(view.getId()==R.id.pickup_rl){
            pickRL.setBackground(getResources().getDrawable(R.drawable.book_slot_card_border));
            manualRL.setBackgroundResource(0);
            recyclerView_pick.setClickable(true);
            recyclerView_manual.setClickable(false);
            selected=PICKUP;
            updateButtonUI(pickUpDateSelected);
        }else if(view.getId()==R.id.manual_rl){
            manualRL.setBackground(getResources().getDrawable(R.drawable.book_slot_card_border));
            pickRL.setBackgroundResource(0);
            recyclerView_manual.setClickable(true);
            recyclerView_pick.setClickable(false);
            selected=MANUAL;
            updateButtonUI(manualDateSelected);
        }
    }
    public void updateButtonUI(Boolean active){
        if(active){
            placeRequest.setAlpha(1f);
            placeRequest.setEnabled(true);
        }else{
            placeRequest.setAlpha(0.3f);
            placeRequest.setEnabled(false);
        }
    }
    public void onPickUpDateClick(View v){
        if(selected==PICKUP) {
            DialogFragment fragment = new DatePickerFragment();
            fragment.show(getSupportFragmentManager(), "Date Picker");
        }else{
            onRLClick(pickRL);
        }
    }
    public void onManualDateclick(View v){
        if(selected==MANUAL) {
            DialogFragment fragment = new DatePickerFragment();
            fragment.show(getSupportFragmentManager(), "Date Picker");
        }else{
            onRLClick(manualRL);
        }
    }
    public void setUpPickupRecyclerView(Calendar selectedDate){
        recyclerView_pick.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView_pick.setHasFixedSize(true);
        ((SimpleItemAnimator) recyclerView_pick.getItemAnimator()).setSupportsChangeAnimations(false);
        pickupSlotsAdapter=new SlotAdapter(this,selectedDate);
        recyclerView_pick.setAdapter(pickupSlotsAdapter);
        pickupSlotsAdapter.setOnItemClickListener(new SlotAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {

                if(selected.equals(PICKUP)) {
                    pickupSlotsAdapter.updateSelctedItem(position);
                }
                else {
                    onRLClick(pickRL);
                }
            }
        });
    }
    public void setUpManualRecyclerView(Calendar selectedDate){
        recyclerView_manual.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView_manual.setHasFixedSize(true);
        ((SimpleItemAnimator) recyclerView_pick.getItemAnimator()).setSupportsChangeAnimations(false);
        manualSlotsAdapter=new SlotAdapter(this,selectedDate);
        recyclerView_manual.setAdapter(manualSlotsAdapter);
        manualSlotsAdapter.setOnItemClickListener(new SlotAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(selected.equals(MANUAL)) {
                    manualSlotsAdapter.updateSelctedItem(position);
                }
                else
                    onRLClick(manualRL);
            }
        });
    }
    TextView pickupDayTextView,pickupMonthTextView,pickupYearTextView,manualDayTextView,manualMonthTextView,manualYearTextView;
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat dayformat=new SimpleDateFormat("dd");
        SimpleDateFormat monthformat=new SimpleDateFormat("MM");
        SimpleDateFormat yearformat=new SimpleDateFormat("yyyy");
        String d=dayformat.format(c.getTime());
        String m=monthformat.format(c.getTime());
        String y=yearformat.format(c.getTime());
        if(selected==PICKUP){
            pickupDayTextView=findViewById(R.id.day_pickup);
            pickupMonthTextView=findViewById(R.id.month_pickup);
            pickupYearTextView=findViewById(R.id.year_pickup);
            pickupDayTextView.setText(d);
            pickupMonthTextView.setText(m);
            pickupYearTextView.setText(y);
            pickUpDateSelected=true;
            updateButtonUI(pickUpDateSelected);
            setUpPickupRecyclerView(c);
        }else if(selected==MANUAL){
            manualDayTextView=findViewById(R.id.day_manual);
            manualMonthTextView=findViewById(R.id.month_manual);
            manualYearTextView=findViewById(R.id.year_manual);
            manualDayTextView.setText(d);
            manualMonthTextView.setText(m);
            manualYearTextView.setText(y);
            manualDateSelected=true;
            updateButtonUI(manualDateSelected);
            setUpManualRecyclerView(c);
        }
    }
    public void placeRequestWork(View view){
        if(!CheckInternet.isConnected(this)){
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            return;
        }
        updateButtonUI(false);
        Order order=new Order();
        if(selected==PICKUP){
            order.setOrderType("pickup");
            order.setSlotNumber(pickupSlotsAdapter.slotNames.get(pickupSlotsAdapter.selectedIndex).substring(5,6));
            order.setPickupDate(new Date(pickupDayTextView.getText().toString(),
                                         pickupMonthTextView.getText().toString(),
                                         pickupYearTextView.getText().toString()));
            order.setPickupAddress(address);
        }else{
            order.setOrderType("manual");
            order.setSlotNumber(manualSlotsAdapter.slotNames.get(manualSlotsAdapter.selectedIndex).substring(5,6));
            order.setPickupDate(new Date(manualDayTextView.getText().toString(),
                                         manualMonthTextView.getText().toString(),
                                         manualYearTextView.getText().toString()));
            order.setPickupAddress(new Address(manualAddress.getText().toString()));
        }
        viewModel.placeSellRequest(order).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer==1){
                    LocalCart.emptyCart(getApplication());
                    String orderId=viewModel.getOrderDatabaseId();
                    goToRequestDetailActivity(orderId);
                }else if(integer==-1){
                    updateButtonUI(true);
                    Toast.makeText(BookSlotActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void onBackClick(View view){
        finish();
    }
    public void goToRequestDetailActivity(String orderId){
        Intent intent=new Intent(this,RequestDetailActivity.class);
        intent.putExtra(RequestDetailActivity.ORDER_ID,orderId);
        startActivity(intent);
        finish();
    }
}
