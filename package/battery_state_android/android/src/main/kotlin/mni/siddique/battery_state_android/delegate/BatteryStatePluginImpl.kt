package mni.siddique.battery_state_android.delegate

import mni.siddique.battery_state_android.workmanager.BatteryLevelMonitoringTriggerEnqueuer
import mni.siddique.battery_state_android.settings.Settings
import mni.siddique.battery_state_android.settings.SettingsSrc

class BatteryStatePluginImpl(
    private val queue: BatteryLevelMonitoringTriggerEnqueuer,
    private val settingsSrc: SettingsSrc
) : BatteryStatePlugin {
    override suspend fun observeBatteryState(settingsString: String) {
        val settings = Settings.fromJson(settingsString)
        settingsSrc.saveSettings(settingsString)
        if(settings.isAlertOn){
            queue.enqueueTrigger()
        } else {
            queue.cancelChargeLevelMonitoring()
        }
    }

    override suspend fun getSettings(): Settings {
        return settingsSrc.getSettings()
    }
}

/*
* One class that manages all usecase
* class per usecase
* class per usecase + combinator
* conceptually usecase is orchestrator
*
* let say, wehave following usecases
*
* EnqueueBatteryLevelMonitoringWork
*  - enqueue(settings) -> scope plugin class, Broadcast class, service class
*
* CheckBatteryLevelThreshold
*   - check(settings)
*
* SettingsSrc
*   - save(settings)
*   - getSettings()
*   - getCachedSettings()
*
* Settings is cached when saved
* Settings is cached when retrieved
* Problem: Insecurity of having null cached setting
* Solution introduces unnecessary usage of coroutine
*
* */