package com.example.leaverequest;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class SMSreceiver extends BroadcastReceiver {

    private static final String TAG =  SMSreceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";

    private DatabaseReference reference;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        reference = FirebaseDatabase.getInstance().getReference();
        SmsMessage[] sms;
        String[] smsBody;
        String strMessage = "";
        String format = bundle.getString("format");

        String uid,pnum,status;
        Object[] pds = (Object[])bundle.get(pdu_type);

        if(pds!=null){
            boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
            sms = new SmsMessage[pds.length];
            for (int i=0; i < pds.length; i++) {
                if(isVersionM)
                    sms[i] = SmsMessage.createFromPdu((byte[])pds[i], format);
                else
                    sms[i] = SmsMessage.createFromPdu((byte[])pds[i]);

                pnum = sms[i].getOriginatingAddress();
                strMessage = sms[i].getMessageBody();

                smsBody = strMessage.split(" ");
                if(smsBody.length==2){
                    uid = smsBody[0];
                    status = smsBody[1];
                    if(status.equalsIgnoreCase("y"))
                        status = "1";
                    else if(status.equalsIgnoreCase("n"))
                        status = "-1";
                    else
                        status = "0";
                    if(pnum.contains("+91"))
                        pnum = pnum.substring(3);
                    setStatus(uid,pnum,status);

                }
            }
        }
        Log.e(TAG, "onReceive: " + strMessage);
    }

    public void setStatus(final String sid,final String pnum,final String status){

        reference.child("data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Gson gson = new Gson();
                for (DataSnapshot kid: dataSnapshot.getChildren()){
                    Log.e(TAG, "onDataChange: "+kid.toString());
                    MessageObject object = new MessageObject();
                    object.setFrom(kid.child("f_from").getValue(String.class));
                    object.setStatus(kid.child("f_status").getValue(String.class));
                    object.setTill(kid.child("f_till").getValue(String.class));
                    object.setSenderId(kid.child("f_sender_id").getValue(String.class));
                    object.setToNumber(kid.child("f_to_number").getValue(String.class));
                    object.setReason(kid.child("f_reason").getValue(String.class));
                    Log.e("Sms", "onDataChange: "+sid+" "+pnum);
                    Log.e("Sms", "onDataChange: "+gson.toJson(object));
                    if(object.getStatus().equalsIgnoreCase("0") &&
                        object.getSenderId().equalsIgnoreCase(sid) &&
                        object.getToNumber().equalsIgnoreCase(pnum)) {
                        reference.child("data").child(kid.getKey()).child("f_status").setValue(status);
                        Log.e("Sms", "setStatus: "+sid+" status"+status);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
