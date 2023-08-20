package com.mosambee.notification_sdk.device_registration

import android.annotation.SuppressLint
import android.content.Context
import com.mosambee.notification_sdk.callback_listener.NotificationListener
import com.mosambee.notification_sdk.device_registration.DeviceRegistration.Companion.getInstance
import com.mosambee.notification_sdk.model.MqttRequestData

class StartDeviceRegistration(private val context: Context) {
    fun registerNotificationService(
        checkListCallback: NotificationListener?,
        mqttRequestData: MqttRequestData?
    ) {
        getInstance(context, checkListCallback)!!
            .deviceRegistration(mqttRequestData!!)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var startDeviceRegistration: StartDeviceRegistration? = null
        fun getInstance(context: Context): StartDeviceRegistration? {
            if (startDeviceRegistration == null) {
                synchronized(StartDeviceRegistration::class.java) {
                    if (startDeviceRegistration == null) startDeviceRegistration =
                        StartDeviceRegistration(context)
                }
            }
            return startDeviceRegistration
        }
    }
}