package com.mosambee.notification_sdk.callback_listener

import com.mosambee.notification_sdk.enumeration.ResultAction
import org.json.JSONObject

interface NotificationListener {
    fun onMessageArrived(response: JSONObject?)
    fun onFailed(resultAction: ResultAction?, message: String?)
    fun onSuccess(message: String?)
    fun onSoundPayingStatus(isComplete: Boolean)
}