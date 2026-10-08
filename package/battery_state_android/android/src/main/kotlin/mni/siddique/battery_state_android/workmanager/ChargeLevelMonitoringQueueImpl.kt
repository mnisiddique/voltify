package mni.siddique.battery_state_android.workmanager

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class ChargeLevelMonitoringQueueImpl(
    private val workManager: WorkManager
) : ChargeLevelMonitoringQueue {
    // Monitoring is one-time work with a "requires charging" constraint: it runs when the device
    // starts charging, starts ChargeLevelMonitoringService, and finishes. The service queues the
    // next request when the charger is unplugged (ChargeState.Disconnected), so monitoring
    // continues for every charge cycle. Re-enqueuing from the worker would loop, because the
    // device is still charging and the constraint is already met.
    // KEEP avoids duplicating a request that is already waiting; a finished one is replaced.
    // Turning the alert off calls cancelChargeLevelMonitoring().
    override fun enqueueChargeLevelMonitoring() {
        val constraints = Constraints.Builder().setRequiresCharging(true).build()
        val workRequest = OneTimeWorkRequestBuilder<ChargeLevelMonitoringWorker>()
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniqueWork(
            WORK_NAME,
            ExistingWorkPolicy.KEEP,
            workRequest
        )
    }

    override fun cancelChargeLevelMonitoring() {
        workManager.cancelUniqueWork(WORK_NAME)
    }

    private companion object {
        const val WORK_NAME = "ChargeLevelMonitoringWork"
    }
}