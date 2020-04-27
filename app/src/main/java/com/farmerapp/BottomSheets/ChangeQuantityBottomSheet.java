package com.farmerapp.BottomSheets;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.farmerapp.Data.Crop;
import com.example.farmerapp.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ChangeQuantityBottomSheet extends BottomSheetDialogFragment {
    Crop crop;
    public ChangeQuantityBottomSheet(Crop crop) {
        this.crop=crop;
    }
    ImageView image;
    Button update,remove;
    TextView name,price,offerPrice,unit,min,max;
    EditText quantity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_change_quantity, container, false);
        image=v.findViewById(R.id.image);
        update=v.findViewById(R.id.update);
        remove=v.findViewById(R.id.remove);
        name=v.findViewById(R.id.name);
        price=v.findViewById(R.id.price);
        offerPrice=v.findViewById(R.id.offer_price);
        unit=v.findViewById(R.id.unit);
        quantity=v.findViewById(R.id.quantity);
        min=v.findViewById(R.id.min);
        max=v.findViewById(R.id.max);
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                View bottomSheetInternal = d.findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDone(Integer.parseInt(quantity.getText().toString().trim()));
                dismiss();
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDone(0);
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
                if(s.toString().trim().length()>0 &&
                        Integer.parseInt(s.toString().trim())>=crop.getMinQuantity() &&
                        Integer.parseInt(s.toString().trim())<=crop.getMaxQuantity()){
                    updateButtonStatus(true,1f);
                }else{
                    updateButtonStatus(false,0.3f);
                }
            }
        });
        setUI();
        return v;
    }
    private OnDoneListener mListener;
    public interface OnDoneListener{
        void onDone(Integer quantity);
    }
    public void updateButtonStatus(Boolean enabled,Float alpha){
        update.setEnabled(enabled);
        update.setAlpha(alpha);
    }
    public void setOnDoneListener(OnDoneListener listener){
        mListener=listener;
    }
    public void setUI(){
        if(crop.getImage()!=null)
            Glide.with(this).load(crop.getImage()).into(image);
        name.setText(crop.getName().substring(0,1).toUpperCase()+crop.getName().substring(1).toLowerCase());
        price.setText(getResources().getString(R.string.Rs)+Float.toString(crop.getPrice()));
        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        unit.setText(crop.getBatchSize()+" "+crop.getUnit());
        offerPrice.setText(getResources().getString(R.string.Rs)+Float.toString(crop.getOfferPrice()));
        quantity.setText(Integer.toString(crop.getQuantity()));
        min.setText("min: "+crop.getMinQuantity());
        max.setText("max: "+crop.getMaxQuantity());
    }
}
