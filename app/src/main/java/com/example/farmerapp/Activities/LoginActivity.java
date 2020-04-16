package com.example.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    Button loginButton;
    public static Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activity=this;
        viewModel= ViewModelProviders.of(this).get(LoginViewModel.class);
        phoneEditText=findViewById(R.id.phone);
        loginButton=findViewById(R.id.login);
        buttonEnabling();
    }
    public void login(View view){
        updateUI(false,0.3f);
        hideKeyboard();
        if(CheckInternet.isConnected(this)) {
            viewModel.generateOTP(phoneEditText.getText().toString());
            goToOTPActivity();
        }
        else {
            showNetworkError();
            updateUI(true,1f);
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        updateUI(true,1f);
    }

    public void buttonEnabling(){
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
                    updateUI(true,1f);
                }else{
                    updateUI(false,0.3f);
                }
            }
        });
    }
    public void updateUI(Boolean bool,Float alpha){
        loginButton.setEnabled(bool);
        loginButton.setAlpha(alpha);
    }
    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
