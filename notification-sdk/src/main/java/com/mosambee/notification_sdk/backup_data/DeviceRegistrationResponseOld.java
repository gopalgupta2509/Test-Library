package com.mosambee.notification_sdk.backup_data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceRegistrationResponseOld {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responseCode")
    @Expose
    private String responseCode;
    @SerializedName("deviceSerialNo")
    @Expose
    private String deviceSerialNo;
    @SerializedName("notificationDetails")
    @Expose
    private NotificationDetails notificationDetails;
    private String notificationURL;
    private boolean deviceRegistered =false;

    public String getNotificationURL() {
        return notificationURL;
    }

    public void setNotificationURL(String notificationURL) {
        this.notificationURL = notificationURL;
    }

    public boolean isDeviceRegistered() {
        return deviceRegistered;
    }

    public void setDeviceRegistered(boolean deviceRegistered) {
        this.deviceRegistered = deviceRegistered;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getDeviceSerialNo() {
        return deviceSerialNo;
    }

    public void setDeviceSerialNo(String deviceSerialNo) {
        this.deviceSerialNo = deviceSerialNo;
    }

    public NotificationDetails getNotificationDetails() {
        return notificationDetails;
    }

    public void setNotificationDetails(NotificationDetails notificationDetails) {
        this.notificationDetails = notificationDetails;
    }

    public String getEnvNotificationConnect(String environment) {
        return environment.equalsIgnoreCase("TEST") ? "tcp://uat.notifypro.in:1883" : "ssl://notifypro.in:8883";
    }

    public class NotificationDetails {

        @SerializedName("cryptoKey")
        @Expose
        private String cryptoKey;
        @SerializedName("cryptoNonce")
        @Expose
        private String cryptoNonce;
        @SerializedName("mqttQos")
        @Expose
        private String mqttQos;
        @SerializedName("mqttRetainFlag")
        @Expose
        private String mqttRetainFlag;
        @SerializedName("mqttCleanSession")
        @Expose
        private String mqttCleanSession;
        @SerializedName("mqttUserName")
        @Expose
        private String mqttUserName;
        @SerializedName("mqttClientId")
        @Expose
        private String mqttClientId;
        @SerializedName("mqttSubscribeTopic")
        @Expose
        private String mqttSubscribeTopic;

        public String getCryptoKey() {
            return cryptoKey;
        }

        public void setCryptoKey(String cryptoKey) {
            this.cryptoKey = cryptoKey;
        }

        public String getCryptoNonce() {
            return cryptoNonce;
        }

        public void setCryptoNonce(String cryptoNonce) {
            this.cryptoNonce = cryptoNonce;
        }

        public String getMqttQos() {
            return mqttQos;
        }

        public void setMqttQos(String mqttQos) {
            this.mqttQos = mqttQos;
        }

        public String getMqttRetainFlag() {
            return mqttRetainFlag;
        }

        public void setMqttRetainFlag(String mqttRetainFlag) {
            this.mqttRetainFlag = mqttRetainFlag;
        }

        public String getMqttCleanSession() {
            return mqttCleanSession;
        }

        public void setMqttCleanSession(String mqttCleanSession) {
            this.mqttCleanSession = mqttCleanSession;
        }

        public String getMqttUserName() {
            return mqttUserName;
        }

        public void setMqttUserName(String mqttUserName) {
            this.mqttUserName = mqttUserName;
        }

        public String getMqttClientId() {
            return mqttClientId;
        }

        public void setMqttClientId(String mqttClientId) {
            this.mqttClientId = mqttClientId;
        }

        public String getMqttSubscribeTopic() {
            return mqttSubscribeTopic;
        }

        public void setMqttSubscribeTopic(String mqttSubscribeTopic) {
            this.mqttSubscribeTopic = mqttSubscribeTopic;
        }

    }
}
