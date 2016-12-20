package com.keniobyte.bruino.minsegapp.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * SMS listener for users authentication.
 */
public class SmsListener extends BroadcastReceiver {

    final String TAG = this.getClass().getName();

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();//---get the SMS message passed in---
            SmsMessage[] msgs = null;

            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];

                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        String msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();


                        Intent local = new Intent();
                        local.setAction("com.minsegapp.SMSverification");
                        local.putExtra("verification_code", msgBody);
                        context.sendBroadcast(local);
                        break;
                    }
                } catch(Exception e){
                    //Log.d("Exception caught", e.getMessage());
                }
            }
        }
    }
}
