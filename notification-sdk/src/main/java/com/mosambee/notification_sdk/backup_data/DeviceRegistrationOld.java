package com.mosambee.notification_sdk.backup_data;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.mosambee.networkservice.networkcall.entity.ErrorCode;

import com.mosambee.networkservice.networkcall.network.NetworkCallControllerT;
import com.mosambee.networkservice.networkcall.network.listener.NetworkCallResponse;
import com.mosambee.notification_sdk.callback_listener.NotificationListener;
import com.mosambee.notification_sdk.enumeration.RequestAction;
import com.mosambee.notification_sdk.enumeration.ResultAction;
import com.mosambee.notification_sdk.model.DeviceRegistrationResponse;
import com.mosambee.notification_sdk.model.MqttRequestData;
import com.mosambee.notification_sdk.model.RegisterDevice;
import com.mosambee.notification_sdk.notification_connection.NotifificationConnection;
import com.mosambee.notification_sdk.utils.AESEncryptDecrypt;
import com.mosambee.notification_sdk.utils.AppConstant;
import com.mosambee.notification_sdk.utils.HMACUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DeviceRegistrationOld implements NetworkCallResponse {

    private static DeviceRegistrationOld deviceRegistration;
    private static Gson gson;
    private Context context;
    private NotificationListener notificationListener;
    private MqttRequestData requestData;

    protected DeviceRegistrationOld(Context context, NotificationListener callback) {
        this.context = context;
        this.notificationListener = callback;
    }

    protected static DeviceRegistrationOld getInstance(Context context, NotificationListener callback) {
        if (deviceRegistration == null) {
            synchronized (DeviceRegistrationOld.class) {
                if (deviceRegistration == null)
                    deviceRegistration = new DeviceRegistrationOld(context, callback);
                gson = new Gson();
            }
        }
        return deviceRegistration;
    }

    protected void deviceRegistration(MqttRequestData requestData) {
        try {
            this.requestData = requestData;
            RegisterDevice registerDevice = new RegisterDevice();
            RegisterDevice.LocationDetail locationDetail = new RegisterDevice.LocationDetail();
            RegisterDevice.LatlongDetail latLongDetail = new RegisterDevice.LatlongDetail();

            registerDevice.setDeviceSerialNo(requestData.getDeviceId());
            registerDevice.setDeviceCategory(registerDevice.getDeviceCategory());
            registerDevice.setDeviceRegistrationMode(requestData.getDeviceRegistrationMode());
            registerDevice.setServerApiVersion(registerDevice.getServerApiVersion());
            registerDevice.setDeviceMake(requestData.getDeviceMake());
            registerDevice.setDeviceModel(registerDevice.getDeviceModel());
            registerDevice.setDevicePlatform(registerDevice.getDevicePlatform());
            registerDevice.setDeviceAppVersion(requestData.getAppVersion(context));

            locationDetail.setDetailCategory(locationDetail.getDetailCategory());

            latLongDetail.setLatitude(null != requestData.getLatitude() && !requestData.getLatitude().isEmpty() ? requestData.getLatitude() : "0.0000000");
            latLongDetail.setLongitude(null != requestData.getLongitude() && !requestData.getLongitude().isEmpty() ? requestData.getLongitude() : "0.0000000");

            locationDetail.setLatlongDetail(latLongDetail);
            registerDevice.setLocationDetail(locationDetail);

            String data  = AESEncryptDecryptOld.getAESDecryptedString(AppConstantOld.TEST);
            String jsonStr = gson.toJson(registerDevice);
            JSONObject jsonObject = new JSONObject(jsonStr);
            requestData.setRequestAction(RequestAction.DEVICE_REGISTRATION);
            String hmacValue = new HMACUtil().calculateHMAC(AppConstantOld.HMAC_DATA.HMAC_KEY, jsonObject.toString());

            /*call network services */
            Map<String, String> headerData = new HashMap<>();
            headerData.put("apiClientRef", "MOSAMBEE");
            headerData.put("hmac", hmacValue);
            NetworkCallControllerT.getInstance(context).startAPIService(this, registerDevice.getEnvDeviceRegistration(requestData.getEnvironment()), String.valueOf(12223), true, jsonObject, headerData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailed(@NonNull ErrorCode errorCode) {
        Log.e("errorCode::::::::", errorCode.toString());
        notificationListener.onFailed(ResultAction.DEVICE_REGISTRATION_FAILED, errorCode + " - Device registration failed");
    }

    @Override
    public void onSuccess(@NonNull String response, @NonNull String s1) {
        Log.e("onSuccess::::::::", response + "\n" + s1);
        try {

            DeviceRegistrationResponse registrationResponse = gson.fromJson(response, DeviceRegistrationResponse.class);
            if ("Success".equalsIgnoreCase(registrationResponse.getResult())) {
                registrationResponse.setDeviceRegistered(true);
                registrationResponse.setNotificationURL(requestData.getEnvironment());
                startNotificationConnection(registrationResponse);

            } else {
                notificationListener.onFailed(ResultAction.DEVICE_REGISTRATION_FAILED, registrationResponse.getMessage());
            }
        } catch (Exception err) {
            notificationListener.onFailed(ResultAction.DEVICE_REGISTRATION_FAILED, "Device registration failed");
        }
    }

    private void startNotificationConnection(DeviceRegistrationResponse registrationResponse) {
        NotifificationConnection notifificationConnection = NotifificationConnection.Companion.getInstance();
        NotifificationConnection.Companion.buildMqttClient(context, notificationListener, registrationResponse);
        Intent mServiceIntent = new Intent(context, notifificationConnection.getClass());
        try {
            if (isMyServiceRunning(mServiceIntent.getClass())) {
                context.stopService(mServiceIntent);
                Thread.sleep(1000);
                context.startService(mServiceIntent);
            } else {
                TRACEOld.d("Service__Running::::::::Start:" + isMyServiceRunning(mServiceIntent.getClass()));
                context.startService(mServiceIntent);
            }
        } catch (Exception e) {
            TRACEOld.d("Service_Exception:::::::::" + e.getMessage());
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                TRACEOld.d("Service status Running");
                return true;
            }
        }
        TRACEOld.d("Service status:::::" + "Not running");
        return false;
    }
}
