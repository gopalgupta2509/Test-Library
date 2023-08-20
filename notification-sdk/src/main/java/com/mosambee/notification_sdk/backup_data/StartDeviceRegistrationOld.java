package com.mosambee.notification_sdk.backup_data;

import android.content.Context;

import com.mosambee.notification_sdk.callback_listener.NotificationListener;
import com.mosambee.notification_sdk.device_registration.DeviceRegistration;
import com.mosambee.notification_sdk.model.MqttRequestData;

public class StartDeviceRegistrationOld {

    private Context context;
    private volatile static StartDeviceRegistrationOld startDeviceRegistration;

    public StartDeviceRegistrationOld(Context context) {
        this.context = context;
    }

    public static StartDeviceRegistrationOld getInstance(Context context) {
        if (startDeviceRegistration == null) {
            synchronized (StartDeviceRegistrationOld.class) {
                if (startDeviceRegistration == null)
                    startDeviceRegistration = new StartDeviceRegistrationOld(context);
            }
        }
        return startDeviceRegistration;
    }

    public void registerNotificationService(NotificationListener checkListCallback, MqttRequestData mqttRequestData) {
        DeviceRegistration.Companion.getInstance(context, checkListCallback).deviceRegistration(mqttRequestData);
    }
}
