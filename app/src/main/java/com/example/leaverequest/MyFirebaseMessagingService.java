package com.example.leaverequest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    Gson gson;
    public MyFirebaseMessagingService(){}

    @Override
    public void onCreate() {
        super.onCreate();
        gson = new Gson();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String,String> map = remoteMessage.getData();
        MessageObject object = new MessageObject();
        object.setFrom(map.get("f_from"));
        object.setReason(map.get("f_reason"));
        object.setToNumber(map.get("f_to_number"));
        object.setSenderId(map.get("f_sender_id"));
        object.setStatus(map.get("f_status"));
        object.setTill(map.get("f_till"));
        sendSMS(object);
    }

    @Override
    public void onNewToken(String s) {
        FirebaseDatabase.getInstance().getReference().child("reg").setValue(s);
        super.onNewToken(s);
    }

    public void sendSMS(MessageObject messageObject){
        String phone = messageObject.getToNumber();
        String student_id = messageObject.getSenderId();
        String from = messageObject.getFrom();
        String to = messageObject.getTill();
        String reason = messageObject.getReason();
        String mess_body = student_id+" from: "+from+" to: "+to;

        Log.e("Service", "sendSMS: "+phone);
        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone,null,mess_body,null,null);
            Log.e("Service", "sendSMS: Success");
        }
        catch (Exception e){
            Log.e("Service", "sendSMS: "+e.getMessage());
        }

    }

}
