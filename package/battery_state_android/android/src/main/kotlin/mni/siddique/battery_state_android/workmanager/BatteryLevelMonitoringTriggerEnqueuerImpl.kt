package mni.siddique.battery_state_android.workmanager

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class BatteryLevelMonitoringTriggerEnqueuerImpl(
    private val workManager: WorkManager
) : BatteryLevelMonitoringTriggerEnqueuer {

    override fun enqueueTrigger() {
        val constraints = Constraints.Builder().setRequiresCharging(true).build()
        val workRequest = OneTimeWorkRequestBuilder<BatteryLevelMonitoringTriggerWork>()
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

// START -> ENQUETRIGGER -> STARTED -> WORK DONE -> REPEAT FROM ENQUEING