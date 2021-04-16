package com.example.wisps;

import android.app.Fragment;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.wisps.ui.MainActivity;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class OneNetMqttService extends Service {

    private static final String TAG = OneNetMqttService.class.getSimpleName();

    /** 配置信息三元组 **/
    private static final String PRODUCT_ID = "n8Dqo8b7GC";
    private static final String DEVICE_NAME = "androidWithMqtt";
    private static final String TOKEN = "version=2018-10-31&res=products%2Fn8Dqo8b7GC%2Fdevices%2FandroidWithMqtt&et=1649486826&method=md5&sign=NQ3crqVMm1O6JE2r%2BcceTw%3D%3D";

    /** 消息主题 **/
    private static final String TOPIC = "$sys/n8Dqo8b7GC/androidWithMqtt/thing/property/set";

    /** 域名 **/
    private static final String host = "tcp://218.201.45.7:1883";

    private MqttAndroidClient mClient;

    private MqttConnectOptions mOptions;

    private MqttBinder mBinder = new MqttBinder();

    private OnAlarmListener mListener;

    public OneNetMqttService() {

    }

    /**
     * init the mClient and connect option
     */
    void init() {
        mOptions = new MqttConnectOptions();
        mOptions.setUserName(PRODUCT_ID);
        mOptions.setPassword(TOKEN.toCharArray());
        mOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        Log.d(TAG, "version: " + mOptions.getMqttVersion());

        mClient = new MqttAndroidClient(getApplicationContext(), host, DEVICE_NAME);

        mClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.i(TAG, "connection lost");
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.i(TAG, "topic: " + topic + ", msg: " + new String(message.getPayload()));
                String json = new String(message.getPayload());

                JsonObject jsonObject = (JsonObject) JsonParser.parseString(json);
                Log.d(TAG, jsonObject.toString());
                String id = jsonObject.get("id").getAsString();

                String version = jsonObject.get("version").getAsString();

                Boolean isAlarm = jsonObject.get("params").getAsJsonObject().get("isAlarm").getAsBoolean();


                Log.d(TAG, "id: " + id + " " + "version: " + version + " " + "isAlarm: " + isAlarm);

                if (isAlarm) {
                    if (mListener != null) {
                        mListener.onAlarm();
                    } else {
                        Log.i(TAG, "no set for OnAlarmListener");
                    }
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.i(TAG, "msg delivered");
            }
        });

        connect();
    }

    private void subscribeTopic(String topic) {
        try {
            mClient.subscribe(TOPIC, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "subscribed succeed");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG, "subscribed failed");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void publishMessage(String payload) {
        try {
            if (!mClient.isConnected()) {
                connect();
            }

            MqttMessage message = new MqttMessage();
            message.setPayload(payload.getBytes());
            message.setQos(0);
            mClient.publish(TOPIC, message, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "publish succeed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG, "publish failed!");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        try {
            mClient.connect(mOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "connected succeed");
                    subscribeTopic(TOPIC);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG, "connected failed");
                    Log.e(TAG, "code: " + ((MqttException) exception).getReasonCode());
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MqttBinder extends Binder {
        public void publish(String payload) {
            publishMessage(payload);
        }
        public OneNetMqttService getService() {
            return OneNetMqttService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        init();
        startForeground(1, new Notification());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mClient.isConnected()) mClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void setOnAlarmListener(OnAlarmListener onAlarmListener) {
        this.mListener = onAlarmListener;
    }

    public interface OnAlarmListener {
        public void onAlarm();
    }
}