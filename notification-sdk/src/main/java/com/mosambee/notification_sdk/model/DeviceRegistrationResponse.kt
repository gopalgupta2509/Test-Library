package com.mosambee.notification_sdk.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DeviceRegistrationResponse {

    @SerializedName("result")
    @Expose
    private var result: String? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null

    @SerializedName("responseCode")
    @Expose
    private var responseCode: String? = null

    @SerializedName("deviceSerialNo")
    @Expose
    private var deviceSerialNo: String? = null

    @SerializedName("notificationDetails")
    @Expose
    private var notificationDetails: NotificationDetails? = null
    private var notificationURL: String? = null
    private var deviceRegistered = false

    fun getNotificationURL(): String? {
        return notificationURL
    }

    fun setNotificationURL(notificationURL: String?) {
        this.notificationURL = notificationURL
    }

    fun isDeviceRegistered(): Boolean {
        return deviceRegistered
    }

    fun setDeviceRegistered(deviceRegistered: Boolean) {
        this.deviceRegistered = deviceRegistered
    }

    fun getResult(): String? {
        return result
    }

    fun setResult(result: String?) {
        this.result = result
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }

    fun getResponseCode(): String? {
        return responseCode
    }

    fun setResponseCode(responseCode: String?) {
        this.responseCode = responseCode
    }

    fun getDeviceSerialNo(): String? {
        return deviceSerialNo
    }

    fun setDeviceSerialNo(deviceSerialNo: String?) {
        this.deviceSerialNo = deviceSerialNo
    }

    fun getNotificationDetails(): NotificationDetails? {
        return notificationDetails
    }

    fun setNotificationDetails(notificationDetails: NotificationDetails?) {
        this.notificationDetails = notificationDetails
    }

    fun getEnvNotificationConnect(environment: String): String? {
        return if (environment.equals(
                "TEST",
                ignoreCase = true
            )
        ) "tcp://uat.notifypro.in:1883" else "ssl://notifypro.in:8883"
    }

    class NotificationDetails {
        @SerializedName("cryptoKey")
        @Expose
        var cryptoKey: String? = null

        @SerializedName("cryptoNonce")
        @Expose
        var cryptoNonce: String? = null

        @SerializedName("mqttQos")
        @Expose
        var mqttQos: String? = null

        @SerializedName("mqttRetainFlag")
        @Expose
        var mqttRetainFlag: String? = null

        @SerializedName("mqttCleanSession")
        @Expose
        var mqttCleanSession: String? = null

        @SerializedName("mqttUserName")
        @Expose
        var mqttUserName: String? = null

        @SerializedName("mqttClientId")
        @Expose
        var mqttClientId: String? = null

        @SerializedName("mqttSubscribeTopic")
        @Expose
        var mqttSubscribeTopic: String? = null
    }
}