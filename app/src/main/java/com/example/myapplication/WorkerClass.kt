package com.example.myapplication

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters

class WorkerClass(appContext:Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    val context :Context = appContext
    override fun doWork(): Result {
        // Your work here.

        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context,"Running",Toast.LENGTH_SHORT).show()
        }

        // Your task result
        return Result.success()
    }

}