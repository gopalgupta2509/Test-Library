package com.mosambee.notification_sdk.utils

import android.util.Log

class TRACE {

    companion object{
        private val AppName = "Notification_SDK"
        var isTesting = true

        fun i(string: String?) {
            if (isTesting) {
                Log.i(AppName, string!!)
            }
        }

        fun w(string: String?) {
            if (isTesting) {
                Log.e(AppName, string!!)
            }
        }

        fun e(exception: Exception) {
            if (isTesting) {
                Log.e(AppName, exception.toString())
            }
        }

        fun d(string: String?) {
            if (isTesting) {
                Log.d(AppName, string!!)
            }
        }

        fun a(num: Int) {
            if (isTesting) {
                Log.d(AppName, Integer.toString(num))
            }
        }
    }
}