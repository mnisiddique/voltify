package mni.siddique.battery_state_android.workmanager

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import mni.siddique.battery_state_android.ServiceLocator
import mni.siddique.battery_state_android.service.ChargeLevelMonitoringService

class ChargeLevelMonitoringWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        ServiceLocator.init(applicationContext)
        val serviceIntent = Intent(applicationContext, ChargeLevelMonitoringService::class.java)
        ContextCompat.startForegroundService(applicationContext, serviceIntent)
        return Result.success()
    }
}