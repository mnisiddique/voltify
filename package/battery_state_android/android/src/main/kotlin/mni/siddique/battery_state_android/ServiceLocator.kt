package mni.siddique.battery_state_android

import android.content.Context
import mni.siddique.battery_state_android.service.ChargeLevelReceiver
import mni.siddique.battery_state_android.settings.SettingsSrc
import mni.siddique.battery_state_android.settings.dataStore
import mni.siddique.battery_state_android.usecase.ChargeLevelObserver

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

    fun getChargeLevelReceiver(context: Context): ChargeLevelReceiver {
        return ChargeLevelReceiver(getChargeLevelObserver(context))
    }

    fun getChargeLevelObserver(context: Context): ChargeLevelObserver {
        return ChargeLevelObserver(getSettingsRepo(context))
    }
}
