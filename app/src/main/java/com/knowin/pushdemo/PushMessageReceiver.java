
package com.knowin.pushdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.knowin.pushsdk.KnowinPushWakefulReceiver;


public class PushMessageReceiver extends KnowinPushWakefulReceiver {
    private static final String TAG = "PushDemo";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onHandleIntent:action=" + action);
        ComponentName comp = new ComponentName(context.getPackageName(), KnowinPushIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, KnowinPushIntentService.class, (intent.setComponent(comp)));
    }
}
