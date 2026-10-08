package mni.siddique.battery_state_android

import android.content.Context
import mni.siddique.battery_state_android.service.ChargeLevelReceiver
import mni.siddique.battery_state_android.settings.SettingsSrc
import mni.siddique.battery_state_android.settings.dataStore
import mni.siddique.battery_state_android.usecase.ChargeLevelObserver

object ServiceLocator {


    private var appContext: Context? = null
    private var settingsSrc: SettingsSrc? = null
    private var chargeLevelObserver: ChargeLevelObserver? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun getSettingsRepo(): SettingsSrc {
        val appContext = checkNotNull(appContext) {
            "ServiceLocator has not been initialized"
        }
        return settingsSrc ?: synchronized(this) {
            settingsSrc ?: SettingsSrc(appContext.dataStore).also {
                settingsSrc = it
            }
        }
    }

    fun getChargeLevelReceiver(): ChargeLevelReceiver {
        return ChargeLevelReceiver(getChargeLevelObserver())
    }

    fun getChargeLevelObserver(): ChargeLevelObserver {
        return chargeLevelObserver ?: synchronized(this) {
            chargeLevelObserver ?: ChargeLevelObserver(getSettingsRepo()).also {
                chargeLevelObserver = it
            }
        }
    }

    fun clear() {
        appContext = null
        settingsSrc = null
    }
}
