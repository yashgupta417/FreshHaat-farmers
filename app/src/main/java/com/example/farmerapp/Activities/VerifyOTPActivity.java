package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmerapp.R;
import com.example.farmerapp.Utils.CheckInternet;
import com.example.farmerapp.Utils.OTPSetup;
import com.example.farmerapp.ViewModels.VerifyOTPViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class VerifyOTPActivity extends AppCompatActivity {
    EditText otp1,otp2,otp3,otp4,otp5;
    VerifyOTPViewModel viewModel;
    String mobileNumber;
    String otp;
    TextView otpTimer,resendOtp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        viewModel= ViewModelProviders.of(this).get(VerifyOTPViewModel.class);
        mobileNumber =getIntent().getStringExtra("mobileNumber");
        otpTimer=findViewById(R.id.otp_timer);
        resendOtp=findViewById(R.id.resend_otp);

        OTPSetup otpSetup=new OTPSetup(this);
        otpSetup.setup();
        otpSetup.observeOTP().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                otp=s;
                verifyButtonEnabling();
            }
        });
        viewModel.startTimer();
        viewModel.getTimeLeft().observe(this, new Observer<Long>() {
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
        CheckInternet checkInternet=new CheckInternet();
        if(checkInternet.isConnected(this)) {
            viewModel.regenerateOTP(mobileNumber);
            viewModel.startTimer();
        }
        else
            showNetworkError();
    }
    public void showNetworkError(){
        ConstraintLayout parent=findViewById(R.id.verify_otp_parent);
        Snackbar snackbar=Snackbar.make(parent,"No Internet Connection",Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
    public void verifyButtonEnabling(){
        Button verifyButton=findViewById(R.id.verify);
        if(otp.length()==5){
            verifyButton.setEnabled(true);
            verifyButton.setAlpha(1);
        }else{
            verifyButton.setEnabled(false);
            verifyButton.setAlpha(0.3f);
        }
    }
    public void verify(View view){
        viewModel.verifyOTP(mobileNumber,otp);
        viewModel.checkResult().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer result) {
                if(result==1){
                    Toast.makeText(VerifyOTPActivity.this, "Verified", Toast.LENGTH_SHORT).show();
                    finish();
                    goToSignUpActivity();

                }else if(result==-1){
                    Toast.makeText(VerifyOTPActivity.this, "Wrong OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void goToSignUpActivity(){
        Intent intent=new Intent(getApplicationContext(), RegisterDetailsActivity.class);
        startActivity(intent);
    }
}
