package mni.siddique.battery_state_android.usecase.schedule

import mni.siddique.battery_state_android.settings.SettingsSrc
import mni.siddique.battery_state_android.workmanager.PowerConnectionSchedule

class SchedulePowerConnectionUseCase(
    private val settingsSrc: SettingsSrc,
    private val powerConnectionSchedule: PowerConnectionSchedule
) {
    fun schedule() {
        if (settingsSrc.getSettings().isAlertOn) {
            powerConnectionSchedule.schedule()
        }
    }

    fun cancel() {
        powerConnectionSchedule.cancel()
    }
}