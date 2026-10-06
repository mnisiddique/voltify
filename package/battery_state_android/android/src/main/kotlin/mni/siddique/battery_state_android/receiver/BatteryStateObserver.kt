package mni.siddique.battery_state_android.receiver

import mni.siddique.battery_state_android.settings.SettingsRepo

class BatteryStateObserver(private val settingsRepo: SettingsRepo) {

    fun isBatteryStateNotifiable(action: String?, level: Int?, scale: Int?){}
}