package com.mosambee.notification_sdk.utils

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class WorkerClass(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
  
    override fun doWork(): Result {
  
        // Enter work details to perform on background
          // Log.d is used to for debugging purposes
        Log.d("WorkerClass","It's Working")
  
        // Task result
        return Result.success()
    }
}