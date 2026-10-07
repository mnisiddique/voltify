package mni.siddique.battery_state_android.receiver

import mni.siddique.battery_state_android.settings.SettingsSrc

class BatteryStateObserver(private val settingsSrc: SettingsSrc) {

    fun isBatteryStateNotifiable(action: String?, level: Int?, scale: Int?){}
}