package mni.siddique.battery_state_android.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ChargeLevelMonitoringWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
//        val serviceIntent = Intent(applicationContext, BatteryMonitorService::class.java)
//        ContextCompat.startForegroundService(applicationContext, serviceIntent)
//        return Result.success()
        TODO("Not yet implemented")
    }
}