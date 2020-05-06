package com.farmerapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.farmerapp.Data.Farmer;
import com.farmerapp.Fragments.MainFragments.HomeFragment;
import com.farmerapp.Fragments.MainFragments.PaymentFragment;
import com.farmerapp.Fragments.MainFragments.RequestFragment;
import com.example.farmerapp.R;
import com.farmerapp.Utils.LocalCart;
import com.farmerapp.Utils.LocationUtil;
import com.farmerapp.ViewModels.MainViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawer;
    NavigationView navigationView;
    MainViewModel viewModel;
    Toolbar toolbar;
    ShimmerFrameLayout shimmer;
    public static TextView title;
    public static TextView addressTextView;
    public static ImageView locationSymbol;
    public static String FRUITS="Fruits";
    public static String VEGETABLES="Vegetables";
    public static String PRODUCT_TYPE="product_type";
   // private Integer dialogCount=0;
    Snackbar snackbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageActivity.loadSavedLocale(this);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        addressTextView=findViewById(R.id.address);
        locationSymbol=findViewById(R.id.loc);
        shimmer=findViewById(R.id.shimmer);
        drawer=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.home).setChecked(true);
        title=findViewById(R.id.title);
        setUpDrawer();
        viewModel= ViewModelProviders.of(this).get(MainViewModel.class);
        activateNavigationHeader();
        registerReceiver(gpsReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        snackbar=Snackbar.make(drawer,getResources().getString(R.string.no_gps_message),Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(gpsReceiver);
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
            case R.id.change_language:changeLanguage();break;
            case R.id.about:goToAbout();break;
            case R.id.logout:logout();break;
        }
        if(fragment!=null)
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void goToAbout(){
        Intent intent=new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
    public void changeLanguage(){
        Intent intent=new Intent(this, LanguageActivity.class);
        startActivity(intent);
    }
    CircleImageView imageView;
    public void activateNavigationHeader(){
        View v=navigationView.getHeaderView(0);
        imageView=v.findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfile();
               // drawer.closeDrawer(GravityCompat.START);
            }
        });

    }
    public void loadProfileImage(){
        viewModel.getFarmer().observe(this, new Observer<Farmer>() {
            @Override
            public void onChanged(Farmer farmer) {
                if(farmer!=null && farmer.getProfilePhoto()!=null && !farmer.getProfilePhoto().equals("")){
                    Glide.with(MainActivity.this).load(farmer.getProfilePhoto()).placeholder(R.drawable.load_static).into(imageView);
                }
            }
        });
    }
    public void goToProfile(){
        Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
        startActivity(intent);
    }
    public void onClickLocation(View v){
        goToProfile();
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
        LocalCart.emptyCart(getApplication());
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

    @Override
    protected void onResume() {
        super.onResume();
        getPermissionAndGetLocation();
        loadProfileImage();

    }

    public void setLocation(String s){
        if(!s.isEmpty())
            addressTextView.setText(s);
        else
            addressTextView.setText(getResources().getString(R.string.main_location_empty));
    }
    public static void  showLocation(){
        addressTextView.setVisibility(View.VISIBLE);
        locationSymbol.setVisibility(View.VISIBLE);
    }
    public static void hideLocation(){
        addressTextView.setVisibility(View.INVISIBLE);
        locationSymbol.setVisibility(View.INVISIBLE);
    }
    private BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                //Toast.makeText(context, "gps toggled", Toast.LENGTH_SHORT).show();
                getPermissionAndGetLocation();
            }
        }
    };
    public void getLocation(){
        LocationUtil locationUtil=new LocationUtil(getApplication());
        if(locationUtil.isProviderEnabled()){
            setLocation(getResources().getString(R.string.locating));
            if(!shimmer.isShimmerStarted())
                shimmer.startShimmer();
            else
                shimmer.showShimmer(true);
            snackbar.dismiss();
            locationUtil.observeAddress().observe(this, new Observer<List<Address>>() {
                @Override
                public void onChanged(List<Address> addresses) {
                    String locality = addresses.get(0).getLocality();
                    String subLocality = addresses.get(0).getSubLocality();
                    String location="";
                    if(subLocality!=null)
                        location+=subLocality;
                    if(locality!=null) {
                        if(location.length()>0)
                            location+=",";
                        location+= locality;
                    }
                    setLocation(location);
                    shimmer.hideShimmer();
                }
            });
        }else{
            //if(dialogCount==1)
              //  return;
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    showError();
                }
            }, 2000);

        }
    }
    public void showError(){
        snackbar.show();
       // dialogCount=1;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

    private void getPermissionAndGetLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
                    }
                }, 5000);

            } else {
                getLocation();
            }
        } else {
            getLocation();
        }
    }
}
