package mni.siddique.battery_state_android.plugin

import mni.siddique.battery_state_android.settings.Settings

interface BatteryStatePlugin {
    suspend fun observeBatteryState(settingsString: String)
    suspend fun getSettings(): Settings
}