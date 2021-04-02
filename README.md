#如影Push SDK Demo

一.添加SDK jar包
将对应版本的PushSDK.jar包拷贝到项目的app/lib目录下，并在app下的 build.gradle依赖对应jar包。

二.添加对应权限

需要在AndroidManifest.xml文件的manifest标签下添加对应权限：
#```
<permission
    android:name="com.knowin.pushdemo.permission.PUSH_RECEIVER"
    android:protectionLevel="signature" /> <!-- 这里com.knowin.pushdemo改成app的包名 -->
<uses-permission android:name="com.knowin.pushdemo.permission.PUSH_RECEIVER" /> <!-- 这里com.knowin.pushdemo改成app的包名 -->
<uses-permission android:name="android.permission.WAKE_LOCK" /> <!-- 由于SDK继承JobIntentService所以需要WAKE_LOCK权限 -->
#```
三.继承实现消息接收类

3.1编写广播继承KnowinPushWakefulReceiver

#```
public class PushMessageReceiver extends KnowinPushWakefulReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        ComponentName comp = new ComponentName(context.getPackageName(), KnowinPushIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, KnowinPushIntentService.class, (intent.setComponent(comp)));
    }
}
#```

3.2编写服务继承KnowinPushBaseIntentService

#```
public  class KnowinPushIntentService extends KnowinPushBaseIntentService {

    @Override
    protected void onMessage(Context context, int code, String content) {
        super.onMessage(context, code, content);
        //TODO 用于处理标准消息类型  code：服务器下发的对应code  content：对应的json消息
    }

    @Override
    protected void onMessage(Context context, String jsonStr) {
        super.onMessage(context, jsonStr);
        // TODO 用于非标准的自定义消息信息，自己处理整个JSON文件
    }
}
#```

3.3静态注册广播服务

在AndroidManifest.xml文件的application标签下注册响应的广播和服务

#```
<application>

         <!-- 接收推送消息的service -->
         <service
            android:name=".KnowinPushIntentService"
            android:permission="android.permission.BIND_JOB_SERVICE" />
        <!-- 接收推送消息的receiver -->
        <receiver
            android:name=".PushMessageReceiver"
            android:permission="com.knowin.pushservice.permission.SEND_PUSH">
            <intent-filter>
                <action android:name="com.knowin.pushservice.ACTION_RECEIVE_PUSH_MESSAGE" />
                <!-- category中com.knowin.pushdemo替换为app包名 -->
                <category android:name="com.knowin.pushdemo" />
            </intent-filter>
        </receiver>
    </application>
#```

四.项目注册（未实现,现阶段免注册，直接返回成功）

通过KnowinPushManager进行注册绑定Push 服务，进行广播接收。

#```
KnowinPushManager mPushManager = KnowinPushManager.getInstance(this);
//注册服务
String regID = mPushManager.register(APP_ID, APP_KEY);
//取消注册
boolean  status= mPushManager.unRegister(APP_ID, APP_KEY);
//订阅topic
boolean  status= mPushManager.subscribe(APP_ID, topic);
//取消订阅topic
boolean  status= mPushManager.unsubscribe(APP_ID, topic);
//查询订阅信息
String message = mPushManager.querySubscribe(APP_ID);
//用户绑定
boolean status=  mPushManager.bindUser(token);
//用户解绑
boolean status=  mPushManager.unbindUser();
#```

⚠️备注：

1.推荐参考sdk demo 集成，demo 地址：https://github.com/knowintech/insight-os/tree/develop_v1.2/PushServiceSDK

2.由于现阶段服务器返回的消息没有提供对应包名，需要开发人员配合集成人员代码里手动指定推送的应用包名。

3. 项目需要集成  implementation 'androidx.appcompat:appcompat:x.x.x'  库。