package com.farmerapp.Fragments.RegistrationFragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.farmerapp.Activities.RegisterDetailsActivity;
import com.example.farmerapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class RegisterPhotoFragment extends Fragment {
    public RegisterPhotoFragment() {
        // Required empty public constructor
    }
    ImageView chooseImageButton;
    CircleImageView profileImage;
    Button next;
    Uri image;
    TextView skip;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=  inflater.inflate(R.layout.fragment_reg_photo, container, false);
        chooseImageButton=v.findViewById(R.id.upload);
        profileImage=v.findViewById(R.id.photo);
        next=v.findViewById(R.id.next);
        skip=v.findViewById(R.id.skip);
        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterDetailsActivity)getActivity()).scrollPager(1);
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RegisterDetailsActivity)getActivity()).scrollPager(1);
            }
        });

        //Small hack!but should not be needed...
        if(RegisterDetailsActivity.viewModel.image!=null){
            updateUI(RegisterDetailsActivity.viewModel.image,true,1f,false,0f);
        }
        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==2){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null){
            image=data.getData();
            if(image!=null){
                RegisterDetailsActivity.viewModel.image=image;
                updateUI(image,true,1f,false,0f);
            }
        }
    }
    public void updateUI(Uri image,Boolean nextBool,Float nextAlpha,Boolean skipBool,Float skipAlpha){
        Glide.with(getActivity()).load(image).into(profileImage);
        next.setEnabled(nextBool);
        next.setAlpha(nextAlpha);
        skip.setAlpha(skipAlpha);
        skip.setEnabled(skipBool);
    }
    public void chooseImage(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
            }else{
                Intent intent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        }else{
            Intent intent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,1);
        }
    }

}
