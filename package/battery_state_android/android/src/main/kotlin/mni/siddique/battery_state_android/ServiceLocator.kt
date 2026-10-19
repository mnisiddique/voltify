package mni.siddique.battery_state_android

import android.content.Context
import mni.siddique.battery_state_android.service.BatteryStateReceiver
import mni.siddique.battery_state_android.settings.SettingsSrc
import mni.siddique.battery_state_android.settings.dataStore
import mni.siddique.battery_state_android.usecase.BatteryStateObserver
import mni.siddique.battery_state_android.workmanager.BatteryLevelMonitoringTriggerEnqueuer
import mni.siddique.battery_state_android.workmanager.BatteryLevelMonitoringTriggerEnqueuerImpl
import androidx.work.WorkManager

object ServiceLocator {
    private var appContext: Context? = null
    private var settingsSrc: SettingsSrc? = null
    private var batteryStateObserver: BatteryStateObserver? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun getSettingsSrc(): SettingsSrc {
        val appContext = checkNotNull(appContext) {
            "ServiceLocator has not been initialized"
        }
        return settingsSrc ?: synchronized(this) {
            settingsSrc ?: SettingsSrc(appContext.dataStore).also {
                settingsSrc = it
            }
        }
    }

    fun getBatteryLevelMonitoringTriggerEnqueuer(): BatteryLevelMonitoringTriggerEnqueuer {
        val appContext = checkNotNull(appContext) {
            "ServiceLocator has not been initialized"
        }
        return BatteryLevelMonitoringTriggerEnqueuerImpl(WorkManager.getInstance(appContext), getSettingsSrc())
    }

    fun getBatteryStateReceiver(): BatteryStateReceiver {
        return BatteryStateReceiver(getBatteryStateObserver())
    }

    fun getBatteryStateObserver(): BatteryStateObserver {
        return batteryStateObserver ?: synchronized(this) {
            batteryStateObserver ?: BatteryStateObserver(getSettingsSrc()).also {
                batteryStateObserver = it
            }
        }
    }

    fun clear() {
        appContext = null
        settingsSrc = null
    }
}
