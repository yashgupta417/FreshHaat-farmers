package com.farmerapp.Utils;

import android.app.Activity;
import android.app.Application;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.farmerapp.R;

public class OTPSetup {
    EditText otp1,otp2,otp3,otp4,otp5;
    Activity activity;
    MutableLiveData<String> otp;
    public OTPSetup(Activity activity) {
        this.activity=activity;
        otp=new MutableLiveData<String>();
    }
    public void setOTP(String s){
        if(s==null || s.length()!=5)
            return;
        otp1.setText(s.substring(0,1));
        otp2.setText(s.substring(1,2));
        otp3.setText(s.substring(2,3));
        otp4.setText(s.substring(3,4));
        otp5.setText(s.substring(4,5));
        otp5.setSelection(1);
    }
    public void setup(){
        otp1=activity.findViewById(R.id.otp1);
        otp2=activity.findViewById(R.id.otp2);
        otp3=activity.findViewById(R.id.otp3);
        otp4=activity.findViewById(R.id.otp4);
        otp5=activity.findViewById(R.id.otp5);

        otp1.addTextChangedListener(new GenericTextWatcher(otp1));
        otp2.addTextChangedListener(new GenericTextWatcher(otp2));
        otp3.addTextChangedListener(new GenericTextWatcher(otp3));
        otp4.addTextChangedListener(new GenericTextWatcher(otp4));
        otp5.addTextChangedListener(new GenericTextWatcher(otp4));

        otp1.setOnFocusChangeListener(listener);
        otp2.setOnFocusChangeListener(listener);
        otp3.setOnFocusChangeListener(listener);
        otp4.setOnFocusChangeListener(listener);
        otp5.setOnFocusChangeListener(listener);


        otp1.setOnKeyListener(onKeyListener);
        otp2.setOnKeyListener(onKeyListener);
        otp3.setOnKeyListener(onKeyListener);
        otp4.setOnKeyListener(onKeyListener);
        otp5.setOnKeyListener(onKeyListener);
    }
    public class GenericTextWatcher implements TextWatcher {
        private View view;
        private GenericTextWatcher(View view){
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            updateOTP();
            switch(view.getId()) {
                case R.id.otp1:
                    if(text.length()==1)
                        otp2.requestFocus();
                    break;
                case R.id.otp2:
                    if(text.length()==1)
                        otp3.requestFocus();
                    else if(text.length()==0)
                        otp1.requestFocus();
                    break;
                case R.id.otp3:
                    if(text.length()==1)
                        otp4.requestFocus();
                    else if(text.length()==0)
                        otp2.requestFocus();
                    break;
                case R.id.otp4:
                    if(text.length()==1)
                        otp5.requestFocus();
                    else if(text.length()==0)
                        otp3.requestFocus();
                    break;
                case R.id.otp5:
                    if(text.length()==0)
                        otp5.requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    }

    private EditText.OnFocusChangeListener listener=new EditText.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){

                if(otp1.length()==0)
                    otp1.requestFocus();
                else if(otp2.length()==0)
                    otp2.requestFocus();
                else if(otp3.length()==0)
                    otp3.requestFocus();
                else if(otp4.length()==0)
                    otp4.requestFocus();
                else
                    otp5.requestFocus();
            }
        }
    };

    View.OnKeyListener onKeyListener=new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent event) {
            if(event.getAction()==KeyEvent.ACTION_DOWN && keyCode== KeyEvent.KEYCODE_DEL ){
                switch(view.getId()) {
                    case R.id.otp1:
                        break;
                    case R.id.otp2:
                        if(otp2.length()==0) {
                            otp1.setText("");
                            otp1.requestFocus();
                            updateOTP();
                        }
                        break;
                    case R.id.otp3:
                        if(otp3.length()==0) {
                            otp2.setText("");
                            otp2.requestFocus();
                            updateOTP();
                        }
                        break;
                    case R.id.otp4:
                        if(otp4.length()==0) {
                            otp3.setText("");
                            otp3.requestFocus();
                            updateOTP();
                        }
                        break;
                    case R.id.otp5:
                        if(otp5.length()==0) {
                            otp4.setText("");
                            otp4.requestFocus();
                            updateOTP();
                        }else{
                            return false;
                        }
                        break;
                }
                return true;
            }
            return false;
        }
    };

    public void updateOTP(){
        otp.setValue(otp1.getText().toString()+
                otp2.getText().toString()+
                otp3.getText().toString()+
                otp4.getText().toString()+
                otp5.getText().toString());
    }
    public LiveData<String> observeOTP(){
        return otp;
    }
}
