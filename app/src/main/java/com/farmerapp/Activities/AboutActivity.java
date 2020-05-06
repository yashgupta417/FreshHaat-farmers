package com.farmerapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.farmerapp.BuildConfig;
import com.example.farmerapp.R;

public class AboutActivity extends AppCompatActivity {
    TextView version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        version=findViewById(R.id.version);
        setVersion();
    }
    public void setVersion(){
        String versionName = BuildConfig.VERSION_NAME;
        version.setText("v"+versionName);
    }
    public void onBack(View v){
        finish();
    }
}
