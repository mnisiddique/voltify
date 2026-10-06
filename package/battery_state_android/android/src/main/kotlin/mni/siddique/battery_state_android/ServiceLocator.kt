package mni.siddique.battery_state_android

import android.content.Context
import mni.siddique.battery_state_android.settings.SettingsRepo
import mni.siddique.battery_state_android.settings.dataStore

object ServiceLocator {

    @Volatile
    private var settingsRepo: SettingsRepo? = null

    fun getSettingsRepo(context: Context): SettingsRepo {
        return settingsRepo ?: synchronized(this) {
            settingsRepo ?: SettingsRepo(context.applicationContext.dataStore).also {
                settingsRepo = it
            }
        }
    }
}
