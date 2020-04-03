package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmerapp.R;
import com.example.farmerapp.Utils.CheckInternet;
import com.example.farmerapp.ViewModels.LoginViewModel;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {
    LoginViewModel viewModel;
    EditText phoneEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewModel= ViewModelProviders.of(this).get(LoginViewModel.class);
        phoneEditText=findViewById(R.id.phone);
        buttonEnabling();
    }
    public void login(View view){
        CheckInternet checkInternet=new CheckInternet();
        if(checkInternet.isConnected(this)) {
            viewModel.generateOTP(phoneEditText.getText().toString());
            goToOTPActivity();
        }
        else
            showNetworkError();
    }
    public void showNetworkError(){
        ConstraintLayout parent=findViewById(R.id.parent_login);
        Snackbar snackbar=Snackbar.make(parent,"No Internet Connection",Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
    public void goToOTPActivity(){
        Intent intent=new Intent(getApplicationContext(), VerifyOTPActivity.class);
        intent.putExtra("mobileNumber",phoneEditText.getText().toString());
        startActivity(intent);
    }
    public void buttonEnabling(){
        Button loginButton=findViewById(R.id.login);
        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()==10){
                    loginButton.setEnabled(true);
                    loginButton.setAlpha(1);
                }else{
                    loginButton.setEnabled(false);
                    loginButton.setAlpha(0.3f);
                }
            }
        });
    }
}
