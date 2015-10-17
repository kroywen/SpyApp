package com.spyapp.android;

import android.app.Application;
import android.util.Log;

public class SpyApp extends Application {

    public static final String TAG = SpyApp.class.getSimpleName();

    public static void log(String message) {
        log(TAG, message);
    }

    public static void log(String tag, String message) {
        Log.d(tag, message);
    }

}
