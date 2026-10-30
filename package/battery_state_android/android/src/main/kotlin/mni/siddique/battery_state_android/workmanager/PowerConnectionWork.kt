package mni.siddique.battery_state_android.workmanager

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import mni.siddique.battery_state_android.service.BatteryStateService

class PowerConnectionWork(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val serviceIntent = Intent(applicationContext, BatteryStateService::class.java)
        ContextCompat.startForegroundService(applicationContext, serviceIntent)
        return Result.success()
    }
}