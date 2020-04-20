package com.example.farmerapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.farmerapp.BottomSheets.AddressBottomSheet;
import com.example.farmerapp.Data.Address;
import com.example.farmerapp.Data.Farmer;
import com.example.farmerapp.R;
import com.example.farmerapp.Utils.CheckInternet;
import com.example.farmerapp.ViewModels.ProfileViewModel;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

import static com.example.farmerapp.Activities.RegisterDetailsActivity.viewModel;

public class ProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    CircleImageView imageView;
    TextView mobileTextView;
    EditText nameEdittext,alternateMobileEdittext,kycEdittext,addressEdittext;
    ImageView edit_image,edit_alternateMob,edit_kyc,edit_address;
    ProfileViewModel viewModel;
    ConstraintLayout body;
    GifImageView load;
    boolean isEdited=false;
    public Farmer farmer;
    Button saveChanges;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView=findViewById(R.id.image);
        mobileTextView=findViewById(R.id.mobile_number);
        nameEdittext=findViewById(R.id.name);
        alternateMobileEdittext=findViewById(R.id.alternate_mobile_number);
        kycEdittext=findViewById(R.id.kyc);
        addressEdittext=findViewById(R.id.address);
        load=findViewById(R.id.load);
        body=findViewById(R.id.body_parent);
        edit_image=findViewById(R.id.edit_image);
        edit_alternateMob=findViewById(R.id.edit_alternate_mob);
        edit_kyc=findViewById(R.id.edit_kyc);
        edit_address=findViewById(R.id.edit_address);
        saveChanges=findViewById(R.id.save_changes);
        handleVisibility(View.VISIBLE,View.GONE);
        viewModel= ViewModelProviders.of(this).get(ProfileViewModel.class);
        viewModel.getFarmer().observe(this, new Observer<Farmer>() {
            @Override
            public void onChanged(Farmer f) {
                handleVisibility(View.GONE,View.VISIBLE);
                farmer=f;
                updateUI();
            }
        });
    }
    public void handleVisibility(Integer loadVisibility,Integer bodyVisibility){
        load.setVisibility(loadVisibility);
        body.setVisibility(bodyVisibility);
    }
    public void updateUI(){
        if(farmer.getProfilePhoto()!=null && !farmer.getProfilePhoto().equals("")){
            Glide.with(this).load(farmer.getProfilePhoto()).placeholder(R.drawable.load_static).into(imageView);
            imageView.setBorderWidth(5);
            imageView.setBorderColor(getResources().getColor(R.color.colorPrimary));
        }
        SharedPreferences preferences=getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String mobileNumber=preferences.getString(SplashActivity.MOBILE_NO,null);
        mobileTextView.setText("+91 "+mobileNumber);
        nameEdittext.setText(farmer.getName());
        if(farmer.getAlternateMob()!=null)
            alternateMobileEdittext.setText(farmer.getAlternateMob());
        if(farmer.getKYC()!=null)
            kycEdittext.setText(farmer.getKYC());
        if(farmer.getAddressLine1()!=null){
            addressEdittext.setText(farmer.getAddressLine1());
            if(farmer.getAddressLine1()!=null){
                addressEdittext.append(" "+farmer.getAddressLine2());
            }
        }
        listenEditRequest();
        activateEditingListeners();
    }
    public void onBackClick(View view){
        finish();
    }
    public void listenEditRequest(){
        edit_alternateMob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditextInFocus(alternateMobileEdittext);
            }
        });
        edit_kyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditextInFocus(kycEdittext);
            }
        });
        edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getEditextInFocus(addressEdittext);
                editAddress();
            }
        });
    }
    public void editAddress(){
        Address address=new Address();
        address.setAddressLine1(farmer.getAddressLine1());
        address.setAddressLine2(farmer.getAddressLine2());
        address.setLandmark(farmer.getLandmark());
        address.setPin(farmer.getPin());
        address.setCity(farmer.getCity());
        address.setState(farmer.getState());

        AddressBottomSheet bottomSheet=new AddressBottomSheet(address);
        bottomSheet.show(getSupportFragmentManager(),"address");
        bottomSheet.setOnConfirmListener(new AddressBottomSheet.OnConfirmLocationListener() {
            @Override
            public void onConfirmLocation(Address address) {
                farmer.setAddressLine1(address.getAddressLine1());
                farmer.setAddressLine2(address.getAddressLine2());
                farmer.setLandmark(address.getLandmark());
                farmer.setPin(address.getPin());
                farmer.setCity(address.getCity());
                farmer.setState(address.getState());
                updateUI();
                isEdited=true;
            }
        });
    }
    public void getEditextInFocus(EditText editText){
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
        editText.requestFocus();
        editText.setSelection(editText.getText().length());
        InputMethodManager imm = (InputMethodManager)getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }
    public void activateEditingListeners(){
        alternateMobileEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isEdited=true;
                farmer.setAlternateMob(s.toString());
            }
        });
        kycEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isEdited=true;
                farmer.setKYC(s.toString());

            }
        });
        addressEdittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    addressEdittext.clearFocus();
                    editAddress();
                }
            }
        });
    }
    public void updateSaveButtonUI(Boolean bool,Float alpha,String text){
        saveChanges.setEnabled(bool);
        saveChanges.setAlpha(alpha);
        if(text!=null)
            saveChanges.setText(text);
    }
    public void saveChanges(View view){
        if(!CheckInternet.isConnected(this)){
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isEdited){
            Toast.makeText(this, "No changes to apply", Toast.LENGTH_SHORT).show();
            return;
        }
        updateSaveButtonUI(false,0.3f,"Saving...");
        viewModel.postChanges(farmer).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer==1){
                    isEdited=false;
                    updateSaveButtonUI(true,1f,"Save Changes");
                    Toast.makeText(ProfileActivity.this, "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
                }else if(integer==-1){
                    updateSaveButtonUI(true,1f,"Save Changes");
                    Toast.makeText(ProfileActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==2){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null){
            if(data.getData()!=null){
                viewModel.image=data.getData();
                Glide.with(this).load(viewModel.image).into(imageView);
                imageView.setBorderWidth(5);
                imageView.setBorderColor(getResources().getColor(R.color.colorPrimary));
                isEdited=true;
            }
        }
    }
    public void chooseImage(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
            }else{
                Intent intent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        }else{
            Intent intent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,1);
        }
    }
}
