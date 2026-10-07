package mni.siddique.battery_state_android

import android.content.Context
import mni.siddique.battery_state_android.settings.SettingsSrc
import mni.siddique.battery_state_android.settings.dataStore

object ServiceLocator {

    @Volatile
    private var settingsSrc: SettingsSrc? = null

    fun getSettingsRepo(context: Context): SettingsSrc {
        return settingsSrc ?: synchronized(this) {
            settingsSrc ?: SettingsSrc(context.applicationContext.dataStore).also {
                settingsSrc = it
            }
        }
    }
}
