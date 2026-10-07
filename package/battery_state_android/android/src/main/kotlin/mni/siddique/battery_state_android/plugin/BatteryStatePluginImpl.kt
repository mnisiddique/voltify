package mni.siddique.battery_state_android.plugin

import mni.siddique.battery_state_android.workmanager.ChargeLevelMonitoringQueue
import mni.siddique.battery_state_android.settings.Settings
import mni.siddique.battery_state_android.settings.SettingsSrc

class BatteryStatePluginImpl(
    private val queue: ChargeLevelMonitoringQueue,
    private val settingsSrc: SettingsSrc
) : BatteryStatePlugin {
    override suspend fun observeBatteryState(settingsString: String) {
        val settings = Settings.fromJson(settingsString)
        settingsSrc.saveSettings(settingsString)
        if(settings.isAlertOn){
            queue.enqueueChargeLevelMonitoring()
        }
    }

    override suspend fun getSettings(): Settings {
        return settingsSrc.getSettings()
    }
}