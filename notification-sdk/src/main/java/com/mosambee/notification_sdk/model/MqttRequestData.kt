package com.mosambee.notification_sdk.model

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.mosambee.notification_sdk.enumeration.RequestAction

class MqttRequestData {

    private var deviceId: String? = null
    private var latitude: String? = null
    private var longitude: String? = null
    private var deviceRegistrationMode: String? = null
    private var requestAction: RequestAction? = null
    private var environment = ""

    fun getLatitude(): String? {
        return latitude
    }

    fun setLatitude(latitude: String?) {
        this.latitude = latitude
    }

    fun getLongitude(): String? {
        return longitude
    }

    fun setLongitude(longitude: String?) {
        this.longitude = longitude
    }

    fun getDeviceId(): String? {
        return deviceId
    }

    fun setDeviceId(deviceId: String?) {
        this.deviceId = deviceId
    }

   /* fun getAppVersion2(context: Context): String? {
        return try {
            val pi = context.packageManager.getPackageInfo(context.packageName, 0)
            pi.versionName

        } catch (e: PackageManager.NameNotFoundException) {
            "NA"
        }
    }*/


    @Suppress("DEPRECATION")
    fun getAppVersion(context: Context): String? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val pi = context.packageManager.getPackageInfo(
                    context.packageManager.toString(),
                    PackageManager.PackageInfoFlags.of(0)
                )
                pi.versionName
            } else {
                val pi = context.packageManager.getPackageInfo(context.packageManager.toString(), 0)
                pi.versionName
            }
        } catch (e: PackageManager.NameNotFoundException) {
            "NA"
        }
    }


    fun setDeviceRegistrationMode(deviceRegistrationMode: String?) {
        this.deviceRegistrationMode = deviceRegistrationMode
    }

    fun getDeviceRegistrationMode(): String? {
        return deviceRegistrationMode
    }

    fun getDeviceMake(): String? {
        return Build.MANUFACTURER
    }

    fun getRequestAction(): RequestAction? {
        return requestAction
    }

    fun setRequestAction(requestAction: RequestAction?) {
        this.requestAction = requestAction
    }

    fun getEnvironment(): String {
        return environment
    }

    fun setEnvironment(environment: String) {
        this.environment = environment
    }
}