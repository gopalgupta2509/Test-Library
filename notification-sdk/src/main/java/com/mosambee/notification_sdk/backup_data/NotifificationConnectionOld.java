package com.mosambee.notification_sdk.backup_data;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.mosambee.notification_sdk.callback_listener.NotificationListener;
import com.mosambee.notification_sdk.enumeration.ResultAction;
import com.mosambee.notification_sdk.model.DeviceRegistrationResponse;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import info.mqtt.android.service.Ack;
import info.mqtt.android.service.MqttAndroidClient;

public final class NotifificationConnectionOld extends Service {

    public NotifificationConnectionOld() {
    }

    private static String topic;
    private static String mqttQos;
    private static String cleanSession;
    private static String mqttUsername;
    private static String mqttClientId = "";

    private MqttAndroidClient mqttAndroidClient = null;

    private static String serverUri = "";
    private static Context context;
    static NotificationListener responseListener;
    private static NotifificationConnectionOld notifificationConnection;

    public static NotifificationConnectionOld getInstance() {
        if (notifificationConnection == null) {
            synchronized (NotifificationConnectionOld.class) {
                if (notifificationConnection == null)
                    notifificationConnection = new NotifificationConnectionOld();
            }
        }
        return notifificationConnection;
    }


    public static synchronized void buildMqttClient(Context mcontext, NotificationListener notificationResponseListener, DeviceRegistrationResponse requestData) {
        context = mcontext;
        responseListener = notificationResponseListener;
        serverUri = requestData.getEnvNotificationConnect(requestData.getNotificationURL());
        topic = requestData.getNotificationDetails().getMqttSubscribeTopic();
        mqttQos = requestData.getNotificationDetails().getMqttQos();
        cleanSession = requestData.getNotificationDetails().getMqttCleanSession();
        mqttUsername = requestData.getNotificationDetails().getMqttUserName();
        mqttClientId = requestData.getNotificationDetails().getMqttClientId();
    }

    /*MQTT Subscribe topic*/
    private static void subscribeToTopic(MqttAndroidClient client, String topic, String mqttQos) {

        try {
            TRACEOld.d("SUBSCRIBE_TOPIC:::::: " + topic);
            IMqttToken subToken = client.subscribe(topic, Integer.parseInt(mqttQos));
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    responseListener.onSuccess("Notification Connected Successfully!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    TRACEOld.d("MQTT_SUBSCRIBE:::onFailure::: " + exception.getMessage());
                    responseListener.onFailed(ResultAction.SUBSCRIBE_TO_TOPIC_FAILED, "Failed to subscribe to the topic");
                }
            });
        } catch (Exception exception) {
            TRACEOld.d("MQTT_SUBSCRIBE:::Exception::: " + exception.getMessage());
            responseListener.onFailed(ResultAction.SOMETHING_WENT_WRONG, "Failed to subscribe to the topic");
        }
    }

    /*Build MQTT user passowrd basis host pre-defined pattern*/
    private static String getPassword(String deviceSrNo) {
        StringBuilder revDeviceSrNo = new StringBuilder().append(deviceSrNo).reverse();
        return "Mq@".concat(revDeviceSrNo.toString()).concat("#Mos");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TRACEOld.d("onStartCommand methode called");
        Runnable r = new Runnable() {
            @Override
            public void run() {
                initNotification();
            }
        };
        Thread backgroundThread = new Thread(r);
        backgroundThread.start();
        return Service.START_STICKY;
    }

    private void initNotification() {
        try {
            if (mqttAndroidClient!=null){
                mqttAndroidClient.close();
            }

            if (mqttAndroidClient == null) {
                MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
                mqttConnectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
                mqttConnectOptions.setCleanSession(cleanSession.equalsIgnoreCase("1") ? true : false);
                mqttConnectOptions.setMaxInflight(100);
                mqttConnectOptions.setAutomaticReconnect(true);
                mqttConnectOptions.setKeepAliveInterval(60);
                mqttConnectOptions.setConnectionTimeout(30);
                mqttConnectOptions.setUserName(mqttUsername);
                mqttConnectOptions.setPassword(getPassword(mqttClientId).toCharArray());

                TRACEOld.d("mqttConnectOptions_Object::::::::::::" + mqttConnectOptions);

                MqttAndroidClient client = new MqttAndroidClient(context.getApplicationContext(), serverUri, mqttClientId, Ack.AUTO_ACK);

                client.setCallback(new MqttCallbackExtended() {
                    @Override
                    public void connectComplete(boolean reconnect, String serverURI) {
                        TRACEOld.d("MQTT::::::CALLBACK:::CONNECTION_COMPLETE::: " + reconnect + " :: " + mqttAndroidClient.toString());
                        subscribeToTopic(mqttAndroidClient, topic, mqttQos);
                    }

                    @Override
                    public void connectionLost(Throwable cause) {
                        TRACEOld.d("connectionLost::: " + cause.getMessage());
                        responseListener.onFailed(ResultAction.CONNECTION_LOST, "Connection lost");
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        try {
                            JSONObject jsonObject = new JSONObject(message.toString());
                            responseListener.onMessageArrived(jsonObject);
                            TRACEOld.d("messageArrived:::" + message);
                        } catch (Exception exception) {
                            responseListener.onFailed(ResultAction.SOMETHING_WENT_WRONG, "Message arrived failed");
                        }
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        TRACEOld.d("MQTT_CALLBACK::::::DELIVERY:::" + token);
                    }
                });

                client.connect(mqttConnectOptions);
                TRACEOld.d("AFTER_MQTT_CONNECT_METHOD_CALL::::::::::");
                mqttAndroidClient = client;
                TRACEOld.d("MQTT_CONNECTION_OBJECT_CREATED:::::::::" + mqttAndroidClient.toString());

            } else {
                responseListener.onSuccess("Notification Already Connected!!");
                TRACEOld.d("MQTT_CONNECTION_OBJECT_ALREADY_EXIST:::::::" + mqttAndroidClient.toString());
            }

        } catch (Exception e) {
            responseListener.onFailed(ResultAction.NOTIFICATION_CONNECTION_FAILED, "Notification connection failed!");
            TRACEOld.d("EXCEPTION_IN_MQTT_CONNECTION::::::::::" + e.getMessage());
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        TRACEOld.d("onDestroy methode called from service");
    }
}
