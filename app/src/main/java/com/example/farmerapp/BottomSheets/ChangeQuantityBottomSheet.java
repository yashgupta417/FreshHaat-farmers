package com.example.farmerapp.BottomSheets;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.farmerapp.Data.Crop;
import com.example.farmerapp.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.santalu.maskedittext.MaskEditText;

public class ChangeQuantityBottomSheet extends BottomSheetDialogFragment {
    Crop crop;
    public ChangeQuantityBottomSheet(Crop crop) {
        this.crop=crop;
    }
    ImageView image;
    Button done;
    TextView name,price;
    EditText quantity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_change_quantity, container, false);
        image=v.findViewById(R.id.image);
        done=v.findViewById(R.id.done);
        name=v.findViewById(R.id.name);
        price=v.findViewById(R.id.price);
        quantity=v.findViewById(R.id.quantity);
        setUI();
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                View bottomSheetInternal = d.findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDone(Integer.parseInt(quantity.getText().toString()));
                dismiss();
            }
        });
        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().isEmpty()){
                    updateButtonStatus(false,0.3f);
                }else{
                    updateButtonStatus(true,1f);
                }
            }
        });
        return v;
    }
    public void updateButtonStatus(Boolean enabled,Float alpha){
        done.setEnabled(enabled);
        done.setAlpha(alpha);
    }
    private OnDoneListener mListener;
    public interface OnDoneListener{
        void onDone(Integer quantity);
    }
    public void setOnDoneListener(OnDoneListener listener){
        mListener=listener;
    }
    public void setUI(){
        if(crop.getImage()!=null)
            Glide.with(this).load(crop.getImage()).into(image);
        name.setText(crop.getName().substring(0,1).toUpperCase()+crop.getName().substring(1).toLowerCase());
        price.setText(getResources().getString(R.string.Rs)+" "+Float.toString(crop.getPrice())+" per "+crop.getUnit());
        quantity.setText(Integer.toString(crop.getQuantity()));
    }
}
