package com.mosambee.notification_sdk.backup_data;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.mosambee.notification_sdk.enumeration.RequestAction;

public class MqttRequestDataOld {

    private String deviceId;
    private String latitude;
    private String longitude;
    String deviceRegistrationMode;
    private RequestAction requestAction;
    private String environment = "";

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAppVersion(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (final PackageManager.NameNotFoundException e) {
            return "NA";
        }
    }

    public void setDeviceRegistrationMode(String deviceRegistrationMode) {
        this.deviceRegistrationMode = deviceRegistrationMode;
    }

    public String getDeviceRegistrationMode() {
        return deviceRegistrationMode;
    }

    public String getDeviceMake() {
        return Build.MANUFACTURER;
    }

    public RequestAction getRequestAction() {
        return requestAction;
    }

    public void setRequestAction(RequestAction requestAction) {
        this.requestAction = requestAction;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

}
