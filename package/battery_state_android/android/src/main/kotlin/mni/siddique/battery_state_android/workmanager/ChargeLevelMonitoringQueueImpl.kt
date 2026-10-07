package mni.siddique.battery_state_android.workmanager

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class ChargeLevelMonitoringQueueImpl(
    private val workManager: WorkManager
) : ChargeLevelMonitoringQueue {
    override fun enqueueChargeLevelMonitoring() {
        val constraints = Constraints.Builder().setRequiresCharging(true).build()
        val workRequest = OneTimeWorkRequestBuilder<ChargeLevelMonitoringWorker>()
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniqueWork(
            "ChargeLevelMonitoringWork",
            ExistingWorkPolicy.KEEP,
            workRequest
        )
    }
}