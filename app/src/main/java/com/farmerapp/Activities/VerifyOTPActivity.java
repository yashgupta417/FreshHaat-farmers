package com.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmerapp.R;
import com.farmerapp.Repositories.VerifyOTPRepository;
import com.farmerapp.Utils.CheckInternet;
import com.farmerapp.Utils.OTPSetup;
import com.farmerapp.ViewModels.VerifyOTPViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class VerifyOTPActivity extends AppCompatActivity {
    EditText otp1,otp2,otp3,otp4,otp5;
    VerifyOTPViewModel viewModel;
    String mobileNumber;
    String otp;
    TextView otpTimer,resendOtp;
    Button verifyButton;
    ConstraintLayout parent;
    OTPSetup otpSetup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageActivity.loadSavedLocale(this);
        setContentView(R.layout.activity_verify_otp);
        viewModel= ViewModelProviders.of(this).get(VerifyOTPViewModel.class);
        mobileNumber =getIntent().getStringExtra("mobileNumber");
        otpTimer=findViewById(R.id.otp_timer);
        resendOtp=findViewById(R.id.resend_otp);
        verifyButton=findViewById(R.id.verify);
        parent=findViewById(R.id.verify_otp_parent);

        otpSetup=new OTPSetup(this);
        otpSetup.setup();
        otpSetup.observeOTP().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                otp=s;
                if(otp.length()==5){
                    updateUI(true,1f);
                }else{
                    updateUI(false,0.3f);
                }
            }
        });
        viewModel.startTimer().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long milliLeft) {
                if(milliLeft==0){
                    otpTimer.setVisibility(View.INVISIBLE);
                    resendOtp.setAlpha(1);
                    resendOtp.setEnabled(true);
                }else{
                    otpTimer.setVisibility(View.VISIBLE);
                    resendOtp.setAlpha(0.2f);
                    resendOtp.setEnabled(false);
                    updateTimeTextView(milliLeft);
                }
            }
        });
    }


    public void updateTimeTextView(Long milliLeft){
        int mins=(int) (milliLeft/1000)/60;
        int secs=(int) (milliLeft/1000)%60;
        String timeLeft= String.format(Locale.getDefault(), "%02d:%02d", mins,secs);
        otpTimer.setText(timeLeft);
    }
    public void resendOTP(View view){
        if(CheckInternet.isConnected(this)) {
            viewModel.regenerateOTP(mobileNumber);
            viewModel.startTimer();
        }
        else
            showNetworkError();
    }
    public void showNetworkError(){
        Snackbar snackbar=Snackbar.make(parent,"No Internet Connection",Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
    public void updateUI(Boolean bool,Float alpha){
        verifyButton.setEnabled(bool);
        verifyButton.setAlpha(alpha);
    }
    public void verify(View view){
        updateUI(false,0.3f);
        hideKeyboard();
        if(!CheckInternet.isConnected(this)) {
            updateUI(true,1f);
            showNetworkError();
            return;
        }
        viewModel.verifyOTP(mobileNumber,otp).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer result) {
                if(result== VerifyOTPRepository.NEW_USER){
                    goToActivity(RegisterDetailsActivity.class);
                }
                else if(result==VerifyOTPRepository.OLD_USER){
                    goToActivity(MainActivity.class);
                }
                else if(result==-1){
                    Toast.makeText(VerifyOTPActivity.this, "Wrong OTP", Toast.LENGTH_SHORT).show();
                    updateUI(true,1f);
                }else if(result==-2){
                    Toast.makeText(VerifyOTPActivity.this, "You are not registered as farmer", Toast.LENGTH_SHORT).show();
                    updateUI(true,1f);
                }
            }
        });
    }
    public void goToActivity(Class c){
        Intent intent=new Intent(getApplicationContext(),c);
        startActivity(intent);
        LoginActivity.activity.finish();
        finish();
    }
    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
