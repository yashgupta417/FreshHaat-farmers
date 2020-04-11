package com.example.farmerapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmerapp.Adapters.MainAdapter;
import com.example.farmerapp.R;
import com.example.farmerapp.Utils.LocationUtil;
import com.example.farmerapp.ViewModels.MainViewModel;
import com.example.farmerapp.ViewPager.MainViewPager;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawer;
    NavigationView navigationView;
    MainViewPager viewPager;
    MainAdapter adapter;
    MainViewModel viewModel;
    Toolbar toolbar;
    public static TextView addressTextView;
    public static ImageView locationSymbol;
    public static String FRUITS="Fruits";
    public static String VEGETABLES="Vegetables";
    public static String PRODUCT_TYPE="product_type";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        addressTextView=findViewById(R.id.address);
        locationSymbol=findViewById(R.id.loc);

        drawer=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setUpDrawer();
        activateNavigationHeader();
        viewModel= ViewModelProviders.of(this).get(MainViewModel.class);

    }
    public void setUpDrawer(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        viewPager=findViewById(R.id.view_pager);
        viewPager.disableScroll(true);
        adapter=new MainAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.home:viewPager.setCurrentItem(0,false);break;
            case R.id.requests:viewPager.setCurrentItem(1,false);break;
            case R.id.payments:viewPager.setCurrentItem(2,false);break;
            case R.id.logout:logout();break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void activateNavigationHeader(){
        View v=navigationView.getHeaderView(0);
        CircleImageView imageView=v.findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(intent);
               // drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void logout(){
        SharedPreferences preferences=getSharedPreferences(getPackageName(),Context.MODE_PRIVATE);
        preferences.edit().putBoolean(SplashActivity.IS_LOGGED_IN,false).apply();
        preferences.edit().putBoolean(SplashActivity.IS_REGISTRATION_DONE,false).apply();
        preferences.edit().putString(SplashActivity.TOKEN,null).apply();
        goToHome();
    }
    public void goToHome(){
        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public static void  showLocation(String locality,String subLocality){
        addressTextView.setVisibility(View.VISIBLE);
        locationSymbol.setVisibility(View.VISIBLE);
        addressTextView.setText(locality+","+subLocality);
    }
    public static void hideLocation(){
        addressTextView.setVisibility(View.INVISIBLE);
        locationSymbol.setVisibility(View.INVISIBLE);
    }
}
