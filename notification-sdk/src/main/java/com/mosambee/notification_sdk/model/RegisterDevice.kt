package com.mosambee.notification_sdk.model

import android.os.Build
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegisterDevice {

    @SerializedName("deviceSerialNo")
    @Expose
    private var deviceSerialNo: String? = null

    @SerializedName("deviceCategory")
    @Expose
    private var deviceCategory: String? = null

    @SerializedName("deviceRegistrationMode")
    @Expose
    private var deviceRegistrationMode: String? = null

    @SerializedName("serverApiVersion")
    @Expose
    private var serverApiVersion: Int? = null

    @SerializedName("deviceMake")
    @Expose
    private var deviceMake: String? = null

    @SerializedName("deviceModel")
    @Expose
    private var deviceModel: String? = null

    @SerializedName("devicePlatform")
    @Expose
    private var devicePlatform: String? = null

    @SerializedName("deviceAppVersion")
    @Expose
    private var deviceAppVersion: String? = null

    @SerializedName("locationDetail")
    @Expose
    private var locationDetail: LocationDetail? = null

    fun getDeviceCategory(): String {
        return if (isPosTerminal()) "0" else "1"
    }

    private fun isPosTerminal(): Boolean {
        val modenName = Build.MODEL
        val result: Boolean
        result =
            (modenName.startsWith("SPPF-4") || modenName.startsWith("SPPF 4") || modenName.startsWith(
                "SPPL-4"
            ) || modenName.startsWith("SPPL 4") || modenName.startsWith("MT6737N")
                    || modenName.startsWith("SPPF-3") || modenName.startsWith("SPPF 3") || modenName.startsWith(
                "SPPL-3"
            ) || modenName.startsWith("SPPL 3") || modenName.startsWith("Mosambee")
                    || modenName.startsWith("Qphone2 3") || modenName.startsWith("Qphone2 4") || modenName.startsWith(
                "MF919"
            ) || modenName.startsWith("X990") || modenName.startsWith("D20")
                    || modenName.startsWith("DX8000") || modenName.startsWith("L200") || modenName.startsWith(
                "P10"
            ) || modenName.startsWith("P12"))
        return result
    }

    fun getDeviceSerialNo(): String? {
        return deviceSerialNo
    }

    fun setDeviceSerialNo(deviceSerialNo: String?) {
        this.deviceSerialNo = deviceSerialNo
    }

    fun setDeviceCategory(deviceCategory: String?) {
        this.deviceCategory = deviceCategory
    }

    fun getDeviceRegistrationMode(): String? {
        return deviceRegistrationMode
    }

    fun setDeviceRegistrationMode(deviceRegistrationMode: String?) {
        this.deviceRegistrationMode = deviceRegistrationMode
    }

    fun getServerApiVersion(): Int? {
        return 0
    }

    fun setServerApiVersion(serverApiVersion: Int?) {
        this.serverApiVersion = serverApiVersion
    }

    fun getDeviceMake(): String? {
        return deviceMake
    }

    fun setDeviceMake(deviceMake: String?) {
        this.deviceMake = deviceMake
    }

    fun getDeviceModel(): String? {
        val model = Build.MODEL
        deviceModel = model.replace("[^a-zA-Z0-9]".toRegex(), "")
        return deviceModel
    }

    fun setDeviceModel(deviceModel: String?) {
        this.deviceModel = deviceModel
    }

    fun getDevicePlatform(): String? {
        return "Android"
    }

    fun setDevicePlatform(devicePlatform: String?) {
        this.devicePlatform = devicePlatform
    }

    fun getDeviceAppVersion(): String? {
        return deviceAppVersion
    }

    fun setDeviceAppVersion(deviceAppVersion: String?) {
        this.deviceAppVersion = deviceAppVersion
    }

    fun getLocationDetail(): LocationDetail? {
        return locationDetail
    }

    fun setLocationDetail(locationDetail: LocationDetail?) {
        this.locationDetail = locationDetail
    }


    fun getEnvDeviceRegistration(environment: String): String? {
        return if (environment.equals(
                "TEST",
                ignoreCase = true
            )
        ) "https://uat.notifypro.in/api/device/v1/terminal/registration" else "https://notifypro.in/api/device/v1/terminal/registration"
    }


    class LatlongDetail {
        @SerializedName("latitude")
        @Expose
        var latitude: String? = null

        @SerializedName("longitude")
        @Expose
        var longitude: String? = null
    }

    class LocationDetail {
        @SerializedName("detailCategory")
        @Expose
        var detailCategory: String? = null
            get() = "0"

        @SerializedName("latlongDetail")
        @Expose
        var latlongDetail: LatlongDetail? = null
    }
}