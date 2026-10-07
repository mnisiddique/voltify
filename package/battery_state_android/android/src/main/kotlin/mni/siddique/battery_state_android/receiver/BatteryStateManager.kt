package mni.siddique.battery_state_android.receiver

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class BatteryStateManager(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }

    fun registerListener() {}

    fun unregisterListener() {}
}