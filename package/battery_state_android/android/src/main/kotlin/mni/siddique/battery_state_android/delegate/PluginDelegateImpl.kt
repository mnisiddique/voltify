package mni.siddique.battery_state_android.delegate

import mni.siddique.battery_state_android.Method
import mni.siddique.battery_state_android.settings.SettingsSrc

class PluginDelegateImpl(private val settingsSrc: SettingsSrc) : PluginDelegate {
    override suspend fun call(
        name: String,
        argument: String
    ): PluginReply {
        return when (name) {
            Method.OBSERVE_BATTERY_STATE -> {
                settingsSrc.saveSettings(argument)

                PluginReply.NotImplemented
            }

            Method.GET_SETTINGS -> {
                PluginReply.NotImplemented
            }

            else -> {
                PluginReply.NotImplemented
            }
        }
    }
}

/*
* BatteryStatePlugin
*   - triggerBatteryStateObservation
*   - getSettings
* */
