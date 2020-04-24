package com.farmerapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.farmerapp.Adapters.LanguageAdapter;
import com.example.farmerapp.R;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Toolbar toolbar;
    ImageView done;
    LanguageAdapter adapter;
    SharedPreferences preferences;
    String landCode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        done=findViewById(R.id.done);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        preferences=getSharedPreferences(getPackageName(),Context.MODE_PRIVATE);
        landCode=preferences.getString("langCode",null);
        adapter=new LanguageAdapter(this,landCode);
        recyclerView.setAdapter(adapter);
        if(landCode==null)
            updateDoneUI(false,0.3f);
        adapter.setOnItemClickListener(new LanguageAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position){
                updateDoneUI(true,1f);
                adapter.updateSelectedLang(position);
            }
        });

    }
    public void updateDoneUI(Boolean bool,Float alpha){
        done.setEnabled(bool);
        done.setAlpha(alpha);
    }

    @Override
    public void onBackPressed() {
        //On first time,user will have to select the language to go forward in app,but can perform back button if already selected earlier
        if(landCode==null)
            Toast.makeText(this, "Select Language", Toast.LENGTH_SHORT).show();
        else
            super.onBackPressed();
    }

    public void doneWork(View view){
        if(adapter.selectedIndex!=-1){
            SharedPreferences preferences=getSharedPreferences(getPackageName(),Context.MODE_PRIVATE);
            preferences.edit().putString("langCode",adapter.languageCodes.get(adapter.selectedIndex)).apply();
            setLocale(adapter.languageCodes.get(adapter.selectedIndex),this);
            if(landCode!=null) {
                Intent intent = new Intent(this, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }else{
                finish();
            }
        }
    }
    public static void setLocale(String langCode, Activity activity) {
        Locale locale;
        locale = new Locale(langCode);
        Configuration config = new Configuration(activity.getResources().getConfiguration());
        Locale.setDefault(locale);
        config.setLocale(locale);
        activity.getResources().updateConfiguration(config,activity.getResources().getDisplayMetrics());
    }
    public static void loadSavedLocale(Activity activity){
        SharedPreferences preferences=activity.getSharedPreferences(activity.getPackageName(),Context.MODE_PRIVATE);
        String landCode=preferences.getString("langCode",null);
        if(landCode!=null) {
            setLocale(landCode, activity);
        }else{
            Intent intent=new Intent(activity, LanguageActivity.class);
            activity.startActivity(intent);
        }
    }
}
