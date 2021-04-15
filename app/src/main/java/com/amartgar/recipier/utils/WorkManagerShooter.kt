package com.amartgar.recipier.utils

import android.content.Context
import androidx.work.*
import com.amartgar.recipier.R
import com.amartgar.recipier.data.model.notifications.NotifyWorker
import java.util.concurrent.TimeUnit

class WorkManagerShooter {

    private fun createConstrains() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
        .setRequiresCharging(false)
        .setRequiresBatteryNotLow(true)
        .build()

    private fun createWorkRequest() =
        PeriodicWorkRequestBuilder<NotifyWorker>(15, TimeUnit.MINUTES)
            .setConstraints(createConstrains())
            .build()

    fun startWork(context: Context) {
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                context.getString(R.string.notify_work),
                ExistingPeriodicWorkPolicy.KEEP,
                createWorkRequest()
            )
    }
}