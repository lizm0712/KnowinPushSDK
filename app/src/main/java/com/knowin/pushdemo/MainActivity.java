
package com.knowin.pushdemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.knowin.pushsdk.KnowinPushManager;
import com.knowin.pushsdk.PushException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {
    private static final String TAG = "PushDemo";
    // 发邮件申请
    public static final String APP_ID = "appid_38c47c3fb1284ef48d0f2d9c08f06218";
    private static final String APP_KEY = "appkey_BhUOpWAmCxgSwQqzsqEk";

    private String token = "10210ed5f6WhOVm2U6m1m1X4pkFpICwG2y6huT2QfAWsRHPIUB4aaWvy9qUdwEm3Rm3m3YCnPPhP";
    // 订阅内容;由app拥有者在push服务器生成
    private String topic;

    private KnowinPushManager mPushManager;// 注册管理类
    private ArrayList<String> msgs = new ArrayList<String>();
    private EditText subscribeEdit;
    private TextView receiverText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mPushManager = KnowinPushManager.getInstance(this);
        setMessage(getIntent());
    }

    private void initView() {
        receiverText = (TextView) findViewById(R.id.receiver_message);
        subscribeEdit = (EditText) findViewById(R.id.subscribe_edit);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                msgs.clear();
                registerPush();
                break;
            case R.id.unregister:
                unregisterPush();
                break;
            case R.id.subscribe:
                subscribe();
                break;
            case R.id.unsubscribe:
                unsubscribe();
                break;
            case R.id.query_subscribe:
                querySubscribe();
                break;
            case R.id.pause:
                pause();
                break;
            case R.id.resume:
                resume();
                break;
            case R.id.get_app_state:
                getAppState();
                break;
            case R.id.bind_user:
                bindUser();
                break;
            case R.id.unbind_user:
                unbindUser();
                break;
            case R.id.testing_eco:
                Intent testIntent = new Intent();
                testIntent.putExtra("open_testing",true);
                testIntent.putExtra("testing_domain", "push.beta.scloud.letv.cn");
                changeEco(testIntent);
                break;
            case R.id.online_eco:
                Intent onLineIntent = new Intent();
                onLineIntent.putExtra("open_testing",false);
                changeEco(onLineIntent);
                break;
            default:
                break;
        }
    }

    private void changeEco(Intent intent) {
        ComponentName comp = new ComponentName(com.knowin.pushsdk.util.Constants.PUSH_PKG, com.knowin.pushsdk.util.Constants.PUSH_SERVICE_CLASS);
        intent.setComponent(comp);
        intent.putExtra(Constants.EXTRA_PUSH, Constants.EXTRA_VALUE_PUSH_CONTROLER);
        intent.putExtra(Constants.EXTRA_PKG, MainActivity.this.getPackageName());
        intent.putExtra(Constants.EXTRA_CTRL_ACTION, Constants.ACTION_CHANGE_ECO_STATE);
        startService(intent);
    }



    /**
     * 用户绑定
     */
    public void bindUser() {
        new Thread() {
            public void run() {
                String message;
                try {
                    mPushManager.bindUser(token);
                    message = "OK";
                } catch (PushException e) {
                    int code = e.getCode();
                    message = "error code : " + code + "  error info : " + e.getMessage();
                }
                setReceiverText("binduser:" + message);
            }
        }.start();
    }

    /**
     * 用户解绑
     */
    public void unbindUser() {
        new Thread() {
            public void run() {
                String message;
                try {
                    mPushManager.unbindUser();
                    message = "OK";
                } catch (PushException e) {
                    int code = e.getCode();
                    message = "error code : " + code + "  error info : " + e.getMessage();
                }
                setReceiverText("unBindUser:" + message);
            }
        }.start();
    }

    private void getAppState() {
        new Thread() {

            public void run() {
                int code;
                String message = "";
                try {
                    String state = mPushManager.getAppState(APP_ID);
                    message = state;
                } catch (PushException e) {
                    code = e.getCode();
                    message = "error code : " + code + "  error info : " + e.getMessage();
                }
                setReceiverText("APP STATE: " + message);
            };
        }.start();
    }

    private void resume() {
        new Thread() {

            public void run() {
                int code;
                String message = "";
                try {
                    boolean result = mPushManager.resume(APP_ID);
                    if (result) {
                        message = "resume success";
                    }
                } catch (PushException e) {
                    code = e.getCode();
                    message = "error code : " + code + "  error info : " + e.getMessage();
                }
                setReceiverText("RESUME: " + message);
            };
        }.start();
    }

    private void pause() {
        new Thread() {

            public void run() {
                int code;
                String message = "";
                try {
                    boolean result = mPushManager.pause(APP_ID);
                    if (result) {
                        message = "pause success";
                    }
                } catch (PushException e) {
                    code = e.getCode();
                    message = "error code : " + code + "  error info : " + e.getMessage();
                }
                setReceiverText("PAUSE: " + message);
            };
        }.start();
    }

    /**
     * 注册推送
     */
    public void registerPush() {
        new Thread() {

            public void run() {
                String result = null;
                try {
                    String regID = mPushManager.register(APP_ID, APP_KEY);
                    Log.i(TAG, "regId = " + regID);
                    result = "SUCCESS";
                } catch (PushException e) {
                    result = "error code : " + e.getCode() + "  error info : " + e.getMessage();
                }
                setReceiverText("REGISTER: " + result);
            };
        }.start();
    }

    /**
     * 取消注册推送
     */
    public void unregisterPush() {
        new Thread() {
            public void run() {
                String message;
                try {
                    mPushManager.unRegister(APP_ID, APP_KEY);
                    message = "SUCCESS";
                } catch (PushException e) {
                    message = "error code : " + e.getCode() + "  error info : " + e.getMessage();
                }
                setReceiverText("UNREGISTER: " + message);
            };
        }.start();
    }

    /**
     * 订阅
     */
    void subscribe() {
        topic = subscribeEdit.getText().toString();
        if (null == topic || "".equals(topic)) {
            Toast.makeText(this, "请输入订阅主题", Toast.LENGTH_LONG).show();
            return;
        }
        new Thread() {
            public void run() {
                String message;
                try {
                    mPushManager.subscribe(APP_ID, topic);
                    message = "SUCCESS";
                } catch (PushException e) {
                    int code = e.getCode();
                    message = "error code : " + code + "  error info : " + e.getMessage();
                }
                setReceiverText("subscribe:" + message);
            };
        }.start();
    }

    /**
     * 取消订阅
     */
    void unsubscribe() {
        topic = subscribeEdit.getText().toString();
        if (null == topic || "".equals(topic)) {
            Toast.makeText(this, "请输入取消订阅主题", Toast.LENGTH_LONG).show();
            return;
        }
        new Thread() {
            public void run() {
                String message = "SUCCESS";
                try {
                    mPushManager.unsubscribe(APP_ID, topic);
                } catch (PushException e) {
                    int code = e.getCode();
                    message = "error code : " + code + "  error info : " + e.getMessage();
                }
                setReceiverText("unsubscribe:" + message);
            };
        }.start();
    }

    /**
     * 查询
     */
    void querySubscribe() {
        new Thread() {
            public void run() {
                String message;
                try {
                    message = mPushManager.querySubscribe(APP_ID);
                } catch (PushException e) {
                    int code = e.getCode();
                    message = "error code : " + code + "  error info : " + e.getMessage();
                }
                setReceiverText("querySubscribe:" + message);
            };
        }.start();
    }

    private void setReceiverText(final String message) {
        receiverText.post(new Runnable() {

            @Override
            public void run() {
                receiverText.setText(message);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setMessage(intent);
    }

    void setMessage(Intent intent) {
        if ((getPackageName() + ".onMessage").equals(intent.getAction())) {
            String msg = "[" + currentTime() + "] " + intent.getStringExtra("message");
            if (msgs.size() == 10) {
                msgs.remove(9);
            }
            msgs.add(msg);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < msgs.size(); i++) {
                msg = msgs.get(i);
                builder.append(msg + "\n");
            }
            receiverText.setText(builder);
        }
    }

    private String currentTime() {
        Date time = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        return format.format(time);
    }

}
