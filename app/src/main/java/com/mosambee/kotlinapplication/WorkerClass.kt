package com.mosambee.kotlinapplication

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.Timer
import java.util.TimerTask


class WorkerClass(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    var counter = 0
    override fun doWork(): Result {

        // Enter work details to perform on background
        // Log.d is used to for debugging purposes
        Log.d("WorkerClass", "It's Working")
        startTimer();

        // Task result
        return Result.success()
    }

    fun startTimer() {
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                Log.i("Count", "=========  " + counter++)
            }
        }
        timer!!.schedule(timerTask, 2000, 2000) //
    }

}