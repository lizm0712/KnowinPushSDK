
package com.knowin.pushdemo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.knowin.pushsdk.KnowinPushBaseIntentService;


public  class KnowinPushIntentService extends KnowinPushBaseIntentService {
    private static final String TAG = "PushDemo";


    @Override
    protected void onMessage(Context context, int code, String content) {
        super.onMessage(context, code, content);
        Log.i(TAG, "message=" + code + ",msgId=" + content);
        updateContent(context, content);
    }

    @Override
    protected void onMessage(Context context, String jsonStr) {
        super.onMessage(context, jsonStr);
    }

    private void updateContent(final Context context, final String responseString) {
        Intent intent2 = new Intent(this, MainActivity.class);
        intent2.setAction(getPackageName() + ".onMessage");
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent2.putExtra("message", responseString);
        startActivity(intent2);

    }
}
