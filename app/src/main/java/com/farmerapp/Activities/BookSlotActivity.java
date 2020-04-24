package com.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.farmerapp.Adapters.SlotAdapter;
import com.farmerapp.BottomSheets.AddressBottomSheet;
import com.farmerapp.Data.Address;
import com.farmerapp.Data.Date;
import com.farmerapp.Data.Farmer;
import com.farmerapp.Data.Order;
import com.farmerapp.Fragments.DatePickerFragment;
import com.example.farmerapp.R;
import com.farmerapp.Utils.CheckInternet;
import com.farmerapp.Utils.LocalCart;
import com.farmerapp.ViewModels.BookSlotViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import pl.droidsonroids.gif.GifImageView;

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
    Address address;//pickupAddress
    GifImageView load;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageActivity.loadSavedLocale(this);
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
        load=findViewById(R.id.load);
        selected=MANUAL;
        updateLoadUI(View.VISIBLE,0.3f,false);
        viewModel= ViewModelProviders.of(this).get(BookSlotViewModel.class);
        viewModel.getFarmer().observe(this, new Observer<Farmer>() {
            @Override
            public void onChanged(Farmer farmer) {
                updateLoadUI(View.GONE,1f,true);
                setAddress(farmer);
            }
        });

        Calendar c=Calendar.getInstance();
        setUpManualRecyclerView(c);
        setUpPickupRecyclerView(c);
    }
    public void updateLoadUI(Integer loadVisibility,Float alpha,Boolean enabled){
        load.setVisibility(loadVisibility);
        addNewAddress.setAlpha(alpha);
        addNewAddress.setEnabled(enabled);
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
                updateAddressInUI();
            }
        });
    }
    public void setAddress(Farmer farmer){
        address=new Address();
        address.setAddressLine1(farmer.getAddressLine1());
        address.setAddressLine2(farmer.getAddressLine2());
        address.setLandmark(farmer.getLandmark());
        address.setPin(farmer.getPin());
        address.setCity(farmer.getCity());
        address.setState(farmer.getState());
        updateAddressInUI();
    }
    public void updateAddressInUI(){
        if(address.getAddressLine1()!=null){
            pickUpAddress.setText(address.getAddressLine1());
            if(address.getAddressLine2()!=null)
                pickUpAddress.append(" "+address.getAddressLine2());
        }
    }
    public void onRLClick(View view){
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
            if(address==null){
                Toast.makeText(this, "Address loading", Toast.LENGTH_SHORT).show();
                updateButtonUI(true);
                return;
            }
            order.setOrderType("pickup");
            order.setSlotNumber(Integer.toString(pickupSlotsAdapter.getSelectedSlotNumber()));
            order.setPickupDate(new Date(pickupDayTextView.getText().toString(),
                                         pickupMonthTextView.getText().toString(),
                                         pickupYearTextView.getText().toString()));
            order.setPickupAddress(address);
        }else{
            order.setOrderType("manual");
            order.setSlotNumber(Integer.toString(manualSlotsAdapter.getSelectedSlotNumber()));
            order.setPickupDate(new Date(manualDayTextView.getText().toString(),
                                         manualMonthTextView.getText().toString(),
                                         manualYearTextView.getText().toString()));
            order.setPickupAddress(address);
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
