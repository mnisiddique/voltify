package mni.siddique.battery_state_android.workmanager

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import mni.siddique.battery_state_android.settings.SettingsSrc

class BatteryLevelMonitoringTriggerEnqueuerImpl(
    private val workManager: WorkManager,
    private val settingsSrc: SettingsSrc
) : BatteryLevelMonitoringTriggerEnqueuer {

    override fun enqueueTrigger() {
        if (settingsSrc.getSettings().isAlertOn) {
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
    }

    override fun dequeueTrigger() {
        workManager.cancelUniqueWork(WORK_NAME)
    }

    private companion object {
        const val WORK_NAME = "ChargeLevelMonitoringWork"
    }
}

// START -> ENQUETRIGGER -> STARTED -> WORK DONE -> REPEAT FROM ENQUEING