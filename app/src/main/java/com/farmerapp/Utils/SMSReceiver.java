package com.farmerapp.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;


public class SMSReceiver extends BroadcastReceiver{
    public static OnOTPReceivedListener otpReceivedListener;
    @Override
    public void onReceive(Context context, Intent intent){
        Bundle data  = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        for(int i=0;i<pdus.length;i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String message=smsMessage.getMessageBody();
            if(message!=null && message.length()>13) {
                String sender = message.substring(5, 13);
                String otp = message.replaceAll("\\D", "");
                if (sender != null && sender.toLowerCase().equals("freshhut") && otp != null && otp.trim().length() == 5) {
                    otpReceivedListener.messageReceived(otp);
                }
            }
        }
    }
    public interface OnOTPReceivedListener{
        public void messageReceived(String messageText);
    }
    public static void setOtpReceivedListener(OnOTPReceivedListener listener) {
        otpReceivedListener=listener;
    }
}
