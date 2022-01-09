package com.example.myapplication

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myConstraints = Constraints.Builder()
            .setRequiresCharging(true) //checks whether device should be charging for the WorkRequest to run
            .setRequiredNetworkType(NetworkType.CONNECTED) //checks whether device should have Network Connection
            .setRequiresBatteryNotLow(true) // checks whether device battery should have a specific level to run the work request
            .setRequiresStorageNotLow(true)
            .build()
        val yourWorkRequest =
            OneTimeWorkRequest.Builder(WorkerClass::class.java).setInitialDelay(1, TimeUnit.SECONDS)
                .setConstraints(myConstraints).build()
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(yourWorkRequest.id).observe(this,
            { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                    //Toast
                }
            })

        //Minimum time interval to run a periodic task is 15 mins

        val yourPeriodicWorkRequest =
            PeriodicWorkRequest.Builder(
                WorkerClass::class.java,
                15, // flex interval - worker will run somewhen within this period of time, but at the end of repeating interval
                TimeUnit.MINUTES
            )
                .setConstraints(myConstraints)
                .build()

        WorkManager.getInstance(this).enqueue(yourPeriodicWorkRequest)



        // Run in sequence
        WorkManager.getInstance(this).beginWith(yourWorkRequest)
            .then(yourWorkRequest)
            .enqueue()

        // Run in parallel
        WorkManager.getInstance(this)
            .beginWith(listOf(yourWorkRequest, yourWorkRequest, yourWorkRequest))
            .then(yourWorkRequest)
            .then(yourWorkRequest)
            .enqueue()
    }
}