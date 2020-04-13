package com.example.farmerapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.farmerapp.Fragments.MainFragments.HomeFragment;
import com.example.farmerapp.Fragments.MainFragments.PaymentFragment;
import com.example.farmerapp.Fragments.MainFragments.RequestFragment;
import com.example.farmerapp.R;
import com.example.farmerapp.ViewModels.MainViewModel;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawer;
    NavigationView navigationView;
    MainViewModel viewModel;
    Toolbar toolbar;
    public static TextView title;
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
        navigationView.getMenu().findItem(R.id.home).setChecked(true);
        title=findViewById(R.id.title);
        setUpDrawer();
        activateNavigationHeader();
        viewModel= ViewModelProviders.of(this).get(MainViewModel.class);

    }
    public static void setTitle(String s){
        title.setText(s);
    }
    public void setUpDrawer(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new HomeFragment()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment=null;
        switch (menuItem.getItemId()){
            case R.id.home:fragment=new HomeFragment();break;
            case R.id.requests:fragment=new RequestFragment();break;
            case R.id.payments:fragment=new PaymentFragment();break;
            case R.id.logout:logout();break;
        }
        if(fragment!=null)
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).commit();
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
            return;
        }
        if(navigationView.getMenu().findItem(R.id.home).isChecked()) {
            getSupportFragmentManager().popBackStack();
            super.onBackPressed();
        }else{
            navigationView.getMenu().findItem(R.id.home).setChecked(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new HomeFragment()).commit();
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
    public static void setLocation(String locality,String subLocality){
        addressTextView.setText(locality+","+subLocality);
    }
    public static void  showLocation(){
        addressTextView.setVisibility(View.VISIBLE);
        locationSymbol.setVisibility(View.VISIBLE);
    }
    public static void hideLocation(){
        addressTextView.setVisibility(View.INVISIBLE);
        locationSymbol.setVisibility(View.INVISIBLE);
    }
}
