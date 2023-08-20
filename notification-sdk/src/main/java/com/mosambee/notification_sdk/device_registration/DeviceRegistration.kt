package com.mosambee.notification_sdk.device_registration

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import com.mosambee.networkservice.networkcall.entity.ErrorCode
import com.mosambee.networkservice.networkcall.network.NetworkCallControllerT
import com.mosambee.networkservice.networkcall.network.listener.NetworkCallResponse
import com.mosambee.notification_sdk.callback_listener.NotificationListener
import com.mosambee.notification_sdk.enumeration.RequestAction
import com.mosambee.notification_sdk.enumeration.ResultAction
import com.mosambee.notification_sdk.model.DeviceRegistrationResponse
import com.mosambee.notification_sdk.model.MqttRequestData
import com.mosambee.notification_sdk.model.RegisterDevice
import com.mosambee.notification_sdk.model.RegisterDevice.LatlongDetail
import com.mosambee.notification_sdk.model.RegisterDevice.LocationDetail
import com.mosambee.notification_sdk.notification_connection.NotifificationConnection
import com.mosambee.notification_sdk.utils.AESEncryptDecrypt
import com.mosambee.notification_sdk.utils.AppConstant
import com.mosambee.notification_sdk.utils.HMACUtil
import com.mosambee.notification_sdk.utils.TRACE
import org.json.JSONObject

class DeviceRegistration : NetworkCallResponse {
    private var requestData: MqttRequestData? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        var context: Context? = null

        @SuppressLint("StaticFieldLeak")
        private var deviceRegistration: DeviceRegistration? = null
        private var notificationListener: NotificationListener? = null
        private var gson: Gson? = null
        fun getInstance(
            context: Context?, callback: NotificationListener?
        ): DeviceRegistration? {
            this.context = context
            this.notificationListener = callback
            if (deviceRegistration == null) {
                synchronized(DeviceRegistration::class.java) {
                    if (deviceRegistration == null) deviceRegistration = DeviceRegistration()
                    gson = Gson()
                }
            }
            return deviceRegistration
        }
    }

    fun deviceRegistration(requestData: MqttRequestData) {
        try {
            this.requestData = requestData
            val registerDevice = RegisterDevice()
            val locationDetail = LocationDetail()
            val latLongDetail = LatlongDetail()
            registerDevice.setDeviceSerialNo(requestData.getDeviceId())
            registerDevice.setDeviceCategory(registerDevice.getDeviceCategory())
            registerDevice.setDeviceRegistrationMode(requestData.getDeviceRegistrationMode())
            registerDevice.setServerApiVersion(registerDevice.getServerApiVersion())
            registerDevice.setDeviceMake(requestData.getDeviceMake())
            registerDevice.setDeviceModel(registerDevice.getDeviceModel())
            registerDevice.setDevicePlatform(registerDevice.getDevicePlatform())
            registerDevice.setDeviceAppVersion(requestData.getAppVersion(context!!))
            locationDetail.detailCategory = locationDetail.detailCategory
            latLongDetail.latitude =
                if (null != requestData.getLatitude() && requestData.getLatitude()!!
                        .isNotEmpty()
                ) requestData.getLatitude() else "0.0000000"
            latLongDetail.longitude =
                if (null != requestData.getLongitude() && requestData.getLongitude()!!.isNotEmpty()
                ) requestData.getLongitude() else "0.0000000"
            locationDetail.latlongDetail = latLongDetail
            registerDevice.setLocationDetail(locationDetail)
            val jsonStr = gson!!.toJson(registerDevice)
            val jsonObject = JSONObject(jsonStr)
            requestData.setRequestAction(RequestAction.DEVICE_REGISTRATION)
            val hmacValue = HMACUtil().calculateHMAC(AESEncryptDecrypt().getAESDecryptedString(AppConstant.SECRET_DATA.HMAC_KEY).toString(), jsonObject.toString())

            /*call network services */
            val headerData: MutableMap<String?, String?> = HashMap()
            headerData["apiClientRef"] = "MOSAMBEE"
            headerData["hmac"] = hmacValue
            NetworkCallControllerT.getInstance(context!!).startAPIService(
                this, registerDevice.getEnvDeviceRegistration(
                    requestData.getEnvironment()
                ), 12223.toString(), true, jsonObject, headerData
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onFailed(errorCode: ErrorCode) {
        Log.e("errorCode::::::::", errorCode.toString())
        notificationListener!!.onFailed(
            ResultAction.DEVICE_REGISTRATION_FAILED, "$errorCode - Device registration failed"
        )
    }

    override fun onSuccess(result: String, taskId: String) {
        if (requestData?.getRequestAction()?.equals(RequestAction.DEVICE_REGISTRATION) == true) {
            try {
                val registrationResponse = gson!!.fromJson(
                    result, DeviceRegistrationResponse::class.java
                )
                if ("Success".equals(registrationResponse.getResult(), ignoreCase = true)) {
                    registrationResponse.setDeviceRegistered(true)
                    registrationResponse.setNotificationURL(requestData!!.getEnvironment())
                    startNotificationConnection(registrationResponse)
                } else {
                    notificationListener!!.onFailed(
                        ResultAction.DEVICE_REGISTRATION_FAILED, registrationResponse.getMessage()
                    )
                }
            } catch (err: Exception) {
                notificationListener!!.onFailed(
                    ResultAction.DEVICE_REGISTRATION_FAILED, "Device registration failed"
                )
            }
        } else {
            notificationListener!!.onFailed(
                ResultAction.INVALID_REQUEST, AppConstant.SOMETHING_WENT_WRONG
            )
        }

    }

    private fun startNotificationConnection(registrationResponse: DeviceRegistrationResponse) {
        val notifificationConnection: NotifificationConnection =
            NotifificationConnection.getInstance()!!
        NotifificationConnection.buildMqttClient(
            context, notificationListener, registrationResponse
        )
        val mServiceIntent = Intent(context, notifificationConnection.javaClass)
        try {
            if (isMyServiceRunning(mServiceIntent.javaClass)) {
                context!!.stopService(mServiceIntent)
                Thread.sleep(1000)
                context!!.startService(mServiceIntent)
            } else {
                TRACE.d("Service__Running::::::::Start:" + isMyServiceRunning(mServiceIntent.javaClass))
                context!!.startService(mServiceIntent)
            }
        } catch (e: Exception) {
            TRACE.e(e)
        }
    }

    @Suppress("DEPRECATION")
    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = context!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                TRACE.d("Service status Running")
                return true
            }
        }
        TRACE.d("Service status:::::" + "Not running")
        return false
    }
}