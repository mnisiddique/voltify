package mni.siddique.battery_state_android.delegate

import mni.siddique.battery_state_android.settings.Settings

interface BatteryStatePlugin {
    suspend fun observeBatteryState(settingsString: String)
    suspend fun getSettings(): Settings
}